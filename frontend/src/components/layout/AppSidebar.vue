<template>
  <aside class="sidebar">
    <router-link v-for="item in items" :key="item.to" :to="item.to" class="item">{{ item.text }}</router-link>
  </aside>
</template>
<script setup>
defineProps({ items: { type: Array, default: () => [] } })
</script>
<style scoped>
.sidebar { display:flex; flex-direction:column; gap:6px; }
.item { padding: 10px 12px; border-radius: 8px; color: var(--text-secondary); }
.item.router-link-active { background: var(--brand-light); color: var(--brand); font-weight:600; }
</style>
