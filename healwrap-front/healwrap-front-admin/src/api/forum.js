import request from '@/utils/Request'
import { mergeParams } from '@/utils/Utils'
import { ElMessageBox } from 'element-plus'
import router from '@/router'

const apis = {
  loadArticleList: '/forum/loadArticleList',
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
 * @param pageSize 每页条数
 * @returns {*}
 */
const loadArticleList = (pageNo, pageSize) => {
  const params = mergeParams({ pageNo, pageSize })
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
/**
 * 发布文章
 * @param title 标题
 * @param pBoardId 父板块id
 * @param summary 摘要
 * @param editorType 编辑器类型
 * @param content html内容
 * @param markdownContent markdown内容
 * @param boardId 板块id
 * @param cover 封面
 * @param attachment 附件
 * @param integral 积分
 * @returns {*}
 */
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
    params,
    dataType: 'file'
  })
}
/**
 * 更新文章
 * @param articleId 文章id
 * @param title 标题
 * @param pBoardId 父板块id
 * @param summary 摘要
 * @param editorType 编辑器类型
 * @param attachmentType 附件类型
 * @param content html内容
 * @param markdownContent markdown内容
 * @param boardId 板块id
 * @param cover 封面
 * @param attachment 附件
 * @param integral 积分
 * @returns {*}
 */
const updateArticle = (
  articleId,
  title,
  pBoardId,
  summary,
  editorType,
  attachmentType,
  content,
  markdownContent,
  boardId,
  cover,
  attachment,
  integral
) => {
  if (articleId === null) {
    throw new Error('articleId不能为空')
  }
  const params = mergeParams({
    articleId,
    title,
    pBoardId,
    summary,
    editorType,
    attachmentType,
    content,
    markdownContent,
    boardId,
    cover,
    attachment,
    integral
  })
  return request({
    url: apis.updateArticle,
    method: 'post',
    params,
    dataType: 'file'
  })
}

const forumApi = {
  loadArticleList,
  getArticleDetail,
  doLike,
  getUserDownloadInfo,
  attachmentDownload,
  getArticleDetail4Update,
  postArticle,
  updateArticle
}
export default forumApi
