<template>
  <div class="sidebar">
    <!-- Logo 区域 -->
    <div class="sidebar-logo" :class="{ collapse: appStore.sidebarCollapsed }">
      <div class="logo-icon-wrapper">
        <svg viewBox="0 0 40 40" class="logo-icon">
          <circle cx="20" cy="20" r="18" fill="none" stroke="#409eff" stroke-width="2" />
          <path d="M22 9 L17 21 L21 21 L18 31 L27 19 L22 19 Z" fill="#409eff" />
        </svg>
      </div>
      <span v-show="!appStore.sidebarCollapsed" class="logo-title">银河数字园区</span>
    </div>

    <!-- 菜单区域 -->
    <el-scrollbar>
      <el-menu
        :default-active="activeMenu"
        :collapse="appStore.sidebarCollapsed"
        :collapse-transition="false"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
        :unique-opened="true"
        @select="handleSelect"
      >
        <!-- 静态路由菜单（首页） -->
        <template v-for="route in constantMenuRoutes" :key="route.path">
          <!-- 无子路由的菜单项 -->
          <el-menu-item
            v-if="!route.children || route.children.length === 0"
            :index="route.path.startsWith('/') ? route.path : '/' + route.path"
            @click="handleMenuClick(route)"
          >
            <el-icon v-if="getIconName(route)">
              <component :is="getIconName(route)" />
            </el-icon>
            <el-icon v-else>
              <Document />
            </el-icon>
            <template #title>{{ getTitle(route) }}</template>
          </el-menu-item>

          <!-- 只有一个子路由的菜单项 -->
          <el-menu-item
            v-else-if="route.children.length === 1 && !route.children[0].children"
            :index="getFullPath(route.path, route.children[0].path)"
            @click="handleMenuClick(route.children[0], route.path)"
          >
            <el-icon v-if="getIconName(route.children[0]) || getIconName(route)">
              <component :is="getIconName(route.children[0]) || getIconName(route)" />
            </el-icon>
            <el-icon v-else>
              <Document />
            </el-icon>
            <template #title>{{ getTitle(route.children[0]) }}</template>
          </el-menu-item>

          <!-- 多级子菜单 -->
          <el-sub-menu
            v-else
            :index="route.path.startsWith('/') ? route.path : '/' + route.path"
          >
            <template #title>
              <el-icon v-if="getIconName(route)">
                <component :is="getIconName(route)" />
              </el-icon>
              <el-icon v-else>
                <Folder />
              </el-icon>
              <span>{{ getTitle(route) }}</span>
            </template>

            <!-- 递归渲染子菜单 -->
            <sidebar-item
              v-for="child in route.children"
              :key="child.path"
              :item="child"
              :base-path="route.path"
            />
          </el-sub-menu>
        </template>

        <!-- 动态路由菜单（后端返回） -->
        <template v-for="route in dynamicMenuRoutes" :key="route.path">
          <!-- 无子路由的菜单项 -->
          <el-menu-item
            v-if="!route.children || route.children.length === 0"
            :index="route.path"
            @click="handleMenuClick(route)"
          >
            <el-icon v-if="route.meta?.icon">
              <component :is="route.meta.icon" />
            </el-icon>
            <template #title>{{ route.meta?.title }}</template>
          </el-menu-item>

          <!-- 只有一个子路由的菜单项 -->
          <el-menu-item
            v-else-if="route.children.length === 1 && !route.children[0].children"
            :index="getFullPath(route.path, route.children[0].path)"
            @click="handleMenuClick(route.children[0], route.path)"
          >
            <el-icon v-if="route.children[0].meta?.icon || route.meta?.icon">
              <component :is="route.children[0].meta?.icon || route.meta.icon" />
            </el-icon>
            <template #title>{{ route.children[0].meta?.title }}</template>
          </el-menu-item>

          <!-- 多级子菜单 -->
          <el-sub-menu
            v-else
            :index="route.path"
          >
            <template #title>
              <el-icon v-if="route.meta?.icon">
                <component :is="route.meta.icon" />
              </el-icon>
              <span>{{ route.meta?.title }}</span>
            </template>

            <!-- 递归渲染子菜单 -->
            <sidebar-item
              v-for="child in route.children"
              :key="child.path"
              :item="child"
              :base-path="route.path"
            />
          </el-sub-menu>
        </template>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore, useUserStore } from '@/store/modules'
import SidebarItem from './SidebarItem.vue'
import { Document, Folder } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()
const userStore = useUserStore()

/**
 * 当前激活的菜单路径
 */
const activeMenu = computed(() => {
  return route.path
})

/**
 * 静态路由菜单（首页等）
 */
const constantMenuRoutes = computed(() => {
  return router.getRoutes().filter((r) => {
    return (
      !r.meta?.hidden &&
      r.children?.length > 0 &&
      r.path === '/' // 只有根路由下的静态菜单
    )
  })
})

/**
 * 动态路由菜单（后端返回的菜单数据）
 */
const dynamicMenuRoutes = computed(() => {
  const routers = userStore.routers || []
  return routers.filter((r) => !r.hidden).map(r => {
    if (r.children) {
      return {
        ...r,
        children: r.children.filter(c => !c.hidden && !c.meta?.hidden)
      }
    }
    return r
  })
})

/**
 * 获取图标名称，处理各种图标格式
 */
function getIconName(item) {
  const icon = item.meta?.icon || item.icon
  if (!icon) {
    return null
  }

  // 处理各种可能的图标格式
  let iconName = String(icon)

  // 移除可能的前缀
  iconName = iconName.replace(/^icon-/, '')
  iconName = iconName.replace(/^el-icon-/, '')

  // 首字母大写
  iconName = iconName.charAt(0).toUpperCase() + iconName.slice(1)

  return iconName
}

/**
 * 获取菜单标题
 */
function getTitle(item) {
  return item.meta?.title || item.title || '未命名'
}

/**
 * 拼接完整路径
 */
function getFullPath(parentPath, childPath) {
  if (!childPath || childPath.trim() === '') {
    return parentPath
  }
  if (childPath.startsWith('/') || childPath.startsWith('http')) {
    return childPath
  }
  return `${parentPath}/${childPath}`.replace(/\/+/g, '/')
}

/**
 * 菜单点击事件
 * @param {object} menuRoute - 菜单路由对象
 * @param {string} parentPath - 父级路径（可选）
 */
function handleMenuClick(menuRoute, parentPath) {
  let fullPath = menuRoute.path
  if (parentPath && !fullPath.startsWith('/') && !fullPath.startsWith('http')) {
    fullPath = `${parentPath}/${fullPath}`.replace(/\/+/g, '/')
  }
  if (fullPath.startsWith('http')) {
    window.open(fullPath, '_blank')
  }
}

/**
 * el-menu select 事件处理
 * @param {string} index - 被选中的菜单项的 index（即路由路径）
 */
function handleSelect(index) {
  if (index && !index.startsWith('http')) {
    router.push(index).catch(() => {})
  }
}
</script>

<style lang="scss" scoped>
.sidebar {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.sidebar-logo {
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 12px;
  background-color: #263445;
  overflow: hidden;
  transition: all 0.28s ease;

  .logo-icon-wrapper {
    flex-shrink: 0;

    .logo-icon {
      width: 32px;
      height: 32px;
      filter: drop-shadow(0 0 8px rgba(64, 158, 255, 0.4));
    }
  }

  .logo-title {
    margin-left: 10px;
    font-size: 16px;
    font-weight: 600;
    color: #fff;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  &.collapse {
    padding: 0;
    justify-content: center;

    .logo-title {
      display: none;
    }
  }
}

// 菜单样式覆盖
:deep(.el-menu) {
  border-right: none;
}

:deep(.el-menu-item) {
  height: 50px;
  line-height: 50px;

  &:hover {
    background-color: #263445 !important;
  }

  &.is-active {
    background-color: #263445 !important;
    color: #409eff !important;

    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 0;
      bottom: 0;
      width: 3px;
      background-color: #409eff;
    }
  }
}

:deep(.el-sub-menu) {
  .el-sub-menu__title {
    height: 50px;
    line-height: 50px;

    &:hover {
      background-color: #263445 !important;
    }
  }

  .el-menu-item {
    padding-left: 50px !important;
    min-width: auto;
  }
}
</style>
