<template>
  <div class="page">
    <PageTitle title="我的进度" subtitle="游玩进度自动保存，同一故事最多保留 3 条" />
    <div v-for="p in list" :key="p.progressId" class="card row">
      <div class="info">
        <b>{{ p.storyTitle }}</b>
        <el-tag :type="p.status === 1 ? 'success' : 'warning'" size="small">{{ p.status === 1 ? '已完成' : '进行中' }}</el-tag>
        <span class="muted">槽位 {{ p.slotNo }} · 结局 {{ p.endingCount }} · {{ p.updateTime }}</span>
        <p v-if="p.status !== 1" class="muted">当前：{{ p.currentEndingTitle || p.currentNodeText || '—' }}</p>
      </div>
      <div class="ops">
        <el-button size="small" type="primary" @click="$router.push('/play/' + p.storyId)">{{ p.status === 1 ? '重新游玩' : '继续游玩' }}</el-button>
        <ConfirmDialog simple message="确定重置该条进度吗？" @confirm="reset(p)"><el-button size="small" type="danger">重置</el-button></ConfirmDialog>
      </div>
    </div>
    <EmptyState v-if="!list.length" text="还没有游玩记录，去故事库选一个吧～" />
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { myProgress, resetProgress } from '../api/play'
import PageTitle from '../components/common/PageTitle.vue'
import EmptyState from '../components/common/EmptyState.vue'
import ConfirmDialog from '../components/common/ConfirmDialog.vue'
const list = ref([])
async function load() { list.value = await myProgress() }
async function reset(p) { await resetProgress(p.progressId); ElMessage.success('已重置'); load() }
onMounted(load)
</script>
<style scoped>
.row { display:flex; justify-content:space-between; align-items:center; gap:12px; margin-bottom:12px; flex-wrap:wrap; }
.info { display:flex; flex-direction:column; gap:5px; }
.ops { display:flex; gap:8px; }
</style>
