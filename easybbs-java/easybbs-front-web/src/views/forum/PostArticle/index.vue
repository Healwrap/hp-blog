<template>
  <div class="post-article">
    <el-form class="article-panel" ref="formDataRef" :model="formData" :rules="rules">
      <div class="article-editor box">
        <el-card>
          <template #header>
            <span>正文</span>
            <span @click="changeEditor"
            ><span class="iconfont icon-change"></span>&nbsp;切换为{{
                editorType === 0 ? 'Markdown' : 'Html'
              }}编辑器</span
            >
          </template>
          <div class="editor">
            <html-editor v-if="editorType === 0" v-model:model-value="formData.content"/>
            <markdown-editor v-else v-model="formData.markdownContent"/>
          </div>
        </el-card>
      </div>
      <div class="article-setting box">
        <el-card>
          <template #header>
            <span>设置</span>
          </template>
          <el-form-item prop="title" label="标题">
            <el-input v-model="formData.title" placeholder="请输入标题" clearable/>
          </el-form-item>
          <el-form-item prop="boardIds" label="板块">
            <el-cascader
                v-model="formData.boardIds"
                :options="boardList"
                :props="boardProps"
                placeholder="请选择板块"
                clearable
                filterable
                show-all-levels
                collapse-tags
            />
          </el-form-item>
          <el-form-item prop="cover" label="封面">
            <image-upload/>
          </el-form-item>
          <el-form-item prop="summary" label="摘要">
            <el-input
                v-model="formData.title"
                placeholder="请输入标题"
                type="textarea"
                :row="5"
                maxlength="150"
                resize="none"
                show-word-limit
                clearable
            />
          </el-form-item>
          <el-form-item prop="attachment" label="附件">
            <file-upload v-model="formData.attachment"/>
          </el-form-item>
          <el-button type="primary" style="width: 100%">发布</el-button>
        </el-card>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import {getCurrentInstance, ref} from 'vue'
import HtmlEditor from '@/components/HtmlEditor/HtmlEditor.vue'
import boardApi from '@/api/board'

const {proxy} = getCurrentInstance()
const formData = ref({})
const formDataRef = ref()
const rules = ref({
  title: [
    {required: true, message: '请输入标题', trigger: 'blur'},
    {min: 5, max: 50, message: '长度在 5 到 50 个字符', trigger: 'blur'}
  ],
  boardIds: [{required: true, message: '请选择板块', trigger: 'change'}],
  summary: [
    {required: true, message: '请输入摘要', trigger: 'blur'},
    {min: 5, max: 200, message: '长度在 5 到 200 个字符', trigger: 'blur'}
  ]
})
// 板块信息
const boardProps = {
  multiple: false,
  checkStrictly: true,
  value: 'boardId',
  label: 'boardName'
}
const boardList = ref()
// 编辑器类型 0：富文本 1：markdown
const editorType = ref(Number(proxy.VueCookies.get('editorType')) || 0) // 这里从cookie中获取，是字符串，所以需要转为数字，否则使用`===`判断时会出错
console.log(proxy.VueCookies.get('editorType'))
const changeEditor = () => {
  proxy.Confirm('切换编辑器后，内容将不会保留，确定要切换吗？', () => {
    editorType.value = editorType.value === 0 ? 1 : 0
    formData.value.content = ''
    formData.value.markdownContent = ''
    console.log(editorType.value)
    proxy.VueCookies.set('editorType', editorType.value, -1)
  })
}
const loadBoardList = async () => {
  const result = await boardApi.loadBoard4Post()
  if (!result) {
    return
  }
  boardList.value = result.data
}
loadBoardList()
</script>

<style lang="scss" scoped>
.post-article {
  .article-panel {
    display: flex;

    .box {
      margin: 10px 5px;

      ::v-deep(.el-card) {
        height: 90vh;
        padding: 0;
        overflow: auto;

        .el-card__header {
          display: flex;
          justify-content: space-between;
          padding: 10px;
          // 切换编辑器按钮样式
          span:nth-child(2) {
            color: #1e88e5;
            cursor: pointer;
          }
        }
      }
    }

    .article-editor {
      flex: 1;

      ::v-deep(.el-card__body) {
        padding: 0;
      }
    }

    .article-setting {
      width: 300px;
    }
  }
}
</style>
