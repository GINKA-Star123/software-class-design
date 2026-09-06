<template>
  <div class="page">
    <PageTitle title="管理后台" subtitle="用户、内容与数据统计" />
    <div v-if="dash" class="stats">
      <div v-for="(v,k) in dash" :key="k" class="card"><b>{{ v }}</b><span>{{ label(k) }}</span></div>
    </div>
    <h2 class="sec">用户管理</h2>
    <el-table :data="users" style="width:100%">
      <el-table-column prop="userId" label="ID" width="60" align="center" />
      <el-table-column prop="username" label="用户名" width="130" />
      <el-table-column prop="nickname" label="昵称" width="130" />
      <el-table-column label="角色" min-width="200"><template #default="{ row }"><el-tag v-for="r in row.roles" :key="r" size="small" style="margin-right:4px">{{ r }}</el-tag></template></el-table-column>
      <el-table-column label="状态" width="90" align="center"><template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'danger'">{{ row.status === 1 ? '正常' : '禁用' }}</el-tag></template></el-table-column>
      <el-table-column label="操作" width="210" align="center">
        <template #default="{ row }">
          <el-button size="small" :type="row.status === 1 ? 'danger' : 'success'" plain @click="toggle(row)">{{ row.status === 1 ? '禁用' : '启用' }}</el-button>
          <el-button size="small" type="primary" plain @click="openRoles(row)">角色</el-button>
        </template>
      </el-table-column>
    </el-table>
    <h2 class="sec">内容管理</h2>
    <el-table :data="contents" style="width:100%">
      <el-table-column prop="storyId" label="ID" width="60" align="center" />
      <el-table-column prop="title" label="标题" min-width="180" />
      <el-table-column prop="authorName" label="作者" width="140" />
      <el-table-column label="状态" width="100" align="center"><template #default="{ row }"><el-tag :type="STATUS_TAG[row.status]">{{ STATUS_TEXT[row.status] }}</el-tag></template></el-table-column>
      <el-table-column prop="playCount" label="游玩" width="90" align="center" />
    </el-table>
    <el-dialog v-model="rdlg" title="设置角色" width="420px">
      <el-checkbox-group v-model="roles">
        <el-checkbox :value="2" label="作者" /><el-checkbox :value="3" label="审核员" /><el-checkbox :value="4" label="管理员" />
      </el-checkbox-group>
      <p class="muted">角色将完全覆盖该用户原角色（玩家角色始终保留）。</p>
      <template #footer><el-button @click="rdlg=false">取消</el-button><el-button type="primary" @click="saveRoles">保存</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { adminUsers, updateUserStatus, updateUserRoles, adminStories, dashboard } from '../api/admin'
import { STATUS_TEXT, STATUS_TAG } from '../utils/constants'
import PageTitle from '../components/common/PageTitle.vue'
const dash = ref(null); const users = ref([]); const contents = ref([])
const rdlg = ref(false); const roles = ref([]); const target = ref(null)
const labels = { totalUsers: '用户', totalStories: '故事', publishedStories: '已发布', pendingStories: '待审核', totalPlays: '总游玩', pendingReports: '待处理举报' }
const label = (k) => labels[k] || k
async function load() {
  dash.value = await dashboard(); const d = await adminUsers(); users.value = d.list || []
  contents.value = await adminStories(null)
}
async function toggle(row) { await updateUserStatus(row.userId, row.status === 1 ? 0 : 1); ElMessage.success('已更新'); load() }
function openRoles(row) { target.value = row; roles.value = (row.roles || []).map(r => r === 'AUTHOR' ? 2 : r === 'AUDITOR' ? 3 : r === 'ADMIN' ? 4 : 0).filter(x => x > 0); rdlg.value = true }
async function saveRoles() { await updateUserRoles(target.value.userId, [1, ...roles.value]); rdlg.value = false; ElMessage.success('角色已更新'); load() }
onMounted(load)
</script>
<style scoped>
.stats { display:grid; grid-template-columns:repeat(auto-fill,minmax(150px,1fr)); gap:12px; margin-bottom:14px; }
.stats .card { text-align:center; }
.stats b { font-size:22px; display:block; }
.stats span { color:var(--text-muted); font-size:12px; }
.sec { margin: 22px 0 12px; font-size:17px; }
</style>
