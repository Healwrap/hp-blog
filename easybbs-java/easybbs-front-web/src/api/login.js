const apis = {
    checkCode: '/api/checkCode',
}
const checkCode = (opType) => {
    return `${apis.checkCode}?type=${opType}&time=${new Date().getTime()}`
}
const api = {
    checkCode,
}
export default api
