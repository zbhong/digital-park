import axios from 'axios'

// 外部设备系统（starnet）专用请求实例
// 该系统接口格式与主系统不同，使用独立的 axios 实例
const starnetRequest = axios.create({
  baseURL: '/starnet-api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})

// 请求拦截器
starnetRequest.interceptors.request.use(
  config => {
    // starnet 系统可能需要单独的认证，暂时透传 token
    const token = localStorage.getItem('token') || sessionStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
  },
  error => Promise.reject(error)
)

// 响应拦截器
starnetRequest.interceptors.response.use(
  res => {
    const data = res.data
    // 兼容若依框架的响应格式
    if (data.code === 200 || data.code === 0) {
      return data
    }
    return Promise.reject(new Error(data.msg || '请求失败'))
  },
  error => Promise.reject(error)
)

/**
 * 获取设备机器列表（starnet 系统）
 * 接口地址: /starnet/settings/tDeviceMachine/tDeviceMachineList
 * @param {Object} params - 查询参数
 * @param {number} params.pageNum - 页码
 * @param {number} params.pageSize - 每页数量
 * @param {string} [params.machineName] - 设备名称（模糊搜索）
 * @param {string} [params.machineType] - 设备类型
 * @param {string} [params.status] - 设备状态
 * @returns {Promise<{rows: Array, total: number}>}
 */
export function getDeviceMachineList(params) {
  return starnetRequest({
    url: '/starnet/settings/tDeviceMachine/tDeviceMachineList',
    method: 'get',
    params
  })
}

/**
 * 获取设备机器详情
 * @param {string|number} id - 设备ID
 */
export function getDeviceMachineDetail(id) {
  return starnetRequest({
    url: '/starnet/settings/tDeviceMachine/' + id,
    method: 'get'
  })
}

/**
 * 新增设备机器
 * @param {Object} data - 设备数据
 */
export function addDeviceMachine(data) {
  return starnetRequest({
    url: '/starnet/settings/tDeviceMachine',
    method: 'post',
    data
  })
}

/**
 * 修改设备机器
 * @param {Object} data - 设备数据
 */
export function updateDeviceMachine(data) {
  return starnetRequest({
    url: '/starnet/settings/tDeviceMachine',
    method: 'put',
    data
  })
}

/**
 * 删除设备机器
 * @param {string|number} id - 设备ID
 */
export function deleteDeviceMachine(id) {
  return starnetRequest({
    url: '/starnet/settings/tDeviceMachine/' + id,
    method: 'delete'
  })
}
