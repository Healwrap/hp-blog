import { BasicLayout, UserLayout } from '@/layouts'

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/',
    component: BasicLayout
  },
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    hidden: true,
    children: [
      {
        name: 'login',
        path: '/login',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/login')
      },
      {
        name: 'register',
        path: '/register',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/register')
      }
    ]
  },

  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  }
]
