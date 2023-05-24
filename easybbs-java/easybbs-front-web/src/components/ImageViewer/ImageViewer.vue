<template>
  <div class="image-viewer">
    <el-image-viewer v-if="previewImgIndex != null" :initial-index="previewImgIndex" :url-list="imageList" hide-on-click-modal @close="close" />
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  imageList: {
    type: Array,
    default: () => []
  }
})
const previewImgIndex = ref(null)
const show = index => {
  previewImgIndex.value = index
  handleMouseWheel(true)
}
const close = () => {
  previewImgIndex.value = null
  handleMouseWheel(false)
}
// 禁止滚动
const handleMouseWheel = flag => {
  if (flag) {
    document.body.style.overflow = 'hidden'
  } else {
    document.body.style.overflow = 'auto'
  }
}
defineExpose({
  show
})
</script>

<style scoped></style>
