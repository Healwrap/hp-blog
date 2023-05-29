import request from '@/utils/Request'

const apis = {
  loadBoard: '/board/loadBoard'
}

/**
 * 发表文章时获取板块列表
 * @returns {*}
 */
const loadBoard = () => {
  return request({
    url: apis.loadBoard,
    method: 'get',
    showLoading: false
  })
}

const boardApi = {
  loadBoard
}

export default boardApi
