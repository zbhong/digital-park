import { get, post, put, del } from '@/utils/request'

// ==================== 底图管理 ====================
export function getBaseMapList(params) {
  return get('/twin/config/basemap/list', params)
}
export function getBaseMap(id) {
  return get('/twin/config/basemap/' + id)
}
export function addBaseMap(data) {
  return post('/twin/config/basemap', data)
}
export function updateBaseMap(data) {
  return put('/twin/config/basemap', data)
}
export function deleteBaseMap(id) {
  return del('/twin/config/basemap/' + id)
}
export function toggleBaseMapGrid(id, showGrid) {
  return put('/twin/config/basemap/grid', { id, showGrid })
}

// ==================== 点位管理 ====================
export function getPointList(params) {
  return get('/twin/config/point/list', params)
}
export function getPoint(id) {
  return get('/twin/config/point/' + id)
}
export function addPoint(data) {
  return post('/twin/config/point', data)
}
export function updatePoint(data) {
  return put('/twin/config/point', data)
}
export function deletePoint(id) {
  return del('/twin/config/point/' + id)
}
export function batchSavePoints(data) {
  return post('/twin/config/point/batch', data)
}
export function getPointGroups() {
  return get('/twin/config/point/groups')
}

// ==================== 设备绑定 ====================
export function getDeviceBindings(params) {
  return get('/twin/config/binding/list', params)
}
export function bindDevice(data) {
  return post('/twin/config/binding', data)
}
export function unbindDevice(id) {
  return del('/twin/config/binding/' + id)
}
export function batchBindDevice(data) {
  return post('/twin/config/binding/batch', data)
}
export function getDeviceOptions(params) {
  return get('/twin/config/binding/devices', params)
}

// ==================== 样式配置 ====================
export function getStyleList(params) {
  return get('/twin/config/style/list', params)
}
export function getStyle(id) {
  return get('/twin/config/style/' + id)
}
export function addStyle(data) {
  return post('/twin/config/style', data)
}
export function updateStyle(data) {
  return put('/twin/config/style', data)
}
export function deleteStyle(id) {
  return del('/twin/config/style/' + id)
}
export function applyStyleToGroup(data) {
  return post('/twin/config/style/apply', data)
}

// ==================== 接口配置 ====================
export function getApiConfigList(params) {
  return get('/twin/config/api/list', params)
}
export function getApiConfig(id) {
  return get('/twin/config/api/' + id)
}
export function addApiConfig(data) {
  return post('/twin/config/api', data)
}
export function updateApiConfig(data) {
  return put('/twin/config/api', data)
}
export function deleteApiConfig(id) {
  return del('/twin/config/api/' + id)
}
export function testApiConfig(data) {
  return post('/twin/config/api/test', data)
}
export function getAlertRules(params) {
  return get('/twin/config/api/alert-rules', params)
}
export function addAlertRule(data) {
  return post('/twin/config/api/alert-rules', data)
}
export function updateAlertRule(data) {
  return put('/twin/config/api/alert-rules', data)
}
export function deleteAlertRule(id) {
  return del('/twin/config/api/alert-rules/' + id)
}

// ==================== 发布管理 ====================
export function getVersionList(params) {
  return get('/twin/config/publish/versions', params)
}
export function saveDraft(data) {
  return post('/twin/config/publish/draft', data)
}
export function publishConfig(data) {
  return post('/twin/config/publish', data)
}
export function rollbackVersion(id) {
  return post('/twin/config/publish/rollback/' + id)
}
export function exportConfig(params) {
  return get('/twin/config/publish/export', params)
}
export function importConfig(data) {
  return post('/twin/config/publish/import', data)
}
export function prePublishCheck() {
  return get('/twin/config/publish/check')
}

// ==================== 操作日志 ====================
export function getConfigLogs(params) {
  return get('/twin/config/log/list', params)
}
