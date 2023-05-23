<template>
  <div class="avatar">
    <el-dropdown trigger="click">
      <user-avatar :user-id="userId" :size="size" :add-link="addLink" />
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="goToAccountCenter">我的主页</el-dropdown-item>
          <el-dropdown-item @click="handleLogout">退出</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script setup>
import { defineProps, getCurrentInstance } from 'vue'
import accountApi from '@/api/account'
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'
import router from '@/router'

const { proxy } = getCurrentInstance()
defineProps({
  userId: {
    type: String
  },
  addLink: {
    type: Boolean,
    default: false
  },
  size: {
    type: String,
    default: ''
  }
})
const handleLogout = () => {
  accountApi.logout().then(() => {
    proxy.$VueCookies.remove('loginInfo')
    proxy.$store.commit('UPDATE_USER_INFO', {})
    router.push('/')
  })
}
const goToAccountCenter = () => {
  router.push(`/user/${proxy.$store.getters.userId}`)
}
</script>

<style scoped></style>
