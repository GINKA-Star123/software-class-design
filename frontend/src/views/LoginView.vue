<template>
  <div class="auth-page">
    <div class="auth-card card">
      <h1>欢迎回到抉择工坊</h1>
      <p class="muted">本阶段采用 Session 登录，登录状态由后端会话维护</p>
      <el-form @submit.prevent>
        <el-form-item><el-input v-model="form.username" size="large" placeholder="用户名" /></el-form-item>
        <el-form-item><el-input v-model="form.password" size="large" type="password" show-password placeholder="密码" @keyup.enter="submit" /></el-form-item>
        <el-button type="primary" size="large" style="width:100%" :loading="loading" @click="submit">登 录</el-button>
      </el-form>
      <p class="switch">还没有账号？<router-link to="/register">立即注册</router-link></p>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
const store = useUserStore()
const router = useRouter()
const route = useRoute()
const form = reactive({ username: '', password: '' })
const loading = ref(false)
async function submit() {
  if (!form.username || !form.password) return
  loading.value = true
  try { await store.login({ ...form }); router.push(route.query.redirect || '/') }
  finally { loading.value = false }
}
</script>
<style scoped>
.auth-page { min-height: 100vh; display:flex; align-items:center; justify-content:center; background: linear-gradient(150deg,#e8f0fe,#f7f8fa); padding: 20px; }
.auth-card { width: 380px; max-width: 100%; padding: 28px; }
.auth-card h1 { margin: 0 0 6px; font-size: 22px; }
.switch { text-align:center; margin-top: 16px; }
</style>
