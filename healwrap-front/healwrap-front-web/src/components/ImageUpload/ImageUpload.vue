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
  @apply relative w-[100px] h-[100px] rounded-[4px] flex justify-center items-center cursor-pointer;
  border: 1px dashed #ccc;
  transition: all 0.3s;

  &:hover {
    @apply border-[#409eff];
  }

  img {
    @apply absolute top-0 w-full h-full object-cover;
  }

  ::v-deep(.el-icon) {
    @apply text-[30px] text-[#999];
  }

  ::v-deep(.el-image) {
    @apply w-[100px] h-[100px];
  }
}
</style>
