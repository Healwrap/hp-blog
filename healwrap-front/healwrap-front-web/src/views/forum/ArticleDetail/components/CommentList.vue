<template>
  <div v-if="articleId" class="comment-list">
    <div class="comment-title">
      <div class="title">
        评论<span class="count">{{ commentListInfo.totalCount }}</span>
      </div>
      <div class="tab">
        <span :style="`color: ${orderType === 0 ? '#1e88e5' : '#999'}`" @click="changeOrderType(0)">热榜</span>
        <el-divider direction="vertical" />
        <span :style="`color: ${orderType === 1 ? '#1e88e5' : '#999'}`" @click="changeOrderType(1)">最新</span>
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
    <div class="comment-list-panel">
      <data-list :data-source="commentListInfo" :loading="loading" ikey="commentId" desc="暂无评论" @load-data="loadComment">
        <template #default="{ data }">
          <CommentItem
            :comment-data="data"
            :article-user-id="articleUserid"
            :current-user-id="currentUserinfo.userId"
            @hide-all-reply="hideAllReplyHandler"
            @reload-data="loadComment"
          >
            <template #postComment>
              <PostComment
                :article-id="articleId"
                :user-id="currentUserinfo.userId"
                :p-comment-id="data.commentId"
                :reply-user-id="data.userId"
                @post-comment-finish="postCommentFinish"
              />
            </template>
          </CommentItem>
        </template>
      </data-list>
    </div>
  </div>
</template>

<script setup>
import { getCurrentInstance, ref, watch } from 'vue'
import commentApis from '@/api/comment'
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'
import CommentItem from '@/views/forum/ArticleDetail/components/CommentItem.vue'
import DataList from '@/components/DataList/DataList.vue'
import PostComment from '@/views/forum/ArticleDetail/components/PostComment.vue'

const { proxy } = getCurrentInstance()
const props = defineProps({
  articleId: {
    type: String
  },
  articleUserid: {
    type: String
  }
})
const currentUserinfo = ref({})
// 评论列表
const commentListInfo = ref({})
// 排序
const orderType = ref(0)
// 加载
const loading = ref(false)
const formData = ref({
  content: ''
})
const formDataRef = ref(null)
const rules = {
  content: [
    { required: true, message: '请输入评论内容', trigger: 'blur' },
    { min: 3, max: 800, message: '评论内容长度在 3 到 800 个字符', trigger: 'blur' }
  ]
}
// 提交评论
const emit = defineEmits(['updateCommentCount'])
const handleCommentSubmit = () => {
  // TODO
  formDataRef.value.validate(async valid => {
    if (!valid) {
      return
    }
    const result = await commentApis.postComment(props.articleId, 0, 0, formData.value.content)
    if (!result) {
      return
    }
    proxy.$message.success('评论成功')
    commentListInfo.value.list.unshift(result.data)
    // 更新数量
    commentListInfo.value.totalCount++
    // 重置表单
    formDataRef.value.resetFields()
    emit('updateCommentCount', commentListInfo.value.totalCount)
  })
}
// 评论排序
const changeOrderType = type => {
  orderType.value = type
  loadComment()
}
// 选择图片
const selectImg = () => {
  // TODO
}
// 隐藏所有回复
const hideAllReplyHandler = () => {
  // TODO
  commentListInfo.value.list.forEach(item => {
    item.showReply = false
  })
}
// 加载评论列表
const loadComment = async () => {
  loading.value = true
  const result = await commentApis.loadComment(props.articleId, commentListInfo.value.pageNo, orderType.value)
  if (!result) {
    return
  }
  commentListInfo.value = result.data
  loading.value = false
}
loadComment()
const postCommentFinish = data => {
  commentListInfo.value.list.unshift(data)
}
watch(
  () => proxy.$store.getters.userInfo,
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
  @apply mt-[10px] p-[20px];

  .comment-title {
    @apply flex justify-between items-center;

    .title {
      @apply text-[18px] font-bold;

      .count {
        @apply ml-[10px] text-[14px] text-[#999];
      }
    }

    .tab {
      @apply flex items-center;

      span {
        @apply text-[14px] text-[#999] cursor-pointer;
        transition: all 0.3s;

        &:hover {
          @apply text-[#32c5ff];
        }
      }
    }
  }

  .comment-form-panel {
    @apply flex mt-[20px];

    .comment-form {
      @apply flex-1 ml-[20px];

      .comment-form-btn {
        @apply flex justify-end items-center mt-[20px];

        .insert-img {
          @apply flex flex-1 items-center;

          .icon-image {
            @apply text-[20px] text-[#999] cursor-pointer;
            transition: all 0.3s;

            &:hover {
              @apply text-[#1e88e5];
            }
          }
        }
      }
    }
  }
}
</style>
