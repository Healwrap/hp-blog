import request from '@/utils/Request'
import { mergeParams } from '@/utils/Utils'
import { ElMessageBox } from 'element-plus'
import router from '@/router'

const apis = {
  loadArticle: '/forum/loadArticle',
  getArticleDetail: '/forum/getArticleDetail',
  doLike: '/forum/doLike',
  getUserDownloadInfo: '/forum/getUserDownloadInfo',
  attachmentDownload: '/api/forum/attachmentDownload',
  postArticle: '/forum/postArticle',
  getArticleDetail4Update: '/forum/articleDetail4Update',
  updateArticle: '/forum/updateArticle'
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
  const params = mergeParams({ pageNo, orderType, boardId, pBoardId })
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
/**
 * 下载附件
 * @param fileId
 * @returns {`/api/forum/attachmentDownload?fileId=${string}`}
 */
const attachmentDownload = fileId => {
  return `${apis.attachmentDownload}?fileId=${fileId}`
}
/**
 * 更新时获取文章详情
 * @param articleId 文章id
 * @returns {*}
 */
const getArticleDetail4Update = articleId => {
  return request({
    url: apis.getArticleDetail4Update,
    method: 'post',
    params: {
      articleId
    },
    showError: false,
    errorCallback: response => {
      ElMessageBox.alert(response.info, '错误', {
        showClose: false,
        callback: () => {
          router.go(-1)
        }
      })
    }
  })
}

const postArticle = (title, pBoardId, summary, editorType, content, markdownContent, boardId, cover, attachment, integral) => {
  const params = mergeParams({
    title,
    pBoardId,
    summary,
    editorType,
    content,
    markdownContent,
    boardId,
    cover,
    attachment,
    integral
  })
  return request({
    url: apis.postArticle,
    method: 'post',
    params
  })
}

const forumApi = {
  loadArticle,
  getArticleDetail,
  doLike,
  getUserDownloadInfo,
  attachmentDownload,
  getArticleDetail4Update,
  postArticle
}
export default forumApi
