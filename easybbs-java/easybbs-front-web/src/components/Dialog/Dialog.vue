<template>
  <div class="cust-dialog">
    <el-dialog
      :model-value="show"
      :draggable="true"
      :close-on-click-modal="false"
      :show-close="showClose"
      :title="title"
      @close="close"
      :width="width"
      :top="top"
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
          <el-button link @click="close" v-if="showCancel">取消</el-button>
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
  }
})
const emit = defineEmits()
const close = () => {
  emit('close')
}
</script>

<style lang="scss" scoped>
// 自定义弹窗
.cust-dialog {
  .dialog-body {
    border-top: 1px #ddd solid;
    padding: 20px;
    max-height: calc(100vh - 200px);
    overflow-y: auto;
  }
  .dialog-footer {
    text-align: right;
    padding: 10px 20px;
  }
}
</style>
