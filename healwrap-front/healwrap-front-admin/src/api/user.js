import request from '@/utils/Request'
import { mergeParams } from '@/utils/Utils'

const apis = {
  loadUserList: '/user/loadUserList',
  updateUserStatus: '/user/updateUserStatus',
  sendMessage: '/user/sendMessage'
}

const loadUserList = params => {
  return request({
    url: apis.loadUserList,
    method: 'get',
    params: mergeParams(params),
    showLoading: false
  })
}

const updateUserStatus = (userId, status) => {
  return request({
    url: apis.updateUserStatus,
    method: 'patch',
    params: {
      userId,
      status
    },
    showLoading: false
  })
}

const sendMessage = params => {
  return request({
    url: apis.sendMessage,
    method: 'post',
    params: mergeParams(params),
    showLoading: false
  })
}

const userApi = {
  loadUserList,
  updateUserStatus,
  sendMessage
}
export default userApi
