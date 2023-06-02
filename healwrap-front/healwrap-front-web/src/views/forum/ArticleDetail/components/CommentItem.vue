<template>
  <div class="comment-item">
    <div class="comment-info">
      <div class="inner">
        <div class="user-info">
          <user-avatar class="avatar" :user-id="commentData.userId" />
          <div class="nick-name">
            <span class="name">{{ commentData.nickName }}</span>
            <el-tag v-if="commentData.userId === articleUserId" class="tag-author" effect="dark" size="small">作者 </el-tag>
          </div>
        </div>
        <div class="bottom">
          <div class="comment-content">
            <div class="content">
              <el-tag v-if="commentData.topType === 1" size="small">置顶</el-tag>&nbsp;
              <el-tag v-if="commentData.status === 0" size="small" type="warning">待审核</el-tag>&nbsp;
              <span class="content" v-html="commentData.content"></span>
            </div>
            <comment-image
              v-if="commentData.imgPath"
              :src="filesApi.getImage(commentData.imgPath.replace('.', '_.'))"
              :preview-img="[filesApi.getImage(commentData.imgPath)]"
            />
          </div>
          <div class="op-panel">
            <div class="time">
              <span>{{ commentData.postTime }}</span>
              <span class="address"> &nbsp;·&nbsp;{{ commentData.userIpAddress }} </span>
            </div>
            <div class="iconfont icon-good" @click="commentDoLike(commentData)">
              &nbsp;{{ commentData.goodCount > 0 ? commentData.goodCount : '点赞' }}
            </div>
            <div class="iconfont icon-comment" @click="showReply(commentData, 0)">&nbsp;回复</div>
            <el-dropdown v-if="articleUserId === currentUserId" trigger="click">
              <div class="iconfont icon-more"></div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="changeTopType(commentData)">
                    {{ commentData.topType === 0 ? '设为置顶' : '取消置顶' }}
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>
    </div>
    <div v-if="commentData.showReply" class="reply-panel">
      <slot name="postComment"></slot>
    </div>
    <div v-if="commentData.children" class="comment-sub-list">
      <div v-for="sub in commentData.children" :key="sub.commentId" class="comment-sub-item">
        <div class="inner">
          <div class="user-info">
            <user-avatar class="avatar" size="small" :user-id="sub.userId" />
            <div class="nick-name">
              <span class="name">{{ sub.nickName }}</span>
              <el-tag v-if="sub.userId === articleUserId" class="tag-author" effect="dark" size="small">作者</el-tag>
            </div>
          </div>
          <div class="bottom">
            <div class="comment-content">
              <span v-if="commentData.children.some(obj => obj.commentId === sub.pCommentId)"
                >回复 <user-link :username="sub.replyNickName" :user-id="sub.replyUserId" /> :
              </span>
              <span v-html="sub.content"></span>
              <comment-image v-if="sub.imgPath" src="sub.imgPath" />
            </div>
            <div class="op-panel">
              <div class="time">
                <span>{{ sub.postTime }}</span>
                <span class="address"> &nbsp;·&nbsp;{{ sub.userIpAddress }} </span>
              </div>
              <div class="iconfont icon-good">&nbsp;{{ sub.goodCount > 0 ? sub.goodCount : '点赞' }}</div>
              <div class="iconfont icon-comment" @click="showReply(sub, 1)">&nbsp;回复</div>
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
        <div v-if="sub.showReply" class="reply-panel">
          <PostComment
            :placeholder="placeholderInfo"
            :article-id="sub.articleId"
            :user-id="currentUserId"
            :reply-user-id="sub.userId"
            @post-comment-finish="postChildCommentFinish"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'
import PostComment from '@/views/forum/ArticleDetail/components/PostComment.vue'
import UserLink from '@/components/UserLink/UserLink.vue'
import CommentImage from '@/views/forum/ArticleDetail/components/CommentImage.vue'
import filesApi from '@/api/files'
import commentApis from '@/api/comment'
import { ref } from 'vue'

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
const pCommentId = ref(0)
const replyUserId = ref(0)
const placeholderInfo = ref(null)
// 是否点赞过
const havaLike = ref(false)
const emit = defineEmits(['hideAllReply', 'reloadData'])
const showReply = (curData, type) => {
  const temp = curData.showReply === undefined ? false : curData.showReply
  emit('hideAllReply')
  curData.showReply = !temp
  if (type === 0) {
    props.commentData.showReply = !temp
    pCommentId.value = curData.commentId
  } else {
    props.commentData.showReply = true
  }
  pCommentId.value = props.commentData.commentId
  replyUserId.value = curData.userId
  placeholderInfo.value = `回复 @ ${curData.nickName}`
}
// 置顶
const changeTopType = async curData => {
  const result = await commentApis.changeTopType(curData.commentId, curData.topType === 0 ? 1 : 0)
  if (!result) {
    return
  }
  emit('reloadData')
}
// 评论点赞
const commentDoLike = async commentData => {
  const result = await commentApis.doLike(commentData.commentId)
  if (!result || result.code === 901) {
    return
  }
  commentData.goodCount++
}
const postChildCommentFinish = data => {
  props.commentData.children.push(data)
}
</script>

<style lang="scss" scoped>
.comment-item {
  @apply pt-[10px];
  border-bottom: 1px solid #c7c7c7;

  .comment-info {
    @apply flex items-center p-[10px] pr-[0] mb-[10px];
    transition: all 0.3s;

    &:hover {
      @apply bg-[#f0f2f5];
    }

    .inner {
      @apply ml-[10px];

      .user-info {
        @apply flex items-center;

        .avatar {
          @apply mr-[10px];
        }

        .nick-name {
          @apply flex items-center;

          .name {
            @apply text-[14px] font-bold;
          }

          .tag-author {
            @apply ml-[10px];
          }
        }
      }

      .bottom {
        @apply my-[5px] ml-[50px];

        .comment-content {
          @apply text-[14px] leading-[1.5] break-all;

          .content {
            @apply py-[7px];
          }
        }

        .op-panel {
          @apply flex items-center mt-[10px];

          .time {
            @apply text-[12px] text-[#999];

            .address {
              @apply text-[#999];
            }
          }

          .iconfont {
            @apply ml-[10px] text-[14px] text-[#999] cursor-pointer;
            transition: all 0.3s;

            &:hover {
              @apply text-[#409eff];
            }
          }
        }
      }
    }
  }

  .comment-sub-list {
    @apply mt-[10px];

    .comment-sub-item {
      @apply py-[5px] pl-[50px];
      transition: all 0.3s;

      &:hover {
        @apply bg-[#f0f2f5];
      }

      .inner {
        @apply ml-[10px];

        .user-info {
          @apply flex items-center;

          .avatar {
            @apply mr-[10px];
          }

          .nick-name {
            @apply flex items-center;

            .name {
              @apply text-[14px] font-bold;
            }

            .tag-author {
              @apply ml-[10px];
            }
          }
        }

        .bottom {
          @apply my-[5px] ml-[35px];

          .comment-content {
            @apply text-[14px] leading-[1.5] break-all;

            .content {
              @apply py-[7px];
            }
          }

          .op-panel {
            @apply flex items-center mt-[10px];

            .time {
              @apply text-[12px] text-[#999];

              .address {
                @apply text-[#999];
              }
            }

            .iconfont {
              @apply ml-[10px] text-[14px] text-[#999] cursor-pointer;
              transition: all 0.3s;

              &:hover {
                @apply text-[#409eff];
              }
            }
          }
        }
      }

      .reply-panel {
        @apply m-[10px];
      }
    }
  }

  .reply-panel {
    @apply ml-[20px];
  }
}
</style>
