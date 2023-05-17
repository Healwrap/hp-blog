<template>
  <div class="html-editor">
    <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef" :default-config="toolbarConfig" :mode="mode" />
    <Editor :model-value="modelValue" :default-config="editorConfig" :mode="mode" @onCreated="handleCreated" @onChange="onChange" />
  </div>
</template>

<script setup>
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'
import { getCurrentInstance, onBeforeUnmount, ref, shallowRef } from 'vue'
import store from '@/store'
import filesApi from '@/api/files'

const { proxy } = getCurrentInstance()
const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  }
})
const mode = ref('default')
const editorRef = shallowRef()
const toolbarConfig = {
  excludeKeys: ['group-video']
}
const editorConfig = {
  placeholder: '请输入内容...',
  excludeKeys: ['group-video'],
  MENU_CONF: {
    uploadImage: {
      maxFileSize: 3 * 1024 * 1024,
      server: import.meta.env.VITE_BASE_URL + '/files/uploadImage',
      fieldName: 'file',
      customInsert(responseData, insetFn) {
        // 正常请求
        if (responseData.code === 200) {
          insetFn(filesApi.getImage(responseData.data.fileName), '', '')
        } else if (responseData.code === 901) {
          store.commit('UPDATE_USER_INFO', {})
          store.commit('showLoginDialog', true)
        } else {
          proxy.Toast.error(responseData.info)
        }
      }
    }
  }
}
const emit = defineEmits()
const onChange = editor => {
  emit('update:modelValue', editor.getHtml())
}
// 组件销毁时，销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor === null) {
    return
  }
  editor.destroy()
})
const handleCreated = editor => {
  editorRef.value = editor
}
</script>

<style lang="scss" scoped>
.html-editor {
  height: 79vh;

  ::v-deep(.v-md-editor) {
    box-shadow: none;
    height: 100%;
  }
}
</style>
