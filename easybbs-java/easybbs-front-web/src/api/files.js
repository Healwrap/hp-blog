import request from '@/utils/Request'
import { mergeParams } from '@/utils/Utils'

const apis = {
  getImage: '/api/files/getImage/',
  uploadImage: 'files/uploadImage'
}

/**
 * 获取图片
 * @param cover 图片路径
 * @returns {`/api/files/getImage/${string}`}
 */
const getImage = cover => {
  return `${apis.getImage}${cover}`
}

const uploadImage = file => {
  const params = mergeParams({ file })
  return request({
    url: apis.uploadImage,
    method: 'post',
    params
  })
}

const filesApi = {
  getImage,
  uploadImage
}

export default filesApi
