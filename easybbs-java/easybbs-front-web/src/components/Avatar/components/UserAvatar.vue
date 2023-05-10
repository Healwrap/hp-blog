<template>
  <el-avatar :src="userApi.avatarUrl(userId)" :size="size" style="cursor: pointer" @click="gotoAccountCenter" />
</template>

<script setup>
import router from '@/router'
import userApi from '@/api/user'

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
    router.push('/user/' + props.userId)
  }
}
</script>

<style scoped></style>
