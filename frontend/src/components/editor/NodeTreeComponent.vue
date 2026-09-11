<template>
  <div class="tree">
    <div class="head">
      <div>
        <b>节点（{{ nodes.length }}）</b>
        <p class="tree-sub">点击切换正在编辑的剧情节点</p>
      </div>
      <el-button size="small" type="primary" text @click="$emit('add')">+ 新节点</el-button>
    </div>
    <div v-for="n in nodes" :key="n.nodeId" class="node"
         :class="{ active: current === n.nodeId, start: n.isStart === 1, end: n.isEnding === 1, warn: warnIds.includes(n.nodeId) }"
         @click="$emit('select', n)">
      <span class="flag">{{ n.isStart === 1 ? '始' : n.isEnding === 1 ? '结' : '#' }}</span>
      <span class="id">#{{ n.nodeId }}</span>
      <span class="txt">{{ preview(n.nodeText) }}</span>
      <el-tag v-if="warnIds.includes(n.nodeId)" type="danger" size="small">问题</el-tag>
    </div>
    <p v-if="nodes.length === 0" class="muted empty">还没有节点，点右上角“新节点”开始创作。</p>
  </div>
</template>
<script setup>
defineProps({ nodes: Array, current: Number, warnIds: { type: Array, default: () => [] } })
defineEmits(['select', 'add'])
function preview(t) { return (t || '').replace(/\s+/g, ' ').slice(0, 18) + ((t || '').length > 18 ? '…' : '') }
</script>
<style scoped>
.tree { display:flex; flex-direction:column; gap:6px; }
.head { display:flex; justify-content:space-between; align-items:flex-start; gap:8px; margin-bottom:8px; }
.tree-sub { margin:3px 0 0; font-size:11px; color:var(--text-muted); }
.node { display:flex; gap:6px; align-items:center; border:1px solid var(--border); border-radius:8px; padding:8px; cursor:pointer; font-size:13px; background:#fff; transition:border-color .2s, background .2s, transform .2s; }
.node:hover { border-color:#b7cdea; transform:translateX(2px); }
.node.active { border-color:#2b6cb0; background:#e8f0fe; box-shadow:inset 3px 0 0 #2b6cb0; }
.node.warn { border-color:#c53030; background:#fef2f2; }
.node.start .flag { color:#2f855a; font-weight:700; }
.node.end .flag { color:#c53030; font-weight:700; }
.flag { width:16px; }
.id { color:var(--text-muted); font-size:12px; flex:none; }
.txt { flex:1; min-width:0; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }
.empty { padding:20px 6px; text-align:center; line-height:1.7; border:1px dashed var(--border); border-radius:8px; }
</style>
