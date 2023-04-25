import { createStore } from 'vuex'

export default createStore({
  state: {
    // 用户信息
    loginUserInfo: null,
    // 是否显示登录弹窗
    showLoginDialog: false
  },
  mutations: {
    updateLoginUserInfo(state, loginUserInfo) {
      state.loginUserInfo = loginUserInfo
    },
    showLoginDialog(state, showLoginDialog) {
      state.showLoginDialog = showLoginDialog
    }
  },
  actions: {},
  getters: {
    getLoginUserInfo(state) {
      return state.loginUserInfo
    }
  }
})
