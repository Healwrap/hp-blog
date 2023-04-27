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
          name: 'all_article',
          component: () => import('@/views/forum/ArticleList/index.vue')
        }
      ]
    }
  ]
})

export default router
