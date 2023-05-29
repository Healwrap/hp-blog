import { createRouter, createWebHistory } from 'vue-router'
import { asyncRouterMap, constantRouterMap } from '@/config/router.config'

// 将asyncRouterMap添加到constantRouterMap的baseLayout的children中
asyncRouterMap.forEach(item => {
  constantRouterMap[0].children.push(item)
})
// 创建路由
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: constantRouterMap
})
export default router
