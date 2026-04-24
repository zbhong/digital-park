<template>
  <div class="config-page dark-config-theme">
    <!-- 顶部 -->
    <div class="config-header">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>驾驶舱</el-breadcrumb-item>
        <el-breadcrumb-item>可视化配置</el-breadcrumb-item>
      </el-breadcrumb>
      <div class="header-title">
        <span class="title-text">可视化配置管理</span>
        <span class="title-desc">底图管理、点位编辑、设备绑定、样式配置、接口配置、发布管理</span>
      </div>
    </div>

    <!-- 主体 -->
    <div class="config-body">
      <!-- 左侧导航 -->
      <div class="left-panel">
        <div class="panel-section-title">
          <el-icon><Grid /></el-icon>
          <span>配置管理</span>
        </div>
        <div class="config-menu">
          <div
            v-for="item in menuItems"
            :key="item.key"
            class="menu-item"
            :class="{ active: activeMenu === item.key }"
            @click="activeMenu = item.key"
          >
            <el-icon><component :is="item.icon" /></el-icon>
            <span>{{ item.label }}</span>
          </div>
        </div>
      </div>

      <!-- 右侧内容 -->
      <div class="content-panel">
        <component :is="currentComponent" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, shallowRef, markRaw } from 'vue'
import { Grid } from '@element-plus/icons-vue'
import BaseMapManager from './components/BaseMapManager.vue'
import PointEditor from './components/PointEditor.vue'
import DeviceBinding from './components/DeviceBinding.vue'
import StyleConfig from './components/StyleConfig.vue'
import ApiConfig from './components/ApiConfig.vue'
import PublishManager from './components/PublishManager.vue'
import ConfigLog from './components/ConfigLog.vue'

const activeMenu = ref('basemap')

const menuItems = [
  { key: 'basemap', label: '底图管理', icon: 'MapLocation' },
  { key: 'pointeditor', label: '点位编辑器', icon: 'EditPen' },
  { key: 'devicebind', label: '设备绑定', icon: 'Connection' },
  { key: 'styleconfig', label: '样式配置', icon: 'Brush' },
  { key: 'apiconfig', label: '接口配置', icon: 'Link' },
  { key: 'publish', label: '发布管理', icon: 'Upload' },
  { key: 'log', label: '操作日志', icon: 'Document' }
]

const componentMap = {
  basemap: markRaw(BaseMapManager),
  pointeditor: markRaw(PointEditor),
  devicebind: markRaw(DeviceBinding),
  styleconfig: markRaw(StyleConfig),
  apiconfig: markRaw(ApiConfig),
  publish: markRaw(PublishManager),
  log: markRaw(ConfigLog)
}

const currentComponent = computed(() => componentMap[activeMenu.value])
</script>

<style scoped lang="scss">
@import './styles/config.scss';
@import '../styles/twin.scss';

.config-page {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  background: linear-gradient(180deg, #0A1628 0%, #0D1F3C 50%, #0A1628 100%);
  overflow: hidden;

  .config-header {
    padding: 12px 20px;
    border-bottom: 1px solid rgba(64, 128, 255, 0.15);
    background: rgba(10, 22, 40, 0.5);
    flex-shrink: 0;

    :deep(.el-breadcrumb__inner) { color: #667799 !important; }
    :deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) { color: #E0E8FF !important; }

    .header-title {
      margin-top: 6px;
      .title-text { font-size: 16px; font-weight: 600; color: #E0E8FF; }
      .title-desc { display: block; font-size: 12px; color: #667799; margin-top: 2px; }
    }
  }

  .config-body {
    flex: 1;
    display: flex;
    gap: 12px;
    padding: 12px;
    min-height: 0;

    .left-panel {
      width: 200px;
      flex-shrink: 0;
      border-radius: 12px;
      background: rgba(16, 32, 64, 0.5);
      border: 1px solid rgba(64, 128, 255, 0.15);
      display: flex;
      flex-direction: column;
      overflow: hidden;

      .panel-section-title {
        padding: 12px 16px;
        font-size: 13px;
        font-weight: 600;
        color: #E0E8FF;
        display: flex;
        align-items: center;
        gap: 6px;
        border-bottom: 1px solid rgba(64, 128, 255, 0.1);
      }

      .config-menu {
        flex: 1;
        padding: 8px;

        .menu-item {
          display: flex;
          align-items: center;
          gap: 8px;
          padding: 10px 12px;
          border-radius: 8px;
          cursor: pointer;
          font-size: 13px;
          color: #8899BB;
          transition: all 0.2s;
          margin-bottom: 4px;

          &:hover { background: rgba(64, 128, 255, 0.1); color: #E0E8FF; }
          &.active {
            background: rgba(0, 229, 255, 0.12);
            color: #00E5FF;
            border: 1px solid rgba(0, 229, 255, 0.25);
          }
        }
      }
    }

    .content-panel {
      flex: 1;
      border-radius: 12px;
      background: rgba(16, 32, 64, 0.5);
      border: 1px solid rgba(64, 128, 255, 0.15);
      overflow: hidden;
      display: flex;
      flex-direction: column;
    }
  }
}
</style>
