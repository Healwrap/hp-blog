<template>
  <div class="comment-panel">
    <div class="search-form">
      <el-form :model="searchFormData" label-width="50px">
        <el-row>
          <el-col :span="4">
            <el-form-item label="内容" prop="contentFuzzy">
              <el-input v-model="searchFormData.contentFuzzy" placeholder="输入内容" clearable @change="loadDataList"/>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="昵称" prop="nickNameFuzzy">
              <el-input v-model="searchFormData.nickNameFuzzy" placeholder="请输入昵称" clearable
                        @change="loadDataList"/>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="状态" prop="status">
              <el-select v-model="searchFormData.status" placeholder="请选择状态" clearable @change="loadDataList">
                <el-option label="已删除" :value="-1"/>
                <el-option label="待审核" :value="1"/>
                <el-option label="已审核" :value="0"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6" style="padding-left: 10px">
            <el-button-group>
              <el-button type="success" :disabled="selectBatchList.length === 0" @click="auditBatch"> 批量审核
              </el-button>
              <el-button type="danger" :disabled="selectBatchList.length === 0" @click="deleteBatch"> 批量删除
              </el-button>
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
          :fetch="loadDataList"
          :options="tableOptions"
          @row-selected="setRowSelected"
      >
        <!--用户信息-->
        <template #userInfo="{ row }">
          <div class="user-info">
            <user-avatar :user-id="row.userId" :width="50"/>
            <div class="user-name">
              <user-link :username="row.nickName" :user-id="row.userId"/>
            </div>
          </div>
        </template>
        <!--文章标题-->
        <template #articleTitle="{ row }">
          <alink :is-outer="true" :src="`${appUrl}/article/${row.articleId}`" :content="row.articleTitle"/>
        </template>
        <!--评论内容-->
        <template #content="{ row }">
          <div class="content" v-html="row.content"></div>
          <div v-if="row.imgPath">
            <comment-image v-if="item.imagePath" :src="proxy.$api.files.getImage(sub.imagePath)"/>
          </div>
        </template>
        <!--状态-->
        <template #status="{ row }">
          <el-tag v-if="row.topType === 1" effect="dark">已置顶</el-tag>
          <el-tag v-if="row.status === -1" type="danger">已删除</el-tag>
          <el-tag v-if="row.status === 0" type="info">待审核</el-tag>
          <el-tag v-if="row.status === 1" type="success">已审核</el-tag>
        </template>
        <!--操作-->
        <template #option="{ row }">
          <div v-if="row.status !== -1" class="options">
            <el-dropdown trigger="click">
              <icon icon="More"/>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item v-if="row.topType === 1 && row.status === 1" @click="changeTopType(row)">取消置顶
                  </el-dropdown-item>
                  <el-dropdown-item v-if="row.topType === 0 && row.status === 1" @click="changeTopType(row)">置顶
                  </el-dropdown-item>
                  <el-dropdown-item @click="deleteSingle(row)">删除</el-dropdown-item>
                  <el-dropdown-item v-if="row.status === 0" @click="auditSingle(row)">审核</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </template>
      </Table>
    </div>
  </div>
</template>

<script setup>
import {getCurrentInstance, ref} from 'vue'
import Alink from '@/components/UserLink/Alink/Alink.vue'
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'
import UserLink from '@/components/UserLink/UserLink.vue'
import CommentImage from '@/components/CommentImage/CommentImage.vue'

const {proxy} = getCurrentInstance()
const appUrl = import.meta.env.VITE_APP_URL
const tableRef = ref(null)
const selectBatchList = ref([])
const searchFormData = ref({})
const columns = [
  {
    label: '用户信息',
    prop: 'userInfo',
    width: 200,
    scopedSlots: 'userInfo'
  },
  {
    label: '文章标题',
    prop: 'articleTitle',
    width: 180,
    scopedSlots: 'articleTitle'
  },
  {
    label: '评论内容',
    prop: 'content',
    scopedSlots: 'content'
  },
  {
    label: '点赞',
    prop: 'goodCount',
    width: 100
  },
  {
    label: '状态',
    prop: 'status',
    width: 100,
    scopedSlots: 'status'
  },
  {
    label: '发布时间',
    prop: 'postTime',
    width: 240
  },
  {
    label: '发布地址',
    prop: 'userIpAddress',
    width: 100
  },
  {
    label: '操作',
    prop: 'option',
    width: 100,
    scopedSlots: 'option'
  }
]
const tableData = ref([])
const tableLoading = ref(true)
const tableOptions = {selectType: 'checkbox'}
// 加载数据列表
const loadDataList = async () => {
  tableLoading.value = true
  const params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize
  }
  Object.assign(params, searchFormData.value)
  const result = await proxy.$api.comment.loadCommentList(params)
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
    selectBatchList.value.push(row.commentId)
  })
}
const auditBatch = () => {
  proxy.$confirm('确定要审核评论么？', async () => {
    const result = await proxy.$api.comment.auditComment({commentIds: selectBatchList.value.join(',')})
    if (!result) {
      return
    }
    proxy.$message.success('审核成功')
    loadDataList()
  })
}
const auditSingle = row => {
  proxy.$confirm('确定要审核评论么？', async () => {
    const result = await proxy.$api.comment.auditComment({commentIds: row.commentId})
    if (!result) {
      return
    }
    proxy.$message.success('审核成功')
    loadDataList()
  })
}
const deleteBatch = () => {
  proxy.$confirm('确定要删除评论么？', async () => {
    const result = await proxy.$api.comment.deleteComment({commentIds: selectBatchList.value.join(',')})
    if (!result) {
      return
    }
    tableRef.value.clearSelection()
    proxy.$message.success('删除成功')
    loadDataList()
  })
}
const deleteSingle = row => {
  proxy.$confirm('确定要删除评论么？', async () => {
    const result = await proxy.$api.comment.deleteComment({commentIds: row.commentId})
    if (!result) {
      return
    }
    proxy.$message.success('删除成功')
    loadDataList()
  })
}
const changeTopType = row => {
}
</script>

<style lang="scss" scoped>
.comment-panel {
  @apply h-[calc(100%-100px)] pt-[10px] px-6;
  .data-list {
    @apply h-full;
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
</style>
