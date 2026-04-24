<template>
  <div>
    <el-row :gutter="16" class="stat-cards">
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #409EFF">{{ storageInfo.soc }}%</div><div class="stat-label">当前SOC</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #67C23A">{{ storageInfo.power }}</div><div class="stat-label">当前功率 (kW)</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #E6A23C">{{ storageInfo.todayCharge }}</div><div class="stat-label">今日充电 (kWh)</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #F56C6C">{{ storageInfo.todayDischarge }}</div><div class="stat-label">今日放电 (kWh)</div></el-card></el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header><span>SOC仪表盘</span></template>
          <div ref="gaugeChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header><span>充放电收益趋势</span></template>
          <div ref="revenueChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="hover" style="margin-top: 16px;">
      <template #header><span>储能设备列表</span></template>
      <el-table :data="deviceList" border stripe>
        <el-table-column label="设备名称" prop="name" align="center" min-width="150" />
        <el-table-column label="额定容量" prop="capacity" align="center" min-width="120" />
        <el-table-column label="当前SOC" prop="soc" align="center" min-width="100">
          <template #default="{ row }"><el-progress :percentage="row.soc" :color="row.soc > 50 ? '#67C23A' : row.soc > 20 ? '#E6A23C' : '#F56C6C'" /></template>
        </el-table-column>
        <el-table-column label="状态" prop="status" align="center" min-width="100">
          <template #default="{ row }"><el-tag :type="statusTagMap[row.status]" size="small">{{ statusNameMap[row.status] }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" align="center" min-width="100" fixed="right">
          <template #default="{ row }"><el-button link type="primary" @click="handleDetail(row)">详情</el-button></template>
        </el-table-column>
        <template #empty><el-empty description="暂无数据" /></template>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import { getStorageList, getStorageStatus, getStorageAnalysis } from '@/api/energy'

const storageInfo = reactive({ soc: 0, power: 0, todayCharge: 0, todayDischarge: 0 })
const statusTagMap = { charging: 'primary', discharging: 'warning', standby: 'success', fault: 'danger' }
const statusNameMap = { charging: '充电中', discharging: '放电中', standby: '待机', fault: '故障' }

const loading = ref(false)
const deviceList = ref([])

const gaugeChartRef = ref(null)
const revenueChartRef = ref(null)
let gaugeChart = null
let revenueChart = null

function getDeviceList() {
  loading.value = true
  getStorageList().then(res => {
    deviceList.value = res.rows || res.data || []
  }).catch(() => {
    ElMessage.error('获取储能设备列表失败')
  }).finally(() => { loading.value = false })
}

function getStorageData() {
  getStorageAnalysis({ period: 'today' }).then(res => {
    if (res.data) {
      storageInfo.soc = res.data.soc || 0
      storageInfo.power = res.data.power || 0
      storageInfo.todayCharge = res.data.todayCharge || 0
      storageInfo.todayDischarge = res.data.todayDischarge || 0
    }
  }).catch(() => {})
}

function initCharts() {
  if (gaugeChartRef.value) {
    gaugeChart = echarts.init(gaugeChartRef.value)
    gaugeChart.setOption({
      series: [{
        type: 'gauge', startAngle: 200, endAngle: -20,
        min: 0, max: 100, splitNumber: 10,
        axisLine: { lineStyle: { width: 20, color: [[0.2, '#F56C6C'], [0.5, '#E6A23C'], [1, '#67C23A']] } },
        pointer: { itemStyle: { color: '#409EFF' }, width: 4 },
        axisTick: { distance: -20, length: 6, lineStyle: { color: '#fff', width: 1 } },
        splitLine: { distance: -20, length: 20, lineStyle: { color: '#fff', width: 2 } },
        axisLabel: { color: '#ccc', distance: -30, fontSize: 12 },
        detail: { valueAnimation: true, formatter: '{value}%', color: '#409EFF', fontSize: 24, offsetCenter: [0, '70%'] },
        data: [{ value: storageInfo.soc }]
      }]
    })
  }

  if (revenueChartRef.value) {
    revenueChart = echarts.init(revenueChartRef.value)
    revenueChart.setOption({
      tooltip: { trigger: 'axis' },
      legend: { data: ['充电量', '放电量', '收益(元)'], textStyle: { color: '#ccc' } },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: { type: 'category', data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'], axisLabel: { color: '#ccc' } },
      yAxis: { type: 'value', axisLabel: { color: '#ccc' } },
      series: [
        { name: '充电量', type: 'bar', data: [420, 380, 450, 400, 500, 300, 350], itemStyle: { color: '#409EFF' } },
        { name: '放电量', type: 'bar', data: [380, 350, 400, 360, 450, 280, 320], itemStyle: { color: '#67C23A' } },
        { name: '收益(元)', type: 'line', smooth: true, data: [580, 520, 640, 560, 720, 400, 480], itemStyle: { color: '#E6A23C' } }
      ]
    })
  }
}

function handleDetail(row) {
  ElMessage.info('查看设备详情: ' + row.name)
}

function handleResize() {
  gaugeChart && gaugeChart.resize()
  revenueChart && revenueChart.resize()
}

onMounted(() => {
  getDeviceList()
  getStorageData()
  nextTick(() => { initCharts() })
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  gaugeChart && gaugeChart.dispose()
  revenueChart && revenueChart.dispose()
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
