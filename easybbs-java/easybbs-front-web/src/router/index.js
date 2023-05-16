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
          name: 'allArticle',
          component: () => import('@/views/forum/ArticleList/index.vue')
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
          component: () => import('@/views/Account/Center/index.vue')
        }
      ]
    }
  ]
})

export default router
