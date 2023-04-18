<template>
  <div class="loginAndRegister">
    <Dialog
      :show="dialogConfig.show"
      :title="dialogConfig.title"
      :buttons="dialogConfig.buttons"
      :width="dialogConfig.width"
      :showCancel="dialogConfig.showCancel"
    >
      <!-- 登录表单 -->
      <el-form v-if="opType == 0" :model="formData" :rules="rules" ref="formDataRef">
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
          <div class="check-code-panel">
            <el-input v-model="formData.checkCode" placeholder="请输入验证码" size="large" clearable>
              <template #prefix>
                <span class="iconfont icon-checkcode"></span>
              </template>
            </el-input>
            <img class="check-code" :src="checkCodeUrl" alt="验证码" />
          </div>
        </el-form-item>
        <el-form-item>
          <div class="remember-panel" style="width: 100%; display: flex; justify-content: space-between">
            <el-checkbox v-model="formData.rememberMe">记住密码</el-checkbox>
            <el-link type="primary">忘记密码？</el-link>
          </div>
        </el-form-item>
        <div class="account-pannel" style="display: flex; justify-content: right">
          <el-button type="primary" @click="">登录</el-button>
          <el-button @click="showPanel(1)">注册</el-button>
        </div>
      </el-form>
      <!-- 注册表单 -->
      <el-form v-if="opType == 1">
        <!-- 输入邮箱 -->
        <el-form-item prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱" size="large" clearable>
            <template #prefix>
              <span class="iconfont icon-account"></span>
            </template>
          </el-input>
        </el-form-item>
        <!-- 输入邮箱验证码 -->
        <el-form-item prop="email">
          <div class="email-code-pannel" style="width: 100%; display: flex; justify-content: space-between; align-items: center">
            <el-input v-model="formData.email" placeholder="请输入邮箱验证码" size="large" clearable>
              <template #prefix>
                <span class="iconfont icon-checkcode"></span>
              </template>
            </el-input>
            <!-- 获取验证码 -->
            <el-button type="primary" style="margin-left: 5px" size="large">获取验证码</el-button>
          </div>
        </el-form-item>
        <!-- 输入密码 -->
        <el-form-item prop="password">
          <el-input v-model="formData.password" placeholder="请输入密码" size="large" clearable>
            <template #prefix>
              <span class="iconfont icon-password"></span>
            </template>
          </el-input>
        </el-form-item>
        <!-- 再次输入密码 -->
        <el-form-item prop="password">
          <el-input v-model="formData.password" placeholder="请再次输入密码" size="large" clearable>
            <template #prefix>
              <span class="iconfont icon-password"></span>
            </template>
          </el-input>
        </el-form-item>
        <!-- 输入昵称 -->
        <el-form-item prop="email">
          <el-input v-model="formData.email" placeholder="请输入昵称" size="large" clearable>
            <template #prefix>
              <span class="iconfont icon-account"></span>
            </template>
          </el-input>
        </el-form-item>
        <!-- 输入验证码 -->
        <el-form-item prop="checkCode">
          <div class="check-code-panel">
            <el-input v-model="formData.checkCode" placeholder="请输入验证码" size="large" clearable>
              <template #prefix>
                <span class="iconfont icon-checkcode"></span>
              </template>
            </el-input>
            <img class="check-code" :src="checkCodeUrl" alt="验证码" />
          </div>
        </el-form-item>
        <div class="bottom-pannel" style="display: flex; justify-content: right">
          <el-button type="primary" @click="">注册</el-button>
          <el-button @click="showPanel(0)">登录</el-button>
        </div>
      </el-form>
    </Dialog>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import api from '@/api/login.js'
const { proxy } = getCurrentInstance()
const router = useRouter()
const route = useRoute()

// 0：登录 1：注册 2：忘记密码
const opType = ref(0)
const showPanel = (type) => {
  opType.value = type
}
defineExpose({ showPanel })
const rules = []
// dialog配置
const dialogConfig = reactive({
  show: true,
  title: '登录',
  width: '400px',
  showCancel: false
})
// 表单数据
const formData = reactive({
  email: '',
  password: ''
})
// 验证码
const checkCodeUrl = ref(api.checkCode + '?type=0')
</script>

<style lang="scss" scoped>
.loginAndRegister {
  .check-code-panel {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .check-code {
      width: 120px;
      margin: 5px;
    }
  }
}
</style>
