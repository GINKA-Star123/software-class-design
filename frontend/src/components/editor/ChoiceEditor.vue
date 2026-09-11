<template>
  <section class="choice-editor">
    <div class="choice-head">
      <div>
        <h4>选项（{{ choices.length }}）</h4>
        <p>修改目标、文本或条件后自动保存</p>
      </div>
      <el-button size="small" type="primary" plain @click="$emit('add')">+ 添加选项</el-button>
    </div>

    <div v-for="(ch, index) in choices" :key="ch.choiceId" class="choice">
      <div class="choice-index">{{ index + 1 }}</div>
      <div class="choice-fields">
        <el-select v-model="ch.toNodeId" size="small" placeholder="目标节点" @change="touch(ch)">
          <el-option v-for="t in targets" :key="t.nodeId" :value="t.nodeId" :label="label(t)" />
        </el-select>
        <el-input v-model="ch.choiceText" size="small" placeholder="选项文本" @input="touch(ch)" @blur="flush(ch)" />
        <ConditionEditor v-model="ch.conditionExpr" @change="touch(ch)" @blur="flush(ch)" />
      </div>
      <div class="ops">
        <el-button size="small" type="primary" text @click="save(ch)">保存</el-button>
        <el-button size="small" type="danger" text @click="$emit('delete', ch)">删除</el-button>
      </div>
    </div>

    <div v-if="!choices.length" class="choice-empty">
      当前节点还没有选项，点击右上角“添加选项”开始搭建分支。
    </div>
  </section>
</template>
<script setup>
import ConditionEditor from './ConditionEditor.vue'

defineProps({
  choices: { type: Array, default: () => [] },
  targets: { type: Array, default: () => [] }
})
const emit = defineEmits(['save', 'delete', 'add', 'change', 'flush'])

function label(t) {
  return (t.isStart === 1 ? '[始]' : t.isEnding === 1 ? '[结]' : '') + (t.nodeText || '').slice(0, 12)
}
function touch(ch) {
  emit('change', ch)
}
function flush(ch) {
  emit('flush', ch)
}
function save(ch) {
  emit('save', ch)
}
</script>
<style scoped>
.choice-head {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 10px;
  margin-bottom: 12px;
}
.choice-head h4 {
  margin: 0 0 4px;
  font-size: 15px;
}
.choice-head p {
  margin: 0;
  font-size: 12px;
  color: var(--text-muted);
}
.choice {
  display: grid;
  grid-template-columns: 28px minmax(0, 1fr) auto;
  gap: 10px;
  padding: 12px;
  border: 1px solid var(--border);
  border-radius: 10px;
  background: #fbfcfe;
  margin-bottom: 10px;
}
.choice-index {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: var(--brand-light);
  color: var(--brand);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 700;
}
.choice-fields {
  display: grid;
  grid-template-columns: minmax(150px, 1.1fr) minmax(180px, 1.6fr) minmax(180px, 1.3fr);
  gap: 8px;
  align-items: start;
}
.choice-fields > * {
  min-width: 0;
}
.ops {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
.choice-empty {
  padding: 18px;
  border: 1px dashed var(--border);
  border-radius: 10px;
  color: var(--text-muted);
  font-size: 13px;
  text-align: center;
  background: #fafbfd;
}
@media (max-width: 860px) {
  .choice {
    grid-template-columns: 24px minmax(0, 1fr);
  }
  .choice-fields {
    grid-template-columns: 1fr;
  }
  .ops {
    grid-column: 2;
    flex-direction: row;
  }
}
</style>
