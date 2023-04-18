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
          <el-button type="primary"> <span class="iconfont icon-add"></span>&nbsp; 发帖 </el-button>
          <el-button type="primary"> <span class="iconfont icon-search"></span>&nbsp; 搜索 </el-button>
        </div>
        <!-- 登录、注册、退出 -->
        <el-button-group style="margin-left: 10px">
          <el-button type="primary" plain @click="login"> <span class="iconfont icon-login"></span>&nbsp; 登录 </el-button>
          <el-button type="primary" plain> <span class="iconfont icon-register"></span>&nbsp; 注册 </el-button>
        </el-button-group>
      </template>
    </Header>
    <div>
      <div class="content" :style="{ margin: '0 auto', width: '1000px', height: '40vh', background: 'red' }"></div>
      <div class="content" :style="{ margin: '0 auto', width: '1000px', height: '40vh', background: 'blue' }"></div>
      <div class="content" :style="{ margin: '0 auto', width: '1000px', height: '40vh', background: 'green' }"></div>
      <div class="content" :style="{ margin: '0 auto', width: '1000px', height: '40vh', background: 'red' }"></div>
      <div class="content" :style="{ margin: '0 auto', width: '1000px', height: '40vh', background: 'blue' }"></div>
      <div class="content" :style="{ margin: '0 auto', width: '1000px', height: '40vh', background: 'green' }"></div>
      <router-view />
    </div>
    <!-- <Dialog title="标题" :show="showDialog" :buttons="[{ text: '确定', type: 'primary' }]" @close="showDialog = false"> <div :style="{ height: '1500px' }">这里是内容</div> </Dialog> -->
    <LoginAndRegister v-model:show="showDialog" />
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, onMounted } from 'vue'
import Header from '@/components/Header/Header.vue'
import LoginAndRegister from '@/components/LoginAndRegister/LoginAndRegister.vue'
const { proxy } = getCurrentInstance()
// 显示对话框
const showDialog = ref(false)
// const login = () => {
//   showDialog.value = true
// }
// // 登录注册
// const loginRegister = () => {
//   showDialog.value = true
// }
</script>

<style lang="scss" scoped></style>
