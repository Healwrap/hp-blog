<template>
  <Dialog
    :show="dialogConfig.show"
    :title="dialogConfig.title"
    :top="dialogConfig.top"
    :width="dialogConfig.width"
    :buttons="dialogConfig.buttons"
    :show-cancel="dialogConfig.showCancel"
    :show-close="dialogConfig.showClose"
    :draggable="dialogConfig.draggable"
    @close="close"
  >
    <el-form ref="formDataRef" :model="formData" :rules="rules" :label-width="80">
      <el-form-item label="消息内容" prop="message">
        <el-input v-model="formData.message" type="textarea" :rows="5" :maxlength="200" resize="none" show-word-limit />
      </el-form-item>
      <el-form-item label="积分" prop="integral">
        <el-input v-model="formData.integral" placeholder="可选，可为空或者负数" clearable />
      </el-form-item>
    </el-form>
  </Dialog>
</template>

<script setup>
import { getCurrentInstance, nextTick, reactive, ref } from 'vue'

const { proxy } = getCurrentInstance()
const formDataRef = ref(null)
const formData = ref({})
const rules = {
  message: [{ required: true, message: '请输入消息内容', trigger: 'blur' }]
}
const dialogConfig = reactive({
  show: false,
  title: '发送消息',
  showClose: false,
  width: '28%',
  draggable: true,
  buttons: [
    {
      type: 'primary',
      text: '确定',
      click: () => {
        submitForm()
      }
    }
  ]
})
// 提交表单
const emit = defineEmits(['reload'])
const submitForm = async () => {
  formDataRef.value.validate(async valid => {
    if (!valid) {
      return
    }
    const result = await proxy.$api.user.sendMessage(formData.value)
    if (!result) {
      return
    }
    close()
    proxy.$message.success('已发送')
    emit('reload')
  })
}
// 显示与关闭弹窗
const close = () => {
  dialogConfig.show = false
}
const show = data => {
  dialogConfig.show = true
  nextTick(() => {
    formDataRef.value.resetFields()
    formData.value = {
      userId: data.userId
    }
  })
}
defineExpose({
  show
})
</script>

<style scoped></style>
