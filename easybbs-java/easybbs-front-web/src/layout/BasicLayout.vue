<template>
  <div class="basic-layout">
    <Header class="header" :header-width="headerWidth">
      <template #menu>
        <div class="menu">
          <!--板块信息-->
          <el-menu class="board-menu" mode="horizontal" @select="handleSelect">
            <template v-for="nav in boardList">
              <el-menu-item v-if="Array.prototype.isPrototypeOf(nav.children) && nav.children.length === 0" :index="String(nav.boardId)">
                {{ nav.boardName }}
              </el-menu-item>
              <el-sub-menu v-else :index="String(nav.boardId)">
                <template #title>
                  <span>{{ nav.boardName }}</span>
                </template>
                <el-menu-item v-for="item in nav.children" :index="String(item.boardId)">
                  {{ item.boardName }}
                </el-menu-item>
              </el-sub-menu>
            </template>
          </el-menu>
        </div>
      </template>
      <template #user>
        <!-- 发帖搜索 -->
        <div class="box">
          <el-button type="primary" @click="handleTopButtonClick(0)"><span class="iconfont icon-add"></span>&nbsp; 发帖 </el-button>
          <el-button type="primary"><span class="iconfont icon-search"></span>&nbsp; 搜索</el-button>
        </div>
        <!--用户信息-->
        <div v-if="userInfo !== null" class="user-info" style="width: 150px; display: flex; align-items: center; justify-content: center">
          <el-dropdown trigger="click">
            <el-badge :value="messageCount.total" :hidden="messageCount.total === null || messageCount.total === 0">
              <div class="iconfont icon-message" style="margin: 0 15px; font-size: 25px; cursor: pointer"></div>
            </el-badge>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item class="message-item" @click="goToMessage('reply')"
                  >回复我的<span v-if="messageCount.reply > 0" lass="tag">{{ messageCount.reply <= 99 ? messageCount.reply : '99+' }}</span>
                </el-dropdown-item>
                <el-dropdown-item class="message-item" @click="goToMessage('articleLike')"
                  >赞了我的文章<span v-if="messageCount.likePost > 0" class="tag">{{
                    messageCount.likePost <= 99 ? messageCount.likePost : '99+'
                  }}</span>
                </el-dropdown-item>
                <el-dropdown-item class="message-item" @click="goToMessage('attachmentDownload')"
                  >下载了我的附件<span v-if="messageCount.attachmentDownload > 0" class="tag">{{
                    messageCount.attachmentDownload <= 99 ? messageCount.attachmentDownload : '99+'
                  }}</span>
                </el-dropdown-item>
                <el-dropdown-item class="message-item" @click="goToMessage('commentLike')"
                  >赞了我的评论<span v-if="messageCount.likeComment > 0" class="tag">{{
                    messageCount.likeComment <= 99 ? messageCount.likeComment : '99+'
                  }}</span>
                </el-dropdown-item>
                <el-dropdown-item class="message-item" @click="goToMessage('system')"
                  >系统消息<span v-if="messageCount.sys > 0" class="tag">{{ messageCount.sys <= 99 ? messageCount.sys : '99+' }}</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <!--头像-->
          <Avatar user-id="8743908827" :src="accountApi.avatarUrl(8743908827)" style="margin-left: 25px" />
        </div>
        <!-- 登录、注册、退出 -->
        <el-button-group v-else style="margin-left: 10px">
          <el-button type="primary" plain @click="showUserDialog(0)"><span class="iconfont icon-login"></span>&nbsp; 登录 </el-button>
          <el-button type="primary" plain @click="showUserDialog(1)"><span class="iconfont icon-register"></span>&nbsp; 注册 </el-button>
        </el-button-group>
      </template>
    </Header>
    <!--body-->
    <div class="content" :style="{ width: proxy.$store.getters.contentWidth + 'px', top: proxy.$store.getters.headerHeight + 'px' }">
      <router-view />
    </div>
    <UserDialog ref="userDialog" />
    <div class="side-tools">
      <el-backtop :right="30" :bottom="30" />
    </div>
    <Footer />
  </div>
</template>

<script setup>
import { getCurrentInstance, onMounted, ref, watch } from 'vue'
import accountApi from '@/api/account'
import boardApi from '@/api/board'
import Header from '@/components/Header/Header.vue'
import UserDialog from '@/components/UserDialog/UserDialog.vue'
import Avatar from '@/components/Avatar/Avatar.vue'
import Footer from '@/components/Footer/Footer.vue'
import { useRoute } from 'vue-router'
import { TOGGLE_CONTENT_WIDTH, UPDATE_MESSAGE_COUNT } from '@/store/mutation-types'

const { proxy } = getCurrentInstance()
const route = useRoute()
// 消息数量
const messageCount = ref({})
// 头部宽度
const headerWidth = ref(proxy.$store.getters.contentWidth)
// 元素ref
const userDialog = ref(null)
const showUserDialog = type => {
  userDialog.value.showPanel(type)
}
// 获取用户信息
const getUserInfo = async () => {
  const result = await accountApi.getUserInfo()
  if (!result) {
    return
  }
  proxy.$store.commit('UPDATE_USER_INFO', result.data === null ? {} : result.data)
}
// 获取板块信息
const boardList = ref([])
const loadBoard = async () => {
  const result = await boardApi.loadBoard()
  if (!result) {
    return
  }
  boardList.value = result.data
}
// 选择板块
const handleSelect = (key, keyPath) => {
  if (keyPath.length === 1) {
    proxy.$router.push(`/forum/${key}`)
  } else {
    proxy.$router.push(`/forum/${keyPath[0]}/${key}`)
  }
}
// 处理发帖、搜索按钮点击
const handleTopButtonClick = type => {
  if (type === 0) {
    if (!proxy.$store.getters.userId) {
      showUserDialog(0)
      proxy.$Toast.warning('请先登录')
    } else {
      proxy.$router.push('/postArticle')
    }
  }
}
// 消息中心
const goToMessage = type => {
  proxy.$router.push(`/user/message/${type}`)
}
// 获取消息数量
const getMessageCount = async () => {
  const result = await proxy.$api.user.getMessageCount()
  if (!result) {
    return
  }
  messageCount.value = result.data
  proxy.$store.commit(UPDATE_MESSAGE_COUNT, result.data)
}
const userInfo = ref()
// beforeCreated
getUserInfo()
loadBoard()
// Mounted
onMounted(() => {
  if (route.path === '/postArticle' || route.path.indexOf('/postArticle/') !== -1) {
    headerWidth.value = 1200
    proxy.$store.commit(TOGGLE_CONTENT_WIDTH, document.body.clientWidth)
  }
})
// 监听用户信息
watch(
  () => proxy.$store.getters.userId,
  newVal => {
    if (!newVal) {
      userInfo.value = null
      return
    }
    userInfo.value = newVal
    getMessageCount()
  },
  { immediate: true, deep: true }
)
// 监听消息数量
watch(
  () => proxy.$store.getters.messageCount,
  newVal => {
    messageCount.value = newVal || {}
  },
  { immediate: true, deep: true }
)
// 监听是否展示登录框
watch(
  () => proxy.$store.state.showLoginDialog,
  newVal => {
    if (newVal) {
      showUserDialog(0)
      proxy.$Toast.warning('请先登录')
    }
  },
  { immediate: true, deep: true }
)
// 监听是否需要切换屏幕宽度 (path == /postArticle/:articleId || /postArticle)
watch(
  () => route.path,
  newVal => {
    if (newVal === '/postArticle' || newVal.indexOf('/postArticle/') !== -1) {
      headerWidth.value = 1200
      proxy.$store.commit(TOGGLE_CONTENT_WIDTH, document.body.clientWidth)
    } else {
      proxy.$store.commit(TOGGLE_CONTENT_WIDTH, 1200)
    }
  }
)
</script>

<style lang="scss" scoped>
.basic-layout {
  .header {
    .user-info {
    }
  }

  .content {
    position: relative;
    margin: 0 auto;
    min-height: 93vh;
  }

  .board-menu {
    background: transparent;
  }
}

.message-item {
  display: flex;
  justify-content: space-around;

  .tag {
    position: relative;
    top: -5px;
    min-width: 16px;
    min-height: 16px;
    display: inline-block;
    margin-left: 5px;
    line-height: 16px;
    border-radius: 50%;
    background: #ff4d4f;
    color: #fff;
    font-size: 12px;
    text-align: center;
  }
}
</style>
