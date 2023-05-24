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
        <el-image v-if="modelValue" :src="imageUrl" alt="image" width="100" height="100" fit="cover" />
        <el-icon v-else style="position: absolute">
          <Plus />
        </el-icon>
      </template>
    </el-upload>
  </div>
</template>

<script setup>
import { getCurrentInstance, ref, watch } from 'vue'
import { Plus } from '@element-plus/icons-vue'

const { proxy } = getCurrentInstance()
const props = defineProps({
  modelValue: {
    type: Object,
    default: null
  },
  type: {
    type: String,
    default: 'image'
  }
})
const imageUrl = ref(null)
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
watch(
  () => props.modelValue,
  () => {
    if (props.modelValue instanceof String) {
      if (props.type === 'image') {
        imageUrl.value = proxy.$api.files.getImage(props.modelValue.imageUrl)
      } else if (props.type === 'avatar') {
        imageUrl.value = proxy.$api.account.avatarUrl(props.modelValue.userId)
      }
    }
  }
)
</script>

<style lang="scss" scoped>
.image-upload {
  position: relative;
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
    position: absolute;
    top: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  ::v-deep(.el-icon) {
    font-size: 30px;
    color: #999;
  }

  ::v-deep(.el-image) {
    width: 100px;
    height: 100px;
  }
}
</style>
