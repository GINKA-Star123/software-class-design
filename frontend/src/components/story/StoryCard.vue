<template>
  <div class="story-card card" @click="$router.push('/stories/' + story.storyId)">
    <img v-if="story.coverUrl" :src="story.coverUrl" class="cover" alt="" />
    <div v-else class="cover" :style="coverStyle">{{ story.title.slice(0, 1) }}</div>
    <div class="body">
      <div class="row1">
        <h3>{{ story.title }}</h3>
        <el-tag v-if="story.category" size="small">{{ story.category }}</el-tag>
      </div>
      <p class="intro">{{ story.intro || '暂无简介' }}</p>
      <div class="meta">
        <span>作者：{{ story.authorName }}</span>
        <span>{{ fmt(story.playCount) }} 游玩</span>
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed } from 'vue'
const props = defineProps({ story: { type: Object, required: true } })
const coverStyle = computed(() => ({ background: `linear-gradient(135deg, hsl(${(props.story.storyId * 47) % 360}, 55%, 62%), hsl(${((props.story.storyId * 47) % 360 + 40) % 360}, 60%, 45%))` }))
function fmt(n) { return n >= 10000 ? (n / 10000).toFixed(1) + '万' : String(n || 0) }
</script>
<style scoped>
.story-card { cursor: pointer; padding: 0; overflow: hidden; transition: transform .15s; }
.story-card:hover { transform: translateY(-3px); box-shadow: 0 8px 20px rgba(43,108,176,.12); }
.cover { height: 140px; width:100%; object-fit:cover; display: flex; align-items: center; justify-content: center; color: #fff; font-size: 46px; font-weight: 800; }
.body { padding: 12px 14px 14px; }
.row1 { display: flex; justify-content: space-between; gap: 8px; align-items: flex-start; }
h3 { margin: 0 0 6px; font-size: 17px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.intro { color: var(--text-muted); font-size: 13px; height: 40px; overflow: hidden; margin: 0 0 10px; }
.meta { display: flex; justify-content: space-between; color: var(--text-muted); font-size: 12px; }
</style>
