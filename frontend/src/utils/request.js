import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, removeToken } from '@/utils/auth'

// 创建 axios 实例
const service = axios.create({
  // 基础请求路径
  baseURL: '/api',
  // 请求超时时间（毫秒）
  timeout: 30000,
  // 请求头配置
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    // 在请求发送前添加 Token
    const token = getToken()
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  (error) => {
    console.error('请求错误：', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    // 获取响应数据
    const res = response.data

    // 如果返回的状态码不是200，说明接口有问题
    if (res.code && res.code !== 200) {
      // 401: Token 过期或未登录
      if (res.code === 401) {
        removeToken()
        // 不在这里执行 router.push，让路由守卫统一处理重定向
        // 避免与路由守卫的 catch 块产生重复重定向
        return Promise.reject(new Error(res.msg || '未授权'))
      }

      // 403: 没有权限
      if (res.code === 403) {
        ElMessage.error('没有操作权限')
        return Promise.reject(new Error(res.msg || '禁止访问'))
      }

      // 500: 服务器错误
      if (res.code === 500) {
        ElMessage.error(res.msg || '服务器内部错误')
        return Promise.reject(new Error(res.msg || '服务器内部错误'))
      }

      // 其他错误
      ElMessage.error(res.msg || '请求失败')
      return Promise.reject(new Error(res.msg || '请求失败'))
    }

    // 返回业务数据部分
    let result = res.data !== undefined ? res.data : res
    // 兼容处理：如果 result 是数组，包装为 {rows, total} 对象
    if (Array.isArray(result)) {
      result = { rows: result, total: result.length }
    }
    // 兼容处理：如果 result 有 records 字段，同时挂载到 rows 上
    if (result && typeof result === 'object' && result.records && !result.rows) {
      result.rows = result.records
    }
    return result
  },
  (error) => {
    console.error('响应错误：', error)

    // HTTP 状态码错误处理
    let message = '网络异常，请稍后重试'
    if (error.response) {
      switch (error.response.status) {
        case 400:
          message = '请求参数错误'
          break
        case 401:
          message = '未授权，请重新登录'
          removeToken()
          // 不在这里执行 router.push，让路由守卫统一处理重定向
          break
        case 403:
          message = '拒绝访问'
          break
        case 404:
          message = '请求地址不存在'
          break
        case 408:
          message = '请求超时'
          break
        case 500:
          message = '服务器内部错误'
          break
        case 502:
          message = '网关错误'
          break
        case 503:
          message = '服务不可用'
          break
        case 504:
          message = '网关超时'
          break
        default:
          message = `请求失败(${error.response.status})`
      }
    } else if (error.code === 'ECONNABORTED') {
      message = '请求超时，请稍后重试'
    } else if (error.message.includes('Network Error')) {
      message = '网络连接异常'
    }

    ElMessage.error(message)
    return Promise.reject(error)
  }
)

/**
 * GET 请求
 * @param {string} url 请求地址
 * @param {object} params 请求参数
 * @returns {Promise}
 */
export function get(url, params) {
  return service({
    method: 'get',
    url,
    params
  })
}

/**
 * POST 请求
 * @param {string} url 请求地址
 * @param {object} data 请求体数据
 * @returns {Promise}
 */
export function post(url, data) {
  return service({
    method: 'post',
    url,
    data
  })
}

/**
 * PUT 请求
 * @param {string} url 请求地址
 * @param {object} data 请求体数据
 * @returns {Promise}
 */
export function put(url, data) {
  return service({
    method: 'put',
    url,
    data
  })
}

/**
 * DELETE 请求
 * @param {string} url 请求地址
 * @param {object} params 请求参数
 * @returns {Promise}
 */
export function del(url, params) {
  return service({
    method: 'delete',
    url,
    params
  })
}

export default service
