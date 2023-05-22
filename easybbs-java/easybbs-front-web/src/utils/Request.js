import axios from 'axios'
import { ElLoading } from 'element-plus'
import Toast from '@/utils/Toast'
import store from '@/store'

const requestContentType = {
  json: 'application/json;charset=UTF-8',
  form: 'application/x-www-form-urlencoded;charset=UTF-8',
  file: 'multipart/form-data;charset=UTF-8'
}

const instance = axios.create({
  baseURL: import.meta.env.VITE_BASE_URL,
  timeout: 10 * 1000
})

// 错误代码枚举
const ERROR_CODE = {
  CODE_404: { code: 404, message: '请求地址不能存在' },
  CODE_600: { code: 600, message: '请求参数错误' },
  CODE_601: { code: 601, message: '信息已经存在' },
  CODE_500: { code: 500, message: '服务器返回错误，请联系管理员' },
  CODE_900: { code: 900, message: '请求超时' }
}

// 请求前拦截
let loading = null
instance.interceptors.request.use(
  config => {
    if (config.showLoading) {
      loading = ElLoading.service({
        lock: true,
        text: 'Loading...',
        background: 'rgba(0, 0, 0, 0.7)'
      })
    }
    return config
  },
  error => {
    if (error.config.showLoading && loading) {
      loading.close()
    }
    Toast.error('请求发送失败')
    return Promise.reject('请求发送失败')
  }
)

// 请求后拦截
instance.interceptors.response.use(
  response => {
    const { showLoading, errorCallback, showErr = true } = response.config
    if (showLoading && loading) {
      // 延时0.5s关闭
      setTimeout(() => {
        loading.close()
      }, 500)
    }
    const responseData = response.data
    if (responseData.code === 200) {
      return responseData
    } else if (responseData.code === 901) {
      store.commit('UPDATE_USER_INFO', {})
      store.commit('showLoginDialog', true)
      return Promise.reject({ showError: false, msg: '登录超时，请重新登录' })
    }
    const errCode = Object.keys(ERROR_CODE).find(key => ERROR_CODE[key].code === responseData.code)
    let errMsg = '未知错误'
    if (errCode) {
      errMsg = responseData.info || ERROR_CODE[errCode].message
    }
    if (showErr) {
      Toast.error(errMsg)
    }
    if (errorCallback) {
      errorCallback(responseData)
    }
    return Promise.reject({ showError: false, msg: errMsg })
  },
  error => {
    if (error.config.showLoading && loading) {
      // 延时0.5s关闭
      setTimeout(() => {
        loading.close()
      }, 500)
    }
    return Promise.reject({ showError: true, msg: '网络异常' })
  }
)

const request = config => {
  let { url, method, params, dataType, showLoading = true, errorCallback, showError = true } = config
  let contentType = requestContentType.form
  let headers = {
    'Content-Type': contentType,
    'X-Requested-With': 'XMLHttpRequest'
  }
  let formData = new FormData()
  // 判断请求方法是否为get
  if (method.toLowerCase() === 'get') {
    let paramsArray = []
    // 遍历参数对象，将参数加入到paramsArray中
    for (let key in params) {
      // eslint-disable-next-line no-prototype-builtins
      if (params.hasOwnProperty(key)) {
        paramsArray.push(key + '=' + params[key])
      }
    }
    // 拼接请求url
    if (paramsArray.length > 0) {
      url += '?' + paramsArray.join('&')
    }
  } else {
    // 请求方法不为get，则使用FormData处理请求数据
    for (let key in params) {
      formData.append(String(key), params[key] === undefined ? '' : params[key])
    }
    console.log('------------------------')
    console.log(params['file'] instanceof File)
    console.log('------------------------')
    console.log(formData.get('file') instanceof File)
    if (dataType !== null) {
      if (dataType === 'json') {
        contentType = requestContentType.json
      }
      if (dataType === 'file') {
        contentType = requestContentType.file
      }
    }
    headers['Content-Type'] = contentType
  }
  // debugger
  return instance({
    url: url,
    method: method,
    data: method.toLowerCase() === 'get' ? null : formData,
    headers: headers,
    showLoading: showLoading,
    errorCallback: errorCallback,
    showError: showError
  }).catch(error => {
    if (error.showError) {
      Toast.error(error.msg)
    }
    return null
  })
}

export default request
