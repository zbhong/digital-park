<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header><span>污染源分布</span></template>
          <div ref="scatterChartRef" style="height: 400px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header><span>污染物排放趋势</span></template>
          <div ref="trendChartRef" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="hover" style="margin-top: 16px;">
      <template #header><span>污染源排行</span></template>
      <el-table :data="pollutionList" border stripe>
        <el-table-column label="排名" type="index" align="center" width="60" />
        <el-table-column label="污染源" prop="source" align="center" min-width="150" />
        <el-table-column label="主要污染物" prop="pollutant" align="center" min-width="120" />
        <el-table-column label="排放浓度" prop="concentration" align="center" min-width="100" />
        <el-table-column label="超标情况" prop="exceed" align="center" min-width="100"><template #default="{ row }"><el-tag :type="row.exceed ? 'danger' : 'success'" size="small">{{ row.exceed ? '超标' : '达标' }}</el-tag></template></el-table-column>
        <el-table-column label="风险等级" prop="risk" align="center" min-width="80"><template #default="{ row }"><el-tag :type="riskTagMap[row.risk]" size="small">{{ row.risk }}</el-tag></template></el-table-column>
        <template #empty><el-empty description="暂无数据" /></template>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import * as echarts from 'echarts'

const riskTagMap = { '高': 'danger', '中': 'warning', '低': 'success' }
const pollutionList = ref([
  { source: 'B栋食堂厨房', pollutant: '油烟', concentration: '2.8mg/m3', exceed: true, risk: '中' },
  { source: '化学品仓库', pollutant: 'VOC', concentration: '0.15mg/m3', exceed: false, risk: '低' },
  { source: '污水处理站', pollutant: 'COD', concentration: '38mg/L', exceed: false, risk: '低' },
  { source: '停车场', pollutant: 'NOx', concentration: '0.12mg/m3', exceed: false, risk: '低' },
  { source: '配电房', pollutant: '噪音', concentration: '72dB', exceed: true, risk: '中' }
])

const scatterChartRef = ref(null)
const trendChartRef = ref(null)
let scatterChart = null
let trendChart = null

function initCharts() {
  if (scatterChartRef.value) {
    scatterChart = echarts.init(scatterChartRef.value)
    scatterChart.setOption({
      tooltip: { trigger: 'item' },
      legend: { data: ['废气', '废水', '噪音'], textStyle: { color: '#ccc' } },
      xAxis: { type: 'value', name: 'X坐标', axisLabel: { color: '#ccc' } },
      yAxis: { type: 'value', name: 'Y坐标', axisLabel: { color: '#ccc' } },
      series: [
        { name: '废气', type: 'scatter', data: [[120, 80], [200, 150], [300, 100]], symbolSize: 30, itemStyle: { color: '#F56C6C' } },
        { name: '废水', type: 'scatter', data: [[150, 200], [250, 180]], symbolSize: 30, itemStyle: { color: '#409EFF' } },
        { name: '噪音', type: 'scatter', data: [[180, 120], [280, 160]], symbolSize: 30, itemStyle: { color: '#E6A23C' } }
      ]
    })
  }

  if (trendChartRef.value) {
    trendChart = echarts.init(trendChartRef.value)
    trendChart.setOption({
      tooltip: { trigger: 'axis' },
      legend: { data: ['废气排放', '废水排放', '噪音'], textStyle: { color: '#ccc' } },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: { type: 'category', data: ['1月', '2月', '3月', '4月', '5月', '6月'], axisLabel: { color: '#ccc' } },
      yAxis: { type: 'value', axisLabel: { color: '#ccc' } },
      series: [
        { name: '废气排放', type: 'bar', data: [85, 78, 92, 88, 80, 75], itemStyle: { color: '#F56C6C' } },
        { name: '废水排放', type: 'bar', data: [65, 60, 70, 68, 62, 58], itemStyle: { color: '#409EFF' } },
        { name: '噪音', type: 'line', smooth: true, data: [62, 58, 65, 63, 60, 57], itemStyle: { color: '#E6A23C' } }
      ]
    })
  }
}

function handleResize() { scatterChart && scatterChart.resize(); trendChart && trendChart.resize() }

onMounted(() => { nextTick(() => { initCharts() }); window.addEventListener('resize', handleResize) })
onBeforeUnmount(() => { window.removeEventListener('resize', handleResize); scatterChart && scatterChart.dispose(); trendChart && trendChart.dispose() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
