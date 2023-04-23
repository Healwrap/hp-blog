<template>
  <div class="UserDialog">
    <Dialog
        :show="dialogConfig.show"
        :title="dialogConfig.title"
        :buttons="dialogConfig.buttons"
        width="400px"
        :show-cancel="dialogConfig.showCancel"
        @close="dialogConfig.show = false"
    >
      <el-form ref="formDataRef" :model="formData" :rules="rules">
        <!--登录邮箱-->
        <el-form-item prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱" size="large" clearable>
            <template #prefix>
              <span class="iconfont icon-account"></span>
            </template>
          </el-input>
        </el-form-item>
        <!--登录密码-->
        <el-form-item v-if="opType === 0" prop="password">
          <el-input v-model="formData.password" :type="showPassword.login === false ? 'password' : 'text'"
                    placeholder="请输入密码" size="large">
            <template #prefix>
              <span class="iconfont icon-password"></span>
            </template>
            <template #suffix>
              <span
                  :class="['iconfont', showPassword.login === false ? 'icon-close-eye' : 'icon-eye']"
                  @click="showPassword.login = !showPassword.login"
              ></span>
            </template>
          </el-input>
        </el-form-item>
        <!--注册邮箱验证码-->
        <el-form-item v-if="opType === 1 || opType === 2" prop="emailCode">
          <div style="width: 100%; display: flex; justify-content: space-between">
            <el-input v-model="formData.emailCode" placeholder="请输入邮箱验证码" size="large" clearable>
              <template #prefix>
                <span class="iconfont icon-checkCode"></span>
              </template>
            </el-input>
            <el-button type="primary" size="large" style="margin-left: 5px" @click="showSendDialog"> 获取验证码
            </el-button>
          </div>
          <el-popover placement="left" :width="450" trigger="click">
            <template #reference>
              <el-link type="primary">未收到邮箱验证码？</el-link>
            </template>
            <div>
              <p>1、在垃圾箱中查找邮箱验证码</p>
              <p>2、在邮箱中头像->设置->反垃圾->白名单->设置邮件地址白名单</p>
              <p>3、将邮箱【179763003@qq.com】添加到白名单</p>
              <el-link type="primary">不知道怎么设置?</el-link>
            </div>
          </el-popover>
        </el-form-item>
        <!--注册昵称-->
        <el-form-item v-if="opType === 1" prop="nickName">
          <el-input v-model="formData.nickName" placeholder="请输入昵称" size="large">
            <template #prefix>
              <span class="iconfont icon-account"></span>
            </template>
          </el-input>
        </el-form-item>
        <!--注册密码 重复密码-->
        <el-form-item v-if="opType === 1 || opType === 2" prop="registerPassword">
          <el-input
              v-model="formData.registerPassword"
              :type="showPassword.register[0] === false ? 'password' : 'text'"
              placeholder="请输入密码"
              size="large"
          >
            <template #prefix>
              <span class="iconfont icon-password"></span>
            </template>
            <template #suffix>
              <span
                  :class="['iconfont', showPassword.register[0] === false ? 'icon-close-eye' : 'icon-eye']"
                  @click="showPassword.register[0] = !showPassword.register[0]"
              ></span>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item v-if="opType === 1 || opType === 2" prop="reRegisterPassword">
          <el-input
              v-model="formData.reRegisterPassword"
              :type="showPassword.register[1] === false ? 'password' : 'text'"
              placeholder="请再次输入密码"
              size="large"
          >
            <template #prefix>
              <span class="iconfont icon-password"></span>
            </template>
            <template #suffix>
              <span
                  :class="['iconfont', showPassword.register[1] === false ? 'icon-close-eye' : 'icon-eye']"
                  @click="showPassword.register[1] = !showPassword.register[1]"
              ></span>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="checkCode">
          <div style="width: 100%; display: flex; justify-content: space-between">
            <el-input v-model="formData.checkCode" placeholder="请输入验证码" size="large" clearable>
              <template #prefix>
                <span class="iconfont icon-checkCode"></span>
              </template>
            </el-input>
            <img :src="checkCodeUrl" alt="验证码" @click="changeCheckCode(0)"/>
          </div>
        </el-form-item>
        <el-form-item v-if="opType === 0">
          <el-checkbox v-model="formData.rememberMe"> 记住我？</el-checkbox>
        </el-form-item>
        <el-form-item v-if="opType === 0">
          <div style="width: 100%; display: flex; justify-content: space-between">
            <el-link type="primary" @click="showPanel(2)">忘记密码？</el-link>
            <el-link type="primary" @click="showPanel(1)">没有账号？</el-link>
          </div>
        </el-form-item>
        <el-form-item v-if="opType === 1">
          <el-link type="primary" @click="showPanel(0)">已有账号？</el-link>
        </el-form-item>
        <el-form-item v-if="opType === 2">
          <el-link type="primary" @click="showPanel(0)">去登录</el-link>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="handleBtnClick"
          >{{ opType === 0 ? '登录' : opType === 1 ? '注册' : '重置密码' }}
          </el-button>
        </el-form-item>
      </el-form>
    </Dialog>
    <Dialog
        :show="sendDialogConfig.show"
        :title="sendDialogConfig.title"
        :show-cancel="sendDialogConfig.showCancel"
        :buttons="sendDialogConfig.buttons"
        :width="sendDialogConfig.width"
        @close="sendDialogConfig.show = false"
    >
      <el-form ref="sendFormDataRef" :model="sendFormData" :rules="rules">
        <el-form-item label="邮箱">
          {{ sendFormData.email }}
        </el-form-item>
        <el-form-item label="验证码" prop="checkCode" style="align-items: center">
          <div style="width: 100%; display: flex; justify-content: space-between">
            <el-input v-model="sendFormData.checkCode" placeholder="请输入验证码" size="large" clearable>
              <template #prefix>
                <span class="iconfont icon-checkCode"></span>
              </template>
            </el-input>
            <img :src="sendCheckCodeUrl" alt="验证码" @click="changeCheckCode(1)"/>
          </div>
        </el-form-item>
      </el-form>
    </Dialog>
  </div>
</template>

<script setup>
import {defineExpose, getCurrentInstance, nextTick, reactive, ref} from 'vue'
import md5 from 'js-md5'
import userApi from '@/api/user'

/** ---------------------------data--------------------------------------*/
const {proxy} = getCurrentInstance()
// 元素ref
const formDataRef = ref(null)
const sendFormDataRef = ref(null)
// 操作类型 0-登录 1-注册 2-忘记密码
const opType = ref(0)
// 验证码 0:登录/注册验证码 1:发送邮箱验证码
const checkCodeUrl = ref(userApi.checkCode(0))
const sendCheckCodeUrl = ref(userApi.checkCode(1))
// 显示密码
const showPassword = ref({
  login: false,
  register: [false, false],
  forget: [false, false]
})
// 弹窗配置
const dialogConfig = reactive({
  show: false,
  title: '登录',
  showClose: true,
  width: '35%',
  top: '15vh',
  buttons: [],
  showCancel: false
})
// 发送弹窗配置
const sendDialogConfig = reactive({
  show: false,
  title: '发送验证码',
  showClose: true,
  top: '15vh',
  buttons: [
    {
      text: '发送',
      type: 'primary',
      click: () => {
        getEmailCode()
      }
    }
  ],
  showCancel: false
})
// 表单数据
const formData = ref({
  email: '',
  nickName: '',
  password: '',
  checkCode: '',
  emailCode: '',
  registerPassword: '',
  reRegisterPassword: '',
  rememberMe: false
})
// 发送邮箱验证码表单
const sendFormData = ref({
  email: '',
  checkCode: ''
})
// 检查密码
const checkRePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== formData.value.registerPassword) {
    callback(new Error('两次输入密码不一致!'))
  } else {
    callback()
  }
}
// 表单校验规则
const rules = {
  email: [
    {required: true, message: '请输入邮箱'},
    {max: 150, message: '邮箱长度不能超过150个字符'},
    {validator: proxy.Verify.email, message: '邮箱格式不正确'}
  ],
  password: [{required: true, message: '请输入密码'}],
  emailCode: [
    {required: true, message: '请输入邮箱验证码'},
    {max: 6, message: '邮箱验证码长度不能超过6个字符'}
  ],
  nickName: [
    {required: true, message: '请输入昵称'},
    {max: 20, message: '昵称长度不能超过20个字符'}
  ],
  registerPassword: [
    {required: true, message: '请输入密码'},
    {max: 20, message: '密码长度不能超过20个字符'},
    {validator: proxy.Verify.password, message: '密码格式不正确'}
  ],
  reRegisterPassword: [
    {required: true, message: '请再次输入密码'},
    {max: 20, message: '密码长度不能超过20个字符'},
    {validator: checkRePassword, message: '两次输入密码不一致!'}
  ],
  checkCode: [
    {required: true, message: '请输入验证码'},
    {max: 5, message: '验证码长度不能超过5个字符'}
  ]
}
/** ---------------------------method--------------------------------------*/
// 显示弹窗
const showPanel = type => {
  opType.value = type
  resetForm()
  if (opType.value === 0) {
    const cookieLoginInfo = proxy.VueCookies.get('loginInfo')
    if (cookieLoginInfo) {
      formData.value.rememberMe = true
    }
    formData.value.email = cookieLoginInfo ? cookieLoginInfo.email : ''
    formData.value.password = cookieLoginInfo ? cookieLoginInfo.password : ''
  }
}
// 显示发送邮箱验证码
const showSendDialog = () => {
  formDataRef.value.validateField('email', valid => {
    if (!valid) {
      return
    }
    sendDialogConfig.show = true
    nextTick(() => {
      changeCheckCode(1)
      sendFormData.value.email = formData.value.email
      formDataRef.value.resetFields(['checkCode'])
    })
  })
}
// 发送邮件
const getEmailCode = () => {
  sendFormDataRef.value.validateField('checkCode', async valid => {
    if (!valid) {
      return
    }
    const type = opType.value === 1 ? 0 : 1
    let result = await userApi.getEmailCode(formData.value.email, sendFormData.value.checkCode, type, changeCheckCode)
    if (!result) {
      return
    }
    sendDialogConfig.show = false
    proxy.Toast.success('发送成功')
  })
}
// 切换验证码
const changeCheckCode = type => {
  if (type === 0) {
    checkCodeUrl.value = userApi.checkCode(type)
  } else {
    sendCheckCodeUrl.value = userApi.checkCode(type)
  }
}
// 重置表单
const resetForm = () => {
  dialogConfig.show = true
  dialogConfig.title = opType.value === 0 ? '登录' : opType.value === 1 ? '注册' : '忘记密码'
  nextTick(() => {
    changeCheckCode(0)
    formDataRef.value.resetFields()
  })
}
// 按钮事件 登录/注册/忘记密码
const handleBtnClick = () => {
  formDataRef.value.validate(async valid => {
    if (!valid) {
      return
    }
    let result = null
    if (opType.value === 0) {
      const cookiePassword = proxy.VueCookies.get('loginInfo') ? proxy.VueCookies.get('loginInfo').password : ''
      const password = formData.value.password && formData.value.password !== cookiePassword ? md5(formData.value.password) : cookiePassword
      result = await userApi.login(formData.value.email, password, formData.value.checkCode, changeCheckCode)
    }
    if (opType.value === 1) {
      result = await userApi.register(
          formData.value.email,
          formData.value.nickName,
          formData.value.registerPassword,
          formData.value.checkCode,
          formData.value.emailCode,
          changeCheckCode
      )
    }
    if (opType.value === 2) {
      result = await userApi.resetPwd(
          formData.value.email,
          formData.value.registerPassword,
          formData.value.checkCode,
          formData.value.emailCode,
          changeCheckCode
      )
    }
    if (!result) {
      return
    }
    // 登录
    if (opType.value === 0) {
      if (formData.value.rememberMe) {
        const loginInfo = {
          email: formData.value.email,
          password: md5(formData.value.password)
        }
        proxy.VueCookies.set('loginInfo', loginInfo, '7d')
      } else {
        proxy.VueCookies.remove('loginInfo')
      }
      resetForm()
      dialogConfig.show = false
      proxy.Toast.success('登录成功')
      return
    }
    // 去登录
    if (opType.value === 1) {
      showPanel(0)
      proxy.Toast.success('注册成功')
    }
    if (opType.value === 2) {
      showPanel(0)
      proxy.Toast.success('重置密码成功')
    }
  })
}
defineExpose({
  showPanel
})
</script>

<style lang="scss" scoped>
img {
  margin-left: 5px;
  cursor: pointer;
}
</style>
