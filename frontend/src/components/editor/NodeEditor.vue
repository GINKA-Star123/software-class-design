<template>
  <el-form label-position="top">
    <el-form-item label="结局标题"><el-input v-model="form.endingTitle" :disabled="form.isEnding !== 1" placeholder="结局标题" /></el-form-item>
    <el-form-item label="剧情文本" required><el-input v-model="form.nodeText" type="textarea" :rows="7" maxlength="2000" show-word-limit /></el-form-item>
    <div class="row">
      <el-checkbox v-model="form.isEnding" :true-value="1" :false-value="0">结局节点</el-checkbox>
      <el-button type="primary" @click="$emit('save', { ...form })">保存节点</el-button>
      <el-button v-if="canDelete" type="danger" plain @click="$emit('remove')">删除节点</el-button>
    </div>
  </el-form>
</template>
<script setup>
import { reactive, watch } from 'vue'
const props = defineProps({ node: Object, canDelete: Boolean })
const emit = defineEmits(['save', 'remove'])
const form = reactive({ nodeText: '', isEnding: 0, endingTitle: '' })
watch(() => props.node, (n) => { if (n) { form.nodeText = n.nodeText || ''; form.isEnding = n.isEnding || 0; form.endingTitle = n.endingTitle || '' } }, { immediate: true })
</script>
<style scoped>.row { display:flex; gap:14px; align-items:center; }</style>
