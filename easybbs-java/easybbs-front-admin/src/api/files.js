import request from '@/utils/Request'
import { mergeParams } from '@/utils/Utils'

const apis = {
  getImage: '/api/files/getImage',
  uploadImage: 'files/uploadImage'
}

/**
 * 获取图片
 * @param cover 图片路径
 * @returns {`/api/files/getImage/${string}`}
 */
const getImage = cover => {
  const trimmedCover = cover.trim().replace(/^\/|\/$/g, '') // 删除开头和结尾处的斜杠
  return `${apis.getImage}/${trimmedCover}`
}
/**
 * 上传图片
 * @param file 图片
 * @returns {*}
 */
const uploadImage = file => {
  const params = mergeParams({ file: file })
  return request({
    url: apis.uploadImage,
    method: 'post',
    params,
    dataType: 'file'
  })
}

const filesApi = {
  getImage,
  uploadImage
}

export default filesApi
