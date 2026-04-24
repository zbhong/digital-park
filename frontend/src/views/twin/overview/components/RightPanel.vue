<template>
  <div class="panel-right">
    <!-- 园区总览 Tab -->
    <template v-if="activeTab === 'overview'">
      <DataCard title="实时告警信息" :delay="0.05">
        <div class="alert-scroll-list">
          <div class="alert-item" v-for="(alert, i) in alertList" :key="i">
            <span class="alert-dot" :class="alert.level"></span>
            <span class="alert-name">{{ alert.name }}</span>
            <span class="alert-time">{{ alert.time }}</span>
          </div>
        </div>
      </DataCard>
      <DataCard title="设备在线状态" :delay="0.15">
        <div class="device-status-grid">
          <div class="device-status-item" v-for="item in deviceStatus" :key="item.label">
            <div class="device-status-ring" :style="{ '--ring-color': item.color }">
              <span class="device-status-num">{{ item.value }}</span>
            </div>
            <div class="device-status-label">{{ item.label }}</div>
          </div>
        </div>
      </DataCard>
      <DataCard title="快捷控制" :delay="0.25">
        <div class="device-table-wrapper">
          <table class="device-table">
            <thead>
              <tr>
                <th>设备名称</th>
                <th>设备位置</th>
                <th>设备状态</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in deviceList" :key="item.id">
                <td class="dt-name">{{ item.name }}</td>
                <td class="dt-location">{{ item.location }}</td>
                <td class="dt-status">
                  <span class="status-dot" :class="item.statusClass"></span>
                  {{ item.statusText }}
                </td>
                <td class="dt-action">
                  <span class="action-btn" @click="openDeviceDetail(item)">详情</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <!-- 设备详情弹窗 -->
        <div v-if="selectedDevice" class="detail-overlay" @click.self="selectedDevice = null">
          <div class="detail-popup">
            <div class="detail-header">
              <span class="detail-title">{{ selectedDevice.name }}</span>
              <span class="detail-close" @click="selectedDevice = null">&#10005;</span>
            </div>
            <div class="detail-body">
              <!-- Tab 切换 -->
              <div class="detail-tabs">
                <div class="detail-tab" :class="{ active: detailTab === 'params' }" @click="detailTab = 'params'">参数信息</div>
                <div class="detail-tab" :class="{ active: detailTab === 'control' }" @click="detailTab = 'control'">设备控制</div>
              </div>
              <!-- 参数信息 -->
              <div v-if="detailTab === 'params'" class="params-list">
                <div class="param-row" v-for="p in deviceParams" :key="p.label">
                  <span class="param-label">{{ p.label }}</span>
                  <span class="param-value">{{ p.value }}</span>
                </div>
              </div>
              <!-- 设备控制 -->
              <div v-if="detailTab === 'control'" class="control-panel">
                <div class="ctrl-item" v-for="ctrl in deviceControls" :key="ctrl.label">
                  <span class="ctrl-label">{{ ctrl.label }}</span>
                  <div class="ctrl-switch" :class="{ on: ctrl.on }" @click="ctrl.on = !ctrl.on">
                    <div class="ctrl-switch-thumb"></div>
                  </div>
                </div>
                <div class="ctrl-actions">
                  <button class="ctrl-btn primary" @click="handleControl('restart')">重启设备</button>
                  <button class="ctrl-btn warning" @click="handleControl('reset')">恢复出厂</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </DataCard>
    </template>

    <!-- 安全管理 Tab -->
    <template v-if="activeTab === 'safety'">
      <DataCard title="告警类型统计" :delay="0.05">
        <div ref="alertTypeBarRef" class="chart-container"></div>
      </DataCard>
      <DataCard title="告警等级分布" :delay="0.15">
        <div ref="alertLevelPieRef" class="chart-container"></div>
      </DataCard>
      <DataCard title="未处理告警清单" :delay="0.25">
        <div class="alert-scroll-list">
          <div class="alert-item" v-for="(a, i) in unhandledAlerts" :key="i">
            <span class="alert-dot" :class="a.level"></span>
            <div class="alert-detail">
              <div class="alert-detail-name">{{ a.name }}</div>
              <div class="alert-detail-info">{{ a.location }} · {{ a.time }}</div>
            </div>
          </div>
        </div>
      </DataCard>
    </template>

    <!-- 能源管理 Tab -->
    <template v-if="activeTab === 'energy'">
      <DataCard title="24小时用电趋势" :delay="0.05">
        <div ref="powerCurveRef" class="chart-container"></div>
      </DataCard>
      <DataCard title="光伏/储能/充电桩状态" :delay="0.15">
        <div class="energy-device-grid">
          <div class="energy-device-item" v-for="item in energyDevices" :key="item.label">
            <div class="ed-header">
              <span class="ed-name">{{ item.label }}</span>
              <span class="ed-status" :class="item.status">{{ item.statusText }}</span>
            </div>
            <div class="ed-value">{{ item.value }}</div>
            <div class="ed-desc">{{ item.desc }}</div>
          </div>
        </div>
      </DataCard>
      <DataCard title="能源负荷排行" :delay="0.25">
        <div ref="energyRankRef" class="chart-container"></div>
      </DataCard>
    </template>

    <!-- 环境管理 Tab -->
    <template v-if="activeTab === 'environment'">
      <DataCard title="环境告警统计" :delay="0.05">
        <div ref="envAlertRef" class="chart-container"></div>
      </DataCard>
      <DataCard title="扬尘/水质监测" :delay="0.15">
        <div class="env-special-grid">
          <div class="env-special-item" v-for="item in envSpecial" :key="item.label">
            <div class="es-label">{{ item.label }}</div>
            <div class="es-value" :style="{ color: item.color }">{{ item.value }}</div>
            <div class="es-status" :class="item.level">{{ item.statusText }}</div>
          </div>
        </div>
      </DataCard>
      <DataCard title="异常点位排行" :delay="0.25">
        <div class="rank-list">
          <div class="rank-item" v-for="(item, i) in envRankList" :key="i">
            <span class="rank-index" :class="{ top3: i < 3 }">{{ i + 1 }}</span>
            <span class="rank-name">{{ item.name }}</span>
            <span class="rank-value">{{ item.count }}次</span>
          </div>
        </div>
      </DataCard>
    </template>

    <!-- 资产管理 Tab -->
    <template v-if="activeTab === 'asset'">
      <DataCard title="设备类型占比" :delay="0.05">
        <div ref="assetTypePieRef" class="chart-container"></div>
      </DataCard>
      <DataCard title="维保任务进度" :delay="0.15">
        <div class="maintenance-list">
          <div class="maintenance-item" v-for="item in maintenanceList" :key="item.name">
            <div class="ml-header">
              <span class="ml-name">{{ item.name }}</span>
              <span class="ml-percent">{{ item.percent }}%</span>
            </div>
            <div class="ml-bar">
              <div class="ml-bar-fill" :style="{ width: item.percent + '%', background: item.color }"></div>
            </div>
          </div>
        </div>
      </DataCard>
      <DataCard title="资产告警记录" :delay="0.25">
        <div class="alert-scroll-list">
          <div class="alert-item" v-for="(a, i) in assetAlerts" :key="i">
            <span class="alert-dot" :class="a.level"></span>
            <div class="alert-detail">
              <div class="alert-detail-name">{{ a.name }}</div>
              <div class="alert-detail-info">{{ a.asset }} · {{ a.time }}</div>
            </div>
          </div>
        </div>
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

// ==================== 模拟数据 ====================

// 实时告警列表
const alertList = ref([
  { name: 'A栋3层烟感报警', level: 'urgent', time: '14:32' },
  { name: 'B栋配电室温度过高', level: 'important', time: '14:28' },
  { name: '停车场入口闸机故障', level: 'normal', time: '14:15' },
  { name: 'C栋空调系统异常', level: 'important', time: '13:50' },
  { name: '园区东门门禁离线', level: 'normal', time: '13:42' },
  { name: 'D栋电梯维保提醒', level: 'info', time: '13:30' },
  { name: '消防水泵压力不足', level: 'urgent', time: '13:18' },
  { name: '地下车库CO浓度偏高', level: 'important', time: '12:55' }
])

// 设备在线状态
const deviceStatus = ref([
  { label: '在线', value: 1286, color: '#00e5ff' },
  { label: '离线', value: 47, color: '#ff4757' },
  { label: '维护', value: 23, color: '#ffa502' },
  { label: '告警', value: 15, color: '#ff6b81' }
])

// 快捷控制 - 设备列表
const deviceList = ref([
  { id: 'd1', name: '门禁-01', location: 'A栋1层大厅', status: 'online', statusText: '在线', statusClass: 'online' },
  { id: 'd2', name: '摄像头-03', location: 'B栋2层走廊', status: 'online', statusText: '在线', statusClass: 'online' },
  { id: 'd3', name: '温湿度传感器-05', location: 'C栋3层机房', status: 'alarm', statusText: '告警', statusClass: 'alarm' },
  { id: 'd4', name: '烟感-02', location: 'A栋地下车库', status: 'online', statusText: '在线', statusClass: 'online' },
  { id: 'd5', name: '空调控制器-08', location: 'B栋5层办公区', status: 'offline', statusText: '离线', statusClass: 'offline' },
  { id: 'd6', name: '照明控制器-12', location: 'D栋1层大厅', status: 'online', statusText: '在线', statusClass: 'online' },
  { id: 'd7', name: '能耗监测-01', location: '配电房', status: 'online', statusText: '在线', statusClass: 'online' },
  { id: 'd8', name: '水表-03', location: '消防泵房', status: 'alarm', statusText: '告警', statusClass: 'alarm' }
])

// 设备详情弹窗
const selectedDevice = ref(null)
const detailTab = ref('params')

const deviceParams = ref([
  { label: '设备编号', value: 'DEV-2024-0035' },
  { label: '设备类型', value: '温湿度传感器' },
  { label: '设备品牌', value: '海康威视' },
  { label: '设备型号', value: 'TH-Pro 200' },
  { label: '安装位置', value: 'C栋3层机房' },
  { label: '安装日期', value: '2024-03-15' },
  { label: '通信协议', value: 'MQTT' },
  { label: '信号强度', value: '-42 dBm' },
  { label: '固件版本', value: 'v2.1.3' },
  { label: '最近维护', value: '2025-11-20' }
])

const deviceControls = ref([
  { label: '设备开关', on: true },
  { label: '告警通知', on: true },
  { label: '自动上报', on: true },
  { label: '远程控制', on: false },
  { label: '数据记录', on: true }
])

function openDeviceDetail(item) {
  selectedDevice.value = item
  detailTab.value = 'params'
  // 更新参数中的位置信息
  deviceParams.value = deviceParams.value.map(p => {
    if (p.label === '安装位置') return { ...p, value: item.location }
    if (p.label === '设备名称') return { ...p, value: item.name }
    return p
  })
}

function handleControl(action) {
  const actionText = action === 'restart' ? '重启' : '恢复出厂'
  alert(`已发送${actionText}指令到 ${selectedDevice.value?.name}`)
}

// 未处理告警
const unhandledAlerts = ref([
  { name: 'A栋3层烟感持续报警', level: 'urgent', location: 'A栋3层走廊', time: '14:32' },
  { name: '消防水泵压力不足', level: 'urgent', location: 'B栋地下1层', time: '13:18' },
  { name: '配电室温度过高', level: 'important', location: 'B栋1层配电室', time: '14:28' },
  { name: '地下车库CO超标', level: 'important', location: '地下车库B区', time: '12:55' },
  { name: 'C栋空调主机故障', level: 'normal', location: 'C栋屋顶机房', time: '13:50' },
  { name: '东门闸机通信中断', level: 'normal', location: '园区东门', time: '13:42' }
])

// 能源设备
const energyDevices = ref([
  {
    label: '光伏发电',
    status: 'running',
    statusText: '运行中',
    value: '342.6 kWh',
    desc: '今日累计发电量，较昨日 +12.3%'
  },
  {
    label: '储能系统',
    status: 'running',
    statusText: '充电中',
    value: '78.5%',
    desc: '当前储能电量，预计18:00充满'
  },
  {
    label: '充电桩',
    status: 'warning',
    statusText: '部分离线',
    value: '18/24 台',
    desc: '在线充电桩数量，6台待检修'
  }
])

// 环境特殊监测
const envSpecial = ref([
  { label: 'PM2.5', value: '35 μg/m³', color: '#00e5ff', level: 'good', statusText: '优' },
  { label: 'PM10', value: '68 μg/m³', color: '#ffa502', level: 'moderate', statusText: '良' },
  { label: '噪声', value: '52 dB', color: '#00e5ff', level: 'good', statusText: '达标' },
  { label: '水质pH', value: '7.2', color: '#00e5ff', level: 'good', statusText: '正常' }
])

// 环境异常点位排行
const envRankList = ref([
  { name: 'B栋施工区', count: 23 },
  { name: '地下车库B区', count: 18 },
  { name: 'A栋厨房排烟口', count: 15 },
  { name: '园区东门道路', count: 12 },
  { name: 'C栋屋顶机房', count: 9 },
  { name: 'D栋垃圾回收站', count: 7 }
])

// 维保任务进度
const maintenanceList = ref([
  { name: '电梯年度维保', percent: 85, color: '#00e5ff' },
  { name: '消防系统检测', percent: 60, color: '#ffa502' },
  { name: '空调滤网更换', percent: 100, color: '#2ed573' },
  { name: '配电柜巡检', percent: 40, color: '#ff6b81' },
  { name: '给排水管道疏通', percent: 25, color: '#ff4757' }
])

// 资产告警
const assetAlerts = ref([
  { name: '电梯钢丝绳磨损超标', level: 'urgent', asset: 'A栋客梯#1', time: '14:20' },
  { name: '变压器油温异常', level: 'important', asset: 'B栋变压器T2', time: '13:55' },
  { name: '水泵轴承振动过大', level: 'important', asset: '消防泵房#3', time: '13:30' },
  { name: '空调压缩机效率下降', level: 'normal', asset: 'C栋空调主机', time: '12:45' },
  { name: '门禁读卡器灵敏度低', level: 'normal', asset: '东门闸机#2', time: '12:20' },
  { name: '摄像头镜头积灰', level: 'info', asset: '停车场入口CAM-05', time: '11:50' }
])

// ==================== 图表相关 ====================

const alertTypeBarRef = ref(null)
const alertLevelPieRef = ref(null)
const powerCurveRef = ref(null)
const energyRankRef = ref(null)
const envAlertRef = ref(null)
const assetTypePieRef = ref(null)

let charts = []

// 深海科技风通用配色
const deepSeaColors = ['#00e5ff', '#00b8d4', '#0091ea', '#006db3', '#004c8c', '#003c6e']

// 深海科技风通用文字/轴线样式
const axisLabelStyle = {
  color: 'rgba(255,255,255,0.65)',
  fontSize: 10
}
const axisLineStyle = {
  lineStyle: { color: 'rgba(255,255,255,0.08)' }
}
const splitLineStyle = {
  lineStyle: { color: 'rgba(255,255,255,0.06)' }
}

// 销毁所有图表
function disposeCharts() {
  charts.forEach(c => c.dispose())
  charts = []
}

// 初始化告警类型统计柱状图
function initAlertTypeBar() {
  if (!alertTypeBarRef.value) return
  const chart = echarts.init(alertTypeBarRef.value)
  charts.push(chart)
  chart.setOption({
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(0,20,40,0.9)',
      borderColor: 'rgba(0,229,255,0.3)',
      textStyle: { color: '#fff', fontSize: 12 }
    },
    grid: { top: 10, right: 10, bottom: 24, left: 50 },
    xAxis: {
      type: 'category',
      data: ['火灾', '入侵', '设备', '环境', '电力', '其他'],
      axisLabel: axisLabelStyle,
      axisLine: axisLineStyle,
      axisTick: { show: false }
    },
    yAxis: {
      type: 'value',
      axisLabel: axisLabelStyle,
      axisLine: { show: false },
      splitLine: splitLineStyle
    },
    series: [{
      type: 'bar',
      barWidth: 16,
      data: [12, 8, 25, 15, 18, 6],
      itemStyle: {
        borderRadius: [3, 3, 0, 0],
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#00e5ff' },
          { offset: 1, color: 'rgba(0,229,255,0.15)' }
        ])
      }
    }]
  })
}

// 初始化告警等级分布饼图
function initAlertLevelPie() {
  if (!alertLevelPieRef.value) return
  const chart = echarts.init(alertLevelPieRef.value)
  charts.push(chart)
  chart.setOption({
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(0,20,40,0.9)',
      borderColor: 'rgba(0,229,255,0.3)',
      textStyle: { color: '#fff', fontSize: 12 }
    },
    legend: {
      bottom: 0,
      textStyle: { color: 'rgba(255,255,255,0.65)', fontSize: 10 },
      itemWidth: 10,
      itemHeight: 10,
      itemGap: 12
    },
    series: [{
      type: 'pie',
      radius: ['40%', '65%'],
      center: ['50%', '42%'],
      label: { show: false },
      data: [
        { value: 8, name: '紧急', itemStyle: { color: '#ff4757' } },
        { value: 15, name: '重要', itemStyle: { color: '#ffa502' } },
        { value: 22, name: '一般', itemStyle: { color: '#ffd32a' } },
        { value: 35, name: '提示', itemStyle: { color: '#00e5ff' } }
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowColor: 'rgba(0,229,255,0.5)'
        }
      }
    }]
  })
}

// 初始化24小时用电趋势
function initPowerCurve() {
  if (!powerCurveRef.value) return
  const chart = echarts.init(powerCurveRef.value)
  charts.push(chart)
  const hours = Array.from({ length: 24 }, (_, i) => `${String(i).padStart(2, '0')}:00`)
  const data = [120, 95, 80, 75, 72, 78, 110, 185, 260, 310, 335, 320, 290, 305, 325, 340, 310, 280, 250, 220, 195, 170, 150, 130]
  chart.setOption({
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(0,20,40,0.9)',
      borderColor: 'rgba(0,229,255,0.3)',
      textStyle: { color: '#fff', fontSize: 12 }
    },
    grid: { top: 10, right: 10, bottom: 24, left: 45 },
    xAxis: {
      type: 'category',
      data: hours,
      boundaryGap: false,
      axisLabel: { ...axisLabelStyle, interval: 3 },
      axisLine: axisLineStyle,
      axisTick: { show: false }
    },
    yAxis: {
      type: 'value',
      axisLabel: axisLabelStyle,
      axisLine: { show: false },
      splitLine: splitLineStyle
    },
    series: [{
      type: 'line',
      smooth: true,
      symbol: 'none',
      lineStyle: { color: '#00e5ff', width: 2 },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(0,229,255,0.35)' },
          { offset: 1, color: 'rgba(0,229,255,0.02)' }
        ])
      },
      data
    }]
  })
}

// 初始化能源负荷排行横向柱状图
function initEnergyRank() {
  if (!energyRankRef.value) return
  const chart = echarts.init(energyRankRef.value)
  charts.push(chart)
  const names = ['A栋办公', 'B栋研发', 'C栋生产', '地下车库', '数据中心', '公共区域']
  const values = [420, 380, 350, 180, 310, 150]
  chart.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' },
      backgroundColor: 'rgba(0,20,40,0.9)',
      borderColor: 'rgba(0,229,255,0.3)',
      textStyle: { color: '#fff', fontSize: 12 }
    },
    grid: { top: 5, right: 40, bottom: 5, left: 80 },
    xAxis: {
      type: 'value',
      axisLabel: { ...axisLabelStyle, formatter: '{value}kW' },
      axisLine: { show: false },
      splitLine: splitLineStyle
    },
    yAxis: {
      type: 'category',
      data: names.reverse(),
      axisLabel: axisLabelStyle,
      axisLine: axisLineStyle,
      axisTick: { show: false }
    },
    series: [{
      type: 'bar',
      barWidth: 12,
      data: values.reverse(),
      itemStyle: {
        borderRadius: [0, 3, 3, 0],
        color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
          { offset: 0, color: 'rgba(0,229,255,0.15)' },
          { offset: 1, color: '#00e5ff' }
        ])
      }
    }]
  })
}

// 初始化环境告警统计
function initEnvAlert() {
  if (!envAlertRef.value) return
  const chart = echarts.init(envAlertRef.value)
  charts.push(chart)
  const days = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
  chart.setOption({
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(0,20,40,0.9)',
      borderColor: 'rgba(0,229,255,0.3)',
      textStyle: { color: '#fff', fontSize: 12 }
    },
    legend: {
      bottom: 0,
      textStyle: { color: 'rgba(255,255,255,0.65)', fontSize: 10 },
      itemWidth: 10,
      itemHeight: 10,
      itemGap: 12
    },
    grid: { top: 10, right: 10, bottom: 30, left: 35 },
    xAxis: {
      type: 'category',
      data: days,
      axisLabel: axisLabelStyle,
      axisLine: axisLineStyle,
      axisTick: { show: false }
    },
    yAxis: {
      type: 'value',
      axisLabel: axisLabelStyle,
      axisLine: { show: false },
      splitLine: splitLineStyle
    },
    series: [
      {
        name: '扬尘',
        type: 'line',
        smooth: true,
        symbol: 'circle',
        symbolSize: 5,
        lineStyle: { color: '#ffa502', width: 2 },
        itemStyle: { color: '#ffa502' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(255,165,2,0.25)' },
            { offset: 1, color: 'rgba(255,165,2,0.02)' }
          ])
        },
        data: [5, 8, 6, 12, 9, 3, 4]
      },
      {
        name: '噪声',
        type: 'line',
        smooth: true,
        symbol: 'circle',
        symbolSize: 5,
        lineStyle: { color: '#00e5ff', width: 2 },
        itemStyle: { color: '#00e5ff' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(0,229,255,0.25)' },
            { offset: 1, color: 'rgba(0,229,255,0.02)' }
          ])
        },
        data: [3, 4, 7, 5, 6, 2, 3]
      },
      {
        name: '水质',
        type: 'line',
        smooth: true,
        symbol: 'circle',
        symbolSize: 5,
        lineStyle: { color: '#2ed573', width: 2 },
        itemStyle: { color: '#2ed573' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(46,213,115,0.25)' },
            { offset: 1, color: 'rgba(46,213,115,0.02)' }
          ])
        },
        data: [1, 2, 1, 3, 2, 1, 1]
      }
    ]
  })
}

// 初始化设备类型占比饼图
function initAssetTypePie() {
  if (!assetTypePieRef.value) return
  const chart = echarts.init(assetTypePieRef.value)
  charts.push(chart)
  chart.setOption({
    tooltip: {
      trigger: 'item',
      backgroundColor: 'rgba(0,20,40,0.9)',
      borderColor: 'rgba(0,229,255,0.3)',
      textStyle: { color: '#fff', fontSize: 12 }
    },
    legend: {
      bottom: 0,
      textStyle: { color: 'rgba(255,255,255,0.65)', fontSize: 10 },
      itemWidth: 10,
      itemHeight: 10,
      itemGap: 8
    },
    series: [{
      type: 'pie',
      radius: ['40%', '65%'],
      center: ['50%', '42%'],
      label: { show: false },
      data: [
        { value: 420, name: '安防设备', itemStyle: { color: '#00e5ff' } },
        { value: 310, name: '暖通设备', itemStyle: { color: '#0091ea' } },
        { value: 260, name: '电力设备', itemStyle: { color: '#ffa502' } },
        { value: 180, name: '给排水', itemStyle: { color: '#2ed573' } },
        { value: 120, name: '电梯设备', itemStyle: { color: '#ff6b81' } },
        { value: 81, name: '其他', itemStyle: { color: '#006db3' } }
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowColor: 'rgba(0,229,255,0.5)'
        }
      }
    }]
  })
}

// 根据tab初始化对应图表
function initChartsByTab(tab) {
  disposeCharts()
  nextTick(() => {
    switch (tab) {
      case 'safety':
        initAlertTypeBar()
        initAlertLevelPie()
        break
      case 'energy':
        initPowerCurve()
        initEnergyRank()
        break
      case 'environment':
        initEnvAlert()
        break
      case 'asset':
        initAssetTypePie()
        break
    }
  })
}

// 监听tab切换
watch(() => props.activeTab, (newTab) => {
  initChartsByTab(newTab)
})

onMounted(() => {
  initChartsByTab(props.activeTab)
})

onBeforeUnmount(() => {
  disposeCharts()
})
</script>

<style lang="scss" scoped>
@import '../styles/cockpit.scss';

.panel-right {
  display: flex;
  flex-direction: column;
  gap: 12px;
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
}

// 告警滚动列表
.alert-scroll-list {
  max-height: 200px;
  overflow-y: auto;
}

.alert-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);

  &:last-child {
    border-bottom: none;
  }
}

.alert-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  flex-shrink: 0;

  &.urgent {
    background: #ff4757;
    box-shadow: 0 0 6px rgba(255, 71, 87, 0.6);
  }
  &.important {
    background: #ffa502;
    box-shadow: 0 0 6px rgba(255, 165, 2, 0.6);
  }
  &.normal {
    background: #ffd32a;
    box-shadow: 0 0 6px rgba(255, 211, 42, 0.6);
  }
  &.info {
    background: #00e5ff;
    box-shadow: 0 0 6px rgba(0, 229, 255, 0.6);
  }
}

.alert-name {
  flex: 1;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.85);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.alert-time {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.4);
  flex-shrink: 0;
}

.alert-detail {
  flex: 1;
  min-width: 0;
}

.alert-detail-name {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.85);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.alert-detail-info {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.4);
  margin-top: 2px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

// 设备在线状态
.device-status-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  justify-items: center;
}

.device-status-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.device-status-ring {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  border: 3px solid var(--ring-color);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 0 12px color-mix(in srgb, var(--ring-color) 30%, transparent);
}

.device-status-num {
  font-size: 16px;
  font-weight: 700;
  color: #fff;
}

.device-status-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
}

// 快捷控制 - 设备列表表格
.device-table-wrapper {
  overflow-x: auto;
}

.device-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 12px;

  thead th {
    padding: 8px 10px;
    text-align: left;
    color: var(--accent);
    font-weight: 600;
    border-bottom: 1px solid rgba(0, 229, 255, 0.2);
    white-space: nowrap;
    background: rgba(0, 229, 255, 0.05);
  }

  tbody td {
    padding: 7px 10px;
    color: rgba(255, 255, 255, 0.8);
    border-bottom: 1px solid rgba(255, 255, 255, 0.04);
    white-space: nowrap;
  }

  tbody tr:hover {
    background: rgba(0, 229, 255, 0.05);
  }
}

.dt-name {
  font-weight: 500;
  color: rgba(255, 255, 255, 0.9);
}

.dt-location {
  color: rgba(255, 255, 255, 0.5);
}

.dt-status {
  display: flex;
  align-items: center;
  gap: 6px;
}

.status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  flex-shrink: 0;

  &.online {
    background: #00e5ff;
    box-shadow: 0 0 4px rgba(0, 229, 255, 0.5);
  }
  &.offline {
    background: #ff4757;
    box-shadow: 0 0 4px rgba(255, 71, 87, 0.5);
  }
  &.alarm {
    background: #ffa502;
    box-shadow: 0 0 4px rgba(255, 165, 2, 0.5);
    animation: blink 1.5s infinite;
  }
}

@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.3; }
}

.dt-action {
  .action-btn {
    color: var(--accent);
    cursor: pointer;
    padding: 2px 8px;
    border: 1px solid rgba(0, 229, 255, 0.3);
    border-radius: 4px;
    font-size: 11px;
    transition: all 0.3s ease;

    &:hover {
      background: rgba(0, 229, 255, 0.1);
      border-color: rgba(0, 229, 255, 0.5);
    }
  }
}

// 设备详情弹窗
.detail-overlay {
  position: absolute;
  inset: 0;
  z-index: 200;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.5);
  animation: fadeIn 0.2s ease;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.detail-popup {
  width: 400px;
  max-height: 80%;
  background: rgba(2, 30, 77, 0.95);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border: 1px solid rgba(0, 229, 255, 0.25);
  border-radius: 12px;
  box-shadow: 0 8px 40px rgba(0, 0, 0, 0.6);
  animation: popupIn 0.3s ease;
  overflow: hidden;
}

@keyframes popupIn {
  from { opacity: 0; transform: translateY(20px) scale(0.95); }
  to { opacity: 1; transform: translateY(0) scale(1); }
}

.detail-header {
  padding: 12px 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: linear-gradient(180deg, rgba(0, 229, 255, 0.1) 0%, transparent 100%);
}

.detail-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--accent);
}

.detail-close {
  cursor: pointer;
  color: rgba(255, 255, 255, 0.5);
  font-size: 14px;
  padding: 2px 6px;
  border-radius: 4px;
  transition: all 0.2s ease;

  &:hover {
    color: #ff4757;
    background: rgba(255, 71, 87, 0.1);
  }
}

.detail-body {
  padding: 14px 16px;
  overflow-y: auto;
  max-height: 60vh;
}

.detail-tabs {
  display: flex;
  gap: 0;
  margin-bottom: 14px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.detail-tab {
  padding: 8px 16px;
  cursor: pointer;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
  border-bottom: 2px solid transparent;
  transition: all 0.3s ease;
  user-select: nowrap;

  &:hover:not(.active) {
    color: rgba(255, 255, 255, 0.8);
  }

  &.active {
    color: var(--accent);
    border-bottom-color: var(--accent);
  }
}

.params-list {
  display: flex;
  flex-direction: column;
}

.param-row {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.04);
  font-size: 12px;

  &:last-child {
    border-bottom: none;
  }
}

.param-label {
  color: rgba(255, 255, 255, 0.5);
}

.param-value {
  color: rgba(255, 255, 255, 0.9);
  font-weight: 500;
}

.control-panel {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.ctrl-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.04);
}

.ctrl-label {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.8);
}

.ctrl-switch {
  width: 40px;
  height: 22px;
  border-radius: 11px;
  background: rgba(255, 255, 255, 0.15);
  cursor: pointer;
  position: relative;
  transition: background 0.3s ease;

  &.on {
    background: var(--accent);
  }
}

.ctrl-switch-thumb {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: #fff;
  position: absolute;
  top: 2px;
  left: 2px;
  transition: transform 0.3s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);

  .ctrl-switch.on & {
    transform: translateX(18px);
  }
}

.ctrl-actions {
  display: flex;
  gap: 10px;
  margin-top: 8px;
}

.ctrl-btn {
  flex: 1;
  padding: 8px 0;
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 6px;
  background: transparent;
  color: rgba(255, 255, 255, 0.7);
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    border-color: rgba(0, 229, 255, 0.4);
    background: rgba(0, 229, 255, 0.05);
  }

  &.primary {
    border-color: rgba(0, 229, 255, 0.4);
    color: var(--accent);

    &:hover {
      background: rgba(0, 229, 255, 0.15);
      border-color: var(--accent);
    }
  }

  &.warning {
    border-color: rgba(255, 165, 2, 0.4);
    color: #ffa502;

    &:hover {
      background: rgba(255, 165, 2, 0.15);
      border-color: #ffa502;
    }
  }
}

// 能源设备
.energy-device-grid {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.energy-device-item {
  padding: 10px 12px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 6px;
  border: 1px solid rgba(255, 255, 255, 0.06);
}

.ed-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 6px;
}

.ed-name {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.85);
  font-weight: 500;
}

.ed-status {
  font-size: 11px;
  padding: 1px 8px;
  border-radius: 10px;

  &.running {
    color: #2ed573;
    background: rgba(46, 213, 115, 0.12);
  }
  &.warning {
    color: #ffa502;
    background: rgba(255, 165, 2, 0.12);
  }
  &.offline {
    color: #ff4757;
    background: rgba(255, 71, 87, 0.12);
  }
}

.ed-value {
  font-size: 20px;
  font-weight: 700;
  color: #00e5ff;
  margin-bottom: 4px;
}

.ed-desc {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.4);
}

// 环境特殊监测
.env-special-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.env-special-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 12px 8px;
  background: rgba(255, 255, 255, 0.03);
  border-radius: 6px;
  border: 1px solid rgba(255, 255, 255, 0.06);
}

.es-label {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
}

.es-value {
  font-size: 18px;
  font-weight: 700;
}

.es-status {
  font-size: 11px;
  padding: 1px 8px;
  border-radius: 10px;

  &.good {
    color: #2ed573;
    background: rgba(46, 213, 115, 0.12);
  }
  &.moderate {
    color: #ffa502;
    background: rgba(255, 165, 2, 0.12);
  }
  &.bad {
    color: #ff4757;
    background: rgba(255, 71, 87, 0.12);
  }
}

// 排行列表
.rank-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.rank-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 0;
}

.rank-index {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  color: rgba(255, 255, 255, 0.6);
  flex-shrink: 0;

  &.top3 {
    background: var(--accent);
    color: #001a2e;
    font-weight: 700;
  }
}

.rank-name {
  flex: 1;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.8);
}

.rank-value {
  font-size: 12px;
  color: #00e5ff;
  font-weight: 500;
  flex-shrink: 0;
}

// 维保任务
.maintenance-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.maintenance-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.ml-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.ml-name {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.8);
}

.ml-percent {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.6);
  font-weight: 500;
}

.ml-bar {
  height: 6px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 3px;
  overflow: hidden;
}

.ml-bar-fill {
  height: 100%;
  border-radius: 3px;
  transition: width 1s ease;
}

// 图表容器
.chart-container {
  width: 100%;
  height: 150px;
}
</style>
