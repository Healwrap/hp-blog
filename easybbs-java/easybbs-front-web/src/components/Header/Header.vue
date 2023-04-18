<template>
  <div ref="header" class="header">
    <div class="header-content" :style="{ width: proxy.globalInfo.bodyWidth }">
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
        <slot name="user"> </slot>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, onMounted } from 'vue'
import Logo from '@/components/Logo/Logo.vue'
const { proxy } = getCurrentInstance()
// 获取头部元素
const header = ref(null)
// 获取滚动条高度
const getScrollTop = () => {
  const scrollTop = document.documentElement.scrollTop || window.pageYOffset || document.body.scrollTop
  return scrollTop
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
