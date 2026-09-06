<template>
  <div class="empty-state">
    <p>{{ text }}</p>
  </div>
</template>
<script setup>
defineProps({ text: { type: String, default: '暂无数据' } })
</script>
<style scoped>.empty-state { text-align: center; color: var(--text-muted); padding: 40px 0; }</style>
