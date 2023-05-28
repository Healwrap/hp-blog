<template>
  <div class="article-panel">
    <div class="data-list">
      <Table
          ref="tableRef"
          :columns="columns"
          :data-source="tableData"
          :fetch="loadDataList"
          :options="tableOptions"
          @row-selected="setRowSelected"
      />
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue'

const columns = ref([
  {
    label: '用户信息',
    prop: 'avatar',
    width: 200,
    scopedSlots: 'userInfo'
  },
  {
    label: '封面',
    width: '100',
    prop: 'cover',
    scopedSlots: 'cover'
  },
  {
    label: '标题',
    scopedSlots: 'titleInfo'
  },
  {
    label: '板块',
    width: 200,
    scopedSlots: 'boardInfo'
  },
  {
    label: '互动信息',
    width: 150,
    scopedSlots: 'interactionInfo'
  },
  {
    label: '是否有附件',
    prop: 'attachment',
    width: 100,
    scopedSlots: 'attachmentInfo'
  },
  {
    label: '状态信息',
    prop: 'status',
    width: 100,
    scopedSlots: 'statusInfo'
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
const loadDataList = () => {
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
  }
}
</style>
