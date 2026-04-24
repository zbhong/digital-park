import { get, post, put, del } from '@/utils/request'

// ==================== 环境监测点位 /environment/point ====================

/**
 * 监测点位分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getEnvPointPage = (params) => get('/environment/point/page', params)

/**
 * 监测点位列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getEnvPointList = (params) => get('/environment/point/list', params)

/**
 * 监测点位详情
 * @param {number} id - 点位ID
 * @returns {Promise}
 */
export const getEnvPoint = (id) => get('/environment/point/' + id)

/**
 * 新增监测点位
 * @param {object} data - 点位数据
 * @returns {Promise}
 */
export const addEnvPoint = (data) => post('/environment/point', data)

/**
 * 修改监测点位
 * @param {object} data - 点位数据
 * @returns {Promise}
 */
export const updateEnvPoint = (data) => put('/environment/point', data)

/**
 * 删除监测点位
 * @param {number} id - 点位ID
 * @returns {Promise}
 */
export const deleteEnvPoint = (id) => del('/environment/point/' + id)

// ==================== 环境监测数据 /environment/data ====================

/**
 * 监测数据分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getEnvDataPage = (params) => get('/environment/data/page', params)

/**
 * 监测数据详情
 * @param {number} id - 数据ID
 * @returns {Promise}
 */
export const getEnvData = (id) => get('/environment/data/' + id)

/**
 * 新增监测数据
 * @param {object} data - 监测数据
 * @returns {Promise}
 */
export const addEnvData = (data) => post('/environment/data', data)

/**
 * 批量新增监测数据
 * @param {object} data - 监测数据列表
 * @returns {Promise}
 */
export const batchAddEnvData = (data) => post('/environment/data/batch', data)

/**
 * 修改监测数据
 * @param {object} data - 监测数据
 * @returns {Promise}
 */
export const updateEnvData = (data) => put('/environment/data', data)

/**
 * 删除监测数据
 * @param {number} id - 数据ID
 * @returns {Promise}
 */
export const deleteEnvData = (id) => del('/environment/data/' + id)

/**
 * 按时间统计
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getEnvStatisticsByTime = (params) => get('/environment/data/statistics/time', params)

/**
 * 按区域统计
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getEnvStatisticsByArea = (params) => get('/environment/data/statistics/area', params)

/**
 * 按指标统计
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getEnvStatisticsByIndicator = (params) => get('/environment/data/statistics/indicator', params)

/**
 * 超标预警列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getEnvWarnings = (params) => get('/environment/data/warnings', params)

/**
 * 污染源分析
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getPollutionSourceAnalysis = (params) => get('/environment/data/pollution-source', params)

// ==================== 兼容性别名（供 Vue 组件使用） ====================

/**
 * 监测点位分页列表（别名）
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getMonitorPointPage = (params) => get('/environment/point/page', params)

/**
 * 监测点位详情（别名）
 * @param {number} id - 点位ID
 * @returns {Promise}
 */
export const getMonitorPoint = (id) => get('/environment/point/' + id)

/**
 * 新增监测点位（别名）
 * @param {object} data - 点位数据
 * @returns {Promise}
 */
export const addMonitorPoint = (data) => post('/environment/point', data)

/**
 * 修改监测点位（别名）
 * @param {object} data - 点位数据
 * @returns {Promise}
 */
export const updateMonitorPoint = (data) => put('/environment/point', data)

/**
 * 删除监测点位（别名）
 * @param {number} id - 点位ID
 * @returns {Promise}
 */
export const deleteMonitorPoint = (id) => del('/environment/point/' + id)
