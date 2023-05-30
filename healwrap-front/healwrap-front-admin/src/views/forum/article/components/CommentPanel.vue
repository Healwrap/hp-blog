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
    <div class="comment-list">
      <div v-for="item in commentList" :key="item" class="comment-item">
        <div class="p-comment-item">
          <div class="user-info">
            <user-avatar :user-id="item.userId" />
            <user-link :user-id="item.userId" :username="item.nickName" style="margin-left: 5px" />
          </div>
          <div class="comment-info">
            <div class="content" v-html="item.content"></div>
            <comment-image v-if="item.imagePath" :src="proxy.$api.files.getImage(item.imagePath)" />
          </div>
          <div class="post-info">
            <span>发布于：{{ item.postTime }}&nbsp;·&nbsp;{{ item.userIpAddress }}</span>
            <span>&nbsp;&nbsp;赞：{{ item.goodCount }}</span>
          </div>
          <div v-if="item.children">
            <div v-for="sub in item.children" :key="sub" class="s-comment-item">
              <div class="user-info">
                <user-avatar :user-id="sub.userId" size="small" />
                <user-link :user-id="sub.userId" :username="sub.nickName" style="margin-left: 5px" />
              </div>
              <div class="comment-info">
                <span class="reply-info" v-if="sub.replyUserId">
                  <span>回复</span>
                  <span class="ulink">@{{ sub.replyNickName }}：</span>
                  <span class="content" v-html="sub.content"></span>
                </span>
                <comment-image v-if="item.imagePath" :src="proxy.$api.files.getImage(sub.imagePath)" />
              </div>
              <div class="post-info">
                <span>发布于{{ sub.postTime }}&nbsp;·&nbsp;{{ sub.userIpAddress }}</span>
                <span>&nbsp;&nbsp;赞：{{ sub.goodCount }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="no-data">
      <el-empty v-if="commentList.length === 0" description="暂无评论" />
    </div>
  </Dialog>
</template>

<script setup>
import { getCurrentInstance, reactive, ref } from 'vue'
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'
import UserLink from '@/components/UserLink/UserLink.vue'
import CommentImage from '@/components/CommentImage/CommentImage.vue'

const { proxy } = getCurrentInstance()
const commentList = ref([])
const dialogConfig = reactive({
  show: false,
  title: '评论列表',
  showClose: false,
  top: '5vh',
  width: '28%',
  draggable: true,
  showCancel: false,
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
const show = async articleId => {
  dialogConfig.show = true
  // 加载评论列表
  const result = await proxy.$api.forum.loadComment4Article({ articleId })
  if (!result) {
    return
  }
  commentList.value = result.data
}
defineExpose({
  show
})
</script>

<style lang="scss" scoped>
.comment-list {
  .comment-item {
    @apply flex flex-col mb-1;
    .p-comment-item {
      .user-info {
        @apply flex items-center mb-2;
      }

      .comment-info {
        @apply flex flex-col;
        .content {
          @apply mb-2;
        }
      }

      .post-info {
        @apply flex mb-1.5 items-center text-xs text-gray-400;
      }

      .s-comment-item {
        @apply flex flex-col mb-4 ml-8;
        .user-info {
          @apply flex items-center mb-2;
        }

        .comment-info {
          @apply flex;
          .reply-info {
            @apply mb-2 text-gray-400 text-xs;
            .ulink {
              @apply text-blue-500;
            }
          }
        }

        .post-info {
          @apply flex items-center text-xs text-gray-400;
        }
      }
    }
  }
}
</style>
