<template>
  <div>
    <el-form :model="queryParams" :inline="true" class="search-form">
      <el-form-item label="监测类型" prop="type"><el-select v-model="queryParams.type" placeholder="请选择" style="width: 120px"><el-option label="空气质量" value="air" /><el-option label="水质" value="water" /><el-option label="噪音" value="noise" /></el-select></el-form-item>
      <el-form-item label="时间范围"><el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD" style="width: 260px" /></el-form-item>
      <el-form-item><el-button type="primary" :icon="Search" @click="handleQuery">查询</el-button></el-form-item>
    </el-form>

    <el-row :gutter="16" class="stat-cards">
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #67C23A">{{ stats.aqi }}</div><div class="stat-label">AQI指数</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #409EFF">{{ stats.pm25 }}</div><div class="stat-label">PM2.5 (ug/m3)</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #E6A23C">{{ stats.noise }}</div><div class="stat-label">噪音 (dB)</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #F56C6C">{{ stats.alertCount }}</div><div class="stat-label">超标次数</div></el-card></el-col>
    </el-row>

    <el-card shadow="hover">
      <template #header><span>环境数据趋势</span></template>
      <div ref="trendChartRef" style="height: 350px;"></div>
    </el-card>

    <el-card shadow="hover" style="margin-top: 16px;">
      <template #header><span>超标预警列表</span></template>
      <el-table :data="alertList" border stripe>
        <el-table-column label="监测点位" prop="pointName" align="center" min-width="150" />
        <el-table-column label="监测指标" prop="indicator" align="center" min-width="100" />
        <el-table-column label="监测值" prop="value" align="center" min-width="100" />
        <el-table-column label="标准值" prop="standard" align="center" min-width="100" />
        <el-table-column label="超标倍数" prop="exceedRatio" align="center" min-width="100"><template #default="{ row }"><span style="color: #F56C6C">{{ row.exceedRatio }}x</span></template></el-table-column>
        <el-table-column label="监测时间" prop="time" align="center" min-width="160" />
        <template #empty><el-empty description="暂无超标数据" /></template>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { Search } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getEnvStatisticsByTime } from '@/api/environment'

const queryParams = reactive({ type: 'air' })
const dateRange = ref([])
const stats = reactive({ aqi: 52, pm25: 35, noise: 55, alertCount: 3 })
const alertList = ref([
  { id: 1, pointName: 'B栋楼顶空气站', indicator: 'PM2.5', value: 180, standard: 75, exceedRatio: '2.4', time: '2026-04-19 14:00:00' },
  { id: 2, pointName: '北侧围墙噪音站', indicator: '噪音', value: 75, standard: 65, exceedRatio: '1.15', time: '2026-04-18 10:30:00' },
  { id: 3, pointName: '排放口水质站', indicator: 'COD', value: 55, standard: 40, exceedRatio: '1.38', time: '2026-04-17 16:00:00' }
])

const trendChartRef = ref(null)
let trendChart = null

function initChart() {
  if (trendChartRef.value) {
    trendChart = echarts.init(trendChartRef.value)
    trendChart.setOption({
      tooltip: { trigger: 'axis' },
      legend: { data: ['PM2.5', 'PM10', '噪音'], textStyle: { color: '#ccc' } },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: { type: 'category', data: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00', '24:00'], axisLabel: { color: '#ccc' } },
      yAxis: { type: 'value', axisLabel: { color: '#ccc' } },
      series: [
        { name: 'PM2.5', type: 'line', smooth: true, data: [28, 25, 35, 42, 38, 30, 28], itemStyle: { color: '#409EFF' }, markLine: { data: [{ yAxis: 75, name: '标准线' }], lineStyle: { color: '#F56C6C', type: 'dashed' } } },
        { name: 'PM10', type: 'line', smooth: true, data: [45, 40, 55, 68, 60, 48, 42], itemStyle: { color: '#67C23A' } },
        { name: '噪音', type: 'line', smooth: true, data: [40, 38, 52, 58, 55, 48, 42], itemStyle: { color: '#E6A23C' } }
      ]
    })
  }
}

function handleQuery() { getEnvStatisticsByTime(queryParams).catch(() => {}) }
function handleResize() { trendChart && trendChart.resize() }

onMounted(() => { nextTick(() => { initChart() }); window.addEventListener('resize', handleResize) })
onBeforeUnmount(() => { window.removeEventListener('resize', handleResize); trendChart && trendChart.dispose() })
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
