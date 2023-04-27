import { TOGGLE_CONTENT_WIDTH, TOGGLE_HEADER_HEIGHT, TOGGLE_MOBILE_TYPE, TOGGLE_THEME } from '@/store/mutation-types'

const app = {
  state: {
    isMobile: false,
    theme: 'light',
    contentWidth: '1200px',
    headerHeight: '60px'
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
    }
  }
}
export default app
