import request from '@/utils/Request'

const apis = {
  loadArticle: '/forum/loadArticle',
  getArticleDetail: '/forum/getArticleDetail',
  doLike: '/forum/doLike',
  getUserIntegral: '/getUserIntegral',
  attachmentDownload: '/forum/attachmentDownload'
}
const loadArticle = (pageNo, orderType, boardId, pBoardId) => {
  let params = {}
  if (pageNo !== null && pageNo !== undefined) {
    params.pageNo = pageNo
  }
  if (boardId !== null && boardId !== undefined) {
    params.boardId = boardId
  }
  if (pBoardId !== null && pBoardId !== undefined) {
    params.pBoardId = pBoardId
  }
  if (orderType !== null && orderType !== undefined) {
    params.orderType = orderType
  }
  return request({
    url: apis.loadArticle,
    method: 'get',
    params,
    showLoading: false
  })
}

const getArticleDetail = articleId => {
  return request({
    url: apis.getArticleDetail,
    method: 'get',
    params: {
      articleId
    }
  })
}

const doLike = articleId => {
  return request({
    url: apis.doLike,
    method: 'get',
    params: {
      articleId
    },
    showLoading: false
  })
}

const forumApi = {
  loadArticle,
  getArticleDetail,
  doLike
}
export default forumApi
