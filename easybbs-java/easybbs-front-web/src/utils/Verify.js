const regex = {
  email: /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/,
  number: /^[0-9]*$/,
  password: /^[0-9A-Za-z]{8,18}$/
}

const verify = (rule, value, reg, callback) => {
  if (value) {
    if (!reg.test(value)) {
      callback(new Error(rule.message))
    } else {
      callback()
    }
  } else {
    callback(new Error(rule.message))
  }
}

export default {
  email: (rule, value, callback) => {
    return verify(rule, value, regex.email, callback)
  },
  number: (rule, value, callback) => {
    return verify(rule, value, regex.number, callback)
  },
  password: (rule, value, callback) => {
    return verify(rule, value, regex.password, callback)
  }
}
