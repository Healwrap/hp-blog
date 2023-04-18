<template>
  <!-- 彩虹字logo -->
  <div class="logo"><a href="/index">Easybbs</a></div>
</template>

<script setup></script>

<style lang="scss" scoped>
// 彩虹字logo
.logo {
  // 彩虹字logo
  font: 700 30px 思源黑体;
  color: #fff;
  margin-right: 10px;
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
