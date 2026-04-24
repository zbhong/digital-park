import { get, post, put } from '@/utils/request'

/**
 * 用户登录
 * @param {object} data - 登录表单数据 { username, password }
 * @returns {Promise}
 */
export const login = (data) => post('/auth/login', data)

/**
 * 用户登出
 * @returns {Promise}
 */
export const logout = () => post('/auth/logout')

/**
 * 获取当前用户信息
 * @returns {Promise}
 */
export const getUserInfo = () => get('/auth/userInfo')

/**
 * 获取用户路由
 * @returns {Promise}
 */
export const getRoutes = () => get('/auth/routes')

/**
 * 获取当前用户详情
 * @returns {Promise}
 */
export const getUserDetail = () => get('/auth/userDetail')

/**
 * 修改密码
 * @param {object} data - 密码数据 { oldPassword, newPassword, confirmPassword }
 * @returns {Promise}
 */
export const changePassword = (data) => put('/auth/password', data)
