<template>
  <div v-if="articleId" class="comment-list">
    <div class="comment-title">
      <div class="title">评论<span class="count">0</span></div>
      <div class="tab">
        <span>热榜</span>
        <el-divider direction="vertical" />
        <span>最新</span>
      </div>
    </div>
    <div class="comment-form-panel">
      <UserAvatar size="default" :user-id="currentUserinfo.userId" />
      <div class="comment-form">
        <el-form ref="formDataRef" :model="formData" :rules="rules">
          <!--textarea输入-->
          <el-form-item prop="content">
            <el-input v-model="formData.content" placeholder="输入评论内容" type="textarea" rows="3" maxlength="800" resize="none" show-word-limit />
          </el-form-item>
        </el-form>
        <div class="comment-form-btn">
          <div v-if="currentUserinfo.userId" class="insert-img">
            <el-upload
              name="file"
              :show-file-list="false"
              accept=".png,.PNG,.jpg,.JPG,.jpeg,.JPEG,.gif,.GIF,.bmp,.BMP"
              :multiple="false"
              :http-request="selectImg"
            >
              <span class="iconfont icon-image"></span>
            </el-upload>
          </div>
          <el-button class="btn" type="primary" @click="handleCommentSubmit">发布</el-button>
        </div>
      </div>
    </div>
    <div class="comment-list-panel"></div>
  </div>
</template>

<script setup>
import { getCurrentInstance, ref, watch } from 'vue'
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'

const { proxy } = getCurrentInstance()
defineProps({
  articleId: {
    type: String
  },
  articleUserid: {
    type: String
  }
})
const currentUserinfo = ref({})
const formData = ref({
  content: ''
})
const formDataRef = ref(null)
const rules = {
  content: [
    { required: true, message: '请输入评论内容', trigger: 'blur' },
    { min: 1, max: 800, message: '评论内容长度在 1 到 800 个字符', trigger: 'blur' }
  ]
}
// 提交评论
const handleCommentSubmit = () => {
  // TODO
}
// 选择图片
const selectImg = () => {
  // TODO
}
watch(
  () => proxy.store.getters.userInfo,
  newVal => {
    currentUserinfo.value = newVal
  },
  {
    immediate: true,
    deep: true
  }
)
</script>

<style lang="scss" scoped>
.comment-list {
  margin-top: 20px;
  padding: 20px;

  .comment-title {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .title {
      font-size: 18px;
      font-weight: bold;

      .count {
        margin-left: 10px;
        font-size: 14px;
        color: #999;
      }
    }

    .tab {
      display: flex;
      align-items: center;

      span {
        font-size: 14px;
        color: #999;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          color: #333;
        }
      }
    }
  }

  .comment-form-panel {
    margin-top: 20px;
    display: flex;

    .comment-form {
      flex: 1;
      margin-left: 20px;

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
  }
}
</style>
