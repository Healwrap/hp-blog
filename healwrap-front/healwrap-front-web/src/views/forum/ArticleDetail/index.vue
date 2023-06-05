<template>
  <transition enter-active-class="animate__animated animate__fadeIn" mode="in-out">
    <div v-if="Object.keys(articleInfo).length !== 0" class="article">
      <!--文章目录-->
      <div class="toc-panel animate__animated animate__fadeInRight" v-if="proxy.$store.getters.contentWidth !== '100vw'">
        <div class="toc-container">
          <div class="toc-title">目录</div>
          <div class="toc-list">
            <div v-if="tocList.length === 0" class="no-toc">未解析到目录</div>
            <div v-else class="toc">
              <div
                v-for="toc in tocList"
                :key="toc"
                :class="['toc-item', anchorId === toc.id ? 'active' : '']"
                :style="{ 'padding-left': toc.level * 10 + 'px', 'font-size': 18 - toc.level * 2 + 'px', 'font-weight': 900 - toc.level * 100 }"
                @click="gotoAnchor(toc.id)"
              >
                {{ toc.title }}
              </div>
            </div>
          </div>
        </div>
      </div>
      <!--左侧快捷操作-->
      <div class="article-quick-panel animate__animated animate__fadeInLeft">
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
      <div
        class="article-detail"
        :style="{
          display: `${proxy.$store.getters.isMobile === true ? 'block' : 'inline-block'}`,
          width: `${proxy.$store.getters.isMobile === true ? '' : parseInt(proxy.$store.getters.contentWidth) - 300 + 'px'}`
        }"
      >
        <!--顶部面包屑-->
        <div class="article-detail-info">
          <el-breadcrumb :separator-icon="ArrowRight" style="height: 20px">
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
            <el-tag v-if="articleInfo.status === 0">待审核</el-tag>
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
                <router-link v-if="articleInfo.userId === currentUserinfo.userId" class="edit-btn" :to="`/postArticle/${articleInfo.articleId}`">
                  <span class="iconfont icon-edit">&nbsp;编辑</span>
                </router-link>
              </div>
            </div>
          </div>
          <!--文章内容-->
          <div id="detail" v-katex class="detail" v-html="articleInfo.content"></div>
        </div>
        <!--附件-->
        <div v-if="attachment" id="attachment" class="attachment-panel block">
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
        <div id="comment" class="comment-panel block">
          <CommentList
            v-if="Object.keys(articleInfo).length !== 0"
            :article-id="articleInfo.articleId"
            :article-userid="articleInfo.userId"
            @update-comment-count="updateCommentCount"
          />
        </div>
      </div>
      <!--图片预览-->
      <ImageViewer ref="imageViewerRef" :image-list="previewImgList" />
    </div>
  </transition>
</template>

<script setup>
import { getCurrentInstance, nextTick, onMounted, onUnmounted, ref, watch } from 'vue'
import { ArrowRight } from '@element-plus/icons-vue'
import { useRoute } from 'vue-router'
import router from '@/router'
import { formatFileSize } from '@/utils/Utils'
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'
import hljs from 'highlight.js'
import 'highlight.js/styles/atom-one-light.css' // 样式文件
import CommentList from './components/CommentList.vue'

const { proxy } = getCurrentInstance()
const route = useRoute()
const imageViewerRef = ref(null)
const previewImgList = ref([])
const currentUserinfo = ref({})
const contentWitdh = ref(0)
// 文章详情
const articleInfo = ref({})
// 附件
const attachment = ref({})
// 当前账号是否点赞了文章
const havaLike = ref(false)
// 目录
const tocList = ref([])
//
const anchorId = ref(null)
// 点赞
const handleGoodClick = async () => {
  const result = await proxy.$api.forum.doLike(articleInfo.value.articleId)
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
// 处理下载附件
const handleAttachmentDownload = async () => {
  const result = await proxy.$api.forum.getUserDownloadInfo(attachment.value.fileId)
  if (!result) {
    return
  }
  // 积分不够
  if (result.data.userIntegral < attachment.value.integral && currentUserinfo.value.userId !== articleInfo.value.userId) {
    proxy.$message.warning('你的积分不够，无法下载')
    return
  }
  // 积分为0或者是自己的文章或者已经下载过了
  if (attachment.value.integral === 0 || currentUserinfo.value.userId === articleInfo.value.userId || result.data.havaDownload) {
    window.open(proxy.$api.forum.attachmentDownload(attachment.value.fileId), '_blank')
    attachment.value.downloadCount++
    return
  }
  proxy.$confirm(`你还有${result.data.userIntegral}积分，当前下载会扣除${attachment.value.integral}`, () => {
    window.open(proxy.$api.forum.attachmentDownload(attachment.value.fileId), '_blank')
    attachment.value.downloadCount++
  })
}
// 文章图片预览
const handleImagePreview = () => {
  nextTick(() => {
    // 处理图片预览
    const imageNodelist = document.querySelectorAll('#detail img')
    const imageList = []
    imageNodelist.forEach((item, index) => {
      imageList.push(item.src)
      item.style.cursor = 'pointer'
      item.style.width = '100%'
      item.style.height = 'auto'
      item.style.maxHeight = '600px'
      item.style.objectFit = 'contain'
      item.onclick = () => {
        imageViewerRef.value.show(index) // TODO Katex公式渲染后，图片绑定事件出现问题
      }
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
      hljs.configure({
        ignoreUnescapedHTML: true
      })
    })
  })
}
// 左侧快捷操作 点击
const handleLeftPanelClick = anchor => {
  nextTick(() => {
    const element = document.querySelector(anchor)
    if (element === null) {
      return
    }
    window.scrollTo(0, element.offsetTop - 50)
  })
}
// 更新评论数
const updateCommentCount = count => {
  articleInfo.value.commentCount = count
}
const makeToc = () => {
  nextTick(() => {
    const tocTags = ['H1', 'H2', 'H3', 'H4', 'H5', 'H6']
    // 获取所有H标签
    const content = document.querySelector('#detail')
    const childNodes = content.childNodes
    let index = 0
    childNodes.forEach(item => {
      let tagName = item.tagName
      if (tagName === undefined || !tocTags.includes(tagName)) {
        return
      }
      index++
      let id = 'toc' + index
      item.setAttribute('id', id)
      tocList.value.push({
        id: id,
        title: item.innerText,
        level: Number.parseInt(tagName.substring(1)),
        offsetTop: item.offsetTop
      })
    })
  })
  console.log(tocList.value)
}
// 定位到锚点
const gotoAnchor = id => {
  const anchor = document.querySelector('#' + id)
  if (!anchor) {
    return
  }
  anchorId.value = id
  window.scrollTo(0, anchor.offsetTop - 50)
}
// 获取滚动条高度
const getScrollTop = () => {
  return document.documentElement.scrollTop || document.body.scrollTop
}
// 监听滚动条
const listenScroll = () => {
  const currentScrollTop = getScrollTop()
  tocList.value.forEach((item, index) => {
    if (
      index < tocList.value.length - 1 &&
      currentScrollTop > tocList.value[index].offsetTop &&
      currentScrollTop < tocList.value[index + 1].offsetTop
    ) {
      anchorId.value = item.id
    }
  })
}
// 初始化
const init = async () => {
  let result = await proxy.$api.forum.getArticleDetail(route.params.articleId)
  articleInfo.value = result.data.forumArticleVO
  attachment.value = result.data.forumArticleAttachmentVO
  havaLike.value = result.data.havaLike
  handleImagePreview()
  highlightCode()
  makeToc()
}
onMounted(() => {
  init()
})
// 监听窗口大小变化
onMounted(() => {
  window.addEventListener('scroll', () => {
    listenScroll()
  })
})
onUnmounted(() => {
  window.removeEventListener('scroll', () => {
    listenScroll()
  })
})
// 监听用户信息变化
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
// 监听路由变化
watch(
  () => route.params.articleId,
  (newVal, oldVal) => {
    if (newVal === oldVal || newVal === undefined || oldVal === undefined) {
      return
    }
    proxy.$router.go(0)
  },
  {
    immediate: true,
    deep: true
  }
)
</script>

<style lang="scss" scoped>
.article {
  @apply relative;
  .article-detail {
    @apply relative inline-block ml-10 p-[10px] m-[10px];

    .article-detail-info {
      @apply mb-[20px];
    }

    .article-detail-content {
      @apply p-[10px];

      .title {
        @apply text-[20px] font-bold mb-[10px];
      }

      .user-info {
        @apply flex items-center py-[10px];
        border-bottom: 1px solid #eee;

        .user-info-detail {
          @apply ml-[10px];

          .nick-name {
            @apply text-[16px] font-bold cursor-pointer;

            &:hover {
              @apply text-[#1e88e5];
              text-decoration: underline;
            }
          }

          .info {
            @apply mt-[5px] text-[#999] text-[12px];
          }

          .edit-btn {
            @apply ml-[10px] text-[#1e88e5] cursor-pointer decoration-0;

            &:hover {
              @apply text-[#1976d2];
            }
          }
        }
      }

      .detail {
        @apply py-[5px] px-[15px] leading-[25px] tracking-[1.1px];
        //处理文章内的标签
        ::v-deep(h1),
        ::v-deep(h2),
        ::v-deep(h3),
        ::v-deep(h4),
        ::v-deep(h5),
        ::v-deep(h6) {
          @apply font-bold mt-[20px] mb-[10px];
        }

        ::v-deep(table) {
          @apply my-[20px] border-collapse border-spacing-0 w-full table-fixed overflow-auto text-[14px];

          th,
          td {
            @apply p-[8px];
            border: 1px solid #ddd;
          }

          th {
            @apply bg-[#f5f5f5];
          }
        }

        ::v-deep(ul) {
          @apply ml-[25px];
        }

        ::v-deep(ol) {
          @apply ml-[25px];
        }

        ::v-deep(code) {
          @apply my-[10px] mx-1 p-1 bg-[var(--bg-code)] rounded-md border border-gray-300 text-[14px] leading-[20px] tracking-[1.1px];
        }

        img {
          @apply w-[90%];
        }
      }
    }

    .attachment-panel {
      @apply mt-[20px] p-[10px];
      border: 1px solid #eee;

      .title {
        @apply text-[16px] font-bold mb-[10px];
      }

      .attachment-info {
        @apply flex items-center;

        .iconfont {
          @apply text-[30px] text-[$color-primary];
        }

        .file-name {
          @apply ml-[10px] text-[16px] font-bold;
        }

        .size {
          @apply mx-[10px] text-[12px] text-[#999];
        }

        .integral {
          @apply text-[12px] text-[#f56c6c];
        }

        .download-count {
          @apply ml-[10px] text-[12px] text-[#999];
        }

        .download-btn {
          @apply ml-[10px];
        }
      }
    }

    .block {
      @apply bg-[var(--bg-color)] rounded-2xl shadow-md;
      transition: all 0.3s;

      &:hover {
        @apply shadow-2xl;
      }
    }
  }

  .toc-panel {
    @apply sticky float-right top-28 w-[200px] min-h-[300px] max-h-[80vh] p-[10px] bg-[var(--bg-color)] rounded-[10px] shadow-md overflow-auto;
    transition: all 0.4s;

    &:hover {
      @apply shadow-2xl;
    }

    .toc-container {
      .toc-title {
        @apply text-[20px] font-bold mb-[10px];
      }

      .toc-list {
        .no-toc {
          @apply text-[12px] text-[var(--text-color-1)];
        }

        .toc-item {
          @apply mb-[5px] py-[5px] cursor-pointer;
          border-left: 2px solid var(--bg-color);
          transition: all 0.3s;

          &:hover {
            @apply bg-[var(--bg-color-hover)];
          }
        }

        .active {
          @apply text-[var(--color-primary)] bg-[var(--bg-color-active)];
          border-left: 2px solid var(--color-primary);
        }
      }
    }
  }

  .article-quick-panel {
    @apply sticky float-left flex left-1 top-[50%] flex-col items-center z-10;
    transform: translateY(-50%);
    transition: all 0.4s;

    .quick-item {
      @apply w-[40px] h-[40px] rounded-full bg-[var(--bg-color)] flex justify-center items-center mb-[10px] cursor-pointer shadow-md;
      transition: all 0.3s;

      &:hover {
        @apply shadow-2xl;
      }
    }
  }
}
</style>
