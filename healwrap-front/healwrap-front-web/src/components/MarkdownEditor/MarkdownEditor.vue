<template>
  <div class="markdown-editor">
    <v-md-editor :model-value="modelValue" :disabled-menus="[]" @upload-image="uploadImageHandler" @change="change" />
  </div>
</template>

<script setup>
import VMdEditor from '@kangc/v-md-editor'
import '@kangc/v-md-editor/lib/style/base-editor.css'
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js'
import '@kangc/v-md-editor/lib/theme/style/github.css'
// highlightjs
import hljs from 'highlight.js'
import { ref } from 'vue'
import filesApi from '@/api/files'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  }
})
VMdEditor.use(githubTheme, {
  Hljs: hljs
})
const value = ref(props.modelValue)
const emit = defineEmits()
const change = (markdownContent, htmlContent) => {
  value.value = markdownContent
  emit('update:modelValue', markdownContent)
  emit('htmlContent', htmlContent)
}
const uploadImageHandler = async (event, insertImage, files) => {
  const result = await filesApi.uploadImage(files[0])
  if (!result) {
    return
  }
  const url = filesApi.getImage(result.data.fileName)
  insertImage({ url, desc: result.data.fileName })
}
</script>

<style lang="scss" scoped>
.markdown-editor {
  @apply h-[83vh];

  ::v-deep(.v-md-editor) {
    @apply h-full shadow-none;
  }
}
</style>
