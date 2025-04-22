<template>
  <div class="dashboard">
    <h1>运营数据看板</h1>
    <div class="chart-grid">
      <div class="chart-item">
        <div class="chart-container" ref="lineChartDom" :style="{ height: '400px', width: '100%' }"></div>
      </div>
      <div class="chart-item">
        <div class="chart-container" ref="barChartDom" :style="{ height: '400px', width: '100%' }"></div>
      </div>
      <div class="chart-item">
        <div class="chart-container" ref="pieChartDom" :style="{ height: '400px', width: '100%' }"></div>
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
import { LineChart, BarChart, PieChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  GridComponent,
  DatasetComponent,
  LegendComponent
} from 'echarts/components'
import { LabelLayout } from 'echarts/features'
import { CanvasRenderer } from 'echarts/renderers'

echarts.use([
  LineChart,
  BarChart,
  PieChart,
  TitleComponent,
  TooltipComponent,
  GridComponent,
  DatasetComponent,
  LegendComponent,
  LabelLayout,
  CanvasRenderer
])

// 模拟API获取数据
const mockData = {
  dates: ['2023-01-01', '2023-01-02', '2023-01-03'],
  counts: [1200, 2400, 1800]
}

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
    data: mockData.dates,
    axisLabel: {
      formatter: (value) => value.split('-').slice(1).join('/')
    }
  },
  yAxis: {
    type: 'value',
    name: '人次'
  },
  series: [{
    data: mockData.counts,
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

// 各车站车次数量柱状图
const barOptions = ref({
  title: {
    text: '各车站车次数量',
    left: 'center'
  },
  xAxis: {
    type: 'category',
    data: ['车站A', '车站B', '车站C'],
    axisLabel: {
      rotate: 45
    }
  },
  yAxis: {
    type: 'value',
    name: '车次数量'
  },
  series: [{
    data: [10, 20, 15],
    type: 'bar',
    itemStyle: {
      color: '#a5d8ff'
    }
  }]
})

// 不同座位类型销售占比饼图
const pieOptions = ref({
  title: {
    text: '不同座位类型销售占比',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  series: [{
    type: 'pie',
    radius: '60%',
    data: [
      { value: 45, name: '商务座' },
      { value: 120, name: '一等座' },
      { value: 435, name: '二等座' }
    ],
    emphasis: {
      itemStyle: {
        shadowBlur: 10,
        shadowOffsetX: 0,
        shadowColor: 'rgba(0, 0, 0, 0.5)'
      }
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
    data: ['2023-01-01', '2023-01-02', '2023-01-03'],
    axisLabel: {
      formatter: (value) => value.split('-').slice(1).join('/')
    }
  },
  yAxis: {
    type: 'value',
    name: '订单数量'
  },
  series: [{
    data: [50, 80, 60],
    type: 'line',
    smooth: true,
    areaStyle: {
      color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
        { offset: 0, color: '#f683b3' },
        { offset: 1, color: '#f0188d' }
      ])
    },
    itemStyle: {
      color: '#f0188d'
    }
  }]
})


const lineChartDom = ref(null)
const barChartDom = ref(null)
const pieChartDom = ref(null)
const dailyOrderLineChartDom = ref(null)
const userRoleBarChartDom = ref(null)
let lineChart = null
let barChart = null
let pieChart = null
let dailyOrderLineChart = null
let userRoleBarChart = null

const fetchRealTimeData = async () => {
  try {
    const res = await fetch('/api/passenger-flow')
    const data = await res.json()
    lineOptions.value.series[0].data = data.counts
    lineOptions.value.xAxis.data = data.dates
    lineChart.setOption(lineOptions.value)
  } catch (error) {
    console.error('数据获取失败:', error)
  }
}

onMounted(() => {
  lineChart = echarts.init(lineChartDom.value)
  lineChart.setOption(lineOptions.value)

  barChart = echarts.init(barChartDom.value)
  barChart.setOption(barOptions.value)

  pieChart = echarts.init(pieChartDom.value)
  pieChart.setOption(pieOptions.value)

  dailyOrderLineChart = echarts.init(dailyOrderLineChartDom.value)
  dailyOrderLineChart.setOption(dailyOrderLineOptions.value)

  userRoleBarChart = echarts.init(userRoleBarChartDom.value)
  userRoleBarChart.setOption(userRoleBarOptions.value)

  window.addEventListener('resize', handleResize)

  setInterval(fetchRealTimeData, 5000) // 每5秒更新
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  lineChart?.dispose()
  barChart?.dispose()
  pieChart?.dispose()
  dailyOrderLineChart?.dispose()
  userRoleBarChart?.dispose()
})

const handleResize = () => {
  lineChart?.resize()
  barChart?.resize()
  pieChart?.resize()
  dailyOrderLineChart?.resize()
  userRoleBarChart?.resize()
}

watch(() => lineOptions.value, (newVal) => {
  lineChart?.setOption(newVal)
})

watch(() => barOptions.value, (newVal) => {
  barChart?.setOption(newVal)
})

watch(() => pieOptions.value, (newVal) => {
  pieChart?.setOption(newVal)
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
</style>