<template>
  <el-icon v-if="icon" :size="size">
    <component :is="icon" />
  </el-icon>
</template>

<script setup>
defineProps({
  icon: {
    type: String,
    required: true
  },
  size: {
    type: Number,
    default: 16
  }
})
</script>

<style scoped></style>
