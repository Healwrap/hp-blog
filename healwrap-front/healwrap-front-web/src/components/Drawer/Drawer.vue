<template>
  <el-drawer :model-value="show" :size="width" @close="close">
    <slot></slot>
  </el-drawer>
</template>

<script setup>
import {defineProps, nextTick, onMounted, ref, watch} from 'vue'
import {useRoute} from 'vue-router'

const route = useRoute()
const width = ref('30%')
const props = defineProps({
  show: {
    type: Boolean,
    default: true
  }
})
// 根据屏幕宽度设定弹窗大小
const setWidth = () => {
  nextTick(() => {
    const screenWidth = document.documentElement.clientWidth
    if (screenWidth < 600) {
      width.value = '100vw'
    } else if (screenWidth < 768) {
      width.value = '80%'
    } else {
      width.value = '30%'
    }
  })
}
// 关闭
const emit = defineEmits(['close'])
const close = () => {
  emit('close')
}
onMounted(() => {
  setWidth()
  window.addEventListener('resize', setWidth)
})
// 监听路由变化
watch(
    () => route.path,
    () => {
      console.log('path change')
      close()
    },
    {immediate: true}
)
</script>

<style lang="scss" scoped></style>
