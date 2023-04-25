import request from '@/utils/Request'

const apis = {
  loadBoard: '/board/loadBoard'
}

const loadBoard = boardId => {
  return request({
    url: apis.loadBoard,
    method: 'get',
    params: {
      boardId
    }
  })
}

const boardApi = {
  loadBoard
}

export default boardApi
