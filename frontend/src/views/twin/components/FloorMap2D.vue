<template>
  <div class="floor-map-container" ref="containerRef">
    <canvas ref="canvasRef" class="map-canvas"
      @mousedown="onMouseDown"
      @mousemove="onMouseMove"
      @mouseup="onMouseUp"
      @mouseleave="onMouseUp"
      @wheel.prevent="onWheel"
      @click="onClick"
    ></canvas>

    <!-- 楼层信息 -->
    <div class="floor-info-bar">
      <button class="back-btn" @click="emit('back')">
        <svg viewBox="0 0 24 24" width="14" height="14" fill="currentColor">
          <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"/>
        </svg>
        返回园区
      </button>
      <span class="floor-title">{{ floorInfo.buildingName }} - {{ floorInfo.floorNum }}F</span>
      <div class="floor-stats">
        <span>功率: {{ floorInfo.power }}kW</span>
        <span>温度: {{ floorInfo.temperature }}°C</span>
        <span>湿度: {{ floorInfo.humidity }}%</span>
        <span>人员: {{ floorInfo.peopleCount }}</span>
      </div>
    </div>

    <!-- 楼层选择器 -->
    <div class="floor-selector">
      <div class="floor-title">楼层</div>
      <button
        v-for="f in floorInfo.totalFloors"
        :key="f"
        class="floor-btn"
        :class="{ active: f === floorInfo.floorNum }"
        @click="emit('floor-change', f)"
      >
        {{ f }}F
      </button>
    </div>

    <!-- 缩放控件 -->
    <div class="zoom-controls">
      <button class="zoom-btn" @click="zoomIn">+</button>
      <div class="zoom-level">{{ Math.round(scale * 100) }}%</div>
      <button class="zoom-btn" @click="zoomOut">-</button>
    </div>

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
import { ref, reactive, onMounted, onBeforeUnmount, nextTick, watch } from 'vue'

const props = defineProps({
  floorInfo: { type: Object, default: () => ({ buildingName: '', floorNum: 1, totalFloors: 1 }) },
  rooms: { type: Array, default: () => [] },
  devices: { type: Array, default: () => [] },
  personnel: { type: Array, default: () => [] }
})

const emit = defineEmits(['back', 'floor-change', 'device-click'])

const containerRef = ref(null)
const canvasRef = ref(null)
let ctx = null
let animFrameId = null

const viewState = reactive({ offsetX: 0, offsetY: 0, scale: 1, minScale: 0.5, maxScale: 3 })
const scale = ref(1)
const drag = reactive({ isDragging: false, startX: 0, startY: 0, startOffsetX: 0, startOffsetY: 0 })
const hover = reactive({ type: null, id: null })
const tooltip = reactive({ visible: false, title: '', items: [], style: {} })
let blinkPhase = 0

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
  ctx.clearRect(0, 0, w, h)

  // 背景
  ctx.fillStyle = '#0A1628'
  ctx.fillRect(0, 0, w, h)

  // 网格
  ctx.strokeStyle = 'rgba(64, 128, 255, 0.04)'
  ctx.lineWidth = 0.5
  for (let x = 0; x < w; x += 30) {
    ctx.beginPath(); ctx.moveTo(x, 0); ctx.lineTo(x, h); ctx.stroke()
  }
  for (let y = 0; y < h; y += 30) {
    ctx.beginPath(); ctx.moveTo(0, y); ctx.lineTo(w, y); ctx.stroke()
  }

  ctx.save()
  ctx.translate(viewState.offsetX, viewState.offsetY)
  ctx.scale(viewState.scale, viewState.scale)

  // 绘制楼层外框
  const floorW = 90
  const floorH = 80
  const ox = (w / viewState.scale - floorW * 8) / 2
  const oy = (h / viewState.scale - floorH * 6) / 2
  ctx.strokeStyle = 'rgba(64, 128, 255, 0.3)'
  ctx.lineWidth = 1.5
  ctx.strokeRect(ox, oy, floorW * 8, floorH * 6)

  // 绘制房间
  drawRooms(ox, oy)

  // 绘制设备
  drawFloorDevices(ox, oy)

  // 绘制人员
  drawPersonnel(ox, oy)

  // 绘制环境数据
  drawEnvData(ox, oy)

  ctx.restore()

  blinkPhase += 0.05
  animFrameId = requestAnimationFrame(draw)
}

function drawRooms(ox, oy) {
  const roomColors = {
    office: { fill: 'rgba(40, 70, 130, 0.3)', border: 'rgba(64, 128, 255, 0.3)' },
    meeting: { fill: 'rgba(119, 93, 208, 0.2)', border: 'rgba(119, 93, 208, 0.4)' },
    utility: { fill: 'rgba(100, 100, 120, 0.2)', border: 'rgba(150, 150, 170, 0.3)' },
    corridor: { fill: 'rgba(80, 90, 110, 0.15)', border: 'rgba(100, 110, 130, 0.2)' },
    restroom: { fill: 'rgba(0, 180, 80, 0.1)', border: 'rgba(0, 180, 80, 0.2)' }
  }

  props.rooms.forEach(room => {
    const rx = ox + room.x * 8
    const ry = oy + room.y * 6
    const rw = room.width * 8
    const rh = room.height * 6
    const colors = roomColors[room.type] || roomColors.office
    const isHover = hover.type === 'room' && hover.id === room.id

    ctx.fillStyle = isHover ? colors.fill.replace('0.3', '0.5') : colors.fill
    ctx.fillRect(rx, ry, rw, rh)
    ctx.strokeStyle = isHover ? 'rgba(64, 128, 255, 0.6)' : colors.border
    ctx.lineWidth = isHover ? 1.5 : 0.8
    ctx.strokeRect(rx, ry, rw, rh)

    // 房间名称
    ctx.fillStyle = 'rgba(200, 220, 255, 0.8)'
    ctx.font = '10px sans-serif'
    ctx.textAlign = 'center'
    ctx.textBaseline = 'middle'
    ctx.fillText(room.name, rx + rw / 2, ry + rh / 2)

    // 面积
    if (room.area) {
      ctx.fillStyle = 'rgba(136, 153, 187, 0.6)'
      ctx.font = '8px sans-serif'
      ctx.fillText(`${room.area}m²`, rx + rw / 2, ry + rh / 2 + 12)
    }
  })
}

function drawFloorDevices(ox, oy) {
  const deviceIcons = {
    temp_humidity: { symbol: 'TH', color: '#00D4FF' },
    smoke: { symbol: 'SMK', color: '#FF4560' },
    camera: { symbol: 'CAM', color: '#4080FF' },
    access_control: { symbol: 'AC', color: '#775DD0' },
    lighting: { symbol: 'LT', color: '#FEB019' },
    hvac: { symbol: 'HVAC', color: '#90CAF9' }
  }

  props.devices.forEach(d => {
    const dx = ox + d.x * 8
    const dy = oy + d.y * 6
    const icon = deviceIcons[d.type] || { symbol: 'D', color: '#666' }
    const isHover = hover.type === 'device' && hover.id === d.id

    // 设备底座
    ctx.fillStyle = isHover ? 'rgba(64, 128, 255, 0.3)' : 'rgba(16, 32, 64, 0.6)'
    ctx.beginPath()
    ctx.arc(dx, dy, isHover ? 9 : 7, 0, Math.PI * 2)
    ctx.fill()

    ctx.strokeStyle = icon.color
    ctx.lineWidth = 1.2
    ctx.beginPath()
    ctx.arc(dx, dy, isHover ? 9 : 7, 0, Math.PI * 2)
    ctx.stroke()

    ctx.fillStyle = '#E0E8FF'
    ctx.font = 'bold 5px sans-serif'
    ctx.textAlign = 'center'
    ctx.textBaseline = 'middle'
    ctx.fillText(icon.symbol, dx, dy)

    if (isHover) {
      ctx.fillStyle = '#B0BEC5'
      ctx.font = '8px sans-serif'
      ctx.fillText(d.name, dx, dy + 13)
    }
  })
}

function drawPersonnel(ox, oy) {
  props.personnel.forEach(p => {
    const px = ox + p.x * 8
    const py = oy + p.y * 6

    // 人员定位点
    ctx.fillStyle = 'rgba(0, 227, 150, 0.6)'
    ctx.beginPath()
    ctx.arc(px, py, 4, 0, Math.PI * 2)
    ctx.fill()

    // 脉冲效果
    const pulse = Math.sin(blinkPhase * 2 + p.id) * 0.5 + 0.5
    ctx.strokeStyle = `rgba(0, 227, 150, ${0.3 * (1 - pulse)})`
    ctx.lineWidth = 1
    ctx.beginPath()
    ctx.arc(px, py, 4 + pulse * 6, 0, Math.PI * 2)
    ctx.stroke()

    // 人员图标
    ctx.fillStyle = '#00E396'
    ctx.beginPath()
    ctx.arc(px, py - 1.5, 1.5, 0, Math.PI * 2)
    ctx.fill()
    ctx.strokeStyle = '#00E396'
    ctx.lineWidth = 1
    ctx.beginPath()
    ctx.moveTo(px, py)
    ctx.lineTo(px, py + 3)
    ctx.stroke()
  })
}

function drawEnvData(ox, oy) {
  // 在楼层四角显示环境数据
  const envData = [
    { x: 5, y: 5, label: `${props.floorInfo.temperature}°C`, color: '#FEB019' },
    { x: 85, y: 5, label: `${props.floorInfo.humidity}%`, color: '#00D4FF' },
    { x: 5, y: 75, label: `PM2.5: 35`, color: '#00E396' },
    { x: 85, y: 75, label: `CO2: 680ppm`, color: '#90CAF9' }
  ]

  envData.forEach(env => {
    const ex = ox + env.x * 8
    const ey = oy + env.y * 6
    ctx.fillStyle = 'rgba(16, 32, 64, 0.7)'
    roundRect(ctx, ex - 30, ey - 8, 60, 16, 4)
    ctx.fill()
    ctx.strokeStyle = 'rgba(64, 128, 255, 0.2)'
    ctx.lineWidth = 0.5
    roundRect(ctx, ex - 30, ey - 8, 60, 16, 4)
    ctx.stroke()
    ctx.fillStyle = env.color
    ctx.font = '8px sans-serif'
    ctx.textAlign = 'center'
    ctx.textBaseline = 'middle'
    ctx.fillText(env.label, ex, ey)
  })
}

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

  const { x: mx, y: my } = screenToMap(sx, sy)
  const w = canvasRef.value.clientWidth
  const h = canvasRef.value.clientHeight
  const floorW = 90
  const floorH = 80
  const ox = (w / viewState.scale - floorW * 8) / 2
  const oy = (h / viewState.scale - floorH * 6) / 2

  let found = false
  // 检测设备
  for (const d of props.devices) {
    const dx = ox + d.x * 8
    const dy = oy + d.y * 6
    if (Math.sqrt((mx - dx) ** 2 + (my - dy) ** 2) <= 9) {
      hover.type = 'device'; hover.id = d.id
      canvasRef.value.style.cursor = 'pointer'
      tooltip.visible = true
      tooltip.title = d.name
      tooltip.items = [{ label: '类型', value: d.type }, { label: '状态', value: d.status }]
      tooltip.style = { left: (e.clientX + 16) + 'px', top: (e.clientY - 10) + 'px' }
      found = true
      break
    }
  }

  if (!found) {
    hover.type = null; hover.id = null
    canvasRef.value.style.cursor = drag.isDragging ? 'grabbing' : 'grab'
    tooltip.visible = false
  }
}

function onMouseUp() { drag.isDragging = false }

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
  const w = canvasRef.value.clientWidth
  const h = canvasRef.value.clientHeight
  const floorW = 90
  const floorH = 80
  const ox = (w / viewState.scale - floorW * 8) / 2
  const oy = (h / viewState.scale - floorH * 6) / 2

  for (const d of props.devices) {
    const dx = ox + d.x * 8
    const dy = oy + d.y * 6
    if (Math.sqrt((mx - dx) ** 2 + (my - dy) ** 2) <= 9) {
      emit('device-click', d, e)
      return
    }
  }
}

function zoomIn() {
  const newScale = Math.min(viewState.maxScale, viewState.scale * 1.2)
  const c = containerRef.value
  const cx = c.clientWidth / 2, cy = c.clientHeight / 2
  const ratio = newScale / viewState.scale
  viewState.offsetX = cx - (cx - viewState.offsetX) * ratio
  viewState.offsetY = cy - (cy - viewState.offsetY) * ratio
  viewState.scale = newScale; scale.value = newScale
}

function zoomOut() {
  const newScale = Math.max(viewState.minScale, viewState.scale / 1.2)
  const c = containerRef.value
  const cx = c.clientWidth / 2, cy = c.clientHeight / 2
  const ratio = newScale / viewState.scale
  viewState.offsetX = cx - (cx - viewState.offsetX) * ratio
  viewState.offsetY = cy - (cy - viewState.offsetY) * ratio
  viewState.scale = newScale; scale.value = newScale
}

onMounted(() => {
  nextTick(() => { initCanvas(); draw() })
  window.addEventListener('resize', () => initCanvas())
})

onBeforeUnmount(() => {
  if (animFrameId) cancelAnimationFrame(animFrameId)
})

watch(() => [props.rooms, props.devices, props.personnel], () => {}, { deep: true })
</script>

<style scoped lang="scss">
.floor-map-container {
  width: 100%;
  height: 100%;
  position: relative;
  overflow: hidden;

  .map-canvas {
    width: 100%;
    height: 100%;
    cursor: grab;
    &:active { cursor: grabbing; }
  }

  .floor-info-bar {
    position: absolute;
    top: 12px;
    left: 12px;
    z-index: 20;
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 8px 16px;
    border-radius: 10px;
    background: rgba(16, 32, 64, 0.85);
    backdrop-filter: blur(8px);
    border: 1px solid rgba(64, 128, 255, 0.2);

    .back-btn {
      display: flex;
      align-items: center;
      gap: 4px;
      padding: 4px 10px;
      border-radius: 6px;
      border: 1px solid rgba(64, 128, 255, 0.3);
      background: rgba(64, 128, 255, 0.1);
      color: #8899BB;
      cursor: pointer;
      font-size: 12px;
      transition: all 0.2s;

      &:hover { background: rgba(64, 128, 255, 0.2); color: #E0E8FF; }
    }

    .floor-title {
      font-size: 14px;
      font-weight: 600;
      color: #E0E8FF;
    }

    .floor-stats {
      display: flex;
      gap: 12px;
      font-size: 11px;
      color: #8899BB;

      span { white-space: nowrap; }
    }
  }

  .floor-selector {
    position: absolute;
    top: 12px;
    right: 12px;
    z-index: 20;
    display: flex;
    flex-direction: column;
    gap: 4px;
    padding: 8px;
    border-radius: 12px;
    background: rgba(16, 32, 64, 0.85);
    backdrop-filter: blur(8px);
    border: 1px solid rgba(64, 128, 255, 0.2);

    .floor-title {
      font-size: 11px;
      color: #667799;
      text-align: center;
      margin-bottom: 4px;
    }

    .floor-btn {
      width: 36px;
      height: 36px;
      border-radius: 8px;
      border: 1px solid rgba(64, 128, 255, 0.15);
      background: rgba(64, 128, 255, 0.05);
      color: #8899BB;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 13px;
      font-weight: 500;
      transition: all 0.2s;

      &:hover { background: rgba(64, 128, 255, 0.15); color: #E0E8FF; }

      &.active {
        background: linear-gradient(135deg, #4080FF, #2060DD);
        color: #FFFFFF;
        border-color: #4080FF;
        box-shadow: 0 0 8px rgba(64, 128, 255, 0.4);
      }
    }
  }

  .zoom-controls {
    position: absolute;
    bottom: 24px;
    right: 12px;
    z-index: 20;
    display: flex;
    flex-direction: column;
    gap: 4px;

    .zoom-btn {
      width: 36px;
      height: 36px;
      border-radius: 8px;
      border: 1px solid rgba(64, 128, 255, 0.3);
      background: rgba(16, 32, 64, 0.8);
      backdrop-filter: blur(8px);
      color: #8899BB;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 18px;
      font-weight: bold;
      transition: all 0.2s;

      &:hover { background: rgba(64, 128, 255, 0.2); color: #E0E8FF; }
    }

    .zoom-level {
      text-align: center;
      font-size: 11px;
      color: #667799;
      padding: 2px 0;
    }
  }
}

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
