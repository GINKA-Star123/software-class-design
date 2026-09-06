<template>
  <el-popconfirm v-if="simple" :title="message" @confirm="$emit('confirm')">
    <template #reference><slot /></template>
  </el-popconfirm>
  <el-dialog v-else :model-value="modelValue" :title="title" width="400px" @update:model-value="$emit('update:modelValue', $event)">
    <p>{{ message }}</p>
    <template #footer>
      <el-button @click="$emit('update:modelValue', false)">取消</el-button>
      <el-button type="danger" @click="$emit('confirm')">确定</el-button>
    </template>
  </el-dialog>
</template>
<script setup>
defineProps({ modelValue: Boolean, title: String, message: { type: String, default: '确定执行该操作吗？' }, simple: Boolean })
defineEmits(['update:modelValue', 'confirm'])
</script>
