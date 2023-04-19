<template>
  <div class="basic-layout">
    <Header>
      <template #menu>
        <div class="menu">

        </div>
      </template>
      <template #user>
        <!-- 发帖搜索 -->
        <div class="box">
          <el-button type="primary"><span class="iconfont icon-add"></span>&nbsp; 发帖</el-button>
          <el-button type="primary"><span class="iconfont icon-search"></span>&nbsp; 搜索</el-button>
        </div>
        <!-- 登录、注册、退出 -->
        <el-button-group style="margin-left: 10px">
          <el-button type="primary" plain @click="login"><span class="iconfont icon-login"></span>&nbsp; 登录
          </el-button>
          <el-button type="primary" plain><span class="iconfont icon-register"></span>&nbsp; 注册</el-button>
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
      <router-view/>
    </div>
    <UserDialog v-if="true"/>
  </div>
</template>

<script setup>
import {getCurrentInstance, ref} from 'vue'
import Header from '@/components/Header/Header.vue'
import UserDialog from "@/components/UserDialog/UserDialog.vue";

const {proxy} = getCurrentInstance()
// 显示对话框
const showDialog = ref(false)
const login = () => {
  console.log('login')
  showDialog.value = true
}
</script>

<style lang="scss" scoped></style>
