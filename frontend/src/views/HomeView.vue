<template>
  <div class="page home">
    <h1 class="page-title">抉择工坊</h1>
    <p class="page-sub">官方主线与玩家共创的决策树游戏平台</p>
    <div v-if="loading"><EmptyState text="加载中…" /></div>
    <template v-else>
      <div v-if="official" class="hero card">
        <div>
          <el-tag type="danger" effect="dark" size="small">官方推荐</el-tag>
          <h2>{{ official.title }}</h2>
          <p>{{ official.intro }}</p>
          <el-button type="primary" size="large" @click="$router.push('/stories/' + official.storyId)">立即游玩</el-button>
        </div>
      </div>
      <h2 class="sec">热门故事</h2>
      <div class="grid">
        <StoryCard v-for="s in hot" :key="s.storyId" :story="s" />
      </div>
      <h2 class="sec">新作尝鲜</h2>
      <div class="grid">
        <StoryCard v-for="s in fresh" :key="s.storyId" :story="s" />
      </div>
      <EmptyState v-if="!hot.length && !fresh.length" text="暂无已发布故事" />
    </template>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { home } from '../api/story'
import StoryCard from '../components/story/StoryCard.vue'
import EmptyState from '../components/common/EmptyState.vue'
const loading = ref(true); const official = ref(null); const hot = ref([]); const fresh = ref([])
onMounted(async () => { try { const d = await home(); official.value = (d.recommend || [])[0] || null; hot.value = d.hot || []; fresh.value = d.fresh || [] } finally { loading.value = false } })
</script>
<style scoped>
.hero { background: linear-gradient(120deg,#1f4d78,#2b6cb0); color:#fff; border:none; margin-bottom:20px; }
.hero h2 { margin: 12px 0 8px; font-size: 26px; }
.hero p { color: rgba(255,255,255,.88); line-height:1.8; }
.sec { font-size: 19px; margin: 22px 0 12px; }
.grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(235px, 1fr)); gap: 14px; }
</style>
