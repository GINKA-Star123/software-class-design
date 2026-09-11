<template>
  <el-form label-position="top" class="node-form">
    <div class="form-grid">
      <el-form-item label="结局标题">
        <el-input
          v-model="form.endingTitle"
          :disabled="form.isEnding !== 1"
          placeholder="设为结局节点后填写"
          @input="touch"
          @blur="flush"
        />
      </el-form-item>
      <el-form-item label="节点属性">
        <div class="attr-row">
          <el-checkbox v-model="form.isEnding" :true-value="1" :false-value="0" @change="touch">
            结局节点
          </el-checkbox>
          <span class="hint">修改后自动保存</span>
        </div>
      </el-form-item>
    </div>
    <el-form-item label="剧情文本" required>
      <el-input
        v-model="form.nodeText"
        type="textarea"
        :autosize="{ minRows: 7, maxRows: 18 }"
        maxlength="2000"
        show-word-limit
        @input="touch"
        @blur="flush"
      />
    </el-form-item>
    <div class="row">
      <el-button type="primary" @click="$emit('save', { ...form })">保存节点</el-button>
      <el-button v-if="canDelete" type="danger" plain @click="$emit('remove')">删除节点</el-button>
      <span class="autosave-hint">输入停止约 0.8 秒后自动保存</span>
    </div>
  </el-form>
</template>
<script setup>
import { reactive, watch } from 'vue'

const props = defineProps({ node: Object, canDelete: Boolean })
const emit = defineEmits(['save', 'remove', 'change', 'flush'])
const form = reactive({ nodeText: '', isEnding: 0, endingTitle: '', sortOrder: 0 })

watch(() => props.node, (n) => {
  if (n) {
    form.nodeText = n.nodeText || ''
    form.isEnding = n.isEnding || 0
    form.endingTitle = n.endingTitle || ''
    form.sortOrder = n.sortOrder || 0
  }
}, { immediate: true })

function touch() {
  emit('change', { ...form })
}

function flush() {
  emit('flush')
}
</script>
<style scoped>
.node-form :deep(.el-form-item) {
  margin-bottom: 14px;
}
.form-grid {
  display: grid;
  grid-template-columns: minmax(220px, 1fr) minmax(180px, 1fr);
  gap: 12px;
}
.attr-row {
  display: flex;
  align-items: center;
  gap: 10px;
  min-height: 32px;
}
.hint,
.autosave-hint {
  font-size: 12px;
  color: var(--text-muted);
}
.row {
  display: flex;
  gap: 12px;
  align-items: center;
  flex-wrap: wrap;
}
@media (max-width: 720px) {
  .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>
