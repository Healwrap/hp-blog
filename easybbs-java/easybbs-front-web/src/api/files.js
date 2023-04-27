const apis = {
  getImage: '/api/files/getImage/'
}

const getImage = cover => {
  return `${apis.getImage}${cover}`
}

const filesApi = {
  getImage
}

export default filesApi
