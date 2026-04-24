import { get, post, put, del } from '@/utils/request'

// ==================== 应急物资 /emergency/material ====================

/**
 * 应急物资分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getMaterialPage = (params) => get('/emergency/material/page', params)

/**
 * 应急物资列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getMaterialList = (params) => get('/emergency/material/list', params)

/**
 * 应急物资详情
 * @param {number} id - 物资ID
 * @returns {Promise}
 */
export const getMaterial = (id) => get('/emergency/material/' + id)

/**
 * 新增应急物资
 * @param {object} data - 物资数据
 * @returns {Promise}
 */
export const addMaterial = (data) => post('/emergency/material', data)

/**
 * 修改应急物资
 * @param {object} data - 物资数据
 * @returns {Promise}
 */
export const updateMaterial = (data) => put('/emergency/material', data)

/**
 * 删除应急物资
 * @param {number} id - 物资ID
 * @returns {Promise}
 */
export const deleteMaterial = (id) => del('/emergency/material/' + id)

/**
 * 低库存物资列表
 * @param {object} params - 查询参数 { threshold }
 * @returns {Promise}
 */
export const getLowStockMaterial = (params) => get('/emergency/material/low-stock', params)

// ==================== 应急队伍 /emergency/team ====================

/**
 * 应急队伍分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getTeamPage = (params) => get('/emergency/team/page', params)

/**
 * 应急队伍列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getTeamList = (params) => get('/emergency/team/list', params)

/**
 * 应急队伍详情
 * @param {number} id - 队伍ID
 * @returns {Promise}
 */
export const getTeam = (id) => get('/emergency/team/' + id)

/**
 * 新增应急队伍
 * @param {object} data - 队伍数据
 * @returns {Promise}
 */
export const addTeam = (data) => post('/emergency/team', data)

/**
 * 修改应急队伍
 * @param {object} data - 队伍数据
 * @returns {Promise}
 */
export const updateTeam = (data) => put('/emergency/team', data)

/**
 * 删除应急队伍
 * @param {number} id - 队伍ID
 * @returns {Promise}
 */
export const deleteTeam = (id) => del('/emergency/team/' + id)

// ==================== 应急预案 /emergency/plan ====================

/**
 * 应急预案分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getPlanPage = (params) => get('/emergency/plan/page', params)

/**
 * 应急预案详情
 * @param {number} id - 预案ID
 * @returns {Promise}
 */
export const getPlan = (id) => get('/emergency/plan/' + id)

/**
 * 新增应急预案
 * @param {object} data - 预案数据
 * @returns {Promise}
 */
export const addPlan = (data) => post('/emergency/plan', data)

/**
 * 修改应急预案
 * @param {object} data - 预案数据
 * @returns {Promise}
 */
export const updatePlan = (data) => put('/emergency/plan', data)

/**
 * 删除应急预案
 * @param {number} id - 预案ID
 * @returns {Promise}
 */
export const deletePlan = (id) => del('/emergency/plan/' + id)

/**
 * 发布应急预案
 * @param {number} id - 预案ID
 * @returns {Promise}
 */
export const publishPlan = (id) => put('/emergency/plan/' + id + '/publish')

/**
 * 撤销应急预案
 * @param {number} id - 预案ID
 * @returns {Promise}
 */
export const revokePlan = (id) => put('/emergency/plan/' + id + '/revoke')

// ==================== 应急演练 /emergency/drill ====================

/**
 * 应急演练分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getDrillPage = (params) => get('/emergency/drill/page', params)

/**
 * 应急演练详情
 * @param {number} id - 演练ID
 * @returns {Promise}
 */
export const getDrill = (id) => get('/emergency/drill/' + id)

/**
 * 新增应急演练
 * @param {object} data - 演练数据
 * @returns {Promise}
 */
export const addDrill = (data) => post('/emergency/drill', data)

/**
 * 修改应急演练
 * @param {object} data - 演练数据
 * @returns {Promise}
 */
export const updateDrill = (data) => put('/emergency/drill', data)

/**
 * 删除应急演练
 * @param {number} id - 演练ID
 * @returns {Promise}
 */
export const deleteDrill = (id) => del('/emergency/drill/' + id)

// ==================== 应急事件 /emergency/event ====================

/**
 * 应急事件分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getEmergencyEventPage = (params) => get('/emergency/event/page', params)

/**
 * 应急事件详情
 * @param {number} id - 事件ID
 * @returns {Promise}
 */
export const getEmergencyEvent = (id) => get('/emergency/event/' + id)

/**
 * 新增应急事件
 * @param {object} data - 事件数据
 * @returns {Promise}
 */
export const addEmergencyEvent = (data) => post('/emergency/event', data)

/**
 * 修改应急事件
 * @param {object} data - 事件数据
 * @returns {Promise}
 */
export const updateEmergencyEvent = (data) => put('/emergency/event', data)

/**
 * 删除应急事件
 * @param {number} id - 事件ID
 * @returns {Promise}
 */
export const deleteEmergencyEvent = (id) => del('/emergency/event/' + id)

/**
 * 处理应急事件
 * @param {number} id - 事件ID
 * @param {object} data - 处理数据 { handleResult }
 * @returns {Promise}
 */
export const handleEmergencyEvent = (id, data) => put('/emergency/event/' + id + '/handle', data)

/**
 * 应急事件统计
 * @returns {Promise}
 */
export const getEmergencyEventStatistics = () => get('/emergency/event/statistics')
