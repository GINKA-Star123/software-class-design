<template>
  <div class="search-bar">
    <el-input v-model="keyword" placeholder="搜索标题/简介" clearable @keyup.enter="emitSearch" @clear="emitSearch" />
    <el-radio-group v-model="sort">
      <el-radio-button label="new">最新</el-radio-button>
      <el-radio-button label="hot">最热</el-radio-button>
    </el-radio-group>
    <el-button type="primary" @click="emitSearch">搜索</el-button>
  </div>
</template>
<script setup>
import { ref, watch } from 'vue'
const emit = defineEmits(['search'])
const keyword = ref('')
const sort = ref('new')
watch(sort, () => emit('search', { keyword: keyword.value, sort: sort.value }))
function emitSearch() { emit('search', { keyword: keyword.value, sort: sort.value }) }
defineExpose({ get query() { return { keyword: keyword.value, sort: sort.value } } })
</script>
<style scoped>
.search-bar { display: flex; gap: 12px; margin-bottom: 16px; flex-wrap: wrap; align-items: center; }
.search-bar .el-input { width: 260px; }
</style>
