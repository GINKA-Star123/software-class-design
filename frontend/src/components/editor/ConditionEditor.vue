<template>
  <div class="cond">
    <el-input
      v-model="value"
      placeholder="如 ach:FIRST_PLAY、end:结局标题"
      size="small"
      @input="$emit('change', value)"
      @blur="$emit('blur')"
    />
    <p class="tip">支持 ach:成就编码、end:结局标题、true/false，可用 &amp;&amp; 或 || 组合</p>
  </div>
</template>
<script setup>
import { computed } from 'vue'

const props = defineProps({ modelValue: String })
const emit = defineEmits(['update:modelValue', 'change', 'blur'])
const value = computed({
  get: () => props.modelValue || '',
  set: (v) => emit('update:modelValue', v)
})
</script>
<style scoped>
.tip {
  font-size: 12px;
  color: var(--text-muted);
  margin: 4px 0 0;
  line-height: 1.5;
}
</style>
