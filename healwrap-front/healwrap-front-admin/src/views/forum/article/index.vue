<template>
  <div class="article-panel">
    <div class="search-form">
      <el-form :model="searchFormData" label-width="50px">
        <el-row>
          <el-col :span="4">
            <el-form-item label="标题" prop="titleFuzzy">
              <el-input v-model="searchFormData.titleFuzzy" placeholder="请输入标题" clearable @change="loadArticleList" />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="昵称" prop="nickNameFuzzy">
              <el-input v-model="searchFormData.nickNameFuzzy" placeholder="请输入昵称" clearable @change="loadArticleList" />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="板块" prop="board">
              <el-cascader
                v-model="searchFormData.boardIds"
                :options="boardList"
                :props="boardProps"
                placeholder="请选择板块"
                clearable
                @change="loadArticleList"
              />
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="附件" prop="attachmentType">
              <el-select v-model="searchFormData.attachmentType" placeholder="请选择" clearable @change="loadArticleList">
                <el-option label="有附件" :value="1" />
                <el-option label="无附件" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="状态" prop="status">
              <el-select v-model="searchFormData.status" placeholder="请选择" clearable @change="loadArticleList">
                <el-option label="待审核" :value="0" />
                <el-option label="已审核" :value="1" />
                <el-option label="已删除" :value="-1" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="4">
            <el-form-item label="置顶" prop="topType">
              <el-select v-model="searchFormData.topType" placeholder="请选择" clearable @change="loadArticleList">
                <el-option label="未置顶" :value="0" />
                <el-option label="已置顶" :value="1" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6" style="padding-left: 10px">
            <el-button-group>
              <el-button type="success" :disabled="selectBatchList.length === 0" @click="auditBatch"> 批量审核 </el-button>
              <el-button type="danger" :disabled="selectBatchList.length === 0" @click="deleteBatch"> 批量删除 </el-button>
            </el-button-group>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="data-list">
      <Table
        ref="tableRef"
        class="table"
        :loading="tableLoading"
        :columns="columns"
        :data-source="tableData"
        :fetch="loadArticleList"
        :options="tableOptions"
        @row-selected="setRowSelected"
      >
        <!--用户信息-->
        <template #user="{ row }">
          <div class="user-info">
            <user-avatar :user-id="row.userId" :width="50" />
            <div class="user-name">
              <user-link :username="row.nickName" :user-id="row.userId" />
              <div class="address">{{ row.userIpAddress }}</div>
            </div>
          </div>
        </template>
        <!--封面-->
        <template #cover="{ row }">
          <el-image style="width: 80px; height: 80px" :src="proxy.$api.files.getImage(row.cover)" fit="cover" />
        </template>
        <!--标题-->
        <template #title="{ row }">
          <Alink :src="appUrl + '/article/' + row.articleId" :content="row.title" :is-outer="true" />
        </template>
        <!--板块-->
        <template #board="{ row }">
          <span>{{ row.pboardName }}</span>
          <span v-if="row.boardName">/{{ row.boardName }}</span>
        </template>
        <!--互动信息-->
        <template #interaction="{ row }">
          <div>阅读：{{ row.readCount }}</div>
          <div>点赞：{{ row.goodCount }}</div>
          <div>
            <span>评论：{{ row.commentCount }}</span>
            <span v-if="row.commentCount" class="link" @click="showCommentPanel(row.articleId)">查看</span>
          </div>
        </template>
        <!--状态-->
        <template #status="{ row }">
          <el-tag v-if="row.topType === 1" effect="dark">已置顶</el-tag>
          <el-tag v-if="row.status === -1" type="danger">已删除</el-tag>
          <el-tag v-if="row.status === 0" type="info">待审核</el-tag>
          <el-tag v-if="row.status === 1" type="success">已审核</el-tag>
        </template>
        <!--附件-->
        <template #attachment="{ row }">
          <span v-if="row.attachmentType === 0">无附件</span>
          <span v-else class="link" @click="showAttachmentPanel(row.nickName, row.articleId)">查看</span>
        </template>
        <!--操作-->
        <template #option="{ row }">
          <div v-if="row.status !== -1" class="options">
            <el-dropdown trigger="click">
              <icon icon="More" />
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="showBoardPanel(row)">修改板块</el-dropdown-item>
                  <el-dropdown-item v-if="row.topType === 1 && row.status === 1" @click="changeTopType(row)">取消置顶 </el-dropdown-item>
                  <el-dropdown-item v-if="row.topType === 0 && row.status === 1" @click="changeTopType(row)">置顶 </el-dropdown-item>
                  <el-dropdown-item @click="deleteSingle(row)">删除</el-dropdown-item>
                  <el-dropdown-item v-if="row.status === 0" @click="auditSingle(row)">审核</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </template>
      </Table>
    </div>
    <!--修改板块信息-->
    <board-panel ref="boardPanelRef" @update="loadArticleList" />
    <!--获取附件信息-->
    <attachment-panel ref="attachmentPanelRef" />
    <!--获取评论列表-->
    <comment-panel ref="commentPanelRef" />
  </div>
</template>

<script setup>
import { getCurrentInstance, ref } from 'vue'
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'
import UserLink from '@/components/UserLink/UserLink.vue'
import Icon from '@/components/Icon/Icon.vue'
import BoardPanel from './components/BoardPanel.vue'
import AttachmentPanel from '@/views/forum/article/components/AttachmentPanel.vue'
import CommentPanel from '@/views/forum/article/components/CommentPanel.vue'

const boardPanelRef = ref(null)
const attachmentPanelRef = ref(null)
const commentPanelRef = ref(null)
const tableRef = ref(null)
const appUrl = import.meta.env.VITE_APP_URL
const { proxy } = getCurrentInstance()
const boardList = ref([])
const boardProps = {
  multiple: false,
  checkStrictly: true,
  value: 'boardId',
  label: 'boardName'
}
const selectBatchList = ref([])
const searchFormData = ref({})
const tableData = ref({})
const tableLoading = ref(false)
const tableOptions = ref({
  selectType: 'checkbox'
})
const columns = ref([
  {
    label: '用户信息',
    prop: 'avatar',
    width: 150,
    scopedSlots: 'user'
  },
  {
    label: '封面',
    width: 100,
    prop: 'cover',
    scopedSlots: 'cover'
  },
  {
    label: '标题',
    scopedSlots: 'title'
  },
  {
    label: '板块',
    width: 150,
    scopedSlots: 'board'
  },
  {
    label: '互动信息',
    width: 150,
    scopedSlots: 'interaction'
  },
  {
    label: '状态信息',
    prop: 'status',
    width: 100,
    scopedSlots: 'status'
  },
  {
    label: '是否有附件',
    prop: 'attachment',
    width: 100,
    scopedSlots: 'attachment'
  },
  {
    label: '发布时间',
    prop: 'postTime',
    width: 180
  },
  {
    label: '操作',
    prop: 'option',
    width: 80,
    scopedSlots: 'option'
  }
])
// 加载板块列表
const loadBoardList = async () => {
  const result = await proxy.$api.board.loadBoard()
  if (!result) {
    return
  }
  boardList.value = result.data
}
loadBoardList()
// 加载文章列表
const loadArticleList = async () => {
  tableLoading.value = true
  const params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize
  }
  Object.assign(params, searchFormData.value)
  if (params.boardIds) {
    if (params.boardIds.length === 1) {
      params.pboardId = params.boardIds[0]
    } else if (params.boardIds.length === 2) {
      params.pboardId = params.boardIds[0]
      params.boardId = params.boardIds[1]
    }
  }
  delete params.boardIds
  const result = await proxy.$api.forum.loadArticleList(params)
  if (!result) {
    return
  }
  tableData.value = result.data
  tableLoading.value = false
}
// 设置行多选
const setRowSelected = rows => {
  selectBatchList.value = []
  rows.forEach(row => {
    selectBatchList.value.push(row.articleId)
  })
}
// 批量审核
const auditBatch = () => {
  proxy.$confirm('确定要审核选中的文章吗？', async () => {
    const result = await proxy.$api.forum.auditArticle(selectBatchList.value.join(','))
    if (!result) {
      return
    }
    tableRef.value.clearSelection()
    loadArticleList()
    proxy.$message.success('审核成功')
  })
}
// 批量删除
const deleteBatch = () => {
  proxy.$confirm('确定要删除选中的文章吗？', async () => {
    const result = await proxy.$api.forum.deleteArticle(selectBatchList.value.join(','))
    if (!result) {
      return
    }
    tableRef.value.clearSelection()
    loadArticleList()
    proxy.$message.success('删除成功')
  })
}
// 审核单个文章
const auditSingle = row => {
  proxy.$confirm(`确定要审核文章《${row.title}》吗？`, async () => {
    const result = await proxy.$api.forum.auditArticle(row.articleId)
    if (!result) {
      return
    }
    loadArticleList()
    proxy.$message.success('审核成功')
  })
}
// 删除单个文章
const deleteSingle = row => {
  proxy.$confirm(`确定要删除文章《${row.title}》吗？`, async () => {
    const result = await proxy.$api.forum.deleteArticle(row.articleId)
    if (!result) {
      return
    }
    loadArticleList()
    proxy.$message.success('删除成功')
  })
}
// 修改文章置顶状态
const changeTopType = row => {
  proxy.$confirm(`确定要${row.topType === 0 ? '置顶' : '取消置顶'}文章《${row.title}》吗？`, async () => {
    const result = await proxy.$api.forum.changeTopType(row.articleId, row.topType === 0 ? 1 : 0)
    if (!result) {
      return
    }
    loadArticleList()
    proxy.$message.success(`${row.topType === 0 ? '置顶' : '取消置顶'}成功`)
  })
}
// 显示板块编辑器
const showBoardPanel = row => {
  boardPanelRef.value.show(row)
}
// 显示附件信息
const showAttachmentPanel = (nickName, articleId) => {
  attachmentPanelRef.value.show(nickName, articleId)
}
// 显示评论信息
const showCommentPanel = articleId => {
  commentPanelRef.value.show(articleId)
}
</script>

<style lang="scss" scoped>
.article-panel {
  @apply h-[calc(100%-140px)] pt-[10px] px-6;
  .data-list {
    @apply h-full;
    .table {
      .user-info {
        @apply flex items-center;
        .user-name {
          @apply ml-2;
          .address {
            @apply text-xs text-gray-400;
          }
        }
      }
    }
  }
}
</style>
