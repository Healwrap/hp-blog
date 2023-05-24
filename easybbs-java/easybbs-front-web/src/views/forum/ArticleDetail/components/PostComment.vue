<template>
  <div class="comment-form">
    <el-form ref="formDataRef" :model="formData" :rules="rules">
      <!--textarea输入-->
      <el-form-item prop="content">
        <el-input v-model="formData.content" :placeholder="placeholder" type="textarea" rows="2" maxlength="800" resize="none" show-word-limit />
      </el-form-item>
    </el-form>
    <div class="comment-form-btn">
      <!--<div v-if="currentUserinfo.userId" class="insert-img">-->
      <!--  <el-upload-->
      <!--    name="file"-->
      <!--    :show-file-list="false"-->
      <!--    accept=".png,.PNG,.jpg,.JPG,.jpeg,.JPEG,.gif,.GIF,.bmp,.BMP"-->
      <!--    :multiple="false"-->
      <!--    :http-request="selectImg"-->
      <!--  >-->
      <!--    <span class="iconfont icon-image"></span>-->
      <!--  </el-upload>-->
      <!--</div>-->
      <el-button class="btn" type="primary" size="small" @click="handleCommentSubmit">发布</el-button>
    </div>
  </div>
</template>

<script setup>
import { getCurrentInstance, ref } from 'vue'
import commentApis from '@/api/comment'

const { proxy } = getCurrentInstance()
const props = defineProps({
  articleId: {
    type: String
  },
  userId: {
    type: String
  },
  pCommentId: {
    type: String
  },
  replyUserId: {
    type: String
  },
  placeholder: {
    type: String,
    default: '输入评论内容'
  }
})
const formData = ref({
  content: ''
})
const checkPostComment = (rule, value, callback) => {
  if (value == null && formData.value.image == null) {
    callback(new Error('请输入评论内容'))
  } else {
    callback()
  }
}
const formDataRef = ref(null)
const rules = {
  content: [
    { required: true, message: '请输入评论内容', trigger: 'blur', validator: checkPostComment },
    { min: 3, max: 800, message: '评论内容长度在 3 到 800 个字符', trigger: 'blur' }
  ]
}
// 提交评论
const emit = defineEmits(['postCommentFinish'])
const handleCommentSubmit = () => {
  formDataRef.value.validate(async valid => {
    if (!valid) {
      return
    }
    const result = await commentApis.postComment(props.articleId, props.pCommentId, props.replyUserId, formData.value.content)
    if (!result) {
      return
    }
    proxy.$Toast.success('评论成功')
    formDataRef.value.resetField()
    emit('postCommentFinish', result.data)
  })
}
// 选择图片
const selectImg = () => {
  // TODO
}
</script>

<style lang="scss" scoped>
.comment-form {
  flex: 1;

  .comment-form-btn {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-top: 20px;

    .insert-img {
      flex: 1;
      display: flex;
      align-items: center;

      .icon-image {
        font-size: 20px;
        color: #999;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          color: #1e88e5;
        }
      }
    }
  }
}
</style>
