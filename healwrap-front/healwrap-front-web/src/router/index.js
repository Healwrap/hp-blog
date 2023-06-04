import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'index',
      component: () => import('@/layout/BasicLayout.vue'),
      children: [
        {
          path: '/',
          name: 'index',
          component: () => import('@/views/index.vue')
        },
        {
          path: '/forum/:pBoardId',
          name: 'pBoard',
          component: () => import('@/views/forum/ArticleList/index.vue')
        },
        {
          path: '/forum/:pBoardId/:boardId',
          name: 'board',
          component: () => import('@/views/forum/ArticleList/index.vue')
        },
        {
          path: '/article/:articleId',
          name: 'article',
          component: () => import('@/views/forum/ArticleDetail/index.vue')
        },
        {
          path: '/postArticle',
          name: 'postArticle',
          component: () => import('@/views/forum/PostArticle/index.vue')
        },
        {
          path: '/postArticle/:articleId',
          name: 'editArticle',
          component: () => import('@/views/forum/PostArticle/index.vue')
        },
        {
          path: '/user/:userId',
          name: 'userCenter',
          component: () => import('@/views/User/Center/index.vue')
        },
        {
          path: '/user/message/:type',
          name: 'message',
          component: () => import('@/views/User/Message/index.vue')
        }
      ]
    },
    {
      path: '/:pathMatch(.*)*',
      name: '404',
      component: () => import('@/views/404.vue')
    }
  ]
})

export default router
