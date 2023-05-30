<template>
  <div class="table">
    <el-table
      ref="dataTableRef"
      :style="{ height: `calc(100% - ${extHeight})` }"
      :data="dataSource.list || []"
      highlight-current-row
      @row-click="handleRowClick"
      @selection-change="handleSelectionChange"
    >
      <!--选择框-->
      <el-table-column
        v-if="options.selectType && options.selectType === 'checkbox'"
        type="selection"
        width="50"
        align="center"
        @change="handleSelectionChange"
      />
      <!--序号-->
      <el-table-column v-if="options.showIndex" label="序号" type="index" width="60" align="center" />
      <!--数据列-->
      <template v-for="(column, index) in columns">
        <template v-if="column.scopedSlots">
          <el-table-column :key="index" :prop="column.prop" :label="column.label" :align="column.align || 'left'" :width="column.width">
            <template #default="scope">
              <slot :name="column.scopedSlots" :index="scope.$index" :row="scope.row"></slot>
            </template>
          </el-table-column>
        </template>
        <template v-else>
          <el-table-column
            :key="index"
            :prop="column.prop"
            :label="column.label"
            :align="column.align || 'left'"
            :width="column.width"
            :fixed="column.fixed"
          />
        </template>
      </template>
    </el-table>
    <!--分页-->
    <div v-if="showPagination" class="pagination">
      <el-pagination
        v-if="dataSource.totalCount"
        v-model:current-page="dataSource.pageNo"
        style="text-align: right"
        :total="dataSource.totalCount"
        :page-size="dataSource.pageSize"
        layout="total,prev,pager,next,jumper"
        @current-change="handlePageNoChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const dataTableRef = ref(null)
const props = defineProps({
  dataSource: {
    type: Object,
    default: () => ({})
  },
  extHeight: {
    type: String,
    default: '39px'
  },
  showPagination: {
    type: Boolean,
    default: true
  },
  options: {
    type: Object,
    default: () => {
      return {
        stripe: true,
        border: false,
        showIndex: false
      }
    }
  },
  columns: {
    type: Array,
    default: () => []
  },
  fetch: {
    type: Function
  },
  initFetch: {
    type: Boolean,
    default: true
  }
})
const init = () => {
  if (props.initFetch && props.fetch) {
    props.fetch()
  }
}
init()
// 清除选中
const clearSelection = () => {
  dataTableRef.value.clearSelection()
}
// 设置行选中
const setCurrentRow = (rowKey, rowValue) => {
  const row = props.dataSource.list.find(item => {
    return item[rowKey] === rowValue
  })
  dataTableRef.value.setCurrentRow(row)
}
// 将子组件暴露出去，否则父组件无法调用
defineExpose({ setCurrentRow, clearSelection })
const emit = defineEmits(['rowSelected', 'rowClick'])
// 行点击
const handleRowClick = row => {
  emit('rowClick', row)
}
// 多选
const handleSelectionChange = row => {
  emit('rowSelected', row)
}
// 切换页码
const handlePageNoChange = pageNo => {
  props.dataSource.pageNo = pageNo
  props.fetch()
}
</script>

<style lang="scss" scoped>
.table {
  @apply h-full;
}
</style>
