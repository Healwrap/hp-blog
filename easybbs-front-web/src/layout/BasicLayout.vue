<template>
  <div class="basic-layout">
    <div ref="header" class="header">
      <div class="header-content" :style="{ width: proxy.globalInfo.bodyWidth }">
        <!-- 彩虹字logo -->
        <div class="logo"><a href="/index">Easybbs</a></div>
        <!-- 板块信息 -->
        <div class="menu-panel"></div>
        <!-- 用户板块 -->
        <div class="user-info-panel">
          <!-- 发帖搜索 -->
          <div class="box">
            <el-button type="primary"> <span class="iconfont icon-add"></span>&nbsp; 发帖 </el-button>
            <el-button type="primary"> <span class="iconfont icon-search"></span>&nbsp; 搜索 </el-button>
          </div>
          <!-- 登录、注册、退出 -->
          <el-button-group :style="{ 'margin-left': '10px' }">
            <el-button type="primary" plain @click="login"> <span class="iconfont icon-login"></span>&nbsp; 登录 </el-button>
            <el-button type="primary" plain> <span class="iconfont icon-register"></span>&nbsp; 注册 </el-button>
          </el-button-group>
        </div>
      </div>
    </div>
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
import LoginAndRegister from '@/components/LoginAndRegister/LoginAndRegister.vue'
const { proxy } = getCurrentInstance()
// 获取头部元素
const header = ref(null)
// 获取滚动条高度
const getScrollTop = () => {
  const scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop
  return scrollTop
}
// 显示对话框
const showDialog = ref(false)
const login = () => {
  showDialog.value = true
}
// 登录注册
const loginRegister = () => {
  showDialog.value = true
}
onMounted(() => {
  let scrollType = 0 // 0:未滚动 1:向上滚动 2:向下滚动
  let scrollTop = getScrollTop()
  window.addEventListener('scroll', () => {
    const currentScrollTop = getScrollTop()
    if (currentScrollTop > scrollTop) {
      // 向下滚动
      scrollType = 2
      header.value.classList.add('header-up')
    } else if (currentScrollTop < scrollTop) {
      // 向上滚动
      scrollType = 1
      header.value.classList.remove('header-up')
    } else {
      // 未滚动
      scrollType = 0
    }
    // 滚动距离的绝对值大于300px时才执行
    if (Math.abs(currentScrollTop - scrollTop) > 300) {
      scrollTop = currentScrollTop
    }
  })
})
</script>

<style lang="scss" scoped>
// 头部
.header {
  position: fixed;
  top: 0;
  width: 100%;
  height: 60px;
  box-shadow: 0 2px 8px #aaaaaa;
  transition: all 0.3s;
  z-index: 9999;
  // 模糊颗粒效果
  background-color: rgba(255, 255, 255, 0.5);
  background-image: radial-gradient(transparent 1px, #ffffff 1px);
  background-size: 4px 4px;
  backdrop-filter: saturate(50%) blur(4px);
  -webkit-backdrop-filter: saturate(50%) blur(4px);
  backdrop-filter: blur(4px);
  // 头部内容
  .header-content {
    display: flex;
    align-items: center;
    margin: 0 auto;
    height: 100%;
    // 彩虹字logo
    .logo {
      // 彩虹字logo
      font: 700 30px 思源黑体;
      color: #fff;
      margin-right: 10px;
      background: -webkit-linear-gradient(30deg, #32c5ff 25%, #b620e0 50%, #f7b500 75%, #20e050 100%);
      background-clip: text;
      transition: all 0.3s;
      background-position: 0% 82%;
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      // 添加hover效果
      &:hover {
        background: -webkit-linear-gradient(30deg, #32c5ff 25%, #b620e0 50%, #f7b500 75%, #20e050 100%);
        background-clip: text;
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-position: 100% 19%;
        animation: rainbow 150s linear infinite;
      }
      // 添加动画
      @keyframes rainbow {
        0% {
          background-position: 0;
        }
        50% {
          background-position: 2800px;
        }
        100% {
          background-position: 0;
        }
      }
    }
    // 板块信息
    .menu-panel {
      flex: 1;
    }
    // 登录、注册、退出
    .user-info-panel {
      display: flex;
      align-items: center;
      width: 400px;
      height: 100%;
      // 发帖搜索
      .box {
        .el-button + .el-button {
          margin-left: 5px;
        }
      }
    }
  }
}
// 头部收起
.header-up {
  transform: translateY(-60px);
  box-shadow: none;
  overflow: hidden;
}
</style>
