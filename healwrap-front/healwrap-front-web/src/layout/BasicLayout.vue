<template>
  <div class="basic-layout">
    <background />
    <Header class="header">
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
        <div v-if="proxy.$store.getters.isMobile === false" class="box" style="">
          <custom-button v-if="userInfo !== null" name="postArticle" @click="handleTopButtonClick(0)" />
          <custom-button name="search" @click="searchRef.show()" />
        </div>
        <!--用户信息-->
        <div v-if="userInfo !== null" class="user-info" style="width: 120px; display: flex; align-items: center; justify-content: center">
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
        <!-- 登录、注册 -->
        <custom-button v-else name="loginAndRegister" :config="{ text1: '未登录', text2: '去注册' }" @click="showUserDialog(1)" />

        <div v-if="proxy.$store.getters.isMobile === true" class="op-menu" @click="collapseMenu">
          <Icon :icon="elMenuConfig.collapse ? 'Expand' : 'Fold'" :size="27" />
        </div>
      </template>
    </Header>
    <!--body-->
    <div class="content" :style="{ width: proxy.$store.getters.contentWidth }">
      <router-view />
    </div>
    <!--左侧板块选择-->
    <board-select />
    <!--用户登录注册-->
    <user-dialog ref="userDialog" />
    <!--右侧工具-->
    <div class="side-tools">
      <el-backtop :right="30" :bottom="30" />
    </div>
    <!--搜索-->
    <search ref="searchRef" />
    <!--底部-->
    <Footer />
    <!--侧边栏-->
    <Drawer :show="elMenuConfig.collapse" class="drawer" @close="closeMenu">
      <!--板块信息-->
      <el-menu class="board-menu" :mode="elMenuConfig.mode" @select="handleSelect">
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
      <!-- 发帖搜索 -->
      <div v-if="proxy.$store.getters.isMobile === true" class="box" style="">
        <custom-button v-if="userInfo !== null" name="postArticle" @click="handleTopButtonClick(0)" />
        <custom-button name="search" @click="searchRef.show()" />
      </div>
    </Drawer>
  </div>
</template>

<script setup>
import { getCurrentInstance, onMounted, onUnmounted, reactive, ref, watch } from 'vue'
import accountApi from '@/api/account'
import boardApi from '@/api/board'
import Header from '@/components/Header/Header.vue'
import UserDialog from '@/components/UserDialog/UserDialog.vue'
import Avatar from '@/components/Avatar/Avatar.vue'
import Footer from '@/components/Footer/Footer.vue'
import { useRoute } from 'vue-router'
import { TOGGLE_CONTENT_WIDTH, TOGGLE_EDITOR_TYPE, TOGGLE_MOBILE_TYPE, UPDATE_MESSAGE_COUNT } from '@/store/mutation-types'
import CustomButton from '@/components/CustomButton/CustomButton.vue'
import Background from '@/components/Background/Background.vue'
import Search from '@/components/Search/Search.vue'
import BoardSelect from '@/components/BoardSelect/BoardSelect.vue'
import Drawer from '@/components/Drawer/Drawer.vue'
import { changeTheme } from '@/utils/Utils'

const { proxy } = getCurrentInstance()
const route = useRoute()
const searchRef = ref(null)
// 菜单配置
const elMenuConfig = reactive({
  collapse: false,
  defaultActive: route.path,
  mode: 'horizontal'
})
// 消息数量
const messageCount = ref({})
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
      proxy.$message.warning('请先登录')
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
const collapseMenu = () => {
  if (elMenuConfig.collapse === false) {
    elMenuConfig.collapse = true
  }
  if (proxy.$store.getters.isMobile === true) {
    elMenuConfig.mode = 'vertical'
  } else {
    elMenuConfig.mode = elMenuConfig.collapse ? 'vertical' : 'horizontal'
  }
}
const closeMenu = () => {
  elMenuConfig.collapse = false
}
// beforeCreated
getUserInfo()
loadBoard()
// 获取屏幕宽度
const getScreenWidth = () => {
  if (document.documentElement.clientWidth < 1100) {
    proxy.$store.commit(TOGGLE_CONTENT_WIDTH, '100vw')
    proxy.$store.commit(TOGGLE_MOBILE_TYPE, true)
  } else {
    proxy.$store.commit(TOGGLE_CONTENT_WIDTH, '1100px')
    proxy.$store.commit(TOGGLE_MOBILE_TYPE, false)
  }
  if (proxy.$store.getters.isEditor === true) {
    proxy.$store.commit(TOGGLE_CONTENT_WIDTH, '100vw')
  }
}
// Mounted
onMounted(() => {
  // 获取主题
  const theme = localStorage.getItem('theme')
  if (theme) {
    changeTheme(theme)
  } else {
    changeTheme('dark')
  }
  if (route.path === '/postArticle' || route.path.indexOf('/postArticle/') !== -1) {
    proxy.$store.commit(TOGGLE_EDITOR_TYPE, true)
    proxy.$store.commit(TOGGLE_CONTENT_WIDTH, '100vw')
  }
})
// 监听屏幕宽度变化
onMounted(() => {
  getScreenWidth()
  window.onresize = () => {
    getScreenWidth()
  }
})
onUnmounted(() => {
  window.onresize = null
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
      proxy.$message.warning('请先登录')
    }
  },
  { immediate: true, deep: true }
)
// 监听是否需要切换屏幕宽度 (path == /postArticle/:articleId || /postArticle)
watch(
  () => route.path,
  newVal => {
    if (newVal === '/postArticle' || newVal.indexOf('/postArticle/') !== -1) {
      proxy.$store.commit(TOGGLE_EDITOR_TYPE, true)
      proxy.$store.commit(TOGGLE_CONTENT_WIDTH, '100vw')
    } else if (proxy.$store.getters.isEditor) {
      proxy.$store.commit(TOGGLE_CONTENT_WIDTH, '1100px')
      proxy.$store.commit(TOGGLE_EDITOR_TYPE, false)
    } else if (proxy.$store.getters.isMobile === true) {
      proxy.$store.commit(TOGGLE_CONTENT_WIDTH, '100vw')
    }
  }
)
</script>

<style lang="scss" scoped>
.basic-layout {
  .header {
    .user-info {
    }

    .op-menu {
      @apply flex mx-1 items-center cursor-pointer text-black;
    }

    .box {
      @apply flex max-w-[190px] h-full mr-[10px] justify-items-end items-center;
    }
  }

  .content {
    @apply relative mx-auto min-h-[93vh];
  }
}

.message-item {
  @apply flex justify-around;

  .tag {
    @apply relative top-[-5px] min-w-[16px] min-h-[16px] inline-block ml-[5px] leading-[10px] rounded-full bg-[#ff4d4f] text-white text-[12px] text-center;
  }
}

.menu {
  ::v-deep(.board-menu) {
    @apply h-[45px] bg-transparent overflow-hidden border-none;
  }
}

.drawer {
  .box {
    @apply flex flex-col h-24 justify-between items-center;
  }
}
</style>
