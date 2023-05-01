<template>
  <div class="basic-layout">
    <Header>
      <template #menu>
        <div class="menu">
          <!--板块信息-->
          <el-menu class="board-menu" mode="horizontal" @select="handleSelect">
            <template v-for="nav in boardList">
              <el-menu-item v-if="Array.prototype.isPrototypeOf(nav.children) && nav.children.length === 0"
                            :index="String(nav.boardId)">
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
          <el-button type="primary"><span class="iconfont icon-add"></span>&nbsp; 发帖</el-button>
          <el-button type="primary"><span class="iconfont icon-search"></span>&nbsp; 搜索</el-button>
        </div>
        <!--用户信息-->
        <div v-if="userInfo !== null" class="user-info"
             style="width: 150px; display: flex; align-items: center; justify-content: center">
          <el-dropdown>
            <el-badge :value="12" class="item">
              <div class="iconfont icon-message" style="margin: 0 15px; font-size: 25px; cursor: pointer"></div>
            </el-badge>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>回复我的</el-dropdown-item>
                <el-dropdown-item>赞了我的文章</el-dropdown-item>
                <el-dropdown-item>下载了我的附件</el-dropdown-item>
                <el-dropdown-item>赞了我的评论</el-dropdown-item>
                <el-dropdown-item>系统消息</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <!--头像-->
          <Avatar user-id="8743908827" :src="userApi.avatarUrl(8743908827)" style="margin-left: 25px"/>
        </div>
        <!-- 登录、注册、退出 -->
        <el-button-group v-else style="margin-left: 10px">
          <el-button type="primary" plain @click="showUserDialog(0)"><span class="iconfont icon-login"></span>&nbsp; 登录
          </el-button>
          <el-button type="primary" plain @click="showUserDialog(1)"><span class="iconfont icon-register"></span>&nbsp;
            注册
          </el-button>
        </el-button-group>
      </template>
    </Header>
    <!--body-->
    <div class="content" :style="{ width: proxy.store.getters.contentWidth, top: proxy.store.getters.headerHeight }">
      <router-view/>
    </div>
    <UserDialog ref="userDialog"/>
  </div>
</template>

<script setup>
import {getCurrentInstance, onMounted, ref, watch} from 'vue'
import userApi from '@/api/user'
import boardApi from '@/api/board'
import Header from '@/components/Header/Header.vue'
import UserDialog from '@/components/UserDialog/UserDialog.vue'
import Avatar from '@/components/Avatar/Avatar.vue'
import router from '@/router'

const {proxy} = getCurrentInstance()
// 元素ref
const userDialog = ref(null)
const showUserDialog = type => {
  userDialog.value.showPanel(type)
}
// 获取用户信息
const getUserInfo = async () => {
  const result = await userApi.getUserInfo()
  if (!result) {
    return
  }
  proxy.store.commit('UPDATE_USER_INFO', result.data === null ? {} : result.data)
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
    router.push(`/forum/${key}`)
  } else {
    router.push(`/forum/${keyPath[0]}/${key}`)
  }
}
const userInfo = ref()
// beforeCreated
getUserInfo()
loadBoard()
// Mounted
onMounted(() => {
})
// 监听用户信息
watch(
    () => proxy.store.getters.userId,
    newVal => {
      if (!newVal) {
        userInfo.value = {}
      }
      userInfo.value = newVal
    },
    {immediate: true, deep: true}
)
// 监听是否展示登录框
watch(
    () => proxy.store.state.showLoginDialog,
    newVal => {
      if (newVal) {
        showUserDialog(0)
      }
    },
    {immediate: true, deep: true}
)
</script>

<style lang="scss" scoped>
.basic-layout {
  .content {
    position: relative;
    margin: 0 auto;
  }

  .board-menu {
    background: transparent;
  }
}
</style>
