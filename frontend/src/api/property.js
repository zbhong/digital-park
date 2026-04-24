import { get, post, put, del } from '@/utils/request'

// ==================== 物业费用 /property/fee ====================

/**
 * 费用分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getFeePage = (params) => get('/property/fee/page', params)

/**
 * 费用详情
 * @param {number} id - 费用ID
 * @returns {Promise}
 */
export const getFee = (id) => get('/property/fee/' + id)

/**
 * 根据企业ID获取费用列表
 * @param {number} enterpriseId - 企业ID
 * @returns {Promise}
 */
export const getFeeByEnterpriseId = (enterpriseId) => get('/property/fee/enterprise/' + enterpriseId)

/**
 * 企业未缴费总额
 * @param {number} enterpriseId - 企业ID
 * @returns {Promise}
 */
export const getUnpaidTotal = (enterpriseId) => get('/property/fee/unpaid/' + enterpriseId)

/**
 * 费用统计
 * @returns {Promise}
 */
export const getFeeStatistics = () => get('/property/fee/statistics')

/**
 * 费用类型统计
 * @returns {Promise}
 */
export const getFeeTypeStatistics = () => get('/property/fee/statistics/type')

/**
 * 新增费用
 * @param {object} data - 费用数据
 * @returns {Promise}
 */
export const addFee = (data) => post('/property/fee', data)

/**
 * 缴费
 * @param {number} id - 费用ID
 * @returns {Promise}
 */
export const payFee = (id) => put('/property/fee/pay/' + id)

/**
 * 批量缴费
 * @param {object} data - 费用ID列表
 * @returns {Promise}
 */
export const batchPayFee = (data) => put('/property/fee/batch-pay', data)

/**
 * 删除费用
 * @param {number} id - 费用ID
 * @returns {Promise}
 */
export const deleteFee = (id) => del('/property/fee/' + id)

// ==================== 物业工单 /property/work-order ====================

/**
 * 工单分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getWorkOrderPage = (params) => get('/property/work-order/page', params)

/**
 * 工单详情
 * @param {number} id - 工单ID
 * @returns {Promise}
 */
export const getWorkOrder = (id) => get('/property/work-order/' + id)

/**
 * 根据企业ID获取工单列表
 * @param {number} enterpriseId - 企业ID
 * @returns {Promise}
 */
export const getWorkOrderByEnterpriseId = (enterpriseId) => get('/property/work-order/enterprise/' + enterpriseId)

/**
 * 工单类型统计
 * @returns {Promise}
 */
export const getWorkOrderTypeStatistics = () => get('/property/work-order/statistics/type')

/**
 * 工单状态统计
 * @returns {Promise}
 */
export const getWorkOrderStatusStatistics = () => get('/property/work-order/statistics/status')

/**
 * 工单优先级统计
 * @returns {Promise}
 */
export const getWorkOrderPriorityStatistics = () => get('/property/work-order/statistics/priority')

/**
 * 新增工单
 * @param {object} data - 工单数据
 * @returns {Promise}
 */
export const addWorkOrder = (data) => post('/property/work-order', data)

/**
 * 修改工单
 * @param {object} data - 工单数据
 * @returns {Promise}
 */
export const updateWorkOrder = (data) => put('/property/work-order', data)

/**
 * 派单
 * @param {number} id - 工单ID
 * @param {object} params - 派单参数 { handlerId }
 * @returns {Promise}
 */
export const dispatchWorkOrder = (id, params) => put('/property/work-order/dispatch/' + id, params)

/**
 * 处理工单
 * @param {number} id - 工单ID
 * @param {object} params - 处理参数 { handleResult }
 * @returns {Promise}
 */
export const handleWorkOrder = (id, params) => put('/property/work-order/handle/' + id, params)

/**
 * 完成工单
 * @param {number} id - 工单ID
 * @returns {Promise}
 */
export const completeWorkOrder = (id) => put('/property/work-order/complete/' + id)

/**
 * 评价工单
 * @param {number} id - 工单ID
 * @param {object} params - 评价参数 { satisfaction }
 * @returns {Promise}
 */
export const evaluateWorkOrder = (id, params) => put('/property/work-order/evaluate/' + id, params)

/**
 * 删除工单
 * @param {number} id - 工单ID
 * @returns {Promise}
 */
export const deleteWorkOrder = (id) => del('/property/work-order/' + id)

// ==================== 访客管理 /property/visitor ====================

/**
 * 访客分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getVisitorPage = (params) => get('/property/visitor/page', params)

/**
 * 访客详情
 * @param {number} id - 访客ID
 * @returns {Promise}
 */
export const getVisitor = (id) => get('/property/visitor/' + id)

/**
 * 新增访客
 * @param {object} data - 访客数据
 * @returns {Promise}
 */
export const addVisitor = (data) => post('/property/visitor', data)

/**
 * 修改访客
 * @param {object} data - 访客数据
 * @returns {Promise}
 */
export const updateVisitor = (data) => put('/property/visitor', data)

/**
 * 审批访客
 * @param {number} id - 访客ID
 * @returns {Promise}
 */
export const approveVisitor = (id) => put('/property/visitor/approve/' + id)

/**
 * 拒绝访客
 * @param {number} id - 访客ID
 * @returns {Promise}
 */
export const rejectVisitor = (id) => put('/property/visitor/reject/' + id)

/**
 * 访客离开
 * @param {number} id - 访客ID
 * @returns {Promise}
 */
export const leaveVisitor = (id) => put('/property/visitor/leave/' + id)

/**
 * 删除访客
 * @param {number} id - 访客ID
 * @returns {Promise}
 */
export const deleteVisitor = (id) => del('/property/visitor/' + id)

// ==================== 车辆管理 /property/vehicle ====================

/**
 * 车辆分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getVehiclePage = (params) => get('/property/vehicle/page', params)

/**
 * 车辆详情
 * @param {number} id - 车辆ID
 * @returns {Promise}
 */
export const getVehicle = (id) => get('/property/vehicle/' + id)

/**
 * 根据车牌号查询车辆
 * @param {string} plateNumber - 车牌号
 * @returns {Promise}
 */
export const getVehicleByPlateNumber = (plateNumber) => get('/property/vehicle/plate/' + plateNumber)

/**
 * 新增车辆
 * @param {object} data - 车辆数据
 * @returns {Promise}
 */
export const addVehicle = (data) => post('/property/vehicle', data)

/**
 * 修改车辆
 * @param {object} data - 车辆数据
 * @returns {Promise}
 */
export const updateVehicle = (data) => put('/property/vehicle', data)

/**
 * 禁用车辆
 * @param {number} id - 车辆ID
 * @returns {Promise}
 */
export const disableVehicle = (id) => put('/property/vehicle/disable/' + id)

/**
 * 启用车辆
 * @param {number} id - 车辆ID
 * @returns {Promise}
 */
export const enableVehicle = (id) => put('/property/vehicle/enable/' + id)

/**
 * 删除车辆
 * @param {number} id - 车辆ID
 * @returns {Promise}
 */
export const deleteVehicle = (id) => del('/property/vehicle/' + id)

// ==================== 设施运维 /property/facility ====================

/**
 * 设施分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getFacilityPage = (params) => get('/property/facility/page', params)

/**
 * 设施详情
 * @param {number} id - 设施ID
 * @returns {Promise}
 */
export const getFacility = (id) => get('/property/facility/' + id)

/**
 * 根据类型获取设施列表
 * @param {string} type - 类型
 * @returns {Promise}
 */
export const getFacilityByType = (type) => get('/property/facility/type/' + type)

/**
 * 根据区域获取设施列表
 * @param {number} areaId - 区域ID
 * @returns {Promise}
 */
export const getFacilityByAreaId = (areaId) => get('/property/facility/area/' + areaId)

/**
 * 巡检提醒列表
 * @returns {Promise}
 */
export const getCheckReminders = () => get('/property/facility/check-reminders')

/**
 * 新增设施
 * @param {object} data - 设施数据
 * @returns {Promise}
 */
export const addFacility = (data) => post('/property/facility', data)

/**
 * 修改设施
 * @param {object} data - 设施数据
 * @returns {Promise}
 */
export const updateFacility = (data) => put('/property/facility', data)

/**
 * 设施巡检
 * @param {number} id - 设施ID
 * @returns {Promise}
 */
export const checkFacility = (id) => put('/property/facility/check/' + id)

/**
 * 删除设施
 * @param {number} id - 设施ID
 * @returns {Promise}
 */
export const deleteFacility = (id) => del('/property/facility/' + id)
