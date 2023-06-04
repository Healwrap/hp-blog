<template>
  <div class="image" :style="{ width: width, height: width }">
    <img v-if="!loadErr" :src="src" @error="handleError" :style="{ 'object-fit': fit }" />
    <div v-else class="image-error">
      <span class="error">加载错误</span>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

defineProps({
  src: {
    type: String,
    default: ''
  },
  width: {
    type: String,
    default: '100%'
  },
  fit: {
    type: String,
    default: 'contain'
  }
})
const loadErr = ref(false)
const handleError = () => {
  loadErr.value = true
}
</script>

<style lang="scss" scoped>
.image {
  @apply relative overflow-hidden bg-transparent;

  img {
    @apply w-full h-full;
    transition: all 0.5s;

    &:hover {
      transform: scale(1.06);
    }
  }

  .image-error {
    @apply w-full h-full bg-[#eee] flex justify-center items-center;

    .error {
      @apply text-[var(--text-color-2)] text-[18x];
    }
  }
}
</style>
