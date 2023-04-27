const user = {
  state: {
    token: '',
    nickName: '',
    userId: '',
    admin: false,
    avatar: '',
    province: ''
  },
  mutations: {
    UPDATE_USER_INFO: (state, userInfo) => {
      state.token = 'token' in userInfo ? userInfo.token : null
      state.nickName = 'nickName' in userInfo ? userInfo.nickName : null
      state.userId = 'userId' in userInfo ? userInfo.userId : null
      state.admin = 'admin' in userInfo ? userInfo.admin : null
      state.avatar = 'avatar' in userInfo ? userInfo.avatar : null
      state.province = 'province' in userInfo ? userInfo.province : null
    },
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NICKNAME: (state, nickName) => {
      state.nickName = nickName
    },
    SET_USERID: (state, userId) => {
      state.userId = userId
    },
    SET_ADMIN: (state, admin) => {
      state.admin = admin
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_PROVINCE: (state, province) => {
      state.province = province
    }
  }
}
export default user
