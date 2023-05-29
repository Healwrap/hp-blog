<template>
  <router-link v-if="!isOuter" class="link" :to="src">{{ content }}</router-link>
  <a v-else class="link" :href="src" target="_blank">{{ content }}</a>
</template>

<script setup>
defineProps({
  isOuter: {
    type: Boolean,
    default: false
  },
  src: {
    type: String
  },
  content: {
    type: String
  }
})
</script>

<style scoped>
.link {
  color: #1890ff;
  text-decoration: none;
}
</style>
