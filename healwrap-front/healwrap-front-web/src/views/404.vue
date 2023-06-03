<template>
  <div>
    <div class="not-found">
      <div class="not-found-title">404</div>
      <div class="not-found-subtitle">页面不存在</div>
      <div class="not-found-btn">
        <router-link to="/">返回首页</router-link>
      </div>
    </div>
  </div>
</template>

<script setup></script>

<style lang="scss" scoped>
.not-found {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  .not-found-title {
    font-size: 100px;
    font-weight: 700;
    color: #f56c6c;
  }
  .not-found-subtitle {
    font-size: 20px;
    font-weight: 700;
    color: #f56c6c;
  }
  .not-found-btn {
    margin-top: 20px;
    a {
      padding: 10px 20px;
      border-radius: 5px;
      background-color: #f56c6c;
      color: #fff;
      &:hover {
        color: #fff;
      }
    }
  }
}
</style>
