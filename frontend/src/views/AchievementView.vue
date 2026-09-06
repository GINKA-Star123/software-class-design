<template>
  <div class="page">
    <PageTitle title="我的成就" subtitle="达成结局与完成挑战即可解锁" />
    <div class="grid">
      <div v-for="a in list" :key="a.achId" class="card item" :class="{ locked: !a.achieved }">
        <div class="badge" :class="{ on: a.achieved }">{{ a.achieved ? '✓' : '?' }}</div>
        <div><b>{{ a.achName }}</b><p class="muted">{{ a.description }}</p><el-tag v-if="a.achieved" type="success" size="small">已达成</el-tag></div>
      </div>
    </div>
    <EmptyState v-if="!list.length" text="暂无成就" />
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { myAchievements } from '../api/achievement'
import PageTitle from '../components/common/PageTitle.vue'
import EmptyState from '../components/common/EmptyState.vue'
const list = ref([])
onMounted(async () => { list.value = await myAchievements() })
</script>
<style scoped>
.grid { display:grid; grid-template-columns:repeat(auto-fill,minmax(250px,1fr)); gap:12px; }
.item { display:flex; gap:12px; align-items:center; }
.locked { opacity:.55; }
.badge { width:44px;height:44px;flex:none;border-radius:50%;display:flex;align-items:center;justify-content:center;border:2px dashed #cbd5e0;color:#a0aec0; }
.badge.on { border:none; background:linear-gradient(135deg,#f6ad55,#ed8936); color:#fff; }
</style>
