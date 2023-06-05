import {
  ENTER_APP,
  TOGGLE_CONTENT_WIDTH,
  TOGGLE_EDITOR_TYPE,
  TOGGLE_HEADER_HEIGHT,
  TOGGLE_MOBILE_TYPE,
  TOGGLE_THEME,
  UPDATE_MESSAGE_COUNT
} from '@/store/mutation-types'

const app = {
  state: {
    isMobile: false,
    isEditor: false,
    theme: 'light',
    contentWidth: '1100px',
    headerHeight: '60px',
    enterApp: true,
    messageCount: {}
  },
  mutations: {
    [TOGGLE_MOBILE_TYPE]: (state, isMobile) => {
      state.isMobile = isMobile
    },
    [TOGGLE_EDITOR_TYPE]: (state, isEditor) => {
      state.isEditor = isEditor
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
    [ENTER_APP]: (state, flag) => {
      state.enterApp = flag
    },
    [UPDATE_MESSAGE_COUNT]: (state, messageCount) => {
      state.messageCount = messageCount
    }
  }
}
export default app
