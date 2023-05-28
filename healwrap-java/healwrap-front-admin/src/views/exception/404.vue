<template>
  <el-result title="404" sub-title="Sorry, the page you visited does not exist.">
    <template #extra>
      <el-button type="primary" @click="toHome"> Back Home</el-button>
    </template>
  </el-result>
</template>

<script setup>
import { getCurrentInstance } from 'vue'

const { proxy } = getCurrentInstance()
const toHome = () => {
  proxy.$router.push('/')
}
</script>
