<template>
  <div style="max-width:360px;margin:80px auto;padding:32px;border:1px solid #ddd;border-radius:8px">
    <h2 style="text-align:center;margin-bottom:24px">门诊管理系统</h2>
    <div style="margin-bottom:16px">
      <label>用户名</label>
      <input v-model="form.username" type="text" placeholder="请输入用户名"
             style="width:100%;padding:8px;margin-top:4px;box-sizing:border-box;border:1px solid #ccc;border-radius:4px" />
    </div>
    <div style="margin-bottom:24px">
      <label>密码</label>
      <input v-model="form.password" type="password" placeholder="请输入密码"
             style="width:100%;padding:8px;margin-top:4px;box-sizing:border-box;border:1px solid #ccc;border-radius:4px" />
    </div>
    <button @click="handleLogin" :disabled="loading"
            style="width:100%;padding:10px;background:#1a73e8;color:#fff;border:none;border-radius:4px;cursor:pointer;font-size:16px">
      {{ loading ? '登录中...' : '登录' }}
    </button>
    <p v-if="error" style="color:red;margin-top:12px;text-align:center">{{ error }}</p>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '../api'

const router = useRouter()
const form = reactive({ username: '', password: '' })
const loading = ref(false)
const error = ref('')

async function handleLogin() {
  loading.value = true
  error.value = ''
  try {
    const res = await authApi.login(form)
    if (res.success) {
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('username', res.data.username)
      localStorage.setItem('role', res.data.role)
      router.push('/patients')
    } else {
      error.value = res.message
    }
  } catch {
    error.value = '网络错误，请稍后重试'
  } finally {
    loading.value = false
  }
}
</script>
