import { get, post, put, del } from '@/utils/request'

// ==================== 招商房源 /investment/asset ====================

/**
 * 房源分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getInvestmentAssetPage = (params) => get('/investment/asset/page', params)

/**
 * 房源详情
 * @param {number} id - 房源ID
 * @returns {Promise}
 */
export const getInvestmentAsset = (id) => get('/investment/asset/' + id)

/**
 * 根据楼栋获取房源列表
 * @param {number} buildingId - 楼栋ID
 * @returns {Promise}
 */
export const getAssetByBuildingId = (buildingId) => get('/investment/asset/building/' + buildingId)

/**
 * 可用房源列表
 * @returns {Promise}
 */
export const getAvailableAsset = () => get('/investment/asset/available')

/**
 * 租控数据
 * @returns {Promise}
 */
export const getRentControlData = () => get('/investment/asset/rent-control')

/**
 * 可用面积
 * @returns {Promise}
 */
export const getAvailableArea = () => get('/investment/asset/available-area')

/**
 * 新增房源
 * @param {object} data - 房源数据
 * @returns {Promise}
 */
export const addInvestmentAsset = (data) => post('/investment/asset', data)

/**
 * 修改房源
 * @param {object} data - 房源数据
 * @returns {Promise}
 */
export const updateInvestmentAsset = (data) => put('/investment/asset', data)

/**
 * 删除房源
 * @param {number} id - 房源ID
 * @returns {Promise}
 */
export const deleteInvestmentAsset = (id) => del('/investment/asset/' + id)

// ==================== 兼容性别名（供 Vue 组件使用） ====================

/**
 * 房源分页列表（别名）
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getAssetPage = (params) => get('/investment/asset/page', params)

/**
 * 房源详情（别名）
 * @param {number} id - 房源ID
 * @returns {Promise}
 */
export const getAsset = (id) => get('/investment/asset/' + id)

/**
 * 新增房源（别名）
 * @param {object} data - 房源数据
 * @returns {Promise}
 */
export const addAsset = (data) => post('/investment/asset', data)

/**
 * 修改房源（别名）
 * @param {object} data - 房源数据
 * @returns {Promise}
 */
export const updateAsset = (data) => put('/investment/asset', data)

/**
 * 删除房源（别名）
 * @param {number} id - 房源ID
 * @returns {Promise}
 */
export const deleteAsset = (id) => del('/investment/asset/' + id)

// ==================== 客户管理 /investment/customer ====================

/**
 * 客户分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getCustomerPage = (params) => get('/investment/customer/page', params)

/**
 * 客户详情
 * @param {number} id - 客户ID
 * @returns {Promise}
 */
export const getCustomer = (id) => get('/investment/customer/' + id)

/**
 * 公共池客户列表
 * @returns {Promise}
 */
export const getPublicPool = () => get('/investment/customer/public-pool')

/**
 * 根据跟进人获取客户列表
 * @param {number} followUserId - 跟进人ID
 * @returns {Promise}
 */
export const getCustomerByFollowUser = (followUserId) => get('/investment/customer/follow-user/' + followUserId)

/**
 * 客户状态统计
 * @returns {Promise}
 */
export const getCustomerStatusStatistics = () => get('/investment/customer/statistics/status')

/**
 * 客户来源统计
 * @returns {Promise}
 */
export const getCustomerSourceStatistics = () => get('/investment/customer/statistics/source')

/**
 * 获取客户跟进记录
 * @param {number} customerId - 客户ID
 * @returns {Promise}
 */
export const getFollowRecords = (customerId) => get('/investment/customer/' + customerId + '/follow-records')

/**
 * 新增客户
 * @param {object} data - 客户数据
 * @returns {Promise}
 */
export const addCustomer = (data) => post('/investment/customer', data)

/**
 * 修改客户
 * @param {object} data - 客户数据
 * @returns {Promise}
 */
export const updateCustomer = (data) => put('/investment/customer', data)

/**
 * 分配客户
 * @param {number} id - 客户ID
 * @param {object} params - 分配参数 { followUserId }
 * @returns {Promise}
 */
export const assignCustomer = (id, params) => put('/investment/customer/assign/' + id, params)

/**
 * 释放客户到公共池
 * @param {number} id - 客户ID
 * @returns {Promise}
 */
export const releaseCustomer = (id) => put('/investment/customer/release/' + id)

/**
 * 新增跟进记录
 * @param {object} data - 跟进记录数据
 * @returns {Promise}
 */
export const addFollowRecord = (data) => post('/investment/customer/follow', data)

/**
 * 删除客户
 * @param {number} id - 客户ID
 * @returns {Promise}
 */
export const deleteCustomer = (id) => del('/investment/customer/' + id)

// ==================== 商机管理 /investment/opportunity ====================

/**
 * 商机分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getOpportunityPage = (params) => get('/investment/opportunity/page', params)

/**
 * 商机详情
 * @param {number} id - 商机ID
 * @returns {Promise}
 */
export const getOpportunity = (id) => get('/investment/opportunity/' + id)

/**
 * 根据客户获取商机列表
 * @param {number} customerId - 客户ID
 * @returns {Promise}
 */
export const getOpportunityByCustomerId = (customerId) => get('/investment/opportunity/customer/' + customerId)

/**
 * 商机阶段统计
 * @returns {Promise}
 */
export const getOpportunityStageStatistics = () => get('/investment/opportunity/statistics/stage')

/**
 * 新增商机
 * @param {object} data - 商机数据
 * @returns {Promise}
 */
export const addOpportunity = (data) => post('/investment/opportunity', data)

/**
 * 修改商机
 * @param {object} data - 商机数据
 * @returns {Promise}
 */
export const updateOpportunity = (data) => put('/investment/opportunity', data)

/**
 * 推进商机阶段
 * @param {number} id - 商机ID
 * @param {object} params - 推进参数 { stage }
 * @returns {Promise}
 */
export const advanceOpportunityStage = (id, params) => put('/investment/opportunity/advance/' + id, params)

/**
 * 删除商机
 * @param {number} id - 商机ID
 * @returns {Promise}
 */
export const deleteOpportunity = (id) => del('/investment/opportunity/' + id)

// ==================== 合同管理 /investment/contract ====================

/**
 * 合同分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getContractPage = (params) => get('/investment/contract/page', params)

/**
 * 合同详情
 * @param {number} id - 合同ID
 * @returns {Promise}
 */
export const getContract = (id) => get('/investment/contract/' + id)

/**
 * 根据合同编号查询
 * @param {string} contractNo - 合同编号
 * @returns {Promise}
 */
export const getContractByNo = (contractNo) => get('/investment/contract/no/' + contractNo)

/**
 * 合同状态统计
 * @returns {Promise}
 */
export const getContractStatusStatistics = () => get('/investment/contract/statistics/status')

/**
 * 新增合同
 * @param {object} data - 合同数据
 * @returns {Promise}
 */
export const addContract = (data) => post('/investment/contract', data)

/**
 * 修改合同
 * @param {object} data - 合同数据
 * @returns {Promise}
 */
export const updateContract = (data) => put('/investment/contract', data)

/**
 * 提交合同审批
 * @param {number} id - 合同ID
 * @returns {Promise}
 */
export const submitContract = (id) => put('/investment/contract/submit/' + id)

/**
 * 审批通过合同
 * @param {number} id - 合同ID
 * @returns {Promise}
 */
export const approveContract = (id) => put('/investment/contract/approve/' + id)

/**
 * 驳回合同
 * @param {number} id - 合同ID
 * @returns {Promise}
 */
export const rejectContract = (id) => put('/investment/contract/reject/' + id)

/**
 * 终止合同
 * @param {number} id - 合同ID
 * @returns {Promise}
 */
export const terminateContract = (id) => put('/investment/contract/terminate/' + id)

/**
 * 删除合同
 * @param {number} id - 合同ID
 * @returns {Promise}
 */
export const deleteContract = (id) => del('/investment/contract/' + id)

// ==================== 招商政策 /investment/policy ====================

/**
 * 政策分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getInvestmentPolicyPage = (params) => get('/investment/policy/page', params)

/**
 * 政策详情
 * @param {number} id - 政策ID
 * @returns {Promise}
 */
export const getInvestmentPolicy = (id) => get('/investment/policy/' + id)

/**
 * 已启用政策列表
 * @returns {Promise}
 */
export const getEnabledPolicy = () => get('/investment/policy/enabled')

/**
 * 根据类型获取政策列表
 * @param {string} type - 类型
 * @returns {Promise}
 */
export const getPolicyByType = (type) => get('/investment/policy/type/' + type)

/**
 * 政策匹配
 * @param {object} params - 查询参数 { industry, scale }
 * @returns {Promise}
 */
export const matchPolicies = (params) => get('/investment/policy/match', params)

/**
 * 新增政策
 * @param {object} data - 政策数据
 * @returns {Promise}
 */
export const addInvestmentPolicy = (data) => post('/investment/policy', data)

/**
 * 修改政策
 * @param {object} data - 政策数据
 * @returns {Promise}
 */
export const updateInvestmentPolicy = (data) => put('/investment/policy', data)

/**
 * 启用政策
 * @param {number} id - 政策ID
 * @returns {Promise}
 */
export const enablePolicy = (id) => put('/investment/policy/enable/' + id)

/**
 * 禁用政策
 * @param {number} id - 政策ID
 * @returns {Promise}
 */
export const disablePolicy = (id) => put('/investment/policy/disable/' + id)

/**
 * 删除政策
 * @param {number} id - 政策ID
 * @returns {Promise}
 */
export const deleteInvestmentPolicy = (id) => del('/investment/policy/' + id)
