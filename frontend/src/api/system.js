import { get, post, put, del } from '@/utils/request'

// ==================== 用户管理 /system/user ====================

/**
 * 获取用户列表
 * @param {object} params - 查询参数 { current, size, keyword }
 * @returns {Promise}
 */
export const listUser = (params) => get('/system/user/list', params)

/**
 * 获取用户详情
 * @param {number} userId - 用户ID
 * @returns {Promise}
 */
export const getUser = (userId) => get('/system/user/' + userId)

/**
 * 新增用户
 * @param {object} data - 用户数据
 * @returns {Promise}
 */
export const addUser = (data) => post('/system/user', data)

/**
 * 修改用户
 * @param {object} data - 用户数据
 * @returns {Promise}
 */
export const updateUser = (data) => put('/system/user', data)

/**
 * 删除用户
 * @param {string} userIds - 用户ID
 * @returns {Promise}
 */
export const deleteUser = (userIds) => del('/system/user/' + userIds)

/**
 * 重置密码
 * @param {object} data - { userId, newPassword }
 * @returns {Promise}
 */
export const resetUserPwd = (data) => put('/system/user/resetPassword', data)

/**
 * 分配角色
 * @param {object} params - { userId }
 * @param {object} data - 角色ID列表
 * @returns {Promise}
 */
export const assignUserRole = (params, data) => put('/system/user/assignRole', data, params)

/**
 * 修改用户状态
 * @param {object} data - { userId, status }
 * @returns {Promise}
 */
export const changeUserStatus = (data) => put('/system/user/changeStatus', data)

// ==================== 角色管理 /system/role ====================

/**
 * 获取角色列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const listRole = (params) => get('/system/role/list', params)

/**
 * 获取角色详情
 * @param {number} roleId - 角色ID
 * @returns {Promise}
 */
export const getRole = (roleId) => get('/system/role/' + roleId)

/**
 * 新增角色
 * @param {object} data - 角色数据
 * @returns {Promise}
 */
export const addRole = (data) => post('/system/role', data)

/**
 * 修改角色
 * @param {object} data - 角色数据
 * @returns {Promise}
 */
export const updateRole = (data) => put('/system/role', data)

/**
 * 删除角色
 * @param {string} roleIds - 角色ID
 * @returns {Promise}
 */
export const deleteRole = (roleIds) => del('/system/role/' + roleIds)

/**
 * 获取角色选项列表
 * @returns {Promise}
 */
export const getRoleOptionSelect = () => get('/system/role/all')

/**
 * 分配菜单权限
 * @param {object} params - { roleId }
 * @param {object} data - 菜单ID列表
 * @returns {Promise}
 */
export const assignRoleMenu = (params, data) => put('/system/role/assignMenu', data, params)

/**
 * 获取角色菜单ID列表
 * @param {number} roleId - 角色ID
 * @returns {Promise}
 */
export const getRoleMenus = (roleId) => get('/system/role/menus/' + roleId)

// ==================== 菜单管理 /system/menu ====================

/**
 * 获取菜单列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const listMenu = (params) => get('/system/menu/list', params)

/**
 * 获取菜单详情
 * @param {number} menuId - 菜单ID
 * @returns {Promise}
 */
export const getMenu = (menuId) => get('/system/menu/' + menuId)

/**
 * 新增菜单
 * @param {object} data - 菜单数据
 * @returns {Promise}
 */
export const addMenu = (data) => post('/system/menu', data)

/**
 * 修改菜单
 * @param {object} data - 菜单数据
 * @returns {Promise}
 */
export const updateMenu = (data) => put('/system/menu', data)

/**
 * 删除菜单
 * @param {number} menuId - 菜单ID
 * @returns {Promise}
 */
export const deleteMenu = (menuId) => del('/system/menu/' + menuId)

// ==================== 组织/部门管理 /system/org ====================

/**
 * 获取组织树形列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const listDept = (params) => get('/system/org/list', params)

/**
 * 获取组织详情
 * @param {number} deptId - 组织ID
 * @returns {Promise}
 */
export const getDept = (deptId) => get('/system/org/' + deptId)

/**
 * 新增组织
 * @param {object} data - 组织数据
 * @returns {Promise}
 */
export const addDept = (data) => post('/system/org', data)

/**
 * 修改组织
 * @param {object} data - 组织数据
 * @returns {Promise}
 */
export const updateDept = (data) => put('/system/org', data)

/**
 * 删除组织
 * @param {number} deptId - 组织ID
 * @returns {Promise}
 */
export const deleteDept = (deptId) => del('/system/org/' + deptId)

/**
 * 获取组织树选择列表
 * @returns {Promise}
 */
export const getDeptTreeSelect = () => get('/system/org/tree')

// ==================== 字典管理 /system/dict ====================

/**
 * 获取字典类型列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const listDictType = (params) => get('/system/dict/list', params)

/**
 * 获取字典类型详情
 * @param {number} dictId - 字典ID
 * @returns {Promise}
 */
export const getDictType = (dictId) => get('/system/dict/type/' + dictId)

/**
 * 新增字典类型
 * @param {object} data - 字典类型数据
 * @returns {Promise}
 */
export const addDictType = (data) => post('/system/dict/type', data)

/**
 * 修改字典类型
 * @param {object} data - 字典类型数据
 * @returns {Promise}
 */
export const updateDictType = (data) => put('/system/dict/type', data)

/**
 * 删除字典类型
 * @param {string} dictIds - 字典ID
 * @returns {Promise}
 */
export const deleteDictType = (dictIds) => del('/system/dict/type/' + dictIds)

/**
 * 刷新字典缓存
 * @returns {Promise}
 */
export const refreshCache = () => post('/system/dict/type/refreshCache')

/**
 * 获取字典数据列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const listDictData = (params) => get('/system/dict/data/list', params)

/**
 * 获取字典数据详情
 * @param {number} dictCode - 字典数据编码
 * @returns {Promise}
 */
export const getDictData = (dictCode) => get('/system/dict/data/' + dictCode)

/**
 * 新增字典数据
 * @param {object} data - 字典数据
 * @returns {Promise}
 */
export const addDictData = (data) => post('/system/dict/data', data)

/**
 * 修改字典数据
 * @param {object} data - 字典数据
 * @returns {Promise}
 */
export const updateDictData = (data) => put('/system/dict/data', data)

/**
 * 删除字典数据
 * @param {string} dictCodes - 字典数据编码
 * @returns {Promise}
 */
export const deleteDictData = (dictCodes) => del('/system/dict/data/' + dictCodes)

/**
 * 根据字典类型获取字典数据
 * @param {string} dictType - 字典类型
 * @returns {Promise}
 */
export const getDicts = (dictType) => get('/system/dict/data/type/' + dictType)

// ==================== 系统配置 /system/config ====================

/**
 * 获取配置列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const listConfig = (params) => get('/system/config/list', params)

/**
 * 获取配置详情
 * @param {number} configId - 配置ID
 * @returns {Promise}
 */
export const getConfig = (configId) => get('/system/config/' + configId)

/**
 * 根据配置键名获取配置值
 * @param {string} configKey - 配置键名
 * @returns {Promise}
 */
export const getConfigKey = (configKey) => get('/system/config/' + configKey)

/**
 * 新增配置
 * @param {object} data - 配置数据
 * @returns {Promise}
 */
export const addConfig = (data) => post('/system/config', data)

/**
 * 修改配置
 * @param {object} data - 配置数据
 * @returns {Promise}
 */
export const updateConfig = (data) => put('/system/config', data)

/**
 * 删除配置
 * @param {string} configIds - 配置ID
 * @returns {Promise}
 */
export const deleteConfig = (configIds) => del('/system/config/' + configIds)

/**
 * 刷新配置缓存
 * @returns {Promise}
 */
export const refreshConfigCache = () => post('/system/config/refreshCache')

// ==================== 操作日志 /system/log ====================

/**
 * 获取操作日志列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const listOperLog = (params) => get('/system/log/list', params)

/**
 * 获取操作日志详情
 * @param {number} operId - 日志ID
 * @returns {Promise}
 */
export const getOperLog = (operId) => get('/system/log/' + operId)

/**
 * 删除操作日志
 * @param {string} operIds - 日志ID
 * @returns {Promise}
 */
export const deleteOperLog = (operIds) => del('/system/log/' + operIds)

/**
 * 清空操作日志
 * @returns {Promise}
 */
export const cleanOperLog = () => del('/system/log/clear')

// ==================== 登录日志 /system/loginLog ====================

/**
 * 获取登录日志列表
 * @param {object} params - 查询参数
 * @returns {Promise}
 */
export const listLoginLog = (params) => get('/system/loginLog/list', params)

/**
 * 删除登录日志
 * @param {string} infoIds - 日志ID
 * @returns {Promise}
 */
export const deleteLoginLog = (infoIds) => del('/system/loginLog/' + infoIds)

/**
 * 清空登录日志
 * @returns {Promise}
 */
export const cleanLoginLog = () => del('/system/loginLog/clear')

/**
 * 解锁账号
 * @param {string} username - 用户名
 * @returns {Promise}
 */
export const unlockLoginUser = (username) => get('/system/loginLog/unlock/' + username)
