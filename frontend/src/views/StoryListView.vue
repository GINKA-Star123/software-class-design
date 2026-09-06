<template>
  <div class="page">
    <PageTitle title="故事库" subtitle="浏览官方与玩家共创的决策树故事" />
    <StorySearchBar ref="bar" @search="apply" />
    <div class="grid">
      <StoryCard v-for="s in list" :key="s.storyId" :story="s" />
    </div>
    <EmptyState v-if="!loading && !list.length" text="没有找到相关故事" />
    <div class="more"><el-button v-if="list.length && !loading" :loading="loading" @click="loadMore">加载更多</el-button></div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { listStories } from '../api/story'
import StoryCard from '../components/story/StoryCard.vue'
import StorySearchBar from '../components/story/StorySearchBar.vue'
import PageTitle from '../components/common/PageTitle.vue'
import EmptyState from '../components/common/EmptyState.vue'
const list = ref([]); const page = ref(1); const pageSize = 12; const loading = ref(false)
async function apply(q = { keyword: '', sort: 'new' }) {
  loading.value = true; page.value = 1
  try { list.value = await listStories({ page: page.value, pageSize, keyword: q.keyword, sort: q.sort }) || [] } finally { loading.value = false }
}
async function loadMore() {
  page.value += 1; loading.value = true
  try { const more = await listStories({ page: page.value, pageSize, sort: 'new' }) || []; list.value.push(...more) } finally { loading.value = false }
}
onMounted(() => apply())
</script>
<style scoped>
.grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(235px, 1fr)); gap: 14px; }
.more { text-align:center; margin-top: 18px; }
</style>
