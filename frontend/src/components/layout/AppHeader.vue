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
        <el-button text type="primary" class="appearance-btn" @click="appearanceOpen = true">外观</el-button>
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

    <el-drawer v-model="appearanceOpen" title="网站外观" size="380px" append-to-body>
      <div class="drawer-section">
        <p class="drawer-title">背景主题</p>
        <p class="drawer-tip">选择后立即应用，并保存在当前浏览器。</p>
        <div class="theme-grid">
          <button
            v-for="item in SITE_THEMES"
            :key="item.value"
            class="theme-card"
            :class="{ active: siteTheme.theme === item.value }"
            type="button"
            @click="siteTheme.setTheme(item.value)"
          >
            <span class="swatch" :style="{ background: item.css }"></span>
            <span class="theme-label">{{ item.label }}</span>
            <small>{{ item.desc }}</small>
          </button>
        </div>
      </div>

      <el-divider />

      <div class="drawer-section">
        <p class="drawer-title">自定义背景图</p>
        <p class="drawer-tip">支持 jpg/png/webp，上传后会保存在服务器，并在当前浏览器中生效。</p>
        <el-upload :show-file-list="false" accept="image/*" :http-request="uploadBg">
          <el-button size="small" type="primary" :loading="bgUploading">上传背景图</el-button>
        </el-upload>
        <div v-if="siteTheme.customBg" class="bg-preview">
          <img :src="siteTheme.customBg" alt="自定义背景" />
          <el-button size="small" text type="danger" @click="siteTheme.setCustomBg('')">移除背景图</el-button>
        </div>
      </div>

      <div v-if="siteTheme.customBg" class="drawer-section">
        <p class="drawer-title">背景遮罩浓度</p>
        <p class="drawer-tip">数值越高，页面文字越清晰，背景图越淡。</p>
        <el-slider v-model="bgOpacity" :min="0" :max="0.8" :step="0.05" />
      </div>

      <el-button text class="reset-btn" @click="siteTheme.reset()">恢复默认外观</el-button>
    </el-drawer>
  </header>
</template>
<script setup>
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store/user'
import { useSiteThemeStore, SITE_THEMES } from '../../store/siteTheme'
import { applyAuthor as apiApplyAuthor } from '../../api/user'
import { uploadCover } from '../../api/upload'
import { validateImageFile } from '../../utils/imageUpload'

const store = useUserStore()
const siteTheme = useSiteThemeStore()
const router = useRouter()
const appearanceOpen = ref(false)
const bgUploading = ref(false)
const bgOpacity = computed({
  get: () => siteTheme.opacity,
  set: (value) => siteTheme.setOpacity(value)
})

async function onCommand(cmd) {
  if (cmd === 'logout') { await store.logout(); router.push('/login') }
  else router.push('/' + cmd)
}

async function applyAuthor() {
  const u = await apiApplyAuthor()
  store.setUser(u)
  ElMessage.success('已开通作者权限')
}

async function uploadBg({ file }) {
  const check = validateImageFile(file)
  if (!check.ok) {
    ElMessage.warning(check.message)
    return
  }
  bgUploading.value = true
  try {
    const res = await uploadCover(file)
    siteTheme.setCustomBg(res.url)
    ElMessage.success('背景已更新')
  } catch (e) {
    // 上传失败时 request 拦截器已提示
  } finally {
    bgUploading.value = false
  }
}
</script>
<style scoped>
.site-header { background: rgba(255, 255, 255, 0.86); border-bottom: 1px solid var(--border); position: sticky; top: 0; z-index: 20; backdrop-filter: blur(8px); }
.header-inner { max-width: var(--page-w); margin: 0 auto; height: 58px; display: flex; align-items: center; gap: 18px; padding: 0 16px; }
.logo { font-weight: 800; font-size: 19px; color: var(--text); }
.nav { flex: 1; display: flex; gap: 14px; font-size: 14px; color: var(--text-secondary); align-items: center; flex-wrap: wrap; }
.nav a.active { color: var(--brand); font-weight: 700; }
.right { display: flex; align-items: center; gap: 8px; }
.user { cursor: pointer; font-weight: 600; color: var(--text); }
.appearance-btn { padding: 6px 8px; }
.drawer-section { padding: 2px 0 10px; }
.drawer-title { margin: 0 0 4px; font-size: 14px; font-weight: 700; color: var(--text); }
.drawer-tip { margin: 0 0 12px; font-size: 12px; color: var(--text-muted); line-height: 1.6; }
.theme-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
.theme-card { display: flex; flex-direction: column; align-items: flex-start; gap: 4px; padding: 10px; border: 2px solid var(--border); border-radius: 10px; background: #fff; cursor: pointer; text-align: left; }
.theme-card.active { border-color: var(--brand); box-shadow: 0 0 0 3px rgba(43, 108, 176, 0.12); }
.theme-card small { color: var(--text-muted); font-size: 11px; }
.theme-label { font-size: 13px; font-weight: 700; color: var(--text); }
.swatch { width: 100%; height: 34px; border-radius: 7px; border: 1px solid rgba(0, 0, 0, 0.05); }
.bg-preview { margin-top: 10px; }
.bg-preview img { width: 100%; height: 110px; object-fit: cover; border-radius: 10px; border: 1px solid var(--border); display: block; margin-bottom: 6px; }
.reset-btn { margin-top: 16px; }
</style>
