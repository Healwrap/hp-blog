import request from '@/utils/request'
import { mergeParams } from '@/utils/Utils'

const apis = {
  loadComment: '/comment/loadComment',
  postComment: '/comment/postComment',
  doLike: '/comment/doLike',
  changeTopType: '/comment/changeTopType'
}

const loadComment = (articleId, pageNo, orderType) => {
  const params = mergeParams({ articleId: articleId, pageNo: pageNo, orderType: orderType })
  console.log(params)
  return request({
    url: apis.loadComment,
    method: 'get',
    params
  })
}

const commentApis = {
  loadComment
}

export default commentApis
