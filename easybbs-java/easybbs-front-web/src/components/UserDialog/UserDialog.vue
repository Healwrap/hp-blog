<template>
  <div class="UserDialog">
    <Dialog
        :show="true"
        :title="dialogConfig.title"
        :buttons="dialogConfig.buttons"
        :width="dialogConfig.width"
        :showCancel="dialogConfig.showCancel"
    >
      <!-- 登录表单 -->
      <el-form v-if="opType === 0" :model="formData" :rules="rules" ref="formDataRefLogin">
        <!-- 输入邮箱 -->
        <el-form-item prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱" size="large" clearable>
            <template #prefix>
              <span class="iconfont icon-account"></span>
            </template>
          </el-input>
        </el-form-item>
        <!-- 输入密码 -->
        <el-form-item prop="password">
          <el-input v-model="formData.password" placeholder="请输入密码" size="large" clearable>
            <template #prefix>
              <span class="iconfont icon-password"></span>
            </template>
          </el-input>
        </el-form-item>
        <!-- 输入验证码 -->
        <el-form-item prop="checkCode">
          <div class="check-code-panel" style="display: flex; justify-content: space-between">
            <el-input v-model="formData.checkCode" placeholder="请输入验证码" size="large" clearable>
              <template #prefix>
                <span class="iconfont icon-checkcode"></span>
              </template>
            </el-input>
            <img class="check-code" style="margin-left: 5px" :src="checkCodeUrl" alt="验证码"
                 @click="resetCheckCode(0)"/>
          </div>
        </el-form-item>
        <el-form-item>
          <div class="remember-panel" style="width: 100%; display: flex; justify-content: space-between">
            <el-checkbox v-model="formData.rememberMe">记住密码</el-checkbox>
            <el-link type="primary" @click="changeForm(2)">忘记密码？</el-link>
          </div>
        </el-form-item>
        <div class="account-pannel" style="display: flex; justify-content: right">
          <el-button type="primary" @click="">登录</el-button>
          <el-button @click="changeForm(1)">注册</el-button>
        </div>
      </el-form>
      <!-- 注册表单 -->
      <el-form v-if="opType === 1" :model="formData" ref="formDataRefRegister">
        <!-- 输入邮箱 -->
        <el-form-item prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱" size="large" clearable>
            <template #prefix>
              <span class="iconfont icon-account"></span>
            </template>
          </el-input>
        </el-form-item>
        <!-- 输入邮箱验证码 -->
        <el-form-item prop="emailCode">
          <div class="email-code-pannel"
               style="width: 100%; display: flex; justify-content: space-between; align-items: center">
            <el-input v-model="formData.emailCode" placeholder="请输入邮箱验证码" size="large" clearable>
              <template #prefix>
                <span class="iconfont icon-checkcode"></span>
              </template>
            </el-input>
            <!-- 获取验证码 -->
            <el-button type="primary" style="margin-left: 5px" size="large">获取验证码</el-button>
          </div>
        </el-form-item>
        <!-- 输入密码 -->
        <el-form-item prop="registerPassword">
          <el-input v-model="formData.registerPassword" placeholder="请输入密码" size="large" clearable>
            <template #prefix>
              <span class="iconfont icon-password"></span>
            </template>
          </el-input>
        </el-form-item>
        <!-- 再次输入密码 -->
        <el-form-item prop="reRegisterPassword">
          <el-input v-model="formData.reRegisterPassword" placeholder="请再次输入密码" size="large" clearable>
            <template #prefix>
              <span class="iconfont icon-password"></span>
            </template>
          </el-input>
        </el-form-item>
        <!-- 输入昵称 -->
        <el-form-item prop="nickName">
          <el-input v-model="formData.nickName" placeholder="请输入昵称" size="large" clearable>
            <template #prefix>
              <span class="iconfont icon-account"></span>
            </template>
          </el-input>
        </el-form-item>
        <!-- 输入验证码 -->
        <el-form-item prop="checkCode">
          <div class="check-code-panel" style="display: flex; justify-content: space-between">
            <el-input v-model="formData.checkCode" placeholder="请输入验证码" size="large" clearable>
              <template #prefix>
                <span class="iconfont icon-checkcode"></span>
              </template>
            </el-input>
            <img class="check-code" style="margin-left: 5px" :src="checkCodeUrl" alt="验证码"
                 @click="resetCheckCode(1)"/>
          </div>
        </el-form-item>
        <div class="bottom-pannel" style="display: flex; justify-content: right">
          <el-button type="primary" @click="">注册</el-button>
          <el-button @click="changeForm(0)">登录</el-button>
        </div>
      </el-form>
      <!--重置密码-->
      <el-form v-if="opType === 2" ref="formDataRefReset">
        <!-- 输入邮箱 -->
        <el-form-item prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱" size="large" clearable>
            <template #prefix>
              <span class="iconfont icon-account"></span>
            </template>
          </el-input>
        </el-form-item>
        <!-- 输入邮箱验证码 -->
        <el-form-item prop="emailCode">
          <div class="email-code-pannel"
               style="width: 100%; display: flex; justify-content: space-between; align-items: center">
            <el-input v-model="formData.emailCode" placeholder="请输入邮箱验证码" size="large" clearable>
              <template #prefix>
                <span class="iconfont icon-checkcode"></span>
              </template>
            </el-input>
            <!-- 获取验证码 -->
            <el-button type="primary" style="margin-left: 5px" size="large">获取验证码</el-button>
          </div>
        </el-form-item>
        <!-- 输入密码 -->
        <el-form-item prop="registerPassword">
          <el-input v-model="formData.registerPassword" placeholder="请输入密码" size="large" clearable>
            <template #prefix>
              <span class="iconfont icon-password"></span>
            </template>
          </el-input>
        </el-form-item>
        <!-- 再次输入密码 -->
        <el-form-item prop="reRegisterPassword">
          <el-input v-model="formData.reRegisterPassword" placeholder="请再次输入密码" size="large" clearable>
            <template #prefix>
              <span class="iconfont icon-password"></span>
            </template>
          </el-input>
        </el-form-item>
        <div class="bottom-pannel" style="display: flex; justify-content: right">
          <el-button type="primary" @click="">重置密码</el-button>
          <el-button @click="changeForm(0)">登录</el-button>
        </div>
      </el-form>
    </Dialog>
  </div>
</template>

<script setup>
import {getCurrentInstance, nextTick, reactive, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import api from "@/api/login";

const {proxy} = getCurrentInstance()
const router = useRouter()
const route = useRoute()
// 获取表单ref
const formDataRefLogin = ref(null)
const formDataRefRegister = ref(null)
const formDataRefReset = ref(null)
// dialog配置
const dialogConfig = reactive({
  show: true,
  title: '登录',
  width: '400px',
  showCancel: false
})
// 操作类型 0：登录 1：注册 2：重置密码
const opType = ref(0)
// 表单
const formData = reactive({
  email: '',
  password: '',
  checkCode: '',
  rememberMe: false,
  registerPassword: '',
  reRegisterPassword: '',
  nickName: '',
  emailCode: ''
})
// 验证码
const checkCodeUrl = ref(api.checkCode(1))
// 表单验证规则
const rules = {}
// 切换表单
const changeForm = (type) => {
  opType.value = type
  dialogConfig.title = type === 0 ? '登录' : type === 1 ? '注册' : '重置密码'
  nextTick(() => {
    if (type === 0) {
      formDataRefLogin.value.resetFields()
    } else if (type === 1) {
      formDataRefRegister.value.resetFields()
    } else {
      formDataRefReset.value.resetFields()
    }
  })
  resetCheckCode(type)
}
// 重置验证码
const resetCheckCode = (type) => {
  checkCodeUrl.value = api.checkCode(type)
}
</script>

<style lang="scss" scoped>
</style>
