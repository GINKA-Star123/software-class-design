<template>
  <div class="page-title-wrap">
    <h1 class="page-title">{{ title }}</h1>
    <p v-if="subtitle" class="page-sub">{{ subtitle }}</p>
  </div>
</template>
<script setup>
defineProps({ title: String, subtitle: String })
</script>
