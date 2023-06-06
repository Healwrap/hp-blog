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

/**
 * 获取css变量
 * @param name 变量名
 * @returns {string}
 */
export function getCssVar(name) {
  return getComputedStyle(document.documentElement).getPropertyValue(name)
}

/**
 * 获取assets文件
 * @param url 文件路径
 * @returns {string}
 */
export function getAssetsFile(url) {
  return new URL(`../assets/${url}`, import.meta.url).href
}

/**
 * 切换主题
 * @param theme 主题
 */
export function changeTheme(theme) {
  // 切换主题
  document.documentElement.setAttribute('data-theme', theme)
  // 保存主题
  localStorage.setItem('theme', theme)
}
