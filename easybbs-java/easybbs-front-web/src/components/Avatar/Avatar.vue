<template>
  <div class="avatar">
    <el-dropdown>
      <user-avatar :user-id="userId" :size="size" :add-link="addLink" />
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item>我的主页</el-dropdown-item>
          <el-dropdown-item @click="handleLogout">退出</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script setup>
import { defineProps, getCurrentInstance } from 'vue'
import userApi from '@/api/user'
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'

const { proxy } = getCurrentInstance()
defineProps({
  userId: {
    type: String
  },
  addLink: {
    type: Boolean,
    default: true
  },
  size: {
    type: String,
    default: ''
  }
})
const handleLogout = () => {
  userApi.logout().then(() => {
    proxy.VueCookies.remove('loginInfo')
    proxy.store.commit('UPDATE_USER_INFO', {})
  })
}
</script>

<style scoped></style>
