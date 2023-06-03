import request from '@/utils/Request'

const apis = {
  getIntegralCount: '/data/getIntegralCount'
}
const getIntegralCount = () => {
  return request({
    url: apis.getIntegralCount,
    method: 'post'
  })
}
const dataApi = {
  getIntegralCount
}
export default dataApi
