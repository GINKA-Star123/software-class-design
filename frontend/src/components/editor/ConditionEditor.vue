<template>
  <div class="cond">
    <el-input v-model="value" placeholder="如 ach:FIRST_PLAY、end:结局标题，可用 && || 组合" size="small" />
    <p class="tip">支持：ach:成就编码、end:结局标题、true/false；例：ach:FIRST_PLAY || end:破晓之前</p>
  </div>
</template>
<script setup>
import { computed } from 'vue'
const props = defineProps({ modelValue: String })
const emit = defineEmits(['update:modelValue'])
const value = computed({ get: () => props.modelValue || '', set: (v) => emit('update:modelValue', v) })
</script>
<style scoped>.tip { font-size:12px; color:var(--text-muted); margin:4px 0 0; }</style>
