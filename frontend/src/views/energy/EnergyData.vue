<template>
  <div>
    <el-form :model="queryParams" :inline="true" class="search-form">
      <el-form-item label="能源类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择" style="width: 120px">
          <el-option label="电力" value="electricity" /><el-option label="水" value="water" />
          <el-option label="天然气" value="gas" />
        </el-select>
      </el-form-item>
      <el-form-item label="时间范围">
        <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD" style="width: 260px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">查询</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="16" class="stat-cards">
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #409EFF">{{ stats.totalKwh }}</div><div class="stat-label">总用电量 (kWh)</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #67C23A">{{ stats.totalWater }}</div><div class="stat-label">总用水量 (吨)</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #E6A23C">{{ stats.totalGas }}</div><div class="stat-label">总用气量 (m3)</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #F56C6C">{{ stats.cost }}</div><div class="stat-label">总费用 (万元)</div></el-card></el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header><span>能耗趋势</span></template>
          <div ref="trendChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header><span>能耗分类占比</span></template>
          <div ref="pieChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="hover" style="margin-top: 16px;">
      <template #header><span>区域能耗对比</span></template>
      <div ref="barChartRef" style="height: 350px;"></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { Search } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getConsumptionAnalysis, getEnergyOverview } from '@/api/energy'

const queryParams = reactive({ type: 'electricity' })
const dateRange = ref([])
const stats = reactive({ totalKwh: 125680, totalWater: 8560, totalGas: 3200, cost: 156.8 })

const trendChartRef = ref(null)
const pieChartRef = ref(null)
const barChartRef = ref(null)
let trendChart = null
let pieChart = null
let barChart = null

function initCharts() {
  // 趋势图
  if (trendChartRef.value) {
    trendChart = echarts.init(trendChartRef.value)
    trendChart.setOption({
      tooltip: { trigger: 'axis' },
      legend: { data: ['电力', '水', '天然气'], textStyle: { color: '#ccc' } },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: { type: 'category', data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'], axisLabel: { color: '#ccc' } },
      yAxis: { type: 'value', axisLabel: { color: '#ccc' } },
      series: [
        { name: '电力', type: 'line', smooth: true, data: [9800, 8500, 9200, 10500, 12000, 13500, 14200, 13800, 11500, 10200, 9500, 9800], itemStyle: { color: '#409EFF' } },
        { name: '水', type: 'line', smooth: true, data: [680, 620, 700, 750, 820, 900, 950, 920, 800, 720, 680, 700], itemStyle: { color: '#67C23A' } },
        { name: '天然气', type: 'line', smooth: true, data: [280, 250, 220, 180, 150, 120, 100, 110, 150, 200, 260, 300], itemStyle: { color: '#E6A23C' } }
      ]
    })
  }

  // 饼图
  if (pieChartRef.value) {
    pieChart = echarts.init(pieChartRef.value)
    pieChart.setOption({
      tooltip: { trigger: 'item' },
      legend: { orient: 'vertical', left: 'left', textStyle: { color: '#ccc' } },
      series: [{
        type: 'pie', radius: ['40%', '70%'], center: ['60%', '50%'],
        data: [
          { value: 45, name: '空调暖通', itemStyle: { color: '#409EFF' } },
          { value: 25, name: '照明', itemStyle: { color: '#67C23A' } },
          { value: 15, name: '动力设备', itemStyle: { color: '#E6A23C' } },
          { value: 10, name: '办公设备', itemStyle: { color: '#F56C6C' } },
          { value: 5, name: '其他', itemStyle: { color: '#909399' } }
        ],
        label: { color: '#ccc' }
      }]
    })
  }

  // 柱状图
  if (barChartRef.value) {
    barChart = echarts.init(barChartRef.value)
    barChart.setOption({
      tooltip: { trigger: 'axis' },
      legend: { data: ['电力(kWh)', '水(吨)', '气(m3)'], textStyle: { color: '#ccc' } },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: { type: 'category', data: ['A栋', 'B栋', 'C栋', 'D栋', '地下车库', '公共区域'], axisLabel: { color: '#ccc' } },
      yAxis: { type: 'value', axisLabel: { color: '#ccc' } },
      series: [
        { name: '电力(kWh)', type: 'bar', data: [32000, 28000, 22000, 18000, 15000, 10680], itemStyle: { color: '#409EFF' } },
        { name: '水(吨)', type: 'bar', data: [2200, 1800, 1500, 1200, 800, 1060], itemStyle: { color: '#67C23A' } },
        { name: '气(m3)', type: 'bar', data: [800, 600, 500, 400, 200, 700], itemStyle: { color: '#E6A23C' } }
      ]
    })
  }
}

function handleQuery() {
  getEnergyOverview(queryParams).then(res => {
    // Update charts with real data
  }).catch(() => {
    // Use mock data
  })
}

function handleResize() {
  trendChart && trendChart.resize()
  pieChart && pieChart.resize()
  barChart && barChart.resize()
}

onMounted(() => {
  nextTick(() => { initCharts() })
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  trendChart && trendChart.dispose()
  pieChart && pieChart.dispose()
  barChart && barChart.dispose()
})
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
.stat-cards { margin-bottom: 16px; }
.stat-card { text-align: center; padding: 10px 0; }
.stat-value { font-size: 28px; font-weight: bold; }
.stat-label { font-size: 14px; color: #909399; margin-top: 4px; }
</style>
