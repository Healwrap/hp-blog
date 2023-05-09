<template>
  <div class="comment-item">
    <div class="comment-info">
      <user-avatar class="avatar" :user-id="commentData.userId" />
      <div class="right-panel">
        <div class="nick-name">
          <span class="name">{{ commentData.nickName }}</span>
          <el-tag v-if="commentData.userId === articleUserId" class="tag-author" effect="dark" size="small">作者 </el-tag>
        </div>
        <div class="comment-content">
          <span v-html="commentData.content"></span>
        </div>
        <div class="op-panel">
          <div class="time">
            <span>{{ commentData.postTime }}</span>
            <span class="address"> &nbsp;·&nbsp;{{ commentData.userIpAddress }} </span>
          </div>
          <div class="iconfont icon-good">&nbsp;{{ commentData.goodCount > 0 ? commentData.goodCount : '点赞' }}</div>
          <div class="iconfont icon-comment" @click="showReply(commentData)">&nbsp;回复</div>
          <el-dropdown v-if="articleUserId === currentUserId">
            <div class="iconfont icon-more"></div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>{{ commentData.topType === 0 ? '设为置顶' : '取消置顶' }}</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    <div v-if="commentData.showReply" class="reply-panel">
      <post-comment :user-id="currentUserId" :p-comment-id="pCommentId" :reply-user-id="replyUserId" @post-comment="postComment" />
    </div>
  </div>
</template>

<script setup>
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'
import PostComment from '@/views/forum/ArticleDetail/components/PostComment.vue'
import { ref } from 'vue'

defineProps({
  commentData: {
    type: Object
  },
  articleUserId: {
    type: String
  },
  currentUserId: {
    type: String
  }
})
const pCommentId = ref('')
const replyUserId = ref('')
const emit = defineEmits(['hideAllReply'])
const showReply = curData => {
  const temp = curData.showReply === undefined ? false : curData.showReply
  emit('hideAllReply')
  curData.showReply = !temp
  pCommentId.value = curData.commentId
}
const postComment = () => {
  console.log('postComment')
}
</script>

<style lang="scss" scoped>
.comment-item {
  padding: 10px 0;

  .comment-info {
    display: flex;
    align-items: center;
    margin-left: 20px;
    padding-bottom: 10px;
    margin-bottom: 10px;
    border-bottom: 1px solid #ebeef5;

    .right-panel {
      margin-left: 10px;

      .nick-name {
        display: flex;
        align-items: center;

        .name {
          font-size: 14px;
          font-weight: 500;
        }

        .tag-author {
          margin-left: 10px;
        }
      }

      .comment-content {
        margin-top: 5px;
        font-size: 14px;
        line-height: 1.5;
        word-break: break-all;
      }

      .op-panel {
        display: flex;
        align-items: center;
        margin-top: 10px;

        .time {
          font-size: 12px;
          color: #999;

          .address {
            color: #999;
          }
        }

        .iconfont {
          margin-left: 10px;
          font-size: 14px;
          color: #999;
          transition: all 0.3s;
          cursor: pointer;

          &:hover {
            color: #409eff;
          }
        }
      }
    }
  }

  .reply-panel {
    margin-left: 20px;
  }
}
</style>
