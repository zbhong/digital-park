<template>
  <div class="panel-left">
    <!-- 园区总览 Tab -->
    <template v-if="activeTab === 'overview'">
      <DataCard title="园区综合信息" :delay="0">
        <div class="info-grid">
          <div class="info-item" v-for="item in parkInfo" :key="item.label">
            <div class="info-value">{{ item.value }}</div>
            <div class="info-label">{{ item.label }}</div>
          </div>
        </div>
      </DataCard>
      <DataCard title="安防消防设备状态" :delay="0.1">
        <div ref="safetyPieRef" class="chart-container"></div>
      </DataCard>
      <DataCard title="隐患趋势与AI识别" :delay="0.2">
        <div ref="hazardBarRef" class="chart-container"></div>
      </DataCard>
    </template>

    <!-- 安全管理 Tab -->
    <template v-if="activeTab === 'safety'">
      <DataCard title="安防实时态势" :delay="0">
        <div class="safety-stats">
          <div class="safety-stat-item" v-for="item in safetyStats" :key="item.label">
            <div class="safety-stat-value" :style="{ color: item.color }">{{ item.value }}</div>
            <div class="safety-stat-label">{{ item.label }}</div>
          </div>
        </div>
      </DataCard>
      <DataCard title="消防设备健康状态" :delay="0.1">
        <div ref="firePieRef" class="chart-container"></div>
      </DataCard>
      <DataCard title="用电安全监测" :delay="0.2">
        <div class="monitor-list">
          <div class="monitor-item" v-for="item in electricalMonitor" :key="item.name">
            <span class="monitor-name">{{ item.name }}</span>
            <span class="monitor-status" :class="item.status">{{ item.statusText }}</span>
            <span class="monitor-value">{{ item.value }}</span>
          </div>
        </div>
      </DataCard>
    </template>

    <!-- 能源管理 Tab -->
    <template v-if="activeTab === 'energy'">
      <DataCard title="今日能耗总览" :delay="0">
        <div class="energy-overview">
          <div class="energy-big-number">
            <span class="energy-value">3,842</span>
            <span class="energy-unit">kWh</span>
          </div>
          <div class="energy-compare">
            <span class="compare-down">↓ 12.3%</span>
            <span class="compare-label">较昨日</span>
          </div>
        </div>
      </DataCard>
      <DataCard title="分项能耗统计" :delay="0.1">
        <div ref="energyCategoryRef" class="chart-container"></div>
      </DataCard>
      <DataCard title="能耗趋势分析" :delay="0.2">
        <div ref="energyTrendRef" class="chart-container chart-container--tall"></div>
      </DataCard>
    </template>

    <!-- 环境管理 Tab -->
    <template v-if="activeTab === 'environment'">
      <DataCard title="环境实时指标" :delay="0">
        <div class="env-grid">
          <div class="env-item" v-for="item in envMetrics" :key="item.label">
            <div class="env-value" :style="{ color: item.color }">{{ item.value }}</div>
            <div class="env-label">{{ item.label }}</div>
            <div class="env-status" :class="item.level">{{ item.levelText }}</div>
          </div>
        </div>
      </DataCard>
      <DataCard title="24小时环境趋势" :delay="0.1">
        <div ref="envTrendRef" class="chart-container chart-container--tall"></div>
      </DataCard>
      <DataCard title="环境整体评级" :delay="0.2">
        <div class="env-rating">
          <div class="rating-circle">
            <span class="rating-score">92</span>
            <span class="rating-label">优</span>
          </div>
          <div class="rating-desc">空气质量优良，温湿度适宜</div>
        </div>
      </DataCard>
    </template>

    <!-- 资产管理 Tab -->
    <template v-if="activeTab === 'asset'">
      <DataCard title="资产总览" :delay="0">
        <div class="asset-overview">
          <div class="asset-stat" v-for="item in assetOverview" :key="item.label">
            <div class="asset-stat-value">{{ item.value }}</div>
            <div class="asset-stat-label">{{ item.label }}</div>
          </div>
        </div>
      </DataCard>
      <DataCard title="楼栋资产分类" :delay="0.1">
        <div ref="buildingAssetRef" class="chart-container"></div>
      </DataCard>
      <DataCard title="设备完好率" :delay="0.2">
        <div ref="assetHealthRef" class="chart-container"></div>
      </DataCard>
    </template>
  </div>
</template>

<script setup>
import DataCard from './DataCard.vue'
import * as echarts from 'echarts'
import { ref, watch, onMounted, onBeforeUnmount, nextTick } from 'vue'

const props = defineProps({
  activeTab: {
    type: String,
    default: 'overview'
  }
})

// ============ Chart Refs ============
const safetyPieRef = ref(null)
const hazardBarRef = ref(null)
const firePieRef = ref(null)
const energyCategoryRef = ref(null)
const energyTrendRef = ref(null)
const envTrendRef = ref(null)
const buildingAssetRef = ref(null)
const assetHealthRef = ref(null)

// ============ Chart Instances ============
let safetyPieChart = null
let hazardBarChart = null
let firePieChart = null
let energyCategoryChart = null
let energyTrendChart = null
let envTrendChart = null
let buildingAssetChart = null
let assetHealthChart = null

// ============ 统一颜色系列 ============
const COLORS = ['#00E5FF', '#27C97A', '#FF9500', '#FF3333', '#7C5CFC']
const AXIS_LINE_COLOR = 'rgba(255,255,255,0.1)'
const SPLIT_LINE_COLOR = 'rgba(255,255,255,0.05)'
const TEXT_COLOR = 'rgba(232,240,254,0.6)'
const TEXT_COLOR_LIGHT = 'rgba(232,240,254,0.85)'

// ============ 模拟数据 ============

// 园区总览 - 园区综合信息
const parkInfo = ref([
  { label: '总面积', value: '12.8万m²' },
  { label: '入驻企业', value: '186家' },
  { label: '园区人数', value: '8,432人' },
  { label: '停车位', value: '1,260个' }
])

// 安全管理 - 安防实时态势
const safetyStats = ref([
  { label: '监控点位', value: '1,024', color: '#00E5FF' },
  { label: '在线率', value: '98.6%', color: '#27C97A' },
  { label: '今日告警', value: '23', color: '#FF9500' },
  { label: '待处理', value: '5', color: '#FF3333' },
  { label: '巡检次数', value: '47', color: '#7C5CFC' },
  { label: '安全评分', value: '96', color: '#00E5FF' }
])

// 安全管理 - 用电安全监测
const electricalMonitor = ref([
  { name: 'A栋配电室', status: 'normal', statusText: '正常', value: '342A' },
  { name: 'B栋配电室', status: 'normal', statusText: '正常', value: '287A' },
  { name: 'C栋配电室', status: 'warning', statusText: '预警', value: '498A' },
  { name: '地下车库照明', status: 'normal', statusText: '正常', value: '156A' },
  { name: '中央空调主机', status: 'normal', statusText: '正常', value: '623A' },
  { name: '消防泵房', status: 'normal', statusText: '正常', value: '89A' }
])

// 环境管理 - 环境实时指标
const envMetrics = ref([
  { label: '温度', value: '24.5°C', color: '#00E5FF', level: 'good', levelText: '适宜' },
  { label: '湿度', value: '58%', color: '#27C97A', level: 'good', levelText: '适宜' },
  { label: 'PM2.5', value: '35μg/m³', color: '#27C97A', level: 'good', levelText: '优' },
  { label: '噪音', value: '48dB', color: '#FF9500', level: 'warning', levelText: '偏高' }
])

// 资产管理 - 资产总览
const assetOverview = ref([
  { label: '资产总数', value: '12,486' },
  { label: '在用资产', value: '11,203' },
  { label: '闲置资产', value: '892' },
  { label: '报废资产', value: '391' }
])

// ============ 图表初始化函数 ============

/** 园区总览 - 安防消防设备状态（环形图） */
function initSafetyPie() {
  if (!safetyPieRef.value) return
  safetyPieChart = echarts.init(safetyPieRef.value)
  safetyPieChart.setOption({
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(8,24,58,0.9)',
      borderColor: 'rgba(0,229,255,0.3)',
      textStyle: { color: '#E8F0FE' }
    },
    legend: {
      bottom: 0,
      textStyle: { color: TEXT_COLOR, fontSize: 11 },
      itemWidth: 10,
      itemHeight: 10,
      itemGap: 16
    },
    series: [
      {
        type: 'pie',
        radius: ['42%', '68%'],
        center: ['50%', '45%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 4,
          borderColor: 'rgba(8,24,58,0.8)',
          borderWidth: 2
        },
        label: {
          show: true,
          position: 'center',
          formatter: '{total|856}\n{label|设备总数}',
          rich: {
            total: {
              fontSize: 22,
              fontWeight: 'bold',
              color: TEXT_COLOR_LIGHT,
              lineHeight: 30
            },
            label: {
              fontSize: 11,
              color: TEXT_COLOR,
              lineHeight: 20
            }
          }
        },
        emphasis: {
          label: { show: true },
          itemStyle: {
            shadowBlur: 20,
            shadowColor: 'rgba(0,229,255,0.3)'
          }
        },
        data: [
          { value: 798, name: '在线', itemStyle: { color: '#00E5FF' } },
          { value: 32, name: '离线', itemStyle: { color: '#FF3333' } },
          { value: 26, name: '维护中', itemStyle: { color: '#FF9500' } }
        ]
      }
    ]
  })
}

/** 园区总览 - 隐患趋势与AI识别（柱状图） */
function initHazardBar() {
  if (!hazardBarRef.value) return
  hazardBarChart = echarts.init(hazardBarRef.value)
  const days = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
  hazardBarChart.setOption({
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(8,24,58,0.9)',
      borderColor: 'rgba(0,229,255,0.3)',
      textStyle: { color: '#E8F0FE' },
      axisPointer: { type: 'shadow' }
    },
    legend: {
      top: 0,
      textStyle: { color: TEXT_COLOR, fontSize: 11 },
      itemWidth: 10,
      itemHeight: 10,
      itemGap: 16
    },
    grid: {
      left: 36,
      right: 12,
      top: 30,
      bottom: 24
    },
    xAxis: {
      type: 'category',
      data: days,
      axisLine: { lineStyle: { color: AXIS_LINE_COLOR } },
      axisTick: { show: false },
      axisLabel: { color: TEXT_COLOR, fontSize: 10 }
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      axisTick: { show: false },
      splitLine: { lineStyle: { color: SPLIT_LINE_COLOR } },
      axisLabel: { color: TEXT_COLOR, fontSize: 10 }
    },
    series: [
      {
        name: '隐患数量',
        type: 'bar',
        barWidth: 12,
        itemStyle: {
          borderRadius: [4, 4, 0, 0],
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#00E5FF' },
            { offset: 1, color: 'rgba(0,229,255,0.15)' }
          ])
        },
        data: [12, 8, 15, 6, 10, 4, 7]
      },
      {
        name: 'AI识别事件',
        type: 'bar',
        barWidth: 12,
        itemStyle: {
          borderRadius: [4, 4, 0, 0],
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#7C5CFC' },
            { offset: 1, color: 'rgba(124,92,252,0.15)' }
          ])
        },
        data: [5, 3, 8, 2, 6, 1, 4]
      }
    ]
  })
}

/** 安全管理 - 消防设备健康状态（环形图） */
function initFirePie() {
  if (!firePieRef.value) return
  firePieChart = echarts.init(firePieRef.value)
  firePieChart.setOption({
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(8,24,58,0.9)',
      borderColor: 'rgba(0,229,255,0.3)',
      textStyle: { color: '#E8F0FE' }
    },
    legend: {
      bottom: 0,
      textStyle: { color: TEXT_COLOR, fontSize: 11 },
      itemWidth: 10,
      itemHeight: 10,
      itemGap: 16
    },
    series: [
      {
        type: 'pie',
        radius: ['42%', '68%'],
        center: ['50%', '45%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 4,
          borderColor: 'rgba(8,24,58,0.8)',
          borderWidth: 2
        },
        label: {
          show: true,
          position: 'center',
          formatter: '{total|342}\n{label|消防设备}',
          rich: {
            total: {
              fontSize: 22,
              fontWeight: 'bold',
              color: TEXT_COLOR_LIGHT,
              lineHeight: 30
            },
            label: {
              fontSize: 11,
              color: TEXT_COLOR,
              lineHeight: 20
            }
          }
        },
        emphasis: {
          label: { show: true },
          itemStyle: {
            shadowBlur: 20,
            shadowColor: 'rgba(0,229,255,0.3)'
          }
        },
        data: [
          { value: 298, name: '正常', itemStyle: { color: '#27C97A' } },
          { value: 28, name: '需维护', itemStyle: { color: '#FF9500' } },
          { value: 12, name: '故障', itemStyle: { color: '#FF3333' } },
          { value: 4, name: '已报废', itemStyle: { color: 'rgba(255,255,255,0.15)' } }
        ]
      }
    ]
  })
}

/** 能源管理 - 分项能耗统计（横向柱状图） */
function initEnergyCategory() {
  if (!energyCategoryRef.value) return
  energyCategoryChart = echarts.init(energyCategoryRef.value)
  const categories = ['空调', '照明', '电梯', '办公设备', '其他']
  energyCategoryChart.setOption({
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(8,24,58,0.9)',
      borderColor: 'rgba(0,229,255,0.3)',
      textStyle: { color: '#E8F0FE' },
      axisPointer: { type: 'shadow' }
    },
    grid: {
      left: 70,
      right: 30,
      top: 8,
      bottom: 8
    },
    xAxis: {
      type: 'value',
      axisLine: { show: false },
      axisTick: { show: false },
      splitLine: { lineStyle: { color: SPLIT_LINE_COLOR } },
      axisLabel: { color: TEXT_COLOR, fontSize: 10, formatter: '{value} kWh' }
    },
    yAxis: {
      type: 'category',
      data: categories,
      axisLine: { show: false },
      axisTick: { show: false },
      axisLabel: { color: TEXT_COLOR, fontSize: 11 }
    },
    series: [
      {
        type: 'bar',
        barWidth: 14,
        itemStyle: {
          borderRadius: [0, 4, 4, 0],
          color: (params) => {
            const colorList = [
              new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                { offset: 0, color: 'rgba(0,229,255,0.15)' },
                { offset: 1, color: '#00E5FF' }
              ]),
              new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                { offset: 0, color: 'rgba(39,201,122,0.15)' },
                { offset: 1, color: '#27C97A' }
              ]),
              new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                { offset: 0, color: 'rgba(124,92,252,0.15)' },
                { offset: 1, color: '#7C5CFC' }
              ]),
              new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                { offset: 0, color: 'rgba(255,149,0,0.15)' },
                { offset: 1, color: '#FF9500' }
              ]),
              new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                { offset: 0, color: 'rgba(255,255,255,0.05)' },
                { offset: 1, color: 'rgba(255,255,255,0.3)' }
              ])
            ]
            return colorList[params.dataIndex]
          }
        },
        data: [1520, 680, 420, 860, 362]
      }
    ]
  })
}

/** 能源管理 - 能耗趋势分析（折线面积图） */
function initEnergyTrend() {
  if (!energyTrendRef.value) return
  energyTrendChart = echarts.init(energyTrendRef.value)
  const hours = Array.from({ length: 24 }, (_, i) => `${String(i).padStart(2, '0')}:00`)
  energyTrendChart.setOption({
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(8,24,58,0.9)',
      borderColor: 'rgba(0,229,255,0.3)',
      textStyle: { color: '#E8F0FE' }
    },
    legend: {
      top: 0,
      textStyle: { color: TEXT_COLOR, fontSize: 11 },
      itemWidth: 16,
      itemHeight: 3,
      itemGap: 16
    },
    grid: {
      left: 36,
      right: 12,
      top: 30,
      bottom: 24
    },
    xAxis: {
      type: 'category',
      data: hours,
      boundaryGap: false,
      axisLine: { lineStyle: { color: AXIS_LINE_COLOR } },
      axisTick: { show: false },
      axisLabel: {
        color: TEXT_COLOR,
        fontSize: 10,
        interval: 3
      }
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      axisTick: { show: false },
      splitLine: { lineStyle: { color: SPLIT_LINE_COLOR } },
      axisLabel: { color: TEXT_COLOR, fontSize: 10 }
    },
    series: [
      {
        name: '今日',
        type: 'line',
        smooth: true,
        symbol: 'none',
        lineStyle: { color: '#00E5FF', width: 2 },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(0,229,255,0.35)' },
            { offset: 1, color: 'rgba(0,229,255,0)' }
          ])
        },
        data: [120, 85, 60, 45, 40, 50, 110, 220, 310, 280, 260, 250, 270, 290, 300, 280, 260, 240, 200, 180, 160, 140, 130, 125]
      },
      {
        name: '昨日',
        type: 'line',
        smooth: true,
        symbol: 'none',
        lineStyle: { color: '#7C5CFC', width: 1.5, type: 'dashed' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(124,92,252,0.15)' },
            { offset: 1, color: 'rgba(124,92,252,0)' }
          ])
        },
        data: [130, 95, 70, 55, 48, 58, 125, 240, 340, 310, 290, 275, 295, 315, 330, 305, 285, 265, 225, 200, 180, 160, 145, 138]
      }
    ]
  })
}

/** 环境管理 - 24小时环境趋势（折线面积图） */
function initEnvTrend() {
  if (!envTrendRef.value) return
  envTrendChart = echarts.init(envTrendRef.value)
  const hours = Array.from({ length: 24 }, (_, i) => `${String(i).padStart(2, '0')}:00`)
  envTrendChart.setOption({
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(8,24,58,0.9)',
      borderColor: 'rgba(0,229,255,0.3)',
      textStyle: { color: '#E8F0FE' }
    },
    legend: {
      top: 0,
      textStyle: { color: TEXT_COLOR, fontSize: 11 },
      itemWidth: 16,
      itemHeight: 3,
      itemGap: 16
    },
    grid: {
      left: 36,
      right: 36,
      top: 30,
      bottom: 24
    },
    xAxis: {
      type: 'category',
      data: hours,
      boundaryGap: false,
      axisLine: { lineStyle: { color: AXIS_LINE_COLOR } },
      axisTick: { show: false },
      axisLabel: {
        color: TEXT_COLOR,
        fontSize: 10,
        interval: 3
      }
    },
    yAxis: [
      {
        type: 'value',
        name: '温度(°C)',
        nameTextStyle: { color: TEXT_COLOR, fontSize: 10 },
        axisLine: { show: false },
        axisTick: { show: false },
        splitLine: { lineStyle: { color: SPLIT_LINE_COLOR } },
        axisLabel: { color: TEXT_COLOR, fontSize: 10 }
      },
      {
        type: 'value',
        name: '湿度(%)',
        nameTextStyle: { color: TEXT_COLOR, fontSize: 10 },
        axisLine: { show: false },
        axisTick: { show: false },
        splitLine: { show: false },
        axisLabel: { color: TEXT_COLOR, fontSize: 10 }
      }
    ],
    series: [
      {
        name: '温度',
        type: 'line',
        smooth: true,
        symbol: 'none',
        yAxisIndex: 0,
        lineStyle: { color: '#00E5FF', width: 2 },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(0,229,255,0.3)' },
            { offset: 1, color: 'rgba(0,229,255,0)' }
          ])
        },
        data: [22.1, 21.8, 21.5, 21.2, 21.0, 21.3, 22.0, 23.2, 24.5, 25.1, 25.8, 26.2, 26.5, 26.3, 25.9, 25.5, 25.0, 24.5, 24.0, 23.5, 23.0, 22.6, 22.3, 22.0]
      },
      {
        name: '湿度',
        type: 'line',
        smooth: true,
        symbol: 'none',
        yAxisIndex: 1,
        lineStyle: { color: '#27C97A', width: 2 },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(39,201,122,0.25)' },
            { offset: 1, color: 'rgba(39,201,122,0)' }
          ])
        },
        data: [65, 66, 67, 68, 69, 68, 66, 62, 58, 55, 52, 50, 49, 50, 52, 54, 56, 58, 60, 62, 63, 64, 65, 65]
      }
    ]
  })
}

/** 资产管理 - 楼栋资产分类（玫瑰图） */
function initBuildingAsset() {
  if (!buildingAssetRef.value) return
  buildingAssetChart = echarts.init(buildingAssetRef.value)
  buildingAssetChart.setOption({
    backgroundColor: 'transparent',
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(8,24,58,0.9)',
      borderColor: 'rgba(0,229,255,0.3)',
      textStyle: { color: '#E8F0FE' },
      formatter: '{b}: {c}件 ({d}%)'
    },
    legend: {
      bottom: 0,
      textStyle: { color: TEXT_COLOR, fontSize: 11 },
      itemWidth: 10,
      itemHeight: 10,
      itemGap: 12
    },
    series: [
      {
        type: 'pie',
        radius: ['15%', '65%'],
        center: ['50%', '45%'],
        roseType: 'area',
        itemStyle: {
          borderRadius: 4,
          borderColor: 'rgba(8,24,58,0.8)',
          borderWidth: 2
        },
        label: {
          show: false
        },
        emphasis: {
          itemStyle: {
            shadowBlur: 20,
            shadowColor: 'rgba(0,229,255,0.3)'
          }
        },
        data: [
          { value: 3842, name: 'A栋', itemStyle: { color: '#00E5FF' } },
          { value: 3210, name: 'B栋', itemStyle: { color: '#27C97A' } },
          { value: 2650, name: 'C栋', itemStyle: { color: '#7C5CFC' } },
          { value: 1580, name: 'D栋', itemStyle: { color: '#FF9500' } },
          { value: 1204, name: '附属设施', itemStyle: { color: '#FF3333' } }
        ]
      }
    ]
  })
}

/** 资产管理 - 设备完好率（仪表盘图） */
function initAssetHealth() {
  if (!assetHealthRef.value) return
  assetHealthChart = echarts.init(assetHealthRef.value)
  assetHealthChart.setOption({
    backgroundColor: 'transparent',
    series: [
      {
        type: 'gauge',
        center: ['50%', '58%'],
        radius: '80%',
        startAngle: 200,
        endAngle: -20,
        min: 0,
        max: 100,
        splitNumber: 10,
        axisLine: {
          lineStyle: {
            width: 12,
            color: [
              [0.7, '#FF3333'],
              [0.85, '#FF9500'],
              [1, '#00E5FF']
            ]
          }
        },
        pointer: {
          icon: 'path://M12.8,0.7l12,40.1H0.7L12.8,0.7z',
          length: '55%',
          width: 8,
          offsetCenter: [0, '-10%'],
          itemStyle: {
            color: '#00E5FF'
          }
        },
        axisTick: {
          length: 6,
          lineStyle: {
            color: 'auto',
            width: 1
          }
        },
        splitLine: {
          length: 12,
          lineStyle: {
            color: 'auto',
            width: 2
          }
        },
        axisLabel: {
          color: TEXT_COLOR,
          fontSize: 10,
          distance: -40
        },
        title: {
          offsetCenter: [0, '30%'],
          fontSize: 12,
          color: TEXT_COLOR
        },
        detail: {
          fontSize: 24,
          fontWeight: 'bold',
          offsetCenter: [0, '55%'],
          valueAnimation: true,
          formatter: '{value}%',
          color: '#00E5FF'
        },
        data: [
          {
            value: 96.8,
            name: '设备完好率'
          }
        ]
      }
    ]
  })
}

// ============ 销毁所有图表 ============
function disposeAllCharts() {
  safetyPieChart?.dispose()
  hazardBarChart?.dispose()
  firePieChart?.dispose()
  energyCategoryChart?.dispose()
  energyTrendChart?.dispose()
  envTrendChart?.dispose()
  buildingAssetChart?.dispose()
  assetHealthChart?.dispose()
  safetyPieChart = null
  hazardBarChart = null
  firePieChart = null
  energyCategoryChart = null
  energyTrendChart = null
  envTrendChart = null
  buildingAssetChart = null
  assetHealthChart = null
}

// ============ 根据当前Tab初始化图表 ============
function initChartsForTab(tab) {
  disposeAllCharts()
  nextTick(() => {
    switch (tab) {
      case 'overview':
        initSafetyPie()
        initHazardBar()
        break
      case 'safety':
        initFirePie()
        break
      case 'energy':
        initEnergyCategory()
        initEnergyTrend()
        break
      case 'environment':
        initEnvTrend()
        break
      case 'asset':
        initBuildingAsset()
        initAssetHealth()
        break
    }
  })
}

// ============ 监听 activeTab 变化 ============
watch(
  () => props.activeTab,
  (newTab) => {
    initChartsForTab(newTab)
  }
)

// ============ 生命周期 ============
onMounted(() => {
  initChartsForTab(props.activeTab)
})

onBeforeUnmount(() => {
  disposeAllCharts()
})
</script>

<style lang="scss" scoped>
@import '../styles/cockpit.scss';

.panel-left {
  display: flex;
  flex-direction: column;
  gap: 12px;
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
}

// ============ 园区总览 - 综合信息网格 ============
.info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px 12px;
  padding: 4px 0;

  .info-item {
    text-align: center;

    .info-value {
      font-size: 20px;
      font-weight: 700;
      color: #00E5FF;
      line-height: 1.4;
      font-family: 'DIN Alternate', 'Orbitron', monospace;
    }

    .info-label {
      font-size: 12px;
      color: rgba(232, 240, 254, 0.5);
      margin-top: 4px;
    }
  }
}

// ============ 安全管理 - 安防态势网格 ============
.safety-stats {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 12px 8px;
  padding: 4px 0;

  .safety-stat-item {
    text-align: center;

    .safety-stat-value {
      font-size: 18px;
      font-weight: 700;
      line-height: 1.4;
      font-family: 'DIN Alternate', 'Orbitron', monospace;
    }

    .safety-stat-label {
      font-size: 11px;
      color: rgba(232, 240, 254, 0.5);
      margin-top: 4px;
    }
  }
}

// ============ 安全管理 - 用电监测列表 ============
.monitor-list {
  display: flex;
  flex-direction: column;
  gap: 10px;

  .monitor-item {
    display: flex;
    align-items: center;
    padding: 8px 12px;
    background: rgba(0, 229, 255, 0.03);
    border: 1px solid rgba(0, 229, 255, 0.06);
    border-radius: 6px;
    transition: background 0.3s;

    &:hover {
      background: rgba(0, 229, 255, 0.06);
    }

    .monitor-name {
      flex: 1;
      font-size: 13px;
      color: rgba(232, 240, 254, 0.75);
    }

    .monitor-status {
      font-size: 11px;
      padding: 2px 8px;
      border-radius: 10px;
      margin-right: 12px;

      &.normal {
        color: #27C97A;
        background: rgba(39, 201, 122, 0.12);
        border: 1px solid rgba(39, 201, 122, 0.2);
      }

      &.warning {
        color: #FF9500;
        background: rgba(255, 149, 0, 0.12);
        border: 1px solid rgba(255, 149, 0, 0.2);
      }

      &.error {
        color: #FF3333;
        background: rgba(255, 51, 51, 0.12);
        border: 1px solid rgba(255, 51, 51, 0.2);
      }
    }

    .monitor-value {
      font-size: 14px;
      font-weight: 600;
      color: #00E5FF;
      font-family: 'DIN Alternate', 'Orbitron', monospace;
      min-width: 48px;
      text-align: right;
    }
  }
}

// ============ 能源管理 - 能耗总览 ============
.energy-overview {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px 0 12px;

  .energy-big-number {
    display: flex;
    align-items: baseline;
    gap: 6px;

    .energy-value {
      font-size: 36px;
      font-weight: 700;
      color: #00E5FF;
      font-family: 'DIN Alternate', 'Orbitron', monospace;
      text-shadow: 0 0 20px rgba(0, 229, 255, 0.4);
    }

    .energy-unit {
      font-size: 14px;
      color: rgba(232, 240, 254, 0.5);
    }
  }

  .energy-compare {
    display: flex;
    align-items: center;
    gap: 6px;
    margin-top: 8px;

    .compare-down {
      font-size: 14px;
      font-weight: 600;
      color: #27C97A;
    }

    .compare-up {
      font-size: 14px;
      font-weight: 600;
      color: #FF3333;
    }

    .compare-label {
      font-size: 12px;
      color: rgba(232, 240, 254, 0.4);
    }
  }
}

// ============ 环境管理 - 环境指标网格 ============
.env-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px 12px;
  padding: 4px 0;

  .env-item {
    text-align: center;

    .env-value {
      font-size: 20px;
      font-weight: 700;
      line-height: 1.4;
      font-family: 'DIN Alternate', 'Orbitron', monospace;
    }

    .env-label {
      font-size: 12px;
      color: rgba(232, 240, 254, 0.5);
      margin-top: 4px;
    }

    .env-status {
      display: inline-block;
      font-size: 10px;
      padding: 1px 8px;
      border-radius: 8px;
      margin-top: 4px;

      &.good {
        color: #27C97A;
        background: rgba(39, 201, 122, 0.12);
      }

      &.warning {
        color: #FF9500;
        background: rgba(255, 149, 0, 0.12);
      }

      &.danger {
        color: #FF3333;
        background: rgba(255, 51, 51, 0.12);
      }
    }
  }
}

// ============ 环境管理 - 环境评级 ============
.env-rating {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 16px 0;

  .rating-circle {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    border: 3px solid rgba(0, 229, 255, 0.3);
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background: radial-gradient(circle, rgba(0, 229, 255, 0.08) 0%, transparent 70%);
    box-shadow: 0 0 30px rgba(0, 229, 255, 0.15), inset 0 0 20px rgba(0, 229, 255, 0.05);

    .rating-score {
      font-size: 32px;
      font-weight: 700;
      color: #00E5FF;
      font-family: 'DIN Alternate', 'Orbitron', monospace;
      line-height: 1.2;
      text-shadow: 0 0 15px rgba(0, 229, 255, 0.5);
    }

    .rating-label {
      font-size: 14px;
      color: #27C97A;
      font-weight: 600;
    }
  }

  .rating-desc {
    font-size: 12px;
    color: rgba(232, 240, 254, 0.5);
    margin-top: 12px;
  }
}

// ============ 资产管理 - 资产总览网格 ============
.asset-overview {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px 12px;
  padding: 4px 0;

  .asset-stat {
    text-align: center;

    .asset-stat-value {
      font-size: 20px;
      font-weight: 700;
      color: #00E5FF;
      line-height: 1.4;
      font-family: 'DIN Alternate', 'Orbitron', monospace;
    }

    .asset-stat-label {
      font-size: 12px;
      color: rgba(232, 240, 254, 0.5);
      margin-top: 4px;
    }
  }
}

// ============ 图表容器 ============
.chart-container {
  width: 100%;
  height: 150px;

  &--tall {
    height: 200px;
  }
}
</style>
