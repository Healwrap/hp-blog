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
    params
  })
}

const postComment = (articleId, pCommentId, replyUserId, content) => {
  const params = mergeParams({ articleId: articleId, pCommentId: pCommentId, replyUserId: replyUserId, content: content })
  return request({
    url: apis.postComment,
    method: 'post',
    params
  })
}

const commentApis = {
  loadComment,
  postComment
}

export default commentApis
