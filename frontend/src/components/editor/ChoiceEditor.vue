<template>
  <div>
    <h4>选项（{{ choices.length }}）</h4>
    <div v-for="ch in choices" :key="ch.choiceId" class="choice">
      <el-select v-model="ch.toNodeId" size="small" placeholder="目标节点">
        <el-option v-for="t in targets" :key="t.nodeId" :value="t.nodeId" :label="label(t)" />
      </el-select>
      <el-input v-model="ch.choiceText" size="small" placeholder="选项文本" />
      <ConditionEditor v-model="ch.conditionExpr" />
      <div class="ops">
        <el-button size="small" type="primary" text @click="save(ch)">保存</el-button>
        <el-button size="small" type="danger" text @click="$emit('delete', ch)">删除</el-button>
      </div>
    </div>
    <el-button size="small" text type="primary" @click="$emit('add')">+ 添加选项</el-button>
  </div>
</template>
<script setup>
import ConditionEditor from './ConditionEditor.vue'
defineProps({ choices: Array, targets: Array })
defineEmits(['save', 'delete', 'add'])
function label(t) { return (t.isStart === 1 ? '[始]' : t.isEnding === 1 ? '[结]' : '') + (t.nodeText || '').slice(0, 10) }
</script>
<style scoped>
.choice { display:grid; grid-template-columns:1.2fr 1.4fr 1fr auto; gap:8px; margin-bottom:10px; }
.choice > * { min-width:0; }
.ops { display:flex; }
</style>
