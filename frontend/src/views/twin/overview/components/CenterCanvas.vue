<template>
  <div class="panel-center">
    <!-- ====== 园区总览视图 ====== -->
    <div v-if="viewMode === 'campus'" class="campus-bg">
      <!-- 配置的底图（优先显示） -->
      <img v-if="campusBaseMap" :src="campusBaseMap" class="campus-base-map" alt="园区底图" />

      <!-- 科技网格底图（默认/后备） -->
      <div v-if="!campusBaseMap" class="grid-overlay"></div>
      <!-- 园区边界（默认/后备） -->
      <div v-if="!campusBaseMap" class="campus-boundary">
        <svg viewBox="0 0 800 500" class="campus-svg">
          <g class="roads" stroke="rgba(0,229,255,0.15)" stroke-width="2" fill="none">
            <path d="M50,250 H750" />
            <path d="M400,30 V470" />
            <path d="M150,100 H650" />
            <path d="M250,400 H550" />
            <path d="M100,150 L300,350" />
            <path d="M500,100 L700,400" />
          </g>
          <g class="greenery" fill="rgba(39,201,122,0.08)" stroke="rgba(39,201,122,0.2)" stroke-width="1">
            <rect x="60" y="60" width="120" height="80" rx="8" />
            <rect x="600" y="60" width="140" height="100" rx="8" />
            <rect x="300" y="350" width="200" height="80" rx="8" />
          </g>
        </svg>
      </div>

      <!-- 楼栋点位 -->
      <div
        v-for="building in buildings"
        :key="building.id"
        class="poi-building"
        :class="{ alert: building.alert }"
        :style="{ left: building.x + '%', top: building.y + '%' }"
        @click="enterBuilding(building)"
      >
        <div class="poi-icon">
          <svg viewBox="0 0 24 24" width="28" height="28">
            <path
              d="M12 2L2 12h3v8h6v-6h2v6h6v-8h3L12 2z"
              :fill="building.alert ? '#FF3333' : '#00E5FF'"
              :stroke="building.alert ? '#FF3333' : '#00E5FF'"
              stroke-width="0.5"
            />
          </svg>
        </div>
        <div class="poi-label">{{ building.name }}</div>
        <div class="poi-pulse" v-if="building.alert"></div>
      </div>

      <!-- 设备点位（园区级别） -->
      <div
        v-for="device in campusDevices"
        :key="device.id"
        class="poi-device"
        :class="{ alert: device.alert, online: !device.alert }"
        :style="{ left: device.x + '%', top: device.y + '%' }"
        @click="handleDeviceClick(device)"
      >
        <div class="device-dot"></div>
      </div>
    </div>

    <!-- ====== 楼层视图 ====== -->
    <div v-if="viewMode === 'floor'" class="floor-view">
      <!-- 楼层平面背景 -->
      <div class="floor-bg">
        <div class="grid-overlay"></div>
        <!-- 楼层平面轮廓 -->
        <div class="floor-outline">
          <svg viewBox="0 0 600 400" class="floor-svg">
            <!-- 房间分隔 -->
            <g stroke="rgba(0,229,255,0.2)" stroke-width="1.5" fill="none">
              <rect x="20" y="20" width="260" height="170" rx="4" />
              <rect x="300" y="20" width="280" height="170" rx="4" />
              <rect x="20" y="210" width="180" height="170" rx="4" />
              <rect x="220" y="210" width="180" height="170" rx="4" />
              <rect x="420" y="210" width="160" height="170" rx="4" />
              <!-- 走廊 -->
              <rect x="200" y="190" width="200" height="20" rx="2" stroke-dasharray="4,4" />
            </g>
            <!-- 房间标签 -->
            <g fill="rgba(232,240,254,0.3)" font-size="12" text-anchor="middle">
              <text x="150" y="110">办公区A</text>
              <text x="440" y="110">办公区B</text>
              <text x="110" y="300">会议室</text>
              <text x="310" y="300">机房</text>
              <text x="500" y="300">休息区</text>
            </g>
          </svg>
        </div>
      </div>

      <!-- 楼层设备图标 -->
      <div
        v-for="device in floorDevices"
        :key="device.id"
        class="floor-device"
        :class="{ alert: device.alert }"
        :style="{ left: device.x + '%', top: device.y + '%' }"
        @click="handleDeviceClick(device)"
      >
        <div class="floor-device-icon" :style="{ background: device.iconBg }">
          <span class="floor-device-emoji">{{ device.icon }}</span>
        </div>
        <div class="floor-device-label">{{ device.name }}</div>
        <div class="floor-device-pulse" v-if="device.alert"></div>
      </div>

      <!-- 楼层信息栏 -->
      <div class="floor-info-bar">
        <div class="floor-info-back" @click="backToCampus">
          <span>← 返回园区</span>
        </div>
        <div class="floor-info-title">{{ currentBuilding.name }} - {{ currentFloor }}F</div>
        <div class="floor-tabs">
          <div
            v-for="f in currentBuilding.floors"
            :key="f"
            class="floor-tab"
            :class="{ active: currentFloor === f }"
            @click="switchFloor(f)"
          >{{ f }}F</div>
        </div>
      </div>
    </div>

    <!-- ====== 设备详情弹窗 ====== -->
    <div v-if="selectedDevice" class="popup-overlay" @click.self="selectedDevice = null">
      <div class="glass-popup device-popup">
        <div class="popup-header">
          <span class="popup-title">{{ selectedDevice.name }}</span>
          <span class="popup-close" @click="selectedDevice = null">&#10005;</span>
        </div>
        <div class="popup-body">
          <div class="device-tabs">
            <div class="dt-item" :class="{ active: deviceTab === 'info' }" @click="deviceTab = 'info'">基础信息</div>
            <div class="dt-item" :class="{ active: deviceTab === 'realtime' }" @click="deviceTab = 'realtime'">实时数据</div>
            <div class="dt-item" :class="{ active: deviceTab === 'history' }" @click="deviceTab = 'history'">历史趋势</div>
            <div class="dt-item" :class="{ active: deviceTab === 'alerts' }" @click="deviceTab = 'alerts'">告警记录</div>
          </div>
          <div v-if="deviceTab === 'info'" class="device-info">
            <div class="di-row" v-for="item in deviceInfoData" :key="item.label">
              <span class="di-label">{{ item.label }}</span>
              <span class="di-value">{{ item.value }}</span>
            </div>
          </div>
          <div v-if="deviceTab === 'realtime'" class="device-realtime">
            <div class="dr-item" v-for="item in realtimeData" :key="item.label">
              <span class="dr-label">{{ item.label }}</span>
              <span class="dr-value" :style="{ color: item.color }">{{ item.value }}</span>
            </div>
          </div>
          <div v-if="deviceTab === 'history'" class="device-history">
            <div ref="deviceHistoryRef" style="width:100%;height:150px;"></div>
          </div>
          <div v-if="deviceTab === 'alerts'" class="device-alerts">
            <div class="da-item" v-for="(a, i) in deviceAlertData" :key="i">
              <span class="da-dot" :class="a.level"></span>
              <span class="da-text">{{ a.text }}</span>
              <span class="da-time">{{ a.time }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, nextTick, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import { getBaseMapList } from '../../config/api/config'

// ---- 视图模式：campus 园区 / floor 楼层 ----
const viewMode = ref('campus')

// ---- 园区底图（从视觉配置系统加载） ----
const campusBaseMap = ref(null) // Will hold the campus base map URL

async function loadCampusBaseMap() {
  try {
    const res = await getBaseMapList({ mapType: 'campus', status: 1, pageNum: 1, pageSize: 1 })
    const list = res.records || res.rows || res.data || []
    if (list.length > 0) {
      campusBaseMap.value = list[0].mapUrl || list[0].fileUrl || null
    }
  } catch (e) {
    // Silently fail, will use SVG fallback
  }
}

// ---- 楼栋数据 ----
const buildings = ref([
  { id: 'b1', name: 'A栋-研发中心', x: 18, y: 22, area: '28,000 m²', floors: [1,2,3,4,5,6,7,8], companies: 12, deviceCount: 186, alert: false },
  { id: 'b2', name: 'B栋-创新工场', x: 42, y: 18, area: '35,000 m²', floors: [1,2,3,4,5,6,7,8,9,10], companies: 18, deviceCount: 245, alert: true },
  { id: 'b3', name: 'C栋-数据中心', x: 68, y: 25, area: '22,000 m²', floors: [1,2,3,4,5], companies: 6, deviceCount: 320, alert: false },
  { id: 'b4', name: 'D栋-综合办公', x: 85, y: 35, area: '18,500 m²', floors: [1,2,3,4,5,6], companies: 15, deviceCount: 142, alert: false },
  { id: 'b5', name: 'E栋-会议中心', x: 25, y: 55, area: '12,000 m²', floors: [1,2,3], companies: 3, deviceCount: 98, alert: true },
  { id: 'b6', name: 'F栋-孵化器', x: 50, y: 50, area: '20,000 m²', floors: [1,2,3,4,5,6,7], companies: 22, deviceCount: 178, alert: false },
  { id: 'b7', name: 'G栋-展示中心', x: 75, y: 60, area: '8,500 m²', floors: [1,2], companies: 2, deviceCount: 64, alert: false },
  { id: 'b8', name: 'H栋-配套商业', x: 38, y: 78, area: '15,000 m²', floors: [1,2,3,4], companies: 28, deviceCount: 112, alert: false }
])

// ---- 园区级设备点 ----
const campusDevices = ref([
  { id: 'd1', name: '门禁-01', x: 12, y: 15, type: 'access', alert: false },
  { id: 'd2', name: '摄像头-03', x: 30, y: 10, type: 'camera', alert: false },
  { id: 'd3', name: '温湿度传感器-05', x: 55, y: 12, type: 'sensor', alert: true },
  { id: 'd4', name: '烟感-02', x: 78, y: 18, type: 'smoke', alert: false },
  { id: 'd5', name: '门禁-02', x: 90, y: 28, type: 'access', alert: false },
  { id: 'd6', name: '摄像头-07', x: 15, y: 42, type: 'camera', alert: false },
  { id: 'd7', name: '能耗监测-01', x: 35, y: 38, type: 'energy', alert: true },
  { id: 'd8', name: '水表-03', x: 60, y: 42, type: 'water', alert: false },
  { id: 'd9', name: '摄像头-12', x: 82, y: 48, type: 'camera', alert: false },
  { id: 'd10', name: '门禁-05', x: 20, y: 68, type: 'access', alert: false },
  { id: 'd11', name: '烟感-08', x: 45, y: 62, type: 'smoke', alert: false },
  { id: 'd12', name: '温湿度传感器-11', x: 65, y: 72, type: 'sensor', alert: false },
  { id: 'd13', name: '能耗监测-04', x: 85, y: 75, type: 'energy', alert: false },
  { id: 'd14', name: '摄像头-15', x: 30, y: 88, type: 'camera', alert: false },
  { id: 'd15', name: '水表-06', x: 58, y: 85, type: 'water', alert: true }
])

// ---- 楼层视图状态 ----
const currentBuilding = ref(null)
const currentFloor = ref(null)

// ---- 楼层设备数据生成 ----
const floorDevices = ref([])

const deviceTemplates = [
  { name: '门禁', icon: '🚪', iconBg: 'rgba(0,229,255,0.15)', type: 'access' },
  { name: '摄像头', icon: '📹', iconBg: 'rgba(124,92,252,0.15)', type: 'camera' },
  { name: '烟感', icon: '🔥', iconBg: 'rgba(255,51,51,0.15)', type: 'smoke' },
  { name: '温湿度', icon: '🌡️', iconBg: 'rgba(39,201,122,0.15)', type: 'sensor' },
  { name: '空调', icon: '❄️', iconBg: 'rgba(0,229,255,0.15)', type: 'hvac' },
  { name: '照明', icon: '💡', iconBg: 'rgba(255,149,0,0.15)', type: 'light' },
  { name: '能耗表', icon: '⚡', iconBg: 'rgba(255,149,0,0.15)', type: 'energy' },
  { name: '水表', icon: '💧', iconBg: 'rgba(0,229,255,0.15)', type: 'water' }
]

function generateFloorDevices(building, floor) {
  const count = 8 + Math.floor(Math.random() * 6)
  const devices = []
  for (let i = 0; i < count; i++) {
    const tpl = deviceTemplates[i % deviceTemplates.length]
    devices.push({
      id: `${building.id}-${floor}-${i}`,
      name: `${tpl.name}-${String(floor).padStart(2,'0')}${String(i+1).padStart(2,'0')}`,
      icon: tpl.icon,
      iconBg: tpl.iconBg,
      type: tpl.type,
      x: 10 + Math.random() * 80,
      y: 10 + Math.random() * 75,
      alert: Math.random() > 0.75,
      building: building.name,
      floor: floor
    })
  }
  return devices
}

// ---- 进入楼栋楼层视图 ----
function enterBuilding(building) {
  currentBuilding.value = building
  currentFloor.value = building.floors[0]
  floorDevices.value = generateFloorDevices(building, currentFloor.value)
  viewMode.value = 'floor'
}

function switchFloor(f) {
  currentFloor.value = f
  floorDevices.value = generateFloorDevices(currentBuilding.value, f)
}

function backToCampus() {
  viewMode.value = 'campus'
  currentBuilding.value = null
  currentFloor.value = null
  floorDevices.value = []
}

// ---- 设备弹窗 ----
const selectedDevice = ref(null)
const deviceTab = ref('info')
const deviceHistoryRef = ref(null)
let historyChart = null

function handleDeviceClick(device) {
  selectedDevice.value = device
  deviceTab.value = 'info'
}

const deviceInfoData = ref([
  { label: '设备编号', value: 'DEV-2024-0035' },
  { label: '设备类型', value: '温湿度传感器' },
  { label: '安装位置', value: 'B栋 3F 机房' },
  { label: '安装日期', value: '2024-03-15' },
  { label: '设备品牌', value: '海康威视' },
  { label: '设备型号', value: 'TH-Pro 200' },
  { label: '通信协议', value: 'MQTT' },
  { label: '最近维护', value: '2025-11-20' }
])

const realtimeData = ref([
  { label: '当前温度', value: '26.3°C', color: '#00E5FF' },
  { label: '当前湿度', value: '58.2%', color: '#27C97A' },
  { label: 'PM2.5', value: '35 μg/m³', color: '#FF9500' },
  { label: 'CO₂浓度', value: '420 ppm', color: '#00E5FF' },
  { label: '设备电压', value: '3.28V', color: '#27C97A' },
  { label: '信号强度', value: '-42 dBm', color: '#00E5FF' },
  { label: '在线时长', value: '72h 15min', color: '#27C97A' },
  { label: '数据上报频率', value: '5s/次', color: '#00E5FF' }
])

const deviceAlertData = ref([
  { level: 'danger', text: '温度超过阈值 30°C，当前 32.1°C', time: '04-23 09:15' },
  { level: 'warning', text: '湿度偏高，当前 72.5%', time: '04-22 14:30' },
  { level: 'danger', text: '设备离线超过 10 分钟', time: '04-21 08:45' },
  { level: 'warning', text: '信号强度低于 -60 dBm', time: '04-20 16:20' },
  { level: 'success', text: '设备恢复正常在线', time: '04-20 16:35' },
  { level: 'success', text: '固件升级完成 v2.1.3', time: '04-19 02:00' }
])

watch(deviceTab, (val) => {
  if (val === 'history') {
    nextTick(() => initHistoryChart())
  }
})

function initHistoryChart() {
  if (!deviceHistoryRef.value) return
  if (historyChart) { historyChart.dispose(); historyChart = null }
  historyChart = echarts.init(deviceHistoryRef.value)
  const hours = Array.from({ length: 24 }, (_, i) => `${String(i).padStart(2,'0')}:00`)
  historyChart.setOption({
    backgroundColor: 'transparent',
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(2,30,77,0.9)', borderColor: 'rgba(0,229,255,0.3)', textStyle: { color: '#E8F0FE', fontSize: 11 } },
    legend: { top: 0, right: 0, textStyle: { color: 'rgba(232,240,254,0.6)', fontSize: 10 }, itemWidth: 14, itemHeight: 2 },
    grid: { top: 28, left: 36, right: 12, bottom: 24 },
    xAxis: { type: 'category', data: hours, axisLine: { lineStyle: { color: 'rgba(0,229,255,0.15)' } }, axisTick: { show: false }, axisLabel: { color: 'rgba(232,240,254,0.4)', fontSize: 9, interval: 3 } },
    yAxis: [
      { type: 'value', name: '°C', nameTextStyle: { color: 'rgba(232,240,254,0.4)', fontSize: 9 }, axisLine: { show: false }, axisTick: { show: false }, splitLine: { lineStyle: { color: 'rgba(0,229,255,0.06)' } }, axisLabel: { color: 'rgba(232,240,254,0.4)', fontSize: 9 } },
      { type: 'value', name: '%', nameTextStyle: { color: 'rgba(232,240,254,0.4)', fontSize: 9 }, axisLine: { show: false }, axisTick: { show: false }, splitLine: { show: false }, axisLabel: { color: 'rgba(232,240,254,0.4)', fontSize: 9 } }
    ],
    series: [
      { name: '温度', type: 'line', data: [22.1,21.8,21.5,21.2,21.0,21.3,22.0,23.5,24.8,25.6,26.3,27.1,27.8,28.2,28.5,28.1,27.5,26.8,25.9,25.2,24.5,23.8,23.1,22.5], smooth: true, symbol: 'none', lineStyle: { color: '#00E5FF', width: 1.5 }, areaStyle: { color: new echarts.graphic.LinearGradient(0,0,0,1,[{offset:0,color:'rgba(0,229,255,0.25)'},{offset:1,color:'rgba(0,229,255,0)'}]) } },
      { name: '湿度', type: 'line', yAxisIndex: 1, data: [62,63,64,65,66,65,63,60,57,55,53,51,50,49,48,50,52,54,56,58,59,60,61,62], smooth: true, symbol: 'none', lineStyle: { color: '#27C97A', width: 1.5 }, areaStyle: { color: new echarts.graphic.LinearGradient(0,0,0,1,[{offset:0,color:'rgba(39,201,122,0.2)'},{offset:1,color:'rgba(39,201,122,0)'}]) } }
    ]
  })
}

onMounted(() => {
  loadCampusBaseMap()
})

onBeforeUnmount(() => {
  if (historyChart) { historyChart.dispose(); historyChart = null }
})
</script>

<style scoped lang="scss">
@import '../styles/cockpit.scss';

.panel-center {
  position: relative;
  overflow: hidden;
  border-radius: var(--card-radius);
  border: 1px solid var(--border-glow);
  background: var(--bg-card);
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
}

// ============ 园区视图 ============
.campus-bg {
  position: absolute;
  inset: 0;
  background: radial-gradient(ellipse at 50% 40%, rgba(0,229,255,0.04) 0%, transparent 70%);
}

.campus-base-map {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;  // 等比居中裁剪，保留原图比例，不挤压变形
  object-position: center center;
  z-index: 0;
}

.grid-overlay {
  position: absolute;
  inset: 0;
  background:
    repeating-linear-gradient(0deg, transparent, transparent 39px, rgba(0,229,255,0.035) 39px, rgba(0,229,255,0.035) 40px),
    repeating-linear-gradient(90deg, transparent, transparent 39px, rgba(0,229,255,0.035) 39px, rgba(0,229,255,0.035) 40px);
  background-size: 40px 40px;
  pointer-events: none;
}

.campus-boundary {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 80%;
  height: 85%;
  pointer-events: none;
}
.campus-svg, .floor-svg { width: 100%; height: 100%; }

// ---- 楼栋点位 ----
.poi-building {
  position: absolute;
  transform: translate(-50%, -50%);
  cursor: pointer;
  text-align: center;
  z-index: 10;
  transition: transform 0.3s ease;
  &:hover { transform: translate(-50%, -50%) scale(1.15); .poi-label { color: var(--accent); } }
  &.alert .poi-icon svg path { filter: drop-shadow(0 0 6px rgba(255,51,51,0.6)); }
}
.poi-icon {
  display: flex; align-items: center; justify-content: center;
  width: 36px; height: 36px; margin: 0 auto;
  border-radius: 8px; background: rgba(0,229,255,0.08);
  border: 1px solid rgba(0,229,255,0.2); transition: all 0.3s ease;
  .alert & { background: rgba(255,51,51,0.1); border-color: rgba(255,51,51,0.3); }
  .poi-building:hover & { background: rgba(0,229,255,0.15); border-color: rgba(0,229,255,0.4); box-shadow: 0 0 12px var(--accent-glow); }
}
.poi-label { font-size: 11px; color: var(--text-secondary); margin-top: 2px; white-space: nowrap; transition: color 0.3s ease; text-shadow: 0 1px 4px rgba(0,0,0,0.8); }
.poi-pulse { position: absolute; top: 50%; left: 50%; transform: translate(-50%,-50%); width: 30px; height: 30px; border-radius: 50%; border: 2px solid var(--danger); animation: alertPulse 2s infinite; pointer-events: none; }

// ---- 园区设备点 ----
.poi-device { position: absolute; transform: translate(-50%,-50%); cursor: pointer; z-index: 5; }
.device-dot { width: 8px; height: 8px; border-radius: 50%; background: var(--accent); box-shadow: 0 0 6px var(--accent-glow); transition: all 0.3s ease;
  .alert & { background: var(--danger); box-shadow: 0 0 6px rgba(255,51,51,0.5); animation: alertPulse 1.5s infinite; }
  .online & { animation: pulse 3s infinite; }
  .poi-device:hover & { transform: scale(1.5); box-shadow: 0 0 12px var(--accent-glow); }
}

// ============ 楼层视图 ============
.floor-view {
  position: absolute;
  inset: 0;
  animation: fadeIn 0.3s ease;
}
.floor-bg {
  position: absolute;
  inset: 0;
  background: radial-gradient(ellipse at 50% 50%, rgba(0,229,255,0.03) 0%, transparent 70%);
}
.floor-outline {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 75%;
  height: 80%;
  pointer-events: none;
}

// ---- 楼层设备 ----
.floor-device {
  position: absolute;
  transform: translate(-50%, -50%);
  cursor: pointer;
  text-align: center;
  z-index: 10;
  transition: transform 0.3s ease;
  &:hover { transform: translate(-50%, -50%) scale(1.2); .floor-device-label { color: var(--accent); } }
  &.alert .floor-device-icon { box-shadow: 0 0 10px rgba(255,51,51,0.5); border-color: rgba(255,51,51,0.5); }
}
.floor-device-icon {
  width: 36px; height: 36px; margin: 0 auto;
  border-radius: 8px; border: 1px solid rgba(0,229,255,0.2);
  display: flex; align-items: center; justify-content: center;
  font-size: 18px; transition: all 0.3s ease;
  .floor-device:hover & { border-color: rgba(0,229,255,0.5); box-shadow: 0 0 12px var(--accent-glow); }
}
.floor-device-label { font-size: 10px; color: var(--text-secondary); margin-top: 2px; white-space: nowrap; transition: color 0.3s ease; text-shadow: 0 1px 4px rgba(0,0,0,0.8); }
.floor-device-pulse { position: absolute; top: 50%; left: 50%; transform: translate(-50%,-50%); width: 30px; height: 30px; border-radius: 50%; border: 2px solid var(--danger); animation: alertPulse 2s infinite; pointer-events: none; }

// ---- 楼层信息栏 ----
.floor-info-bar {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  z-index: 50;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 16px;
  background: linear-gradient(180deg, rgba(2,30,77,0.95) 0%, rgba(2,30,77,0.6) 80%, transparent 100%);
  pointer-events: none;
  & > * { pointer-events: auto; }
}
.floor-info-back {
  font-size: 12px;
  color: var(--accent);
  cursor: pointer;
  padding: 4px 10px;
  border: 1px solid rgba(0,229,255,0.3);
  border-radius: 6px;
  transition: all 0.3s ease;
  white-space: nowrap;
  &:hover { background: rgba(0,229,255,0.1); border-color: rgba(0,229,255,0.5); }
}
.floor-info-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
  white-space: nowrap;
}
.floor-tabs {
  display: flex;
  gap: 4px;
  margin-left: auto;
}
.floor-tab {
  padding: 3px 10px;
  border: 1px solid rgba(255,255,255,0.1);
  border-radius: 4px;
  cursor: pointer;
  font-size: 11px;
  color: var(--text-secondary);
  transition: all 0.3s ease;
  user-select: none;
  white-space: nowrap;
  &:hover:not(.active) { border-color: rgba(0,229,255,0.3); color: var(--text-primary); }
  &.active { border-color: var(--accent); background: rgba(0,229,255,0.15); color: var(--accent); box-shadow: 0 0 8px var(--accent-glow); }
}

// ============ 弹窗 ============
.popup-overlay { position: absolute; inset: 0; z-index: 200; display: flex; align-items: center; justify-content: center; background: rgba(0,0,0,0.4); animation: fadeIn 0.2s ease; }
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }

.glass-popup {
  width: 420px; max-height: 80%;
  background: var(--bg-card); backdrop-filter: blur(16px); -webkit-backdrop-filter: blur(16px);
  border: 1px solid var(--border-glow); border-radius: 16px;
  box-shadow: 0 8px 40px rgba(0,0,0,0.6), inset 0 1px 0 rgba(0,229,255,0.1);
  animation: popupSlideIn 0.3s ease; overflow: hidden;
}
@keyframes popupSlideIn { from { opacity: 0; transform: translateY(20px) scale(0.95); } to { opacity: 1; transform: translateY(0) scale(1); } }

.popup-header { padding: 14px 18px; border-bottom: 1px solid rgba(255,255,255,0.08); display: flex; justify-content: space-between; align-items: center; background: linear-gradient(180deg, rgba(0,229,255,0.1) 0%, transparent 100%); }
.popup-title { font-size: 15px; font-weight: 600; color: var(--accent); letter-spacing: 0.5px; }
.popup-close { cursor: pointer; color: var(--text-secondary); font-size: 14px; padding: 2px 6px; border-radius: 4px; transition: all 0.2s ease; &:hover { color: var(--danger); background: rgba(255,51,51,0.1); } }
.popup-body { padding: 16px 18px; overflow-y: auto; max-height: 60vh; }

.device-tabs { display: flex; gap: 2px; margin-bottom: 14px; border-bottom: 1px solid rgba(255,255,255,0.08); }
.dt-item { padding: 8px 14px; cursor: pointer; font-size: 12px; color: var(--text-secondary); border-bottom: 2px solid transparent; transition: all 0.3s ease; user-select: none; white-space: nowrap;
  &:hover:not(.active) { color: var(--text-primary); }
  &.active { color: var(--accent); border-bottom-color: var(--accent); }
}

.device-info, .device-realtime { display: flex; flex-direction: column; }
.di-row, .dr-item { display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid rgba(255,255,255,0.04); font-size: 12px; &:last-child { border-bottom: none; } }
.di-label, .dr-label { color: var(--text-secondary); }
.di-value { color: var(--text-primary); font-weight: 500; }
.dr-value { font-weight: 600; font-family: 'DIN Alternate', 'Orbitron', monospace; }

.device-history { padding: 4px 0; }

.device-alerts { display: flex; flex-direction: column; }
.da-item { display: flex; align-items: center; gap: 8px; padding: 8px 0; border-bottom: 1px solid rgba(255,255,255,0.04); font-size: 12px; &:last-child { border-bottom: none; } }
.da-dot { display: inline-block; width: 6px; height: 6px; border-radius: 50%; flex-shrink: 0;
  &.danger { background: var(--danger); box-shadow: 0 0 6px rgba(255,51,51,0.5); }
  &.warning { background: var(--warning); box-shadow: 0 0 6px rgba(255,149,0,0.5); }
  &.success { background: var(--success); box-shadow: 0 0 6px rgba(39,201,122,0.5); }
}
.da-text { flex: 1; color: var(--text-secondary); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.da-time { color: var(--text-secondary); opacity: 0.6; font-size: 11px; flex-shrink: 0; }
</style>
