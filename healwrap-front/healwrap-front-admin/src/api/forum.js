import request from '@/utils/Request'
import { ElMessageBox } from 'element-plus'
import router from '@/router'

const apis = {
  loadArticleList: '/forum/loadArticleList',
  getArticleDetail: '/forum/getArticleDetail',
  getUserDownloadInfo: '/forum/getUserDownloadInfo',
  attachmentDownload: '/api/forum/attachmentDownload',
  getArticleDetail4Update: '/forum/articleDetail4Update',
  updateBoard: '/forum/updateBoard',
  auditArticle: '/forum/auditArticle',
  deleteArticle: '/forum/deleteArticle',
  changeTopType: 'forum/changeTopType',
  getAttachment: 'forum/getAttachment',
  loadCommentList: '/forum/loadCommentList'
}

/**
 * 获取文章列表
 * @param params 参数列表
 * @returns {*}
 */
const loadArticleList = params => {
  return request({
    url: apis.loadArticleList,
    method: 'post',
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

const updateBoard = params => {
  return request({
    url: apis.updateBoard,
    method: 'patch',
    params
  })
}

const auditArticle = articleIds => {
  return request({
    url: apis.auditArticle,
    method: 'patch',
    params: {
      articleIds
    },
    showLoading: false
  })
}

const deleteArticle = articleIds => {
  return request({
    url: apis.deleteArticle,
    method: 'delete',
    params: {
      articleIds
    },
    showLoading: false
  })
}

const changeTopType = (articleId, topType) => {
  return request({
    url: apis.changeTopType,
    method: 'patch',
    params: {
      articleId,
      topType
    },
    showLoading: false
  })
}

const getAttachment = articleId => {
  return request({
    url: apis.getAttachment,
    method: 'post',
    params: {
      articleId
    },
    showLoading: false
  })
}

const loadCommentList = articleId => {
  return request({
    url: apis.loadCommentList,
    method: 'post',
    params: {
      articleId
    },
    showLoading: false
  })
}

const forumApi = {
  loadArticleList,
  getArticleDetail,
  getUserDownloadInfo,
  attachmentDownload,
  getArticleDetail4Update,
  updateBoard,
  auditArticle,
  deleteArticle,
  changeTopType,
  getAttachment,
  loadCommentList
}
export default forumApi
