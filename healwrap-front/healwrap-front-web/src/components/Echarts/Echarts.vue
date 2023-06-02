<template>
  <div class="chart">
    <v-echart class="chart" :option="option" autoresize/>
  </div>
</template>

<script>
// ------基础文件---------
import VEchart from 'vue-echarts'
import {use} from 'echarts/core'
import {CanvasRenderer} from 'echarts/renderers'
// ------可选文件---------
import {
  BarChart,
  BoxplotChart,
  CandlestickChart,
  HeatmapChart,
  LineChart,
  RadarChart,
  SankeyChart,
  ScatterChart
} from 'echarts/charts'
import {
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
  MarkLineComponent
])
export default {
  components: {
    VEchart
  },
  props: {
    option: {
      type: Object,
      default: () => {
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.box {
  @apply flex justify-items-start;
}
</style>
