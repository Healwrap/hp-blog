<template>
  <div class="chart">
    <v-echart class="chart" :option="option" autoresize :style="{ height: height + 'px' }" />
  </div>
</template>

<script setup>
// ------基础文件---------
import VEchart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
// ------可选文件---------
import { BarChart, BoxplotChart, CandlestickChart, HeatmapChart, LineChart, RadarChart, SankeyChart, ScatterChart } from 'echarts/charts'
import {
  CalendarComponent,
  GridComponent,
  LegendComponent,
  MarkLineComponent,
  TitleComponent,
  ToolboxComponent,
  TooltipComponent,
  VisualMapComponent
} from 'echarts/components'

use([
  CanvasRenderer,
  BarChart,
  GridComponent,
  RadarChart,
  HeatmapChart,
  LineChart,
  ScatterChart,
  CandlestickChart,
  BoxplotChart,
  SankeyChart,
  TooltipComponent,
  VisualMapComponent,
  TitleComponent,
  LegendComponent,
  ToolboxComponent,
  MarkLineComponent,
  CalendarComponent
])
defineProps({
  option: {
    type: Object,
    default: () => {}
  },
  height: {
    type: Number,
    default: 300
  }
})
</script>

<style lang="scss" scoped>
.box {
  @apply flex justify-items-start;
}
</style>
