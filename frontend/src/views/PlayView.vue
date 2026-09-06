<template>
  <div class="page play">
    <PageTitle title="游玩" :subtitle="node ? node.storyTitle : '加载中…'" />
    <template v-if="node">
      <ProgressBar :node-id="node.nodeId" :status="node.isEnding" />
      <div class="card scene">
        <p class="text">{{ node.nodeText }}</p>
        <template v-if="node.isEnding === 1">
          <EndingPanel :node="node" @restart="start" />
        </template>
        <template v-else>
          <ChoiceButton v-for="c in node.choices" :key="c.choiceId" :choice="c" :busy="busy" @pick="pick" />
          <EmptyState v-if="!node.choices.length" text="当前节点没有可选分支（作者可能在编辑中）" />
        </template>
      </div>
    </template>
    <div class="ops" v-if="node"><el-button @click="$router.push('/progress')">我的进度</el-button><el-button @click="$router.push('/stories/' + node.storyId)">返回详情</el-button></div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { startPlay, choose } from '../api/play'
import { useUserStore } from '../store/user'
import PageTitle from '../components/common/PageTitle.vue'
import ProgressBar from '../components/play/ProgressBar.vue'
import ChoiceButton from '../components/play/ChoiceButton.vue'
import EndingPanel from '../components/play/EndingPanel.vue'
import EmptyState from '../components/common/EmptyState.vue'
const route = useRoute(); const router = useRouter(); const store = useUserStore()
const node = ref(null); const busy = ref(false)
async function start() { node.value = await startPlay(route.params.id) }
async function pick(choice) {
  busy.value = true
  try { node.value = await choose({ progressId: node.value.progressId, choiceId: choice.choiceId }) }
  finally { busy.value = false }
}
onMounted(() => { if (!store.isLogin) return router.replace({ name: 'login', query: { redirect: route.fullPath } }); start() })
</script>
<style scoped>
.scene { min-height: 260px; }
.text { font-size: 19px; line-height: 2; margin-bottom: 22px; }
.play :deep(.choice) { margin-bottom: 12px; }
.ops { margin-top: 14px; display: flex; gap: 8px; }
</style>
