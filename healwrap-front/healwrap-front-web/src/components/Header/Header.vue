<template>
  <div ref="header" class="header">
    <div class="header-content" :style="{ width: headerWidth }">
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
import { getCurrentInstance, nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import Logo from '@/components/Logo/Logo.vue'

const { proxy } = getCurrentInstance()
defineProps({
  headerWidth: {
    type: String
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
const handleScroll = () => {
  // 向下滚动
  if (window.scrollY >= 70) {
    header.value.classList.add('header-up')
  } else if (proxy.$store.getters.enterApp) {
    header.value.classList.remove('header-up')
  }
}
watch(
  () => proxy.$store.getters.enterApp,
  e => {
    nextTick(() => {
      if (!e) {
        header.value.classList.add('header-up')
      } else {
        header.value.classList.remove('header-up')
      }
    })
  },
  {
    immediate: true,
    deep: true
  }
)
</script>

<style lang="scss" scoped>
// 头部
.header {
  position: fixed;
  top: 0;
  width: 100%;
  height: 50px;
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
      width: 330px;
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
