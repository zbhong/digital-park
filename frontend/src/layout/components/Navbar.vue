<template>
  <div class="navbar">
    <!-- 左侧：折叠按钮 + 面包屑 -->
    <div class="navbar-left">
      <!-- 侧边栏折叠/展开按钮 -->
      <div class="hamburger" @click="appStore.toggleSidebar">
        <el-icon :size="20">
          <component :is="appStore.sidebarCollapsed ? 'Expand' : 'Fold'" />
        </el-icon>
      </div>

      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="breadcrumb">
        <el-breadcrumb-item
          v-for="(item, index) in breadcrumbs"
          :key="item.path"
        >
          <span v-if="index === breadcrumbs.length - 1" class="no-redirect">
            {{ item.meta?.title }}
          </span>
          <a v-else class="redirect-link" @click.prevent="handleLink(item)">
            {{ item.meta?.title }}
          </a>
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <!-- 右侧：功能按钮 + 用户信息 -->
    <div class="navbar-right">
      <!-- 全屏按钮 -->
      <div class="navbar-action" @click="toggleFullscreen">
        <el-icon :size="18">
          <FullScreen />
        </el-icon>
      </div>

      <!-- 用户头像下拉菜单 -->
      <el-dropdown trigger="click" @command="handleCommand">
        <div class="user-info">
          <el-avatar :size="30" :src="userStore.avatar || undefined">
            <el-icon><UserFilled /></el-icon>
          </el-avatar>
          <span class="username">{{ userStore.userInfo.nickname || userStore.userInfo.username || '用户' }}</span>
          <el-icon class="arrow"><ArrowDown /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon>
              个人中心
            </el-dropdown-item>
            <el-dropdown-item command="password">
              <el-icon><Lock /></el-icon>
              修改密码
            </el-dropdown-item>
            <el-dropdown-item divided command="logout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore, useUserStore } from '@/store/modules'
import { ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()
const userStore = useUserStore()

/**
 * 面包屑数据
 */
const breadcrumbs = computed(() => {
  const matched = route.matched.filter(
    (item) => item.meta && item.meta.title
  )
  // 如果第一个不是首页，则添加首页
  if (matched.length > 0 && matched[0].path !== '/dashboard') {
    matched.unshift({
      path: '/dashboard',
      meta: { title: '首页' }
    })
  }
  return matched
})

/**
 * 面包屑链接点击
 * @param {object} item - 路由对象
 */
function handleLink(item) {
  const { redirect, path } = item
  if (redirect) {
    router.push(redirect)
    return
  }
  router.push(path)
}

/**
 * 切换全屏
 */
function toggleFullscreen() {
  if (!document.fullscreenElement) {
    document.documentElement.requestFullscreen()
  } else {
    document.exitFullscreen()
  }
}

/**
 * 用户下拉菜单命令处理
 * @param {string} command - 命令类型
 */
function handleCommand(command) {
  switch (command) {
    case 'profile':
      router.push('/user/profile')
      break
    case 'password':
      router.push('/user/password')
      break
    case 'logout':
      handleLogout()
      break
  }
}

/**
 * 退出登录
 */
function handleLogout() {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logout()
  })
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  background-color: #fff;
}

.navbar-left {
  display: flex;
  align-items: center;
}

.hamburger {
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s;

  &:hover {
    background-color: #f0f2f5;
  }
}

.breadcrumb {
  margin-left: 12px;
  font-size: 14px;

  .no-redirect {
    color: #97a8be;
    cursor: text;
  }

  .redirect-link {
    color: #606266;
    cursor: pointer;

    &:hover {
      color: #409eff;
    }
  }
}

.navbar-right {
  display: flex;
  align-items: center;
}

.navbar-action {
  cursor: pointer;
  padding: 6px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background-color 0.3s;

  &:hover {
    background-color: #f0f2f5;
  }
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background-color 0.3s;

  &:hover {
    background-color: #f0f2f5;
  }

  .username {
    margin-left: 8px;
    font-size: 14px;
    color: #606266;
    max-width: 120px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .arrow {
    margin-left: 4px;
    font-size: 12px;
    color: #909399;
  }
}
</style>
