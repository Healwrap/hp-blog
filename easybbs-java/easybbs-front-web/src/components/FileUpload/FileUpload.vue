<template>
  <div class="file-upload">
    <template v-if="modelValue">
      <div class="file-name" :title="modelValue.name">
        {{ modelValue.name }}
      </div>
      <div class="iconfont icon-del" @click="handleRemove"></div>
    </template>
    <el-upload v-else name="file" :multiple="false" :show-file-list="false" accept=".zip,.rar" :http-request="uploadFile">
      <el-button type="primary">添加文件</el-button>
    </el-upload>
  </div>
</template>

<script setup>
const props = defineProps({
  modelValue: {
    type: Object,
    default: null
  }
})
const emit = defineEmits(['update:modelValue'])
const uploadFile = file => {
  emit('update:modelValue', file.file)
}
const handleRemove = () => {
  emit('update:modelValue', null)
}
</script>

<style lang="scss" scoped>
.file-upload {
  width: 100%;
  display: flex;
  justify-content: space-between;

  .file-name {
    flex: 1;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    color: #1e88e5;
  }

  .icon-del {
    font-size: 20px;
    color: #ff4d4f;
    margin-left: 10px;
    cursor: pointer;
  }
}
</style>
