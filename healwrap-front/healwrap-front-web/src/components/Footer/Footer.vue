<template>
  <div v-if="show" class="footer">
    <div class="content" :style="{ width: proxy.$store.getters.contentWidth }">
      <el-row>
        <el-col :span="proxy.$store.getters.isMobile ? 10 : 6" class="item">
          <Logo class="logo" :show-rain-bow="false" color="white" />
          <div class="desc">愈屋</div>
          <div class="author">@He_Atom</div>
        </el-col>
        <el-col :span="proxy.$store.getters.isMobile ? 7 : 6" class="item">
          <div class="title">技术栈</div>
          <div class="desc">Vue</div>
          <div class="desc">SpringBoot</div>
          <div class="desc">Redis</div>
          <div class="desc">MySql</div>
        </el-col>
        <el-col v-if="proxy.$store.getters.isMobile === false" :span="6" class="item">
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
        <el-col :span="proxy.$store.getters.isMobile ? 7 : 6" class="item">
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
import { getCurrentInstance, nextTick, ref, watch } from 'vue'
import Logo from '@/components/Logo/Logo.vue'

const { proxy } = getCurrentInstance()
const show = ref(false)
watch(
  () => proxy.$store.getters.enterApp,
  e => {
    nextTick(() => {
      if (!e) {
        show.value = false
      } else {
        show.value = true
      }
    })
  },
  {
    immediate: true,
    deep: true
  }
)
</script>

<style lang="scss" scoped>
.footer {
  @apply relative top-[10vh] w-full h-[200px] bg-[rgba(0,0,0,0.8)];

  .content {
    @apply my-0 mx-auto;

    .logo {
      @apply mb-[10px];
    }

    .item {
      @apply p-[20px];

      .title {
        @apply text-[20px] text-white font-bold mb-[15px];
      }

      .desc {
        @apply text-[14px] text-[var(--text-color-1)] mb-[5px];

        .link {
          @apply relative text-[var(--color-primary)];
        }
      }

      .author {
        @apply text-[14px] text-[var(--text-color-1)] mb-[5px];
      }

      span {
        @apply mr-[10px];
      }
    }
  }
}
</style>
