<template>
  <div class="image-upload">
    <el-upload
      name="file"
      :show-file-list="false"
      accept=".png, .PNG, .jpg, .JPG, .jpeg, .JPEG, .gif, .GIF, .bmp, .BMP"
      :multiple="false"
      :http-request="uploadFile"
    >
      <template v-if="localFile">
        <img :src="localFile" alt="image" />
      </template>
      <template v-else>
        <el-image v-if="modelValue && modelValue.imageUrl" :src="filesApi.getImage(modelValue.imageUrl)" alt="image" />
        <el-icon v-else>
          <Plus />
        </el-icon>
      </template>
    </el-upload>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import filesApi from '@/api/files'

defineProps({
  modelValue: {
    type: Object,
    default: null
  }
})
const localFile = ref(null)
const emit = defineEmits()
const uploadFile = file => {
  file = file.file
  let img = new FileReader()
  img.readAsDataURL(file)
  img.onload = ({ target }) => {
    localFile.value = target.result
  }
  emit('update:modelValue', file)
}
</script>

<style lang="scss" scoped>
.image-upload {
  width: 100px;
  height: 100px;
  border: 1px dashed #ccc;
  border-radius: 4px;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.3s;
  cursor: pointer;

  &:hover {
    border-color: #409eff;
  }

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  ::v-deep(.el-icon) {
    font-size: 30px;
    color: #999;
  }
}
</style>
