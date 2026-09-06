<template>
  <div class="ending">
    <el-tag type="success" size="large" effect="dark">结局达成</el-tag>
    <h2>{{ node.endingTitle }}</h2>
    <p>{{ node.nodeText }}</p>
    <p v-if="node.endingCount != null" class="muted">本故事已达成结局数：{{ node.endingCount }}</p>
    <div class="ops">
      <el-button type="primary" @click="$emit('restart')">再玩一次</el-button>
      <el-button @click="$router.push('/stories/' + node.storyId)">返回详情</el-button>
    </div>
  </div>
</template>
<script setup>
defineProps({ node: Object })
defineEmits(['restart'])
</script>
<style scoped>
.ending { text-align:center; padding: 30px 10px; }
.ending h2 { font-size: 26px; margin: 14px 0; }
.ending p { line-height: 1.9; color: var(--text-secondary); }
.ops { margin-top: 18px; }
</style>
