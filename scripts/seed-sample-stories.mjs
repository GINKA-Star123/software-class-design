import fs from 'node:fs'

const baseUrl = process.env.SITE_URL || 'http://47.76.83.128'
const sqlPath = new URL('../database/sample_stories.sql', import.meta.url)
const sql = fs.readFileSync(sqlPath, 'utf8')

let cookie = ''

async function request(method, path, body) {
  const headers = {}
  if (body !== undefined) headers['Content-Type'] = 'application/json'
  if (cookie) headers.Cookie = cookie

  const response = await fetch(baseUrl + path, {
    method,
    headers,
    body: body === undefined ? undefined : JSON.stringify(body)
  })

  const setCookies = typeof response.headers.getSetCookie === 'function'
    ? response.headers.getSetCookie()
    : (response.headers.get('set-cookie') ? [response.headers.get('set-cookie')] : [])
  if (setCookies.length) cookie = setCookies[0].split(';')[0]

  const json = await response.json()
  if (json.code !== 200) {
    throw new Error(`${method} ${path} 失败：${json.code} ${json.message}`)
  }
  return json.data
}

function parseSampleStories() {
  const stories = []
  const nodes = []
  const choices = []

  for (const line of sql.split(/\r?\n/)) {
    const storyMatch = line.match(/^\s*\((\d+),\s*(\d+),\s*'(.*)',\s*'(.*)',\s*'(.*)',\s*'(.*)',\s*(\d+),/)
    if (storyMatch) {
      stories.push({
        storyId: Number(storyMatch[1]),
        authorId: Number(storyMatch[2]),
        title: storyMatch[3],
        intro: storyMatch[4],
        category: storyMatch[5],
        coverUrl: storyMatch[6],
        status: Number(storyMatch[7])
      })
      continue
    }

    const nodeMatch = line.match(/^\s*\((\d+),\s*(\d+),\s*'(.*)',\s*(\d),\s*(\d),\s*(?:NULL|'(.*)'),\s*(\d+)\),?\s*$/)
    if (nodeMatch) {
      nodes.push({
        nodeId: Number(nodeMatch[1]),
        storyId: Number(nodeMatch[2]),
        nodeText: nodeMatch[3],
        isStart: Number(nodeMatch[4]),
        isEnding: Number(nodeMatch[5]),
        endingTitle: nodeMatch[6] || null,
        sortOrder: Number(nodeMatch[7])
      })
      continue
    }

    const choiceMatch = line.match(/^\s*\((\d+),\s*(\d+),\s*(\d+),\s*'(.*)',\s*(?:NULL|'(.*)'),\s*(\d+)\),?\s*$/)
    if (choiceMatch) {
      choices.push({
        choiceId: Number(choiceMatch[1]),
        fromNodeId: Number(choiceMatch[2]),
        toNodeId: Number(choiceMatch[3]),
        choiceText: choiceMatch[4],
        conditionExpr: choiceMatch[5] || null,
        sortOrder: Number(choiceMatch[6])
      })
    }
  }

  return { stories, nodes, choices }
}

async function main() {
  const { stories, nodes, choices } = parseSampleStories()
  if (process.env.DRY_RUN === '1') {
    console.log(`解析到 ${stories.length} 篇故事、${nodes.length} 个节点、${choices.length} 个选项`)
    for (const story of stories) {
      const storyNodes = nodes.filter((node) => node.storyId === story.storyId)
      const storyChoices = choices.filter((choice) => storyNodes.some((node) => node.nodeId === choice.fromNodeId))
      console.log(`- 《${story.title}》：${storyNodes.length} 节点 / ${storyChoices.length} 分支`)
    }
    return
  }
  await request('POST', '/api/auth/login', { username: 'official', password: '123456' })

  const mine = await request('GET', '/api/stories/mine')
  const existingTitles = new Set((mine || []).map((story) => story.title))

  for (const story of stories) {
    if (existingTitles.has(story.title)) {
      console.log(`SKIP 《${story.title}》已存在`)
      continue
    }

    const storyId = await request('POST', '/api/stories', {
      title: story.title,
      intro: story.intro,
      category: story.category,
      coverUrl: story.coverUrl
    })

    const storyNodes = nodes.filter((node) => node.storyId === story.storyId)
    const storyChoices = choices.filter((choice) => storyNodes.some((node) => node.nodeId === choice.fromNodeId))
    const idMap = new Map()

    for (const node of storyNodes) {
      const newNodeId = await request('POST', `/api/editor/stories/${storyId}/nodes`, {
        nodeText: node.nodeText,
        isStart: node.isStart,
        isEnding: node.isEnding,
        endingTitle: node.endingTitle,
        sortOrder: node.sortOrder
      })
      idMap.set(node.nodeId, newNodeId)
    }

    for (const choice of storyChoices) {
      await request('POST', `/api/editor/nodes/${idMap.get(choice.fromNodeId)}/choices`, {
        toNodeId: idMap.get(choice.toNodeId),
        choiceText: choice.choiceText,
        conditionExpr: choice.conditionExpr,
        sortOrder: choice.sortOrder
      })
    }

    const validation = await request('POST', `/api/stories/${storyId}/validate`)
    if (!validation?.valid) {
      throw new Error(`《${story.title}》校验失败：${(validation?.issues || []).join('；')}`)
    }

    await request('POST', `/api/stories/${storyId}/submit`)
    await request('POST', `/api/audit/stories/${storyId}/approve`)
    console.log(`OK 《${story.title}》已发布，故事 ID=${storyId}`)
  }
}

main().catch((error) => {
  console.error(error.message)
  process.exit(1)
})
