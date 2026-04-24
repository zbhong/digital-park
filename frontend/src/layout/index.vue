<template>
  <el-container class="app-wrapper" :class="{ 'sidebar-collapsed': appStore.sidebarCollapsed }">
    <!-- 侧边栏 -->
    <el-aside :width="appStore.sidebarCollapsed ? '64px' : '210px'" class="sidebar-container">
      <Sidebar />
    </el-aside>

    <el-container class="main-container">
      <!-- 顶部导航栏 -->
      <el-header class="header-container" height="50px">
        <Navbar />
      </el-header>

      <!-- 标签页导航 -->
      <TagsView />

      <!-- 主内容区 -->
      <el-main class="app-main">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <component :is="Component" :key="route.fullPath" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { useAppStore } from '@/store/modules'
import Sidebar from './components/Sidebar.vue'
import Navbar from './components/Navbar.vue'
import TagsView from './components/TagsView.vue'

const route = useRoute()
const appStore = useAppStore()
</script>

<style lang="scss" scoped>
.app-wrapper {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.sidebar-container {
  height: 100%;
  overflow: hidden;
  background-color: #304156;
  transition: width 0.28s ease;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);

  :deep(.el-menu) {
    border-right: none;
    height: 100%;
    overflow-y: auto;
    overflow-x: hidden;
  }

  // 滚动条样式
  :deep(.el-menu::-webkit-scrollbar) {
    width: 6px;
  }

  :deep(.el-menu::-webkit-scrollbar-thumb) {
    background-color: rgba(144, 147, 153, 0.3);
    border-radius: 3px;
  }
}

.main-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

.header-container {
  padding: 0;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  z-index: 10;
}

.app-main {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  background-color: #f0f2f5;

  // 驾驶舱页面全屏模式：透明背景、无内边距、隐藏溢出
  &:has(.cockpit-container) {
    background-color: transparent !important;
    padding: 0 !important;
    overflow: hidden !important;
  }
}

// 侧边栏折叠状态
.sidebar-collapsed {
  .sidebar-container {
    width: 64px !important;
  }

  :deep(.hideSidebar) {
    .sidebar-logo {
      width: 64px;
    }
  }
}

// 过渡动画
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: opacity 0.3s, transform 0.3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
