import request from '@/utils/Request'
import { mergeParams } from '@/utils/Utils'

const apis = {
  loadBoard: '/board/loadBoard',
  loadBoard4Post: '/board/loadBoard4Post'
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
/**
 * 发表文章时获取板块列表
 * @returns {*}
 */
const loadBoard4Post = () => {
  return request({
    url: apis.loadBoard4Post,
    method: 'get',
    showLoading: false
  })
}

const boardApi = {
  loadBoard,
  loadBoard4Post
}

export default boardApi
