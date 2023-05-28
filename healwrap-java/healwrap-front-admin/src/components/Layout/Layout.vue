<template>
  <div class="basic-layout">
    <el-container>
      <el-aside class="aside" width="240px">
        <logo text="HealWrap管理后台" font-size="24px" style="text-align: center; padding: 15px 0" />
        <el-menu
          class="menu"
          :default-openeds="elMenuConfig.defaultOpeneds"
          :default-active="elMenuConfig.defaultActive"
          :collapse="elMenuConfig.collapse"
          :active-text-color="elMenuConfig.activeTextColor"
          :background-color="elMenuConfig.backgroundColor"
          :text-color="elMenuConfig.textColor"
          @open="handleOpen"
          @close="handleClose"
        >
          <template v-for="item in menuList" :key="item.name">
            <el-sub-menu v-if="'children' in item" :index="item.path">
              <template #title>
                <span :class="['iconfont', item.icon]" style="padding-right: 20px"></span>
                <span>{{ item.name }}</span>
              </template>
              <el-menu-item v-for="subItem in item.children" :key="subItem.name" :index="subItem.path">
                <router-link :to="subItem.path">{{ subItem.name }}</router-link>
              </el-menu-item>
            </el-sub-menu>
            <el-menu-item v-else :index="item.path">
              <router-link :to="item.path">
                <span :class="['iconfont', item.icon]" style="padding-right: 20px"></span>
                <span>{{ item.name }}</span>
              </router-link>
            </el-menu-item>
          </template>
        </el-menu>
      </el-aside>
      <el-container>
        <el-main>
          <el-button @click="elMenuConfig.collapse = !elMenuConfig.collapse">collapse</el-button>
          <slot></slot>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import Logo from '@/components/Logo/Logo.vue'

const elMenuConfig = ref({
  defaultOpeneds: ['1'],
  defaultActive: '1',
  collapse: false,
  textColor: '#fff',
  activeTextColor: '#1890ff',
  backgroundColor: '#003665'
})
const menuList = [
  {
    name: '内容管理',
    icon: 'icon-article',
    path: '/forum',
    children: [
      {
        name: '帖子管理',
        path: '/forum/article'
      },
      {
        name: '评论管理',
        path: '/forum/comment'
      },
      {
        name: '板块管理',
        path: '/forum/board'
      }
    ]
  },
  {
    name: '用户管理',
    icon: 'icon-user',
    path: '/user',
    children: [
      {
        name: '用户列表',
        path: '/user/list'
      }
    ]
  },
  {
    name: '设置',
    icon: 'icon-settings',
    path: '/settings'
  }
]
const init = () => {
  menuList.forEach(item => {
    elMenuConfig.value.defaultOpeneds.push(item.path)
  })
}
init()
const handleOpen = () => {}
const handleClose = () => {}
</script>

<style lang="scss" scoped>
.basic-layout {
  .aside {
    @apply bg-[#003665] h-screen;
    ::v-deep(.el-menu) {
      @apply border-none bg-transparent;
      .el-menu-item {
        @apply border-none;
        .el-icon {
          @apply mr-[10px];
        }
      }

      .el-sub-menu {
        @apply bg-[#041e38];
      }
    }
  }
}
</style>
