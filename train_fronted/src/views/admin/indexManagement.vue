<template>
  <div class="dashboard">
    <h1>运营数据看板</h1>
    <!-- 新增实时计数器 -->
    <div class="counter-container">
      <div class="counter-item">
        <span class="counter-label">今日浏览人数</span>
        <span class="counter-value">{{ todayViewCount }}</span>
      </div>
      <div class="counter-item">
        <span class="counter-label">今日新增订单数</span>
        <span class="counter-value">{{ todayNewOrderCount }}</span>
      </div>
      <div class="counter-item">
        <span class="counter-label">今日运行车次数</span>
        <span class="counter-value">{{ todayTrainCount }}</span>
      </div>
      <div class="counter-item">
        <span class="counter-label">今日运载乘客数</span>
        <span class="counter-value">{{ todayPassengerCount }}</span>
      </div>
    </div>
    <div class="chart-grid">
      <div class="chart-item">
        <div class="chart-container" ref="lineChartDom" :style="{ height: '400px', width: '100%' }"></div>
      </div>
      <div class="chart-item">
        <div class="chart-container" ref="stationFlowBarChartDom" :style="{ height: '400px', width: '100%' }"></div>
      </div>
      <div class="chart-item">
        <div class="chart-container" ref="trainOccupancyBarChartDom" :style="{ height: '400px', width: '100%' }"></div>
      </div>
      <div class="chart-item">
        <div class="chart-container" ref="dailyOrderLineChartDom" :style="{ height: '400px', width: '100%' }"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import * as echarts from 'echarts/core'
import { LineChart, BarChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  GridComponent,
  DatasetComponent,
  LegendComponent
} from 'echarts/components'
import { LabelLayout } from 'echarts/features'
import { CanvasRenderer } from 'echarts/renderers'
import {fetchIndexData} from '@/service/index.js'

echarts.use([
  LineChart,
  BarChart,
  TitleComponent,
  TooltipComponent,
  GridComponent,
  DatasetComponent,
  LegendComponent,
  LabelLayout,
  CanvasRenderer
])

// 初始数据
const lineOptions = ref({
  title: {
    text: '每日乘车人数趋势',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    data: [],
    axisLabel: {
      formatter: (value) => value.split('-').slice(1).join('/')
    }
  },
  yAxis: {
    type: 'value',
    name: '人次'
  },
  series: [{
    data: [],
    type: 'line',
    smooth: true,
    areaStyle: {
      color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
        { offset: 0, color: '#83bff6' },
        { offset: 1, color: '#188df0' }
      ])
    },
    itemStyle: {
      color: '#188df0'
    }
  }]
})

// 车站人流量前十名排行榜柱状图
const stationFlowBarOptions = ref({
  title: {
    text: '车站人流量前十名排行榜',
    left: 'center'
  },
  xAxis: {
    type: 'category',
    data: [],
    axisLabel: {
      rotate: 45
    }
  },
  yAxis: {
    type: 'value',
    name: '人流量'
  },
  series: [{
    data: [],
    type: 'bar',
    itemStyle: {
      color: '#a5d8ff'
    }
  }]
})

// 车次平均上座率排行榜柱状图
const trainOccupancyBarOptions = ref({
  title: {
    text: '车次平均上座率排行榜',
    left: 'center'
  },
  xAxis: {
    type: 'category',
    data: [],
    axisLabel: {
      rotate: 45
    }
  },
  yAxis: {
    type: 'value',
    name: '上座率（%）',
    min: 0,
    max: 100
  },
  series: [{
    data: [],
    type: 'bar',
    itemStyle: {
      color: '#ffb7a5'
    }
  }]
})

// 每日订单数量趋势折线图
const dailyOrderLineOptions = ref({
  title: {
    text: '每日订单数量趋势',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  xAxis: {
    type: 'category',
    data: [],
    axisLabel: {
      formatter: (value) => value.split('-').slice(1).join('/')
    }
  },
  yAxis: {
    type: 'value',
    name: '订单数量'
  },
  series: [{
    data: [],
    type: 'line',
    smooth: true,
    areaStyle: {
      color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
        {offset: 0, color: '#f683b3'},
        {offset: 1, color: '#f0188d'}
      ])
    },
    itemStyle: {
      color: '#f0188d'
    }
  }]
})

const lineChartDom = ref(null)
const stationFlowBarChartDom = ref(null)
const trainOccupancyBarChartDom = ref(null)
const dailyOrderLineChartDom = ref(null)
let lineChart = null
let stationFlowBarChart = null
let trainOccupancyBarChart = null
let dailyOrderLineChart = null

// 新增实时计数器数据
const todayViewCount = ref(0)
const todayNewOrderCount = ref(0)
const todayTrainCount = ref(0)
const todayPassengerCount = ref(0)

const fetchData = async () => {
  try {
    fetchIndexData().then((data) => {
      console.log('数据获取成功1')
      console.log(data)

      // 实时计数器数据
      todayViewCount.value = data.realTimeData.todayViewCount
      todayNewOrderCount.value = data.realTimeData.todayNewOrderCount
      todayTrainCount.value = data.realTimeData.todayTrainCount
      todayPassengerCount.value = data.realTimeData.todayPassengerCount

      // 乘车趋势数据
      lineOptions.value.xAxis.data = data.passengerTrend.dates
      lineOptions.value.series[0].data = data.passengerTrend.counts
      lineChart.setOption(lineOptions.value)

      // 车站流量排行
      stationFlowBarOptions.value.xAxis.data = data.stationRank.names
      stationFlowBarOptions.value.series[0].data = data.stationRank.values
      stationFlowBarChart.setOption(stationFlowBarOptions.value)

      // 车次上座率
      trainOccupancyBarOptions.value.xAxis.data = data.trainOccupancyRank.names
      trainOccupancyBarOptions.value.series[0].data = data.trainOccupancyRank.values
      trainOccupancyBarChart.setOption(trainOccupancyBarOptions.value)

      // 订单趋势数据
      dailyOrderLineOptions.value.xAxis.data = data.orderTrend.dates
      dailyOrderLineOptions.value.series[0].data = data.orderTrend.counts
      dailyOrderLineChart.setOption(dailyOrderLineOptions.value)


    })



  } catch (error) {
    console.error('数据获取失败:', error)
  }
}

onMounted(() => {
  lineChart = echarts.init(lineChartDom.value)
  lineChart.setOption(lineOptions.value)

  stationFlowBarChart = echarts.init(stationFlowBarChartDom.value)
  stationFlowBarChart.setOption(stationFlowBarOptions.value)

  trainOccupancyBarChart = echarts.init(trainOccupancyBarChartDom.value)
  trainOccupancyBarChart.setOption(trainOccupancyBarOptions.value)

  dailyOrderLineChart = echarts.init(dailyOrderLineChartDom.value)
  dailyOrderLineChart.setOption(dailyOrderLineOptions.value)

  window.addEventListener('resize', handleResize)

  setInterval(fetchData, 5000) // 每5秒更新
  fetchData() // 初始加载
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  lineChart?.dispose()
  stationFlowBarChart?.dispose()
  trainOccupancyBarChart?.dispose()
  dailyOrderLineChart?.dispose()
})

const handleResize = () => {
  lineChart?.resize()
  stationFlowBarChart?.resize()
  trainOccupancyBarChart?.resize()
  dailyOrderLineChart?.resize()
}

watch(() => lineOptions.value, (newVal) => {
  lineChart?.setOption(newVal)
})

watch(() => stationFlowBarOptions.value, (newVal) => {
  stationFlowBarChart?.setOption(newVal)
})

watch(() => trainOccupancyBarOptions.value, (newVal) => {
  trainOccupancyBarChart?.setOption(newVal)
})

watch(() => dailyOrderLineOptions.value, (newVal) => {
  dailyOrderLineChart?.setOption(newVal)
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.chart-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.chart-item {
  background: white;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.chart-container {
  min-height: 300px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 15px;
  margin-bottom: 20px;
}

/* 新增实时计数器样式 */
.counter-container {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-top: 20px;
  background: white;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.counter-item {
  text-align: center;
}

.counter-label {
  font-size: 14px;
  color: #666;
}

.counter-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}
</style>