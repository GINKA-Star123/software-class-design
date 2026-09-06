<template>
  <div v-if="storyId" class="page">
    <PageTitle title="故事编辑器" :subtitle="'故事ID：' + storyId" />
    <el-alert type="info" :closable="false" class="help" title="最小可玩结构"
      description="故事至少要：① 1 个起始节点；② 至少 1 个结局节点；③ 从起始节点（或后续节点）通过「选项」能连到结局。不会搭？直接点「一键补全最小可玩结构」。" />
    <div class="card meta">
      <div class="cover-col">
        <img v-if="meta.coverUrl" :src="meta.coverUrl" class="cover-preview" alt="封面" />
        <div v-else class="cover-preview empty">暂无封面</div>
        <el-upload :show-file-list="false" accept="image/*" :http-request="doUpload">
          <el-button size="small" type="primary">{{ meta.coverUrl ? '更换封面' : '上传封面' }}</el-button>
        </el-upload>
      </div>
      <el-form label-width="70px" class="meta-form">
        <el-form-item label="标题"><el-input v-model="meta.title" /></el-form-item>
        <el-form-item label="分类"><el-input v-model="meta.category" placeholder="如：古风/悬疑" /></el-form-item>
        <el-form-item label="简介"><el-input v-model="meta.intro" type="textarea" :rows="2" /></el-form-item>
        <el-button type="primary" :loading="metaSaving" @click="saveMeta">保存封面与信息</el-button>
      </el-form>
    </div>
    <div class="layout">
      <aside class="card panel">
        <NodeTreeComponent :nodes="nodes" :current="current?.nodeId" :warn-ids="warnIds" @select="select" @add="addNode" />
      </aside>
      <main class="card panel grow">
        <template v-if="current">
          <NodeEditor :node="current" :can-delete="current.isStart !== 1" @save="saveNode" @remove="removeNode" />
          <el-divider />
          <ChoiceEditor :choices="choices" :targets="nodes" @add="addChoice" @save="saveChoice" @delete="deleteChoice" />
        </template>
        <EmptyState v-else text="选择或新增节点开始编辑" />
        <div class="ops">
          <el-button type="success" :loading="fixing" @click="ensureMinimal">一键补全最小可玩结构</el-button>
          <el-button type="warning" @click="doValidate">完整性校验</el-button>
          <el-button type="primary" @click="doSubmit">提交审核</el-button>
        </div>
      </main>
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
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listNodes, listChoices, addNode as apiAddNode, updateNode, deleteNode, addChoice as apiAddChoice, updateChoice, deleteChoice as apiDeleteChoice, validateStory, submitAudit } from '../api/editor'
import { reactive } from 'vue'
import { getStory } from '../api/story'
import { uploadCover } from '../api/upload'
import { updateStoryMeta } from '../api/editor'
import PageTitle from '../components/common/PageTitle.vue'
import EmptyState from '../components/common/EmptyState.vue'
import NodeTreeComponent from '../components/editor/NodeTreeComponent.vue'
import NodeEditor from '../components/editor/NodeEditor.vue'
import ChoiceEditor from '../components/editor/ChoiceEditor.vue'

const route = useRoute(); const router = useRouter()
const storyId = route.params.id
const nodes = ref([]); const current = ref(null); const choices = ref([])
const vdlg = ref(false); const vresult = ref(null); const fixing = ref(false)
const meta = reactive({ title: '', intro: '', category: '', coverUrl: '' })
const metaSaving = ref(false)

async function loadMeta() {
  try { const d = await getStory(storyId); Object.assign(meta, { title: d.title || '', intro: d.intro || '', category: d.category || '', coverUrl: d.coverUrl || '' }) } catch (e) {}
}
async function doUpload({ file }) {
  try { const res = await uploadCover(file); meta.coverUrl = res.url; ElMessage.success('封面上传成功') } catch (e) {}
}
async function saveMeta() {
  if (!meta.title.trim()) return ElMessage.warning('标题不能为空')
  metaSaving.value = true
  try { await updateStoryMeta(storyId, { ...meta }); ElMessage.success('已保存') } finally { metaSaving.value = false }
}

const warnIds = computed(() => {
  const set = new Set()
  for (const issue of vresult.value?.issues || []) for (const id of idsIn(issue)) set.add(id)
  return [...set]
})
function idsIn(text) { return (String(text).match(/\d+/g) || []).map(Number) }

async function loadNodes() { nodes.value = await listNodes(storyId) || []; if (!current.value && nodes.value.length) select(nodes.value[0]) }
async function select(n) { current.value = n; await loadChoices(n.nodeId) }
async function loadChoices(nodeId) { choices.value = await listChoices(nodeId) || [] }
function nodeById(id) { return nodes.value.find(x => Number(x.nodeId) === Number(id)) }
async function jumpTo(id) { const n = nodeById(id); if (n) { await select(n); vdlg.value = false } }

async function ensureMinimal() {
  fixing.value = true
  try {
    await loadNodes()
    let start = nodes.value.find(x => x.isStart === 1)
    if (!start) {
      const id = await apiAddNode(storyId, { nodeText: '（起点：写下开场剧情）', isStart: 1, isEnding: 0, sortOrder: 0 })
      await loadNodes(); start = nodeById(id)
    }
    let endings = nodes.value.filter(x => x.isEnding === 1)
    if (!endings.length) {
      const id = await apiAddNode(storyId, { nodeText: '（结局：你完成了这段旅程）', isStart: 0, isEnding: 1, endingTitle: '结局', sortOrder: nodes.value.length })
      await loadNodes(); endings = nodes.value.filter(x => x.isEnding === 1)
    }
    if (!start || !endings.length) throw new Error('无法创建起始/结局节点')
    const cs = await listChoices(start.nodeId)
    const linked = cs.find(c => endings.some(e => Number(e.nodeId) === Number(c.toNodeId)))
    if (!linked) {
      await apiAddChoice(start.nodeId, { toNodeId: endings[0].nodeId, choiceText: '继续', conditionExpr: '', sortOrder: cs.length })
    }
    await loadNodes(); await select(nodes.value.find(x => x.nodeId === start.nodeId) || nodes.value[0])
    ElMessage.success('已补全：起始节点 → 结局节点已连通')
  } finally { fixing.value = false }
}

async function saveNode(form) {
  if (!form.nodeText.trim()) return ElMessage.warning('节点文本不能为空')
  await updateNode(current.value.nodeId, form); ElMessage.success('已保存'); await loadNodes()
}
async function addNode() {
  const nodeId = await apiAddNode(storyId, { nodeText: '（新节点：双击编辑剧情）', isStart: 0, isEnding: 0, sortOrder: nodes.value.length })
  await loadNodes(); const n = nodeById(nodeId); if (n) select(n)
}
async function removeNode() {
  await ElMessageBox.confirm('删除该节点会同步删除相关选项，确定？', '删除', { type: 'warning' })
  await deleteNode(current.value.nodeId); current.value = null; choices.value = []; await loadNodes()
}
async function addChoice() {
  const target = nodes.value.find(x => x.nodeId !== current.value.nodeId) || current.value
  await apiAddChoice(current.value.nodeId, { toNodeId: target.nodeId, choiceText: '新选项', conditionExpr: '', sortOrder: choices.value.length })
  await loadChoices(current.value.nodeId)
}
async function saveChoice(ch) {
  if (!ch.choiceText.trim()) return ElMessage.warning('选项文本不能为空')
  await updateChoice(ch.choiceId, { toNodeId: ch.toNodeId, choiceText: ch.choiceText, conditionExpr: ch.conditionExpr, sortOrder: ch.sortOrder || 0 })
  ElMessage.success('选项已保存'); loadChoices(current.value.nodeId)
}
async function deleteChoice(ch) { await apiDeleteChoice(ch.choiceId); loadChoices(current.value.nodeId) }
async function doValidate() { vresult.value = await validateStory(storyId); vdlg.value = true }
async function doSubmit() {
  try { await submitAudit(storyId); ElMessage.success('已提交审核'); router.push('/my-stories') } catch (e) {}
}
onMounted(() => { loadMeta(); loadNodes() })
</script>
<style scoped>
.help { margin-bottom: 12px; }
.layout { display:flex; gap:14px; align-items:flex-start; }
.panel { padding:14px; }
.grow { flex:1; min-width:0; }
.meta { display:flex; gap:16px; margin-bottom:14px; flex-wrap:wrap; align-items:flex-start; }
.cover-col { width:200px; text-align:center; }
.cover-preview { width:200px; height:120px; object-fit:cover; border-radius:8px; border:1px solid var(--border); margin-bottom:8px; }
.cover-preview.empty { display:flex; align-items:center; justify-content:center; color:var(--text-muted); font-size:13px; background:#f7fafc; }
.meta-form { flex:1; min-width:280px; }
.ops { margin-top:18px; display:flex; gap:8px; flex-wrap:wrap; }
.issue { display:flex; align-items:center; gap:8px; padding:8px 0; border-bottom:1px dashed var(--border); }
.issue span { flex:1; }
.tip { margin-top:10px; font-size:13px; color:var(--text-muted); }
@media (max-width:900px){ .layout{flex-direction:column;} }
</style>
