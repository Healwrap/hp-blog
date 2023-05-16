<template>
  <div class="post-article">
    <el-form class="article-panel" ref="formDataRef" :model="formData" :rules="rules" label-width="60px">
      <div class="article-editor box">
        <el-card>
          <template #header>
            <span>正文</span>
          </template>
          <markdown-editor v-model="formData.content"/>
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
          <!--<el-form-item prop="cover" label="封面"></el-form-item>-->
          <el-form-item prop="summary" label="摘要">
            <el-input v-model="formData.title" placeholder="请输入标题" type="textarea" :row="5" resize="none"
                      clearable/>
          </el-form-item>
          <el-button type="primary" style="width: 100%">发布</el-button>
        </el-card>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import {ref} from 'vue'
import MarkdownEditor from '@/components/MarkdownEditor/MarkdownEditor.vue'

const formData = ref({})
const formDataRef = ref()
const rules = ref()
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
          padding: 10px;
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
