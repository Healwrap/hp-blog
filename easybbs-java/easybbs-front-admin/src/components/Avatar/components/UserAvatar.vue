<template>
  <el-avatar :src="accountApi.avatarUrl(userId)" :size="size" style="cursor: pointer" @click="gotoAccountCenter" />
</template>

<script setup>
import router from '@/router'
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
    router.push('/user/' + props.userId)
  }
}
</script>

<style scoped></style>
