<template>
  <div class="post-article">
    <el-form class="article-panel" ref="formDataRef" :model="formData" :rules="rules" label-width="60px">
      <div class="article-editor box">
        <el-card>
          <template #header>
            <span>正文</span>
            <span @click="changeEditor"
              ><span class="iconfont icon-change"></span>&nbsp;切换为{{ editorType === 0 ? 'Markdown' : 'Html' }}编辑器</span
            >
          </template>
          <div class="editor">
            <html-editor v-if="editorType === 0" v-model:model-value="formData.content" />
            <markdown-editor v-else v-model:model-value="formData.markdownContent" @html-content="setHtmlContent" />
          </div>
        </el-card>
      </div>
      <div class="article-setting box">
        <el-card>
          <template #header>
            <span>设置</span>
          </template>
          <el-form-item prop="title" label="标题">
            <el-input v-model="formData.title" placeholder="请输入标题" clearable />
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
            <image-upload v-model:model-value="formData.cover" />
          </el-form-item>
          <el-form-item prop="summary" label="摘要">
            <el-input
              v-model="formData.summary"
              placeholder="请输入摘要"
              type="textarea"
              :row="5"
              maxlength="150"
              resize="none"
              show-word-limit
              clearable
            />
          </el-form-item>
          <el-form-item prop="attachment" label="附件">
            <file-upload v-model="formData.attachment" />
          </el-form-item>
          <el-form-item prop="integral" label="积分">
            <el-input v-model="formData.integral" clearable placeholder="请输入积分" />
            <div style="font-size: 13px; color: #999">附件下载积分，0表示无需积分即可下载</div>
          </el-form-item>
          <el-button type="primary" style="width: 100%" @click="postHandler">发布</el-button>
        </el-card>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { getCurrentInstance, nextTick, ref, watch } from 'vue'
import HtmlEditor from '@/components/HtmlEditor/HtmlEditor.vue'
import boardApi from '@/api/board'
import { useRoute } from 'vue-router'
import forumApi from '@/api/forum'

const route = useRoute()
const { proxy } = getCurrentInstance()
const formData = ref({})
const formDataRef = ref()
const rules = ref({
  title: [
    { required: true, message: '请输入标题', trigger: 'blur' },
    { min: 5, max: 50, message: '长度在 5 到 50 个字符', trigger: 'blur' }
  ],
  boardIds: [{ required: true, message: '请选择板块', trigger: 'change' }],
  summary: [
    { required: true, message: '请输入摘要', trigger: 'blur' },
    { min: 5, max: 200, message: '长度在 5 到 200 个字符', trigger: 'blur' }
  ]
})
// 板块信息
const boardProps = {
  multiple: false,
  checkStrictly: true,
  value: 'boardId',
  label: 'boardName'
}
// 文章id
const articleId = ref()
// 板块列表
const boardList = ref()
// 编辑器类型 0：富文本 1：markdown
const editorType = ref(Number(proxy.VueCookies.get('editorType')) || 0) // 这里从cookie中获取，是字符串，所以需要转为数字，否则使用`===`判断时会出错
// 提交信息
const postHandler = () => {
  formDataRef.value.validate(async valid => {
    if (!valid) {
      return
    }
    // 设置板块id
    const params = {}
    Object.assign(params, formData.value)
    console.log(params.boardIds.length)
    if (params.boardIds.length === 1) {
      params.pBoardId = params.boardIds[0]
    } else if (params.boardIds.length === 2) {
      params.pBoardId = params.boardIds[0]
      params.boardId = params.boardIds[1]
    }
    delete params.boardIds
    // 设置编辑器类型
    params.editorType = editorType.value
    // 设置内容
    const contentText = params.content.replace(/<(?!img).*?>/g, '')
    if (contentText.length < 5) {
      proxy.$message.error('正文不能为空')
      return
    }
    if (params.attachment != null) {
      params.attachmentType = 1
    } else {
      params.attachmentType = 0
    }
    console.log(params)
    // 封面
    if (!(params.cover instanceof File)) {
      delete params.cover
    }
    // 附件不是文件类型时，设置为空
    if (!(params.attachment instanceof File)) {
      delete params.attachment
    }
    console.log(params)
    const result = await forumApi.postArticle(
      params.title,
      params.pBoardId,
      params.summary,
      params.editorType,
      params.content,
      params.markdownContent,
      params.boardId,
      params.cover,
      params.attachment,
      params.integral
    )
  })
}
// 切换编辑器
const changeEditor = () => {
  proxy.Confirm('切换编辑器后，内容将不会保留，确定要切换吗？', () => {
    editorType.value = editorType.value === 0 ? 1 : 0
    formData.value.content = ''
    formData.value.markdownContent = ''
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
const getArticleDetail = () => {
  nextTick(async () => {
    formDataRef.value.resetFields()
    if (articleId.value) {
      const result = await forumApi.getArticleDetail4Update(articleId.value)
      if (!result) {
        return
      }
      const articleInfo = result.data.forumArticleVO
      // 设置编辑器
      editorType.value = articleInfo.editorType
      // 设置板块信息
      articleInfo.boardIds = []
      if (articleInfo.pBoardId != null && articleInfo.pBoardId !== 0) {
        articleInfo.boardIds.push(articleInfo.pBoardId)
      }
      if (articleInfo.boardId != null && articleInfo.boardId !== 0) {
        articleInfo.boardIds.push(articleInfo.boardId)
      }
      // 设置封面
      if (articleInfo.cover) {
        articleInfo.cover = { imageUrl: articleInfo.cover }
      }
      // 设置附件
      const attachmentInfo = result.data.forumArticleAttachmentVO
      if (attachmentInfo) {
        articleInfo.attachment = {
          name: attachmentInfo.fileName
        }
        articleInfo.integral = attachmentInfo.integral
      }
      formData.value = articleInfo
    }
  })
}
// 设置markdown编辑器的富文本信息
const setHtmlContent = htmlContent => {
  formData.value.content = htmlContent
}
// 当路径后有文章id时，表示是编辑文章
watch(
  () => route,
  newVal => {
    if (!newVal.params.articleId) {
      return
    }
    articleId.value = newVal.params.articleId
    getArticleDetail()
  },
  {
    immediate: true,
    deep: true
  }
)
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
