import { get, post, put, del } from '@/utils/request'

// ==================== 监测点位 /safety/monitor-point ====================

/**
 * 监测点位分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getMonitorPointPage = (params) => get('/safety/monitor-point/page', params)

/**
 * 监测点位列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getMonitorPointList = (params) => get('/safety/monitor-point/list', params)

/**
 * 监测点位详情
 * @param {number} id - 点位ID
 * @returns {Promise}
 */
export const getMonitorPoint = (id) => get('/safety/monitor-point/' + id)

/**
 * 新增监测点位
 * @param {object} data - 点位数据
 * @returns {Promise}
 */
export const addMonitorPoint = (data) => post('/safety/monitor-point', data)

/**
 * 修改监测点位
 * @param {object} data - 点位数据
 * @returns {Promise}
 */
export const updateMonitorPoint = (data) => put('/safety/monitor-point', data)

/**
 * 删除监测点位
 * @param {number} id - 点位ID
 * @returns {Promise}
 */
export const deleteMonitorPoint = (id) => del('/safety/monitor-point/' + id)

// ==================== 安全事件 /safety/event ====================

/**
 * 安全事件分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getSafetyEventPage = (params) => get('/safety/event/page', params)

/**
 * 安全事件详情
 * @param {number} id - 事件ID
 * @returns {Promise}
 */
export const getSafetyEvent = (id) => get('/safety/event/' + id)

/**
 * 新增安全事件
 * @param {object} data - 事件数据
 * @returns {Promise}
 */
export const addSafetyEvent = (data) => post('/safety/event', data)

/**
 * 修改安全事件
 * @param {object} data - 事件数据
 * @returns {Promise}
 */
export const updateSafetyEvent = (data) => put('/safety/event', data)

/**
 * 删除安全事件
 * @param {number} id - 事件ID
 * @returns {Promise}
 */
export const deleteSafetyEvent = (id) => del('/safety/event/' + id)

/**
 * 处理安全事件
 * @param {number} id - 事件ID
 * @param {object} data - 处理数据 { handleResult }
 * @returns {Promise}
 */
export const handleSafetyEvent = (id, data) => put('/safety/event/' + id + '/handle', data)

/**
 * 安全事件统计
 * @returns {Promise}
 */
export const getSafetyEventStatistics = () => get('/safety/event/statistics')

// ==================== 巡检 /safety/patrol ====================

/**
 * 巡检分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getPatrolPage = (params) => get('/safety/patrol/page', params)

/**
 * 巡检详情
 * @param {number} id - 巡检ID
 * @returns {Promise}
 */
export const getPatrol = (id) => get('/safety/patrol/' + id)

/**
 * 新增巡检
 * @param {object} data - 巡检数据
 * @returns {Promise}
 */
export const addPatrol = (data) => post('/safety/patrol', data)

/**
 * 修改巡检
 * @param {object} data - 巡检数据
 * @returns {Promise}
 */
export const updatePatrol = (data) => put('/safety/patrol', data)

/**
 * 删除巡检
 * @param {number} id - 巡检ID
 * @returns {Promise}
 */
export const deletePatrol = (id) => del('/safety/patrol/' + id)

// ==================== 巡检路线 /safety/patrol-route ====================

/**
 * 巡检路线分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getPatrolRoutePage = (params) => get('/safety/patrol-route/page', params)

/**
 * 巡检路线列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getPatrolRouteList = (params) => get('/safety/patrol-route/list', params)

/**
 * 巡检路线详情
 * @param {number} id - 路线ID
 * @returns {Promise}
 */
export const getPatrolRoute = (id) => get('/safety/patrol-route/' + id)

/**
 * 新增巡检路线
 * @param {object} data - 路线数据
 * @returns {Promise}
 */
export const addPatrolRoute = (data) => post('/safety/patrol-route', data)

/**
 * 修改巡检路线
 * @param {object} data - 路线数据
 * @returns {Promise}
 */
export const updatePatrolRoute = (data) => put('/safety/patrol-route', data)

/**
 * 删除巡检路线
 * @param {number} id - 路线ID
 * @returns {Promise}
 */
export const deletePatrolRoute = (id) => del('/safety/patrol-route/' + id)

// ==================== 消防设备 /safety/fire-equipment ====================

/**
 * 消防设备分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getFireEquipmentPage = (params) => get('/safety/fire-equipment/page', params)

/**
 * 消防设备列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getFireEquipmentList = (params) => get('/safety/fire-equipment/list', params)

/**
 * 消防设备详情
 * @param {number} id - 设备ID
 * @returns {Promise}
 */
export const getFireEquipment = (id) => get('/safety/fire-equipment/' + id)

/**
 * 新增消防设备
 * @param {object} data - 设备数据
 * @returns {Promise}
 */
export const addFireEquipment = (data) => post('/safety/fire-equipment', data)

/**
 * 修改消防设备
 * @param {object} data - 设备数据
 * @returns {Promise}
 */
export const updateFireEquipment = (data) => put('/safety/fire-equipment', data)

/**
 * 删除消防设备
 * @param {number} id - 设备ID
 * @returns {Promise}
 */
export const deleteFireEquipment = (id) => del('/safety/fire-equipment/' + id)

/**
 * 即将过期消防设备
 * @param {object} params - 查询参数 { days }
 * @returns {Promise}
 */
export const getExpiringFireEquipment = (params) => get('/safety/fire-equipment/expiring', params)

// ==================== 网络安全 /safety/network ====================

/**
 * 网络安全分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getNetworkPage = (params) => get('/safety/network/page', params)

/**
 * 网络安全详情
 * @param {number} id - 记录ID
 * @returns {Promise}
 */
export const getNetwork = (id) => get('/safety/network/' + id)

/**
 * 新增网络安全记录
 * @param {object} data - 数据
 * @returns {Promise}
 */
export const addNetwork = (data) => post('/safety/network', data)

/**
 * 修改网络安全记录
 * @param {object} data - 数据
 * @returns {Promise}
 */
export const updateNetwork = (data) => put('/safety/network', data)

/**
 * 删除网络安全记录
 * @param {number} id - 记录ID
 * @returns {Promise}
 */
export const deleteNetwork = (id) => del('/safety/network/' + id)

// ==================== 生产安全 /safety/production ====================

/**
 * 生产安全分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getProductionPage = (params) => get('/safety/production/page', params)

/**
 * 生产安全详情
 * @param {number} id - 记录ID
 * @returns {Promise}
 */
export const getProduction = (id) => get('/safety/production/' + id)

/**
 * 新增生产安全记录
 * @param {object} data - 数据
 * @returns {Promise}
 */
export const addProduction = (data) => post('/safety/production', data)

/**
 * 修改生产安全记录
 * @param {object} data - 数据
 * @returns {Promise}
 */
export const updateProduction = (data) => put('/safety/production', data)

/**
 * 删除生产安全记录
 * @param {number} id - 记录ID
 * @returns {Promise}
 */
export const deleteProduction = (id) => del('/safety/production/' + id)
