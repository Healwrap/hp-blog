import request from '@/utils/Request'

const apis = {
  loadArticle: '/forum/loadArticle'
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

const forumApi = {
  loadArticle
}
export default forumApi
