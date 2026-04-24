import { defineStore } from 'pinia'
import { login as loginApi, logout as logoutApi, getUserInfo as getUserInfoApi, getRoutes as getUserRoutesApi } from '@/api/auth'
import { getToken, setToken, removeToken } from '@/utils/auth'

/**
 * 用户状态管理
 */
const useUserStore = defineStore('user', {
  state: () => ({
    // 访问令牌
    token: getToken() || '',
    // 用户信息
    userInfo: {},
    // 用户角色
    roles: [],
    // 用户权限
    permissions: [],
    // 动态路由
    routers: []
  }),

  getters: {
    /**
     * 获取用户名
     */
    username: (state) => state.userInfo.username || '',

    /**
     * 获取用户头像
     */
    avatar: (state) => state.userInfo.avatar || '',

    /**
     * 是否已登录
     */
    isLoggedIn: (state) => !!state.token
  },

  actions: {
    /**
     * 用户登录
     * @param {object} loginForm - 登录表单 { username, password, rememberMe }
     * @returns {Promise}
     */
    async login(loginForm) {
      try {
        const res = await loginApi(loginForm)
        const token = res.token || (res.data && res.data.token)
        this.token = token
        setToken(token, loginForm.rememberMe)
        return res
      } catch (error) {
        return Promise.reject(error)
      }
    },

    /**
     * 获取用户信息
     * @returns {Promise}
     */
    async getInfo() {
      try {
        const res = await getUserInfoApi()
        // 兼容拦截器包装：res 可能是对象或被包装过
        const data = (res && res.user) ? res : (res && res.data) ? res.data : res
        this.userInfo = data.user || data
        this.roles = data.roles || []
        this.permissions = data.permissions || []
        return data
      } catch (error) {
        return Promise.reject(error)
      }
    },

    /**
     * 获取动态路由
     * @returns {Promise}
     */
    async getRoutes() {
      try {
        const res = await getUserRoutesApi()
        // 兼容拦截器包装：可能是数组、{rows} 或 {data}
        let data = res
        if (data && !Array.isArray(data)) {
          data = data.rows || data.data || data.records || []
        }
        this.routers = data
        return data
      } catch (error) {
        return Promise.reject(error)
      }
    },

    /**
     * 退出登录
     */
    async logout() {
      try {
        await logoutApi()
      } catch (error) {
        // 忽略登出接口错误，继续清理本地状态
      } finally {
        this.token = ''
        this.userInfo = {}
        this.roles = []
        this.permissions = []
        this.routers = []
        removeToken()
        const { default: router } = await import('@/router')
        router.push('/login')
      }
    }
  }
})

/**
 * 应用全局状态管理
 */
const useAppStore = defineStore('app', {
  state: () => ({
    // 侧边栏是否折叠
    sidebarCollapsed: false,
    // 设备类型：desktop / mobile
    device: 'desktop',
    // 标签页列表
    visitedViews: [],
    // 缓存页面列表
    cachedViews: []
  }),

  actions: {
    /**
     * 切换侧边栏折叠状态
     */
    toggleSidebar() {
      this.sidebarCollapsed = !this.sidebarCollapsed
    },

    /**
     * 设置设备类型
     * @param {string} device - 设备类型
     */
    setDevice(device) {
      this.device = device
    },

    /**
     * 添加标签页
     * @param {object} view - 路由对象
     */
    addView(view) {
      if (this.visitedViews.some((v) => v.path === view.path)) return
      this.visitedViews.push(
        Object.assign({}, view, {
          title: view.meta.title || '未命名'
        })
      )
    },

    /**
     * 删除标签页
     * @param {string} path - 路由路径
     */
    delView(path) {
      const index = this.visitedViews.findIndex((v) => v.path === path)
      if (index > -1) {
        this.visitedViews.splice(index, 1)
      }
    },

    /**
     * 删除其他标签页
     * @param {string} path - 保留的路由路径
     */
    delOtherViews(path) {
      this.visitedViews = this.visitedViews.filter((v) => v.path === path)
    },

    /**
     * 删除所有标签页
     */
    delAllViews() {
      this.visitedViews = []
    },

    /**
     * 添加缓存页面
     * @param {object} view - 路由对象
     */
    addCachedView(view) {
      if (!view.meta.noCache && this.cachedViews.includes(view.name)) return
      if (view.meta.noCache) return
      this.cachedViews.push(view.name)
    },

    /**
     * 删除缓存页面
     * @param {string} name - 路由名称
     */
    delCachedView(name) {
      const index = this.cachedViews.indexOf(name)
      if (index > -1) {
        this.cachedViews.splice(index, 1)
      }
    }
  }
})

/**
 * 字典缓存状态管理
 */
const useDictStore = defineStore('dict', {
  state: () => ({
    // 字典缓存数据
    dictMap: new Map()
  }),

  actions: {
    /**
     * 获取字典数据
     * @param {string} dictType - 字典类型
     * @returns {Array}
     */
    getDict(dictType) {
      if (this.dictMap.has(dictType)) {
        return this.dictMap.get(dictType)
      }
      return []
    },

    /**
     * 设置字典数据
     * @param {string} dictType - 字典类型
     * @param {Array} data - 字典数据
     */
    setDict(dictType, data) {
      this.dictMap.set(dictType, data)
    },

    /**
     * 清空字典缓存
     */
    clearDict() {
      this.dictMap.clear()
    },

    /**
     * 移除指定字典缓存
     * @param {string} dictType - 字典类型
     */
    removeDict(dictType) {
      this.dictMap.delete(dictType)
    }
  }
})

export { useUserStore, useAppStore, useDictStore }
