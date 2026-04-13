<template>
  <div>
    <nav v-if="isLoggedIn" style="padding:12px 24px;background:#1a73e8;display:flex;gap:16px;align-items:center">
      <span style="color:#fff;font-weight:bold;margin-right:auto">门诊管理系统</span>
      <router-link to="/patients" style="color:#fff;text-decoration:none">患者管理</router-link>
      <router-link to="/visits" style="color:#fff;text-decoration:none">就诊记录</router-link>
      <router-link to="/drugs" style="color:#fff;text-decoration:none">药品库存</router-link>
      <button @click="logout" style="background:transparent;border:1px solid #fff;color:#fff;padding:4px 12px;cursor:pointer;border-radius:4px">退出</button>
    </nav>
    <router-view style="padding:24px" />
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const isLoggedIn = computed(() => !!localStorage.getItem('token'))

function logout() {
  localStorage.clear()
  router.push('/login')
}
</script>
