<template>
  <el-menu-item
    v-if="(!item.children || item.children.length === 0) && !isHidden"
    :index="resolvePath(item.path)"
    @click="handleMenuClick(item)"
  >
    <el-icon v-if="getIconName(item)">
      <component :is="getIconName(item)" />
    </el-icon>
    <el-icon v-else>
      <Document />
    </el-icon>
    <template #title>{{ getTitle(item) }}</template>
  </el-menu-item>

  <el-sub-menu
    v-else
    :index="resolvePath(item.path)"
  >
    <template #title>
      <el-icon v-if="getIconName(item)">
        <component :is="getIconName(item)" />
      </el-icon>
      <el-icon v-else>
        <Folder />
      </el-icon>
      <span>{{ getTitle(item) }}</span>
    </template>

    <sidebar-item
      v-for="child in visibleChildren"
      :key="child.path"
      :item="child"
      :base-path="resolvePath(item.path)"
    />
  </el-sub-menu>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { Document, Folder } from '@element-plus/icons-vue'

const props = defineProps({
  item: {
    type: Object,
    required: true
  },
  basePath: {
    type: String,
    default: ''
  }
})

const router = useRouter()

const isHidden = computed(() => props.item.hidden || props.item.meta?.hidden)

const visibleChildren = computed(() => {
  return (props.item.children || []).filter(child => !child.hidden && !child.meta?.hidden)
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
 * 解析完整路径
 * 将相对路径与 basePath 拼接成绝对路径
 */
function resolvePath(routePath) {
  if (routePath.startsWith('/') || routePath.startsWith('http')) {
    return routePath
  }
  let fullPath
  if (props.basePath) {
    fullPath = `${props.basePath}/${routePath}`.replace(/\/+/g, '/')
  } else {
    fullPath = `/${routePath}`.replace(/\/+/g, '/')
  }
  // 确保返回绝对路径
  if (!fullPath.startsWith('/') && !fullPath.startsWith('http')) {
    fullPath = `/${fullPath}`
  }
  return fullPath
}

function handleMenuClick(menuRoute) {
  const fullPath = resolvePath(menuRoute.path)
  if (fullPath.startsWith('http')) {
    window.open(fullPath, '_blank')
  } else {
    router.push(fullPath)
  }
}
</script>
