import request from '@/utils/Request'
import { mergeParams } from '@/utils/Utils'

const apis = {
  getSetting: '/setting/getSetting',
  saveSetting: '/setting/saveSetting'
}

const getSetting = () => {
  return request({
    url: apis.getSetting,
    method: 'get',
    showLoading: false
  })
}

const saveSetting = params => {
  return request({
    url: apis.saveSetting,
    method: 'patch',
    params: mergeParams(params),
    showLoading: false
  })
}

const settingApi = {
  getSetting,
  saveSetting
}

export default settingApi
