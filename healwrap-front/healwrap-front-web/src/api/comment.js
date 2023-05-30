import request from '@/utils/request'
import { mergeParams } from '@/utils/Utils'

const apis = {
  loadComment: '/comment/loadComment',
  postComment: '/comment/postComment',
  doLike: '/comment/doLike',
  changeTopType: '/comment/changeTopType'
}
/**
 * 加载评论
 * @param articleId 文章id
 * @param pageNo 页码
 * @param orderType 排序类型
 * @returns {*}
 */
const loadComment = (articleId, pageNo, orderType) => {
  const params = mergeParams({ articleId: articleId, pageNo: pageNo, orderType: orderType })
  return request({
    url: apis.loadComment,
    method: 'get',
    params,
    showLoading: false
  })
}
/**
 * 发表评论
 * @param articleId 文章id
 * @param pCommentId 父评论id
 * @param replyUserId 回复用户id
 * @param content 评论内容
 * @returns {*}
 */
const postComment = (articleId, pCommentId, replyUserId, content) => {
  const params = mergeParams({ articleId: articleId, pCommentId: pCommentId, replyUserId: replyUserId, content: content })
  return request({
    url: apis.postComment,
    method: 'post',
    params,
    showLoading: false
  })
}
/**
 * 修改评论置顶类型
 * @param commentId 评论id
 * @param topType 置顶类型
 * @returns {*}
 */
const changeTopType = (commentId, topType) => {
  const params = mergeParams({ commentId: commentId, topType: topType })
  return request({
    url: apis.changeTopType,
    method: 'post',
    params,
    showLoading: false
  })
}
/**
 * 点赞
 * @param commentId 评论id
 * @returns {*}
 */
const doLike = commentId => {
  const params = mergeParams({ commentId: commentId })
  return request({
    url: apis.doLike,
    method: 'post',
    params,
    showLoading: false
  })
}

const commentApis = {
  loadComment,
  postComment,
  changeTopType,
  doLike
}

export default commentApis
