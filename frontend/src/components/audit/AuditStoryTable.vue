<template>
  <el-table :data="stories" style="width:100%">
    <el-table-column prop="title" label="标题" min-width="160" />
    <el-table-column prop="authorName" label="作者" width="130" />
    <el-table-column prop="nodeCount" label="节点" width="70" align="center" />
    <el-table-column prop="endingCount" label="结局" width="70" align="center" />
    <el-table-column prop="submitTime" label="提交时间" width="180" />
    <el-table-column label="操作" width="220" align="center">
      <template #default="{ row }">
        <el-button size="small" @click="$emit('preview', row)">预览</el-button>
        <el-button size="small" type="success" @click="$emit('approve', row)">通过</el-button>
        <el-button size="small" type="danger" @click="$emit('reject', row)">驳回</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>
<script setup>
defineProps({ stories: Array })
defineEmits(['preview', 'approve', 'reject'])
</script>
