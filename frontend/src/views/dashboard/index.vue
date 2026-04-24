<template>
  <div class="dashboard-container">
    <!-- 欢迎信息区域 -->
    <div class="welcome-section">
      <div class="welcome-text">
        <h2>欢迎回来，{{ username }}</h2>
        <p class="welcome-desc">{{ currentDate }}，祝您工作愉快！</p>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stat-cards">
      <el-col :xs="12" :sm="6" v-for="(card, index) in statCards" :key="index">
        <div class="stat-card" :style="{ borderTop: `3px solid ${card.color}` }">
          <div class="stat-card__icon" :style="{ backgroundColor: card.color + '15', color: card.color }">
            <el-icon :size="26"><component :is="card.icon" /></el-icon>
          </div>
          <div class="stat-card__content">
            <div class="stat-card__label">{{ card.label }}</div>
            <div class="stat-card__value">
              <span class="stat-card__number">{{ card.value }}</span>
              <span class="stat-card__unit">{{ card.unit }}</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :xs="24" :lg="16">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">能耗趋势</span>
              <el-radio-group v-model="energyPeriod" size="small">
                <el-radio-button value="week">近7天</el-radio-button>
                <el-radio-button value="month">近30天</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div class="chart-wrapper" ref="energyChartRef"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="8">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <span class="card-title">告警分布</span>
          </template>
          <div class="chart-wrapper" ref="alertChartRef"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="chart-row">
      <el-col :xs="24" :lg="8">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <span class="card-title">设备状态</span>
          </template>
          <div class="chart-wrapper" ref="deviceChartRef"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="16">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">最新告警</span>
              <el-button text type="primary" size="small">查看全部</el-button>
            </div>
          </template>
          <div class="alert-list">
            <div v-for="(alert, index) in recentAlerts" :key="index" class="alert-item">
              <div class="alert-item__dot" :class="'alert-item__dot--' + alert.level"></div>
              <div class="alert-item__content">
                <div class="alert-item__title">{{ alert.title }}</div>
                <div class="alert-item__time">{{ alert.time }}</div>
              </div>
              <el-tag :type="alert.statusType" size="small">{{ alert.status }}</el-tag>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount, watch, shallowRef } from 'vue'
import { useUserStore } from '@/store/modules'
import * as echarts from 'echarts'

const userStore = useUserStore()
const username = computed(() => userStore.username || '管理员')

const currentDate = computed(() => {
  const now = new Date()
  const weekDays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const weekDay = weekDays[now.getDay()]
  return `${year}年${month}月${day}日 ${weekDay}`
})

const energyPeriod = ref('week')

const statCards = ref([
  { label: '入驻企业', value: 128, unit: '家', icon: 'OfficeBuilding', color: '#409eff' },
  { label: '在线设备', value: 1024, unit: '台', icon: 'Monitor', color: '#67c23a' },
  { label: '待处理告警', value: 23, unit: '条', icon: 'Warning', color: '#e6a23c' },
  { label: '今日能耗', value: 3842, unit: 'kWh', icon: 'Lightning', color: '#909399' }
])

const recentAlerts = ref([
  { title: 'A栋3楼烟感探测器离线', level: 'high', time: '10分钟前', status: '待处理', statusType: 'danger' },
  { title: 'B栋配电室温度超限', level: 'high', time: '25分钟前', status: '处理中', statusType: 'warning' },
  { title: '停车场入口闸机故障', level: 'medium', time: '1小时前', status: '待处理', statusType: 'danger' },
  { title: 'C栋空调系统异常', level: 'medium', time: '2小时前', status: '已处理', statusType: 'success' },
  { title: '园区PM2.5浓度偏高', level: 'low', time: '3小时前', status: '已处理', statusType: 'success' }
])

// ECharts refs
const energyChartRef = ref(null)
const alertChartRef = ref(null)
const deviceChartRef = ref(null)
let energyChart = null
let alertChart = null
let deviceChart = null

function generateWeekDays() {
  const days = []
  const now = new Date()
  for (let i = 6; i >= 0; i--) {
    const d = new Date(now)
    d.setDate(d.getDate() - i)
    days.push(`${d.getMonth() + 1}/${d.getDate()}`)
  }
  return days
}

function generateMonthDays() {
  const days = []
  const now = new Date()
  for (let i = 29; i >= 0; i--) {
    const d = new Date(now)
    d.setDate(d.getDate() - i)
    days.push(`${d.getMonth() + 1}/${d.getDate()}`)
  }
  return days
}

function generateData(count, min, max) {
  return Array.from({ length: count }, () => Math.floor(Math.random() * (max - min) + min))
}

function initEnergyChart() {
  if (!energyChartRef.value) return
  energyChart = echarts.init(energyChartRef.value)
  updateEnergyChart()
}

function updateEnergyChart() {
  const isWeek = energyPeriod.value === 'week'
  const xData = isWeek ? generateWeekDays() : generateMonthDays()
  const count = xData.length

  energyChart.setOption({
    tooltip: { trigger: 'axis', axisPointer: { type: 'cross' } },
    legend: { data: ['电力(kWh)', '水(m³)', '天然气(m³)'], bottom: 0 },
    grid: { left: 50, right: 20, top: 20, bottom: 40 },
    xAxis: { type: 'category', data: xData, boundaryGap: false },
    yAxis: { type: 'value' },
    series: [
      { name: '电力(kWh)', type: 'line', smooth: true, data: generateData(count, 3000, 5000), itemStyle: { color: '#409eff' }, areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(64,158,255,0.25)' }, { offset: 1, color: 'rgba(64,158,255,0.02)' }]) } },
      { name: '水(m³)', type: 'line', smooth: true, data: generateData(count, 200, 500), itemStyle: { color: '#67c23a' }, areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(103,194,58,0.2)' }, { offset: 1, color: 'rgba(103,194,58,0.02)' }]) } },
      { name: '天然气(m³)', type: 'line', smooth: true, data: generateData(count, 100, 300), itemStyle: { color: '#e6a23c' }, areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(230,162,60,0.2)' }, { offset: 1, color: 'rgba(230,162,60,0.02)' }]) } }
    ]
  }, true)
}

function initAlertChart() {
  if (!alertChartRef.value) return
  alertChart = echarts.init(alertChartRef.value)
  alertChart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { bottom: 0, itemWidth: 10, itemHeight: 10 },
    series: [{
      type: 'pie',
      radius: ['45%', '70%'],
      center: ['50%', '45%'],
      avoidLabelOverlap: false,
      itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
      label: { show: false },
      emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
      data: [
        { value: 8, name: '安全告警', itemStyle: { color: '#f56c6c' } },
        { value: 5, name: '设备告警', itemStyle: { color: '#e6a23c' } },
        { value: 4, name: '环境告警', itemStyle: { color: '#409eff' } },
        { value: 3, name: '能源告警', itemStyle: { color: '#67c23a' } },
        { value: 3, name: '其他告警', itemStyle: { color: '#909399' } }
      ]
    }]
  })
}

function initDeviceChart() {
  if (!deviceChartRef.value) return
  deviceChart = echarts.init(deviceChartRef.value)
  deviceChart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c}台 ({d}%)' },
    legend: { bottom: 0, itemWidth: 10, itemHeight: 10 },
    series: [{
      type: 'pie',
      radius: ['45%', '70%'],
      center: ['50%', '45%'],
      itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
      label: { show: false },
      emphasis: { label: { show: true, fontSize: 14, fontWeight: 'bold' } },
      data: [
        { value: 680, name: '在线', itemStyle: { color: '#67c23a' } },
        { value: 156, name: '离线', itemStyle: { color: '#909399' } },
        { value: 128, name: '告警', itemStyle: { color: '#e6a23c' } },
        { value: 60, name: '维护中', itemStyle: { color: '#409eff' } }
      ]
    }]
  })
}

function handleResize() {
  energyChart?.resize()
  alertChart?.resize()
  deviceChart?.resize()
}

watch(energyPeriod, () => updateEnergyChart())

onMounted(() => {
  initEnergyChart()
  initAlertChart()
  initDeviceChart()
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  energyChart?.dispose()
  alertChart?.dispose()
  deviceChart?.dispose()
})
</script>

<script>
export default { name: 'Dashboard' }
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 0;
}

.welcome-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  background: linear-gradient(135deg, #409eff 0%, #67c23a 100%);
  border-radius: 8px;
  color: #fff;
  margin-bottom: 16px;

  .welcome-text {
    h2 {
      margin: 0 0 6px 0;
      font-size: 22px;
      font-weight: 600;
      color: #fff;
    }

    .welcome-desc {
      margin: 0;
      font-size: 14px;
      color: rgba(255, 255, 255, 0.85);
    }
  }
}

.stat-cards {
  margin-bottom: 16px;

  .stat-card {
    display: flex;
    align-items: center;
    padding: 16px 20px;
    background: #fff;
    border-radius: 8px;
    border: 1px solid #ebeef5;
    transition: box-shadow 0.3s;

    &:hover {
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    }

    &__icon {
      width: 52px;
      height: 52px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
      flex-shrink: 0;
    }

    &__content {
      flex: 1;
      min-width: 0;
    }

    &__label {
      font-size: 13px;
      color: #909399;
      margin-bottom: 4px;
    }

    &__value {
      display: flex;
      align-items: baseline;
    }

    &__number {
      font-size: 24px;
      font-weight: 700;
      color: #303133;
      line-height: 1;
    }

    &__unit {
      font-size: 12px;
      color: #909399;
      margin-left: 4px;
    }
  }
}

.chart-row {
  margin-bottom: 16px;
}

.chart-card {
  :deep(.el-card__header) {
    padding: 14px 20px;
    border-bottom: 1px solid #f0f0f0;
  }

  :deep(.el-card__body) {
    padding: 16px 20px;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .card-title {
    font-size: 15px;
    font-weight: 600;
    color: #303133;
  }

  .chart-wrapper {
    height: 280px;
  }
}

.alert-list {
  .alert-item {
    display: flex;
    align-items: center;
    padding: 10px 0;
    border-bottom: 1px solid #f5f5f5;

    &:last-child {
      border-bottom: none;
    }

    &__dot {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      margin-right: 12px;
      flex-shrink: 0;

      &--high { background: #f56c6c; }
      &--medium { background: #e6a23c; }
      &--low { background: #409eff; }
    }

    &__content {
      flex: 1;
      min-width: 0;
    }

    &__title {
      font-size: 13px;
      color: #303133;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    &__time {
      font-size: 12px;
      color: #c0c4cc;
      margin-top: 2px;
    }
  }
}
</style>
