import router from '@/router'
import VueCookies from 'vue-cookies'
import Toast from '@/utils/Toast'

const vueCookies = VueCookies
// const allowList = ['login', 'register'] // 无重定向允许列表
const permissionList = ['postArticle', 'message'] // 权限列表
const defaultRoutePath = '/'

/**
 * 路由守卫 对路由进行权限控制 未登录时跳转到登录页
 */
router.beforeEach((to, from, next) => {
  // debugger
  if (vueCookies.get('loginInfo')) {
    next()
  } else {
    if (permissionList.includes(to.name)) {
      Toast.error('请先登录')
      next({ path: defaultRoutePath })
    } else {
      next()
    }
  }
})

router.afterEach(() => {})
