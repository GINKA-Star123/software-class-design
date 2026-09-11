import fs from 'node:fs'

const sqlPath = new URL('../database/sample_stories.sql', import.meta.url)
const sql = fs.readFileSync(sqlPath, 'utf8')

const nodes = new Map()
const choices = []

for (const line of sql.split(/\r?\n/)) {
  const nodeMatch = line.match(/^\s*\((\d+),\s*(\d+),\s*'.*',\s*(\d),\s*(\d),\s*(?:NULL|'.*'),\s*(\d+)\),?\s*$/)
  if (nodeMatch) {
    const [, nodeId, storyId, isStart, isEnding, sortOrder] = nodeMatch
    nodes.set(Number(nodeId), {
      nodeId: Number(nodeId),
      storyId: Number(storyId),
      isStart: Number(isStart),
      isEnding: Number(isEnding),
      sortOrder: Number(sortOrder)
    })
    continue
  }

  const choiceMatch = line.match(/^\s*\((\d+),\s*(\d+),\s*(\d+),\s*'.*',\s*(?:NULL|'.*'),\s*(\d+)\),?\s*$/)
  if (choiceMatch) {
    const [, choiceId, fromNodeId, toNodeId, sortOrder] = choiceMatch
    choices.push({
      choiceId: Number(choiceId),
      fromNodeId: Number(fromNodeId),
      toNodeId: Number(toNodeId),
      sortOrder: Number(sortOrder)
    })
  }
}

if (!nodes.size || !choices.length) {
  throw new Error('未解析到示例故事节点或选项，请检查 SQL 格式')
}

const storyIds = [...new Set([...nodes.values()].map((node) => node.storyId))].sort((a, b) => a - b)
let failed = false

for (const storyId of storyIds) {
  const storyNodes = [...nodes.values()].filter((node) => node.storyId === storyId)
  const nodeIds = new Set(storyNodes.map((node) => node.nodeId))
  const start = storyNodes.find((node) => node.isStart === 1)
  const endings = storyNodes.filter((node) => node.isEnding === 1)
  const outgoing = new Map()

  for (const nodeId of nodeIds) outgoing.set(nodeId, [])
  for (const choice of choices) {
    if (!outgoing.has(choice.fromNodeId)) continue
    if (!nodeIds.has(choice.toNodeId)) {
      console.error(`FAIL story ${storyId}: choice ${choice.choiceId} 指向不存在的节点 ${choice.toNodeId}`)
      failed = true
    }
    outgoing.get(choice.fromNodeId).push(choice.toNodeId)
  }

  if (!start) {
    console.error(`FAIL story ${storyId}: 缺少起始节点`)
    failed = true
    continue
  }
  if (!endings.length) {
    console.error(`FAIL story ${storyId}: 缺少结局节点`)
    failed = true
    continue
  }

  const visited = new Set([start.nodeId])
  const queue = [start.nodeId]
  while (queue.length) {
    const current = queue.shift()
    for (const next of outgoing.get(current) || []) {
      if (!visited.has(next)) {
        visited.add(next)
        queue.push(next)
      }
    }
  }

  const unreachable = storyNodes.filter((node) => !visited.has(node.nodeId)).map((node) => node.nodeId)
  const reachableEndings = endings.filter((node) => visited.has(node.nodeId)).map((node) => node.nodeId)

  if (unreachable.length || !reachableEndings.length) {
    console.error(`FAIL story ${storyId}: 不可达节点=${unreachable.join(',') || '无'}，可达结局=${reachableEndings.join(',') || '无'}`)
    failed = true
  } else {
    console.log(`PASS story ${storyId}: ${storyNodes.length} 节点，${choices.filter((choice) => nodeIds.has(choice.fromNodeId)).length} 分支，${endings.length} 结局`)
  }
}

if (failed) process.exit(1)
