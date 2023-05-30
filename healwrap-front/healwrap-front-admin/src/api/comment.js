import request from '@/utils/request'
import { mergeParams } from '@/utils/Utils'

const apis = {
  loadCommentList: '/comment/loadCommentList',
  changeTopType: '/comment/changeTopType',
  deleteComment: '/comment/deleteComment',
  auditComment: '/comment/auditComment'
}

const loadCommentList = params => {
  return request({
    url: apis.loadCommentList,
    method: 'post',
    params,
    showLoading: false
  })
}

const changeTopType = (commentId, topType) => {
  const params = mergeParams({ commentId: commentId, topType: topType })
  return request({
    url: apis.changeTopType,
    method: 'post',
    params
  })
}

const deleteComment = params => {
  return request({
    url: apis.deleteComment,
    method: 'delete',
    params,
    showLoading: false
  })
}

const auditComment = params => {
  return request({
    url: apis.auditComment,
    method: 'patch',
    params,
    showLoading: false
  })
}

const commentApis = {
  loadCommentList,
  changeTopType,
  deleteComment,
  auditComment
}

export default commentApis
