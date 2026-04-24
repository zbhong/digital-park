<template>
  <transition name="popup-fade">
    <div v-if="visible && device" class="device-popup glass-panel" :style="popupStyle">
      <div class="popup-header">
        <div class="popup-title">
          <span class="popup-icon">{{ currentConfig.icon }}</span>
          <span>{{ currentConfig.title }}</span>
          <el-tag :type="device.status === 'online' ? 'success' : 'danger'" size="small">
            {{ device.status === 'online' ? '在线' : '离线' }}
          </el-tag>
        </div>
        <div class="popup-actions">
          <div class="popup-btn" @click="emit('minimize')">&#9472;</div>
          <div class="popup-btn" @click="emit('close')">&#10005;</div>
        </div>
      </div>
      <div class="popup-body">
        <!-- 基础信息 -->
        <div class="popup-section">
          <div class="section-title">基础信息</div>
          <div class="info-grid">
            <div v-for="field in currentConfig.infoFields" :key="field.key" class="info-item">
              <span class="info-label">{{ field.label }}</span>
              <span class="info-value">{{ device[field.key] || device.data?.[field.key] || '-' }}</span>
            </div>
          </div>
        </div>

        <!-- 实时数据 -->
        <div class="popup-section">
          <div class="section-title">实时数据</div>
          <div class="info-grid">
            <div v-for="field in currentConfig.dataFields" :key="field.key" class="info-item">
              <span class="info-label">{{ field.label }}</span>
              <span class="info-value">
                {{ device[field.key] || device.data?.[field.key] || '-' }}
                <span v-if="field.unit" class="info-unit">{{ field.unit }}</span>
              </span>
            </div>
          </div>
        </div>

        <!-- 告警信息 -->
        <div class="popup-section" v-if="device.alerts && device.alerts.length">
          <div class="section-title">告警信息</div>
          <div class="alert-list">
            <div v-for="alert in device.alerts" :key="alert.id" class="alert-item">
              <span class="alert-dot" :class="alert.level"></span>
              <span class="alert-text">{{ alert.message }}</span>
              <span class="alert-time">{{ alert.time }}</span>
            </div>
          </div>
        </div>

        <!-- 运行曲线 -->
        <div class="popup-section">
          <div class="section-title">运行曲线</div>
          <div class="chart-container" ref="chartRef"></div>
        </div>

        <!-- 快捷操作 -->
        <div class="popup-section">
          <div class="section-title">快捷操作</div>
          <div class="control-grid">
            <div v-for="btn in currentConfig.controlButtons" :key="btn.action"
                 class="ctrl-btn" :class="{ danger: btn.danger }"
                 @click="handleControl(btn)">
              {{ btn.label }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup>
import { computed, ref, watch, onMounted, onBeforeUnmount, nextTick } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  visible: Boolean,
  device: Object,
  position: Object
})

const emit = defineEmits(['close', 'minimize', 'control'])

const chartRef = ref(null)
let chartInstance = null

// 设备类型配置
const deviceConfigs = {
  camera: {
    title: '视频监控', icon: '📹',
    infoFields: [
      { label: '设备编号', key: 'code' },
      { label: '安装位置', key: 'name' },
      { label: '分辨率', key: 'resolution' },
      { label: '帧率', key: 'frameRate' }
    ],
    dataFields: [
      { label: '带宽', key: 'bandwidth', unit: 'Mbps' },
      { label: '设备温度', key: 'temperature', unit: '°C' },
      { label: '存储状态', key: 'storage' }
    ],
    controlButtons: [
      { label: '实时播放', action: 'live_play' },
      { label: '抓拍', action: 'capture' },
      { label: '录像', action: 'record' },
      { label: 'PTZ控制', action: 'ptz_control' }
    ]
  },
  smoke: {
    title: '烟感探测器', icon: '🚨',
    infoFields: [
      { label: '设备编号', key: 'code' },
      { label: '安装位置', key: 'name' },
      { label: '设备类型', key: 'deviceType' }
    ],
    dataFields: [
      { label: '烟感浓度', key: 'smokeDensity' },
      { label: '电池电量', key: 'battery', unit: '%' },
      { label: '信号强度', key: 'signal' }
    ],
    controlButtons: [
      { label: '消音', action: 'silence' },
      { label: '复位', action: 'reset' },
      { label: '查看历史', action: 'view_history' },
      { label: '告警查看', action: 'view_alarms' }
    ]
  },
  charging: {
    title: '充电桩', icon: '🔌',
    infoFields: [
      { label: '桩编号', key: 'code' },
      { label: '安装位置', key: 'name' },
      { label: '充电类型', key: 'chargeType' }
    ],
    dataFields: [
      { label: '充电功率', key: 'power', unit: 'kW' },
      { label: '电压', key: 'voltage', unit: 'V' },
      { label: '电流', key: 'current', unit: 'A' },
      { label: '充电量', key: 'energy', unit: 'kWh' }
    ],
    controlButtons: [
      { label: '启动充电', action: 'start' },
      { label: '停止充电', action: 'stop' },
      { label: '限功率', action: 'limit_power' },
      { label: '预约充电', action: 'reserve' }
    ]
  },
  temp_humidity: {
    title: '温湿度传感器', icon: '🌡️',
    infoFields: [
      { label: '编号', key: 'code' },
      { label: '安装位置', key: 'name' }
    ],
    dataFields: [
      { label: '温度', key: 'temperature', unit: '°C' },
      { label: '湿度', key: 'humidity', unit: '%' },
      { label: 'PM2.5', key: 'pm25', unit: 'μg/m³' },
      { label: 'CO₂', key: 'co2', unit: 'ppm' }
    ],
    controlButtons: [
      { label: '校准', action: 'calibrate' },
      { label: '阈值设置', action: 'set_threshold' },
      { label: '数据导出', action: 'export_data' },
      { label: '告警查看', action: 'view_alarms' }
    ]
  },
  access_control: {
    title: '门禁', icon: '🔐',
    infoFields: [
      { label: '门编号', key: 'code' },
      { label: '安装位置', key: 'name' },
      { label: '授权人数', key: 'authCount' }
    ],
    dataFields: [
      { label: '门磁状态', key: 'doorStatus' },
      { label: '最近通行', key: 'lastPass' },
      { label: '今日通行', key: 'todayCount', unit: '次' }
    ],
    controlButtons: [
      { label: '远程开门', action: 'remote_open' },
      { label: '密码重置', action: 'password_reset' },
      { label: '授权管理', action: 'auth_manage' },
      { label: '通行记录', action: 'pass_record' }
    ]
  },
  solar_panel: {
    title: '光伏逆变器', icon: '☀️',
    infoFields: [
      { label: '设备编号', key: 'code' },
      { label: '装机容量', key: 'capacity', unit: 'kWp' },
      { label: '安装位置', key: 'name' }
    ],
    dataFields: [
      { label: '实时功率', key: 'power', unit: 'kW' },
      { label: '日发电量', key: 'dailyGen', unit: 'kWh' },
      { label: '电压', key: 'voltage', unit: 'V' },
      { label: '电流', key: 'current', unit: 'A' }
    ],
    controlButtons: [
      { label: '远程启停', action: 'remote_toggle' },
      { label: '功率调节', action: 'power_adjust' },
      { label: '刷新', action: 'refresh' },
      { label: '查看告警', action: 'view_alarms' }
    ]
  },
  storage: {
    title: '储能系统', icon: '🔋',
    infoFields: [
      { label: '设备编号', key: 'code' },
      { label: '额定功率', key: 'ratedPower', unit: 'kW' },
      { label: '额定容量', key: 'ratedCapacity', unit: 'kWh' }
    ],
    dataFields: [
      { label: 'SOC', key: 'soc', unit: '%' },
      { label: 'SOH', key: 'soh', unit: '%' },
      { label: '充放电功率', key: 'power', unit: 'kW' },
      { label: '总电压', key: 'voltage', unit: 'V' }
    ],
    controlButtons: [
      { label: '远程充放电', action: 'remote_charge' },
      { label: '暂停', action: 'pause' },
      { label: '限功率', action: 'limit_power' },
      { label: '紧急停机', action: 'emergency_stop', danger: true }
    ]
  },
  env_station: {
    title: '环境监测站', icon: '🌍',
    infoFields: [
      { label: '站点编号', key: 'code' },
      { label: '安装位置', key: 'name' },
      { label: '监测因子', key: 'factors' }
    ],
    dataFields: [
      { label: 'PM2.5', key: 'pm25', unit: 'μg/m³' },
      { label: 'PM10', key: 'pm10', unit: 'μg/m³' },
      { label: '噪声', key: 'noise', unit: 'dB' },
      { label: '风速', key: 'windSpeed', unit: 'm/s' }
    ],
    controlButtons: [
      { label: '校准', action: 'calibrate' },
      { label: '数据导出', action: 'export_data' },
      { label: '告警阈值', action: 'set_threshold' },
      { label: '查看详情', action: 'view_detail' }
    ]
  },
  street_light: {
    title: '智能路灯', icon: '🏮',
    infoFields: [
      { label: '灯杆编号', key: 'code' },
      { label: '安装位置', key: 'name' },
      { label: '额定功率', key: 'ratedPower', unit: 'W' }
    ],
    dataFields: [
      { label: '开关状态', key: 'switchStatus' },
      { label: '当前功率', key: 'power', unit: 'W' },
      { label: '亮度', key: 'brightness', unit: '%' }
    ],
    controlButtons: [
      { label: '开关', action: 'toggle_switch' },
      { label: '调光', action: 'dim' },
      { label: '定时设置', action: 'timer_setting' },
      { label: '节能模式', action: 'eco_mode' }
    ]
  },
  energy_meter: {
    title: '电表', icon: '⚡',
    infoFields: [
      { label: '表号', key: 'code' },
      { label: '安装位置', key: 'name' },
      { label: '倍率', key: 'multiplier' }
    ],
    dataFields: [
      { label: '有功功率', key: 'power', unit: 'kW' },
      { label: '电压', key: 'voltage', unit: 'V' },
      { label: '电流', key: 'current', unit: 'A' },
      { label: '日用电量', key: 'dailyEnergy', unit: 'kWh' }
    ],
    controlButtons: [
      { label: '数据导出', action: 'export_data' },
      { label: '报表生成', action: 'generate_report' },
      { label: '阈值设置', action: 'set_threshold' },
      { label: '查看详情', action: 'view_detail' }
    ]
  },
  water_meter: {
    title: '智能水表', icon: '💧',
    infoFields: [
      { label: '表号', key: 'code' },
      { label: '安装位置', key: 'name' },
      { label: '口径', key: 'caliber' }
    ],
    dataFields: [
      { label: '瞬时流量', key: 'flow', unit: 'm³/h' },
      { label: '累计用水', key: 'totalUsage', unit: 'm³' },
      { label: '水压', key: 'pressure', unit: 'MPa' }
    ],
    controlButtons: [
      { label: '阀门开关', action: 'valve_toggle' },
      { label: '阈值设置', action: 'set_threshold' },
      { label: '数据导出', action: 'export_data' },
      { label: '查看详情', action: 'view_detail' }
    ]
  },
  lighting: {
    title: '照明控制器', icon: '💡',
    infoFields: [
      { label: '回路编号', key: 'code' },
      { label: '安装位置', key: 'name' },
      { label: '灯具数量', key: 'lampCount' }
    ],
    dataFields: [
      { label: '当前功率', key: 'power', unit: 'W' },
      { label: '开关状态', key: 'switchStatus' },
      { label: '亮度', key: 'brightness', unit: '%' }
    ],
    controlButtons: [
      { label: '开关', action: 'toggle_switch' },
      { label: '调光', action: 'dim' },
      { label: '场景模式', action: 'scene_mode' },
      { label: '定时设置', action: 'timer_setting' }
    ]
  },
  hvac: {
    title: '空调控制器', icon: '❄️',
    infoFields: [
      { label: '机组编号', key: 'code' },
      { label: '安装位置', key: 'name' },
      { label: '额定功率', key: 'ratedPower', unit: 'kW' }
    ],
    dataFields: [
      { label: '送风温度', key: 'supplyTemp', unit: '°C' },
      { label: '回风温度', key: 'returnTemp', unit: '°C' },
      { label: '设定温度', key: 'setTemp', unit: '°C' },
      { label: '功率', key: 'power', unit: 'kW' }
    ],
    controlButtons: [
      { label: '开关机', action: 'power_toggle' },
      { label: '调温', action: 'temp_adjust' },
      { label: '模式切换', action: 'mode_switch' },
      { label: '节能模式', action: 'eco_mode' }
    ]
  }
}

const currentConfig = computed(() => {
  if (!props.device || !props.device.deviceType) {
    return deviceConfigs.camera
  }
  return deviceConfigs[props.device.deviceType] || deviceConfigs.camera
})

const popupStyle = computed(() => {
  if (!props.position) return {}
  const { x, y } = props.position
  const maxX = window.innerWidth - 440
  const maxY = window.innerHeight - 100
  return {
    left: `${Math.min(x, maxX)}px`,
    top: `${Math.min(y, maxY)}px`
  }
})

function generate24hData() {
  const hours = Array.from({ length: 24 }, (_, i) => `${String(i).padStart(2, '0')}:00`)
  const data = hours.map(() => Math.round(Math.random() * 100))
  return { hours, data }
}

function initChart() {
  if (!chartRef.value) return
  if (chartInstance) chartInstance.dispose()
  chartInstance = echarts.init(chartRef.value)
  const { hours, data } = generate24hData()
  chartInstance.setOption({
    backgroundColor: 'transparent',
    textStyle: { color: '#8899BB', fontSize: 11 },
    grid: { left: '10%', right: '5%', top: '10%', bottom: '15%', containLabel: true },
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(16, 32, 64, 0.9)',
      borderColor: 'rgba(64, 128, 255, 0.3)',
      textStyle: { color: '#E0E8FF', fontSize: 12 }
    },
    xAxis: {
      type: 'category',
      data: hours,
      axisLine: { lineStyle: { color: 'rgba(64, 128, 255, 0.2)' } },
      axisLabel: { color: '#667799', fontSize: 10, interval: 3 },
      splitLine: { show: false }
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      axisLabel: { color: '#667799', fontSize: 10 },
      splitLine: { lineStyle: { color: 'rgba(64, 128, 255, 0.06)' } }
    },
    series: [{
      type: 'line',
      data,
      smooth: true,
      symbol: 'none',
      lineStyle: { color: '#4080FF', width: 2 },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(64, 128, 255, 0.3)' },
          { offset: 1, color: 'rgba(64, 128, 255, 0.02)' }
        ])
      }
    }]
  })
}

function handleControl(btn) {
  emit('control', {
    action: btn.action,
    label: btn.label,
    deviceId: props.device?.id,
    deviceType: props.device?.deviceType
  })
}

watch(
  () => [props.visible, props.device],
  ([newVisible]) => {
    if (newVisible) {
      nextTick(() => initChart())
    }
  },
  { immediate: true }
)

onMounted(() => {
  window.addEventListener('resize', () => chartInstance?.resize())
})

onBeforeUnmount(() => {
  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }
})
</script>

<style scoped lang="scss">
@import '../styles/twin.scss';

.info-unit {
  font-size: 10px;
  color: #667799;
  margin-left: 2px;
}
</style>
