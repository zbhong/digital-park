<template>
  <div class="cockpit-container">
    <!-- 顶部标题栏 + Tab -->
    <TopTabBar v-model="activeTab" />

    <!-- 三栏主体 -->
    <div class="cockpit-main" style="position: relative; z-index: 1;">
      <!-- 左侧数据面板 -->
      <LeftPanel :activeTab="activeTab" />

      <!-- 中间画布 -->
      <CenterCanvas />

      <!-- 右侧数据面板 -->
      <RightPanel :activeTab="activeTab" />
    </div>

    <!-- 底部装饰 -->
    <div class="cockpit-footer">
      <img src="./assets/footer-decor.png" alt="" class="footer-bg-img" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import TopTabBar from './components/TopTabBar.vue'
import LeftPanel from './components/LeftPanel.vue'
import RightPanel from './components/RightPanel.vue'
import CenterCanvas from './components/CenterCanvas.vue'

const activeTab = ref('overview')
</script>

<style lang="scss">
@import './styles/cockpit.scss';
</style>

<style lang="scss" scoped>
.cockpit-footer {
  width: 100%;
  height: 40px;
  flex-shrink: 0;
  position: relative;
  z-index: 10;
  pointer-events: none;
  overflow: hidden;

  .footer-bg-img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    object-position: center bottom;
  }
}
</style>
