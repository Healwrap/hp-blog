const apis = {
  getImage: '/api/files/getImage/'
}

/**
 * 获取图片
 * @param cover 图片路径
 * @returns {`/api/files/getImage/${string}`}
 */
const getImage = cover => {
  return `${apis.getImage}${cover}`
}

const filesApi = {
  getImage
}

export default filesApi
