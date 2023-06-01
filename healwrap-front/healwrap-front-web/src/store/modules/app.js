import { TOGGLE_CONTENT_WIDTH, TOGGLE_HEADER_HEIGHT, TOGGLE_MOBILE_TYPE, TOGGLE_THEME, UPDATE_MESSAGE_COUNT } from '@/store/mutation-types'

const app = {
  state: {
    isMobile: false,
    theme: 'light',
    contentWidth: 1100,
    headerHeight: 60,
    messageCount: {}
  },
  mutations: {
    [TOGGLE_MOBILE_TYPE]: (state, isMobile) => {
      state.isMobile = isMobile
    },
    [TOGGLE_THEME]: (state, theme) => {
      state.theme = theme
    },
    [TOGGLE_CONTENT_WIDTH]: (state, width) => {
      state.contentWidth = width
    },
    [TOGGLE_HEADER_HEIGHT]: (state, height) => {
      state.headerHeight = height
    },
    [UPDATE_MESSAGE_COUNT]: (state, messageCount) => {
      state.messageCount = messageCount
    }
  }
}
export default app
