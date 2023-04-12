<template>
  <div class="loginAndRegister">
    <Dialog :show="dialogConfig.show" :title="dialogConfig.title" :buttons="dialogConfig.buttons" :width="dialogConfig.width">
      <!-- 登录表单 -->
      <el-form :model="formData" :rules="rules" ref="formDataRef">
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
      </el-form>
    </Dialog>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import api from '@/api/login'
const { proxy } = getCurrentInstance()
const router = useRouter()
const route = useRoute()

// 0：注册 1：登录 2：忘记密码
const onType = ref()
const showPanel = (type) => {
  onType.value = type
}
defineExpose({ showPanel })

// dialog配置
const dialogConfig = reactive({
  show: true,
  title: '登录',
  width: '400px'
})
// 表单数据
const formData = reactive({
  email: '',
  password: ''
})
// 验证码
const checkCodeUrl = ref(api.checkCode)
console.log(api.checkCode)
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
