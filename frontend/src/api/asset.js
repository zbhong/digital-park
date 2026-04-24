import { get, post, put, del } from '@/utils/request'

// ==================== 资产分类 /asset/category ====================

/**
 * 资产分类树
 * @returns {Promise}
 */
export const getCategoryTree = () => get('/asset/category/tree')

/**
 * 资产分类列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getCategoryList = (params) => get('/asset/category/list', params)

/**
 * 资产分类详情
 * @param {number} id - 分类ID
 * @returns {Promise}
 */
export const getCategory = (id) => get('/asset/category/' + id)

/**
 * 新增资产分类
 * @param {object} data - 分类数据
 * @returns {Promise}
 */
export const addCategory = (data) => post('/asset/category', data)

/**
 * 修改资产分类
 * @param {object} data - 分类数据
 * @returns {Promise}
 */
export const updateCategory = (data) => put('/asset/category', data)

/**
 * 删除资产分类
 * @param {number} id - 分类ID
 * @returns {Promise}
 */
export const deleteCategory = (id) => del('/asset/category/' + id)

// ==================== 资产信息 /asset/info ====================

/**
 * 资产分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getAssetInfoPage = (params) => get('/asset/info/page', params)

/**
 * 资产详情
 * @param {number} id - 资产ID
 * @returns {Promise}
 */
export const getAssetInfo = (id) => get('/asset/info/' + id)

/**
 * 新增资产
 * @param {object} data - 资产数据
 * @returns {Promise}
 */
export const addAssetInfo = (data) => post('/asset/info', data)

/**
 * 修改资产
 * @param {object} data - 资产数据
 * @returns {Promise}
 */
export const updateAssetInfo = (data) => put('/asset/info', data)

/**
 * 删除资产
 * @param {number} id - 资产ID
 * @returns {Promise}
 */
export const deleteAssetInfo = (id) => del('/asset/info/' + id)

/**
 * 资产类型统计
 * @returns {Promise}
 */
export const getAssetStatisticsByType = () => get('/asset/info/statistics/type')

/**
 * 资产状态统计
 * @returns {Promise}
 */
export const getAssetStatisticsByStatus = () => get('/asset/info/statistics/status')

// ==================== 资产文档 /asset/document ====================

/**
 * 资产文档分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getDocumentPage = (params) => get('/asset/document/page', params)

/**
 * 根据资产ID获取文档列表
 * @param {number} assetId - 资产ID
 * @returns {Promise}
 */
export const getDocumentListByAssetId = (assetId) => get('/asset/document/asset/' + assetId)

/**
 * 资产文档详情
 * @param {number} id - 文档ID
 * @returns {Promise}
 */
export const getDocument = (id) => get('/asset/document/' + id)

/**
 * 新增资产文档
 * @param {object} data - 文档数据
 * @returns {Promise}
 */
export const addDocument = (data) => post('/asset/document', data)

/**
 * 修改资产文档
 * @param {object} data - 文档数据
 * @returns {Promise}
 */
export const updateDocument = (data) => put('/asset/document', data)

/**
 * 删除资产文档
 * @param {number} id - 文档ID
 * @returns {Promise}
 */
export const deleteDocument = (id) => del('/asset/document/' + id)

// ==================== 资产维保 /asset/maintenance ====================

/**
 * 资产维保分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getMaintenancePage = (params) => get('/asset/maintenance/page', params)

/**
 * 根据资产ID获取维保列表
 * @param {number} assetId - 资产ID
 * @returns {Promise}
 */
export const getMaintenanceListByAssetId = (assetId) => get('/asset/maintenance/asset/' + assetId)

/**
 * 资产维保详情
 * @param {number} id - 维保ID
 * @returns {Promise}
 */
export const getMaintenance = (id) => get('/asset/maintenance/' + id)

/**
 * 新增资产维保
 * @param {object} data - 维保数据
 * @returns {Promise}
 */
export const addMaintenance = (data) => post('/asset/maintenance', data)

/**
 * 修改资产维保
 * @param {object} data - 维保数据
 * @returns {Promise}
 */
export const updateMaintenance = (data) => put('/asset/maintenance', data)

/**
 * 删除资产维保
 * @param {number} id - 维保ID
 * @returns {Promise}
 */
export const deleteMaintenance = (id) => del('/asset/maintenance/' + id)

/**
 * 即将到期维保列表
 * @param {object} params - 查询参数 { days }
 * @returns {Promise}
 */
export const getExpiringMaintenance = (params) => get('/asset/maintenance/expiring', params)

// ==================== 资产盘点 /asset/inventory ====================

/**
 * 资产盘点分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getInventoryPage = (params) => get('/asset/inventory/page', params)

/**
 * 资产盘点详情
 * @param {number} id - 盘点ID
 * @returns {Promise}
 */
export const getInventory = (id) => get('/asset/inventory/' + id)

/**
 * 新增资产盘点
 * @param {object} data - 盘点数据
 * @returns {Promise}
 */
export const addInventory = (data) => post('/asset/inventory', data)

/**
 * 修改资产盘点
 * @param {object} data - 盘点数据
 * @returns {Promise}
 */
export const updateInventory = (data) => put('/asset/inventory', data)

/**
 * 删除资产盘点
 * @param {number} id - 盘点ID
 * @returns {Promise}
 */
export const deleteInventory = (id) => del('/asset/inventory/' + id)

/**
 * 完成盘点
 * @param {number} id - 盘点ID
 * @returns {Promise}
 */
export const completeInventory = (id) => put('/asset/inventory/' + id + '/complete')
