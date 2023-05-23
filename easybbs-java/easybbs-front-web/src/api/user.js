import request from '@/utils/Request'
import { mergeParams } from '@/utils/Utils'

const apis = {
  getUserInfo: '/user/getUserInfo',
  getUserArticleList: '/user/getUserArticleList',
  updateUserInfo: '/user/updateUserInfo'
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



const userApi = {
  getUserInfo,
  getUserArticleList
}
export default userApi
