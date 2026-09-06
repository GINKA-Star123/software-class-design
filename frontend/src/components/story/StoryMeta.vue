<template>
  <div class="meta">
    <span>作者：{{ story.authorName }}</span>
    <el-tag v-if="story.isOfficial === 1" type="danger" size="small">官方</el-tag>
    <span>节点 {{ story.nodeCount }}</span>
    <span>结局 {{ story.endingCount }}</span>
    <span>游玩 {{ story.playCount }}</span>
  </div>
</template>
<script setup>
defineProps({ story: Object })
</script>
<style scoped>.meta { display: flex; gap: 14px; align-items: center; flex-wrap: wrap; color: var(--text-secondary); }</style>
