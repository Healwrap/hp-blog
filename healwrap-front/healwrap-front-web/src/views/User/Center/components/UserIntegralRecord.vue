<template>
  <div class="user-integral-record">
    <Dialog
      :show="dialogConfig.show"
      :title="dialogConfig.title"
      :width="dialogConfig.width"
      :top="dialogConfig.top"
      :buttons="dialogConfig.buttons"
      :show-cancel="dialogConfig.showCancel"
      :draggable="dialogConfig.draggable"
      @close="close"
    >
      <el-form ref="formDataRef" :model="formData">
        <el-form-item label="日期">
          <el-date-picker
            v-model="formData.createTimeRange"
            type="daterange"
            range-separator="~"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            clearable
            @change="loadRecord"
          />
        </el-form-item>
      </el-form>
      <el-tabs>
        <el-tab-pane label="图表">
          <echarts class="echarts" :option="option" />
        </el-tab-pane>
        <el-tab-pane label="列表">
          <div class="data-title">
            <div class="record-type">类型</div>
            <div class="integral">积分</div>
            <div class="create-type">时间</div>
          </div>
          <data-list :loading="loading" :data-source="recordInfo" @load-data="loadRecord">
            <template #default="{ data }">
              <div class="data-item">
                <div class="record-type">{{ data.operTypeName }}</div>
                <div class="integral" :style="{ color: data.integral > 0 ? 'green' : 'red' }">{{ data.integral }}</div>
                <div class="create-type">{{ data.createTime }}</div>
              </div>
            </template>
          </data-list>
        </el-tab-pane>
      </el-tabs>
    </Dialog>
  </div>
</template>

<script setup>
// 弹窗配置
import { getCurrentInstance, reactive, ref } from 'vue'
import DataList from '@/components/DataList/DataList.vue'

const { proxy } = getCurrentInstance()
const loading = ref(true)
const option = reactive({
  title: {
    text: '积分变化',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '积分变化',
      type: 'line',
      data: [],
      markLine: {
        silent: true,
        data: [
          {
            yAxis: 0
          }
        ]
      }
    }
  ]
})
const recordInfo = ref({})
const props = defineProps({})
const formDataRef = ref(null)
const formData = ref({})
const dialogConfig = reactive({
  show: false,
  title: '查看用户积分记录',
  top: '5vh',
  width: '24%',
  showCancel: false,
  draggable: false,
  buttons: [
    {
      type: 'primary',
      text: '确定',
      click: e => {
        close()
      }
    }
  ]
})
// 关闭弹窗
const close = () => {
  dialogConfig.show = false
  formData.value = {}
}
// 显示编辑个人信息弹窗
const showIntegralRecordDialog = () => {
  dialogConfig.show = true
  loadRecord()
}
// 加载用户积分记录
const loadRecord = async () => {
  loading.value = true
  let params = {
    pageNo: recordInfo.value.pageNo
  }
  if (formData.value.createTimeRange) {
    params.createTimeStart = formData.value.createTimeRange[0]
    params.createTimeEnd = formData.value.createTimeRange[1]
  }
  const result = await proxy.$api.user.getUserIntegralRecord(params.pageNo, params.createTimeStart, params.createTimeEnd)
  if (!result) {
    return
  }
  recordInfo.value = result.data
  option.xAxis.data = result.data.list.map(item => item.createTime.substr(0, 10))
  option.series[0].data = result.data.list.map(item => item.integral)
  loading.value = false
}
defineExpose({
  showIntegralRecordDialog
})
</script>

<style lang="scss" scoped>
.user-integral-record {
  .data-title {
    @apply flex justify-between px-[20px] mb-[10px];

    .record-type {
      @apply w-[30%];
    }

    .integral {
      @apply w-[30%];
    }

    .create-type {
      @apply w-[30%];
    }
  }

  .echarts {
    @apply w-full h-[300px];
  }

  .data-item {
    @apply flex justify-between px-[20px] py-[10px] cursor-pointer;
    transition: background-color 0.3s;

    &:hover {
      @apply bg-[#f5f5f5];
    }

    .record-type {
      @apply w-[30%];
    }

    .integral {
      @apply w-[20%];
    }

    .create-type {
      @apply w-[50%];
    }
  }
}
</style>
