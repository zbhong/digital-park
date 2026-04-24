<template>
  <div class="data-panel" :class="{ collapsed }">
    <!-- 面板头部 -->
    <div class="panel-header">
      <div class="panel-title">
        <svg viewBox="0 0 24 24" width="16" height="16" fill="currentColor" style="color:#4080FF">
          <path d="M3 13h8V3H3v10zm0 8h8v-6H3v6zm10 0h8V11h-8v10zm0-18v6h8V3h-8z"/>
        </svg>
        <span>数据面板</span>
      </div>
      <button class="panel-collapse-btn" @click="collapsed = !collapsed">
        <svg viewBox="0 0 24 24" width="14" height="14" fill="currentColor">
          <path v-if="collapsed" d="M8.59 16.59L13.17 12 8.59 7.41 10 6l6 6-6 6z"/>
          <path v-else d="M15.41 16.59L10.83 12l4.58-4.59L14 6l-6 6 6 6z"/>
        </svg>
      </button>
    </div>

    <!-- Tab 切换 -->
    <div class="panel-tabs" v-show="!collapsed">
      <button
        v-for="tab in tabs"
        :key="tab.key"
        class="panel-tab"
        :class="{ active: activeTab === tab.key }"
        @click="activeTab = tab.key"
      >{{ tab.label }}</button>
    </div>

    <!-- Tab 内容 -->
    <div class="panel-body" v-show="!collapsed">
      <!-- 综合概览 -->
      <div v-show="activeTab === 'overview'" class="tab-content">
        <div class="kpi-grid">
          <div class="kpi-card">
            <div class="kpi-label">入驻企业</div>
            <div class="kpi-value">{{ overview.kpi.enterpriseCount }}<span class="kpi-unit">家</span></div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">在线设备</div>
            <div class="kpi-value">{{ overview.kpi.onlineDevices }}<span class="kpi-unit">台</span></div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">今日能耗</div>
            <div class="kpi-value">{{ overview.kpi.todayEnergy }}<span class="kpi-unit">kWh</span></div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">告警数</div>
            <div class="kpi-value" :style="{ color: overview.kpi.alertCount > 10 ? '#FF4560' : '#00E396' }">
              {{ overview.kpi.alertCount }}<span class="kpi-unit">条</span>
            </div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">停车位</div>
            <div class="kpi-value">{{ overview.kpi.parkingUsed }}<span class="kpi-unit">/{{ overview.kpi.parkingTotal }}</span></div>
            <div class="progress-bar" style="margin-top:6px">
              <div class="progress-fill" :style="{ width: (overview.kpi.parkingUsed / overview.kpi.parkingTotal * 100) + '%' }"></div>
            </div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">园区人数</div>
            <div class="kpi-value">{{ overview.kpi.peopleCount }}<span class="kpi-unit">人</span></div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">绿化率</div>
            <div class="kpi-value">{{ overview.kpi.greenRate }}<span class="kpi-unit">%</span></div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">AQI</div>
            <div class="kpi-value" style="color:#00E396">{{ overview.environment.aqi }}<span class="kpi-unit">{{ overview.environment.aqiLevel }}</span></div>
          </div>
        </div>

        <!-- 告警列表 -->
        <div class="glass-panel" style="margin-top:12px">
          <div class="panel-title">
            <span class="panel-icon" style="font-size:12px">&#9888;</span>
            <span>最新告警</span>
          </div>
          <div class="alert-list">
            <div v-for="alert in overview.safety.topAlerts" :key="alert.name" class="alert-item">
              <span class="alert-dot" :class="alert.level"></span>
              <span>{{ alert.name }}</span>
              <span class="alert-time">{{ alert.time }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 安全态势 -->
      <div v-show="activeTab === 'safety'" class="tab-content">
        <div class="kpi-grid" style="margin-bottom:12px">
          <div class="kpi-card">
            <div class="kpi-label">今日事件</div>
            <div class="kpi-value">{{ overview.safety.todayEvents }}</div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">紧急告警</div>
            <div class="kpi-value" style="color:#FF4560">{{ overview.safety.criticalAlerts }}</div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">重要告警</div>
            <div class="kpi-value" style="color:#FEB019">{{ overview.safety.majorAlerts }}</div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">一般告警</div>
            <div class="kpi-value" style="color:#90CAF9">{{ overview.safety.minorAlerts }}</div>
          </div>
        </div>

        <!-- 安全事件趋势 -->
        <div class="glass-panel" style="margin-bottom:12px">
          <div class="panel-title"><span>安全事件趋势</span></div>
          <div class="chart-container" ref="safetyTrendChartRef"></div>
        </div>

        <!-- 告警等级分布 -->
        <div class="glass-panel">
          <div class="panel-title"><span>告警等级分布</span></div>
          <div class="chart-container" ref="alertPieChartRef"></div>
        </div>
      </div>

      <!-- 能源管理 -->
      <div v-show="activeTab === 'energy'" class="tab-content">
        <div class="kpi-grid" style="margin-bottom:12px">
          <div class="kpi-card">
            <div class="kpi-label">光伏发电</div>
            <div class="kpi-value" style="color:#FEB019">{{ overview.energy.solarPower }}<span class="kpi-unit">kW</span></div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">日发电量</div>
            <div class="kpi-value">{{ overview.energy.dailyGen }}<span class="kpi-unit">kWh</span></div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">日用电量</div>
            <div class="kpi-value">{{ overview.energy.dailyConsume }}<span class="kpi-unit">kWh</span></div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">储能SOC</div>
            <div class="kpi-value" style="color:#00E396">{{ overview.energy.storageSoc }}<span class="kpi-unit">%</span></div>
            <div class="progress-bar" style="margin-top:6px">
              <div class="progress-fill" :style="{ width: overview.energy.storageSoc + '%' }"></div>
            </div>
          </div>
        </div>

        <!-- 能耗趋势 -->
        <div class="glass-panel" style="margin-bottom:12px">
          <div class="panel-title"><span>能耗趋势</span></div>
          <div class="chart-container" ref="energyTrendChartRef"></div>
        </div>

        <!-- 碳排放统计 -->
        <div class="glass-panel">
          <div class="panel-title"><span>碳排放统计</span></div>
          <div class="info-grid" style="display:grid;grid-template-columns:1fr 1fr;gap:8px;margin-top:8px">
            <div class="info-item" style="display:flex;flex-direction:column;align-items:center;padding:10px;background:rgba(64,128,255,0.05);border-radius:8px">
              <span style="font-size:11px;color:#667799">碳排放量</span>
              <span style="font-size:18px;font-weight:700;color:#FF4560">{{ overview.energy.carbonEmission }}<span style="font-size:11px;color:#667799"> tCO2</span></span>
            </div>
            <div class="info-item" style="display:flex;flex-direction:column;align-items:center;padding:10px;background:rgba(64,128,255,0.05);border-radius:8px">
              <span style="font-size:11px;color:#667799">碳减排量</span>
              <span style="font-size:18px;font-weight:700;color:#00E396">{{ overview.energy.carbonReduction }}<span style="font-size:11px;color:#667799"> tCO2</span></span>
            </div>
          </div>
        </div>
      </div>

      <!-- 环境监测 -->
      <div v-show="activeTab === 'environment'" class="tab-content">
        <div class="glass-panel" style="margin-bottom:12px">
          <div class="panel-title"><span>AQI 指标</span></div>
          <div class="chart-container-sm" ref="aqiGaugeRef"></div>
        </div>

        <div class="kpi-grid" style="margin-bottom:12px">
          <div class="kpi-card">
            <div class="kpi-label">PM2.5</div>
            <div class="kpi-value" style="color:#00E396">{{ overview.environment.pm25 }}<span class="kpi-unit">μg/m³</span></div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">PM10</div>
            <div class="kpi-value">{{ overview.environment.pm10 }}<span class="kpi-unit">μg/m³</span></div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">温度</div>
            <div class="kpi-value">{{ overview.environment.temperature }}<span class="kpi-unit">°C</span></div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">湿度</div>
            <div class="kpi-value">{{ overview.environment.humidity }}<span class="kpi-unit">%</span></div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">噪声</div>
            <div class="kpi-value">{{ overview.environment.noise }}<span class="kpi-unit">dB</span></div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">水质</div>
            <div class="kpi-value" style="color:#00D4FF;font-size:16px">{{ overview.environment.waterQuality }}</div>
          </div>
        </div>

        <!-- PM2.5 趋势 -->
        <div class="glass-panel">
          <div class="panel-title"><span>PM2.5 趋势</span></div>
          <div class="chart-container" ref="pm25TrendChartRef"></div>
        </div>
      </div>

      <!-- 设备运营 -->
      <div v-show="activeTab === 'equipment'" class="tab-content">
        <div class="kpi-grid" style="margin-bottom:12px">
          <div class="kpi-card">
            <div class="kpi-label">设备总数</div>
            <div class="kpi-value">{{ overview.equipment.totalDevices }}<span class="kpi-unit">台</span></div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">在线率</div>
            <div class="kpi-value" style="color:#00E396">{{ overview.equipment.onlineRate }}<span class="kpi-unit">%</span></div>
            <div class="progress-bar" style="margin-top:6px">
              <div class="progress-fill" :style="{ width: overview.equipment.onlineRate + '%' }"></div>
            </div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">维保任务</div>
            <div class="kpi-value">{{ overview.equipment.maintenanceCount }}<span class="kpi-unit">项</span></div>
          </div>
          <div class="kpi-card">
            <div class="kpi-label">待处理工单</div>
            <div class="kpi-value" :style="{ color: overview.equipment.workOrderPending > 0 ? '#FEB019' : '#00E396' }">
              {{ overview.equipment.workOrderPending }}<span class="kpi-unit">/{{ overview.equipment.workOrderCount }}</span>
            </div>
          </div>
        </div>

        <!-- 设备在线率仪表盘 -->
        <div class="glass-panel" style="margin-bottom:12px">
          <div class="panel-title"><span>设备在线率</span></div>
          <div class="chart-container-sm" ref="onlineGaugeRef"></div>
        </div>

        <!-- 工单统计 -->
        <div class="glass-panel">
          <div class="panel-title"><span>工单统计</span></div>
          <div class="chart-container" ref="workOrderChartRef"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted, onBeforeUnmount, nextTick } from 'vue'
import * as echarts from 'echarts'
import { useParkOverviewData } from '../composables/useTwinData'

const props = defineProps({
  activeTab: { type: String, default: 'overview' }
})

const collapsed = ref(false)
const activeTab = ref(props.activeTab)

watch(() => props.activeTab, (val) => {
  activeTab.value = val
  nextTick(() => initCharts())
})

const tabs = [
  { key: 'overview', label: '综合概览' },
  { key: 'safety', label: '安全态势' },
  { key: 'energy', label: '能源管理' },
  { key: 'environment', label: '环境监测' },
  { key: 'equipment', label: '设备运营' }
]

const { kpi, safety, energy, environment, equipment, energyTrend, alertTrend, get24hLabels, get7dLabels, loaded } = useParkOverviewData()

const overview = reactive({ kpi, safety, energy, environment, equipment })

// 图表引用
const safetyTrendChartRef = ref(null)
const alertPieChartRef = ref(null)
const energyTrendChartRef = ref(null)
const aqiGaugeRef = ref(null)
const pm25TrendChartRef = ref(null)
const onlineGaugeRef = ref(null)
const workOrderChartRef = ref(null)

let charts = []
let resizeHandler = null

function createChart(el) {
  if (!el) return null
  const chart = echarts.init(el)
  charts.push(chart)
  return chart
}

function initCharts() {
  // 安全态势图表
  if (activeTab.value === 'safety') {
    initSafetyCharts()
  }
  // 能源管理图表
  if (activeTab.value === 'energy') {
    initEnergyCharts()
  }
  // 环境监测图表
  if (activeTab.value === 'environment') {
    initEnvironmentCharts()
  }
  // 设备运营图表
  if (activeTab.value === 'equipment') {
    initEquipmentCharts()
  }
}

function initSafetyCharts() {
  // 安全事件趋势
  if (safetyTrendChartRef.value) {
    const chart = createChart(safetyTrendChartRef.value)
    chart.setOption({
      backgroundColor: 'transparent',
      tooltip: { trigger: 'axis', backgroundColor: 'rgba(16,32,64,0.9)', borderColor: 'rgba(64,128,255,0.3)', textStyle: { color: '#E0E8FF' } },
      grid: { left: '10%', right: '5%', top: '10%', bottom: '15%', containLabel: true },
      xAxis: { type: 'category', data: get7dLabels(), axisLine: { lineStyle: { color: 'rgba(64,128,255,0.2)' } }, axisLabel: { color: '#667799', fontSize: 10 } },
      yAxis: { type: 'value', axisLine: { show: false }, axisLabel: { color: '#667799', fontSize: 10 }, splitLine: { lineStyle: { color: 'rgba(64,128,255,0.06)' } } },
      series: [{
        type: 'bar',
        data: alertTrend.value,
        barWidth: '40%',
        itemStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: '#FF4560' }, { offset: 1, color: 'rgba(255,69,96,0.3)' }]), borderRadius: [3, 3, 0, 0] }
      }]
    })
  }

  // 告警等级分布饼图
  if (alertPieChartRef.value) {
    const chart = createChart(alertPieChartRef.value)
    chart.setOption({
      backgroundColor: 'transparent',
      tooltip: { trigger: 'item', backgroundColor: 'rgba(16,32,64,0.9)', borderColor: 'rgba(64,128,255,0.3)', textStyle: { color: '#E0E8FF' } },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '50%'],
        data: [
          { value: safety.criticalAlerts, name: '紧急', itemStyle: { color: '#FF4560' } },
          { value: safety.majorAlerts, name: '重要', itemStyle: { color: '#FEB019' } },
          { value: safety.minorAlerts, name: '一般', itemStyle: { color: '#90CAF9' } },
          { value: safety.infoAlerts, name: '提示', itemStyle: { color: '#666' } }
        ],
        label: { color: '#8899BB', fontSize: 11 },
        labelLine: { lineStyle: { color: 'rgba(64,128,255,0.3)' } }
      }]
    })
  }
}

function initEnergyCharts() {
  if (energyTrendChartRef.value) {
    const chart = createChart(energyTrendChartRef.value)
    chart.setOption({
      backgroundColor: 'transparent',
      tooltip: { trigger: 'axis', backgroundColor: 'rgba(16,32,64,0.9)', borderColor: 'rgba(64,128,255,0.3)', textStyle: { color: '#E0E8FF' } },
      legend: { textStyle: { color: '#8899BB', fontSize: 11 }, icon: 'roundRect', itemWidth: 12, itemHeight: 8, top: 0 },
      grid: { left: '10%', right: '5%', top: '15%', bottom: '12%', containLabel: true },
      xAxis: { type: 'category', data: get24hLabels(), axisLine: { lineStyle: { color: 'rgba(64,128,255,0.2)' } }, axisLabel: { color: '#667799', fontSize: 10, interval: 3 } },
      yAxis: { type: 'value', axisLine: { show: false }, axisLabel: { color: '#667799', fontSize: 10 }, splitLine: { lineStyle: { color: 'rgba(64,128,255,0.06)' } } },
      series: [
        {
          name: '发电', type: 'line', data: energyTrend.value.map(v => v * 0.4), smooth: true, symbol: 'none',
          lineStyle: { color: '#FEB019', width: 2 },
          areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(254,176,25,0.3)' }, { offset: 1, color: 'rgba(254,176,25,0.02)' }]) }
        },
        {
          name: '用电', type: 'line', data: energyTrend.value, smooth: true, symbol: 'none',
          lineStyle: { color: '#4080FF', width: 2 },
          areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(64,128,255,0.3)' }, { offset: 1, color: 'rgba(64,128,255,0.02)' }]) }
        }
      ]
    })
  }
}

function initEnvironmentCharts() {
  // AQI 仪表盘
  if (aqiGaugeRef.value) {
    const chart = createChart(aqiGaugeRef.value)
    chart.setOption({
      backgroundColor: 'transparent',
      series: [{
        type: 'gauge',
        startAngle: 220,
        endAngle: -40,
        min: 0,
        max: 300,
        radius: '85%',
        progress: { show: true, width: 12 },
        pointer: { show: false },
        axisLine: { lineStyle: { width: 12, color: [[0.17, '#00E396'], [0.33, '#FEB019'], [0.5, '#FF8C42'], [0.67, '#FF4560'], [1, '#9B1C1C']] } },
        axisTick: { show: false },
        splitLine: { show: false },
        axisLabel: { show: false },
        title: { show: true, offsetCenter: [0, '70%'], color: '#8899BB', fontSize: 12 },
        detail: { valueAnimation: true, fontSize: 22, fontWeight: 700, offsetCenter: [0, '40%'], formatter: '{value}', color: '#00E396' },
        data: [{ value: environment.aqi, name: 'AQI' }]
      }]
    })
  }

  // PM2.5 趋势
  if (pm25TrendChartRef.value) {
    const chart = createChart(pm25TrendChartRef.value)
    const pm25Data = Array.from({ length: 24 }, () => Math.floor(20 + Math.random() * 40))
    chart.setOption({
      backgroundColor: 'transparent',
      tooltip: { trigger: 'axis', backgroundColor: 'rgba(16,32,64,0.9)', borderColor: 'rgba(64,128,255,0.3)', textStyle: { color: '#E0E8FF' } },
      grid: { left: '10%', right: '5%', top: '10%', bottom: '15%', containLabel: true },
      xAxis: { type: 'category', data: get24hLabels(), axisLine: { lineStyle: { color: 'rgba(64,128,255,0.2)' } }, axisLabel: { color: '#667799', fontSize: 10, interval: 3 } },
      yAxis: { type: 'value', axisLine: { show: false }, axisLabel: { color: '#667799', fontSize: 10 }, splitLine: { lineStyle: { color: 'rgba(64,128,255,0.06)' } } },
      series: [{
        type: 'line', data: pm25Data, smooth: true, symbol: 'none',
        lineStyle: { color: '#00D4FF', width: 2 },
        areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(0,212,255,0.3)' }, { offset: 1, color: 'rgba(0,212,255,0.02)' }]) }
      }]
    })
  }
}

function initEquipmentCharts() {
  // 设备在线率仪表盘
  if (onlineGaugeRef.value) {
    const chart = createChart(onlineGaugeRef.value)
    chart.setOption({
      backgroundColor: 'transparent',
      series: [{
        type: 'gauge',
        startAngle: 220,
        endAngle: -40,
        min: 0,
        max: 100,
        radius: '85%',
        progress: { show: true, width: 12 },
        pointer: { show: false },
        axisLine: { lineStyle: { width: 12, color: [[0.3, '#FF4560'], [0.7, '#FEB019'], [1, '#00E396']] } },
        axisTick: { show: false },
        splitLine: { show: false },
        axisLabel: { show: false },
        detail: { valueAnimation: true, fontSize: 22, fontWeight: 700, offsetCenter: [0, '30%'], formatter: '{value}%', color: '#00E396' },
        data: [{ value: equipment.onlineRate }]
      }]
    })
  }

  // 工单统计
  if (workOrderChartRef.value) {
    const chart = createChart(workOrderChartRef.value)
    chart.setOption({
      backgroundColor: 'transparent',
      tooltip: { trigger: 'axis', backgroundColor: 'rgba(16,32,64,0.9)', borderColor: 'rgba(64,128,255,0.3)', textStyle: { color: '#E0E8FF' } },
      grid: { left: '10%', right: '5%', top: '10%', bottom: '15%', containLabel: true },
      xAxis: { type: 'category', data: get7dLabels(), axisLine: { lineStyle: { color: 'rgba(64,128,255,0.2)' } }, axisLabel: { color: '#667799', fontSize: 10 } },
      yAxis: { type: 'value', axisLine: { show: false }, axisLabel: { color: '#667799', fontSize: 10 }, splitLine: { lineStyle: { color: 'rgba(64,128,255,0.06)' } } },
      series: [
        {
          name: '新建', type: 'bar', stack: 'total', data: [5, 3, 8, 2, 6, 4, 3], barWidth: '40%',
          itemStyle: { color: '#4080FF', borderRadius: [0, 0, 0, 0] }
        },
        {
          name: '处理中', type: 'bar', stack: 'total', data: [3, 2, 4, 1, 3, 2, 2],
          itemStyle: { color: '#FEB019' }
        },
        {
          name: '已完成', type: 'bar', stack: 'total', data: [4, 5, 6, 3, 5, 4, 3],
          itemStyle: { color: '#00E396', borderRadius: [3, 3, 0, 0] }
        }
      ]
    })
  }
}

onMounted(() => {
  resizeHandler = () => charts.forEach(c => c.resize())
  window.addEventListener('resize', resizeHandler)
})

onBeforeUnmount(() => {
  if (resizeHandler) window.removeEventListener('resize', resizeHandler)
  charts.forEach(c => c.dispose())
  charts = []
})

watch(loaded, (val) => {
  if (val) {
    nextTick(() => initCharts())
  }
})
</script>

<style scoped lang="scss">
@import '../styles/twin.scss';

.tab-content {
  animation: fadeIn 0.3s ease;
}
</style>
