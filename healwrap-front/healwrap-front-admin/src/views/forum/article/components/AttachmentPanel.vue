<template>
  <Dialog
    :show="dialogConfig.show"
    :title="dialogConfig.title"
    :width="dialogConfig.width"
    :buttons="dialogConfig.buttons"
    :show-cancel="dialogConfig.showCancel"
    :show-close="dialogConfig.showClose"
    :draggable="dialogConfig.draggable"
    @close="close"
  >
    <el-form ref="formDataRef" :model="formData">
      <el-form-item label="发布人">
        <user-avatar :user="formData.userId" />
        <user-link :user="formData.userId" :username="formData.nickName" style="margin-left: 10px" />
      </el-form-item>
      <el-form-item label="文件名">
        {{ formData.fileName }}
      </el-form-item>
      <el-form-item label="文件大小">
        {{ formatFileSize(formData.fileSize) }}
      </el-form-item>
      <el-form-item label="下载所需积分">
        {{ formData.integral }}
      </el-form-item>
      <el-form-item label="下载次数">
        {{ formData.downloadCount }}
      </el-form-item>
      <el-form-item label="下载">
        <Alink :src="`/api/forum/attachmentDownload?fileId=${formData.fileId}`" :is-outer="true" content="下载" />
      </el-form-item>
    </el-form>
  </Dialog>
</template>

<script setup>
import { getCurrentInstance, reactive, ref } from 'vue'
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'
import UserLink from '@/components/UserLink/UserLink.vue'
import { formatFileSize } from '@/utils/Utils'

const { proxy } = getCurrentInstance()
const formDataRef = ref(null)
const formData = ref({})
const dialogConfig = reactive({
  show: false,
  title: '附件信息',
  showClose: false,
  showCancel: false,
  width: '28%',
  draggable: true,
  buttons: [
    {
      type: 'primary',
      text: '确定',
      click: () => {
        close()
      }
    }
  ]
})
// 显示与关闭弹窗
const close = () => {
  dialogConfig.show = false
}
const show = async (nickName, articleId) => {
  dialogConfig.show = true
  const result = await proxy.$api.forum.getAttachment(articleId)
  if (!result) {
    return
  }
  formData.value = result.data
  formData.value.nickName = nickName
}
defineExpose({
  show
})
</script>

<style scoped></style>
