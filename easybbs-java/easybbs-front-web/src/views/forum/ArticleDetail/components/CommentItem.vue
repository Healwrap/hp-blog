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
          <el-dropdown>
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
      <slot name="postComment"></slot>
    </div>
    <div v-if="commentData.children" class="comment-sub-list">
      <div v-for="sub in commentData.children" :key="sub.commentId" class="comment-sub-item">
        <user-avatar class="avatar" size="small" :user-id="sub.userId" />
        <div class="right-panel">
          <div class="nick-name">
            <span class="name">{{ sub.nickName }}</span>
            <el-tag v-if="sub.userId === articleUserId" class="tag-author" effect="dark" size="small">作者</el-tag>
          </div>
          <div class="comment-content">
            <span v-html="sub.content"></span>
          </div>
          <div class="op-panel">
            <div class="time">
              <span>{{ sub.postTime }}</span>
              <span class="address"> &nbsp;·&nbsp;{{ sub.userIpAddress }} </span>
            </div>
            <div class="iconfont icon-good">&nbsp;{{ sub.goodCount > 0 ? sub.goodCount : '点赞' }}</div>
            <div class="iconfont icon-comment" @click="showReply(sub)">&nbsp;回复</div>
            <el-dropdown v-if="articleUserId === currentUserId">
              <div class="iconfont icon-more"></div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>{{ sub.topType === 0 ? '设为置顶' : '取消置顶' }}</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'

const props = defineProps({
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
const emit = defineEmits(['hideAllReply'])
const showReply = curData => {
  const temp = curData.showReply === undefined ? false : curData.showReply
  emit('hideAllReply')
  curData.showReply = !temp
}
</script>

<style lang="scss" scoped>
.comment-item {
  padding: 10px 0 0 0;

  .comment-info {
    display: flex;
    align-items: center;
    padding: 10px 0 10px 10px;
    margin-bottom: 10px;
    transition: all 0.3s;

    &:hover {
      background: #f0f2f5;
    }

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

  .comment-sub-list {
    margin-top: 10px;

    .comment-sub-item {
      display: flex;
      align-items: center;
      padding: 5px 0 5px 50px;
      transition: all 0.3s;

      &:hover {
        background: #f0f2f5;
      }

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
  }

  .reply-panel {
    margin-left: 20px;
  }
}
</style>
