<template>
  <aside class="left-panel">
    <!-- ===== Tab1 园区总览 ===== -->
    <template v-if="activeTopTab === 'overview'">
      <!-- 1. 安防实时概况 -->
      <div class="data-card">
        <div class="card-header">安防实时概况</div>
        <div class="stat-grid cols-3">
          <div class="stat-item">
            <span class="stat-label">摄像头在线</span>
            <span class="stat-value led">{{ overviewData.cameraOnline }}<small>/{{ overviewData.cameraTotal }}</small></span>
          </div>
          <div class="stat-item">
            <span class="stat-label">门禁状态</span>
            <span class="stat-value led" :style="{ color: overviewData.accessNormal ? 'var(--success)' : 'var(--danger)' }">{{ overviewData.accessNormal ? '正常' : '异常' }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">布防状态</span>
            <span class="stat-value led" :style="{ color: overviewData.armed ? 'var(--success)' : 'var(--warning)' }">{{ overviewData.armed ? '已布防' : '未布防' }}</span>
          </div>
          <div class="stat-item">
            <span class="stat-label">巡检完成率</span>
            <span class="stat-value led">{{ overviewData.patrolRate }}<small>%</small></span>
          </div>
          <div class="stat-item">
            <span class="stat-label">今日访客</span>
            <span class="stat-value led">{{ overviewData.todayVisitor }}<small>人</small></span>
          </div>
          <div class="stat-item">
            <span class="stat-label">车辆进出</span>
            <span class="stat-value led">{{ overviewData.vehicleIn }}<small>/{{ overviewData.vehicleOut }}</small></span>
          </div>
        </div>
      </div>
      <!-- 2. 消防&用电状态 -->
      <div class="data-card">
        <div class="card-header">消防&用电状态</div>
        <div class="fire-electric-layout">
          <div :ref="bindChartRef('overviewFireRing')" class="chart-box-sm"></div>
          <div class="fire-electric-rows">
            <div class="fe-row" v-for="row in overviewData.electricRows" :key="row.label">
              <span class="fe-label">{{ row.label }}</span>
              <span class="fe-status" :class="row.status">{{ row.value }}</span>
            </div>
          </div>
        </div>
      </div>
      <!-- 3. 隐患趋势图表 -->
      <div class="data-card">
        <div class="card-header">隐患趋势图表</div>
        <div :ref="bindChartRef('overviewTrend')" class="chart-container"></div>
      </div>
    </template>

    <!-- ===== Tab2 安全管理 ===== -->
    <template v-if="activeTopTab === 'safety'">
      <!-- 1. 安防监测 -->
      <div class="data-card">
        <div class="card-header">安防监测</div>
        <div class="stat-grid cols-3">
          <div class="stat-item" v-for="item in safetyData.monitorItems" :key="item.label">
            <span class="stat-label">{{ item.label }}</span>
            <span class="stat-value">
              <span class="status-dot" :class="item.status"></span>
              <span class="led">{{ item.value }}</span>
            </span>
          </div>
        </div>
      </div>
      <!-- 2. 消防设备统计 -->
      <div class="data-card">
        <div class="card-header">消防设备统计</div>
        <div class="fire-stats-layout">
          <div :ref="bindChartRef('safetyFirePie')" class="chart-box-sm"></div>
          <div class="fire-stats-legend">
            <div class="legend-item" v-for="item in safetyData.fireStats" :key="item.label">
              <span class="legend-dot" :style="{ background: item.color }"></span>
              <span class="legend-label">{{ item.label }}</span>
              <span class="legend-value led">{{ item.value }}</span>
            </div>
          </div>
        </div>
      </div>
      <!-- 3. AI事件列表 -->
      <div class="data-card">
        <div class="card-header">AI事件列表</div>
        <div class="scroll-list">
          <div class="list-item" v-for="evt in safetyData.aiEvents" :key="evt.id">
            <span class="list-badge" :class="evt.level">{{ evt.levelText }}</span>
            <span class="list-content">{{ evt.content }}</span>
            <span class="list-time">{{ evt.time }}</span>
          </div>
          <div v-if="!safetyData.aiEvents.length" class="empty-hint">暂无数据</div>
        </div>
      </div>
    </template>

    <!-- ===== Tab3 能源管理 ===== -->
    <template v-if="activeTopTab === 'energy'">
      <!-- 1. 今日能耗总览 -->
      <div class="data-card">
        <div class="card-header">今日能耗总览</div>
        <div class="energy-big-numbers">
          <div class="big-num-item">
            <span class="big-num-label">用电</span>
            <span class="big-num-value led">{{ energyData.todayElec }}<small>kWh</small></span>
            <span class="big-num-compare" :class="energyData.elecCompare >= 0 ? 'up' : 'down'">
              {{ energyData.elecCompare >= 0 ? '↑' : '↓' }}{{ Math.abs(energyData.elecCompare) }}%
            </span>
          </div>
          <div class="big-num-item">
            <span class="big-num-label">用水</span>
            <span class="big-num-value led">{{ energyData.todayWater }}<small>m³</small></span>
            <span class="big-num-compare" :class="energyData.waterCompare >= 0 ? 'up' : 'down'">
              {{ energyData.waterCompare >= 0 ? '↑' : '↓' }}{{ Math.abs(energyData.waterCompare) }}%
            </span>
          </div>
          <div class="big-num-item">
            <span class="big-num-label">用气</span>
            <span class="big-num-value led">{{ energyData.todayGas }}<small>m³</small></span>
            <span class="big-num-compare" :class="energyData.gasCompare >= 0 ? 'up' : 'down'">
              {{ energyData.gasCompare >= 0 ? '↑' : '↓' }}{{ Math.abs(energyData.gasCompare) }}%
            </span>
          </div>
        </div>
      </div>
      <!-- 2. 用电用水用气数据 -->
      <div class="data-card">
        <div class="card-header">用电用水用气数据</div>
        <div class="sparkline-stack">
          <div class="sparkline-row">
            <span class="sparkline-label">用电</span>
            <div :ref="bindChartRef('energyElecSpark')" class="sparkline-box"></div>
          </div>
          <div class="sparkline-row">
            <span class="sparkline-label">用水</span>
            <div :ref="bindChartRef('energyWaterSpark')" class="sparkline-box"></div>
          </div>
          <div class="sparkline-row">
            <span class="sparkline-label">用气</span>
            <div :ref="bindChartRef('energyGasSpark')" class="sparkline-box"></div>
          </div>
        </div>
      </div>
      <!-- 3. 能耗同比分析 -->
      <div class="data-card">
        <div class="card-header">能耗同比分析</div>
        <div :ref="bindChartRef('energyCompare')" class="chart-container"></div>
      </div>
    </template>

    <!-- ===== Tab4 环境管理 ===== -->
    <template v-if="activeTopTab === 'environment'">
      <!-- 1. 大气环境数据 -->
      <div class="data-card">
        <div class="card-header">大气环境数据</div>
        <div class="stat-grid cols-3">
          <div class="stat-item" v-for="item in envData.airItems" :key="item.label">
            <span class="stat-label">{{ item.label }}</span>
            <span class="stat-value led" :style="{ color: item.color }">{{ item.value }}<small>{{ item.unit }}</small></span>
          </div>
        </div>
      </div>
      <!-- 2. 水质监测 -->
      <div class="data-card">
        <div class="card-header">水质监测</div>
        <div class="stat-grid cols-3">
          <div class="stat-item" v-for="item in envData.waterItems" :key="item.label">
            <span class="stat-label">{{ item.label }}</span>
            <span class="stat-value led" :style="{ color: item.color }">{{ item.value }}<small>{{ item.unit }}</small></span>
          </div>
        </div>
      </div>
      <!-- 3. 环境趋势曲线 -->
      <div class="data-card">
        <div class="card-header">环境趋势曲线</div>
        <div :ref="bindChartRef('envTrend')" class="chart-container"></div>
      </div>
    </template>

    <!-- ===== Tab5 资产管理 ===== -->
    <template v-if="activeTopTab === 'asset'">
      <!-- 1. 资产总览 -->
      <div class="data-card">
        <div class="card-header">资产总览</div>
        <div class="stat-grid cols-3">
          <div class="stat-item" v-for="item in assetData.overviewItems" :key="item.label">
            <span class="stat-label">{{ item.label }}</span>
            <span class="stat-value led">{{ item.value }}<small v-if="item.unit">{{ item.unit }}</small></span>
          </div>
        </div>
      </div>
      <!-- 2. 楼栋资产统计 -->
      <div class="data-card">
        <div class="card-header">楼栋资产统计</div>
        <div :ref="bindChartRef('assetBuildingBar')" class="chart-container"></div>
      </div>
      <!-- 3. 设备完好率 -->
      <div class="data-card">
        <div class="card-header">设备完好率</div>
        <div class="ring-center-layout">
          <div :ref="bindChartRef('assetHealthRing')" class="chart-box-md"></div>
          <div class="ring-center-text">
            <span class="ring-percent led">{{ assetData.healthRate }}<small>%</small></span>
            <span class="ring-label">完好率</span>
          </div>
        </div>
      </div>
    </template>
  </aside>
</template>

<script setup>
import { inject } from 'vue'
import '../styles/cockpit-common.css'

const { activeTopTab, overviewData, safetyData, energyData, envData, assetData } = inject('cockpitData')
const { bindChartRef } = inject('cockpitCharts')
</script>

<style scoped>
.left-panel {
  width: 20%;
  min-width: 280px;
  max-width: 360px;
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  overflow: hidden;
}

/* 消防用电布局 */
.fire-electric-layout {
  display: flex;
  gap: 10px;
  flex: 1;
  min-height: 0;
  align-items: center;
}

.fire-electric-rows {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
  min-height: 0;
  overflow: auto;
}

.fe-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 4px 8px;
  background: rgba(0, 217, 255, 0.02);
  border-radius: 4px;
  font-size: 11px;
}

.fe-label { color: var(--text-secondary); }

.fe-status {
  font-family: 'Roboto Mono', monospace;
  font-weight: 600;
  font-size: 11px;
}

.fe-status.normal { color: var(--success); }
.fe-status.warning { color: var(--warning); }
.fe-status.danger { color: var(--danger); animation: alertBlink 1.5s ease-in-out infinite; }

/* 消防统计布局 */
.fire-stats-layout {
  display: flex;
  gap: 10px;
  flex: 1;
  min-height: 0;
  align-items: center;
}

.fire-stats-legend {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
}

.legend-dot { width: 8px; height: 8px; border-radius: 2px; flex-shrink: 0; }
.legend-label { color: var(--text-secondary); flex: 1; }
.legend-value { font-size: 13px; }

/* 能源大数字 */
.energy-big-numbers {
  display: flex;
  justify-content: space-around;
  align-items: center;
  flex: 1;
  gap: 8px;
}

.big-num-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  flex: 1;
}

.big-num-label { font-size: 12px; color: var(--text-secondary); }
.big-num-value { font-size: 26px; }
.big-num-value small { font-size: 0.45em; }
.big-num-compare { font-size: 11px; font-family: 'Roboto Mono', monospace; }
.big-num-compare.up { color: var(--danger); }
.big-num-compare.down { color: var(--success); }

/* 迷你折线 */
.sparkline-stack {
  display: flex;
  flex-direction: column;
  gap: 6px;
  flex: 1;
  min-height: 0;
}

.sparkline-row {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
  min-height: 0;
}

.sparkline-label { font-size: 11px; color: var(--text-secondary); width: 30px; flex-shrink: 0; }
.sparkline-box { flex: 1; height: 100%; min-height: 30px; }

/* 环形中心布局 */
.ring-center-layout {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.ring-center-text {
  position: absolute;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
}

.ring-percent { font-size: 28px; }
.ring-percent small { font-size: 0.5em; }
.ring-label { font-size: 11px; color: var(--text-secondary); }
</style>
