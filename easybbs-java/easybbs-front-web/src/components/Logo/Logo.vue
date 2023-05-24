<template>
  <!-- 彩虹字logo -->
  <div :class="['logo', showRainBow ? 'rainbow' : '']">
    <router-link to="/" :style="{ color: color }">Easybbs</router-link>
  </div>
</template>

<script setup>
defineProps({
  showRainBow: {
    type: Boolean,
    default: true
  },
  color: {
    type: String,
    default: 'black'
  }
})
</script>

<style lang="scss" scoped>
.logo {
  font: 700 30px 思源黑体;
  margin-right: 10px;

  a {
    text-decoration: none;
  }
}

// 彩虹字logo
.rainbow {
  // 彩虹字logo
  color: #fff;
  background: -webkit-linear-gradient(30deg, #32c5ff 25%, #b620e0 50%, #f7b500 75%, #20e050 100%);
  background-clip: text;
  transition: all 0.3s;
  background-position: 0% 82%;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  // 添加hover效果
  &:hover {
    background: -webkit-linear-gradient(30deg, #32c5ff 25%, #b620e0 50%, #f7b500 75%, #20e050 100%);
    background-clip: text;
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-position: 100% 19%;
    animation: rainbow 150s linear infinite;
  }

  // 添加动画
  @keyframes rainbow {
    0% {
      background-position: 0;
    }
    50% {
      background-position: 2800px;
    }
    100% {
      background-position: 0;
    }
  }
}
</style>
