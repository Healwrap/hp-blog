<template>
  <div class="cust-dialog">
    <el-dialog
      class="dialog"
      :model-value="show"
      :draggable="draggable"
      :close-on-click-modal="false"
      :show-close="showClose"
      :title="title"
      :top="dialogConfig.top || top"
      :width="dialogConfig.width || width"
      :height="dialogConfig.height || height"
      @close="close"
      :style="{ height: dialogConfig.height || height }"
    >
      <!-- 弹窗标题 -->
      <template #header="{ titleId, titleClass }">
        <div class="dialog-title">
          <h3 :id="titleId" :class="titleClass">
            {{ title }}
          </h3>
        </div>
      </template>
      <!-- 弹窗主体 -->
      <div class="dialog-body">
        <slot></slot>
      </div>
      <!-- 弹窗底部按钮 -->
      <template v-if="(buttons && buttons.length > 0) || showCancel">
        <div class="dialog-footer">
          <el-button v-if="showCancel" link @click="close">取消</el-button>
          <!-- 自定义按钮 -->
          <el-button v-for="btn in buttons" :type="btn.type" @click="btn.click">
            {{ btn.text }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { nextTick, onMounted, reactive } from 'vue'

const dialogConfig = reactive({
  top: null,
  width: null,
  height: null
})
const props = defineProps({
  show: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: 'title'
  },
  showClose: {
    type: Boolean,
    default: true
  },
  width: {
    type: String,
    default: '35%'
  },
  height: {
    type: String,
    default: 'auto'
  },
  top: {
    type: String,
    default: '15vh'
  },
  buttons: {
    type: Array,
    default: () => []
  },
  showCancel: {
    type: Boolean,
    default: true
  },
  draggable: {
    type: Boolean,
    default: true
  }
})
const emit = defineEmits()
const close = () => {
  emit('close')
}
// 根据屏幕宽度设定弹窗大小
const setWidth = () => {
  nextTick(() => {
    const width = document.documentElement.clientWidth
    if (width < 600) {
      dialogConfig.top = '0'
      dialogConfig.width = '100vw'
      dialogConfig.height = '100vh'
    } else if (width < 768) {
      dialogConfig.top = '8vh'
      dialogConfig.width = '80%'
      dialogConfig.height = 'auto'
    } else {
      dialogConfig.top = null
      dialogConfig.width = null
    }
  })
}
onMounted(() => {
  setWidth()
  window.addEventListener('resize', setWidth)
})
</script>

<style lang="scss" scoped>
// 自定义弹窗
.cust-dialog {
  .dialog-body {
    @apply p-[20px] max-h-[calc(100vh-200px)] overflow-y-auto;
    border-top: 1px #ddd solid;
  }

  .dialog-footer {
    @apply text-right py-[10px] px-[20px];
  }
}
</style>
