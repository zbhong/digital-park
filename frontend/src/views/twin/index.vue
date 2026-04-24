<template>
  <div class="cockpit-container">
    <!-- ==================== 顶部标题栏 ==================== -->
    <header class="top-bar">
      <div class="top-left">
        <span class="logo-icon">&#9670;</span>
        <span class="top-title">银河数字园区一体化平台</span>
      </div>
      <div class="top-tabs">
        <div
          v-for="tab in topTabs"
          :key="tab.key"
          :class="['top-tab', { active: activeTopTab === tab.key }]"
          @click="handleSwitchTab(tab.key)"
        >{{ tab.label }}</div>
      </div>
      <div class="top-right">
        <span class="top-time">{{ currentTime }}</span>
      </div>
      <div class="top-bar-line"></div>
    </header>

    <!-- ==================== 主体区域 ==================== -->
    <div class="cockpit-body">
      <LeftPanel />
      <CenterCanvas />
      <RightPanel />
    </div>
  </div>
</template>

<script setup>
import { ref, provide, nextTick, watch, onMounted, onBeforeUnmount } from 'vue'
import LeftPanel from './components/LeftPanel.vue'
import CenterCanvas from './components/CenterCanvas.vue'
import RightPanel from './components/RightPanel.vue'
import { useCockpitData } from './composables/useCockpitData'
import { useCockpitCharts } from './composables/useCockpitCharts'

// ==================== 常量 ====================
const topTabs = [
  { key: 'overview', label: '园区总览' },
  { key: 'safety', label: '安全管理' },
  { key: 'energy', label: '能源管理' },
  { key: 'environment', label: '环境管理' },
  { key: 'asset', label: '资产管理' }
]

// ==================== 图表 Composable ====================
const {
  bindChartRef,
  disposeAllCharts,
  resizeAllCharts,
  initChartsForTab
} = useCockpitCharts()

// ==================== 数据 Composable ====================
const {
  currentTime, sceneBgUrl, buildings, devices, selectedBuilding,
  showAllBuildings, showAllDevices, alertMuted, layerControlVisible,
  deviceDialogVisible, selectedDevice, layerOptions,
  alertStats, overviewData, safetyData, energyData, envData, assetData,
  deviceDialogBasicInfo, deviceDialogParams, deviceDialogAlerts,
  switchTab, onBuildingClick, onFloorSelect, onDeviceClick,
  handleDeviceCommand, refreshCanvas, resetCanvasView,
  loadCanvasData, loadDataForTab, updateClock
} = useCockpitData()

// ==================== Tab 状态 ====================
const activeTopTab = ref('overview')

// ==================== 数据加载与渲染 ====================
async function loadAndRenderTab(tab) {
  disposeAllCharts()
  await loadDataForTab(tab)
  await nextTick()
  initChartsForTab(tab, { overviewData, safetyData, energyData, envData, assetData })
}

function handleSwitchTab(tab) {
  if (activeTopTab.value === tab) return
  activeTopTab.value = switchTab(tab)
}

async function refreshAll() {
  await loadAndRenderTab(activeTopTab.value)
  await loadCanvasData()
  ElMessage.success('刷新成功')
}

// ==================== Provide / Inject ====================
provide('cockpitData', {
  activeTopTab, currentTime, sceneBgUrl, buildings, devices, selectedBuilding,
  showAllBuildings, showAllDevices, alertMuted, layerControlVisible,
  deviceDialogVisible, selectedDevice, layerOptions,
  alertStats, overviewData, safetyData, energyData, envData, assetData,
  deviceDialogBasicInfo, deviceDialogParams, deviceDialogAlerts,
  onBuildingClick, onFloorSelect, onDeviceClick,
  handleDeviceCommand, refreshCanvas, resetCanvasView, refreshAll
})

provide('cockpitCharts', {
  bindChartRef,
  disposeAllCharts,
  resizeAllCharts,
  initChartsForTab
})

// ==================== Watch Tab 切换 ====================
watch(activeTopTab, (newTab) => {
  loadAndRenderTab(newTab)
})

// ==================== 定时器 ====================
let clockTimer = null
let autoRefreshTimer = null

// ==================== 生命周期 ====================
onMounted(async () => {
  updateClock()
  clockTimer = setInterval(updateClock, 1000)
  await loadCanvasData()
  await loadAndRenderTab('overview')
  autoRefreshTimer = setInterval(() => {
    loadAndRenderTab(activeTopTab.value)
  }, 60000)
  window.addEventListener('resize', resizeAllCharts)
})

onBeforeUnmount(() => {
  if (clockTimer) clearInterval(clockTimer)
  if (autoRefreshTimer) clearInterval(autoRefreshTimer)
  disposeAllCharts()
  window.removeEventListener('resize', resizeAllCharts)
})
</script>

<style scoped>
/* ==================== CSS 变量 ==================== */
:root {
  --bg-deep: #071126;
  --bg-panel: rgba(10, 22, 50, 0.8);
  --accent: #00d9ff;
  --accent-glow: rgba(0, 217, 255, 0.25);
  --success: #28c76f;
  --warning: #ffc107;
  --danger: #ff3b3b;
  --text-primary: #e0e8ff;
  --text-secondary: rgba(224, 232, 255, 0.55);
  --glass-border: 1px solid rgba(0, 217, 255, 0.12);
  --glass-shadow: 0 4px 24px rgba(0, 0, 0, 0.5);
  --card-radius: 10px;
}

/* ==================== 容器 ==================== */
.cockpit-container {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 100;
  background: #071126 !important;
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
  color: #e0e8ff;
}

/* 背景网格 */
.cockpit-container::before {
  content: '';
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(0, 217, 255, 0.015) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 217, 255, 0.015) 1px, transparent 1px);
  background-size: 50px 50px;
  pointer-events: none;
  z-index: 0;
}

.cockpit-container > * {
  position: relative;
  z-index: 1;
}

/* ==================== 顶部栏 ==================== */
.top-bar {
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  background: linear-gradient(180deg, rgba(10, 22, 50, 0.95) 0%, rgba(7, 17, 38, 0.9) 100%);
  border-bottom: 1px solid rgba(0, 217, 255, 0.08);
  flex-shrink: 0;
  position: relative;
}

.top-bar-line {
  position: absolute;
  bottom: -1px;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent 0%, var(--accent) 50%, transparent 100%);
  box-shadow: 0 0 12px var(--accent-glow), 0 0 24px rgba(0, 217, 255, 0.1);
}

.top-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo-icon {
  font-size: 22px;
  color: var(--accent);
  text-shadow: 0 0 10px var(--accent-glow);
  animation: pulseGlow 3s ease-in-out infinite;
}

.top-title {
  font-size: 18px;
  font-weight: 700;
  background: linear-gradient(90deg, var(--accent), #5ac8fa);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  letter-spacing: 2px;
}

.top-tabs {
  display: flex;
  gap: 4px;
}

.top-tab {
  padding: 8px 20px;
  font-size: 13px;
  font-weight: 500;
  color: var(--text-secondary);
  cursor: pointer;
  border-radius: 6px 6px 0 0;
  transition: all 0.3s ease;
  position: relative;
  user-select: none;
}

.top-tab:hover {
  color: var(--text-primary);
  background: rgba(0, 217, 255, 0.05);
}

.top-tab.active {
  color: var(--accent);
  background: rgba(0, 217, 255, 0.08);
  text-shadow: 0 0 8px var(--accent-glow);
}

.top-tab.active::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 20%;
  right: 20%;
  height: 2px;
  background: var(--accent);
  border-radius: 1px;
  box-shadow: 0 0 8px var(--accent-glow);
}

.top-right {
  display: flex;
  align-items: center;
}

.top-time {
  font-family: 'Roboto Mono', monospace;
  font-size: 13px;
  color: var(--accent);
  text-shadow: 0 0 6px var(--accent-glow);
  letter-spacing: 1px;
}

/* ==================== 主体布局 ==================== */
.cockpit-body {
  display: flex;
  height: calc(100% - 56px);
  gap: 0;
  overflow: hidden;
}

/* ==================== 动画 ==================== */
@keyframes pulseGlow {
  0%, 100% {
    box-shadow: 0 0 6px var(--accent-glow);
  }
  50% {
    box-shadow: 0 0 16px var(--accent-glow);
  }
}
</style>
