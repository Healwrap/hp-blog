<template>
  <el-avatar :src="accountApi.avatarUrl(userId)" :size="size" style="cursor: pointer" @click="gotoAccountCenter" />
</template>

<script setup>
import accountApi from '@/api/account'

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
