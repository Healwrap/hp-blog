<template>
  <div class="avatar">
    <el-dropdown>
      <el-avatar :src="src" :size="size" @click="gotoAccountCenter">
        <!--加载失败头像-->
        <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="" />
      </el-avatar>
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
import router from '@/router'

const { proxy } = getCurrentInstance()
const props = defineProps({
  userId: {
    type: String
  },
  addLink: {
    type: Boolean,
    default: true
  },
  src: {
    type: String
  },
  size: {
    type: String,
    default: ''
  }
})
const gotoAccountCenter = () => {
  if (props.addLink) {
    router.push('/user/' + props.userId)
  }
}
const handleLogout = () => {
  userApi.logout().then(() => {
    proxy.VueCookies.remove('loginInfo')
    proxy.store.commit('UPDATE_USER_INFO', {})
  })
}
</script>

<style scoped></style>
