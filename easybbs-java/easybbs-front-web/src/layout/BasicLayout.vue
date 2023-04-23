<template>
  <div class="basic-layout">
    <Header>
      <template #menu>
        <div class="menu"></div>
      </template>
      <template #user>
        <!-- 发帖搜索 -->
        <div class="box">
          <el-button type="primary"><span class="iconfont icon-add"></span>&nbsp; 发帖</el-button>
          <el-button type="primary"><span class="iconfont icon-search"></span>&nbsp; 搜索</el-button>
        </div>
        <div class="user-info">
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
        </div>
        <!--头像-->
        <Avatar user-id="8743908827" :src="userApi.avatarUrl(8743908827)" style="margin-left: 25px" />
        <!-- 登录、注册、退出 -->
        <el-button-group style="margin-left: 10px" v-if="false">
          <el-button type="primary" plain @click="showUserDialog(0)"><span class="iconfont icon-login"></span>&nbsp; 登录 </el-button>
          <el-button type="primary" plain @click="showUserDialog(1)"><span class="iconfont icon-register"></span>&nbsp; 注册 </el-button>
        </el-button-group>
      </template>
    </Header>
    <div v-if="false">
      <div class="content" :style="{ margin: '0 auto', width: '1000px', height: '40vh', background: 'red' }"></div>
      <div class="content" :style="{ margin: '0 auto', width: '1000px', height: '40vh', background: 'blue' }"></div>
      <div class="content" :style="{ margin: '0 auto', width: '1000px', height: '40vh', background: 'green' }"></div>
      <div class="content" :style="{ margin: '0 auto', width: '1000px', height: '40vh', background: 'red' }"></div>
      <div class="content" :style="{ margin: '0 auto', width: '1000px', height: '40vh', background: 'blue' }"></div>
      <div class="content" :style="{ margin: '0 auto', width: '1000px', height: '40vh', background: 'green' }"></div>
      <router-view />
    </div>
    <UserDialog ref="userDialog" />
  </div>
</template>

<script setup>
import { getCurrentInstance, ref } from 'vue'
import userApi from '@/api/user'
import Header from '@/components/Header/Header.vue'
import UserDialog from '@/components/UserDialog/UserDialog.vue'
import Avatar from '@/components/Avatar/Avatar.vue'

const { proxy } = getCurrentInstance()
// 元素ref
const userDialog = ref(null)
const showUserDialog = type => {
  userDialog.value.showPanel(type)
}
</script>

<style lang="scss" scoped></style>
