import request from '@/utils/Request'

const apis = {
  loadBoard: '/board/loadBoard'
}

const loadBoard = boardId => {
  let params = {}
  if (boardId !== undefined) {
    params.boardId = boardId
  }
  return request({
    url: apis.loadBoard,
    method: 'get',
    params
  })
}

const boardApi = {
  loadBoard
}

export default boardApi
