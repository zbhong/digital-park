<template>
  <div class="map-container" ref="containerRef">
    <canvas ref="canvasRef" class="map-canvas"
      @mousedown="onMouseDown"
      @mousemove="onMouseMove"
      @mouseup="onMouseUp"
      @mouseleave="onMouseUp"
      @wheel.prevent="onWheel"
      @click="onClick"
    ></canvas>

    <!-- 视图切换 -->
    <div class="view-switcher">
      <button class="view-btn active" @click="emit('view-change', 'park')">园区视图</button>
      <button class="view-btn" @click="emit('view-change', 'floor')">楼层视图</button>
    </div>

    <!-- 缩放控件 -->
    <div class="zoom-controls">
      <button class="zoom-btn" @click="zoomIn">+</button>
      <div class="zoom-level">{{ Math.round(scale * 100) }}%</div>
      <button class="zoom-btn" @click="zoomOut">-</button>
      <button class="zoom-btn" @click="resetView" title="重置视图" style="font-size:12px;">&#8634;</button>
    </div>

    <!-- 图层控制 -->
    <LayerControl ref="layerControlRef" @change="onLayerChange" />

    <!-- Tooltip -->
    <transition name="popup-fade">
      <div v-if="tooltip.visible" class="map-tooltip" :style="tooltip.style">
        <div class="tooltip-title">{{ tooltip.title }}</div>
        <div v-if="tooltip.items" class="tooltip-items">
          <div v-for="item in tooltip.items" :key="item.label" class="tooltip-item">
            <span class="tooltip-label">{{ item.label }}</span>
            <span class="tooltip-value">{{ item.value }}</span>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, watch, nextTick } from 'vue'
import LayerControl from './LayerControl.vue'

const props = defineProps({
  buildings: { type: Array, default: () => [] },
  devices: { type: Array, default: () => [] },
  alerts: { type: Array, default: () => [] }
})

const emit = defineEmits(['building-click', 'device-click', 'view-change'])

const containerRef = ref(null)
const canvasRef = ref(null)
const layerControlRef = ref(null)

let ctx = null
let animFrameId = null

// 视图变换
const viewState = reactive({
  offsetX: 0,
  offsetY: 0,
  scale: 1,
  minScale: 0.3,
  maxScale: 3
})

const scale = ref(1)

// 拖拽状态
const drag = reactive({
  isDragging: false,
  startX: 0,
  startY: 0,
  startOffsetX: 0,
  startOffsetY: 0
})

// 悬停状态
const hover = reactive({
  type: null, // 'building' | 'device'
  id: null
})

// 图层状态
const layerState = reactive({
  building: { visible: true, opacity: 1 },
  device: { visible: true, opacity: 1 },
  security: { visible: true, opacity: 1 },
  environment: { visible: true, opacity: 1 },
  pipeline: { visible: true, opacity: 1 },
  area: { visible: true, opacity: 1 }
})

// Tooltip
const tooltip = reactive({
  visible: false,
  title: '',
  items: [],
  style: {}
})

// 告警闪烁
let blinkPhase = 0

// ==================== 园区地图数据 ====================
const parkData = {
  // 道路
  roads: [
    { x1: 0, y1: 48, x2: 100, y2: 48, width: 3 }, // 主干道
    { x1: 38, y1: 0, x2: 38, y2: 100, width: 2 }, // 纵向道路
    { x1: 62, y1: 0, x2: 62, y2: 100, width: 2 }, // 纵向道路
    { x1: 0, y1: 85, x2: 100, y2: 85, width: 2 }  // 底部道路
  ],
  // 绿化区域
  greenAreas: [
    { x: 2, y: 2, width: 10, height: 8 },
    { x: 90, y: 2, width: 8, height: 8 },
    { x: 2, y: 88, width: 12, height: 10 },
    { x: 35, y: 50, width: 3, height: 35 },
    { x: 59, y: 50, width: 3, height: 35 },
    { x: 40, y: 85, width: 20, height: 3 }
  ],
  // 停车场
  parking: [
    { x: 82, y: 68, width: 16, height: 14, name: '地面停车场', total: 120, used: 85 },
    { x: 2, y: 68, width: 12, height: 14, name: '地下停车场入口', total: 560, used: 373 }
  ],
  // 出入口
  entrances: [
    { x: 0, y: 48, name: '西门' },
    { x: 100, y: 48, name: '东门' },
    { x: 50, y: 100, name: '南门' },
    { x: 50, y: 0, name: '北门' }
  ]
}

// ==================== Canvas 绘制 ====================

function initCanvas() {
  if (!canvasRef.value || !containerRef.value) return
  const container = containerRef.value
  const dpr = window.devicePixelRatio || 1
  canvasRef.value.width = container.clientWidth * dpr
  canvasRef.value.height = container.clientHeight * dpr
  canvasRef.value.style.width = container.clientWidth + 'px'
  canvasRef.value.style.height = container.clientHeight + 'px'
  ctx = canvasRef.value.getContext('2d')
  ctx.scale(dpr, dpr)
}

function draw() {
  if (!ctx || !canvasRef.value) return
  const w = canvasRef.value.clientWidth
  const h = canvasRef.value.clientHeight

  // 清空
  ctx.clearRect(0, 0, w, h)

  // 背景
  ctx.fillStyle = '#0A1628'
  ctx.fillRect(0, 0, w, h)

  // 网格
  drawGrid(w, h)

  ctx.save()
  ctx.translate(viewState.offsetX, viewState.offsetY)
  ctx.scale(viewState.scale, viewState.scale)

  // 绘制区域
  if (layerState.area.visible) {
    drawParkBoundary(w, h)
    drawGreenAreas()
    drawParking()
    drawEntrances()
  }

  // 绘制道路
  drawRoads()

  // 绘制管网
  if (layerState.pipeline.visible) {
    drawPipelines()
  }

  // 绘制建筑
  if (layerState.building.visible) {
    drawBuildings()
  }

  // 绘制设备
  if (layerState.device.visible) {
    drawDevices()
  }

  // 绘制安防
  if (layerState.security.visible) {
    drawAlerts()
  }

  // 绘制环境
  if (layerState.environment.visible) {
    drawEnvironment()
  }

  ctx.restore()

  // 闪烁动画
  blinkPhase += 0.05
  animFrameId = requestAnimationFrame(draw)
}

function drawGrid(w, h) {
  ctx.strokeStyle = 'rgba(64, 128, 255, 0.04)'
  ctx.lineWidth = 0.5
  const gridSize = 40
  for (let x = 0; x < w; x += gridSize) {
    ctx.beginPath()
    ctx.moveTo(x, 0)
    ctx.lineTo(x, h)
    ctx.stroke()
  }
  for (let y = 0; y < h; y += gridSize) {
    ctx.beginPath()
    ctx.moveTo(0, y)
    ctx.lineTo(w, y)
    ctx.stroke()
  }
}

function drawParkBoundary(w, h) {
  const margin = 1
  ctx.strokeStyle = 'rgba(64, 128, 255, 0.2)'
  ctx.lineWidth = 1
  ctx.setLineDash([5, 5])
  ctx.strokeRect(margin, margin, 98, 98)
  ctx.setLineDash([])
}

function drawGreenAreas() {
  const alpha = layerState.area.opacity
  parkData.greenAreas.forEach(area => {
    ctx.fillStyle = `rgba(0, 180, 80, ${0.15 * alpha})`
    ctx.strokeStyle = `rgba(0, 180, 80, ${0.3 * alpha})`
    ctx.lineWidth = 0.5
    const rx = area.x * 8
    const ry = area.y * 6
    const rw = area.width * 8
    const rh = area.height * 6
    roundRect(ctx, rx, ry, rw, rh, 4)
    ctx.fill()
    ctx.stroke()
    // 树木图标
    drawTree(rx + rw / 2, ry + rh / 2, alpha)
  })
}

function drawTree(x, y, alpha) {
  ctx.fillStyle = `rgba(0, 200, 100, ${0.4 * alpha})`
  ctx.beginPath()
  ctx.arc(x, y, 3, 0, Math.PI * 2)
  ctx.fill()
}

function drawParking() {
  const alpha = layerState.area.opacity
  parkData.parking.forEach(p => {
    const px = p.x * 8
    const py = p.y * 6
    const pw = p.width * 8
    const ph = p.height * 6
    ctx.fillStyle = `rgba(100, 100, 120, ${0.15 * alpha})`
    ctx.strokeStyle = `rgba(150, 150, 170, ${0.3 * alpha})`
    ctx.lineWidth = 0.5
    roundRect(ctx, px, py, pw, ph, 4)
    ctx.fill()
    ctx.stroke()
    // 停车场图标
    ctx.fillStyle = `rgba(150, 150, 170, ${0.6 * alpha})`
    ctx.font = '10px sans-serif'
    ctx.textAlign = 'center'
    ctx.fillText('P', px + pw / 2, py + 12)
    ctx.font = '8px sans-serif'
    ctx.fillText(`${p.used}/${p.total}`, px + pw / 2, py + 22)
  })
}

function drawEntrances() {
  const alpha = layerState.area.opacity
  parkData.entrances.forEach(e => {
    const ex = e.x * 8
    const ey = e.y * 6
    ctx.fillStyle = `rgba(64, 128, 255, ${0.6 * alpha})`
    ctx.beginPath()
    ctx.arc(ex, ey, 4, 0, Math.PI * 2)
    ctx.fill()
    ctx.fillStyle = `rgba(200, 220, 255, ${0.8 * alpha})`
    ctx.font = '7px sans-serif'
    ctx.textAlign = 'center'
    ctx.fillText(e.name, ex, ey - 7)
  })
}

function drawRoads() {
  ctx.strokeStyle = 'rgba(100, 110, 130, 0.4)'
  parkData.roads.forEach(road => {
    ctx.lineWidth = road.width
    ctx.beginPath()
    ctx.moveTo(road.x1 * 8, road.y1 * 6)
    ctx.lineTo(road.x2 * 8, road.y2 * 6)
    ctx.stroke()
    // 道路中心线
    ctx.strokeStyle = 'rgba(200, 200, 50, 0.2)'
    ctx.lineWidth = 0.5
    ctx.setLineDash([4, 4])
    ctx.beginPath()
    ctx.moveTo(road.x1 * 8, road.y1 * 6)
    ctx.lineTo(road.x2 * 8, road.y2 * 6)
    ctx.stroke()
    ctx.setLineDash([])
    ctx.strokeStyle = 'rgba(100, 110, 130, 0.4)'
  })
}

function drawPipelines() {
  const alpha = layerState.pipeline.opacity
  ctx.strokeStyle = `rgba(254, 176, 25, ${0.3 * alpha})`
  ctx.lineWidth = 1.5
  ctx.setLineDash([3, 3])
  // 电力管线
  ctx.beginPath()
  ctx.moveTo(10 * 8, 10 * 6)
  ctx.lineTo(38 * 8, 10 * 6)
  ctx.lineTo(38 * 8, 48 * 6)
  ctx.stroke()
  // 水管线
  ctx.strokeStyle = `rgba(0, 212, 255, ${0.3 * alpha})`
  ctx.beginPath()
  ctx.moveTo(2 * 8, 75 * 6)
  ctx.lineTo(38 * 8, 75 * 6)
  ctx.lineTo(38 * 8, 48 * 6)
  ctx.stroke()
  ctx.setLineDash([])
}

function drawBuildings() {
  const alpha = layerState.building.opacity
  props.buildings.forEach(b => {
    const bx = b.x * 8
    const by = b.y * 6
    const bw = b.width * 8
    const bh = b.height * 6
    const isHover = hover.type === 'building' && hover.id === b.id
    const isWarning = b.status === 'warning'

    // 建筑阴影
    ctx.fillStyle = `rgba(0, 0, 0, ${0.3 * alpha})`
    roundRect(ctx, bx + 2, by + 2, bw, bh, 6)
    ctx.fill()

    // 建筑主体
    const gradient = ctx.createLinearGradient(bx, by, bx, by + bh)
    if (isHover) {
      gradient.addColorStop(0, `rgba(64, 128, 255, ${0.5 * alpha})`)
      gradient.addColorStop(1, `rgba(32, 96, 221, ${0.4 * alpha})`)
    } else if (isWarning) {
      gradient.addColorStop(0, `rgba(254, 176, 25, ${0.35 * alpha})`)
      gradient.addColorStop(1, `rgba(200, 140, 20, ${0.25 * alpha})`)
    } else {
      gradient.addColorStop(0, `rgba(40, 70, 130, ${0.5 * alpha})`)
      gradient.addColorStop(1, `rgba(25, 50, 100, ${0.4 * alpha})`)
    }
    ctx.fillStyle = gradient
    roundRect(ctx, bx, by, bw, bh, 6)
    ctx.fill()

    // 建筑边框
    ctx.strokeStyle = isHover
      ? `rgba(64, 128, 255, ${0.8 * alpha})`
      : isWarning
        ? `rgba(254, 176, 25, ${0.6 * alpha})`
        : `rgba(64, 128, 255, ${0.3 * alpha})`
    ctx.lineWidth = isHover ? 2 : 1
    roundRect(ctx, bx, by, bw, bh, 6)
    ctx.stroke()

    // 建筑名称
    ctx.fillStyle = `rgba(224, 232, 255, ${0.9 * alpha})`
    ctx.font = 'bold 11px sans-serif'
    ctx.textAlign = 'center'
    ctx.textBaseline = 'middle'
    ctx.fillText(b.name, bx + bw / 2, by + bh / 2 - 8)

    // 楼层信息
    ctx.fillStyle = `rgba(136, 153, 187, ${0.7 * alpha})`
    ctx.font = '9px sans-serif'
    ctx.fillText(`${b.floors}F | ${b.area}m²`, bx + bw / 2, by + bh / 2 + 6)

    // 实时指标（建筑上方）
    ctx.fillStyle = `rgba(0, 227, 150, ${0.8 * alpha})`
    ctx.font = '8px sans-serif'
    ctx.fillText(`${b.power}kW`, bx + bw / 2, by - 6)

    // 告警标记
    if (isWarning) {
      const blink = Math.sin(blinkPhase * 3) > 0
      if (blink) {
        ctx.fillStyle = `rgba(255, 69, 96, ${0.8 * alpha})`
        ctx.beginPath()
        ctx.arc(bx + bw - 6, by + 6, 4, 0, Math.PI * 2)
        ctx.fill()
      }
    }
  })
}

function drawDevices() {
  const alpha = layerState.device.opacity
  const deviceIcons = {
    camera: { symbol: 'CAM', color: '#4080FF' },
    smoke: { symbol: 'SMK', color: '#FF4560' },
    charging: { symbol: 'CHG', color: '#00E396' },
    temp_humidity: { symbol: 'TH', color: '#00D4FF' },
    access_control: { symbol: 'AC', color: '#775DD0' },
    solar_panel: { symbol: 'PV', color: '#FEB019' },
    storage: { symbol: 'ESS', color: '#00E396' },
    env_station: { symbol: 'ENV', color: '#00D4FF' },
    street_light: { symbol: 'SL', color: '#FF8C42' },
    energy_meter: { symbol: 'EM', color: '#4080FF' },
    water_meter: { symbol: 'WM', color: '#00D4FF' },
    lighting: { symbol: 'LT', color: '#FEB019' },
    hvac: { symbol: 'HVAC', color: '#90CAF9' }
  }

  props.devices.forEach(d => {
    const dx = d.x * 8
    const dy = d.y * 6
    const icon = deviceIcons[d.deviceType] || { symbol: 'DEV', color: '#666' }
    const isHover = hover.type === 'device' && hover.id === d.id
    const isAlert = d.alert
    const isOffline = d.status === 'offline'

    // 设备底座
    ctx.fillStyle = isHover
      ? `rgba(64, 128, 255, ${0.3 * alpha})`
      : `rgba(16, 32, 64, ${0.6 * alpha})`
    ctx.beginPath()
    ctx.arc(dx, dy, isHover ? 10 : 8, 0, Math.PI * 2)
    ctx.fill()

    // 设备边框
    ctx.strokeStyle = isOffline
      ? `rgba(255, 69, 96, ${0.6 * alpha})`
      : icon.color.replace(')', `, ${0.6 * alpha})`).replace('rgb', 'rgba')
    ctx.lineWidth = 1.5
    ctx.beginPath()
    ctx.arc(dx, dy, isHover ? 10 : 8, 0, Math.PI * 2)
    ctx.stroke()

    // 设备图标文字
    ctx.fillStyle = isOffline
      ? `rgba(255, 69, 96, ${0.8 * alpha})`
      : `rgba(224, 232, 255, ${0.8 * alpha})`
    ctx.font = 'bold 6px sans-serif'
    ctx.textAlign = 'center'
    ctx.textBaseline = 'middle'
    ctx.fillText(icon.symbol, dx, dy)

    // 设备名称
    if (isHover) {
      ctx.fillStyle = `rgba(200, 220, 255, ${0.9 * alpha})`
      ctx.font = '8px sans-serif'
      ctx.fillText(d.name, dx, dy + 14)
    }

    // 告警闪烁
    if (isAlert) {
      const blink = Math.sin(blinkPhase * 4) > 0
      if (blink) {
        ctx.fillStyle = `rgba(255, 69, 96, ${0.9 * alpha})`
        ctx.beginPath()
        ctx.arc(dx + 6, dy - 6, 3, 0, Math.PI * 2)
        ctx.fill()
      }
    }
  })
}

function drawAlerts() {
  const alpha = layerState.security.opacity
  // 安防摄像头覆盖范围
  props.devices.filter(d => d.deviceType === 'camera').forEach(d => {
    const dx = d.x * 8
    const dy = d.y * 6
    ctx.fillStyle = `rgba(64, 128, 255, ${0.05 * alpha})`
    ctx.beginPath()
    ctx.arc(dx, dy, 30, 0, Math.PI * 2)
    ctx.fill()
    ctx.strokeStyle = `rgba(64, 128, 255, ${0.1 * alpha})`
    ctx.lineWidth = 0.5
    ctx.stroke()
  })
}

function drawEnvironment() {
  const alpha = layerState.environment.opacity
  // 环境监测站数据标注
  props.devices.filter(d => d.deviceType === 'env_station').forEach(d => {
    const dx = d.x * 8
    const dy = d.y * 6
    ctx.fillStyle = `rgba(0, 212, 255, ${0.6 * alpha})`
    ctx.font = '8px sans-serif'
    ctx.textAlign = 'center'
    ctx.fillText('AQI: 52', dx, dy + 14)
    ctx.fillText('26.5°C', dx, dy + 24)
  })
}

// ==================== 工具函数 ====================

function roundRect(ctx, x, y, w, h, r) {
  ctx.beginPath()
  ctx.moveTo(x + r, y)
  ctx.lineTo(x + w - r, y)
  ctx.quadraticCurveTo(x + w, y, x + w, y + r)
  ctx.lineTo(x + w, y + h - r)
  ctx.quadraticCurveTo(x + w, y + h, x + w - r, y + h)
  ctx.lineTo(x + r, y + h)
  ctx.quadraticCurveTo(x, y + h, x, y + h - r)
  ctx.lineTo(x, y + r)
  ctx.quadraticCurveTo(x, y, x + r, y)
  ctx.closePath()
}

function screenToMap(sx, sy) {
  return {
    x: (sx - viewState.offsetX) / viewState.scale,
    y: (sy - viewState.offsetY) / viewState.scale
  }
}

function hitTestBuildings(mx, my) {
  for (let i = props.buildings.length - 1; i >= 0; i--) {
    const b = props.buildings[i]
    const bx = b.x * 8
    const by = b.y * 6
    const bw = b.width * 8
    const bh = b.height * 6
    if (mx >= bx && mx <= bx + bw && my >= by && my <= by + bh) {
      return b
    }
  }
  return null
}

function hitTestDevices(mx, my) {
  for (let i = props.devices.length - 1; i >= 0; i--) {
    const d = props.devices[i]
    const dx = d.x * 8
    const dy = d.y * 6
    const dist = Math.sqrt((mx - dx) ** 2 + (my - dy) ** 2)
    if (dist <= 10) {
      return d
    }
  }
  return null
}

// ==================== 事件处理 ====================

function onMouseDown(e) {
  drag.isDragging = true
  drag.startX = e.clientX
  drag.startY = e.clientY
  drag.startOffsetX = viewState.offsetX
  drag.startOffsetY = viewState.offsetY
}

function onMouseMove(e) {
  const rect = canvasRef.value.getBoundingClientRect()
  const sx = e.clientX - rect.left
  const sy = e.clientY - rect.top

  if (drag.isDragging) {
    viewState.offsetX = drag.startOffsetX + (e.clientX - drag.startX)
    viewState.offsetY = drag.startOffsetY + (e.clientY - drag.startY)
    return
  }

  // 悬停检测
  const { x: mx, y: my } = screenToMap(sx, sy)
  const building = hitTestBuildings(mx, my)
  const device = hitTestDevices(mx, my)

  if (building) {
    hover.type = 'building'
    hover.id = building.id
    canvasRef.value.style.cursor = 'pointer'
    showBuildingTooltip(building, e.clientX, e.clientY)
  } else if (device) {
    hover.type = 'device'
    hover.id = device.id
    canvasRef.value.style.cursor = 'pointer'
    showDeviceTooltip(device, e.clientX, e.clientY)
  } else {
    hover.type = null
    hover.id = null
    canvasRef.value.style.cursor = drag.isDragging ? 'grabbing' : 'grab'
    tooltip.visible = false
  }
}

function onMouseUp() {
  drag.isDragging = false
}

function onWheel(e) {
  const rect = canvasRef.value.getBoundingClientRect()
  const sx = e.clientX - rect.left
  const sy = e.clientY - rect.top

  const delta = e.deltaY > 0 ? 0.9 : 1.1
  const newScale = Math.min(viewState.maxScale, Math.max(viewState.minScale, viewState.scale * delta))
  const ratio = newScale / viewState.scale

  viewState.offsetX = sx - (sx - viewState.offsetX) * ratio
  viewState.offsetY = sy - (sy - viewState.offsetY) * ratio
  viewState.scale = newScale
  scale.value = newScale
}

function onClick(e) {
  const rect = canvasRef.value.getBoundingClientRect()
  const sx = e.clientX - rect.left
  const sy = e.clientY - rect.top
  const { x: mx, y: my } = screenToMap(sx, sy)

  const building = hitTestBuildings(mx, my)
  if (building) {
    emit('building-click', building, e)
    return
  }

  const device = hitTestDevices(mx, my)
  if (device) {
    emit('device-click', device, e)
    return
  }

  tooltip.visible = false
}

function showBuildingTooltip(building, clientX, clientY) {
  tooltip.visible = true
  tooltip.title = building.name
  tooltip.items = [
    { label: '楼层', value: `${building.floors}F` },
    { label: '面积', value: `${building.area}m²` },
    { label: '入驻企业', value: `${building.enterprises}家` },
    { label: '设备数', value: `${building.devices}台` },
    { label: '实时功率', value: `${building.power}kW` }
  ]
  tooltip.style = {
    left: (clientX + 16) + 'px',
    top: (clientY - 10) + 'px'
  }
}

function showDeviceTooltip(device, clientX, clientY) {
  tooltip.visible = true
  tooltip.title = device.name
  tooltip.items = [
    { label: '编号', value: device.code },
    { label: '类型', value: device.deviceType },
    { label: '状态', value: device.status === 'online' ? '在线' : '离线' }
  ]
  if (device.alert) {
    tooltip.items.push({ label: '告警', value: '有告警' })
  }
  tooltip.style = {
    left: (clientX + 16) + 'px',
    top: (clientY - 10) + 'px'
  }
}

function onLayerChange(state) {
  Object.assign(layerState, state)
}

function zoomIn() {
  const newScale = Math.min(viewState.maxScale, viewState.scale * 1.2)
  const container = containerRef.value
  const cx = container.clientWidth / 2
  const cy = container.clientHeight / 2
  const ratio = newScale / viewState.scale
  viewState.offsetX = cx - (cx - viewState.offsetX) * ratio
  viewState.offsetY = cy - (cy - viewState.offsetY) * ratio
  viewState.scale = newScale
  scale.value = newScale
}

function zoomOut() {
  const newScale = Math.max(viewState.minScale, viewState.scale / 1.2)
  const container = containerRef.value
  const cx = container.clientWidth / 2
  const cy = container.clientHeight / 2
  const ratio = newScale / viewState.scale
  viewState.offsetX = cx - (cx - viewState.offsetX) * ratio
  viewState.offsetY = cy - (cy - viewState.offsetY) * ratio
  viewState.scale = newScale
  scale.value = newScale
}

function resetView() {
  viewState.scale = 1
  viewState.offsetX = 0
  viewState.offsetY = 0
  scale.value = 1
}

// ==================== 生命周期 ====================

onMounted(() => {
  nextTick(() => {
    initCanvas()
    draw()
  })
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  if (animFrameId) cancelAnimationFrame(animFrameId)
  window.removeEventListener('resize', handleResize)
})

function handleResize() {
  initCanvas()
}

// 监听数据变化
watch(() => [props.buildings, props.devices], () => {
  // 数据变化时自动重绘（draw 循环已运行）
}, { deep: true })
</script>

<style scoped lang="scss">
@import '../styles/twin.scss';

.map-tooltip {
  position: fixed;
  z-index: 200;
  padding: 10px 14px;
  border-radius: 8px;
  background: rgba(16, 32, 64, 0.9);
  backdrop-filter: blur(8px);
  border: 1px solid rgba(64, 128, 255, 0.3);
  pointer-events: none;

  .tooltip-title {
    font-size: 13px;
    font-weight: 600;
    color: #E0E8FF;
    margin-bottom: 6px;
    padding-bottom: 4px;
    border-bottom: 1px solid rgba(64, 128, 255, 0.15);
  }

  .tooltip-items {
    .tooltip-item {
      display: flex;
      justify-content: space-between;
      gap: 16px;
      font-size: 11px;
      padding: 2px 0;

      .tooltip-label { color: #667799; }
      .tooltip-value { color: #E0E8FF; font-weight: 500; }
    }
  }
}
</style>
