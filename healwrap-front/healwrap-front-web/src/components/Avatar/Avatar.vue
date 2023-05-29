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
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'

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
  proxy.$confirm('确定要退出吗？', () => {
    proxy.$api.account.logout().then(() => {
      proxy.$VueCookies.remove('loginInfo')
      proxy.$store.commit('UPDATE_USER_INFO', {})
      proxy.$router.go(0)
    })
  })
}
const goToAccountCenter = () => {
  proxy.$router.push(`/user/${proxy.$store.getters.userId}`)
}
</script>

<style scoped></style>
