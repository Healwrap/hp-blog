<template>
  <div class="data-list">
    <div class="no-data">
      <el-empty v-if="!loading && dataSource.list.length === 0" :description="desc" />
    </div>
    <div v-for="item in dataSource.list" :key="item.articleId">
      <slot :data="item"></slot>
    </div>
    <el-pagination
      :current-page="dataSource.pageNo"
      :total="dataSource.totalCount"
      :page-size="dataSource.pageSize"
      layout="prev, pager, next"
      hide-on-single-page
      @current-change="handlePageNoChange"
    />
  </div>
</template>

<script setup>
const props = defineProps({
  dataSource: {
    type: Object
  },
  loading: {
    type: Boolean,
    required: true
  },
  rows: {
    type: Number,
    default: 4
  },
  desc: {
    type: String,
    default: '暂无数据'
  }
})
const emit = defineEmits(['loadData'])
const handlePageNoChange = pageNo => {
  // eslint-disable-next-line vue/no-mutating-props
  props.dataSource.pageNo = pageNo // TODO 修改了父组件的值，不推荐
  emit('loadData')
}
</script>

<style lang="scss" scoped>
.data-list {
  transition: all 0.3s;

  ::v-deep(.el-pagination) {
    @apply bg-transparent;
    button {
      @apply bg-transparent;
    }

    .el-pager {
      @apply bg-transparent;
      li {
        @apply bg-transparent;
      }
    }
  }
}
</style>
