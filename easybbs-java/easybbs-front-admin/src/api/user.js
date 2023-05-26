import request from '@/utils/Request'
import { mergeParams } from '@/utils/Utils'

const apis = {
  getUserInfo: '/user/getUserInfo',
  getUserArticleList: '/user/getUserArticleList',
  updateUserInfo: '/user/updateUserInfo',
  getUserIntegralRecord: '/user/getUserIntegralRecord',
  getMessageList: '/user/getMessageList',
  getMessageCount: '/user/getMessageCount'
}
/**
 * 获取用户信息
 * @param userId 用户id
 * @param errorCallback 错误回调
 * @returns {*}
 */
const getUserInfo = (userId, errorCallback) => {
  return request({
    url: apis.getUserInfo,
    method: 'post',
    params: { userId },
    showLoading: false,
    errorCallback
  })
}
/**
 * 获取用户文章列表
 * @param userId 用户id
 * @param type 类型 0:发表 1:评论 2:点赞
 * @param pageNo 页码
 * @returns {*}
 */
const getUserArticleList = (userId, type, pageNo) => {
  const params = mergeParams({ userId, type, pageNo })
  return request({
    url: apis.getUserArticleList,
    method: 'post',
    params,
    showLoading: false
  })
}
/**
 * 更新用户信息
 * @param sex 性别
 * @param description 个人描述
 * @param avatar 头像
 * @returns {*}
 */
const updateUserInfo = (sex, description, avatar) => {
  const params = mergeParams({ sex, description, avatar })
  return request({
    url: apis.updateUserInfo,
    method: 'put',
    params
  })
}
/**
 * 获取用户积分记录
 * @param pageNo 页码
 * @param createTimeStart 开始时间
 * @param createTimeEnd 结束时间
 * @returns {*}
 */
const getUserIntegralRecord = (pageNo, createTimeStart, createTimeEnd) => {
  const params = mergeParams({ pageNo, createTimeStart, createTimeEnd })
  return request({
    url: apis.getUserIntegralRecord,
    method: 'post',
    params,
    showLoading: false
  })
}

/**
 * 获取消息数量
 * @returns {*}
 */

const getMessageCount = () => {
  return request({
    url: apis.getMessageCount,
    method: 'get',
    showLoading: false
  })
}

/**
 * 获取用户消息列表
 * @param code 消息类型
 * @param pageNo 页码
 * @returns {*}
 */

const getMessageList = (code, pageNo) => {
  const params = mergeParams({ code, pageNo })
  return request({
    url: apis.getMessageList,
    method: 'post',
    params,
    showLoading: false
  })
}

const userApi = {
  getUserInfo,
  getUserArticleList,
  updateUserInfo,
  getUserIntegralRecord,
  getMessageList,
  getMessageCount
}
export default userApi
