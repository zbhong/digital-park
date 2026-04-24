<template>
  <div>
    <el-form :model="queryParams" :inline="true" class="search-form">
      <el-form-item label="年份"><el-date-picker v-model="queryParams.year" type="year" placeholder="选择年份" value-format="YYYY" style="width: 120px" /></el-form-item>
      <el-form-item><el-button type="primary" :icon="Search" @click="handleQuery">查询</el-button></el-form-item>
    </el-form>

    <el-row :gutter="16">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header><span>园区产值趋势</span></template>
          <div ref="outputChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header><span>税收贡献趋势</span></template>
          <div ref="taxChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top: 16px;">
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header><span>产业结构</span></template>
          <div ref="industryChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header><span>企业规模分布</span></template>
          <div ref="scaleChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { Search } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getIndicatorStatistics, getIndicatorTrend, getIndicatorByPeriod } from '@/api/economy'

const queryParams = reactive({ year: '2026' })
const outputChartRef = ref(null)
const taxChartRef = ref(null)
const industryChartRef = ref(null)
const scaleChartRef = ref(null)
let charts = []

function initCharts() {
  const c1 = echarts.init(outputChartRef.value)
  c1.setOption({
    tooltip: { trigger: 'axis' }, grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', data: ['2021', '2022', '2023', '2024', '2025', '2026'], axisLabel: { color: '#ccc' } },
    yAxis: { type: 'value', name: '万元', axisLabel: { color: '#ccc' } },
    series: [{ name: '产值', type: 'bar', data: [35000, 42000, 58000, 72000, 89000, 105000], itemStyle: { color: '#409EFF' } }]
  })

  const c2 = echarts.init(taxChartRef.value)
  c2.setOption({
    tooltip: { trigger: 'axis' }, grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', data: ['2021', '2022', '2023', '2024', '2025', '2026'], axisLabel: { color: '#ccc' } },
    yAxis: { type: 'value', name: '万元', axisLabel: { color: '#ccc' } },
    series: [{ name: '税收', type: 'line', smooth: true, areaStyle: { opacity: 0.3 }, data: [2450, 2940, 4060, 5040, 6230, 7350], itemStyle: { color: '#67C23A' } }]
  })

  const c3 = echarts.init(industryChartRef.value)
  c3.setOption({
    tooltip: { trigger: 'item' },
    series: [{ type: 'pie', radius: ['40%', '70%'], data: [
      { value: 35, name: '信息技术', itemStyle: { color: '#409EFF' } },
      { value: 28, name: '制造业', itemStyle: { color: '#67C23A' } },
      { value: 20, name: '生物医药', itemStyle: { color: '#E6A23C' } },
      { value: 17, name: '新能源', itemStyle: { color: '#F56C6C' } }
    ], label: { color: '#ccc' } }]
  })

  const c4 = echarts.init(scaleChartRef.value)
  c4.setOption({
    tooltip: { trigger: 'axis' }, grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: { type: 'category', data: ['微型(<20人)', '小型(20-100)', '中型(100-300)', '大型(>300人)'], axisLabel: { color: '#ccc' } },
    yAxis: { type: 'value', name: '企业数', axisLabel: { color: '#ccc' } },
    series: [{ name: '企业数', type: 'bar', data: [15, 28, 18, 8], itemStyle: { color: ['#909399', '#409EFF', '#67C23A', '#E6A23C'] } }]
  })

  charts = [c1, c2, c3, c4]
}

function handleQuery() { getIndicatorStatistics(queryParams).catch(() => {}) }
function handleResize() { charts.forEach(c => c.resize()) }

onMounted(() => { nextTick(() => { initCharts() }); window.addEventListener('resize', handleResize) })
onBeforeUnmount(() => { window.removeEventListener('resize', handleResize); charts.forEach(c => c.dispose()) })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
