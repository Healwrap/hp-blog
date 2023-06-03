<template>
  <div class="user-info-editor">
    <Dialog
      :show="dialogConfig.show"
      :title="dialogConfig.title"
      :width="dialogConfig.width"
      :buttons="dialogConfig.buttons"
      :show-close="dialogConfig.showClose"
      :draggable="dialogConfig.draggable"
      @close="close"
    >
      <el-form ref="formDataRef" :model="formData" :rules="rules">
        <el-form-item label="昵称" prop="nickName">
          {{ formData.nickName }}
        </el-form-item>
        <el-form-item label="头像" prop="avatar">
          <image-upload v-model:model-value="formData.avatar" type="avatar" />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-radio-group v-model="formData.sex">
            <el-radio :label="0">女</el-radio>
            <el-radio :label="1">男</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="formData.personDescription" type="textarea" :rows="5" :maxlength="150" resize="none" show-word-limit />
        </el-form-item>
      </el-form>
    </Dialog>
  </div>
</template>

<script setup>
// 弹窗配置
import { getCurrentInstance, nextTick, reactive, ref, watch } from 'vue'

const { proxy } = getCurrentInstance()
const props = defineProps({})
const formDataRef = ref()
const formData = ref({})
const rules = ref()
const dialogConfig = reactive({
  show: false,
  title: '编辑个人信息',
  showClose: false,
  width: '28%',
  draggable: false,
  buttons: [
    {
      type: 'primary',
      text: '确定',
      click: e => {
        updateUserInfoHandler()
      }
    }
  ]
})
const close = () => {
  dialogConfig.show = false
}
// 更新用户信息
const updateUserInfoHandler = () => {
  formDataRef.value.validate(async valid => {
    if (!valid) {
      return
    }
    let params = {}
    Object.assign(params, formData.value)
    const result = await proxy.$api.user.updateUserInfo(params.sex, params.personDescription, params.avatar)
    if (!result) {
      return
    }
    dialogConfig.show = false
    proxy.$router.go(0)
  })
}
// 显示编辑个人信息弹窗
const showEditUserInfoDialog = userInfo => {
  dialogConfig.show = true
  nextTick(() => {
    formDataRef.value.resetFields()
    const dataInfo = JSON.parse(JSON.stringify(userInfo))
    dataInfo.avatar = {
      userId: userInfo.userId
    }
    formData.value = dataInfo
  })
}
defineExpose({
  showEditUserInfoDialog
})
watch(
  () => formData.value.avatar,
  newVal => {},
  {
    immediate: true,
    deep: true
  }
)
</script>

<style lang="scss" scoped></style>
