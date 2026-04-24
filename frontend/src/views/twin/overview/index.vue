<template>
  <div class="cockpit-viewport">
    <div class="cockpit-container" ref="cockpitRef">
      <!-- 顶部标题栏 + Tab -->
      <TopTabBar v-model="activeTab" />

      <!-- 三栏主体 -->
      <div class="cockpit-main">
        <!-- 左侧数据面板 -->
        <LeftPanel :activeTab="activeTab" />

        <!-- 中间画布 -->
        <CenterCanvas />

        <!-- 右侧数据面板 -->
        <RightPanel :activeTab="activeTab" />
      </div>

      <!-- 全屏切换按钮（放在 cockpit-main 外部避免 overflow:hidden 裁剪） -->
      <button class="fullscreen-toggle" :title="isFullscreen ? '退出全屏' : '全屏放大'" @click="toggleFullscreen">
        <svg v-if="!isFullscreen" viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <polyline points="15 3 21 3 21 9"></polyline>
          <polyline points="9 21 3 21 3 15"></polyline>
          <line x1="21" y1="3" x2="14" y2="10"></line>
          <line x1="3" y1="21" x2="10" y2="14"></line>
        </svg>
        <svg v-else viewBox="0 0 24 24" width="18" height="18" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <polyline points="4 14 10 14 10 20"></polyline>
          <polyline points="20 10 14 10 14 4"></polyline>
          <line x1="14" y1="10" x2="21" y2="3"></line>
          <line x1="3" y1="21" x2="10" y2="14"></line>
        </svg>
      </button>

      <!-- 底部装饰 -->
      <div class="cockpit-footer">
        <img src="./assets/footer-decor.png" alt="" class="footer-bg-img" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import TopTabBar from './components/TopTabBar.vue'
import LeftPanel from './components/LeftPanel.vue'
import RightPanel from './components/RightPanel.vue'
import CenterCanvas from './components/CenterCanvas.vue'

const activeTab = ref('overview')
const cockpitRef = ref(null)
const isFullscreen = ref(false)

// ---- 全屏切换 ----
function toggleFullscreen() {
  const el = cockpitRef.value
  if (!el) return
  if (!document.fullscreenElement) {
    el.requestFullscreen().then(() => {
      isFullscreen.value = true
    }).catch(() => {})
  } else {
    document.exitFullscreen().then(() => {
      isFullscreen.value = false
    }).catch(() => {})
  }
}

// ---- 16:9 等比缩放 ----
const DESIGN_WIDTH = 1920
const DESIGN_HEIGHT = 1080

function updateScale() {
  const el = cockpitRef.value
  if (!el) return
  const vw = window.innerWidth
  const vh = window.innerHeight
  const scaleX = vw / DESIGN_WIDTH
  const scaleY = vh / DESIGN_HEIGHT
  const scale = Math.min(scaleX, scaleY)
  el.style.transform = `scale(${scale})`
  el.style.transformOrigin = 'top left'
  // 居中偏移
  const actualW = DESIGN_WIDTH * scale
  const actualH = DESIGN_HEIGHT * scale
  el.style.marginLeft = `${(vw - actualW) / 2}px`
  el.style.marginTop = `${(vh - actualH) / 2}px`
}

let resizeTimer = null
function onResize() {
  clearTimeout(resizeTimer)
  resizeTimer = setTimeout(updateScale, 50)
}

onMounted(() => {
  updateScale()
  window.addEventListener('resize', onResize)
  document.addEventListener('fullscreenchange', onFullscreenChange)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', onResize)
  document.removeEventListener('fullscreenchange', onFullscreenChange)
  clearTimeout(resizeTimer)
})

function onFullscreenChange() {
  isFullscreen.value = !!document.fullscreenElement
  // 全屏切换后延迟重新计算缩放
  setTimeout(updateScale, 100)
}
</script>

<style lang="scss">
@import './styles/cockpit.scss';

// 全屏切换按钮（全局样式，放在 cockpit-container 内）
.cockpit-container {
  .fullscreen-toggle {
    position: absolute;
    top: 68px;
    right: 12px;
    z-index: 100;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    border-radius: 6px;
    border: 1px solid rgba(0, 229, 255, 0.3);
    background: rgba(2, 30, 77, 0.85);
    color: #00e5ff;
    cursor: pointer;
    padding: 0;
    transition: all 0.3s ease;
    backdrop-filter: blur(8px);

    &:hover {
      background: rgba(0, 229, 255, 0.15);
      border-color: rgba(0, 229, 255, 0.6);
      box-shadow: 0 0 12px rgba(0, 229, 255, 0.4);
    }
  }
}
</style>

<style lang="scss" scoped>
.cockpit-viewport {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  background: #021E4D;
  position: relative;
}

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
