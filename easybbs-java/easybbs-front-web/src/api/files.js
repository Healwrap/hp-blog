const apis = {
  getImage: '/api/files/getImage/',
  attachmentDownload: '/file/attachmentDownload'
}

/**
 * 获取图片
 * @param cover 图片路径
 * @returns {`/api/files/getImage/${string}`}
 */
const getImage = cover => {
  return `${apis.getImage}${cover}`
}

const attachmentDownload = fileId => {
  return `${apis.attachmentDownload}?fileId=${fileId}`
}

const filesApi = {
  getImage,
  attachmentDownload
}

export default filesApi
