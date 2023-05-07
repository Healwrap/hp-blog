import request from '@/utils/Request'
import { mergeParams } from '@/utils/Utils'
const apis = {
  loadBoard: '/board/loadBoard'
}

/**
 * 获取板块
 * @param boardId 板块id
 * @returns {*}
 */
const loadBoard = boardId => {
  const params = mergeParams({ boardId })
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
