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
      <template #tip>
        <div class="el-upload__tip">只能上传zip/rar文件</div>
      </template>
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
  @apply flex justify-between w-full;

  .file-name {
    @apply flex-1 overflow-hidden overflow-ellipsis whitespace-nowrap text-[#1e88e5];
  }

  .icon-del {
    @apply text-[20px] text-[#ff4d4f] ml-[10px] cursor-pointer;
  }
}
</style>
