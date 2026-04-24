<template>
  <main class="center-canvas">
    <div class="canvas-bg" :style="canvasBgStyle">
      <img v-if="sceneBgUrl" :src="sceneBgUrl" class="canvas-bg-img" alt="park" />
    </div>
    <!-- 建筑图标 -->
    <div
      v-for="b in buildings"
      :key="'b-' + b.id"
      class="building-marker"
      :class="{ 'has-alert': b.hasAlert }"
      :style="{ left: b.x + '%', top: b.y + '%' }"
      @click="onBuildingClick(b)"
    >
      <div class="building-icon-wrap">
        <svg width="36" height="36" viewBox="0 0 36 36">
          <rect x="2" y="8" width="32" height="26" rx="3" fill="url(#bGrad)" stroke="rgba(0,217,255,0.5)" stroke-width="1"/>
          <rect x="8" y="14" width="6" height="6" rx="1" fill="rgba(0,217,255,0.3)"/>
          <rect x="18" y="14" width="6" height="6" rx="1" fill="rgba(0,217,255,0.3)"/>
          <rect x="8" y="24" width="6" height="6" rx="1" fill="rgba(0,217,255,0.3)"/>
          <rect x="18" y="24" width="6" height="6" rx="1" fill="rgba(0,217,255,0.3)"/>
          <defs>
            <linearGradient id="bGrad" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stop-color="rgba(0,217,255,0.25)"/>
              <stop offset="100%" stop-color="rgba(0,217,255,0.05)"/>
            </linearGradient>
          </defs>
        </svg>
      </div>
      <span class="building-label">{{ b.name }}</span>
      <!-- 楼层选择弹窗 -->
      <div v-if="selectedBuilding?.id === b.id" class="floor-popup" @click.stop>
        <div class="floor-popup-title">{{ b.name }} - 选择楼层</div>
        <div class="floor-btns">
          <el-button
            v-for="f in (b.floors || 5)"
            :key="f"
            size="small"
            type="primary"
            plain
            @click="onFloorSelect(b, f)"
          >{{ f }}F</el-button>
        </div>
      </div>
    </div>
    <!-- 设备图标 -->
    <div
      v-for="d in devices"
      :key="'d-' + d.id"
      class="device-dot"
      :class="d.status"
      :style="{ left: d.x + '%', top: d.y + '%' }"
      :title="d.name + ': ' + (d.value || d.status)"
      @click="onDeviceClick(d)"
    ></div>
    <!-- 画布工具栏 -->
    <div class="canvas-toolbar">
      <el-tooltip content="刷新" placement="left">
        <el-button :icon="Refresh" circle size="small" @click="refreshCanvas" />
      </el-tooltip>
      <el-tooltip content="图层控制" placement="left">
        <el-button :icon="Grid" circle size="small" @click="layerControlVisible = true" />
      </el-tooltip>
    </div>

    <!-- 设备详情弹窗 -->
    <el-dialog
      v-model="deviceDialogVisible"
      :title="selectedDevice?.name || '设备详情'"
      width="700px"
      custom-class="device-dialog"
      :append-to-body="true"
      destroy-on-close
    >
      <div class="device-dialog-body" v-if="selectedDevice">
        <div class="dialog-section">
          <div class="dialog-section-title">基本信息</div>
          <div class="stat-grid cols-3">
            <div class="dialog-info-item" v-for="item in deviceDialogBasicInfo" :key="item.label">
              <span class="dialog-info-label">{{ item.label }}</span>
              <span class="dialog-info-value led">{{ item.value }}</span>
            </div>
          </div>
        </div>
        <div class="dialog-section">
          <div class="dialog-section-title">实时参数</div>
          <el-table :data="deviceDialogParams" size="small" class="dark-table" stripe>
            <el-table-column prop="name" label="参数名" />
            <el-table-column prop="value" label="当前值" />
            <el-table-column prop="unit" label="单位" width="80" />
            <el-table-column prop="status" label="状态" width="80">
              <template #default="{ row }">
                <span class="status-dot" :class="row.status === '正常' ? 'online' : 'alarm'"></span>
                {{ row.status }}
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="dialog-section">
          <div class="dialog-section-title">24h趋势</div>
          <div :ref="bindChartRef('deviceTrend')" class="dialog-chart-box"></div>
        </div>
        <div class="dialog-section">
          <div class="dialog-section-title">告警记录</div>
          <div class="scroll-list dialog-scroll">
            <div class="list-item" v-for="a in deviceDialogAlerts" :key="a.id">
              <span class="list-badge" :class="a.level">{{ a.levelText }}</span>
              <span class="list-content">{{ a.content }}</span>
              <span class="list-time">{{ a.time }}</span>
            </div>
            <div v-if="!deviceDialogAlerts.length" class="empty-hint">暂无告警记录</div>
          </div>
        </div>
        <div class="dialog-actions">
          <el-button type="primary" size="small" @click="handleDeviceCommand('restart')">远程重启</el-button>
          <el-button type="warning" size="small" plain @click="handleDeviceCommand('config')">参数配置</el-button>
          <el-button type="info" size="small" plain @click="handleDeviceCommand('history')">历史数据</el-button>
          <el-button size="small" @click="deviceDialogVisible = false">关闭</el-button>
        </div>
      </div>
    </el-dialog>

    <!-- 图层控制弹窗 -->
    <el-dialog
      v-model="layerControlVisible"
      title="图层控制"
      width="320px"
      custom-class="layer-dialog"
      :append-to-body="true"
    >
      <div class="layer-list">
        <div class="layer-item" v-for="layer in layerOptions" :key="layer.key">
          <el-switch v-model="layer.visible" size="small" />
          <span class="layer-label">{{ layer.label }}</span>
        </div>
      </div>
    </el-dialog>
  </main>
</template>

<script setup>
import { inject, computed, watch, nextTick } from 'vue'
import { Refresh, Grid } from '@element-plus/icons-vue'
import '../styles/cockpit-common.css'

const {
  sceneBgUrl, buildings, devices, selectedBuilding,
  layerControlVisible, deviceDialogVisible, selectedDevice, layerOptions,
  deviceDialogBasicInfo, deviceDialogParams, deviceDialogAlerts,
  onBuildingClick, onFloorSelect, onDeviceClick,
  handleDeviceCommand, refreshCanvas
} = inject('cockpitData')
const { bindChartRef, initDeviceDialogChart } = inject('cockpitCharts')

const canvasBgStyle = computed(() => ({
  backgroundImage: sceneBgUrl.value ? 'none' : 'linear-gradient(135deg, rgba(0,217,255,0.03) 0%, rgba(7,17,38,1) 50%, rgba(0,217,255,0.02) 100%)'
}))

watch(deviceDialogVisible, (val) => {
  if (val) {
    nextTick(() => initDeviceDialogChart())
  }
})
</script>

<style scoped>
.center-canvas {
  flex: 1;
  position: relative;
  margin: 12px 0;
  border-radius: var(--card-radius);
  overflow: hidden;
  border: var(--glass-border);
  background: var(--bg-panel);
}

.canvas-bg {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.canvas-bg-img { width: 100%; height: 100%; object-fit: cover; opacity: 0.8; }

/* 建筑标记 */
.building-marker {
  position: absolute;
  transform: translate(-50%, -50%);
  cursor: pointer;
  z-index: 5;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  transition: transform 0.2s;
}

.building-marker:hover { transform: translate(-50%, -50%) scale(1.1); }
.building-marker.has-alert .building-icon-wrap { animation: alertBlink 2s ease-in-out infinite; }
.building-icon-wrap { filter: drop-shadow(0 0 6px var(--accent-glow)); }

.building-label {
  font-size: 10px;
  color: var(--text-primary);
  white-space: nowrap;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.8);
  background: rgba(7, 17, 38, 0.7);
  padding: 1px 6px;
  border-radius: 3px;
}

/* 楼层弹窗 */
.floor-popup {
  position: absolute;
  top: -60px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(10, 22, 50, 0.95);
  backdrop-filter: blur(16px);
  border: var(--glass-border);
  border-radius: 8px;
  padding: 10px 12px;
  min-width: 140px;
  z-index: 20;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.6);
}

.floor-popup::after {
  content: '';
  position: absolute;
  bottom: -6px;
  left: 50%;
  transform: translateX(-50%) rotate(45deg);
  width: 10px;
  height: 10px;
  background: rgba(10, 22, 50, 0.95);
  border-right: var(--glass-border);
  border-bottom: var(--glass-border);
}

.floor-popup-title { font-size: 12px; color: var(--accent); margin-bottom: 8px; text-align: center; }

.floor-btns {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  justify-content: center;
}

.floor-btns :deep(.el-button) { min-width: 36px; font-size: 11px; }

/* 设备点 */
.device-dot {
  position: absolute;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  transform: translate(-50%, -50%);
  cursor: pointer;
  z-index: 4;
  transition: transform 0.2s, box-shadow 0.2s;
}

.device-dot:hover { transform: translate(-50%, -50%) scale(1.5); }
.device-dot.online { background: var(--success); box-shadow: 0 0 6px rgba(40, 199, 111, 0.5); }
.device-dot.offline { background: rgba(255, 255, 255, 0.2); }
.device-dot.alarm { background: var(--danger); box-shadow: 0 0 8px rgba(255, 59, 59, 0.6); animation: alertBlink 1.2s ease-in-out infinite; }

/* 画布工具栏 */
.canvas-toolbar {
  position: absolute;
  right: 12px;
  bottom: 12px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  z-index: 10;
}

.canvas-toolbar :deep(.el-button) {
  background: rgba(10, 22, 50, 0.8) !important;
  border: var(--glass-border) !important;
  color: var(--accent) !important;
  backdrop-filter: blur(8px);
}

.canvas-toolbar :deep(.el-button:hover) { background: rgba(0, 217, 255, 0.1) !important; }

/* 设备详情弹窗 */
:deep(.device-dialog) {
  background: rgba(10, 22, 50, 0.95) !important;
  backdrop-filter: blur(20px);
  border: 1px solid rgba(0, 217, 255, 0.15) !important;
  border-radius: 12px !important;
  box-shadow: 0 8px 40px rgba(0, 0, 0, 0.6), 0 0 20px rgba(0, 217, 255, 0.08) !important;
}

:deep(.device-dialog .el-dialog__header) { border-bottom: 1px solid rgba(0, 217, 255, 0.08); padding: 16px 20px; }
:deep(.device-dialog .el-dialog__title) { color: var(--accent); font-weight: 600; font-size: 15px; }
:deep(.device-dialog .el-dialog__headerbtn .el-dialog__close) { color: var(--text-secondary); }
:deep(.device-dialog .el-dialog__body) { padding: 16px 20px; color: var(--text-primary); }

.device-dialog-body { display: flex; flex-direction: column; gap: 16px; }
.dialog-section { display: flex; flex-direction: column; gap: 10px; }
.dialog-section-title { font-size: 13px; font-weight: 600; color: var(--accent); padding-left: 10px; border-left: 3px solid var(--accent); }

.dialog-info-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding: 8px 10px;
  background: rgba(0, 217, 255, 0.03);
  border-radius: 6px;
}

.dialog-info-label { font-size: 11px; color: var(--text-secondary); }
.dialog-info-value { font-size: 13px; }
.dialog-chart-box { width: 100%; height: 180px; }
.dialog-scroll { max-height: 150px; }

.dialog-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  padding-top: 10px;
  border-top: 1px solid rgba(0, 217, 255, 0.06);
}

.dialog-actions :deep(.el-button--primary) { background: rgba(0, 217, 255, 0.15); border-color: rgba(0, 217, 255, 0.3); color: var(--accent); }
.dialog-actions :deep(.el-button--primary:hover) { background: rgba(0, 217, 255, 0.25); }
.dialog-actions :deep(.el-button--warning) { background: rgba(255, 193, 7, 0.1); border-color: rgba(255, 193, 7, 0.3); color: var(--warning); }
.dialog-actions :deep(.el-button--info) { background: rgba(255, 255, 255, 0.05); border-color: rgba(255, 255, 255, 0.1); color: var(--text-secondary); }

/* 暗色表格 */
:deep(.dark-table) {
  --el-table-bg-color: transparent;
  --el-table-tr-bg-color: transparent;
  --el-table-header-bg-color: rgba(0, 217, 255, 0.05);
  --el-table-row-hover-bg-color: rgba(0, 217, 255, 0.06);
  --el-table-border-color: rgba(0, 217, 255, 0.06);
  --el-table-text-color: var(--text-primary);
  --el-table-header-text-color: var(--text-secondary);
  font-size: 12px;
}

:deep(.dark-table .el-table__row.el-table__row--striped td) { background: rgba(0, 217, 255, 0.02); }

/* 图层控制弹窗 */
:deep(.layer-dialog) {
  background: rgba(10, 22, 50, 0.95) !important;
  backdrop-filter: blur(20px);
  border: 1px solid rgba(0, 217, 255, 0.15) !important;
  border-radius: 10px !important;
}

:deep(.layer-dialog .el-dialog__title) { color: var(--accent); font-weight: 600; }
:deep(.layer-dialog .el-dialog__headerbtn .el-dialog__close) { color: var(--text-secondary); }
:deep(.layer-dialog .el-dialog__body) { color: var(--text-primary); }

.layer-list { display: flex; flex-direction: column; gap: 12px; }
.layer-item { display: flex; align-items: center; gap: 10px; }
.layer-label { font-size: 13px; color: var(--text-primary); }
.layer-item :deep(.el-switch) { --el-switch-on-color: var(--accent); }
</style>
