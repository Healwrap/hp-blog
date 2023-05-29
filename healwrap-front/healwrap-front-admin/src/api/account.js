import request from '@/utils/Request'

const apis = {
  checkCode: '/api/checkCode',
  sendEmailCode: '/sendEmailCode',
  register: '/register',
  login: '/login',
  resetPwd: '/resetPwd',
  getUserInfo: '/getUserInfo',
  avatarUrl: '/api/files/getAvatar',
  logout: '/logout'
}

/**
 * 获取验证码
 * @param opType 类型 0:登录
 * @returns {`/api/checkCode?time=${number}`}
 */
const checkCode = () => {
  return `${apis.checkCode}?time=${new Date().getTime()}`
}

/**
 * 获取用户头像
 * @param userId 用户id
 * @returns {`/api/file/getAvatar/${string}`}
 */
const avatarUrl = userId => {
  return `${apis.avatarUrl}/${userId}`
}

/**
 * 获取邮箱验证码
 * @param email 邮箱
 * @param checkCode 验证码
 * @param type 类型 0:注册 1:重置密码
 * @param errorCallback 错误回调
 * @returns {*}
 */
const getEmailCode = (email, checkCode, type, errorCallback) => {
  return request({
    url: apis.sendEmailCode,
    method: 'post',
    params: {
      email,
      checkCode,
      type
    },
    errorCallback() {
      errorCallback(1)
    }
  })
}

/**
 * 注册
 * @param email 邮箱
 * @param nickName 昵称
 * @param password 密码
 * @param checkCode 验证码
 * @param emailCode 邮箱验证码
 * @param errorCallback 错误回调
 * @returns {*}
 */
const register = (email, nickName, password, checkCode, emailCode, errorCallback) => {
  return request({
    url: apis.register,
    method: 'post',
    params: {
      email,
      nickName,
      password,
      checkCode,
      emailCode
    },
    errorCallback() {
      errorCallback(0)
    }
  })
}

/**
 * 重置密码
 * @param email 邮箱
 * @param password 密码
 * @param checkCode 验证码
 * @param emailCode 邮箱验证码
 * @param errorCallback 错误回调
 * @returns {*}
 */
const resetPwd = (email, password, checkCode, emailCode, errorCallback) => {
  return request({
    url: apis.resetPwd,
    method: 'post',
    params: {
      email,
      password,
      checkCode,
      emailCode
    },
    errorCallback() {
      errorCallback(0)
    }
  })
}

/**
 * 登录
 * @param email 邮箱
 * @param password 密码
 * @param checkCode 验证码
 * @param errorCallback 错误回调
 * @returns {*}
 */
const login = (email, password, checkCode, errorCallback) => {
  return request({
    url: apis.login,
    method: 'post',
    params: {
      email,
      password,
      checkCode
    },
    errorCallback() {
      errorCallback()
    }
  })
}

/**
 * 退出登录
 * @returns {*}
 */
const logout = () => {
  return request({
    url: apis.logout,
    method: 'post'
  })
}

/**
 * 获取用户信息
 * @returns {*}
 */
const getUserInfo = () => {
  return request({
    url: apis.getUserInfo,
    method: 'get'
  })
}
const accountApi = {
  checkCode,
  getEmailCode,
  register,
  resetPwd,
  login,
  avatarUrl,
  logout,
  getUserInfo
}
export default accountApi
