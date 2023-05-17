<template>
  <div ref="header" class="header">
    <div class="header-content" :style="{ width: headerWidth + 'px' }">
      <!-- Logo -->
      <slot name="logo">
        <Logo />
      </slot>
      <!-- 板块信息 -->
      <div class="menu-panel">
        <slot name="menu"></slot>
      </div>
      <!-- 用户板块 -->
      <div class="user-info-panel">
        <slot name="user"></slot>
      </div>
    </div>
  </div>
</template>

<script setup>
import { getCurrentInstance, onBeforeUnmount, onMounted, ref } from 'vue'
import Logo from '@/components/Logo/Logo.vue'

const { proxy } = getCurrentInstance()
defineProps({
  headerWidth: {
    type: Number
  }
})
// 获取头部元素
const header = ref(null)
// 绑定滚动事件
onMounted(() => {
  window.addEventListener('scroll', handleScroll) // 监听滚动事件
})
onBeforeUnmount(() => {
  window.removeEventListener('scroll', handleScroll) // 移除滚动事件
})
// 处理滚动事件
let scrollPosition = 0
const handleScroll = () => {
  console.log(scrollPosition)
  const currentPosition = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop
  const scrollDirection = currentPosition > scrollPosition ? 'down' : 'up'
  scrollPosition = currentPosition // 更新滚动位置
  if (scrollDirection === 'down' && currentPosition > 200) {
    // 向下滚动超过200px
    header.value.classList.add('header-up') // 添加属性
    scrollPosition = 0 // 重置滚动位置
  } else if (scrollDirection === 'up' && currentPosition < 200) {
    // 向上滚动超过200px
    header.value.classList.remove('header-up') // 移除属性
    scrollPosition = 0 // 重置滚动位置
  }
}
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
  // 模糊颗粒效果
  background-color: rgba(255, 255, 255, 0.5);
  background-image: radial-gradient(transparent 1px, #ffffff 1px);
  background-size: 4px 4px;
  backdrop-filter: saturate(50%) blur(4px);
  -webkit-backdrop-filter: saturate(50%) blur(4px);
  z-index: 10;
  // 头部内容
  .header-content {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin: 0 auto;
    height: 100%;
    // 板块信息
    .menu-panel {
      flex: 1;
    }

    // 登录、注册、退出
    .user-info-panel {
      display: flex;
      align-items: center;
      justify-content: right;
      width: 500px;
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
