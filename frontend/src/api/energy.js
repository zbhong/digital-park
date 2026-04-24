import { get, post, put, del } from '@/utils/request'

// ==================== 计量表 /energy/meters ====================

/**
 * 计量表列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getMeterList = (params) => get('/energy/meters', params)

/**
 * 计量表分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getMeterPage = (params) => get('/energy/meters/page', params)

/**
 * 计量表详情
 * @param {number} id - 计量表ID
 * @returns {Promise}
 */
export const getMeter = (id) => get('/energy/meters/' + id)

/**
 * 新增计量表
 * @param {object} data - 计量表数据
 * @returns {Promise}
 */
export const addMeter = (data) => post('/energy/meters', data)

/**
 * 修改计量表
 * @param {object} data - 计量表数据
 * @returns {Promise}
 */
export const updateMeter = (data) => put('/energy/meters', data)

/**
 * 删除计量表
 * @param {number} id - 计量表ID
 * @returns {Promise}
 */
export const deleteMeter = (id) => del('/energy/meters/' + id)

// ==================== 计量记录 /energy/records ====================

/**
 * 计量记录分页列表
 * @param {object} params - 查询参数 { meterId, startTime, endTime, pageNum, pageSize }
 * @returns {Promise}
 */
export const getRecordPage = (params) => get('/energy/records', params)

/**
 * 批量导入计量数据
 * @param {object} data - 计量记录列表
 * @returns {Promise}
 */
export const importRecords = (data) => post('/energy/records/import', data)

/**
 * 峰谷平统计
 * @param {object} params - 查询参数 { meterId, startTime, endTime }
 * @returns {Promise}
 */
export const getPeakValley = (params) => get('/energy/records/peak-valley', params)

/**
 * 计量记录趋势分析
 * @param {object} params - 查询参数 { meterId, period, startTime, endTime }
 * @returns {Promise}
 */
export const getRecordTrend = (params) => get('/energy/records/trend', params)

// ==================== 源侧设备 /energy/sources ====================

/**
 * 源侧设备列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getSourceList = (params) => get('/energy/sources', params)

/**
 * 源侧设备详情
 * @param {number} id - 设备ID
 * @returns {Promise}
 */
export const getSource = (id) => get('/energy/sources/' + id)

/**
 * 新增源侧设备
 * @param {object} data - 设备数据
 * @returns {Promise}
 */
export const addSource = (data) => post('/energy/sources', data)

/**
 * 修改源侧设备
 * @param {object} data - 设备数据
 * @returns {Promise}
 */
export const updateSource = (data) => put('/energy/sources', data)

/**
 * 删除源侧设备
 * @param {number} id - 设备ID
 * @returns {Promise}
 */
export const deleteSource = (id) => del('/energy/sources/' + id)

/**
 * 源侧设备实时数据
 * @param {number} id - 设备ID
 * @returns {Promise}
 */
export const getSourceRealtime = (id) => get('/energy/sources/' + id + '/realtime')

/**
 * 发电统计
 * @param {number} id - 设备ID
 * @param {object} params - 查询参数 { period }
 * @returns {Promise}
 */
export const getGenerationStats = (id, params) => get('/energy/sources/' + id + '/generation', params)

// ==================== 配电网 /energy/grids ====================

/**
 * 配电网列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getGridList = (params) => get('/energy/grids', params)

/**
 * 配电网详情
 * @param {number} id - 配电网ID
 * @returns {Promise}
 */
export const getGrid = (id) => get('/energy/grids/' + id)

/**
 * 新增配电网
 * @param {object} data - 配电网数据
 * @returns {Promise}
 */
export const addGrid = (data) => post('/energy/grids', data)

/**
 * 修改配电网
 * @param {object} data - 配电网数据
 * @returns {Promise}
 */
export const updateGrid = (data) => put('/energy/grids', data)

/**
 * 删除配电网
 * @param {number} id - 配电网ID
 * @returns {Promise}
 */
export const deleteGrid = (id) => del('/energy/grids/' + id)

// ==================== 负荷管理 /energy/loads ====================

/**
 * 负荷列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getLoadList = (params) => get('/energy/loads', params)

/**
 * 负荷详情
 * @param {number} id - 负荷ID
 * @returns {Promise}
 */
export const getLoad = (id) => get('/energy/loads/' + id)

/**
 * 新增负荷
 * @param {object} data - 负荷数据
 * @returns {Promise}
 */
export const addLoad = (data) => post('/energy/loads', data)

/**
 * 修改负荷
 * @param {object} data - 负荷数据
 * @returns {Promise}
 */
export const updateLoad = (data) => put('/energy/loads', data)

/**
 * 删除负荷
 * @param {number} id - 负荷ID
 * @returns {Promise}
 */
export const deleteLoad = (id) => del('/energy/loads/' + id)

/**
 * 负荷统计
 * @returns {Promise}
 */
export const getLoadStatistics = () => get('/energy/loads/statistics')

/**
 * 柔性调度
 * @param {number} id - 负荷ID
 * @param {object} params - 调度参数 { action, targetPower }
 * @returns {Promise}
 */
export const flexibleDispatch = (id, params) => post('/energy/loads/' + id + '/dispatch', params)

// ==================== 储能系统 /energy/storages ====================

/**
 * 储能系统列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getStorageList = (params) => get('/energy/storages', params)

/**
 * 储能系统详情
 * @param {number} id - 储能ID
 * @returns {Promise}
 */
export const getStorage = (id) => get('/energy/storages/' + id)

/**
 * 新增储能系统
 * @param {object} data - 储能数据
 * @returns {Promise}
 */
export const addStorage = (data) => post('/energy/storages', data)

/**
 * 修改储能系统
 * @param {object} data - 储能数据
 * @returns {Promise}
 */
export const updateStorage = (data) => put('/energy/storages', data)

/**
 * 删除储能系统
 * @param {number} id - 储能ID
 * @returns {Promise}
 */
export const deleteStorage = (id) => del('/energy/storages/' + id)

/**
 * 储能SOC监控
 * @param {number} id - 储能ID
 * @returns {Promise}
 */
export const getStorageSoc = (id) => get('/energy/storages/' + id + '/soc')

/**
 * 储能实时状态
 * @param {number} id - 储能ID
 * @returns {Promise}
 */
export const getStorageStatus = (id) => get('/energy/storages/' + id + '/status')

/**
 * 更新储能策略
 * @param {object} data - 策略数据
 * @returns {Promise}
 */
export const updateStorageStrategy = (data) => put('/energy/storages/strategy', data)

/**
 * 储能收益统计
 * @param {number} id - 储能ID
 * @param {object} params - 查询参数 { period }
 * @returns {Promise}
 */
export const getStorageIncome = (id, params) => get('/energy/storages/' + id + '/income', params)

// ==================== 充电桩 /energy/chargings ====================

/**
 * 充电桩列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getChargingList = (params) => get('/energy/chargings', params)

/**
 * 充电桩详情
 * @param {number} id - 充电桩ID
 * @returns {Promise}
 */
export const getCharging = (id) => get('/energy/chargings/' + id)

/**
 * 新增充电桩
 * @param {object} data - 充电桩数据
 * @returns {Promise}
 */
export const addCharging = (data) => post('/energy/chargings', data)

/**
 * 修改充电桩
 * @param {object} data - 充电桩数据
 * @returns {Promise}
 */
export const updateCharging = (data) => put('/energy/chargings', data)

/**
 * 删除充电桩
 * @param {number} id - 充电桩ID
 * @returns {Promise}
 */
export const deleteCharging = (id) => del('/energy/chargings/' + id)

/**
 * 充电桩状态监控
 * @returns {Promise}
 */
export const getChargingMonitor = () => get('/energy/chargings/monitor')

/**
 * 充电桩运营统计
 * @param {object} params - 查询参数 { period }
 * @returns {Promise}
 */
export const getChargingOperation = (params) => get('/energy/chargings/operation', params)

/**
 * 充电桩调度配置
 * @param {object} data - 调度数据
 * @returns {Promise}
 */
export const updateChargingSchedule = (data) => put('/energy/chargings/schedule', data)

// ==================== 充电记录 /energy/charging-records ====================

/**
 * 充电记录分页列表
 * @param {object} params - 查询参数 { chargingId, plateNumber, startTime, endTime, pageNum, pageSize }
 * @returns {Promise}
 */
export const getChargingRecordPage = (params) => get('/energy/charging-records', params)

/**
 * 充电记录收入统计
 * @param {object} params - 查询参数 { period }
 * @returns {Promise}
 */
export const getChargingRecordIncome = (params) => get('/energy/charging-records/income', params)

// ==================== 能源分析 /energy/analysis ====================

/**
 * 能耗统计(按时间/区域/类型)
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getConsumptionAnalysis = (params) => get('/energy/analysis/consumption', params)

/**
 * 费用统计
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getCostAnalysis = (params) => get('/energy/analysis/cost', params)

/**
 * 碳排放统计
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getCarbonAnalysis = (params) => get('/energy/analysis/carbon', params)

/**
 * 对标分析
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getCompareAnalysis = (params) => get('/energy/analysis/compare', params)

/**
 * 负荷分析
 * @param {object} params - 查询参数 { type, startDate, endDate, granularity }
 * @returns {Promise}
 */
export const getLoadAnalysis = (params) => get('/energy/analysis/load', params)

/**
 * 储能分析
 * @param {object} params - 查询参数 { storageId, period }
 * @returns {Promise}
 */
export const getStorageAnalysis = (params) => get('/energy/analysis/storage', params)

/**
 * 充电桩运营分析
 * @param {object} params - 查询参数 { period }
 * @returns {Promise}
 */
export const getChargingAnalysis = (params) => get('/energy/analysis/charging', params)

/**
 * 能源流向数据
 * @returns {Promise}
 */
export const getEnergyFlow = () => get('/energy/analysis/flow')

// ==================== 能源报告 /energy/report ====================

/**
 * 能耗日报
 * @param {object} params - 查询参数 { date }
 * @returns {Promise}
 */
export const getDailyReport = (params) => get('/energy/report/daily', params)

/**
 * 能耗月报
 * @param {object} params - 查询参数 { year, month }
 * @returns {Promise}
 */
export const getMonthlyReport = (params) => get('/energy/report/monthly', params)

/**
 * 能耗年报
 * @param {object} params - 查询参数 { year }
 * @returns {Promise}
 */
export const getYearlyReport = (params) => get('/energy/report/yearly', params)

/**
 * 综合能源概览
 * @returns {Promise}
 */
export const getEnergyOverview = () => get('/energy/report/overview')

/**
 * 节能建议报告
 * @returns {Promise}
 */
export const getEnergySuggestions = () => get('/energy/report/suggestions')
