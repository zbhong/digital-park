import { get, post, put, del } from '@/utils/request'

// ==================== 模板管理 /ai-report/templates ====================

/**
 * 模板分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getTemplatePage = (params) => get('/ai-report/templates', params)

/**
 * 创建模板
 * @param {object} data - 模板数据
 * @returns {Promise}
 */
export const addTemplate = (data) => post('/ai-report/templates', data)

/**
 * 更新模板
 * @param {number} id - 模板ID
 * @param {object} data - 模板数据
 * @returns {Promise}
 */
export const updateTemplate = (id, data) => put('/ai-report/templates/' + id, data)

/**
 * 删除模板
 * @param {number} id - 模板ID
 * @returns {Promise}
 */
export const deleteTemplate = (id) => del('/ai-report/templates/' + id)

// ==================== 报告实例管理 /ai-report/instances ====================

/**
 * 报告实例分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getInstancePage = (params) => get('/ai-report/instances', params)

/**
 * 生成报告
 * @param {object} data - 生成参数 { templateId, period, customParams }
 * @returns {Promise}
 */
export const generateReport = (data) => post('/ai-report/instances/generate', data)

/**
 * 报告实例详情
 * @param {number} id - 实例ID
 * @returns {Promise}
 */
export const getInstance = (id) => get('/ai-report/instances/' + id)

/**
 * 删除报告实例
 * @param {number} id - 实例ID
 * @returns {Promise}
 */
export const deleteInstance = (id) => del('/ai-report/instances/' + id)

// ==================== 统计 /ai-report/statistics ====================

/**
 * 按模块统计报告数量
 * @returns {Promise}
 */
export const getReportStatistics = () => get('/ai-report/statistics')
