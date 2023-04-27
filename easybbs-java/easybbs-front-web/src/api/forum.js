import request from '@/utils/Request'

const apis = {
  loadArticle: '/forum/loadArticle'
}
const loadArticle = (pageNo = undefined, orderType = undefined, boardId = undefined, pBoardId = undefined) => {
  let params = {}
  if (pageNo !== undefined) {
    params.pageNo = pageNo
  }
  if (boardId !== undefined) {
    params.boardId = boardId
  }
  if (pBoardId !== undefined) {
    params.pBoardId = pBoardId
  }
  if (orderType !== undefined) {
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
