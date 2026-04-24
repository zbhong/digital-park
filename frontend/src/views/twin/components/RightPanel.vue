<template>
  <aside class="right-panel">
    <!-- ===== Tab1 园区总览 ===== -->
    <template v-if="activeTopTab === 'overview'">
      <!-- 1. 告警总览 -->
      <div class="data-card">
        <div class="card-header">告警总览</div>
        <div class="alert-overview-grid">
          <div class="alert-overview-item critical">
            <span class="alert-ov-num led">{{ alertStats.critical }}</span>
            <span class="alert-ov-label">紧急</span>
          </div>
          <div class="alert-overview-item major">
            <span class="alert-ov-num led">{{ alertStats.major }}</span>
            <span class="alert-ov-label">重要</span>
          </div>
          <div class="alert-overview-item minor">
            <span class="alert-ov-num led">{{ alertStats.minor }}</span>
            <span class="alert-ov-label">一般</span>
          </div>
          <div class="alert-overview-item info">
            <span class="alert-ov-num led">{{ alertStats.info }}</span>
            <span class="alert-ov-label">提示</span>
          </div>
        </div>
        <div class="alert-total-row">
          <span class="alert-total-label">告警总数</span>
          <span class="alert-total-value led">{{ alertStats.total }}</span>
        </div>
      </div>
      <!-- 2. 设备全局状态 -->
      <div class="data-card">
        <div class="card-header">设备全局状态</div>
        <div class="stat-grid cols-3">
          <div class="stat-item" v-for="item in overviewData.deviceStatusItems" :key="item.label">
            <span class="stat-label">{{ item.label }}</span>
            <span class="stat-value">
              <span class="status-dot" :class="item.status"></span>
              <span class="led">{{ item.value }}</span>
            </span>
          </div>
        </div>
      </div>
      <!-- 3. 快捷操作控制 -->
      <div class="data-card">
        <div class="card-header">快捷操作控制</div>
        <div class="quick-action-grid">
          <div class="quick-action-item" @click="refreshAll">
            <el-icon :size="20"><Refresh /></el-icon>
            <span>刷新</span>
          </div>
          <div class="quick-action-item" @click="showAllBuildings = !showAllBuildings">
            <el-icon :size="20"><OfficeBuilding /></el-icon>
            <span>楼栋</span>
          </div>
          <div class="quick-action-item" @click="showAllDevices = !showAllDevices">
            <el-icon :size="20"><Monitor /></el-icon>
            <span>设备</span>
          </div>
          <div class="quick-action-item" @click="alertMuted = !alertMuted">
            <el-icon :size="20"><Bell /></el-icon>
            <span>{{ alertMuted ? '取消静音' : '告警静音' }}</span>
          </div>
          <div class="quick-action-item" @click="layerControlVisible = true">
            <el-icon :size="20"><Grid /></el-icon>
            <span>图层</span>
          </div>
          <div class="quick-action-item" @click="resetCanvasView">
            <el-icon :size="20"><Position /></el-icon>
            <span>全景</span>
          </div>
        </div>
      </div>
    </template>

    <!-- ===== Tab2 安全管理 ===== -->
    <template v-if="activeTopTab === 'safety'">
      <div class="data-card">
        <div class="card-header">告警分级统计</div>
        <div :ref="bindChartRef('safetyAlertLevel')" class="chart-container"></div>
      </div>
      <div class="data-card">
        <div class="card-header">区域告警排行</div>
        <div :ref="bindChartRef('safetyAreaRank')" class="chart-container"></div>
      </div>
      <div class="data-card">
        <div class="card-header">未处理告警清单</div>
        <div class="scroll-list">
          <div class="list-item" v-for="a in safetyData.pendingAlerts" :key="a.id">
            <span class="list-badge" :class="a.level">{{ a.levelText }}</span>
            <span class="list-content">{{ a.content }}</span>
            <span class="list-time">{{ a.time }}</span>
          </div>
          <div v-if="!safetyData.pendingAlerts.length" class="empty-hint">暂无未处理告警</div>
        </div>
      </div>
    </template>

    <!-- ===== Tab3 能源管理 ===== -->
    <template v-if="activeTopTab === 'energy'">
      <div class="data-card">
        <div class="card-header">用电综合曲线</div>
        <div :ref="bindChartRef('energyElecCurve')" class="chart-container"></div>
      </div>
      <div class="data-card">
        <div class="card-header">能源流向图</div>
        <div :ref="bindChartRef('energyFlow')" class="chart-container"></div>
      </div>
      <div class="data-card">
        <div class="card-header">光伏储能状态</div>
        <div class="stat-grid cols-2">
          <div class="stat-item">
            <span class="stat-label">光伏功率</span>
            <span class="stat-value led">{{ energyData.pvPower }}<small>kW</small></span>
          </div>
          <div class="stat-item">
            <span class="stat-label">储能SOC</span>
            <span class="stat-value led">{{ energyData.storageSoc }}<small>%</small></span>
          </div>
          <div class="stat-item">
            <span class="stat-label">充电桩在线</span>
            <span class="stat-value led">{{ energyData.chargerOnline }}<small>台</small></span>
          </div>
          <div class="stat-item">
            <span class="stat-label">今日充电量</span>
            <span class="stat-value led">{{ energyData.todayCharge }}<small>kWh</small></span>
          </div>
        </div>
      </div>
    </template>

    <!-- ===== Tab4 环境管理 ===== -->
    <template v-if="activeTopTab === 'environment'">
      <div class="data-card">
        <div class="card-header">环境告警统计</div>
        <div :ref="bindChartRef('envAlertBar')" class="chart-container"></div>
      </div>
      <div class="data-card">
        <div class="card-header">扬尘噪声数据</div>
        <div class="stat-grid cols-3">
          <div class="stat-item" v-for="item in envData.dustNoiseItems" :key="item.label">
            <span class="stat-label">{{ item.label }}</span>
            <span class="stat-value led" :style="{ color: item.color }">{{ item.value }}<small>{{ item.unit }}</small></span>
          </div>
        </div>
      </div>
      <div class="data-card">
        <div class="card-header">环境排行</div>
        <div :ref="bindChartRef('envAreaRank')" class="chart-container"></div>
      </div>
    </template>

    <!-- ===== Tab5 资产管理 ===== -->
    <template v-if="activeTopTab === 'asset'">
      <div class="data-card">
        <div class="card-header">维保任务</div>
        <div class="maintenance-bars">
          <div class="maint-bar-item" v-for="item in assetData.maintTasks" :key="item.label">
            <div class="maint-bar-header">
              <span class="maint-bar-label">{{ item.label }}</span>
              <span class="maint-bar-value led">{{ item.value }}</span>
            </div>
            <div class="maint-bar-track">
              <div class="maint-bar-fill" :style="{ width: item.percent + '%', background: item.color }"></div>
            </div>
          </div>
        </div>
      </div>
      <div class="data-card">
        <div class="card-header">资产告警列表</div>
        <div class="scroll-list">
          <div class="list-item" v-for="a in assetData.alertList" :key="a.id">
            <span class="list-badge" :class="a.level">{{ a.levelText }}</span>
            <span class="list-content">{{ a.content }}</span>
            <span class="list-time">{{ a.time }}</span>
          </div>
          <div v-if="!assetData.alertList.length" class="empty-hint">暂无资产告警</div>
        </div>
      </div>
      <div class="data-card">
        <div class="card-header">设备分类占比</div>
        <div :ref="bindChartRef('assetCategoryPie')" class="chart-container"></div>
      </div>
    </template>
  </aside>
</template>

<script setup>
import { inject } from 'vue'
import { Refresh, Bell, Monitor, Position, Grid, OfficeBuilding } from '@element-plus/icons-vue'
import '../styles/cockpit-common.css'

const {
  activeTopTab, alertStats, overviewData, safetyData, energyData, envData, assetData,
  showAllBuildings, showAllDevices, alertMuted, layerControlVisible,
  refreshAll, resetCanvasView
} = inject('cockpitData')
const { bindChartRef } = inject('cockpitCharts')
</script>

<style scoped>
.right-panel {
  width: 20%;
  min-width: 280px;
  max-width: 360px;
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  overflow: hidden;
}

/* 告警总览 */
.alert-overview-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 8px;
}

.alert-overview-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 10px 8px;
  border-radius: 6px;
  background: rgba(0, 0, 0, 0.2);
}

.alert-overview-item.critical { border: 1px solid rgba(255, 59, 59, 0.2); }
.alert-overview-item.major { border: 1px solid rgba(255, 149, 0, 0.2); }
.alert-overview-item.minor { border: 1px solid rgba(0, 217, 255, 0.15); }
.alert-overview-item.info { border: 1px solid rgba(255, 255, 255, 0.08); }

.alert-ov-num { font-size: 22px; }
.alert-overview-item.critical .alert-ov-num { color: var(--danger); text-shadow: 0 0 8px rgba(255, 59, 59, 0.4); }
.alert-overview-item.major .alert-ov-num { color: #ff9500; text-shadow: 0 0 8px rgba(255, 149, 0, 0.4); }
.alert-overview-item.minor .alert-ov-num { color: var(--accent); }
.alert-overview-item.info .alert-ov-num { color: var(--text-secondary); }
.alert-ov-label { font-size: 11px; color: var(--text-secondary); }

.alert-total-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
  padding-top: 8px;
  border-top: 1px solid rgba(0, 217, 255, 0.06);
}

.alert-total-label { font-size: 12px; color: var(--text-secondary); }
.alert-total-value { font-size: 20px; }

/* 快捷操作 */
.quick-action-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 8px;
  flex: 1;
}

.quick-action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 12px 4px;
  background: rgba(0, 217, 255, 0.03);
  border: 1px solid rgba(0, 217, 255, 0.06);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.25s ease;
  color: var(--text-secondary);
  font-size: 11px;
}

.quick-action-item:hover {
  background: rgba(0, 217, 255, 0.08);
  border-color: rgba(0, 217, 255, 0.2);
  color: var(--accent);
  transform: translateY(-1px);
}

.quick-action-item :deep(.el-icon) { color: var(--accent); }

/* 维保进度条 */
.maintenance-bars {
  display: flex;
  flex-direction: column;
  gap: 10px;
  flex: 1;
  justify-content: center;
}

.maint-bar-item { display: flex; flex-direction: column; gap: 4px; }

.maint-bar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.maint-bar-label { font-size: 12px; color: var(--text-secondary); }
.maint-bar-value { font-size: 14px; }

.maint-bar-track {
  height: 6px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 3px;
  overflow: hidden;
}

.maint-bar-fill {
  height: 100%;
  border-radius: 3px;
  transition: width 0.6s ease;
}
</style>
