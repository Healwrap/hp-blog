import { createRouter, createWebHistory } from 'vue-router'
import { constantRouterMap ,asyncRouterMap} from '@/config/router.config'

// 将asyncRouterMap添加到constantRouterMap的baseLayout的children中
constantRouterMap[0].children = asyncRouterMap
// 创建路由
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: constantRouterMap
})
export default router
