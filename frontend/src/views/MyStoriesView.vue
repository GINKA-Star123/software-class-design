<template>
  <div class="page">
    <PageTitle title="创作中心" subtitle="管理我的决策树故事" />
    <el-alert type="info" :closable="false" class="help" title="删除规则"
      description="草稿/已驳回可直接删除；待审核、已发布、已下架的故事需提交「删除申请」，由审核员或管理员同意后删除。" />
    <div class="head"><el-button type="primary" @click="openCreate">新建故事</el-button></div>
    <el-table :data="list" style="width:100%">
      <el-table-column prop="title" label="标题" min-width="150" />
      <el-table-column prop="intro" label="简介" min-width="190" show-overflow-tooltip />
      <el-table-column label="状态" width="100" align="center"><template #default="{ row }"><el-tag :type="STATUS_TAG[row.status]">{{ STATUS_TEXT[row.status] }}</el-tag></template></el-table-column>
      <el-table-column label="操作" width="300" align="center">
        <template #default="{ row }">
          <el-button size="small" type="primary" @click="$router.push('/editor/' + row.storyId)">编辑</el-button>
          <el-button v-if="row.status === 0 || row.status === 3" size="small" type="warning" @click="submit(row)">提交审核</el-button>
          <ConfirmDialog v-if="row.status === 0 || row.status === 3" simple message="确定删除该故事吗？" @confirm="remove(row)">
            <el-button size="small" type="danger">删除</el-button>
          </ConfirmDialog>
          <el-button v-else size="small" type="danger" plain @click="openApply(row)">申请删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <EmptyState v-if="!list.length" text="还没有故事，点击右上角新建" />

    <el-dialog v-model="dlg" title="新建故事" width="480px">
      <el-form label-position="top">
        <el-form-item label="标题" required><el-input v-model="form.title" maxlength="100" /></el-form-item>
        <el-form-item label="简介"><el-input v-model="form.intro" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="分类"><el-input v-model="form.category" placeholder="如：古风/悬疑/测试" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dlg=false">取消</el-button><el-button type="primary" :loading="busy" @click="create">创建并编辑</el-button></template>
    </el-dialog>

    <el-dialog v-model="adlg" title="申请删除故事" width="440px">
      <p>《{{ applyTarget?.title }}》当前状态：{{ STATUS_TEXT[applyTarget?.status] }}，提交申请后需审核员/管理员同意。</p>
      <el-input v-model="applyReason" type="textarea" :rows="3" placeholder="填写删除理由（可选）" />
      <template #footer><el-button @click="adlg=false">取消</el-button><el-button type="danger" :loading="applying" @click="apply">提交申请</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { myStories, createStory, deleteStory, submitAudit, addNode, requestDeleteStory } from '../api/editor'
import { STATUS_TEXT, STATUS_TAG } from '../utils/constants'
import PageTitle from '../components/common/PageTitle.vue'
import EmptyState from '../components/common/EmptyState.vue'
import ConfirmDialog from '../components/common/ConfirmDialog.vue'
const router = useRouter(); const list = ref([])
const dlg = ref(false); const busy = ref(false); const form = reactive({ title: '', intro: '', category: '' })
const adlg = ref(false); const applyTarget = ref(null); const applyReason = ref(''); const applying = ref(false)
async function load() { list.value = await myStories() }
function openCreate() { Object.assign(form, { title: '', intro: '', category: '' }); dlg.value = true }
async function create() {
  if (!form.title.trim()) return ElMessage.warning('请填写标题')
  busy.value = true
  try {
    const storyId = await createStory({ ...form })
    await addNode(storyId, { nodeText: '（起始节点：写下第一段剧情吧）', isStart: 1, isEnding: 0, sortOrder: 0 })
    dlg.value = false; router.push('/editor/' + storyId)
  } finally { busy.value = false }
}
async function submit(row) {
  await ElMessageBox.confirm('确定提交《' + row.title + '》进入审核吗？', '提交审核', { type: 'info' })
  await submitAudit(row.storyId); ElMessage.success('已提交'); load()
}
async function remove(row) { await deleteStory(row.storyId); ElMessage.success('已删除'); load() }
function openApply(row) { applyTarget.value = row; applyReason.value = ''; adlg.value = true }
async function apply() {
  applying.value = true
  try { await requestDeleteStory(applyTarget.value.storyId, applyReason.value); adlg.value = false; ElMessage.success('删除申请已提交'); load() }
  finally { applying.value = false }
}
onMounted(load)
</script>
<style scoped>.head { margin-bottom: 12px; } .help { margin-bottom: 12px; }</style>
