import { get, post, put, del } from '@/utils/request'

// ==================== 概览 /twin ====================

/**
 * 园区全景概览
 * @returns {Promise}
 */
export const getTwinOverview = () => get('/twin/overview')

// ==================== 指标 /twin/indicators ====================

/**
 * 核心指标列表
 * @param {object} params - 查询参数 { category }
 * @returns {Promise}
 */
export const getTwinIndicators = (params) => get('/twin/indicators', params)

/**
 * 实时指标数据
 * @returns {Promise}
 */
export const getTwinIndicatorsRealtime = () => get('/twin/indicators/realtime')

// ==================== 告警 /twin/alerts ====================

/**
 * 告警分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getTwinAlerts = (params) => get('/twin/alerts', params)

/**
 * 处理告警
 * @param {number} id - 告警ID
 * @param {object} params - 处理参数 { handlerId, handleResult }
 * @returns {Promise}
 */
export const handleTwinAlert = (id, params) => put('/twin/alerts/' + id + '/handle', params)

/**
 * 告警统计
 * @returns {Promise}
 */
export const getTwinAlertStatistics = () => get('/twin/alerts/statistics')

// ==================== 能流 /twin ====================

/**
 * 能流数据
 * @returns {Promise}
 */
export const getTwinEnergyFlow = () => get('/twin/energy-flow')

/**
 * 碳流数据
 * @returns {Promise}
 */
export const getTwinCarbonFlow = () => get('/twin/carbon-flow')

// ==================== 安全态势 /twin ====================

/**
 * 安全态势数据
 * @returns {Promise}
 */
export const getTwinSafetyStatus = () => get('/twin/safety-status')

// ==================== 环境监测 /twin ====================

/**
 * 环境监测数据
 * @returns {Promise}
 */
export const getTwinEnvironmentStatus = () => get('/twin/environment-status')

// ==================== 建筑 /twin/buildings ====================

/**
 * 建筑分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getBuildingPage = (params) => get('/twin/buildings', params)

/**
 * 建筑详情
 * @param {number} id - 建筑ID
 * @returns {Promise}
 */
export const getBuilding = (id) => get('/twin/buildings/' + id)

/**
 * 根据区域获取建筑列表
 * @param {number} areaId - 区域ID
 * @returns {Promise}
 */
export const getBuildingsByArea = (areaId) => get('/twin/buildings/area/' + areaId)

/**
 * 新增建筑
 * @param {object} data - 建筑数据
 * @returns {Promise}
 */
export const addBuilding = (data) => post('/twin/buildings', data)

/**
 * 修改建筑
 * @param {object} data - 建筑数据
 * @returns {Promise}
 */
export const updateBuilding = (data) => put('/twin/buildings', data)

/**
 * 删除建筑
 * @param {number} id - 建筑ID
 * @returns {Promise}
 */
export const deleteBuilding = (id) => del('/twin/buildings/' + id)

// ==================== 区域 /twin/areas ====================

/**
 * 区域树
 * @returns {Promise}
 */
export const getAreaTree = () => get('/twin/areas')

/**
 * 区域分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getAreaPage = (params) => get('/twin/areas/page', params)

/**
 * 区域详情
 * @param {number} id - 区域ID
 * @returns {Promise}
 */
export const getArea = (id) => get('/twin/areas/' + id)

/**
 * 子区域列表
 * @param {number} parentId - 父区域ID
 * @returns {Promise}
 */
export const getAreaChildren = (parentId) => get('/twin/areas/children/' + parentId)

/**
 * 新增区域
 * @param {object} data - 区域数据
 * @returns {Promise}
 */
export const addArea = (data) => post('/twin/areas', data)

/**
 * 修改区域
 * @param {object} data - 区域数据
 * @returns {Promise}
 */
export const updateArea = (data) => put('/twin/areas', data)

/**
 * 删除区域
 * @param {number} id - 区域ID
 * @returns {Promise}
 */
export const deleteArea = (id) => del('/twin/areas/' + id)

// ==================== 配置-场景 /twin/config/scenes ====================

/**
 * 场景配置分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getSceneConfigPage = (params) => get('/twin/config/scenes', params)

/**
 * 所有场景配置
 * @returns {Promise}
 */
export const getSceneConfigAll = () => get('/twin/config/scenes/all')

/**
 * 场景配置详情
 * @param {number} id - 场景ID
 * @returns {Promise}
 */
export const getSceneConfig = (id) => get('/twin/config/scenes/' + id)

/**
 * 根据类型获取场景配置
 * @param {string} type - 类型
 * @returns {Promise}
 */
export const getSceneConfigByType = (type) => get('/twin/config/scenes/type/' + type)

/**
 * 新增场景配置
 * @param {object} data - 场景数据
 * @returns {Promise}
 */
export const addSceneConfig = (data) => post('/twin/config/scenes', data)

/**
 * 修改场景配置
 * @param {object} data - 场景数据
 * @returns {Promise}
 */
export const updateSceneConfig = (data) => put('/twin/config/scenes', data)

/**
 * 删除场景配置
 * @param {number} id - 场景ID
 * @returns {Promise}
 */
export const deleteSceneConfig = (id) => del('/twin/config/scenes/' + id)

// ==================== 配置-图层 /twin/config/layers ====================

/**
 * 图层配置分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getLayerConfigPage = (params) => get('/twin/config/layers', params)

/**
 * 所有图层配置
 * @returns {Promise}
 */
export const getLayerConfigAll = () => get('/twin/config/layers/all')

/**
 * 图层配置详情
 * @param {number} id - 图层ID
 * @returns {Promise}
 */
export const getLayerConfig = (id) => get('/twin/config/layers/' + id)

/**
 * 根据类型获取图层配置
 * @param {string} type - 类型
 * @returns {Promise}
 */
export const getLayerConfigByType = (type) => get('/twin/config/layers/type/' + type)

/**
 * 新增图层配置
 * @param {object} data - 图层数据
 * @returns {Promise}
 */
export const addLayerConfig = (data) => post('/twin/config/layers', data)

/**
 * 修改图层配置
 * @param {object} data - 图层数据
 * @returns {Promise}
 */
export const updateLayerConfig = (data) => put('/twin/config/layers', data)

/**
 * 删除图层配置
 * @param {number} id - 图层ID
 * @returns {Promise}
 */
export const deleteLayerConfig = (id) => del('/twin/config/layers/' + id)

// ==================== 配置-楼栋POI /twin/config/building-pois ====================

/**
 * 楼栋POI分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getBuildingPoiPage = (params) => get('/twin/config/building-pois', params)

/**
 * 楼栋POI详情
 * @param {number} id - POI ID
 * @returns {Promise}
 */
export const getBuildingPoi = (id) => get('/twin/config/building-pois/' + id)

/**
 * 根据楼栋获取POI列表
 * @param {number} buildingId - 楼栋ID
 * @returns {Promise}
 */
export const getBuildingPoisByBuilding = (buildingId) => get('/twin/config/building-pois/building/' + buildingId)

/**
 * 新增楼栋POI
 * @param {object} data - POI数据
 * @returns {Promise}
 */
export const addBuildingPoi = (data) => post('/twin/config/building-pois', data)

/**
 * 修改楼栋POI
 * @param {object} data - POI数据
 * @returns {Promise}
 */
export const updateBuildingPoi = (data) => put('/twin/config/building-pois', data)

/**
 * 删除楼栋POI
 * @param {number} id - POI ID
 * @returns {Promise}
 */
export const deleteBuildingPoi = (id) => del('/twin/config/building-pois/' + id)

// ==================== 配置-设备POI /twin/config/device-pois ====================

/**
 * 设备POI分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getDevicePoiPage = (params) => get('/twin/config/device-pois', params)

/**
 * 设备POI详情
 * @param {number} id - POI ID
 * @returns {Promise}
 */
export const getDevicePoi = (id) => get('/twin/config/device-pois/' + id)

/**
 * 根据设备类型获取POI列表
 * @param {string} deviceType - 设备类型
 * @returns {Promise}
 */
export const getDevicePoisByType = (deviceType) => get('/twin/config/device-pois/type/' + deviceType)

/**
 * 新增设备POI
 * @param {object} data - POI数据
 * @returns {Promise}
 */
export const addDevicePoi = (data) => post('/twin/config/device-pois', data)

/**
 * 修改设备POI
 * @param {object} data - POI数据
 * @returns {Promise}
 */
export const updateDevicePoi = (data) => put('/twin/config/device-pois', data)

/**
 * 批量保存设备POI
 * @param {object} data - POI数据
 * @returns {Promise}
 */
export const batchSaveDevicePoi = (data) => post('/twin/config/device-pois/batch', data)

/**
 * 删除设备POI
 * @param {number} id - POI ID
 * @returns {Promise}
 */
export const deleteDevicePoi = (id) => del('/twin/config/device-pois/' + id)

// ==================== 配置-指标 /twin/config/indicators ====================

/**
 * 指标配置分页列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const getIndicatorConfigPage = (params) => get('/twin/config/indicators', params)

/**
 * 指标配置详情
 * @param {number} id - 指标ID
 * @returns {Promise}
 */
export const getIndicatorConfig = (id) => get('/twin/config/indicators/' + id)

/**
 * 根据分类获取指标配置
 * @param {string} category - 分类
 * @returns {Promise}
 */
export const getIndicatorConfigByCategory = (category) => get('/twin/config/indicators/category/' + category)

/**
 * 新增指标配置
 * @param {object} data - 指标数据
 * @returns {Promise}
 */
export const addIndicatorConfig = (data) => post('/twin/config/indicators', data)

/**
 * 修改指标配置
 * @param {object} data - 指标数据
 * @returns {Promise}
 */
export const updateIndicatorConfig = (data) => put('/twin/config/indicators', data)

/**
 * 删除指标配置
 * @param {number} id - 指标ID
 * @returns {Promise}
 */
export const deleteIndicatorConfig = (id) => del('/twin/config/indicators/' + id)

// ==================== 视图 /twin/view ====================

/**
 * 获取场景完整数据(含图层、POI、指标)
 * @param {number} id - 场景ID
 * @returns {Promise}
 */
export const getSceneViewData = (id) => get('/twin/view/scene/' + id)

/**
 * 获取所有建筑及POI
 * @returns {Promise}
 */
export const getBuildingViewData = () => get('/twin/view/buildings')

/**
 * 获取所有设备POI
 * @returns {Promise}
 */
export const getDeviceViewData = () => get('/twin/view/devices')

/**
 * 获取图层列表
 * @returns {Promise}
 */
export const getLayerViewData = () => get('/twin/view/layers')

// ==================== 文件上传 /common ====================

/**
 * 文件上传
 * @param {File} file - 文件对象
 * @returns {Promise}
 */
export const uploadFile = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  return post('/common/upload', formData, { headers: { 'Content-Type': 'multipart/form-data' } })
}
