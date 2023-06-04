const getters = {
  isMobile: state => state.app.isMobile,
  theme: state => state.app.theme,
  contentWidth: state => state.app.contentWidth,
  headerHeight: state => state.app.headerHeight,
  enterApp: state => state.app.enterApp,
  messageCount: state => state.app.messageCount,
  userInfo: state => {
    return {
      token: state.user.token,
      nickName: state.user.nickName,
      userId: state.user.userId,
      admin: state.user.admin,
      avatar: state.user.avatar,
      province: state.user.province
    }
  },
  token: state => state.user.token,
  nickName: state => state.user.nickName,
  userId: state => state.user.userId,
  admin: state => state.user.admin,
  avatar: state => state.user.avatar,
  province: state => state.user.province
}
export default getters
