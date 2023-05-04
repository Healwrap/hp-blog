import request from '@/utils/Request'

const apis = {
  loadArticle: '/forum/loadArticle',
  getArticleDetail: '/forum/getArticleDetail',
  doLike: '/forum/doLike',
  getUserDownloadInfo: '/forum/getUserDownloadInfo',
  attachmentDownload: '/forum/attachmentDownload'
}

/**
 * 获取文章列表
 * @param pageNo 页码
 * @param orderType 排序类型
 * @param boardId 板块id
 * @param pBoardId 父板块id
 * @returns {*}
 */
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

/**
 * 获取文章详情
 * @param articleId 文章id
 * @returns {*}
 */

const getArticleDetail = articleId => {
  return request({
    url: apis.getArticleDetail,
    method: 'get',
    params: {
      articleId
    }
  })
}

/**
 * 点赞
 * @param articleId 文章id
 * @returns {*}
 */
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
/**
 * 获取用户积分
 * @param fileId 文件id
 * @returns {*}
 */
const getUserDownloadInfo = fileId => {
  return request({
    url: apis.getUserDownloadInfo,
    method: 'post',
    params: {
      fileId
    },
    showLoading: false
  })
}

const attachmentDownload = fileId => {
  return `${apis.attachmentDownload}?fileId=${fileId}`
}

const forumApi = {
  loadArticle,
  getArticleDetail,
  doLike,
  getUserDownloadInfo,
  attachmentDownload
}
export default forumApi
