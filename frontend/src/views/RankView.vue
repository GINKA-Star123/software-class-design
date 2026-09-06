<template>
  <div class="page">
    <PageTitle title="热度排行榜" subtitle="游玩×1 + 点赞×2 + 收藏×3 + 评论×4" />
    <el-table :data="list" style="width:100%">
      <el-table-column label="排名" width="80" align="center"><template #default="{ $index }"><b>{{ $index + 1 }}</b></template></el-table-column>
      <el-table-column label="故事" min-width="180"><template #default="{ row }"><el-link type="primary" @click="$router.push('/stories/' + row.storyId)">{{ row.title }}</el-link></template></el-table-column>
      <el-table-column prop="authorName" label="作者" width="140" />
      <el-table-column prop="playCount" label="游玩" width="90" align="center" />
      <el-table-column prop="heat" label="热度" width="100" align="center" />
    </el-table>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { rank } from '../api/story'
import PageTitle from '../components/common/PageTitle.vue'
const list = ref([])
onMounted(async () => { list.value = await rank(20) })
</script>
