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
    // 是否显示登录弹窗
    showLoginDialog: false
  },
  mutations: {
    showLoginDialog(state, showLoginDialog) {
      state.showLoginDialog = showLoginDialog
    }
  },
  actions: {},
  getters
})
