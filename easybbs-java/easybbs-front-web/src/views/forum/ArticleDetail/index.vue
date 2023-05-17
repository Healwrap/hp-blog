<template>
  <div class="article">
    <div class="article-detail" :style="{ width: proxy.store.getters.contentWidth - 300 + 'px' }">
      <!--顶部面包屑-->
      <div class="article-detail-info">
        <el-breadcrumb :separator-icon="ArrowRight">
          <el-breadcrumb-item v-if="articleInfo.pBoardId" :to="{ path: `/forum/${articleInfo.pBoardId}` }"
            >{{ articleInfo.pBoardName }}
          </el-breadcrumb-item>
          <el-breadcrumb-item v-if="articleInfo.boardId" :to="{ path: `/forum/${articleInfo.pBoardId}/${articleInfo.boardId}` }"
            >{{ articleInfo.boardName }}
          </el-breadcrumb-item>
          <el-breadcrumb-item>{{ articleInfo.title }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <!--文章内容-->
      <div class="article-detail-content block">
        <!--标题-->
        <div class="title">
          {{ articleInfo.title }}
        </div>
        <!--用户信息-->
        <div class="user-info">
          <user-avatar :user-id="articleInfo.userId" />
          <div class="user-info-detail">
            <div class="nick-name" @click="router.push(`/user/${articleInfo.userId}`)">{{ articleInfo.nickName }}</div>
            <div class="info">
              <span>{{ articleInfo.postTime }}</span>
              <span class="address">&nbsp;·&nbsp;{{ articleInfo.userIpAddress }}</span>
              <span class="iconfont icon-eye-solid" style="font-size: 12px; margin-left: 10px">
                {{ articleInfo.readCount === 0 ? '阅读' : articleInfo.readCount }}
              </span>
            </div>
          </div>
        </div>
        <!--文章内容-->
        <div id="detail" class="detail" v-html="articleInfo.content"></div>
      </div>
      <!--附件-->
      <div v-if="attachment" class="attachment-panel block" id="attachment">
        <div class="title">附件</div>
        <div class="attachment-info">
          <div class="iconfont icon-zip"></div>
          <div class="file-name">{{ attachment.fileName }}</div>
          <div class="size">{{ formatFileSize(attachment.fileSize) }}</div>
          <div>
            需要<span class="integral">{{ attachment.integral }}</span
            >积分
          </div>
          <div class="download-count">已下载{{ attachment.downloadCount }}次</div>
          <div class="download-btn">
            <el-button type="primary" size="small" @click="handleAttachmentDownload">下载</el-button>
          </div>
        </div>
      </div>
      <!--评论-->
      <div class="comment-panel block" id="comment">
        <CommentList
          v-if="Object.keys(articleInfo).length !== 0"
          :article-id="articleInfo.articleId"
          :article-userid="articleInfo.userId"
          @updateCommentCount="updateCommentCount"
        />
      </div>
    </div>
    <!--左侧快捷操作-->
    <div class="article-quick-panel" :style="{ left: quickPanelLeft + 'px' }">
      <!--点赞-->
      <el-badge :value="articleInfo.goodCount" type="info" :hidden="articleInfo.goodCount <= 0">
        <div class="quick-item" @click="handleGoodClick">
          <div class="iconfont icon-good" :style="{ color: havaLike ? '#409eff' : '' }"></div>
        </div>
      </el-badge>
      <!--评论-->
      <el-badge :value="articleInfo.commentCount" type="info" :hidden="articleInfo.commentCount <= 0">
        <div class="quick-item" @click="handleLeftPanelClick('#comment')">
          <div class="iconfont icon-comment"></div>
        </div>
      </el-badge>
      <!--附件-->
      <div class="quick-item" @click="handleLeftPanelClick('#attachment')">
        <div class="iconfont icon-zip"></div>
      </div>
    </div>
    <!--图片预览-->
    <ImageViewer ref="imageViewerRef" :image-list="previewImgList" />
  </div>
</template>

<script setup>
import { getCurrentInstance, nextTick, onMounted, ref, watch } from 'vue'
import { ArrowRight } from '@element-plus/icons-vue'
import { useRoute } from 'vue-router'
import router from '@/router'
import { formatFileSize } from '@/utils/Utils'
import forumApi from '@/api/forum'
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'
import hljs from 'highlight.js'
import 'highlight.js/styles/atom-one-light.css' // 样式文件
import CommentList from './components/CommentList.vue'

const { proxy } = getCurrentInstance()
const route = useRoute()
const imageViewerRef = ref(null)
const previewImgList = ref([])
const currentUserinfo = ref({})
const quickPanelLeft = (window.innerWidth - proxy.store.getters.contentWidth - 70) / 2
// 文章详情
const articleInfo = ref({})
// 附件
const attachment = ref({})
// 当前账号是否点赞了文章
const havaLike = ref(false)
// 点赞
const handleGoodClick = async () => {
  const result = await forumApi.doLike(articleInfo.value.articleId)
  if (!result) {
    return
  }
  havaLike.value = !havaLike.value
  if (havaLike.value) {
    articleInfo.value.goodCount++
  } else {
    articleInfo.value.goodCount--
  }
}
const handleAttachmentDownload = async () => {
  const result = await forumApi.getUserDownloadInfo(attachment.value.fileId)
  if (!result) {
    return
  }
  // 积分不够
  if (result.data.userIntegral < attachment.value.integral && currentUserinfo.value.userId !== articleInfo.value.userId) {
    proxy.Toast.warning('你的积分不够，无法下载')
    return
  }
  // 积分为0或者是自己的文章或者已经下载过了
  if (attachment.value.integral === 0 || currentUserinfo.value.userId === articleInfo.value.userId || result.data.havaDownload) {
    window.open(forumApi.attachmentDownload(attachment.value.fileId), '_blank')
    attachment.value.downloadCount++
    return
  }
  proxy.Confirm(`你还有${result.data.userIntegral}积分，当前下载会扣除${attachment.value.integral}`, () => {
    window.open(forumApi.attachmentDownload(attachment.value.fileId), '_blank')
    attachment.value.downloadCount++
  })
}
// 文章图片预览
const handleImagePreview = () => {
  nextTick(() => {
    const imageNodelist = document.querySelectorAll('#detail img')
    const imageList = []
    imageNodelist.forEach((item, index) => {
      imageList.push(item.src)
      item.style.cursor = 'pointer'
      item.style.width = '100%'
      item.style.height = 'auto'
      item.addEventListener('click', () => {
        imageViewerRef.value.show(index)
      })
    })
    previewImgList.value = imageList
  })
}
// 代码高亮
const highlightCode = () => {
  nextTick(() => {
    const blocks = document.querySelectorAll('pre code')
    blocks.forEach(block => {
      hljs.highlightBlock(block)
    })
  })
}
// 左侧快捷操作 点击
const handleLeftPanelClick = anchor => {
  document.querySelector(anchor).scrollIntoView(true)
}
// 更新评论数
const updateCommentCount = count => {
  articleInfo.value.commentCount = count
}
onMounted(async () => {
  let result = await forumApi.getArticleDetail(route.params.articleId)
  articleInfo.value = result.data.forumArticleVO
  attachment.value = result.data.forumArticleAttachmentVO
  havaLike.value = result.data.havaLike
  handleImagePreview()
  highlightCode()
})
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
.article {
  .article-detail {
    position: relative;
    padding: 10px;
    margin: 10px;

    .article-detail-info {
      margin-bottom: 20px;
    }

    .article-detail-content {
      padding: 10px;

      .title {
        font-size: 20px;
        font-weight: bold;
        margin-bottom: 10px;
      }

      .user-info {
        display: flex;
        align-items: center;
        padding: 10px 0;
        border-bottom: 1px solid #eee;

        .user-info-detail {
          margin-left: 10px;

          .nick-name {
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;

            &:hover {
              text-decoration: underline;
              color: #1e88e5;
            }
          }

          .info {
            margin-top: 5px;
            color: #999;
            font-size: 12px;
          }
        }
      }

      .detail {
        letter-spacing: 1.1px;
        line-height: 25px;

        img {
          width: 90%;
        }
      }
    }

    .attachment-panel {
      margin-top: 20px;
      padding: 10px;
      border: 1px solid #eee;

      .title {
        font-size: 16px;
        font-weight: bold;
        margin-bottom: 10px;
      }

      .attachment-info {
        display: flex;
        align-items: center;

        .iconfont {
          font-size: 30px;
          color: #1e88e5;
        }

        .file-name {
          margin-left: 10px;
          font-size: 16px;
          font-weight: bold;
        }

        .size {
          margin: 0 10px;
          font-size: 12px;
          color: #999;
        }

        .integral {
          font-size: 12px;
          color: #f56c6c;
        }

        .download-count {
          margin-left: 10px;
          font-size: 12px;
          color: #999;
        }

        .download-btn {
          margin-left: 10px;
        }
      }
    }

    .block {
      background: #fff;
      border-radius: 10px;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
      transition: all 0.3s;

      &:hover {
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.15);
      }
    }
  }

  .article-quick-panel {
    position: fixed;
    top: 50%;
    transform: translateY(-50%);
    display: flex;
    flex-direction: column;
    align-items: center;

    .quick-item {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      background: #fff;
      display: flex;
      justify-content: center;
      align-items: center;
      margin-bottom: 10px;
      cursor: pointer;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
      transition: all 0.3s;

      &:hover {
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
      }
    }
  }
}
</style>
