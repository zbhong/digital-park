import { get, post, put, del } from '@/utils/request'

// ==================== 企业画像 /economy/profile ====================

/**
 * 企业画像分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getProfilePage = (params) => get('/economy/profile/page', params)

/**
 * 企业画像详情
 * @param {number} id - 画像ID
 * @returns {Promise}
 */
export const getProfile = (id) => get('/economy/profile/' + id)

/**
 * 根据企业ID获取画像
 * @param {number} enterpriseId - 企业ID
 * @returns {Promise}
 */
export const getProfileByEnterpriseId = (enterpriseId) => get('/economy/profile/enterprise/' + enterpriseId)

/**
 * 根据行业获取画像列表
 * @param {string} industry - 行业
 * @returns {Promise}
 */
export const getProfileByIndustry = (industry) => get('/economy/profile/industry/' + industry)

/**
 * 行业分布统计
 * @returns {Promise}
 */
export const getIndustryDistribution = () => get('/economy/profile/statistics/industry-distribution')

/**
 * 规模分布统计
 * @returns {Promise}
 */
export const getScaleDistribution = () => get('/economy/profile/statistics/scale-distribution')

/**
 * 新增企业画像
 * @param {object} data - 画像数据
 * @returns {Promise}
 */
export const addProfile = (data) => post('/economy/profile', data)

/**
 * 修改企业画像
 * @param {object} data - 画像数据
 * @returns {Promise}
 */
export const updateProfile = (data) => put('/economy/profile', data)

/**
 * 删除企业画像
 * @param {number} id - 画像ID
 * @returns {Promise}
 */
export const deleteProfile = (id) => del('/economy/profile/' + id)

// ==================== 经济指标 /economy/indicator ====================

/**
 * 经济指标分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getIndicatorPage = (params) => get('/economy/indicator/page', params)

/**
 * 经济指标详情
 * @param {number} id - 指标ID
 * @returns {Promise}
 */
export const getIndicator = (id) => get('/economy/indicator/' + id)

/**
 * 根据类型获取指标列表
 * @param {string} type - 类型
 * @returns {Promise}
 */
export const getIndicatorByType = (type) => get('/economy/indicator/type/' + type)

/**
 * 根据周期获取指标列表
 * @param {string} period - 周期
 * @returns {Promise}
 */
export const getIndicatorByPeriod = (period) => get('/economy/indicator/period/' + period)

/**
 * 经济指标统计
 * @param {object} params - 查询参数 { type, period }
 * @returns {Promise}
 */
export const getIndicatorStatistics = (params) => get('/economy/indicator/statistics', params)

/**
 * 经济指标趋势
 * @param {object} params - 查询参数 { name, period }
 * @returns {Promise}
 */
export const getIndicatorTrend = (params) => get('/economy/indicator/trend', params)

/**
 * 新增经济指标
 * @param {object} data - 指标数据
 * @returns {Promise}
 */
export const addIndicator = (data) => post('/economy/indicator', data)

/**
 * 修改经济指标
 * @param {object} data - 指标数据
 * @returns {Promise}
 */
export const updateIndicator = (data) => put('/economy/indicator', data)

/**
 * 删除经济指标
 * @param {number} id - 指标ID
 * @returns {Promise}
 */
export const deleteIndicator = (id) => del('/economy/indicator/' + id)

// ==================== 经济报告 /economy/report ====================

/**
 * 经济报告分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getEconomyReportPage = (params) => get('/economy/report/page', params)

/**
 * 经济报告详情
 * @param {number} id - 报告ID
 * @returns {Promise}
 */
export const getEconomyReport = (id) => get('/economy/report/' + id)

/**
 * 新增经济报告
 * @param {object} data - 报告数据
 * @returns {Promise}
 */
export const addEconomyReport = (data) => post('/economy/report', data)

/**
 * 修改经济报告
 * @param {object} data - 报告数据
 * @returns {Promise}
 */
export const updateEconomyReport = (data) => put('/economy/report', data)

/**
 * 发布经济报告
 * @param {number} id - 报告ID
 * @returns {Promise}
 */
export const publishEconomyReport = (id) => put('/economy/report/publish/' + id)

/**
 * 生成经济报告
 * @param {object} params - 查询参数 { type, period }
 * @returns {Promise}
 */
export const generateEconomyReport = (params) => post('/economy/report/generate', params)

/**
 * 删除经济报告
 * @param {number} id - 报告ID
 * @returns {Promise}
 */
export const deleteEconomyReport = (id) => del('/economy/report/' + id)
