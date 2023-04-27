import { createStore } from 'vuex'
import app from './modules/app'
import user from './modules/user'
import getters from './getters'

export default createStore({
  modules: {
    app,
    user
  },
  state: {
    // 用户信息
    loginUserInfo: null,
    // 是否显示登录弹窗
    showLoginDialog: false
  },
  mutations: {
    updateGlobalInfo(state, globalInfo) {
      state.globalInfo = globalInfo
    },
    updateLoginUserInfo(state, loginUserInfo) {
      state.loginUserInfo = loginUserInfo
    },
    showLoginDialog(state, showLoginDialog) {
      state.showLoginDialog = showLoginDialog
    }
  },
  actions: {},
  getters
})
