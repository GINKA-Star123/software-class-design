<template>
  <div class="page">
    <PageTitle title="审核中心" subtitle="审核故事提交与删除申请" />
    <h2 class="sec">待审核故事</h2>
    <AuditStoryTable :stories="list" @preview="preview" @approve="approve" @reject="openReject" />
    <EmptyState v-if="!list.length" text="暂无待审核故事" />
    <h2 class="sec">删除申请</h2>
    <el-table :data="dels" style="width:100%">
      <el-table-column prop="storyTitle" label="故事" min-width="150" />
      <el-table-column prop="requesterName" label="申请人" width="120" />
      <el-table-column prop="reason" label="理由" min-width="150" />
      <el-table-column prop="createTime" label="申请时间" width="180" />
      <el-table-column label="操作" width="220" align="center">
        <template #default="{ row }">
          <el-button size="small" type="danger" @click="agreeDelete(row)">同意删除</el-button>
          <el-button size="small" type="warning" @click="openRejectDel(row)">拒绝</el-button>
        </template>
      </el-table-column>
    </el-table>
    <EmptyState v-if="!dels.length" text="暂无删除申请" />

    <el-dialog v-model="rdlg" title="驳回故事" width="420px">
      <el-input v-model="reason" type="textarea" :rows="3" placeholder="填写修改意见" />
      <template #footer><el-button @click="rdlg=false">取消</el-button><el-button type="danger" @click="reject">确认驳回</el-button></template>
    </el-dialog>
    <el-dialog v-model="rddlg" title="拒绝删除申请" width="420px">
      <el-input v-model="rdReason" type="textarea" :rows="3" placeholder="填写拒绝理由" />
      <template #footer><el-button @click="rddlg=false">取消</el-button><el-button type="danger" @click="rejectDelete">确认拒绝</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { auditStories, approveStory, rejectStory, deleteRequests, approveDeleteRequest, rejectDeleteRequest } from '../api/audit'
import PageTitle from '../components/common/PageTitle.vue'
import EmptyState from '../components/common/EmptyState.vue'
import AuditStoryTable from '../components/audit/AuditStoryTable.vue'
const router = useRouter()
const list = ref([]); const dels = ref([])
const rdlg = ref(false); const reason = ref(''); const target = ref(null)
const rddlg = ref(false); const rdReason = ref(''); const dTarget = ref(null)
async function load() { list.value = await auditStories(); dels.value = await deleteRequests() }
function preview(row) { router.push('/stories/' + row.storyId) }
async function approve(row) {
  await ElMessageBox.confirm('通过《' + row.title + '》并发布？', '审核', { type: 'info' })
  await approveStory(row.storyId); ElMessage.success('已通过'); load()
}
function openReject(row) { target.value = row; reason.value = ''; rdlg.value = true }
async function reject() {
  if (!reason.value.trim()) return ElMessage.warning('请填写驳回意见')
  await rejectStory(target.value.storyId, reason.value); rdlg.value = false; ElMessage.success('已驳回'); load()
}
async function agreeDelete(row) {
  await ElMessageBox.confirm('同意删除《' + row.storyTitle + '》？删除后不可恢复。', '删除申请', { type: 'warning' })
  await approveDeleteRequest(row.reqId); ElMessage.success('已同意删除'); load()
}
function openRejectDel(row) { dTarget.value = row; rdReason.value = ''; rddlg.value = true }
async function rejectDelete() {
  await rejectDeleteRequest(dTarget.value.reqId, rdReason.value); rddlg.value = false; ElMessage.success('已拒绝'); load()
}
onMounted(load)
</script>
<style scoped>.sec { font-size: 17px; margin: 22px 0 12px; }</style>
