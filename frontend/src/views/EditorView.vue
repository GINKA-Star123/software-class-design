<template>
  <div v-if="storyId" class="page editor-page" :class="'editor-theme-' + editorTheme">
    <PageTitle title="故事编辑器" :subtitle="'故事 ID：' + storyId" />

    <section class="hero card">
      <div class="hero-copy">
        <div class="kicker">DECISION TREE WORKSHOP</div>
        <h2>{{ meta.title || '未命名故事' }}</h2>
        <p>{{ meta.intro || '先写下故事简介，再搭建节点与选项。' }}</p>
        <div class="chips">
          <span class="chip">{{ nodes.length }} 个节点</span>
          <span class="chip">{{ endingCount }} 个结局</span>
          <span class="chip" :class="{ ok: startNode }">{{ startNode ? '已有起点' : '缺起始节点' }}</span>
          <span class="chip" :class="{ ok: endingCount }">{{ endingCount ? '结构可玩' : '待补结局' }}</span>
        </div>
      </div>
      <div class="hero-status">
        <div class="save-pill" :class="statusClass">
          <i></i>
          <span>{{ statusText }}</span>
        </div>
        <div class="hero-cover">
          <img v-if="meta.coverUrl" :src="meta.coverUrl" alt="故事封面" />
          <span v-else>上传封面后，这里会作为详情页和游玩页的氛围背景</span>
        </div>
      </div>
    </section>

    <el-alert
      type="info"
      :closable="false"
      class="help"
      title="最小可玩结构"
      description="故事至少要：① 1 个起始节点；② 至少 1 个结局节点；③ 从起始节点（或后续节点）通过「选项」能连到结局。不会搭？直接点「一键补全最小可玩结构」。"
    />

    <section class="meta card">
      <div class="cover-col">
        <img v-if="meta.coverUrl" :src="meta.coverUrl" class="cover-preview" alt="封面" />
        <div v-else class="cover-preview empty">暂无封面</div>
        <el-upload :show-file-list="false" accept="image/*" :http-request="doUpload">
          <el-button size="small" type="primary">{{ meta.coverUrl ? '更换封面' : '上传封面' }}</el-button>
        </el-upload>
        <p class="cover-hint">封面会作为玩家看到的氛围背景</p>
      </div>

      <el-form label-width="70px" class="meta-form">
        <el-form-item label="标题">
          <el-input v-model="meta.title" placeholder="给故事起一个名字" @input="onMetaChange" />
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="meta.category" placeholder="如：古风/悬疑" @input="onMetaChange" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="meta.intro" type="textarea" :rows="3" placeholder="一句话介绍故事背景" @input="onMetaChange" />
        </el-form-item>
        <div class="meta-actions">
          <el-button type="primary" :loading="metaSaving" @click="saveMeta">立即保存封面与信息</el-button>
          <span class="muted">输入停止约 1 秒后自动保存</span>
        </div>
      </el-form>

      <div class="theme-col">
        <p class="block-title">编辑区背景</p>
        <div class="theme-grid">
          <button
            v-for="t in THEMES"
            :key="t.value"
            class="theme-card"
            :class="{ active: editorTheme === t.value }"
            :style="{ background: t.preview }"
            :title="t.label"
            @click="setTheme(t.value)"
          >
            {{ t.label }}
          </button>
        </div>
        <p class="theme-tip">这里只调整编辑工作区；全站背景请在顶部“外观”中设置。</p>
      </div>
    </section>

    <div class="layout">
      <aside class="card panel tree-panel">
        <NodeTreeComponent
          :nodes="nodes"
          :current="current?.nodeId"
          :warn-ids="warnIds"
          @select="select"
          @add="addNode"
        />
      </aside>

      <main class="card panel editor-panel">
        <div class="panel-head">
          <div>
            <h3>{{ current ? '正在编辑 #' + current.nodeId : '节点编辑' }}</h3>
            <p>{{ current ? nodeRole(current) : '从左侧选择一个节点开始' }}</p>
          </div>
          <div class="panel-tags">
            <el-tag v-if="current && current.isStart === 1" type="success" effect="light">起始</el-tag>
            <el-tag v-if="current && current.isEnding === 1" type="danger" effect="light">结局</el-tag>
            <el-tag v-if="current" type="info" effect="plain">选项 {{ choices.length }}</el-tag>
          </div>
        </div>

        <template v-if="current">
          <NodeEditor
            :node="current"
            :can-delete="current.isStart !== 1"
            @save="saveNode"
            @remove="removeNode"
            @change="onNodeChange"
            @flush="flushNode"
          />
          <el-divider />
          <ChoiceEditor
            :choices="choices"
            :targets="nodes"
            @add="addChoice"
            @save="saveChoice"
            @delete="deleteChoice"
            @change="onChoiceChange"
            @flush="onChoiceFlush"
          />
        </template>
        <EmptyState v-else text="选择或新增节点开始编辑" />

        <div class="ops">
          <el-button type="success" :loading="fixing" @click="ensureMinimal">一键补全最小可玩结构</el-button>
          <el-button type="warning" @click="doValidate">完整性校验</el-button>
          <el-button type="primary" @click="doSubmit">提交审核</el-button>
          <el-button @click="saveAllNow">立即保存全部</el-button>
        </div>
      </main>

      <aside class="card panel side-panel">
        <div class="side-block">
          <p class="block-title">创作助手</p>
          <div class="status-box" :class="statusClass">
            <i></i>
            <span>{{ statusText }}</span>
          </div>
          <p class="status-sub">节点、选项和故事信息都会自动保存，不需要频繁点按钮。</p>
        </div>
        <div class="side-block">
          <p class="block-title">结构概览</p>
          <ul class="stat-list">
            <li><span>节点总数</span><b>{{ nodes.length }}</b></li>
            <li><span>结局数量</span><b>{{ endingCount }}</b></li>
            <li><span>当前选项</span><b>{{ choices.length }}</b></li>
            <li><span>起始节点</span><b>{{ startNode ? '已设置' : '待设置' }}</b></li>
          </ul>
        </div>
        <div class="side-block">
          <p class="block-title">创作提示</p>
          <ul class="tips">
            <li>先补全“起始 → 结局”，再做完整性校验。</li>
            <li>条件表达式可用 ach:成就编码、end:结局标题，支持 &amp;&amp; / ||。</li>
            <li>上传封面后，详情页和游玩页会自动使用它作为氛围背景。</li>
          </ul>
        </div>
      </aside>
    </div>

    <el-dialog v-model="vdlg" title="校验结果" width="520px">
      <template v-if="vresult">
        <el-alert v-if="vresult.valid" type="success" title="校验通过" :closable="false" />
        <template v-else>
          <el-alert type="error" :closable="false" title="发现以下问题（点编号可跳转到对应节点）" />
          <div v-for="(issue, ix) in vresult.issues" :key="ix" class="issue">
            <span>{{ issue }}</span>
            <el-button v-for="id in idsIn(issue)" :key="id" size="small" type="danger" text @click="jumpTo(id)">跳转 #{{ id }}</el-button>
          </div>
          <div class="tip">如果没有编号的提示（如“缺少结局”），点上方「一键补全最小可玩结构」即可自动修复。</div>
        </template>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  listNodes,
  listChoices,
  addNode as apiAddNode,
  updateNode,
  deleteNode,
  addChoice as apiAddChoice,
  updateChoice,
  deleteChoice as apiDeleteChoice,
  validateStory,
  submitAudit,
  updateStoryMeta
} from '../api/editor'
import { getStory } from '../api/story'
import { uploadCover } from '../api/upload'
import { validateImageFile } from '../utils/imageUpload'
import PageTitle from '../components/common/PageTitle.vue'
import EmptyState from '../components/common/EmptyState.vue'
import NodeTreeComponent from '../components/editor/NodeTreeComponent.vue'
import NodeEditor from '../components/editor/NodeEditor.vue'
import ChoiceEditor from '../components/editor/ChoiceEditor.vue'

const route = useRoute()
const router = useRouter()
const storyId = route.params.id

const nodes = ref([])
const current = ref(null)
const choices = ref([])
const vdlg = ref(false)
const vresult = ref(null)
const fixing = ref(false)
const metaSaving = ref(false)
const storyStatus = ref(0)
const meta = reactive({ title: '', intro: '', category: '', coverUrl: '' })

const THEMES = [
  { value: 'paper', label: '宣纸', preview: 'linear-gradient(135deg,#fffdf7,#eef4fb)' },
  { value: 'mist', label: '远山', preview: 'linear-gradient(135deg,#eef7fb,#e5edf9)' },
  { value: 'dusk', label: '暮色', preview: 'linear-gradient(135deg,#f4f0fb,#e9eefb)' },
  { value: 'warm', label: '暖阳', preview: 'linear-gradient(135deg,#fff7ec,#f8efe6)' }
]

function readTheme() {
  try {
    return localStorage.getItem('sw-editor-theme') || 'paper'
  } catch (e) {
    return 'paper'
  }
}

const editorTheme = ref(readTheme())
const saveStatus = ref('idle')
const saveMessage = ref('')
const lastSavedAt = ref('')
const nodeTimer = ref(null)
const metaTimer = ref(null)
const choiceTimers = new Map()
const pendingNode = ref(null)
const pendingMeta = ref(null)
const pendingChoices = new Map()
const committedCondition = new Map()

const endingCount = computed(() => nodes.value.filter((n) => n.isEnding === 1).length)
const startNode = computed(() => nodes.value.find((n) => n.isStart === 1))
const editable = computed(() => storyStatus.value === 0 || storyStatus.value === 3)
const statusClass = computed(() => ({ [saveStatus.value]: true }))
const statusText = computed(() => {
  if (saveStatus.value === 'saving') return '自动保存中…'
  if (saveStatus.value === 'saved') return `已自动保存 ${lastSavedAt.value}`
  if (saveStatus.value === 'dirty') return saveMessage.value || '有改动待保存'
  if (saveStatus.value === 'error') return saveMessage.value || '自动保存失败'
  return '编辑内容会自动保存'
})

function setTheme(value) {
  editorTheme.value = value
  try {
    localStorage.setItem('sw-editor-theme', value)
  } catch (e) {
    // 浏览器禁用本地存储时忽略即可
  }
}

function markDirty(message = '') {
  saveStatus.value = 'dirty'
  saveMessage.value = message
}

function markSaving() {
  saveStatus.value = 'saving'
  saveMessage.value = ''
}

function markSaved() {
  saveStatus.value = 'saved'
  saveMessage.value = ''
  lastSavedAt.value = new Date().toLocaleTimeString('zh-CN', { hour12: false })
}

function markError(message) {
  saveStatus.value = 'error'
  saveMessage.value = message || '自动保存失败，请检查网络'
}

function isConditionValid(expr) {
  const text = String(expr || '').trim()
  if (!text) return true
  const atom = '(?:ach:[A-Za-z0-9_]+|end:[^&|()]+|true|false)'
  const re = new RegExp(`^${atom}(?:\\s*(?:&&|\\|\\|)\\s*${atom})*$`)
  return re.test(text)
}

const warnIds = computed(() => {
  const set = new Set()
  for (const issue of vresult.value?.issues || []) for (const id of idsIn(issue)) set.add(id)
  return [...set]
})

function idsIn(text) {
  return (String(text).match(/\d+/g) || []).map(Number)
}
function nodeById(id) {
  return nodes.value.find((x) => Number(x.nodeId) === Number(id))
}
function nodeRole(node) {
  if (node.isStart === 1) return '起始节点 · 从玩家进入故事的第一步开始'
  if (node.isEnding === 1) return '结局节点 · 玩家抵达后会看到结局'
  return '普通剧情节点'
}

async function loadMeta() {
  try {
    const d = await getStory(storyId)
    Object.assign(meta, {
      title: d.title || '',
      intro: d.intro || '',
      category: d.category || '',
      coverUrl: d.coverUrl || ''
    })
    storyStatus.value = Number(d.status ?? 0)
  } catch (e) {
    // 保持空表单，不阻断编辑器加载
  }
}

async function doUpload({ file }) {
  const check = validateImageFile(file)
  if (!check.ok) {
    ElMessage.warning(check.message)
    return
  }
  try {
    const res = await uploadCover(file)
    meta.coverUrl = res.url
    onMetaChange()
    ElMessage.success('封面上传成功，稍后会自动保存')
  } catch (e) {
    // 上传失败时 request 拦截器已提示
  }
}

function onMetaChange() {
  if (!editable.value) return markDirty('当前故事状态不可编辑，自动保存已暂停')
  pendingMeta.value = { ...meta }
  markDirty()
  if (metaTimer.value) clearTimeout(metaTimer.value)
  metaTimer.value = setTimeout(saveMetaNow, 1000)
}

async function saveMetaNow() {
  const data = pendingMeta.value
  if (!data) return true
  if (!editable.value) {
    pendingMeta.value = null
    markDirty('当前故事状态不可编辑，自动保存已暂停')
    return false
  }
  if (!data.title.trim()) {
    markDirty('标题不能为空，暂不自动保存')
    return false
  }
  pendingMeta.value = null
  markSaving()
  try {
    await updateStoryMeta(storyId, data)
    markSaved()
    return true
  } catch (e) {
    pendingMeta.value = data
    markError(e?.message || '故事信息自动保存失败')
    return false
  }
}

async function saveMeta() {
  if (!editable.value) return ElMessage.warning('当前故事状态不可编辑')
  if (!meta.title.trim()) return ElMessage.warning('标题不能为空')
  if (metaTimer.value) {
    clearTimeout(metaTimer.value)
    metaTimer.value = null
  }
  pendingMeta.value = null
  metaSaving.value = true
  markSaving()
  try {
    await updateStoryMeta(storyId, { ...meta })
    markSaved()
    ElMessage.success('已保存')
  } catch (e) {
    markError(e?.message || '保存失败')
  } finally {
    metaSaving.value = false
  }
}

async function loadNodes() {
  nodes.value = (await listNodes(storyId)) || []
  if (current.value) {
    const found = nodeById(current.value.nodeId)
    current.value = found || null
    if (!found && nodes.value.length) await select(nodes.value[0])
  } else if (nodes.value.length) {
    await select(nodes.value[0])
  }
}

async function select(n) {
  if (!n) return
  if (current.value && Number(current.value.nodeId) === Number(n.nodeId)) return
  await flushAllPending()
  current.value = n
  await loadChoices(n.nodeId)
}

async function loadChoices(nodeId) {
  const list = (await listChoices(nodeId)) || []
  choices.value = list
  committedCondition.clear()
  for (const ch of list) committedCondition.set(ch.choiceId, ch.conditionExpr || '')
}

async function jumpTo(id) {
  const n = nodeById(id)
  if (n) {
    await select(n)
    vdlg.value = false
  }
}

function onNodeChange(form) {
  if (!current.value) return
  if (!editable.value) return markDirty('当前故事状态不可编辑，自动保存已暂停')
  pendingNode.value = { nodeId: current.value.nodeId, form: { ...form } }
  markDirty()
  if (nodeTimer.value) clearTimeout(nodeTimer.value)
  nodeTimer.value = setTimeout(saveNodeNow, 800)
}

async function persistNode(nodeId, form) {
  const node = nodeById(nodeId)
  await updateNode(nodeId, {
    nodeText: form.nodeText,
    isEnding: form.isEnding,
    endingTitle: form.endingTitle,
    sortOrder: form.sortOrder ?? node?.sortOrder ?? 0
  })
  if (node) {
    Object.assign(node, {
      nodeText: form.nodeText,
      isEnding: form.isEnding,
      endingTitle: form.endingTitle,
      sortOrder: form.sortOrder ?? node.sortOrder
    })
  }
}

async function saveNodeNow() {
  const payload = pendingNode.value
  if (!payload) return true
  if (!editable.value) {
    pendingNode.value = null
    markDirty('当前故事状态不可编辑，自动保存已暂停')
    return false
  }
  if (!String(payload.form.nodeText || '').trim()) {
    markDirty('节点文本为空，暂不自动保存')
    return false
  }
  pendingNode.value = null
  markSaving()
  try {
    await persistNode(payload.nodeId, payload.form)
    markSaved()
    return true
  } catch (e) {
    pendingNode.value = payload
    markError(e?.message || '节点自动保存失败')
    return false
  }
}

async function flushNode() {
  if (nodeTimer.value) {
    clearTimeout(nodeTimer.value)
    nodeTimer.value = null
  }
  await saveNodeNow()
}

async function saveNode(form) {
  if (!editable.value) return ElMessage.warning('当前故事状态不可编辑')
  if (!form.nodeText.trim()) return ElMessage.warning('节点文本不能为空')
  if (nodeTimer.value) {
    clearTimeout(nodeTimer.value)
    nodeTimer.value = null
  }
  pendingNode.value = null
  markSaving()
  try {
    await persistNode(current.value.nodeId, form)
    markSaved()
    ElMessage.success('已保存')
  } catch (e) {
    markError(e?.message || '节点保存失败')
  }
}

function onChoiceChange(ch) {
  if (!ch || ch.choiceId == null) return
  if (!editable.value) return markDirty('当前故事状态不可编辑，自动保存已暂停')
  const data = {
    toNodeId: ch.toNodeId,
    choiceText: ch.choiceText,
    conditionExpr: ch.conditionExpr || '',
    sortOrder: ch.sortOrder || 0
  }
  pendingChoices.set(ch.choiceId, { choiceId: ch.choiceId, data })
  markDirty(isConditionSatisfiedForSave(data.conditionExpr) ? '' : '条件表达式未完成，先暂存选项文本')
  const old = choiceTimers.get(ch.choiceId)
  if (old) clearTimeout(old)
  choiceTimers.set(ch.choiceId, setTimeout(() => saveChoiceNow(ch.choiceId), 900))
}

function isConditionSatisfiedForSave(expr) {
  return !String(expr || '').trim() || isConditionValid(expr)
}

async function saveChoiceNow(choiceId) {
  const payload = pendingChoices.get(choiceId)
  if (!payload) return true
  if (!editable.value) {
    pendingChoices.delete(choiceId)
    markDirty('当前故事状态不可编辑，自动保存已暂停')
    return false
  }
  if (!String(payload.data.choiceText || '').trim()) {
    markDirty('选项文本为空，暂不自动保存')
    return false
  }

  const old = choiceTimers.get(choiceId)
  if (old) clearTimeout(old)
  choiceTimers.delete(choiceId)
  pendingChoices.delete(choiceId)

  const conditionValid = isConditionValid(payload.data.conditionExpr)
  const conditionExpr = conditionValid ? payload.data.conditionExpr : (committedCondition.get(choiceId) || '')
  markSaving()
  try {
    await updateChoice(choiceId, { ...payload.data, conditionExpr })
    if (conditionValid) {
      committedCondition.set(choiceId, conditionExpr)
      markSaved()
    } else {
      markDirty('条件表达式暂未保存，补全后会自动保存')
    }
    return true
  } catch (e) {
    pendingChoices.set(choiceId, payload)
    markError(e?.message || '选项自动保存失败')
    return false
  }
}

async function onChoiceFlush(ch) {
  if (!ch || ch.choiceId == null) return
  await saveChoiceNow(ch.choiceId)
}

async function saveChoice(ch) {
  if (!editable.value) return ElMessage.warning('当前故事状态不可编辑')
  if (!ch.choiceText.trim()) return ElMessage.warning('选项文本不能为空')
  const old = choiceTimers.get(ch.choiceId)
  if (old) clearTimeout(old)
  choiceTimers.delete(ch.choiceId)
  pendingChoices.delete(ch.choiceId)
  markSaving()
  try {
    await updateChoice(ch.choiceId, {
      toNodeId: ch.toNodeId,
      choiceText: ch.choiceText,
      conditionExpr: ch.conditionExpr,
      sortOrder: ch.sortOrder || 0
    })
    committedCondition.set(ch.choiceId, ch.conditionExpr || '')
    markSaved()
    ElMessage.success('选项已保存')
  } catch (e) {
    markError(e?.message || '选项保存失败')
  }
}

async function flushAllPending() {
  if (nodeTimer.value) {
    clearTimeout(nodeTimer.value)
    nodeTimer.value = null
  }
  if (metaTimer.value) {
    clearTimeout(metaTimer.value)
    metaTimer.value = null
  }
  for (const timer of choiceTimers.values()) clearTimeout(timer)
  choiceTimers.clear()

  const jobs = []
  if (pendingMeta.value) jobs.push(saveMetaNow())
  if (pendingNode.value) jobs.push(saveNodeNow())
  for (const id of [...pendingChoices.keys()]) jobs.push(saveChoiceNow(id))
  await Promise.allSettled(jobs)
}

async function saveAllNow() {
  await flushAllPending()
  if (saveStatus.value !== 'error') ElMessage.success('已保存全部改动')
}

async function addNode() {
  if (!editable.value) return ElMessage.warning('当前故事状态不可编辑')
  await flushAllPending()
  const nodeId = await apiAddNode(storyId, {
    nodeText: '（新节点：写下这里的剧情）',
    isStart: 0,
    isEnding: 0,
    sortOrder: nodes.value.length
  })
  await loadNodes()
  const n = nodeById(nodeId)
  if (n) await select(n)
}

async function removeNode() {
  if (!editable.value) return ElMessage.warning('当前故事状态不可编辑')
  await flushAllPending()
  await ElMessageBox.confirm('删除该节点会同步删除相关选项，确定？', '删除', { type: 'warning' })
  await deleteNode(current.value.nodeId)
  current.value = null
  choices.value = []
  await loadNodes()
}

async function addChoice() {
  if (!editable.value) return ElMessage.warning('当前故事状态不可编辑')
  await flushAllPending()
  const target = nodes.value.find((x) => x.nodeId !== current.value.nodeId) || current.value
  await apiAddChoice(current.value.nodeId, {
    toNodeId: target.nodeId,
    choiceText: '新选项',
    conditionExpr: '',
    sortOrder: choices.value.length
  })
  await loadChoices(current.value.nodeId)
}

async function deleteChoice(ch) {
  const old = choiceTimers.get(ch.choiceId)
  if (old) clearTimeout(old)
  choiceTimers.delete(ch.choiceId)
  pendingChoices.delete(ch.choiceId)
  await apiDeleteChoice(ch.choiceId)
  await loadChoices(current.value.nodeId)
}

async function ensureMinimal() {
  fixing.value = true
  try {
    await flushAllPending()
    await loadNodes()
    let start = nodes.value.find((x) => x.isStart === 1)
    if (!start) {
      const id = await apiAddNode(storyId, { nodeText: '（起点：写下开场剧情）', isStart: 1, isEnding: 0, sortOrder: 0 })
      await loadNodes()
      start = nodeById(id)
    }
    let endings = nodes.value.filter((x) => x.isEnding === 1)
    if (!endings.length) {
      const id = await apiAddNode(storyId, { nodeText: '（结局：你完成了这段旅程）', isStart: 0, isEnding: 1, endingTitle: '结局', sortOrder: nodes.value.length })
      await loadNodes()
      endings = nodes.value.filter((x) => x.isEnding === 1)
    }
    if (!start || !endings.length) throw new Error('无法创建起始/结局节点')
    const cs = await listChoices(start.nodeId)
    const linked = cs.find((c) => endings.some((e) => Number(e.nodeId) === Number(c.toNodeId)))
    if (!linked) {
      await apiAddChoice(start.nodeId, { toNodeId: endings[0].nodeId, choiceText: '继续', conditionExpr: '', sortOrder: cs.length })
    }
    await loadNodes()
    await select(nodeById(start.nodeId) || nodes.value[0])
    ElMessage.success('已补全：起始节点 → 结局节点已连通')
  } catch (e) {
    if (e?.message && !String(e.message).includes('cancel')) markError(e.message)
  } finally {
    fixing.value = false
  }
}

async function doValidate() {
  await flushAllPending()
  vresult.value = await validateStory(storyId)
  vdlg.value = true
}

async function doSubmit() {
  await flushAllPending()
  try {
    await submitAudit(storyId)
    ElMessage.success('已提交审核')
    router.push('/my-stories')
  } catch (e) {
    // 提交失败时 request 拦截器已提示
  }
}

onMounted(() => {
  loadMeta()
  loadNodes()
})

onBeforeUnmount(() => {
  if (nodeTimer.value) clearTimeout(nodeTimer.value)
  if (metaTimer.value) clearTimeout(metaTimer.value)
  for (const timer of choiceTimers.values()) clearTimeout(timer)
  flushAllPending()
})
</script>

<style scoped>
.editor-page {
  transition: background 0.3s ease;
  border-radius: 18px;
}
.editor-theme-paper {
  background: linear-gradient(180deg, #fbfaf7, #f6f8fc 320px);
}
.editor-theme-mist {
  background: linear-gradient(180deg, #f2f8fb, #f6f9fc 320px);
}
.editor-theme-dusk {
  background: linear-gradient(180deg, #f5f2fb, #f7f9fc 320px);
}
.editor-theme-warm {
  background: linear-gradient(180deg, #fff8f0, #f7f9fc 320px);
}

.hero {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  padding: 22px;
  margin-bottom: 14px;
  background: linear-gradient(135deg, #ffffff, #f2f7ff 62%, #eef4ff);
  border: 1px solid rgba(43, 108, 176, 0.12);
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.05);
}
.hero-copy {
  min-width: 0;
  flex: 1;
}
.kicker {
  font-size: 11px;
  letter-spacing: 0.18em;
  color: #7c8db5;
  margin-bottom: 6px;
}
.hero h2 {
  margin: 0 0 8px;
  font-size: 26px;
  color: #12213a;
}
.hero p {
  margin: 0;
  color: var(--text-secondary);
  line-height: 1.8;
}
.chips {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 14px;
}
.chip {
  padding: 4px 10px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.82);
  border: 1px solid #dbe7f5;
  font-size: 12px;
  color: #436182;
}
.chip.ok {
  background: #e8f7ee;
  border-color: #c6ecd4;
  color: #1f7a43;
}
.hero-status {
  width: 280px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  flex: none;
}
.save-pill {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 9px 12px;
  border-radius: 999px;
  font-size: 13px;
  background: #f1f5f9;
  color: #5b6b85;
}
.save-pill i,
.status-box i {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: currentColor;
  display: inline-block;
}
.save-pill.saved,
.status-box.saved {
  background: #edf9f1;
  color: #1f7a43;
}
.save-pill.saving,
.status-box.saving {
  background: #fff8e6;
  color: #9a6700;
}
.save-pill.error,
.status-box.error {
  background: #fef3f2;
  color: #b42318;
}
.save-pill.dirty,
.status-box.dirty {
  background: #f1f5f9;
  color: #5b6b85;
}
.hero-cover {
  flex: 1;
  min-height: 110px;
  border-radius: 12px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.72);
  border: 1px dashed #cbd8ea;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #7b8aa3;
  font-size: 12px;
  padding: 10px;
  text-align: center;
  line-height: 1.6;
}
.hero-cover img {
  width: 100%;
  height: 140px;
  object-fit: cover;
  border-radius: 8px;
  display: block;
}
.help {
  margin-bottom: 14px;
}
.meta {
  display: grid;
  grid-template-columns: 200px minmax(280px, 1fr) 220px;
  gap: 18px;
  margin-bottom: 14px;
  align-items: start;
}
.cover-col {
  width: 200px;
  text-align: center;
}
.cover-preview {
  width: 200px;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
  border: 1px solid var(--border);
  margin-bottom: 8px;
}
.cover-preview.empty {
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-muted);
  font-size: 13px;
  background: #f7fafc;
}
.cover-hint {
  margin: 8px 0 0;
  font-size: 12px;
  color: var(--text-muted);
  line-height: 1.6;
}
.meta-form {
  flex: 1;
  min-width: 280px;
}
.meta-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}
.theme-col {
  border-left: 1px solid var(--border);
  padding-left: 16px;
}
.block-title {
  margin: 0 0 8px;
  font-size: 13px;
  font-weight: 700;
  color: #33415c;
}
.theme-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px;
}
.theme-card {
  border: 2px solid transparent;
  border-radius: 10px;
  padding: 14px 8px;
  font-size: 12px;
  color: #31435e;
  cursor: pointer;
  text-align: center;
  min-height: 52px;
}
.theme-card.active {
  border-color: var(--brand);
  box-shadow: 0 0 0 3px rgba(43, 108, 176, 0.12);
}
.theme-tip {
  margin: 8px 0 0;
  font-size: 12px;
  color: var(--text-muted);
  line-height: 1.6;
}
.layout {
  display: grid;
  grid-template-columns: 240px minmax(0, 1fr) 260px;
  gap: 14px;
  align-items: start;
}
.panel {
  padding: 14px;
}
.tree-panel,
.side-panel {
  position: sticky;
  top: 14px;
}
.side-panel {
  display: flex;
  flex-direction: column;
  gap: 14px;
}
.editor-panel {
  min-width: 0;
}
.panel-head {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 10px;
  margin-bottom: 10px;
}
.panel-head h3 {
  margin: 0 0 4px;
  font-size: 17px;
  color: #12213a;
}
.panel-head p {
  margin: 0;
  font-size: 12px;
  color: var(--text-muted);
}
.panel-tags {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
  justify-content: flex-end;
}
.status-box {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px;
  border-radius: 9px;
  background: #f8fafc;
  font-size: 13px;
  color: #5b6b85;
}
.status-sub {
  margin: 8px 0 0;
  font-size: 12px;
  color: var(--text-muted);
  line-height: 1.6;
}
.side-block + .side-block {
  border-top: 1px dashed var(--border);
  padding-top: 12px;
}
.stat-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.stat-list li {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  color: var(--text-secondary);
}
.stat-list b {
  color: #12213a;
}
.tips {
  margin: 0;
  padding-left: 16px;
  color: var(--text-secondary);
  font-size: 12px;
  line-height: 1.8;
}
.ops {
  margin-top: 18px;
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}
.issue {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 0;
  border-bottom: 1px dashed var(--border);
}
.issue span {
  flex: 1;
}
.tip {
  margin-top: 10px;
  font-size: 13px;
  color: var(--text-muted);
}
@media (max-width: 1100px) {
  .layout {
    grid-template-columns: 220px minmax(0, 1fr);
  }
  .side-panel {
    grid-column: 1 / -1;
    position: static;
    display: grid;
    grid-template-columns: repeat(3, 1fr);
  }
}
@media (max-width: 860px) {
  .hero {
    flex-direction: column;
  }
  .hero-status {
    width: 100%;
  }
  .meta {
    grid-template-columns: 1fr;
  }
  .cover-col {
    width: 100%;
  }
  .theme-col {
    border-left: none;
    padding-left: 0;
    border-top: 1px solid var(--border);
    padding-top: 12px;
  }
  .layout {
    grid-template-columns: 1fr;
  }
  .tree-panel,
  .side-panel {
    position: static;
  }
  .side-panel {
    grid-template-columns: 1fr;
  }
}
</style>
