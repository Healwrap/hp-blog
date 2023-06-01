<template>
  <div class="settings-panel">
    <el-form ref="formDataRef" :model="formData" :rules="rules" label-width="160px">
      <el-divider content-position="left">用户注册欢迎消息</el-divider>
      <el-row>
        <el-col :span="24">
          <el-form-item label="欢迎消息" prop="registerWelcome">
            <el-input v-model="formData.registerWelcome" placeholder="输入欢迎消息内容" maxlength="250" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">邮件设置</el-divider>
      <el-row>
        <el-col :span="24">
          <el-form-item label="邮件标题" prop="emailTitle">
            <el-input v-model="formData.emailTitle" placeholder="输入邮件标题" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="邮件内容" prop="emailContent">
            <el-input v-model="formData.emailContent" type="textarea" placeholder="输入邮件内容验证码使用%s占位符" maxlength="300" show-word-limit />
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">审核设置</el-divider>
      <el-row>
        <el-col :span="12">
          <el-form-item label="帖子是否需要审核" prop="postAudit">
            <el-radio-group v-model="formData.postAudit">
              <el-radio :label="false">无需审核</el-radio>
              <el-radio :label="true">需要审核</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评论是否需要审核" prop="commentAudit">
            <el-radio-group v-model="formData.commentAudit">
              <el-radio :label="false">无需审核</el-radio>
              <el-radio :label="true">需要审核</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">发帖设置</el-divider>
      <el-row>
        <el-col :span="12">
          <el-form-item label="发帖积分" prop="postIntegral">
            <el-input v-model="formData.postIntegral" placeholder="输入发帖可以获取的积分" />
          </el-form-item>
          <el-form-item label="每天可发帖的数量" prop="postDayCountThreshold">
            <el-input v-model="formData.postDayCountThreshold" placeholder="输入每天可发帖的数量" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="上传图片数量" prop="postUploadImageCount">
            <el-input v-model="formData.postUploadImageCount" placeholder="输入可上传图片的数量" />
          </el-form-item>
          <el-form-item label="上传附件大小" prop="postAttachmentSize">
            <el-input v-model="formData.postAttachmentSize" placeholder="输入可上传附件的大小">
              <template #append>MB</template>
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">评论设置</el-divider>
      <el-row>
        <el-col :span="24">
          <el-form-item label="是否开启评论" prop="commentEnable">
            <el-switch v-model="formData.commentEnable" active-color="#13ce66" inactive-color="#ff4949" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="评论积分" prop="commentIntegral">
            <el-input v-model="formData.commentIntegral" placeholder="输入评论可以获取的积分" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="评论每天可获取的数量" prop="commentDayCountThreshold">
            <el-input v-model="formData.commentDayCountThreshold" placeholder="输入评论每天可获取的数量" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">其他设置</el-divider>
      <el-row>
        <el-col :span="12">
          <el-form-item label="每天可点赞的数量" prop="likeDayCountThreshold">
            <el-input v-model="formData.likeDayCountThreshold" placeholder="输入每天可点赞的数量" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item>
        <el-button type="primary" @click="submit">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { getCurrentInstance, ref } from 'vue'

const { proxy } = getCurrentInstance()
const formDataRef = ref(null)
const formData = ref({})
const rules = {
  registerWelcome: [{ required: true, message: '请输入欢迎消息内容', trigger: 'blur' }],
  emailTitle: [{ required: true, message: '请输入邮件标题', trigger: 'blur' }],
  emailContent: [{ required: true, message: '请输入邮件内容', trigger: 'blur' }],
  postAudit: [{ required: true, message: '请选择是否需要审核', trigger: 'blur' }],
  commentAudit: [{ required: true, message: '请选择是否需要审核', trigger: 'blur' }],
  postIntegral: [
    { required: true, message: '请输入帖子积分', trigger: 'blur' },
    { validator: proxy.$Verify.number, message: '请输入数字', trigger: 'blur' }
  ],
  postDayCountThreshold: [
    { required: true, message: '请输入帖子每日发布数量阈值', trigger: 'blur' },
    { validator: proxy.$Verify.number, message: '请输入数字', trigger: 'blur' }
  ],
  postUploadImageCount: [
    { required: true, message: '请输入帖子上传图片数量', trigger: 'blur' },
    { validator: proxy.$Verify.number, message: '请输入数字', trigger: 'blur' }
  ],
  postAttachmentSize: [
    { required: true, message: '请输入附件大小', trigger: 'blur' },
    { validator: proxy.$Verify.number, message: '请输入数字', trigger: 'blur' }
  ],
  commentEnable: [{ required: true, message: '请选择是否开启评论', trigger: 'blur' }],
  commentIntegral: [
    { required: true, message: '请输入评论积分', trigger: 'blur' },
    { validator: proxy.$Verify.number, message: '请输入数字', trigger: 'blur' }
  ],
  commentDayCountThreshold: [
    { required: true, message: '请输入评论每日发布数量阈值', trigger: 'blur' },
    { validator: proxy.$Verify.number, message: '请输入数字', trigger: 'blur' }
  ],
  likeDayCountThreshold: [
    { required: true, message: '请输入点赞每日发布数量阈值', trigger: 'blur' },
    { validator: proxy.$Verify.number, message: '请输入数字', trigger: 'blur' }
  ]
}
// 获取系统设置
const getSettings = async () => {
  const result = await proxy.$api.setting.getSetting()
  if (!result) {
    return
  }
  let data = result.data
  for (let item in data) {
    let subData = data[item]
    if (subData) {
      for (let subItem in subData) {
        formData.value[subItem] = subData[subItem]
      }
    }
  }
}
getSettings()
// 提交系统设置
const submit = async () => {
  formDataRef.value.validate(async valid => {
    if (!valid) {
      return
    }
    const result = await proxy.$api.setting.saveSetting(formData.value)
    if (!result) {
      return
    }
    proxy.$message.success('保存成功')
  })
}
</script>

<style lang="scss" scoped>
.settings-panel {
  @apply h-[calc(100vh-110px)] px-[20px] py-[10px] overflow-auto;
}
</style>
