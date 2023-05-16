<template>
  <el-image
      class="comment-img"
      :src="src"
      :zoom-rate="1.2"
      :preview-src-list="previewImg ? previewImg : [src]"
      :initial-index="0"
      fit="scale-down"
      loading="lazy"
      hide-on-click-modal
  />
</template>

<script setup>
const props = defineProps({
  src: {
    type: String,
    required: true
  },
  previewImg: {
    type: Array
  }
})
</script>

<style lang="scss" scoped>
.comment-img {
  width: 80px;
  height: 80px;
}
</style>
