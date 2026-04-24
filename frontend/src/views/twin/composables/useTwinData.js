import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import { getTwinOverview, getTwinAlerts, getTwinAlertStatistics, getBuildingPage, getDevicePoiPage } from '@/api/twin'

// ==================== 公共工具函数 ====================

/** 生成24小时时间标签 */
function get24hLabels() {
  return Array.from({ length: 24 }, (_, i) => `${String(i).padStart(2, '0')}:00`)
}

/** 生成7天日期标签 */
function get7dLabels() {
  const days = ['日', '一', '二', '三', '四', '五', '六']
  const now = new Date()
  return Array.from({ length: 7 }, (_, i) => {
    const d = new Date(now)
    d.setDate(d.getDate() - 6 + i)
    return `${d.getMonth() + 1}/${d.getDate()} 周${days[d.getDay()]}`
  })
}

/** 生成随机数据（fallback 用） */
function randomData(count, min, max) {
  return Array.from({ length: count }, () => +(min + Math.random() * (max - min)).toFixed(1))
}

// ==================== 后端数据获取与缓存 ====================

let cachedOverviewData = null
let overviewFetchPromise = null
let refreshTimer = null

/**
 * 获取总览数据（带缓存，避免重复请求）
 * @returns {Promise<object|null>}
 */
async function fetchOverviewData() {
  if (cachedOverviewData) return cachedOverviewData
  if (overviewFetchPromise) return overviewFetchPromise
  overviewFetchPromise = (async () => {
    try {
      const res = await getTwinOverview()
      if (res.code === 200 && res.data) {
        cachedOverviewData = res.data
        return res.data
      }
    } catch (e) {
      console.warn('[useTwinData] 获取总览数据失败，使用默认值', e)
    }
    return null
  })()
  const result = await overviewFetchPromise
  overviewFetchPromise = null
  return result
}

/** 刷新缓存 */
export function refreshOverviewCache() {
  cachedOverviewData = null
}

/** 重新加载总览数据并通知所有 composable */
export async function reloadOverviewData() {
  cachedOverviewData = null
  const data = await fetchOverviewData()
  reloadCallbacks.forEach(cb => cb(data))
}

// 注册 reload 回调
const reloadCallbacks = []
function onReload(callback) {
  reloadCallbacks.push(callback)
}

// ==================== 园区概览数据 ====================
export function useParkOverviewData() {
  const kpi = reactive({
    enterpriseCount: 0,
    onlineDevices: 0,
    todayEnergy: 0,
    alertCount: 0,
    parkingUsed: 0,
    parkingTotal: 0,
    peopleCount: 0,
    greenRate: 0
  })

  const safety = reactive({
    todayEvents: 0,
    criticalAlerts: 0,
    majorAlerts: 0,
    minorAlerts: 0,
    infoAlerts: 0,
    topAlerts: []
  })

  const energy = reactive({
    solarPower: 0,
    dailyGen: 0,
    dailyConsume: 0,
    storageSoc: 0,
    peakLoad: 0,
    valleyLoad: 0,
    carbonEmission: 0,
    carbonReduction: 0
  })

  const environment = reactive({
    aqi: 0,
    aqiLevel: '',
    pm25: 0,
    pm10: 0,
    temperature: 0,
    humidity: 0,
    noise: 0,
    waterQuality: ''
  })

  const equipment = reactive({
    totalDevices: 0,
    onlineRate: 0,
    maintenanceCount: 0,
    workOrderCount: 0,
    workOrderPending: 0
  })

  const energyTrend = ref(randomData(24, 100, 400))
  const alertTrend = ref(randomData(7, 0, 20))
  const loaded = ref(false)

  function applyData(overview) {
    if (!overview) {
      // fallback 默认值
      kpi.enterpriseCount = 86
      kpi.onlineDevices = 1247
      kpi.todayEnergy = 3256
      kpi.alertCount = 23
      kpi.parkingUsed = 458
      kpi.parkingTotal = 680
      kpi.peopleCount = 2156
      kpi.greenRate = 42.5

      safety.todayEvents = 12
      safety.criticalAlerts = 2
      safety.majorAlerts = 5
      safety.minorAlerts = 16
      safety.infoAlerts = 45
      safety.topAlerts = [
        { name: 'A栋变压器温度告警', level: 'critical', time: '14:32' },
        { name: 'B栋消防通道堵塞', level: 'major', time: '13:15' },
        { name: '充电桩#3过流保护', level: 'major', time: '12:48' },
        { name: 'C栋空调制冷不足', level: 'minor', time: '11:20' },
        { name: 'D栋烟感探测器离线', level: 'minor', time: '10:05' }
      ]

      energy.solarPower = 156
      energy.dailyGen = 1248
      energy.dailyConsume = 3200
      energy.storageSoc = 78
      energy.peakLoad = 2800
      energy.valleyLoad = 800
      energy.carbonEmission = 823.6
      energy.carbonReduction = 156.8

      environment.aqi = 52
      environment.aqiLevel = '良'
      environment.pm25 = 35
      environment.pm10 = 68
      environment.temperature = 26.5
      environment.humidity = 65
      environment.noise = 45
      environment.waterQuality = 'II类'

      equipment.totalDevices = 1280
      equipment.onlineRate = 97.4
      equipment.maintenanceCount = 8
      equipment.workOrderCount = 32
      equipment.workOrderPending = 5

      energyTrend.value = randomData(24, 100, 400)
      alertTrend.value = randomData(7, 0, 20)
      return
    }

    // 从 overview 映射数据
    const kpiData = overview.kpi || {}
    kpi.enterpriseCount = kpiData.enterpriseCount ?? 0
    kpi.onlineDevices = kpiData.onlineDevices ?? 0
    kpi.todayEnergy = kpiData.todayEnergy ?? 0
    kpi.alertCount = kpiData.alertCount ?? 0
    kpi.parkingUsed = kpiData.parkingUsed ?? 0
    kpi.parkingTotal = kpiData.parkingTotal ?? 0
    kpi.peopleCount = kpiData.peopleCount ?? 0
    kpi.greenRate = kpiData.greenRate ?? 0

    const safetyData = overview.safety || {}
    safety.todayEvents = safetyData.todayEvents ?? 0
    safety.criticalAlerts = safetyData.criticalAlerts ?? 0
    safety.majorAlerts = safetyData.majorAlerts ?? 0
    safety.minorAlerts = safetyData.minorAlerts ?? 0
    safety.infoAlerts = safetyData.infoAlerts ?? 0
    safety.topAlerts = safetyData.topAlerts || []

    const energyData = overview.energy || {}
    energy.solarPower = energyData.solarPower ?? 0
    energy.dailyGen = energyData.dailyGen ?? 0
    energy.dailyConsume = energyData.dailyConsume ?? 0
    energy.storageSoc = energyData.storageSoc ?? 0
    energy.peakLoad = energyData.peakLoad ?? 0
    energy.valleyLoad = energyData.valleyLoad ?? 0
    energy.carbonEmission = energyData.carbonEmission ?? 0
    energy.carbonReduction = energyData.carbonReduction ?? 0

    const envData = overview.environment || {}
    environment.aqi = envData.aqi ?? 0
    environment.aqiLevel = envData.aqiLevel || ''
    environment.pm25 = envData.pm25 ?? 0
    environment.pm10 = envData.pm10 ?? 0
    environment.temperature = envData.temperature ?? 0
    environment.humidity = envData.humidity ?? 0
    environment.noise = envData.noise ?? 0
    environment.waterQuality = envData.waterQuality || ''

    const equipData = overview.equipment || {}
    equipment.totalDevices = equipData.totalDevices ?? 0
    equipment.onlineRate = equipData.onlineRate ?? 0
    equipment.maintenanceCount = equipData.maintenanceCount ?? 0
    equipment.workOrderCount = equipData.workOrderCount ?? 0
    equipment.workOrderPending = equipData.workOrderPending ?? 0

    if (overview.energyTrend && overview.energyTrend.length) {
      energyTrend.value = overview.energyTrend
    }
    if (overview.alertTrend && overview.alertTrend.length) {
      alertTrend.value = overview.alertTrend
    }
  }

  onMounted(async () => {
    const overview = await fetchOverviewData()
    applyData(overview)
    loaded.value = true
    // 定时刷新（60秒）
    refreshTimer = setInterval(async () => {
      cachedOverviewData = null
      const data = await fetchOverviewData()
      applyData(data)
    }, 60000)
  })

  onBeforeUnmount(() => {
    if (refreshTimer) {
      clearInterval(refreshTimer)
      refreshTimer = null
    }
  })

  onReload((overview) => {
    applyData(overview)
  })

  return { kpi, safety, energy, environment, equipment, energyTrend, alertTrend, get24hLabels, get7dLabels, loaded }
}

// ==================== 告警数据 ====================
export function useAlertData() {
  const alertList = ref([])
  const statistics = reactive({
    pending: 0,
    processing: 0,
    resolved: 0,
    total: 0
  })
  const loading = ref(false)
  const queryParams = reactive({
    type: '',
    level: '',
    status: '',
    dateRange: null,
    pageNum: 1,
    pageSize: 20
  })
  const total = ref(0)

  async function fetchAlerts() {
    loading.value = true
    try {
      const res = await getTwinAlerts(queryParams)
      if (res.code === 200) {
        alertList.value = res.rows || []
        total.value = res.total || 0
      }
    } catch (e) {
      console.warn('[useAlertData] 获取告警列表失败', e)
      // fallback
      alertList.value = generateFallbackAlerts()
      total.value = alertList.value.length
    } finally {
      loading.value = false
    }
  }

  async function fetchStatistics() {
    try {
      const res = await getTwinAlertStatistics()
      if (res.code === 200 && res.data) {
        Object.assign(statistics, res.data)
      }
    } catch (e) {
      console.warn('[useAlertData] 获取告警统计失败', e)
      statistics.pending = 8
      statistics.processing = 5
      statistics.resolved = 42
      statistics.total = 55
    }
  }

  function generateFallbackAlerts() {
    const types = ['fault', 'energy', 'environment', 'safety', 'communication']
    const levels = ['critical', 'major', 'minor', 'info']
    const statuses = ['pending', 'processing', 'resolved']
    const typeNames = { fault: '设备故障', energy: '能耗异常', environment: '环境异常', safety: '安全告警', communication: '通信异常' }
    const levelNames = { critical: '紧急', major: '重要', minor: '一般', info: '提示' }
    const statusNames = { pending: '未处理', processing: '处理中', resolved: '已处理' }
    const sources = ['A栋办公楼', 'B栋研发中心', 'C栋生产车间', 'D栋数据中心', '地下停车场', '园区配电房']
    const titles = [
      '变压器温度超限', '烟感探测器离线', '充电桩过流保护', '空调制冷异常',
      '消防通道堵塞', '水压异常告警', '配电柜漏电告警', '门禁通信中断',
      '光伏逆变器效率低', '储能BMS通信异常', '视频监控离线', '环境PM2.5超标'
    ]
    return Array.from({ length: 20 }, (_, i) => {
      const type = types[i % types.length]
      const level = levels[i % levels.length]
      const status = statuses[i % statuses.length]
      const h = String(8 + (i * 2) % 12).padStart(2, '0')
      const m = String((i * 7) % 60).padStart(2, '0')
      return {
        id: i + 1,
        title: titles[i % titles.length],
        type,
        typeName: typeNames[type],
        level,
        levelName: levelNames[level],
        status,
        statusName: statusNames[status],
        source: sources[i % sources.length],
        location: `${sources[i % sources.length]} ${Math.floor(i / 3) + 1}F`,
        createTime: `2025-01-15 ${h}:${m}:00`
      }
    })
  }

  function getTypeName(type) {
    const map = { fault: '设备故障', energy: '能耗异常', environment: '环境异常', safety: '安全告警', communication: '通信异常' }
    return map[type] || type
  }

  function getLevelName(level) {
    const map = { critical: '紧急', major: '重要', minor: '一般', info: '提示' }
    return map[level] || level
  }

  function getStatusName(status) {
    const map = { pending: '未处理', processing: '处理中', resolved: '已处理' }
    return map[status] || status
  }

  function getLevelTagType(level) {
    const map = { critical: 'danger', major: 'warning', minor: 'info', info: '' }
    return map[level] || ''
  }

  function getStatusTagType(status) {
    const map = { pending: 'danger', processing: 'warning', resolved: 'success' }
    return map[status] || ''
  }

  onMounted(() => {
    fetchAlerts()
    fetchStatistics()
  })

  return {
    alertList, statistics, loading, queryParams, total,
    fetchAlerts, fetchStatistics,
    getTypeName, getLevelName, getStatusName, getLevelTagType, getStatusTagType
  }
}

// ==================== 建筑数据 ====================
export function useBuildingData() {
  const buildingList = ref([])
  const loading = ref(false)

  async function fetchBuildings() {
    loading.value = true
    try {
      const res = await getBuildingPage()
      if (res.code === 200) {
        buildingList.value = res.rows || res.data || []
      }
    } catch (e) {
      console.warn('[useBuildingData] 获取建筑列表失败', e)
      buildingList.value = generateFallbackBuildings()
    } finally {
      loading.value = false
    }
  }

  function generateFallbackBuildings() {
    return [
      { id: 1, name: 'A栋办公楼', type: 'office', floors: 5, area: 12000, x: 15, y: 25, width: 18, height: 22, status: 'normal', enterprises: 28, devices: 320, power: 186 },
      { id: 2, name: 'B栋研发中心', type: 'lab', floors: 4, area: 8000, x: 42, y: 20, width: 16, height: 18, status: 'normal', enterprises: 15, devices: 280, power: 245 },
      { id: 3, name: 'C栋生产车间', type: 'factory', floors: 3, area: 15000, x: 65, y: 25, width: 20, height: 25, status: 'warning', enterprises: 8, devices: 420, power: 580 },
      { id: 4, name: 'D栋数据中心', type: 'datacenter', floors: 2, area: 5000, x: 20, y: 60, width: 14, height: 16, status: 'normal', enterprises: 3, devices: 156, power: 320 },
      { id: 5, name: '综合服务楼', type: 'service', floors: 3, area: 6000, x: 50, y: 58, width: 12, height: 14, status: 'normal', enterprises: 12, devices: 98, power: 85 },
      { id: 6, name: '员工餐厅', type: 'canteen', floors: 2, area: 3000, x: 75, y: 60, width: 10, height: 12, status: 'normal', enterprises: 0, devices: 45, power: 120 }
    ]
  }

  onMounted(() => {
    fetchBuildings()
  })

  return { buildingList, loading, fetchBuildings }
}

// ==================== 设备点位数据 ====================
export function useDevicePoiData() {
  const deviceList = ref([])
  const loading = ref(false)

  async function fetchDevices(params = {}) {
    loading.value = true
    try {
      const res = await getDevicePoiPage(params)
      if (res.code === 200) {
        deviceList.value = res.rows || res.data || []
      }
    } catch (e) {
      console.warn('[useDevicePoiData] 获取设备列表失败', e)
      deviceList.value = generateFallbackDevices()
    } finally {
      loading.value = false
    }
  }

  function generateFallbackDevices() {
    const devices = [
      { id: 1, name: '摄像头-A01', code: 'CAM-A01', deviceType: 'camera', buildingId: 1, floor: 1, x: 18, y: 28, status: 'online', alert: false },
      { id: 2, name: '烟感-A02', code: 'SMK-A02', deviceType: 'smoke', buildingId: 1, floor: 2, x: 22, y: 32, status: 'online', alert: true },
      { id: 3, name: '充电桩-P01', code: 'CHG-P01', deviceType: 'charging', buildingId: 0, floor: 0, x: 85, y: 75, status: 'online', alert: false },
      { id: 4, name: '充电桩-P02', code: 'CHG-P02', deviceType: 'charging', buildingId: 0, floor: 0, x: 88, y: 75, status: 'offline', alert: true },
      { id: 5, name: '温湿度-B01', code: 'TH-B01', deviceType: 'temp_humidity', buildingId: 2, floor: 1, x: 45, y: 24, status: 'online', alert: false },
      { id: 6, name: '门禁-A01', code: 'AC-A01', deviceType: 'access_control', buildingId: 1, floor: 1, x: 15, y: 30, status: 'online', alert: false },
      { id: 7, name: '摄像头-B01', code: 'CAM-B01', deviceType: 'camera', buildingId: 2, floor: 1, x: 48, y: 22, status: 'online', alert: false },
      { id: 8, name: '摄像头-C01', code: 'CAM-C01', deviceType: 'camera', buildingId: 3, floor: 1, x: 70, y: 28, status: 'online', alert: false },
      { id: 9, name: '电表-C01', code: 'EM-C01', deviceType: 'energy_meter', buildingId: 3, floor: 1, x: 72, y: 32, status: 'online', alert: false },
      { id: 10, name: '水表-D01', code: 'WM-D01', deviceType: 'water_meter', buildingId: 4, floor: 1, x: 24, y: 64, status: 'online', alert: false },
      { id: 11, name: '光伏逆变器', code: 'PV-INV01', deviceType: 'solar_panel', buildingId: 0, floor: 0, x: 10, y: 10, status: 'online', alert: false },
      { id: 12, name: '储能系统', code: 'ESS-01', deviceType: 'storage', buildingId: 0, floor: 0, x: 10, y: 85, status: 'online', alert: false },
      { id: 13, name: '环境监测站', code: 'ENV-01', deviceType: 'env_station', buildingId: 0, floor: 0, x: 90, y: 10, status: 'online', alert: false },
      { id: 14, name: '智能路灯-L01', code: 'SL-L01', deviceType: 'street_light', buildingId: 0, floor: 0, x: 35, y: 50, status: 'online', alert: false },
      { id: 15, name: '智能路灯-L02', code: 'SL-L02', deviceType: 'street_light', buildingId: 0, floor: 0, x: 55, y: 50, status: 'online', alert: false }
    ]
    return devices
  }

  onMounted(() => {
    fetchDevices()
  })

  return { deviceList, loading, fetchDevices }
}

// ==================== 楼层数据 ====================
export function useFloorData() {
  const floorInfo = reactive({
    buildingName: '',
    floorNum: 1,
    totalFloors: 1,
    power: 0,
    temperature: 0,
    humidity: 0,
    peopleCount: 0,
    deviceCount: 0,
    alertCount: 0
  })

  const rooms = ref([])
  const devices = ref([])
  const personnel = ref([])

  function setFloorData(buildingName, floorNum, totalFloors) {
    floorInfo.buildingName = buildingName
    floorInfo.floorNum = floorNum
    floorInfo.totalFloors = totalFloors

    // fallback 数据
    floorInfo.power = 120 + Math.floor(Math.random() * 100)
    floorInfo.temperature = +(22 + Math.random() * 6).toFixed(1)
    floorInfo.humidity = +(50 + Math.random() * 20).toFixed(1)
    floorInfo.peopleCount = Math.floor(10 + Math.random() * 40)
    floorInfo.deviceCount = Math.floor(15 + Math.random() * 20)
    floorInfo.alertCount = Math.floor(Math.random() * 3)

    rooms.value = generateFallbackRooms(floorNum)
    devices.value = generateFallbackFloorDevices(floorNum)
    personnel.value = generateFallbackPersonnel(floorNum)
  }

  function generateFallbackRooms(floor) {
    const base = [
      { id: 1, name: `${floor}01`, type: 'office', x: 5, y: 5, width: 35, height: 30, area: 105 },
      { id: 2, name: `${floor}02`, type: 'office', x: 5, y: 40, width: 35, height: 25, area: 87 },
      { id: 3, name: `${floor}03`, type: 'meeting', x: 45, y: 5, width: 25, height: 20, area: 50 },
      { id: 4, name: `${floor}04`, type: 'office', x: 45, y: 30, width: 25, height: 35, area: 87 },
      { id: 5, name: `${floor}05`, type: 'utility', x: 75, y: 5, width: 20, height: 15, area: 30 },
      { id: 6, name: '走廊', type: 'corridor', x: 40, y: 25, width: 5, height: 60, area: 0 },
      { id: 7, name: '卫生间', type: 'restroom', x: 75, y: 25, width: 20, height: 15, area: 30 },
      { id: 8, name: `${floor}06`, type: 'office', x: 75, y: 45, width: 20, height: 20, area: 40 }
    ]
    return base
  }

  function generateFallbackFloorDevices(floor) {
    return [
      { id: 1, name: '温湿度传感器', type: 'temp_humidity', x: 20, y: 15, status: 'online' },
      { id: 2, name: '烟感探测器', type: 'smoke', x: 55, y: 10, status: 'online' },
      { id: 3, name: '摄像头', type: 'camera', x: 42, y: 50, status: 'online' },
      { id: 4, name: '门禁', type: 'access_control', x: 5, y: 35, status: 'online' },
      { id: 5, name: '照明控制器', type: 'lighting', x: 60, y: 45, status: 'online' },
      { id: 6, name: '空调控制器', type: 'hvac', x: 80, y: 50, status: 'online' }
    ]
  }

  function generateFallbackPersonnel(floor) {
    return Array.from({ length: 8 }, (_, i) => ({
      id: i + 1,
      name: `人员${i + 1}`,
      x: 10 + Math.random() * 80,
      y: 10 + Math.random() * 70,
      department: ['研发部', '市场部', '行政部', '财务部'][i % 4]
    }))
  }

  return { floorInfo, rooms, devices, personnel, setFloorData }
}
