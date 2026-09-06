<template>
  <div class="page">
    <PageTitle title="举报处理" subtitle="处理玩家提交的举报" />
    <ReportTable :reports="list" @handle="handle" />
    <EmptyState v-if="!list.length" text="暂无待处理举报" />
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { auditReports, handleReport } from '../api/audit'
import PageTitle from '../components/common/PageTitle.vue'
import EmptyState from '../components/common/EmptyState.vue'
import ReportTable from '../components/audit/ReportTable.vue'
const list = ref([])
async function load() { list.value = await auditReports() }
async function handle(row, action) {
  const label = action === 'offline' ? '下架并处理' : action === 'ignore' ? '忽略' : '标记处理'
  await ElMessageBox.confirm('确定执行「' + label + '」吗？', '举报处理', { type: 'warning' })
  await handleReport(row.reportId, action); ElMessage.success('已处理'); load()
}
onMounted(load)
</script>
