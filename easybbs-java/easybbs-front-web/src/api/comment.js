import request from '@/utils/request'

const apis = {
  loadComment: '/comment/loadComment',
  postComment: '/comment/postComment',
  doLike: '/comment/doLike',
  changeTopType: '/comment/changeTopType'
}

const loadComment = params => {
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
