import {BasicLayout, UserLayout} from '@/layouts'

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/',
    redirect: '/index',
    component: BasicLayout,
    children: []
  },
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    hidden: true,
    children: [
      {
        name: 'login',
        path: '/user/login',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/login/index.vue')
      },
      {
        name: 'register',
        path: '/user/register',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/register/index.vue')
      }
    ]
  },
  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404.vue')
  }
]

export const asyncRouterMap = [
  {
    name: '首页',
    path: '/index',
    meta: {
      icon: 'House'
    },
    component: () => import('@/views/index.vue')
  },
  {
    name: '内容管理',
    path: '/forum',
    redirect: '/forum/article', // TODO 对于空的路由，一定要加上redirect，否则会导致路由无法匹配
    meta: {
      icon: 'Document'
    },
    children: [
      {
        name: '文章管理',
        path: '/forum/article',
        component: () => import('@/views/forum/article/index.vue')
      },
      {
        name: '评论管理',
        path: '/forum/comment',
        component: () => import('@/views/forum/comment/index.vue')
      },
      {
        name: '板块管理',
        path: '/forum/board',
        component: () => import('@/views/forum/board/index.vue')
      }
    ]
  },
  {
    name: '用户管理',
    path: '/user',
    redirect: '/user/list',
    meta: {
      icon: 'User'
    },
    children: [
      {
        name: '用户列表',
        path: '/user/list',
        component: () => import('@/views/user/list/index.vue')
      }
    ]
  },
  {
    name: '设置',
    path: '/settings',
    redirect: '/settings/system',
    meta: {
      icon: 'Setting'
    },
    children: [
      {
        name: '系统设置',
        path: '/settings/system',
        component: () => import('@/views/settings/system/index.vue')
      }
    ]
  }
]
