<template>
  <header class="site-header">
    <div class="header-inner">
      <router-link to="/" class="logo">抉择工坊</router-link>
      <nav class="nav">
        <router-link to="/" exact-active-class="active">首页</router-link>
        <router-link to="/stories" active-class="active">故事库</router-link>
        <router-link to="/rank" active-class="active">排行榜</router-link>
        <template v-if="store.isAuthor"><router-link to="/my-stories" active-class="active">创作中心</router-link></template>
        <template v-if="store.isAuditor"><router-link to="/audit" active-class="active">审核中心</router-link><router-link to="/report" active-class="active">举报处理</router-link></template>
        <template v-if="store.isAdmin"><router-link to="/admin" active-class="active">管理后台</router-link></template>
      </nav>
      <div class="right">
        <template v-if="store.isLogin">
          <el-button v-if="!store.isAuthor && !store.isAdmin" text type="primary" @click="applyAuthor">成为作者</el-button>
          <el-dropdown @command="onCommand">
            <span class="user">{{ store.user?.nickname || store.user?.username }}</span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="progress">我的进度</el-dropdown-item>
                <el-dropdown-item command="achievements">我的成就</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button size="small" @click="$router.push('/login')">登录</el-button>
          <el-button size="small" type="primary" @click="$router.push('/register')">注册</el-button>
        </template>
      </div>
    </div>
  </header>
</template>
<script setup>
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store/user'
import { applyAuthor as apiApplyAuthor } from '../../api/user'
const store = useUserStore(); const router = useRouter()
async function onCommand(cmd) {
  if (cmd === 'logout') { await store.logout(); router.push('/login') }
  else router.push('/' + cmd)
}
async function applyAuthor() { const u = await apiApplyAuthor(); store.setUser(u); ElMessage.success('已开通作者权限') }
</script>
<style scoped>
.site-header { background:#fff; border-bottom:1px solid var(--border); position:sticky; top:0; z-index:20; }
.header-inner { max-width:var(--page-w); margin:0 auto; height:58px; display:flex; align-items:center; gap:18px; padding:0 16px; }
.logo { font-weight:800; font-size:19px; color:var(--text); }
.nav { flex:1; display:flex; gap:14px; font-size:14px; color:var(--text-secondary); align-items:center; flex-wrap:wrap; }
.nav a.active { color:var(--brand); font-weight:700; }
.right { display:flex; align-items:center; gap:8px; }
.user { cursor:pointer; font-weight:600; color:var(--text); }
</style>
