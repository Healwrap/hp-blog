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
          <el-dropdown-item>退出</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'
import router from '@/router'

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
</script>

<style scoped></style>
