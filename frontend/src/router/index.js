import { createRouter, createWebHistory } from 'vue-router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken, removeToken } from '@/utils/auth'
import { useUserStore, useAppStore } from '@/store/modules'
import Layout from '@/layout/index.vue'

// NProgress 配置
NProgress.configure({ showSpinner: false })

/**
 * 使用 import.meta.glob 预加载所有视图组件
 * Vite 会在构建时将所有匹配的文件生成 chunk
 * eager: false 表示懒加载
 */
const viewModules = import.meta.glob('../views/**/*.vue')

/**
 * 根据组件路径获取懒加载组件
 * @param {string} componentPath - 组件路径，如 'system/user/index'
 * @returns {Function} 懒加载组件函数
 */
function resolveComponent(componentPath) {
  if (!componentPath) return undefined

  // 尝试多种路径格式
  const possiblePaths = [
    `../views/${componentPath}.vue`,
    `../views/${componentPath}/index.vue`,
    componentPath.startsWith('../views') ? componentPath : null,
    componentPath.endsWith('.vue') ? `../views/${componentPath}` : null
  ].filter(Boolean)

  for (const path of possiblePaths) {
    if (viewModules[path]) {
      return viewModules[path]
    }
  }

  console.warn('[Route] Component not found:', componentPath)
  return viewModules['../views/404.vue'] || (() => import('@/views/404.vue'))
}

/**
 * 公开路由（无需登录）
 */
const publicRoutes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', hidden: true }
  },
  {
    path: '/redirect/:path(.*)',
    component: Layout,
    meta: { hidden: true },
    children: [
      {
        path: '',
        component: () => import('@/views/login/index.vue'),
        meta: { hidden: true }
      }
    ]
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('@/views/404.vue'),
    meta: { title: '404', hidden: true }
  }
]

/**
 * 静态常量路由
 */
const constantRoutes = [
  ...publicRoutes,
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'HomeFilled', affix: true }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: constantRoutes,
  scrollBehavior: () => ({ top: 0 })
})

/**
 * 白名单路由（不需要登录即可访问）
 */
const whiteList = ['/login', '/404']

/**
 * 是否已添加动态路由标志
 */
let hasAddedRoutes = false

/**
 * 重置路由（用于退出登录时清理动态路由）
 */
export function resetRouter() {
  const routeNames = router.getRoutes().map((route) => route.name)
  routeNames.forEach((name) => {
    if (name && !constantRoutes.find((r) => r.name === name)) {
      router.removeRoute(name)
    }
  })
  hasAddedRoutes = false
}

/**
 * 将后端返回的路由数据转换为 Vue Router 格式
 *
 * 后端返回格式示例:
 * {
 *   path: "/twin",          // 父路由以 / 开头
 *   component: "Layout",
 *   children: [
 *     { path: "overview", component: "twin/overview/index", ... }  // 子路由是相对路径
 *   ]
 * }
 *
 * @param {Array} routes - 后端路由数据
 * @returns {Array} Vue Router 路由配置数组
 */
function generateRoutes(routes) {
  const result = []

  routes.forEach((route) => {
    // 确保父路由 path 以 / 开头
    const parentPath = route.path.startsWith('/') ? route.path : '/' + route.path

    const meta = {
      title: route.meta?.title || route.title || '',
      icon: route.meta?.icon || route.icon || '',
      hidden: route.hidden || false,
      noCache: route.meta?.noCache || false,
      affix: route.meta?.affix || false,
      permissions: route.meta?.permissions || []
    }

    // 判断是否为 Layout 父路由
    const isLayout = !route.component || route.component === 'Layout'

    // 过滤有效子路由：必须有 component 和非空 path
    const validChildren = (route.children || []).filter(
      child => child.component && child.path && child.path.trim() !== ''
    )

    if (isLayout && validChildren.length > 0) {
      // Layout 父路由 + 子路由
      const firstChildPath = validChildren[0].path

      const parentRoute = {
        path: parentPath,
        name: 'dynamic-' + parentPath.replace(/^\//, '').replace(/\//g, '-'),
        component: Layout,
        meta,
        redirect: parentPath + '/' + firstChildPath,
        children: validChildren.map((child) => {
          const childMeta = {
            title: child.meta?.title || child.title || '',
            icon: child.meta?.icon || child.icon || '',
            hidden: child.hidden || false,
            noCache: child.meta?.noCache || false,
            affix: child.meta?.affix || false,
            permissions: child.meta?.permissions || []
          }

          return {
            path: child.path,  // 后端返回的已经是相对路径，直接使用
            name: 'dynamic-' + parentPath.replace(/^\//, '').replace(/\//g, '-') + '-' + (child.name || child.component).replace(/\//g, '-'),
            component: resolveComponent(child.component),
            meta: childMeta
          }
        })
      }

      result.push(parentRoute)
    } else if (isLayout && validChildren.length === 0) {
      // Layout 父路由但没有有效子路由（如 ai-report 子路由 path 为空）
      // 检查子路由中是否有 component 但 path 为空的情况，用 index 作为默认 path
      const childrenWithComponent = (route.children || []).filter(
        child => child.component && (!child.path || child.path.trim() === '')
      )

      if (childrenWithComponent.length > 0) {
        // 将空 path 的子路由作为 index 子路由
        const child = childrenWithComponent[0]
        const childMeta = {
          title: child.meta?.title || child.title || '',
          icon: child.meta?.icon || child.icon || '',
          hidden: child.hidden || false,
          noCache: child.meta?.noCache || false,
          affix: child.meta?.affix || false,
          permissions: child.meta?.permissions || []
        }

        result.push({
          path: parentPath,
          name: 'dynamic-' + parentPath.replace(/^\//, '').replace(/\//g, '-'),
          component: Layout,
          meta,
          redirect: parentPath,
          children: [
            {
              path: '',  // 空路径匹配父路由自身
              name: 'dynamic-' + parentPath.replace(/^\//, '').replace(/\//g, '-') + '-' + (child.name || child.component).replace(/\//g, '-'),
              component: resolveComponent(child.component),
              meta: childMeta
            }
          ]
        })
      }
    } else if (route.component) {
      // 非 Layout 的独立路由
      result.push({
        path: parentPath,
        name: route.name || route.component,
        component: resolveComponent(route.component),
        meta
      })
    }
  })

  return result
}

// 路由守卫
router.beforeEach(async (to, from, next) => {
  NProgress.start()

  const token = getToken()

  if (token) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else {
      const userStore = useUserStore()
      const routesReady = userStore.roles.length > 0 && hasAddedRoutes

      if (routesReady) {
        next()
      } else {
        try {
          await userStore.getInfo()

          if (!hasAddedRoutes) {
            const asyncRouteData = await userStore.getRoutes()
            const dynamicRoutes = generateRoutes(asyncRouteData || [])

            dynamicRoutes.forEach((route) => {
              router.addRoute(route)
            })

            hasAddedRoutes = true

            // 重新导航到目标路由（确保动态路由已生效）
            next({ ...to, replace: true })
          } else {
            next()
          }
        } catch (error) {
          console.error('路由守卫错误：', error)
          // 清除所有用户状态，防止残留 roles 导致循环判断
          userStore.token = ''
          userStore.roles = []
          userStore.permissions = []
          userStore.routers = []
          removeToken()
          hasAddedRoutes = false
          // 避免重复重定向：如果目标已经是 login 则直接放行
          if (to.path === '/login') {
            next()
          } else {
            next(`/login?redirect=${to.path}`)
          }
          NProgress.done()
        }
      }
    }
  } else {
    if (whiteList.includes(to.path)) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach((to) => {
  NProgress.done()
  document.title = to.meta.title
    ? `${to.meta.title} - 银河数字园区一体化平台`
    : '银河数字园区一体化平台'
})

export default router
