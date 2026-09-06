<template>
  <el-table :data="reports" style="width:100%">
    <el-table-column prop="storyTitle" label="被举报故事" min-width="150" />
    <el-table-column prop="reason" label="原因" min-width="140" />
    <el-table-column prop="reporterName" label="举报人" width="120" />
    <el-table-column prop="createTime" label="时间" width="180" />
    <el-table-column label="操作" width="240" align="center">
      <template #default="{ row }">
        <el-button size="small" type="danger" @click="$emit('handle', row, 'offline')">下架</el-button>
        <el-button size="small" type="warning" @click="$emit('handle', row, 'resolve')">处理</el-button>
        <el-button size="small" @click="$emit('handle', row, 'ignore')">忽略</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>
<script setup>
defineProps({ reports: Array })
defineEmits(['handle'])
</script>
