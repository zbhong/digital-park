import { ref, reactive, computed } from 'vue'
import {
  getTwinOverview, getTwinAlerts, getTwinAlertStatistics, getTwinSafetyStatus,
  getTwinEnvironmentStatus, getTwinEnergyFlow, getTwinIndicatorsRealtime,
  getSceneConfigByType, getBuildingViewData, getDeviceViewData
} from '@/api/twin'
import {
  getSafetyEventPage, getSafetyEventStatistics, getFireEquipmentList, getMonitorPointList
} from '@/api/safety'
import {
  getMeterList, getStorageList, getChargingList, getConsumptionAnalysis,
  getCarbonAnalysis, getCompareAnalysis, getEnergyOverview
} from '@/api/energy'
import {
  getEnvPointList, getEnvDataPage, getEnvStatisticsByTime, getEnvWarnings
} from '@/api/environment'
import {
  getAssetInfoPage, getAssetStatisticsByType, getAssetStatisticsByStatus, getMaintenancePage
} from '@/api/asset'
import {
  getWorkOrderPage, getWorkOrderStatusStatistics, getFacilityPage
} from '@/api/property'

// ==================== 工具函数 ====================
async function safeApi(fn, fallback) {
  try {
    const res = await fn()
    return res?.data || res || fallback
  } catch (e) {
    console.warn('API call failed:', e)
    return fallback
  }
}

function getLast7Days() {
  const days = []
  for (let i = 6; i >= 0; i--) {
    const d = new Date(); d.setDate(d.getDate() - i)
    days.push((d.getMonth() + 1) + '/' + d.getDate())
  }
  return days
}

function getLast24Hours() {
  const hours = []
  for (let i = 23; i >= 0; i--) hours.push(String(i).padStart(2, '0') + ':00')
  return hours
}

function getLast12Months() {
  const months = []
  for (let i = 11; i >= 0; i--) {
    const d = new Date(); d.setMonth(d.getMonth() - i)
    months.push((d.getMonth() + 1) + '月')
  }
  return months
}

// ==================== Composable ====================
export function useCockpitData() {
  // ---------- 画布状态 ----------
  const currentTime = ref('')
  const sceneBgUrl = ref('')
  const buildings = ref([])
  const devices = ref([])
  const selectedBuilding = ref(null)
  const showAllBuildings = ref(true)
  const showAllDevices = ref(true)
  const alertMuted = ref(false)
  const layerControlVisible = ref(false)
  const deviceDialogVisible = ref(false)
  const selectedDevice = ref(null)
  const layerOptions = reactive([
    { key: 'building', label: '楼栋图层', visible: true },
    { key: 'device', label: '设备图层', visible: true },
    { key: 'alert', label: '告警图层', visible: true },
    { key: 'environment', label: '环境图层', visible: true }
  ])
  const alertStats = reactive({ critical: 0, major: 0, minor: 0, info: 0, total: 0 })

  // Tab1 园区总览
  const overviewData = reactive({
    cameraOnline: 0, cameraTotal: 0, accessNormal: true, armed: true,
    patrolRate: 0, todayVisitor: 0, vehicleIn: 0, vehicleOut: 0,
    fireStats: { normal: 0, maintain: 0, abnormal: 0 },
    electricRows: [
      { label: '配电箱温度', value: '42°C', status: 'normal' },
      { label: '线缆温度', value: '38°C', status: 'normal' },
      { label: '漏电电流', value: '2mA', status: 'normal' },
      { label: '过载状态', value: '正常', status: 'normal' },
      { label: '功率因数', value: '0.95', status: 'normal' },
      { label: '用电异常', value: '无', status: 'normal' }
    ],
    trendDays: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
    trendValues: [3, 5, 2, 8, 4, 1, 6],
    deviceStatusItems: [
      { label: '变压器', value: '4/4', status: 'online' },
      { label: '配电箱', value: '12/13', status: 'online' },
      { label: '水泵', value: '6/6', status: 'online' },
      { label: '风机', value: '8/8', status: 'online' },
      { label: '消防', value: '15/16', status: 'online' },
      { label: '环境监测', value: '20/20', status: 'online' }
    ]
  })

  // Tab2 安全管理
  const safetyData = reactive({
    monitorItems: [
      { label: '摄像头', value: '48/50', status: 'online' },
      { label: '门禁', value: '12/12', status: 'online' },
      { label: '道闸', value: '4/4', status: 'online' },
      { label: '红外对射', value: '8/8', status: 'online' },
      { label: '烟感', value: '120/125', status: 'online' },
      { label: '温感', value: '60/60', status: 'online' }
    ],
    fireStats: [
      { label: '灭火器', value: 156, color: '#00d9ff' },
      { label: '消防栓', value: 42, color: '#28c76f' },
      { label: '喷淋头', value: 380, color: '#ffc107' },
      { label: '烟感器', value: 125, color: '#af52de' }
    ],
    aiEvents: [],
    pendingAlerts: []
  })

  // Tab3 能源管理
  const energyData = reactive({
    todayElec: 0, todayWater: 0, todayGas: 0,
    elecCompare: 0, waterCompare: 0, gasCompare: 0,
    elecSparkData: [], waterSparkData: [], gasSparkData: [],
    compareMonths: [], compareThisYear: [], compareLastYear: [],
    elecCurveHours: [], elecCurveData: [],
    flowSources: [], flowValues: [],
    pvPower: 0, storageSoc: 0, chargerOnline: 0, todayCharge: 0
  })

  // Tab4 环境管理
  const envData = reactive({
    airItems: [
      { label: 'PM2.5', value: 35, unit: 'μg/m³', color: '#28c76f' },
      { label: 'PM10', value: 58, unit: 'μg/m³', color: '#28c76f' },
      { label: 'AQI', value: 72, unit: '', color: '#ffc107' },
      { label: 'SO2', value: 12, unit: 'μg/m³', color: '#28c76f' },
      { label: 'NO2', value: 28, unit: 'μg/m³', color: '#28c76f' },
      { label: 'O3', value: 85, unit: 'μg/m³', color: '#ffc107' }
    ],
    waterItems: [
      { label: 'pH', value: 7.2, unit: '', color: '#28c76f' },
      { label: 'DO', value: 6.8, unit: 'mg/L', color: '#28c76f' },
      { label: '浊度', value: 3.5, unit: 'NTU', color: '#28c76f' },
      { label: 'COD', value: 18, unit: 'mg/L', color: '#28c76f' },
      { label: '氨氮', value: 0.8, unit: 'mg/L', color: '#28c76f' },
      { label: '总磷', value: 0.15, unit: 'mg/L', color: '#28c76f' }
    ],
    trendTimes: [], trendSeries: [],
    alertTypes: [], alertCounts: [],
    dustNoiseItems: [
      { label: 'TSP', value: 120, unit: 'μg/m³', color: '#ffc107' },
      { label: '噪声', value: 62, unit: 'dB', color: '#28c76f' },
      { label: '风速', value: 2.3, unit: 'm/s', color: '#00d9ff' },
      { label: '风向', value: '东南', unit: '', color: '#00d9ff' },
      { label: '温度', value: 26.5, unit: '°C', color: '#28c76f' },
      { label: '湿度', value: 65, unit: '%', color: '#28c76f' }
    ],
    areaRankNames: [], areaRankValues: []
  })

  // Tab5 资产管理
  const assetData = reactive({
    overviewItems: [
      { label: '总资产', value: 1286, unit: '件' },
      { label: '在用', value: 1050, unit: '件' },
      { label: '闲置', value: 120, unit: '件' },
      { label: '维修', value: 56, unit: '件' },
      { label: '报废', value: 60, unit: '件' },
      { label: '总值', value: '3860', unit: '万' }
    ],
    buildingNames: [], buildingValues: [],
    healthRate: 0,
    maintTasks: [
      { label: '待维保', value: 12, percent: 24, color: '#ffc107' },
      { label: '进行中', value: 8, percent: 16, color: '#00d9ff' },
      { label: '已完成', value: 28, percent: 56, color: '#28c76f' },
      { label: '逾期', value: 2, percent: 4, color: '#ff3b3b' }
    ],
    alertList: [],
    categoryNames: [], categoryValues: []
  })

  // 设备弹窗
  const deviceDialogBasicInfo = computed(() => {
    if (!selectedDevice.value) return []
    const d = selectedDevice.value
    return [
      { label: '设备名称', value: d.name || '--' },
      { label: '设备类型', value: d.type || '--' },
      { label: '所属楼栋', value: d.building || '--' },
      { label: '安装位置', value: d.location || '--' },
      { label: '设备状态', value: d.status || '--' },
      { label: '最后上报', value: d.lastReport || '--' }
    ]
  })
  const deviceDialogParams = ref([])
  const deviceDialogAlerts = ref([])

  // ==================== 数据加载 ====================
  async function loadOverviewData() {
    const overview = await safeApi(() => getTwinOverview(), {})
    if (overview) {
      overviewData.cameraOnline = overview.cameraOnline || 48
      overviewData.cameraTotal = overview.cameraTotal || 50
      overviewData.accessNormal = overview.accessNormal !== false
      overviewData.armed = overview.armed !== false
      overviewData.patrolRate = overview.patrolRate || 92
      overviewData.todayVisitor = overview.todayVisitor || 36
      overviewData.vehicleIn = overview.vehicleIn || 128
      overviewData.vehicleOut = overview.vehicleOut || 115
    }
    const alertRes = await safeApi(() => getTwinAlertStatistics(), {})
    if (alertRes) {
      alertStats.critical = alertRes.critical || 0
      alertStats.major = alertRes.major || 0
      alertStats.minor = alertRes.minor || 0
      alertStats.info = alertRes.info || 0
      alertStats.total = alertStats.critical + alertStats.major + alertStats.minor + alertStats.info
    }
    const safetyStatus = await safeApi(() => getTwinSafetyStatus(), {})
    if (safetyStatus) {
      overviewData.fireStats = { normal: safetyStatus.fireNormal || 85, maintain: safetyStatus.fireMaintain || 10, abnormal: safetyStatus.fireAbnormal || 5 }
      if (safetyStatus.electricRows) overviewData.electricRows = safetyStatus.electricRows
    }
    overviewData.trendDays = getLast7Days()
    overviewData.trendValues = overviewData.trendDays.map(() => Math.floor(Math.random() * 10) + 1)
  }

  async function loadSafetyData() {
    const [eventsRes, fireRes] = await Promise.all([
      safeApi(() => getSafetyEventPage({ pageNum: 1, pageSize: 20 }), {}),
      safeApi(() => getFireEquipmentList({}), {})
    ])
    if (eventsRes?.records) {
      safetyData.aiEvents = eventsRes.records.slice(0, 10).map(e => ({
        id: e.id, level: e.level || 'minor', levelText: e.levelText || '一般',
        content: e.eventName || e.description || '--', time: e.createTime?.slice(5, 16) || '--'
      }))
      safetyData.pendingAlerts = eventsRes.records.filter(e => e.status !== 'handled').slice(0, 10).map(e => ({
        id: e.id, level: e.level || 'minor', levelText: e.levelText || '一般',
        content: e.eventName || e.description || '--', time: e.createTime?.slice(5, 16) || '--'
      }))
    }
    if (fireRes) {
      const list = Array.isArray(fireRes) ? fireRes : fireRes.records || []
      safetyData.fireStats = [
        { label: '灭火器', value: list.filter(f => f.type === '灭火器').length || 156, color: '#00d9ff' },
        { label: '消防栓', value: list.filter(f => f.type === '消防栓').length || 42, color: '#28c76f' },
        { label: '喷淋头', value: list.filter(f => f.type === '喷淋头').length || 380, color: '#ffc107' },
        { label: '烟感器', value: list.filter(f => f.type === '烟感器').length || 125, color: '#af52de' }
      ]
    }
  }

  async function loadEnergyData() {
    const [overviewRes] = await Promise.all([
      safeApi(() => getEnergyOverview(), {}),
      safeApi(() => getCompareAnalysis({ period: 'month' }), {})
    ])
    if (overviewRes) {
      energyData.todayElec = overviewRes.todayElec || 12580
      energyData.todayWater = overviewRes.todayWater || 356
      energyData.todayGas = overviewRes.todayGas || 128
      energyData.elecCompare = overviewRes.elecCompare || -5.2
      energyData.waterCompare = overviewRes.waterCompare || 3.1
      energyData.gasCompare = overviewRes.gasCompare || -1.8
      energyData.pvPower = overviewRes.pvPower || 245
      energyData.storageSoc = overviewRes.storageSoc || 78
      energyData.chargerOnline = overviewRes.chargerOnline || 8
      energyData.todayCharge = overviewRes.todayCharge || 680
    }
    energyData.elecSparkData = getLast24Hours().map(() => Math.floor(Math.random() * 500) + 300)
    energyData.waterSparkData = getLast24Hours().map(() => Math.floor(Math.random() * 20) + 10)
    energyData.gasSparkData = getLast24Hours().map(() => Math.floor(Math.random() * 10) + 5)
    energyData.compareMonths = getLast12Months()
    energyData.compareThisYear = energyData.compareMonths.map(() => Math.floor(Math.random() * 5000) + 8000)
    energyData.compareLastYear = energyData.compareMonths.map(() => Math.floor(Math.random() * 5000) + 7500)
    energyData.elecCurveHours = getLast24Hours()
    energyData.elecCurveData = energyData.elecCurveHours.map(() => Math.floor(Math.random() * 400) + 200)
    energyData.flowSources = ['市电', '光伏', '储能', '其他']
    energyData.flowValues = [65, 20, 10, 5]
  }

  async function loadEnvironmentData() {
    const [envStatus, warnings] = await Promise.all([
      safeApi(() => getTwinEnvironmentStatus(), {}),
      safeApi(() => getEnvWarnings({ pageNum: 1, pageSize: 20 }), {})
    ])
    if (envStatus) {
      if (envStatus.airItems) envData.airItems = envStatus.airItems
      if (envStatus.waterItems) envData.waterItems = envStatus.waterItems
      if (envStatus.dustNoiseItems) envData.dustNoiseItems = envStatus.dustNoiseItems
    }
    envData.trendTimes = getLast24Hours()
    envData.trendSeries = [
      { name: 'PM2.5', data: envData.trendTimes.map(() => Math.floor(Math.random() * 40) + 20) },
      { name: 'PM10', data: envData.trendTimes.map(() => Math.floor(Math.random() * 50) + 30) },
      { name: 'AQI', data: envData.trendTimes.map(() => Math.floor(Math.random() * 60) + 40) }
    ]
    if (warnings?.records) {
      const typeMap = {}
      warnings.records.forEach(w => { const t = w.indicatorName || w.type || '其他'; typeMap[t] = (typeMap[t] || 0) + 1 })
      envData.alertTypes = Object.keys(typeMap)
      envData.alertCounts = Object.values(typeMap)
    } else {
      envData.alertTypes = ['PM2.5', 'PM10', '噪声', '水质', '扬尘']
      envData.alertCounts = [5, 3, 8, 2, 4]
    }
    envData.areaRankNames = ['A栋', 'B栋', 'C栋', 'D栋', 'E栋']
    envData.areaRankValues = [85, 72, 68, 55, 42]
  }

  async function loadAssetData() {
    const [typeRes, statusRes, maintRes] = await Promise.all([
      safeApi(() => getAssetStatisticsByType(), {}),
      safeApi(() => getAssetStatisticsByStatus(), {}),
      safeApi(() => getMaintenancePage({ pageNum: 1, pageSize: 50 }), {})
    ])
    if (typeRes) {
      const items = Array.isArray(typeRes) ? typeRes : typeRes.records || []
      if (items.length) {
        assetData.categoryNames = items.map(i => i.categoryName || i.name)
        assetData.categoryValues = items.map(i => i.count || i.value)
      }
    }
    if (statusRes) {
      const items = Array.isArray(statusRes) ? statusRes : statusRes.records || []
      if (items.length) {
        assetData.overviewItems = [
          { label: '总资产', value: items.reduce((s, i) => s + (i.count || 0), 0), unit: '件' },
          { label: '在用', value: items.find(i => i.status === '在用')?.count || 1050, unit: '件' },
          { label: '闲置', value: items.find(i => i.status === '闲置')?.count || 120, unit: '件' },
          { label: '维修', value: items.find(i => i.status === '维修')?.count || 56, unit: '件' },
          { label: '报废', value: items.find(i => i.status === '报废')?.count || 60, unit: '件' },
          { label: '总值', value: '3860', unit: '万' }
        ]
      }
    }
    if (maintRes?.records) {
      const r = maintRes.records
      assetData.maintTasks = [
        { label: '待维保', value: r.filter(x => x.status === '待维保').length || 12, percent: 24, color: '#ffc107' },
        { label: '进行中', value: r.filter(x => x.status === '进行中').length || 8, percent: 16, color: '#00d9ff' },
        { label: '已完成', value: r.filter(x => x.status === '已完成').length || 28, percent: 56, color: '#28c76f' },
        { label: '逾期', value: r.filter(x => x.status === '逾期').length || 2, percent: 4, color: '#ff3b3b' }
      ]
    }
    assetData.buildingNames = ['A栋', 'B栋', 'C栋', 'D栋', 'E栋', 'F栋']
    assetData.buildingValues = [320, 280, 210, 195, 160, 121]
    assetData.healthRate = 96.8
    assetData.alertList = [
      { id: 1, level: 'major', levelText: '重要', content: 'A栋3F空调机组异常', time: '04-22 09:15' },
      { id: 2, level: 'minor', levelText: '一般', content: 'B栋电梯维保即将到期', time: '04-22 08:30' },
      { id: 3, level: 'critical', levelText: '紧急', content: 'C栋配电室温度过高', time: '04-21 23:45' }
    ]
  }

  async function loadCanvasData() {
    const [sceneRes, buildingRes, deviceRes] = await Promise.all([
      safeApi(() => getSceneConfigByType('park'), {}),
      safeApi(() => getBuildingViewData(), []),
      safeApi(() => getDeviceViewData(), [])
    ])
    if (sceneRes?.bgImage) sceneBgUrl.value = sceneRes.bgImage
    if (Array.isArray(buildingRes) && buildingRes.length) {
      buildings.value = buildingRes.map(b => ({
        id: b.id, name: b.name || '未命名',
        x: b.x ?? Math.random() * 60 + 10, y: b.y ?? Math.random() * 60 + 10,
        floors: b.floors || 5, hasAlert: b.hasAlert || false
      }))
    } else {
      buildings.value = [
        { id: 1, name: 'A栋研发楼', x: 20, y: 25, floors: 6, hasAlert: false },
        { id: 2, name: 'B栋办公楼', x: 50, y: 20, floors: 5, hasAlert: false },
        { id: 3, name: 'C栋生产楼', x: 75, y: 30, floors: 4, hasAlert: true },
        { id: 4, name: 'D栋仓储', x: 30, y: 60, floors: 2, hasAlert: false },
        { id: 5, name: 'E栋综合楼', x: 60, y: 65, floors: 3, hasAlert: false }
      ]
    }
    if (Array.isArray(deviceRes) && deviceRes.length) {
      devices.value = deviceRes.map(d => ({
        id: d.id, name: d.name || '设备', type: d.deviceType || d.type || 'sensor',
        x: d.x ?? Math.random() * 80 + 5, y: d.y ?? Math.random() * 80 + 5,
        status: d.status === 'alarm' ? 'alarm' : d.status === 'online' ? 'online' : 'offline',
        value: d.value || '', building: d.buildingName || '--',
        location: d.location || '--', lastReport: d.lastReport || '--'
      }))
    } else {
      devices.value = [
        { id: 1, name: '温度传感器-01', type: 'temperature', x: 22, y: 28, status: 'online', value: '25.6°C', building: 'A栋', location: '1F大厅', lastReport: '2024-04-22 10:30' },
        { id: 2, name: '烟感-03', type: 'smoke', x: 52, y: 23, status: 'online', value: '正常', building: 'B栋', location: '2F走廊', lastReport: '2024-04-22 10:29' },
        { id: 3, name: '配电监测-02', type: 'power', x: 77, y: 33, status: 'alarm', value: '温度过高', building: 'C栋', location: '1F配电室', lastReport: '2024-04-22 10:28' },
        { id: 4, name: '水压传感器-01', type: 'water', x: 32, y: 63, status: 'online', value: '0.35MPa', building: 'D栋', location: 'B1水泵房', lastReport: '2024-04-22 10:27' },
        { id: 5, name: '摄像头-12', type: 'camera', x: 62, y: 68, status: 'offline', value: '离线', building: 'E栋', location: '1F入口', lastReport: '2024-04-21 18:00' }
      ]
    }
  }

  // ==================== 事件处理 ====================
  function switchTab(tab) { selectedBuilding.value = null; return tab }

  function onBuildingClick(b) { selectedBuilding.value = selectedBuilding.value?.id === b.id ? null : b }

  function onFloorSelect(building, floor) { selectedBuilding.value = null; ElMessage.info('进入 ' + building.name + ' ' + floor + 'F') }

  function onDeviceClick(d) {
    selectedDevice.value = d
    deviceDialogParams.value = [
      { name: '温度', value: '25.6', unit: '°C', status: '正常' },
      { name: '湿度', value: '65.2', unit: '%', status: '正常' },
      { name: '电压', value: '220.5', unit: 'V', status: '正常' },
      { name: '电流', value: '3.2', unit: 'A', status: '正常' },
      { name: '功率', value: '705.6', unit: 'W', status: '正常' }
    ]
    deviceDialogAlerts.value = [
      { id: 1, level: 'major', levelText: '重要', content: '设备温度偏高预警', time: '04-22 09:30' },
      { id: 2, level: 'minor', levelText: '一般', content: '设备固件待升级', time: '04-20 14:00' }
    ]
    deviceDialogVisible.value = true
  }

  function handleDeviceCommand(cmd) { ElMessage.info('执行操作: ' + cmd) }
  function refreshCanvas() { loadCanvasData() }
  function resetCanvasView() { selectedBuilding.value = null; showAllBuildings.value = true; showAllDevices.value = true }

  function updateClock() {
    const now = new Date()
    const pad = (n) => String(n).padStart(2, '0')
    const weekDays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
    currentTime.value = now.getFullYear() + '-' + pad(now.getMonth() + 1) + '-' + pad(now.getDate()) + ' ' +
      pad(now.getHours()) + ':' + pad(now.getMinutes()) + ':' + pad(now.getSeconds()) + ' ' + weekDays[now.getDay()]
  }

  const loadDataMap = { overview: loadOverviewData, safety: loadSafetyData, energy: loadEnergyData, environment: loadEnvironmentData, asset: loadAssetData }
  function loadDataForTab(tab) { return loadDataMap[tab] ? loadDataMap[tab]() : Promise.resolve() }

  return {
    currentTime, sceneBgUrl, buildings, devices, selectedBuilding,
    showAllBuildings, showAllDevices, alertMuted, layerControlVisible,
    deviceDialogVisible, selectedDevice, layerOptions,
    alertStats, overviewData, safetyData, energyData, envData, assetData,
    deviceDialogBasicInfo, deviceDialogParams, deviceDialogAlerts,
    switchTab, onBuildingClick, onFloorSelect, onDeviceClick,
    handleDeviceCommand, refreshCanvas, resetCanvasView,
    loadCanvasData, loadDataForTab, updateClock
  }
}
