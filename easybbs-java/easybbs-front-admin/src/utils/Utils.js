/**
 * 格式化文件大小
 * @param size 文件大小
 * @returns {string}
 */
export function formatFileSize(size) {
  if (size < 1024) {
    return size + ' B'
  } else if (size < 1024 * 1024) {
    return (size / 1024).toFixed(2) + ' KB'
  } else if (size < 1024 * 1024 * 1024) {
    return (size / (1024 * 1024)).toFixed(2) + ' MB'
  }
  return (size / (1024 * 1024 * 1024)).toFixed(2) + ' GB'
}

/**
 * 合并参数
 * @param params 参数
 * @returns {{}}
 */
export function mergeParams(params) {
  let result = {}
  for (const key in params) {
    const value = params[key]
    if (value !== null && value !== undefined) {
      if (typeof value === 'object') {
        if (Array.isArray(value)) {
          result[key] = value.filter(item => item !== null && item !== undefined)
        } else {
          result[key] = mergeParams(value)
        }
      } else {
        result[key] = value
      }
    }
  }
  return result
}
