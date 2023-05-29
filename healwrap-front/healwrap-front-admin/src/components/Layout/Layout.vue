<template>
  <div class="basic-layout">
    <el-container>
      <el-aside class="aside" :style="{ width: elMenuConfig.collapse ? '65px' : '240px' }"
                style="transition: width 0.3s">
        <logo
            color="#fff"
            :show-rain-bow="false"
            :text="elMenuConfig.collapse ? 'HW' : 'HealWrap管理后台'"
            font-size="24px"
            style="text-align: center; padding: 15px 0"
        />
        <el-menu
            class="menu"
            :default-openeds="elMenuConfig.defaultOpeneds"
            :default-active="elMenuConfig.defaultActive"
            :collapse="elMenuConfig.collapse"
            :active-text-color="elMenuConfig.activeTextColor"
            :text-color="elMenuConfig.textColor"
            :background-color="elMenuConfig.backgroundColor"
            router
            @open="handleOpen"
            @close="handleClose"
        >
          <template v-for="item in menuList" :key="item.name">
            <el-sub-menu v-if="'children' in item" :index="item.path">
              <template #title>
                <Icon :icon="item.meta.icon"/>
                <span>{{ item.name }}</span>
              </template>
              <el-menu-item v-for="subItem in item.children" :key="subItem.name" :index="subItem.path">
                <router-link :to="subItem.path">{{ subItem.name }}</router-link>
              </el-menu-item>
            </el-sub-menu>
            <el-menu-item v-else :index="item.path">
              <Icon :icon="item.meta.icon"/>
              <span>{{ item.name }}</span>
            </el-menu-item>
          </template>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="header">
          <span class="op-menu" @click="collapseMenu">
            <Icon :icon="elMenuConfig.collapse ? 'Expand' : 'Fold'" :size="18"/>
          </span>
          <div class="menu-breadcrumb">
            <el-breadcrumb>
              <el-breadcrumb-item v-for="item in route.matched" :key="item.path">
                <div style="display: flex; align-items: center">
                  <Icon v-if="item.meta.icon" :icon="item.meta.icon"/>
                  <span>{{ item.name }}</span>
                </div>
              </el-breadcrumb-item>
            </el-breadcrumb>
          </div>
        </el-header>
        <el-main class="main">
          <div class="content">
            <div class="tab-group">
              <el-tabs v-model="elMenuConfig.defaultActive" type="border-card" @tab-change="tabChange" @edit="editTab">
                <div v-for="item in tabList" :key="item">
                  <el-tab-pane v-if="item" :name="item.path" :label="item.name" :closable="tabList.length > 1"/>
                </div>
              </el-tabs>
            </div>
            <slot></slot>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import {getCurrentInstance, ref, watch} from 'vue'
import Logo from '@/components/Logo/Logo.vue'
import {asyncRouterMap} from '@/config/router.config'
import {useRoute} from 'vue-router'
import Icon from '@/components/Icon/Icon.vue'

const {proxy} = getCurrentInstance()
const route = useRoute()
const elMenuConfig = ref({
  defaultOpeneds: [],
  defaultActive: '',
  collapse: false,
  textColor: '#fff',
  activeTextColor: '#fff',
  backgroundColor: '#20252a'
})
const menuList = asyncRouterMap
const tabList = ref([])
const menuMap = {}
const init = () => {
  elMenuConfig.value.defaultActive = route.path
  menuList.forEach(item => {
    elMenuConfig.value.defaultOpeneds.push(item.path)
    if ('children' in item) {
      item.children.forEach(subItem => {
        menuMap[subItem.path] = subItem
      })
    } else {
      menuMap[item.path] = item
    }
  })
}
init()
const handleOpen = () => {
}
const handleClose = () => {
}
const collapseMenu = () => {
  elMenuConfig.value.collapse = !elMenuConfig.value.collapse
}
const tabChange = e => {
  proxy.$router.push(e)
}
const editTab = (targetKey, action) => {
  if (action !== 'remove') {
    return
  }
  let curPath = elMenuConfig.value.defaultActive
  const tabs = tabList.value
  // 关闭当前选择的tab
  if (targetKey === elMenuConfig.value.defaultActive) {
    tabs.forEach((tab, index) => {
      if (tab.path === targetKey) {
        const nextTab = tabs[index + 1] || tabs[index - 1]
        if (nextTab) {
          curPath = nextTab.path
        }
      }
    })
  }
  tabList.value = tabs.filter(tab => tab.path !== targetKey)
  if (curPath !== elMenuConfig.value.defaultActive) {
    proxy.$router.push(curPath)
  }
}
watch(
    () => route,
    to => {
      elMenuConfig.value.defaultActive = to.path
      const curTab = tabList.value.find(item => item.path === to.path)
      if (curTab || to.path === undefined) {
        return
      }
      tabList.value.push(menuMap[to.path])
    },
    {immediate: true, deep: true}
)
</script>

<style lang="scss" scoped>
.basic-layout {
  .aside {
    @apply bg-[#20252a] h-screen;
    ::v-deep(.el-menu) {
      @apply border-none bg-transparent;
      .el-menu-item {
        a {
          @apply w-full;
        }

        &:hover {
          @apply bg-[#1890ff];
        }

        &.is-active {
          @apply bg-[#1890ff];
        }
      }

      .el-sub-menu {
        .el-sub-menu__title:hover {
          @apply bg-[#1890ff];
        }

        .el-menu {
          @apply bg-[#20252a];
          .el-menu-item {
            a {
              @apply w-full;
            }

            &:hover {
              @apply bg-[#1890ff];
            }

            &.is-active {
              @apply bg-[#1890ff];
            }
          }
        }
      }
    }
  }

  .header {
    @apply flex items-center bg-white border-b-2 border-[#1890ff];
    .op-menu {
      @apply flex items-center cursor-pointer text-black;
    }
  }

  .main {
    @apply p-0;
    .content {
      @apply h-full bg-white;
      .tab-group {
        .el-tabs {
          @apply border-none;
          ::v-deep(.el-tabs__content) {
            @apply p-0;
          }
        }
      }
    }
  }
}
</style>
