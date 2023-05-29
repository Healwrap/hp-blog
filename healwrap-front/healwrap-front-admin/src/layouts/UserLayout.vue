<template>
  <div id="user-layout">
    <div class="container">
      <router-view class="user-form" />
    </div>
  </div>
</template>

<script setup></script>

<style lang="scss" scoped>
#user-layout {
  @apply flex justify-center items-center h-screen;
  .container {
    @apply w-[350px] mx-[auto];
    .user-form {
      @apply p-[20px] bg-white rounded-[10px];
    }
  }
}
</style>
