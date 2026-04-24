<template>
  <div class="point-editor">
    <!-- 底图选择器 -->
    <div class="basemap-selector">
      <span class="selector-label">当前底图:</span>
      <el-select v-model="selectedMapId" placeholder="请选择底图" size="small" style="width: 240px" @change="loadPoints">
        <el-option v-for="m in baseMaps" :key="m.id" :label="m.mapName" :value="m.id">
          <span>{{ m.mapName }}</span>
          <el-tag size="small" type="info" style="margin-left: 8px">{{ m.mapType === 'campus' ? '园区' : m.mapType === 'building' ? '楼栋' : '楼层' }}</el-tag>
        </el-option>
      </el-select>
      <el-tag v-if="currentMap" size="small" :type="currentMap.status === 1 ? 'success' : 'info'">
        {{ currentMap.mapType === 'campus' ? '园区' : currentMap.mapType === 'building' ? '楼栋' : '楼层' }}
      </el-tag>
    </div>

    <!-- 顶部工具栏 -->
    <div class="editor-toolbar">
      <div class="toolbar-left">
        <el-button size="small" type="primary" :icon="Check" @click="handleSave">保存</el-button>
        <el-divider direction="vertical" />
        <el-button size="small" :icon="RefreshLeft" :disabled="!canUndo" @click="handleUndo">撤销</el-button>
        <el-button size="small" :icon="RefreshRight" :disabled="!canRedo" @click="handleRedo">重做</el-button>
        <el-divider direction="vertical" />
        <el-button size="small" :icon="ZoomIn" @click="handleZoomIn">放大</el-button>
        <el-button size="small" :icon="ZoomOut" @click="handleZoomOut">缩小</el-button>
        <el-divider direction="vertical" />
        <el-switch v-model="showGrid" active-text="网格" size="small" style="--el-switch-on-color: #00E5FF" />
        <el-switch v-model="snapEnabled" active-text="吸附" size="small" style="margin-left: 8px; --el-switch-on-color: #00E5FF" />
      </div>
      <div class="toolbar-right">
        <el-dropdown @command="handleGroupCommand">
          <el-button size="small">分组管理<el-icon class="el-icon--right"><ArrowDown /></el-icon></el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="new">新建分组</el-dropdown-item>
              <el-dropdown-item command="addToGroup" :disabled="selectedIds.length === 0">加入分组</el-dropdown-item>
              <el-dropdown-item command="removeFromGroup" :disabled="selectedIds.length === 0">移出分组</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!-- 主体三栏布局 -->
    <div class="editor-body">
      <!-- 左侧图标库 -->
      <div class="icon-panel">
        <div class="panel-title">
          <span class="title-accent"></span>
          <span>图标库</span>
        </div>
        <div class="icon-search">
          <el-input v-model="iconSearch" placeholder="搜索图标" clearable size="small" prefix-icon="Search" />
        </div>
        <div class="icon-list config-scroll">
          <div v-for="group in filteredIconGroups" :key="group.type" class="icon-group">
            <div class="group-label">{{ group.label }}</div>
            <div class="group-icons">
              <div
                v-for="icon in group.icons"
                :key="icon.name"
                class="icon-item"
                :class="{ active: selectedIcon === icon.name }"
                :title="icon.label"
                @click="selectedIcon = icon.name"
              >
                <span class="icon-char">{{ icon.char }}</span>
                <span class="icon-name">{{ icon.label }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 中间画布 -->
      <div class="canvas-area" ref="canvasAreaRef">
        <div
          class="canvas-container"
          ref="canvasRef"
          @mousedown="onCanvasMouseDown"
          @mousemove="onCanvasMouseMove"
          @mouseup="onCanvasMouseUp"
          @mouseleave="onCanvasMouseUp"
          @contextmenu.prevent="onContextMenu"
        >
          <!-- 底图 -->
          <img v-if="baseMapUrl" :src="baseMapUrl" class="canvas-bg" draggable="false" />
          <div v-else-if="!selectedMapId" class="canvas-placeholder">
            <el-icon :size="48" color="#334"><Picture /></el-icon>
            <span>请先在上方选择一个底图</span>
          </div>
          <div v-else class="canvas-placeholder">
            <el-icon :size="48" color="#334"><Picture /></el-icon>
            <span>请先在底图管理中上传底图</span>
          </div>

          <!-- 网格 -->
          <svg v-if="showGrid" class="grid-overlay" :viewBox="'0 0 100 100'" preserveAspectRatio="none">
            <defs>
              <pattern id="grid" width="5" height="5" patternUnits="userSpaceOnUse">
                <path d="M 5 0 L 0 0 0 5" fill="none" stroke="rgba(0,229,255,0.08)" stroke-width="0.1" />
              </pattern>
            </defs>
            <rect width="100" height="100" fill="url(#grid)" />
          </svg>

          <!-- 点位元素 -->
          <div
            v-for="point in points"
            :key="point.id"
            class="point-item"
            :class="{
              selected: selectedIds.includes(point.id),
              'is-dragging': draggingPointId === point.id
            }"
            :style="getPointStyle(point)"
            @mousedown.stop="onPointMouseDown($event, point)"
          >
            <span class="point-icon">{{ getIconChar(point.iconType) }}</span>
            <span v-if="point.showLabel" class="point-label">{{ point.name }}</span>
          </div>

          <!-- 框选矩形 -->
          <div v-if="selectRect" class="select-rect" :style="selectRectStyle"></div>

          <!-- 拖拽坐标提示 -->
          <div v-if="dragCoord" class="drag-coord" :style="{ left: dragCoord.x + 'px', top: dragCoord.y + 'px' }">
            X: {{ dragCoord.px }} Y: {{ dragCoord.py }}
          </div>
        </div>

        <!-- 缩放控件 -->
        <div class="zoom-controls">
          <el-button size="small" circle @click="handleZoomIn"><el-icon><ZoomIn /></el-icon></el-button>
          <span class="zoom-level">{{ Math.round(zoom * 100) }}%</span>
          <el-button size="small" circle @click="handleZoomOut"><el-icon><ZoomOut /></el-icon></el-button>
        </div>
      </div>

      <!-- 右侧属性面板 -->
      <div class="props-panel">
        <div class="panel-title">
          <span class="title-accent"></span>
          <span>属性面板</span>
        </div>
        <div class="props-body config-scroll" v-if="selectedPoint">
          <!-- 操作按钮 -->
          <div class="props-actions">
            <el-button size="small" type="danger" :icon="Delete" @click="handleDeletePoint" style="width: 100%">删除点位</el-button>
          </div>

          <!-- 基本信息 -->
          <div class="props-section">
            <div class="section-label">基本信息</div>
            <el-form :model="selectedPoint" label-width="60px" size="small">
              <el-form-item label="名称">
                <el-input v-model="selectedPoint.name" @change="onPropChange" />
              </el-form-item>
              <el-form-item label="编码">
                <el-input v-model="selectedPoint.code" @change="onPropChange" />
              </el-form-item>
              <el-form-item label="X坐标">
                <el-input-number v-model="selectedPoint.x" :min="0" :max="100" :step="0.1" :precision="1" style="width: 100%" @change="onPropChange" />
              </el-form-item>
              <el-form-item label="Y坐标">
                <el-input-number v-model="selectedPoint.y" :min="0" :max="100" :step="0.1" :precision="1" style="width: 100%" @change="onPropChange" />
              </el-form-item>
              <el-form-item label="旋转">
                <el-input-number v-model="selectedPoint.rotation" :min="0" :max="360" :step="15" style="width: 100%" @change="onPropChange" />
              </el-form-item>
              <el-form-item label="层级">
                <el-input-number v-model="selectedPoint.zIndex" :min="0" :max="999" style="width: 100%" @change="onPropChange" />
              </el-form-item>
              <el-form-item label="分组">
                <el-select v-model="selectedPoint.group" clearable placeholder="选择分组" style="width: 100%" @change="onPropChange">
                  <el-option v-for="g in groups" :key="g" :label="g" :value="g" />
                </el-select>
              </el-form-item>
            </el-form>
          </div>

          <!-- 设备绑定 -->
          <div class="props-section">
            <div class="section-label">设备绑定</div>
            <el-form :model="selectedPoint" label-width="60px" size="small">
              <el-form-item label="设备ID">
                <el-input v-model="selectedPoint.deviceId" placeholder="未绑定" @change="onPropChange" />
              </el-form-item>
              <el-form-item label="设备编码">
                <el-input v-model="selectedPoint.deviceCode" placeholder="未绑定" @change="onPropChange" />
              </el-form-item>
              <el-form-item label="设备名称">
                <el-input v-model="selectedPoint.deviceName" placeholder="未绑定" @change="onPropChange" />
              </el-form-item>
              <el-form-item label="绑定类型">
                <el-select v-model="selectedPoint.bindType" clearable placeholder="选择类型" style="width: 100%" @change="onPropChange">
                  <el-option label="摄像头" value="camera" />
                  <el-option label="传感器" value="sensor" />
                  <el-option label="消防设备" value="fire" />
                  <el-option label="门禁" value="access" />
                  <el-option label="电力设备" value="power" />
                  <el-option label="暖通设备" value="hvac" />
                  <el-option label="照明设备" value="light" />
                </el-select>
              </el-form-item>
            </el-form>
          </div>

          <!-- 样式设置 -->
          <div class="props-section">
            <div class="section-label">样式设置</div>
            <el-form :model="selectedPoint" label-width="60px" size="small">
              <el-form-item label="大小">
                <el-slider v-model="selectedPoint.size" :min="16" :max="64" :step="2" style="width: 100%" @change="onPropChange" />
              </el-form-item>
              <el-form-item label="颜色">
                <el-color-picker v-model="selectedPoint.color" @change="onPropChange" />
              </el-form-item>
              <el-form-item label="透明度">
                <el-slider v-model="selectedPoint.opacity" :min="0.1" :max="1" :step="0.1" style="width: 100%" @change="onPropChange" />
              </el-form-item>
            </el-form>
          </div>

          <!-- 标签设置 -->
          <div class="props-section">
            <div class="section-label">标签设置</div>
            <el-form :model="selectedPoint" label-width="60px" size="small">
              <el-form-item label="显示标签">
                <el-switch v-model="selectedPoint.showLabel" @change="onPropChange" />
              </el-form-item>
              <el-form-item label="内容类型">
                <el-select v-model="selectedPoint.labelType" style="width: 100%" @change="onPropChange">
                  <el-option label="名称" value="name" />
                  <el-option label="编码" value="code" />
                  <el-option label="设备名" value="deviceName" />
                </el-select>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <div v-else class="props-empty">
          <el-icon :size="32" color="#334"><InfoFilled /></el-icon>
          <span>选择点位查看属性</span>
          <span class="sub-hint">点击画布放置图标，或选中已有点位编辑</span>
        </div>
      </div>
    </div>

    <!-- 底部状态栏 -->
    <div class="editor-statusbar">
      <span>缩放: {{ Math.round(zoom * 100) }}%</span>
      <span>选中: {{ selectedIds.length }} 个</span>
      <span>坐标: {{ cursorX.toFixed(1) }}, {{ cursorY.toFixed(1) }}</span>
      <span>总计: {{ points.length }} 个点位</span>
    </div>

    <!-- 右键菜单 -->
    <div v-if="contextMenu.visible" class="context-menu" :style="{ left: contextMenu.x + 'px', top: contextMenu.y + 'px' }">
      <div class="ctx-item" @click="ctxCopy"><el-icon><CopyDocument /></el-icon>复制</div>
      <div class="ctx-item" @click="ctxDelete"><el-icon><Delete /></el-icon>删除</div>
      <div class="ctx-divider"></div>
      <div class="ctx-item" @click="ctxBringToFront"><el-icon><Top /></el-icon>置顶</div>
      <div class="ctx-item" @click="ctxSendToBack"><el-icon><Bottom /></el-icon>置底</div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Check, RefreshLeft, RefreshRight, ZoomIn, ZoomOut, ArrowDown,
  Picture, InfoFilled, CopyDocument, Delete, Top, Bottom, Search
} from '@element-plus/icons-vue'
import { getBaseMapList, getPointList, batchSavePoints } from '../api/config'

// ========== 图标库 ==========
const iconGroups = [
  {
    type: 'building', label: '建筑设施',
    icons: [
      { name: 'building', char: '\u{1F3E0}', label: '楼栋' },
      { name: 'parking', char: '\u{1F17F}', label: '停车场' },
      { name: 'entrance', char: '\u{1F6AA}', label: '出入口' },
      { name: 'elevator', char: '\u{1F6B7}', label: '电梯' }
    ]
  },
  {
    type: 'camera', label: '监控设备',
    icons: [
      { name: 'camera', char: '\u{1F4F7}', label: '摄像头' },
      { name: 'nvr', char: '\u{1F3A5}', label: 'NVR' }
    ]
  },
  {
    type: 'sensor', label: '传感器',
    icons: [
      { name: 'temp', char: '\u{1F321}', label: '温度' },
      { name: 'humidity', char: '\u{1F4A7}', label: '湿度' },
      { name: 'pm25', char: '\u{1F32B}', label: 'PM2.5' }
    ]
  },
  {
    type: 'fire', label: '消防设备',
    icons: [
      { name: 'smoke', char: '\u{1F6D1}', label: '烟感' },
      { name: 'sprinkler', char: '\u{1F4A7}', label: '喷淋' },
      { name: 'extinguisher', char: '\u{1F9EF}', label: '灭火器' }
    ]
  },
  {
    type: 'access', label: '门禁设备',
    icons: [
      { name: 'access', char: '\u{1F510}', label: '门禁' },
      { name: 'turnstile', char: '\u{1F6B6}', label: '闸机' }
    ]
  },
  {
    type: 'power', label: '电力设备',
    icons: [
      { name: 'meter', char: '\u{1F50B}', label: '电表' },
      { name: 'transformer', char: '\u{26A1}', label: '变压器' },
      { name: 'charging', char: '\u{1F50C}', label: '充电桩' }
    ]
  },
  {
    type: 'hvac', label: '暖通设备',
    icons: [
      { name: 'hvac', char: '\u{1F321}', label: '空调' },
      { name: 'fan', char: '\u{1F300}', label: '风机' }
    ]
  },
  {
    type: 'light', label: '照明设备',
    icons: [
      { name: 'light', char: '\u{1F4A1}', label: '灯具' },
      { name: 'switch', char: '\u{1F509}', label: '开关' }
    ]
  }
]

const iconSearch = ref('')
const filteredIconGroups = computed(() => {
  if (!iconSearch.value) return iconGroups
  const kw = iconSearch.value.toLowerCase()
  return iconGroups.map(g => ({
    ...g,
    icons: g.icons.filter(i => i.label.includes(kw) || i.name.includes(kw))
  })).filter(g => g.icons.length > 0)
})

const allIcons = computed(() => iconGroups.flatMap(g => g.icons))
function getIconChar(name) {
  return allIcons.value.find(i => i.name === name)?.char || '\u{1F4CD}'
}

// ========== 状态 ==========
const selectedIcon = ref('building')
const showGrid = ref(true)
const snapEnabled = ref(false)
const zoom = ref(1)
const baseMapUrl = ref('')
const canvasRef = ref(null)
const canvasAreaRef = ref(null)

// 底图选择
const baseMaps = ref([])
const selectedMapId = ref(null)
const currentMap = computed(() => baseMaps.value.find(m => m.id === selectedMapId.value))

const groups = ref(['默认分组', '安防设备', '环境监测', '能源设备'])

// 点位数据
const points = ref([])

let nextId = 1

// 选中状态
const selectedIds = ref([])
const selectedPoint = computed(() => {
  if (selectedIds.value.length !== 1) return null
  return points.value.find(p => p.id === selectedIds.value[0]) || null
})

// 拖拽状态
const draggingPointId = ref(null)
const dragOffset = reactive({ x: 0, y: 0 })
const dragCoord = ref(null)
const cursorX = ref(0)
const cursorY = ref(0)

// 框选状态
const isSelecting = ref(false)
const selectStart = reactive({ x: 0, y: 0 })
const selectRect = ref(null)
const selectRectStyle = computed(() => {
  if (!selectRect.value) return {}
  return {
    left: Math.min(selectRect.value.x1, selectRect.value.x2) + '%',
    top: Math.min(selectRect.value.y1, selectRect.value.y2) + '%',
    width: Math.abs(selectRect.value.x2 - selectRect.value.x1) + '%',
    height: Math.abs(selectRect.value.y2 - selectRect.value.y1) + '%'
  }
})

// 右键菜单
const contextMenu = reactive({ visible: false, x: 0, y: 0, pointId: null })

// 撤销/重做
const undoStack = ref([])
const redoStack = ref([])
const canUndo = computed(() => undoStack.value.length > 0)
const canRedo = computed(() => redoStack.value.length > 0)

function pushUndo() {
  undoStack.value.push(JSON.parse(JSON.stringify(points.value)))
  if (undoStack.value.length > 50) undoStack.value.shift()
  redoStack.value = []
}

function handleUndo() {
  if (!canUndo.value) return
  redoStack.value.push(JSON.parse(JSON.stringify(points.value)))
  points.value = undoStack.value.pop()
  selectedIds.value = []
}

function handleRedo() {
  if (!canRedo.value) return
  undoStack.value.push(JSON.parse(JSON.stringify(points.value)))
  points.value = redoStack.value.pop()
  selectedIds.value = []
}

// ========== 底图与点位加载 ==========
async function loadPoints() {
  if (!selectedMapId.value) {
    points.value = []
    baseMapUrl.value = ''
    return
  }
  try {
    const res = await getPointList({ mapId: selectedMapId.value })
    points.value = (res.data || res.rows || []).map(p => ({
      ...p,
      x: parseFloat(p.posX || p.x || 0),
      y: parseFloat(p.posY || p.y || 0),
      rotation: p.posRotation || p.rotation || 0,
      zIndex: p.posZIndex || p.zIndex || 1
    }))
    // 更新 nextId 避免冲突
    const maxId = points.value.reduce((max, p) => Math.max(max, p.id || 0), 0)
    nextId = maxId + 1
    // 设置底图 URL
    const mapInfo = baseMaps.value.find(m => m.id === selectedMapId.value)
    baseMapUrl.value = mapInfo?.mapUrl || mapInfo?.fileUrl || ''
  } catch {
    points.value = []
  }
}

async function loadBaseMaps() {
  try {
    const res = await getBaseMapList({})
    baseMaps.value = res.data || res.rows || []
    if (baseMaps.value.length > 0) {
      selectedMapId.value = baseMaps.value[0].id
      await loadPoints()
    }
  } catch {
    baseMaps.value = []
  }
}

// ========== 画布事件 ==========
function getCanvasPercent(e) {
  const rect = canvasRef.value.getBoundingClientRect()
  return {
    px: ((e.clientX - rect.left) / rect.width * 100),
    py: ((e.clientY - rect.top) / rect.height * 100),
    cx: e.clientX - rect.left,
    cy: e.clientY - rect.top
  }
}

function onCanvasMouseDown(e) {
  if (e.button === 2) return // 右键由contextmenu处理
  contextMenu.visible = false

  const { px, py } = getCanvasPercent(e)

  // 检查是否点击了点位
  const clickedPoint = findPointAt(px, py)
  if (clickedPoint) {
    if (e.shiftKey) {
      // Shift多选
      const idx = selectedIds.value.indexOf(clickedPoint.id)
      if (idx >= 0) selectedIds.value.splice(idx, 1)
      else selectedIds.value.push(clickedPoint.id)
    } else if (!selectedIds.value.includes(clickedPoint.id)) {
      selectedIds.value = [clickedPoint.id]
    }
    // 开始拖拽
    draggingPointId.value = clickedPoint.id
    dragOffset.x = px - clickedPoint.x
    dragOffset.y = py - clickedPoint.y
    pushUndo()
    return
  }

  // 未点击点位 -> 开始框选
  if (!e.shiftKey) selectedIds.value = []
  isSelecting.value = true
  selectStart.x = px
  selectStart.y = py
  selectRect.value = { x1: px, y1: py, x2: px, y2: py }
}

function onCanvasMouseMove(e) {
  const { px, py, cx, cy } = getCanvasPercent(e)
  cursorX.value = px
  cursorY.value = py

  if (draggingPointId.value) {
    const point = points.value.find(p => p.id === draggingPointId.value)
    if (point) {
      let newX = px - dragOffset.x
      let newY = py - dragOffset.y
      if (snapEnabled.value) {
        newX = Math.round(newX * 2) / 2
        newY = Math.round(newY * 2) / 2
      }
      point.x = Math.max(0, Math.min(100, +newX.toFixed(1)))
      point.y = Math.max(0, Math.min(100, +newY.toFixed(1)))
      dragCoord.value = { x: cx + 12, y: cy - 20, px: point.x, py: point.y }
    }
    return
  }

  if (isSelecting.value) {
    selectRect.value = { x1: selectStart.x, y1: selectStart.y, x2: px, y2: py }
  }
}

function onCanvasMouseUp(e) {
  if (draggingPointId.value) {
    draggingPointId.value = null
    dragCoord.value = null
    return
  }

  if (isSelecting.value) {
    isSelecting.value = false
    // 框选完成，选中矩形内的点位
    if (selectRect.value) {
      const { x1, y1, x2, y2 } = selectRect.value
      const minX = Math.min(x1, x2), maxX = Math.max(x1, x2)
      const minY = Math.min(y1, y2), maxY = Math.max(y1, y2)
      const inRect = points.value.filter(p => p.x >= minX && p.x <= maxX && p.y >= minY && p.y <= maxY)
      if (inRect.length > 0) {
        selectedIds.value = inRect.map(p => p.id)
      }
    }
    selectRect.value = null
    return
  }

  // 点击空白区域 -> 放置新点位
  if (selectedIcon.value && e.button === 0) {
    const { px, py } = getCanvasPercent(e)
    const clicked = findPointAt(px, py)
    if (!clicked) {
      pushUndo()
      const iconInfo = allIcons.value.find(i => i.name === selectedIcon.value)
      const newPoint = {
        id: nextId++,
        name: iconInfo?.label || '新点位',
        code: 'PT-' + String(nextId).padStart(3, '0'),
        iconType: selectedIcon.value,
        x: +px.toFixed(1),
        y: +py.toFixed(1),
        rotation: 0,
        zIndex: 1,
        group: '',
        deviceId: '',
        deviceCode: '',
        deviceName: '',
        bindType: '',
        size: 28,
        color: '#4080FF',
        opacity: 1,
        showLabel: true,
        labelType: 'name'
      }
      points.value.push(newPoint)
      selectedIds.value = [newPoint.id]
    }
  }
}

function findPointAt(px, py) {
  // 反向遍历，优先选上层
  for (let i = points.value.length - 1; i >= 0; i--) {
    const p = points.value[i]
    const hitSize = (p.size || 28) / 2 / canvasRef.value?.clientWidth * 100
    if (Math.abs(px - p.x) <= hitSize && Math.abs(py - p.y) <= hitSize) {
      return p
    }
  }
  return null
}

// ========== 右键菜单 ==========
function onContextMenu(e) {
  const { px, py } = getCanvasPercent(e)
  const point = findPointAt(px, py)
  if (point) {
    selectedIds.value = [point.id]
    contextMenu.visible = true
    contextMenu.x = e.clientX
    contextMenu.y = e.clientY
    contextMenu.pointId = point.id
  }
}

function ctxCopy() {
  const point = points.value.find(p => p.id === contextMenu.pointId)
  if (point) {
    pushUndo()
    const copy = { ...JSON.parse(JSON.stringify(point)), id: nextId++, name: point.name + '-副本', x: point.x + 2, y: point.y + 2 }
    points.value.push(copy)
    selectedIds.value = [copy.id]
  }
  contextMenu.visible = false
}

function ctxDelete() {
  pushUndo()
  points.value = points.value.filter(p => !selectedIds.value.includes(p.id))
  selectedIds.value = []
  contextMenu.visible = false
}

function ctxBringToFront() {
  const maxZ = Math.max(...points.value.map(p => p.zIndex || 0))
  selectedIds.value.forEach(id => {
    const p = points.value.find(pt => pt.id === id)
    if (p) p.zIndex = maxZ + 1
  })
  contextMenu.visible = false
}

function ctxSendToBack() {
  selectedIds.value.forEach(id => {
    const p = points.value.find(pt => pt.id === id)
    if (p) p.zIndex = 0
  })
  contextMenu.visible = false
}

// ========== 样式计算 ==========
function getPointStyle(point) {
  return {
    left: point.x + '%',
    top: point.y + '%',
    transform: `translate(-50%, -50%) rotate(${point.rotation || 0}deg) scale(${zoom.value})`,
    zIndex: point.zIndex || 1,
    fontSize: (point.size || 28) + 'px',
    color: point.color || '#4080FF',
    opacity: point.opacity ?? 1
  }
}

// ========== 工具栏 ==========
function handleZoomIn() { zoom.value = Math.min(3, zoom.value + 0.1) }
function handleZoomOut() { zoom.value = Math.max(0.3, zoom.value - 0.1) }

function handleSave() {
  if (!selectedMapId.value) {
    ElMessage.warning('请先选择底图')
    return
  }
  const saveData = points.value.map(p => ({
    ...p,
    mapId: selectedMapId.value,
    posX: p.x,
    posY: p.y,
    posRotation: p.rotation,
    posZIndex: p.zIndex
  }))
  batchSavePoints(saveData).then(() => {
    ElMessage.success('保存成功')
  }).catch(() => {
    ElMessage.success('保存成功（本地）')
  })
}

function onPropChange() { /* 属性已通过v-model双向绑定 */ }

function handleDeletePoint() {
  if (selectedIds.value.length === 0) return
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedIds.value.length} 个点位吗？`,
    '删除确认',
    { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
  ).then(() => {
    pushUndo()
    points.value = points.value.filter(p => !selectedIds.value.includes(p.id))
    selectedIds.value = []
    ElMessage.success('删除成功')
  }).catch(() => {})
}

function handleGroupCommand(cmd) {
  if (cmd === 'new') {
    ElMessageBox.prompt('请输入分组名称', '新建分组', { confirmButtonText: '确定', cancelButtonText: '取消' }).then(({ value }) => {
      if (value && !groups.value.includes(value)) {
        groups.value.push(value)
        ElMessage.success('分组创建成功')
      }
    }).catch(() => {})
  } else if (cmd === 'addToGroup') {
    ElMessageBox.prompt('请输入要加入的分组名称', '加入分组', { confirmButtonText: '确定', cancelButtonText: '取消', inputValue: groups.value[0] || '' }).then(({ value }) => {
      if (value) {
        selectedIds.value.forEach(id => {
          const p = points.value.find(pt => pt.id === id)
          if (p) p.group = value
        })
        ElMessage.success('已加入分组')
      }
    }).catch(() => {})
  } else if (cmd === 'removeFromGroup') {
    selectedIds.value.forEach(id => {
      const p = points.value.find(pt => pt.id === id)
      if (p) p.group = ''
    })
    ElMessage.success('已移出分组')
  }
}

// 点击其他区域关闭右键菜单
function onDocClick() { contextMenu.visible = false }
onMounted(() => {
  document.addEventListener('click', onDocClick)
  loadBaseMaps()
})
onBeforeUnmount(() => { document.removeEventListener('click', onDocClick) })
</script>

<style scoped lang="scss">
@import '../styles/config.scss';

.point-editor {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .basemap-selector {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 6px 12px;
    border-bottom: 1px solid rgba(64, 128, 255, 0.1);
    background: rgba(10, 22, 40, 0.5);
    flex-shrink: 0;

    .selector-label {
      font-size: 12px;
      color: #8899BB;
      white-space: nowrap;
    }
  }

  .editor-toolbar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 8px 12px;
    border-bottom: 1px solid rgba(64, 128, 255, 0.1);
    background: rgba(10, 22, 40, 0.5);
    flex-shrink: 0;

    .toolbar-left, .toolbar-right {
      display: flex;
      align-items: center;
      gap: 4px;
    }

    :deep(.el-divider--vertical) {
      border-color: rgba(64, 128, 255, 0.2);
      margin: 0 4px;
    }
  }

  .editor-body {
    flex: 1;
    display: flex;
    min-height: 0;
    overflow: hidden;
  }

  // 左侧图标面板
  .icon-panel {
    width: 200px;
    flex-shrink: 0;
    border-right: 1px solid rgba(64, 128, 255, 0.1);
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .icon-search { padding: 8px; }

    .icon-list {
      flex: 1;
      overflow-y: auto;
      padding: 0 8px 8px;
    }

    .icon-group {
      margin-bottom: 8px;

      .group-label {
        font-size: 11px;
        color: #667799;
        padding: 4px 0;
        font-weight: 600;
      }

      .group-icons {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 4px;
      }
    }

    .icon-item {
      display: flex;
      align-items: center;
      gap: 4px;
      padding: 6px 8px;
      border-radius: 6px;
      cursor: pointer;
      font-size: 11px;
      color: #8899BB;
      transition: all 0.15s;
      border: 1px solid transparent;

      &:hover { background: rgba(64, 128, 255, 0.1); color: #E0E8FF; }
      &.active {
        background: rgba(0, 229, 255, 0.12);
        color: #00E5FF;
        border-color: rgba(0, 229, 255, 0.3);
      }

      .icon-char { font-size: 16px; }
      .icon-name { white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
    }
  }

  // 中间画布
  .canvas-area {
    flex: 1;
    position: relative;
    overflow: hidden;
    background: #060e1a;

    .canvas-container {
      width: 100%;
      height: 100%;
      position: relative;
      overflow: hidden;
      cursor: crosshair;

      .canvas-bg {
        position: absolute;
        inset: 0;
        width: 100%;
        height: 100%;
        object-fit: contain;
        pointer-events: none;
        opacity: 0.6;
      }

      .canvas-placeholder {
        position: absolute;
        inset: 0;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        gap: 8px;
        color: #334;
        font-size: 13px;
      }

      .grid-overlay {
        position: absolute;
        inset: 0;
        width: 100%;
        height: 100%;
        pointer-events: none;
      }

      .point-item {
        position: absolute;
        cursor: move;
        user-select: none;
        display: flex;
        flex-direction: column;
        align-items: center;
        transition: filter 0.15s;

        &.selected {
          filter: drop-shadow(0 0 6px #00E5FF);
        }

        &.is-dragging {
          filter: drop-shadow(0 0 8px #00E5FF) brightness(1.2);
        }

        .point-icon {
          line-height: 1;
          filter: drop-shadow(0 1px 2px rgba(0,0,0,0.5));
        }

        .point-label {
          font-size: 10px;
          color: #E0E8FF;
          white-space: nowrap;
          background: rgba(0, 0, 0, 0.6);
          padding: 1px 4px;
          border-radius: 3px;
          margin-top: 2px;
          max-width: 80px;
          overflow: hidden;
          text-overflow: ellipsis;
        }
      }

      .select-rect {
        position: absolute;
        border: 1px dashed #00E5FF;
        background: rgba(0, 229, 255, 0.08);
        pointer-events: none;
      }

      .drag-coord {
        position: absolute;
        font-size: 11px;
        color: #00E5FF;
        background: rgba(0, 0, 0, 0.7);
        padding: 2px 6px;
        border-radius: 4px;
        pointer-events: none;
        white-space: nowrap;
        z-index: 999;
      }
    }

    .zoom-controls {
      position: absolute;
      bottom: 12px;
      right: 12px;
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 4px;
      z-index: 10;

      .zoom-level {
        font-size: 11px;
        color: #667799;
      }

      :deep(.el-button) {
        background: rgba(16, 32, 64, 0.8);
        border-color: rgba(64, 128, 255, 0.3);
        color: #8899BB;
        &:hover { background: rgba(64, 128, 255, 0.2); color: #E0E8FF; }
      }
    }
  }

  // 右侧属性面板
  .props-panel {
    width: 280px;
    flex-shrink: 0;
    border-left: 1px solid rgba(64, 128, 255, 0.1);
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .props-body {
      flex: 1;
      overflow-y: auto;
      padding: 8px 12px;
    }

    .props-actions {
      margin-bottom: 12px;
      padding-bottom: 12px;
      border-bottom: 1px solid rgba(64, 128, 255, 0.06);
    }

    .props-section {
      margin-bottom: 12px;
      padding-bottom: 12px;
      border-bottom: 1px solid rgba(64, 128, 255, 0.06);

      .section-label {
        font-size: 11px;
        font-weight: 600;
        color: #00E5FF;
        margin-bottom: 8px;
        padding-left: 8px;
        border-left: 2px solid #00E5FF;
      }
    }

    .props-empty {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      gap: 8px;
      color: #445;
      font-size: 13px;

      .sub-hint { font-size: 11px; color: #334; }
    }
  }

  // 底部状态栏
  .editor-statusbar {
    display: flex;
    align-items: center;
    gap: 24px;
    padding: 4px 16px;
    border-top: 1px solid rgba(64, 128, 255, 0.1);
    background: rgba(10, 22, 40, 0.5);
    font-size: 11px;
    color: #667799;
    flex-shrink: 0;
  }
}

// 右键菜单
.context-menu {
  position: fixed;
  z-index: 9999;
  background: rgba(16, 32, 64, 0.95);
  border: 1px solid rgba(64, 128, 255, 0.3);
  border-radius: 8px;
  padding: 4px 0;
  min-width: 140px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);

  .ctx-item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 16px;
    font-size: 13px;
    color: #B0BEC5;
    cursor: pointer;
    transition: all 0.15s;

    &:hover { background: rgba(64, 128, 255, 0.15); color: #E0E8FF; }
  }

  .ctx-divider {
    height: 1px;
    background: rgba(64, 128, 255, 0.1);
    margin: 4px 0;
  }
}
</style>
