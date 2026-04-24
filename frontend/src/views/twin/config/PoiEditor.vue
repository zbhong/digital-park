<template>
  <div class="poi-editor">
    <div class="editor-header">
      <span class="editor-title">{{ poi ? '编辑POI' : '新增POI' }}</span>
    </div>
    <div class="editor-body">
      <!-- 2D地图区域 -->
      <div class="poi-map-area">
        <div class="map-hint" v-if="!placingPoi">
          <el-button type="primary" size="small" @click="startPlacing">点击地图放置POI</el-button>
          <span style="font-size:11px;color:#667799;margin-left:8px">或手动输入坐标</span>
        </div>
        <div class="map-hint placing" v-else>
          <span style="color:#FEB019;font-size:12px">请在地图上点击选择位置...</span>
          <el-button size="small" @click="placingPoi = false">取消</el-button>
        </div>
        <canvas ref="mapCanvasRef" class="poi-map-canvas"
          @click="onMapClick"
          @mousedown="onMapMouseDown"
          @mousemove="onMapMouseMove"
          @mouseup="onMapMouseUp"
        ></canvas>
      </div>

      <!-- POI属性表单 -->
      <el-form :model="form" label-width="70px" size="small" class="poi-form">
        <el-form-item label="名称" required>
          <el-input v-model="form.name" placeholder="请输入POI名称" />
        </el-form-item>
        <el-form-item label="类型" required>
          <el-select v-model="form.type" placeholder="请选择" style="width:100%">
            <el-option label="楼栋" value="building" />
            <el-option label="设备" value="device" />
            <el-option label="出入口" value="entrance" />
            <el-option label="停车场" value="parking" />
          </el-select>
        </el-form-item>
        <el-form-item label="图标">
          <div class="icon-selector">
            <div v-for="icon in iconOptions" :key="icon.value"
                 class="icon-option" :class="{ active: form.icon === icon.value }"
                 @click="form.icon = icon.value">
              {{ icon.label }}
            </div>
          </div>
        </el-form-item>
        <el-form-item label="X坐标">
          <el-input-number v-model="form.x" :min="0" :max="100" :step="0.1" style="width:100%" />
        </el-form-item>
        <el-form-item label="Y坐标">
          <el-input-number v-model="form.y" :min="0" :max="100" :step="0.1" style="width:100%" />
        </el-form-item>
        <el-form-item label="所属建筑">
          <el-select v-model="form.buildingId" placeholder="请选择" clearable style="width:100%">
            <el-option v-for="b in buildings" :key="b" :label="b" :value="b" />
          </el-select>
        </el-form-item>
        <el-form-item label="楼层">
          <el-input v-model="form.floor" placeholder="如: 1F" />
        </el-form-item>
        <el-form-item label="弹窗配置">
          <el-input v-model="form.popupConfig" type="textarea" :rows="2" placeholder="JSON格式弹窗配置" />
        </el-form-item>
      </el-form>

      <!-- 操作按钮 -->
      <div class="editor-actions">
        <el-button size="small" @click="emit('cancel')">取消</el-button>
        <el-button type="primary" size="small" @click="handleSave">保存</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  poi: { type: Object, default: null },
  buildings: { type: Array, default: () => [] },
  background: { type: String, default: '' },
  sceneWidth: { type: Number, default: 1920 },
  sceneHeight: { type: Number, default: 1080 }
})

const emit = defineEmits(['save', 'cancel'])

const mapCanvasRef = ref(null)
let ctx = null
const placingPoi = ref(false)
const draggingPoi = ref(false)
let bgImage = null

const form = reactive({
  name: '',
  type: 'building',
  icon: 'building',
  x: 50,
  y: 50,
  buildingId: '',
  floor: '',
  popupConfig: ''
})

const iconOptions = [
  { label: '楼', value: 'building' },
  { label: '摄', value: 'camera' },
  { label: '充', value: 'charging' },
  { label: '门', value: 'entrance' },
  { label: '停', value: 'parking' },
  { label: '温', value: 'temp' },
  { label: '烟', value: 'smoke' },
  { label: '灯', value: 'light' }
]

// 初始化表单
if (props.poi) {
  Object.assign(form, {
    name: props.poi.name || '',
    type: props.poi.type || 'building',
    icon: props.poi.icon || 'building',
    x: props.poi.x || 50,
    y: props.poi.y || 50,
    buildingId: props.poi.buildingId || '',
    floor: props.poi.floor || '',
    popupConfig: props.poi.popupConfig || ''
  })
}

function initMap() {
  if (!mapCanvasRef.value) return
  const parent = mapCanvasRef.value.parentElement
  const dpr = window.devicePixelRatio || 1
  const containerW = parent.clientWidth
  const sceneW = props.sceneWidth || 1920
  const sceneH = props.sceneHeight || 1080
  // Calculate canvas display height to match scene aspect ratio
  const displayH = containerW * (sceneH / sceneW)
  // Use scene dimensions as the internal canvas resolution for correct aspect ratio
  mapCanvasRef.value.width = sceneW * dpr
  mapCanvasRef.value.height = sceneH * dpr
  mapCanvasRef.value.style.width = containerW + 'px'
  mapCanvasRef.value.style.height = displayH + 'px'
  ctx = mapCanvasRef.value.getContext('2d')
  ctx.scale(dpr, dpr)
  // Load background image if available
  if (props.background && !bgImage) {
    const img = new Image()
    img.crossOrigin = 'anonymous'
    img.onload = () => {
      bgImage = img
      drawMap(sceneW, sceneH)
    }
    img.onerror = () => {
      bgImage = null
      drawMap(sceneW, sceneH)
    }
    img.src = props.background
  } else {
    drawMap(sceneW, sceneH)
  }
}

function drawMap(w, h) {
  if (!ctx) return
  // Background
  if (bgImage) {
    ctx.drawImage(bgImage, 0, 0, w, h)
  } else {
    ctx.fillStyle = '#0A1628'
    ctx.fillRect(0, 0, w, h)
  }

  // 网格
  ctx.strokeStyle = 'rgba(64, 128, 255, 0.06)'
  ctx.lineWidth = 0.5
  for (let x = 0; x < w; x += 20) {
    ctx.beginPath(); ctx.moveTo(x, 0); ctx.lineTo(x, h); ctx.stroke()
  }
  for (let y = 0; y < h; y += 20) {
    ctx.beginPath(); ctx.moveTo(0, y); ctx.lineTo(w, y); ctx.stroke()
  }

  // 简化建筑
  const buildings = [
    { x: 0.15, y: 0.25, w: 0.18, h: 0.22, name: 'A栋' },
    { x: 0.42, y: 0.2, w: 0.16, h: 0.18, name: 'B栋' },
    { x: 0.65, y: 0.25, w: 0.2, h: 0.25, name: 'C栋' },
    { x: 0.2, y: 0.6, w: 0.14, h: 0.16, name: 'D栋' }
  ]
  buildings.forEach(b => {
    ctx.fillStyle = 'rgba(40, 70, 130, 0.4)'
    ctx.fillRect(b.x * w, b.y * h, b.w * w, b.h * h)
    ctx.strokeStyle = 'rgba(64, 128, 255, 0.3)'
    ctx.lineWidth = 0.8
    ctx.strokeRect(b.x * w, b.y * h, b.w * w, b.h * h)
    ctx.fillStyle = '#8899BB'
    ctx.font = '9px sans-serif'
    ctx.textAlign = 'center'
    ctx.fillText(b.name, b.x * w + b.w * w / 2, b.y * h + b.h * h / 2)
  })

  // POI标记
  const px = form.x / 100 * w
  const py = form.y / 100 * h
  ctx.fillStyle = '#FF4560'
  ctx.beginPath()
  ctx.arc(px, py, 6, 0, Math.PI * 2)
  ctx.fill()
  ctx.fillStyle = '#FFFFFF'
  ctx.font = 'bold 7px sans-serif'
  ctx.textAlign = 'center'
  ctx.textBaseline = 'middle'
  ctx.fillText(iconOptions.find(i => i.value === form.icon)?.label || 'P', px, py)
}

function startPlacing() {
  placingPoi.value = true
}

function onMapClick(e) {
  if (!placingPoi.value) return
  const rect = mapCanvasRef.value.getBoundingClientRect()
  const x = (e.clientX - rect.left) / rect.width * 100
  const y = (e.clientY - rect.top) / rect.height * 100
  form.x = +x.toFixed(1)
  form.y = +y.toFixed(1)
  placingPoi.value = false
  const sceneW = props.sceneWidth || 1920
  const sceneH = props.sceneHeight || 1080
  drawMap(sceneW, sceneH)
}

function onMapMouseDown(e) {
  if (placingPoi.value) return
  const rect = mapCanvasRef.value.getBoundingClientRect()
  const mx = e.clientX - rect.left
  const my = e.clientY - rect.top
  const px = form.x / 100 * rect.width
  const py = form.y / 100 * rect.height
  if (Math.sqrt((mx - px) ** 2 + (my - py) ** 2) < 10) {
    draggingPoi.value = true
  }
}

function onMapMouseMove(e) {
  if (!draggingPoi.value) return
  const rect = mapCanvasRef.value.getBoundingClientRect()
  form.x = +((e.clientX - rect.left) / rect.width * 100).toFixed(1)
  form.y = +((e.clientY - rect.top) / rect.height * 100).toFixed(1)
  const sceneW = props.sceneWidth || 1920
  const sceneH = props.sceneHeight || 1080
  drawMap(sceneW, sceneH)
}

function onMapMouseUp() {
  draggingPoi.value = false
}

function handleSave() {
  if (!form.name) { ElMessage.warning('请输入POI名称'); return }
  emit('save', { ...form })
}

onMounted(() => {
  nextTick(() => initMap())
  window.addEventListener('resize', initMap)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', initMap)
})

// 监听坐标变化重绘
import { watch } from 'vue'
watch(() => [form.x, form.y], () => {
  if (mapCanvasRef.value) {
    const sceneW = props.sceneWidth || 1920
    const sceneH = props.sceneHeight || 1080
    drawMap(sceneW, sceneH)
  }
})
</script>

<style scoped lang="scss">
.poi-editor {
  display: flex;
  flex-direction: column;
  height: 100%;

  .editor-header {
    padding: 12px 16px;
    border-bottom: 1px solid rgba(64, 128, 255, 0.15);

    .editor-title {
      font-size: 14px;
      font-weight: 600;
      color: #E0E8FF;
    }
  }

  .editor-body {
    flex: 1;
    padding: 12px 16px;
    overflow-y: auto;

    &::-webkit-scrollbar { width: 3px; }
    &::-webkit-scrollbar-thumb { background: rgba(64, 128, 255, 0.3); border-radius: 2px; }
  }

  .poi-map-area {
    position: relative;
    margin-bottom: 16px;
    border-radius: 8px;
    overflow: hidden;
    border: 1px solid rgba(64, 128, 255, 0.2);

    .map-hint {
      position: absolute;
      top: 8px;
      left: 8px;
      z-index: 10;
      display: flex;
      align-items: center;
      padding: 4px 8px;
      border-radius: 6px;
      background: rgba(16, 32, 64, 0.85);
      backdrop-filter: blur(4px);

      &.placing {
        background: rgba(254, 176, 25, 0.15);
        border: 1px solid rgba(254, 176, 25, 0.3);
      }
    }

    .poi-map-canvas {
      width: 100%;
      height: auto;
      cursor: crosshair;
    }
  }

  .poi-form {
    :deep(.el-form-item__label) {
      color: #8899BB;
    }

    :deep(.el-input__wrapper),
    :deep(.el-textarea__inner) {
      background: rgba(64, 128, 255, 0.08) !important;
      border-color: rgba(64, 128, 255, 0.2) !important;
      box-shadow: none !important;
    }

    :deep(.el-input__inner) {
      color: #E0E8FF;
    }
  }

  .icon-selector {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;

    .icon-option {
      width: 32px;
      height: 32px;
      border-radius: 6px;
      border: 1px solid rgba(64, 128, 255, 0.2);
      background: rgba(64, 128, 255, 0.05);
      color: #8899BB;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 12px;
      transition: all 0.2s;

      &:hover { background: rgba(64, 128, 255, 0.15); color: #E0E8FF; }
      &.active {
        background: rgba(64, 128, 255, 0.3);
        border-color: #4080FF;
        color: #FFFFFF;
      }
    }
  }

  .editor-actions {
    display: flex;
    justify-content: flex-end;
    gap: 8px;
    margin-top: 16px;
    padding-top: 12px;
    border-top: 1px solid rgba(64, 128, 255, 0.1);
  }
}
</style>
