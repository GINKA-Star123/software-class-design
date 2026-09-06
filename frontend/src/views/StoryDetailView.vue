<template>
  <div v-if="story" class="page detail">
    <PageTitle :title="story.title" :subtitle="story.authorName + ' · ' + (story.category || '未分类')" />
    <el-tag v-if="story.status !== 2" :type="'info'" style="margin-bottom:10px">当前状态：{{ STATUS_TEXT[story.status] }}</el-tag>
    <div class="card main">
      <p class="intro">{{ story.intro }}</p>
      <StoryMeta :story="story" />
      <div class="ops">
        <el-button v-if="story.status === 2" type="primary" size="large" @click="$router.push('/play/' + story.storyId)">开始游玩</el-button>
        <el-button v-if="store.isLogin && story.status === 2" :type="st.liked ? 'warning' : 'default'" @click="like">♥ {{ st.liked ? '已赞' : '点赞' }}</el-button>
        <el-button v-if="store.isLogin && story.status === 2" :type="st.favorited ? 'warning' : 'default'" @click="fav">★ {{ st.favorited ? '已收藏' : '收藏' }}</el-button>
        <el-button v-if="store.isLogin" type="danger" plain @click="openReport">举报</el-button>
      </div>
    </div>
    <div class="card comments">
      <CommentList :story-id="story.storyId" :can-post="store.isLogin && story.status === 2" :list="comments" :total="story.commentCount || 0" @posted="loadComments" />
    </div>
    <el-dialog v-model="reportOpen" title="举报" width="420px">
      <el-input v-model="reportReason" type="textarea" :rows="3" maxlength="200" show-word-limit placeholder="请说明举报原因" />
      <template #footer><el-button @click="reportOpen=false">取消</el-button><el-button type="danger" @click="report">提交举报</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getStory } from '../api/story'
import { interactStatus, toggleLike, toggleFavorite, listComments, reportStory } from '../api/interact'
import { useUserStore } from '../store/user'
import { STATUS_TEXT } from '../utils/constants'
import PageTitle from '../components/common/PageTitle.vue'
import StoryMeta from '../components/story/StoryMeta.vue'
import CommentList from '../components/story/CommentList.vue'
const route = useRoute(); const store = useUserStore()
const story = ref(null); const st = ref({ liked: false, favorited: false }); const comments = ref([])
const reportOpen = ref(false); const reportReason = ref('')
async function loadStatus() { try { st.value = await interactStatus(story.value.storyId) } catch (e) {} }
async function loadComments() { comments.value = await listComments(story.value.storyId) }
async function like() { const d = await toggleLike(story.value.storyId); st.value.liked = d.liked; story.value.likeCount += d.liked ? 1 : -1 }
async function fav() { const d = await toggleFavorite(story.value.storyId); st.value.favorited = d.favorited; story.value.favoriteCount += d.favorited ? 1 : -1 }
function openReport() { reportOpen.value = true }
async function report() {
  if (!reportReason.value.trim()) return ElMessage.warning('请填写举报原因')
  await reportStory(story.value.storyId, reportReason.value); reportOpen.value = false; reportReason.value = ''; ElMessage.success('举报已提交')
}
onMounted(async () => {
  story.value = await getStory(route.params.id)
  if (store.isLogin) loadStatus()
  loadComments()
})
</script>
<style scoped>
.intro { font-size: 16px; line-height: 1.9; color: var(--text-secondary); }
.ops { margin-top: 18px; display: flex; gap: 8px; flex-wrap: wrap; }
.comments { margin-top: 16px; }
</style>
