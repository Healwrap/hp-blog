import router from '@/router'
import VueCookies from 'vue-cookies'
import NProgress from 'nprogress' // 进度条
import '@/components/NProgress/nprogress.scss' // 进度条自定义样式

NProgress.configure({ showSpinner: false }) // 进度条配置

const vueCookies = VueCookies
const allowList = ['login', 'register'] // 无重定向允许列表
const loginRoutePath = '/user/login'
const defaultRoutePath = '/index'

/**
 * 路由守卫 对路由进行权限控制 未登录时跳转到登录页
 */
router.beforeEach((to, from, next) => {
  NProgress.start() // 开启进度条
  // debugger
  if (vueCookies.get('userInfo')) {
    if (to.path === loginRoutePath || to.path === '/') {
      next({ path: defaultRoutePath })
      NProgress.done()
    } else {
      next()
    }
  } else {
    if (allowList.includes(to.name)) {
      // 在免登录名单，直接进入
      next()
    } else {
      // 无登录时跳转到登录
      next({ path: loginRoutePath, query: { redirect: to.fullPath } })
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 结束进度条
})
