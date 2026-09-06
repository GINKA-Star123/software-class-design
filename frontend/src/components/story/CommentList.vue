<template>
  <div class="comments">
    <h3>评论（{{ total }}）</h3>
    <div v-if="canPost" class="post">
      <el-input v-model="content" type="textarea" :rows="2" maxlength="500" show-word-limit placeholder="说说你的看法…" />
      <el-button type="primary" style="margin-top:8px" @click="post">发表评论</el-button>
    </div>
    <div v-for="c in list" :key="c.commentId" class="item">
      <div class="head"><b>{{ c.nickname }}</b><span class="muted">{{ c.createTime }}</span></div>
      <p>{{ c.content }}</p>
    </div>
    <EmptyState v-if="!list.length" text="还没有评论，快来抢沙发～" />
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { addComment } from '../../api/interact'
import EmptyState from '../common/EmptyState.vue'
const props = defineProps({ storyId: [Number, String], canPost: Boolean, list: { type: Array, default: () => [] }, total: { type: Number, default: 0 } })
const emit = defineEmits(['posted'])
const content = ref('')
async function post() {
  if (!content.value.trim()) return ElMessage.warning('评论不能为空')
  await addComment(props.storyId, content.value)
  content.value = ''
  ElMessage.success('评论成功')
  emit('posted')
}
</script>
<style scoped>
.post { margin-bottom: 14px; }
.item { border-bottom: 1px solid var(--border); padding: 10px 0; }
.head { display: flex; justify-content: space-between; font-size: 13px; }
.item p { margin: 6px 0 0; line-height: 1.7; }
</style>
