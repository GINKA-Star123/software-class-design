<template>
  <div class="tree">
    <div class="head"><b>节点（{{ nodes.length }}）</b><el-button size="small" type="primary" text @click="$emit('add')">+ 新节点</el-button></div>
    <div v-for="n in nodes" :key="n.nodeId" class="node"
         :class="{ active: current === n.nodeId, start: n.isStart === 1, end: n.isEnding === 1, warn: warnIds.includes(n.nodeId) }"
         @click="$emit('select', n)">
      <span class="flag">{{ n.isStart === 1 ? '始' : n.isEnding === 1 ? '结' : '#' }}</span>
      <span class="id">#{{ n.nodeId }}</span>
      <span class="txt">{{ preview(n.nodeText) }}</span>
      <el-tag v-if="warnIds.includes(n.nodeId)" type="danger" size="small">问题</el-tag>
    </div>
    <p v-if="nodes.length === 0" class="muted">还没有节点</p>
  </div>
</template>
<script setup>
defineProps({ nodes: Array, current: Number, warnIds: { type: Array, default: () => [] } })
defineEmits(['select', 'add'])
function preview(t) { return (t || '').replace(/\s+/g, ' ').slice(0, 18) + ((t || '').length > 18 ? '…' : '') }
</script>
<style scoped>
.tree { display:flex; flex-direction:column; gap:6px; }
.head { display:flex; justify-content:space-between; align-items:center; margin-bottom:4px; }
.node { display:flex; gap:6px; align-items:center; border:1px solid var(--border); border-radius:8px; padding:7px 8px; cursor:pointer; font-size:13px; }
.node.active { border-color:#2b6cb0; background:#e8f0fe; }
.node.warn { border-color:#c53030; background:#fef2f2; }
.node.start .flag { color:#2f855a; font-weight:700; }
.node.end .flag { color:#c53030; font-weight:700; }
.flag { width:16px; }
.id { color:var(--text-muted); font-size:12px; flex:none; }
.txt { flex:1; min-width:0; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }
</style>
