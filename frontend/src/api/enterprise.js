import { get, post, put, del } from '@/utils/request'

// ==================== 企业信息 /enterprise/info ====================

/**
 * 企业信息分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getEnterpriseInfoPage = (params) => get('/enterprise/info/page', params)

/**
 * 企业信息详情
 * @param {number} id - 企业ID
 * @returns {Promise}
 */
export const getEnterpriseInfo = (id) => get('/enterprise/info/' + id)

/**
 * 根据区域获取企业列表
 * @param {number} areaId - 区域ID
 * @returns {Promise}
 */
export const getEnterpriseByAreaId = (areaId) => get('/enterprise/info/area/' + areaId)

/**
 * 根据楼栋获取企业列表
 * @param {number} buildingId - 楼栋ID
 * @returns {Promise}
 */
export const getEnterpriseByBuildingId = (buildingId) => get('/enterprise/info/building/' + buildingId)

/**
 * 企业行业统计
 * @returns {Promise}
 */
export const getEnterpriseIndustryStatistics = () => get('/enterprise/info/statistics/industry')

/**
 * 企业状态统计
 * @returns {Promise}
 */
export const getEnterpriseStatusStatistics = () => get('/enterprise/info/statistics/status')

/**
 * 新增企业信息
 * @param {object} data - 企业数据
 * @returns {Promise}
 */
export const addEnterpriseInfo = (data) => post('/enterprise/info', data)

/**
 * 修改企业信息
 * @param {object} data - 企业数据
 * @returns {Promise}
 */
export const updateEnterpriseInfo = (data) => put('/enterprise/info', data)

/**
 * 删除企业信息
 * @param {number} id - 企业ID
 * @returns {Promise}
 */
export const deleteEnterpriseInfo = (id) => del('/enterprise/info/' + id)

// ==================== 服务请求 /enterprise/service ====================

/**
 * 服务请求分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getServicePage = (params) => get('/enterprise/service/page', params)

/**
 * 服务请求详情
 * @param {number} id - 请求ID
 * @returns {Promise}
 */
export const getService = (id) => get('/enterprise/service/' + id)

/**
 * 根据企业ID获取服务请求列表
 * @param {number} enterpriseId - 企业ID
 * @returns {Promise}
 */
export const getServiceByEnterpriseId = (enterpriseId) => get('/enterprise/service/enterprise/' + enterpriseId)

/**
 * 服务类型统计
 * @returns {Promise}
 */
export const getServiceTypeStatistics = () => get('/enterprise/service/statistics/type')

/**
 * 服务状态统计
 * @returns {Promise}
 */
export const getServiceStatusStatistics = () => get('/enterprise/service/statistics/status')

/**
 * 新增服务请求
 * @param {object} data - 请求数据
 * @returns {Promise}
 */
export const addService = (data) => post('/enterprise/service', data)

/**
 * 修改服务请求
 * @param {object} data - 请求数据
 * @returns {Promise}
 */
export const updateService = (data) => put('/enterprise/service', data)

/**
 * 处理服务请求
 * @param {number} id - 请求ID
 * @param {object} params - 处理参数 { handlerId, handleResult }
 * @returns {Promise}
 */
export const handleService = (id, params) => put('/enterprise/service/handle/' + id, params)

/**
 * 关闭服务请求
 * @param {number} id - 请求ID
 * @returns {Promise}
 */
export const closeService = (id) => put('/enterprise/service/close/' + id)

/**
 * 删除服务请求
 * @param {number} id - 请求ID
 * @returns {Promise}
 */
export const deleteService = (id) => del('/enterprise/service/' + id)

// ==================== 政策信息 /enterprise/policy ====================

/**
 * 政策信息分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getPolicyPage = (params) => get('/enterprise/policy/page', params)

/**
 * 政策信息详情
 * @param {number} id - 政策ID
 * @returns {Promise}
 */
export const getPolicy = (id) => get('/enterprise/policy/' + id)

/**
 * 已发布政策列表
 * @returns {Promise}
 */
export const getPublishedPolicy = () => get('/enterprise/policy/published')

/**
 * 根据类型获取政策列表
 * @param {string} type - 类型
 * @returns {Promise}
 */
export const getPolicyByType = (type) => get('/enterprise/policy/type/' + type)

/**
 * 新增政策信息
 * @param {object} data - 政策数据
 * @returns {Promise}
 */
export const addPolicy = (data) => post('/enterprise/policy', data)

/**
 * 修改政策信息
 * @param {object} data - 政策数据
 * @returns {Promise}
 */
export const updatePolicy = (data) => put('/enterprise/policy', data)

/**
 * 发布政策
 * @param {number} id - 政策ID
 * @returns {Promise}
 */
export const publishPolicy = (id) => put('/enterprise/policy/publish/' + id)

/**
 * 过期政策
 * @param {number} id - 政策ID
 * @returns {Promise}
 */
export const expirePolicy = (id) => put('/enterprise/policy/expire/' + id)

/**
 * 删除政策信息
 * @param {number} id - 政策ID
 * @returns {Promise}
 */
export const deletePolicy = (id) => del('/enterprise/policy/' + id)
