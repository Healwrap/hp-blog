<template>
  <el-avatar :src="proxy.$api.account.avatarUrl(userId)" :size="size" style="cursor: pointer" @click="gotoAccountCenter" />
</template>

<script setup>
import { getCurrentInstance } from 'vue'

const { proxy } = getCurrentInstance()
const props = defineProps({
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
const gotoAccountCenter = () => {
  if (props.addLink) {
    // 跳转外部链接
    window.open(import.meta.env.VITE_APP_URL + '/user/' + props.userId)
  }
}
</script>

<style scoped></style>
