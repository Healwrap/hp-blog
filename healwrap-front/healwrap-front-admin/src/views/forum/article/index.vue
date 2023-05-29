<template>
  <div class="article-panel">
    <div class="data-list">
      <Table
          ref="tableRef"
          class="table"
          :columns="columns"
          :data-source="tableData"
          :fetch="loadArticleList"
          :options="tableOptions"
          @row-selected="setRowSelected"
      >
        <!--用户信息-->
        <template #user="{ row }">
          <div class="user-info">
            <user-avatar :user-id="row.userId" :width="50"/>
            <div class="user-name">
              <user-link :username="row.nickName" :user-id="row.userId"/>
              <div class="address">{{ row.userIpAddress }}</div>
            </div>
          </div>
        </template>
        <!--封面-->
        <template #cover="{ row }">
          <el-image style="width: 80px; height: 80px" :src="proxy.$api.files.getImage(row.cover)" fit="cover"/>
        </template>
        <!--标题-->
        <template #title="{ row }">
          <Alink :src="appUrl + '/article/' + row.articleId" :content="row.title" :is-outer="true"/>
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
            <span v-if="row.commentCount" class="link">查看</span>
          </div>
        </template>
        <!--状态-->
        <template #status="{ row }">
          <el-tag v-if="row.topType === 1" effect="dark">已置顶</el-tag>
          <el-tag v-if="row.status === -1" type="danger">已删除</el-tag>
          <el-tag v-if="row.status === 0" type="info">待审核</el-tag>
          <el-tag v-if="row.status === 1" type="success">已审核</el-tag>
        </template>
        <template #attachment="{ row }">
          <span>{{ row.attachmentType === 1 ? '有附件' : '无附件' }}</span>
        </template>
        <!--操作-->
        <template #option="{ row }">
          <div v-if="row.status !== -1" class="options">
            <el-dropdown trigger="click">
              <icon icon="More"/>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>修改板块</el-dropdown-item>
                  <el-dropdown-item v-if="row.topType === 1 && row.status === 1">取消置顶</el-dropdown-item>
                  <el-dropdown-item v-if="row.topType === 0 && row.status === 1">置顶</el-dropdown-item>
                  <el-dropdown-item>删除</el-dropdown-item>
                  <el-dropdown-item v-if="row.status === 0">审核</el-dropdown-item>
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
import UserAvatar from '@/components/Avatar/components/UserAvatar.vue'
import UserLink from '@/components/UserLink/UserLink.vue'
import Icon from '@/components/Icon/Icon.vue'

const appUrl = import.meta.env.VITE_APP_URL
const {proxy} = getCurrentInstance()
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
const tableData = ref({})
const tableOptions = ref()
const loadArticleList = async () => {
  const result = await proxy.$api.forum.loadArticleList(tableData.value.pageNo, tableData.value.pageSize)
  if (!result) {
    return
  }
  tableData.value = result.data
}
// 设置行多选
const setRowSelected = rows => {
}
</script>

<style lang="scss" scoped>
.article-panel {
  @apply h-[calc(100%-39px)];
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
