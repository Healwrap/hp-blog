<template>
  <div class="footer">
    <div class="content" :style="{ width: proxy.$store.getters.contentWidth + 'px' }">
      <el-row>
        <el-col :span="6" class="item">
          <Logo class="logo" :show-rain-bow="false" color="white" />
          <div class="desc">一个分享知识的博客</div>
        </el-col>
        <el-col :span="6" class="item">
          <div class="title">技术栈</div>
          <div class="desc">Vue</div>
          <div class="desc">SpringBoot</div>
          <div class="desc">Redis</div>
          <div class="desc">MySql</div>
        </el-col>
        <el-col :span="6" class="item">
          <div class="title">相关链接</div>
          <div class="desc">
            <a class="link" href="https://vuejs.org/" target="_blank">Vue Framework</a>
          </div>
          <div class="desc">
            <a class="link" href="https://spring.io/" target="_blank">Spring</a>
          </div>
          <div class="desc">
            <a class="link" href="https://www.jxufe.edu.cn/" target="_blank">江西财经大学</a>
          </div>
        </el-col>
        <el-col :span="6" class="item">
          <div class="title">关注我</div>
          <div class="desc">
            <span>github</span>
            <a class="link" href="https://github.com/pepedd864" target="_blank">pepedd864</a>
          </div>
          <div class="desc">
            <span>bilibili</span>
            <a class="link" href="https://space.bilibili.com/442709221" target="_blank">He_Atom</a>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { getCurrentInstance } from 'vue'
import Logo from '@/components/Logo/Logo.vue'

const { proxy } = getCurrentInstance()
</script>

<style lang="scss" scoped>
.footer {
  position: relative;
  top: 10vh;
  width: 100%;
  height: 200px;
  background: #444;

  .content {
    margin: 0 auto;

    .logo {
      margin-bottom: 10px;
    }

    .item {
      padding: 20px;

      .title {
        font-size: 20px;
        font-weight: bold;
        margin-bottom: 15px;
        color: #fff;
      }

      .desc {
        font-size: 14px;
        margin-bottom: 5px;
        color: #ddd;

        .link {
          position: relative;
          color: #409eff;
        }
      }

      span {
        margin-right: 10px;
      }
    }
  }
}
</style>
