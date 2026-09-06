<template>
  <div class="progress"><div class="fill" :style="{ width: pct + '%' }" /></div>
</template>
<script setup>
import { computed } from 'vue'
const props = defineProps({ nodeId: Number, status: Number })
const pct = computed(() => props.status === 1 ? 100 : Math.min(90, 8 + ((props.nodeId || 1) % 50)))
</script>
<style scoped>
.progress { height: 6px; background: #e2e8f0; border-radius: 99px; overflow: hidden; margin-bottom: 20px; }
.fill { height: 100%; background: linear-gradient(90deg,#63b3ed,#2b6cb0); transition: width .4s; }
</style>
