import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: import('@/layouts/UserLayout.vue'),
      children: [
        {
          path: '',
          component: import('@/views/User/Login/index.vue')
        }
      ]
    }
  ]
})

export default router
