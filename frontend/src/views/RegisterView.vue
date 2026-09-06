<template>
  <div class="auth-page">
    <div class="auth-card card">
      <h1>注册账号</h1>
      <p class="muted">注册后默认获得“玩家”角色</p>
      <el-form label-position="top">
        <el-form-item label="用户名（3~20位字母数字或下划线）"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="昵称"><el-input v-model="form.nickname" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" /></el-form-item>
        <el-form-item label="密码（至少8位）"><el-input v-model="form.password" type="password" show-password /></el-form-item>
        <el-button type="primary" style="width:100%" size="large" :loading="loading" @click="submit">注 册</el-button>
      </el-form>
      <p class="switch">已有账号？<router-link to="/login">去登录</router-link></p>
    </div>
  </div>
</template>
<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '../api/auth'
const router = useRouter()
const form = reactive({ username: '', nickname: '', email: '', password: '' })
const loading = ref(false)
async function submit() {
  if (!form.username || !form.nickname || !form.password) return ElMessage.warning('请填写完整')
  loading.value = true
  try { await register({ ...form }); ElMessage.success('注册成功，请登录'); router.push('/login') }
  finally { loading.value = false }
}
</script>
<style scoped>
.auth-page { min-height:100vh; display:flex; align-items:center; justify-content:center; background: linear-gradient(150deg,#e8f0fe,#f7f8fa); padding:20px; }
.auth-card { width: 400px; max-width:100%; padding: 26px 28px; }
.auth-card h1 { margin:0 0 6px; font-size:22px; }
.switch { text-align:center; margin-top:14px; }
</style>
