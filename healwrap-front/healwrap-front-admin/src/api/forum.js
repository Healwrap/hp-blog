import request from '@/utils/Request'
import { ElMessageBox } from 'element-plus'
import router from '@/router'

const apis = {
  loadArticleList: '/forum/loadArticleList',
  getArticleDetail: '/forum/getArticleDetail',
  getUserDownloadInfo: '/forum/getUserDownloadInfo',
  attachmentDownload: '/api/forum/attachmentDownload',
  getArticleDetail4Update: '/forum/articleDetail4Update',
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

const forumApi = {
  loadArticleList,
  getArticleDetail,
  getUserDownloadInfo,
  attachmentDownload,
  getArticleDetail4Update
}
export default forumApi
