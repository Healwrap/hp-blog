<template>
  <div class="user-info-editor">
    <Dialog
      :show="dialogConfig.show"
      :title="dialogConfig.title"
      :buttons="dialogConfig.buttons"
      :show-cancel="dialogConfig.showCancel"
      :show-close="dialogConfig.showClose"
    />
  </div>
</template>

<script setup>
// 弹窗配置
import { reactive } from 'vue'

const dialogConfig = reactive({
  show: false,
  title: '登录',
  showClose: true,
  width: '35%',
  top: '15vh',
  buttons: [],
  showCancel: false
})
</script>

<style lang="scss" scoped></style>
