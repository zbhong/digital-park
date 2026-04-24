<template>
  <div class="layer-control" :class="{ collapsed: !expanded }">
    <div class="layer-title" @click="expanded = !expanded">
      <svg viewBox="0 0 24 24" width="14" height="14" fill="currentColor">
        <path d="M11.99 18.54l-7.37-5.73L3 14.07l9 7 9-7-1.63-1.27-7.38 5.74zM12 16l7.36-5.73L21 9l-9-7-9 7 1.63 1.27L12 16z"/>
      </svg>
      <span>图层控制</span>
      <svg class="collapse-icon" :class="{ rotated: expanded }" viewBox="0 0 24 24" width="12" height="12" fill="currentColor">
        <path d="M7.41 8.59L12 13.17l4.59-4.58L18 10l-6 6-6-6 1.41-1.41z"/>
      </svg>
    </div>
    <transition name="fade">
      <div v-show="expanded" class="layer-list">
        <div v-for="layer in layers" :key="layer.key" class="layer-item">
          <div class="layer-info">
            <el-switch
              v-model="layer.visible"
              size="small"
              :active-color="layer.color"
              @change="handleLayerChange"
            />
            <span class="layer-dot" :style="{ background: layer.color }"></span>
            <span class="layer-name">{{ layer.name }}</span>
          </div>
          <el-slider
            v-model="layer.opacity"
            :min="0"
            :max="100"
            :step="10"
            class="layer-opacity"
            @change="handleLayerChange"
          />
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'

const emit = defineEmits(['change'])

const expanded = ref(true)

const layers = reactive([
  { key: 'building', name: '建筑', color: '#4080FF', visible: true, opacity: 100 },
  { key: 'device', name: '设备', color: '#00E396', visible: true, opacity: 100 },
  { key: 'security', name: '安防', color: '#FF4560', visible: true, opacity: 100 },
  { key: 'environment', name: '环境', color: '#00D4FF', visible: true, opacity: 100 },
  { key: 'pipeline', name: '管网', color: '#FEB019', visible: true, opacity: 100 },
  { key: 'area', name: '区域', color: '#775DD0', visible: true, opacity: 100 }
])

function handleLayerChange() {
  const result = {}
  layers.forEach(l => {
    result[l.key] = {
      visible: l.visible,
      opacity: l.opacity / 100
    }
  })
  emit('change', result)
}

/** 外部调用：获取图层状态 */
function getLayerState() {
  const result = {}
  layers.forEach(l => {
    result[l.key] = { visible: l.visible, opacity: l.opacity / 100 }
  })
  return result
}

/** 外部调用：设置图层状态 */
function setLayerState(state) {
  Object.keys(state).forEach(key => {
    const layer = layers.find(l => l.key === key)
    if (layer) {
      layer.visible = state[key].visible ?? layer.visible
      layer.opacity = (state[key].opacity ?? 1) * 100
    }
  })
}

defineExpose({ getLayerState, setLayerState })
</script>

<style scoped lang="scss">
.layer-control {
  position: absolute;
  bottom: 24px;
  left: 12px;
  z-index: 20;
  width: 220px;
  border-radius: 12px;
  background: rgba(16, 32, 64, 0.85);
  backdrop-filter: blur(8px);
  border: 1px solid rgba(64, 128, 255, 0.2);
  padding: 12px;

  .layer-title {
    font-size: 12px;
    font-weight: 600;
    color: #E0E8FF;
    margin-bottom: 10px;
    display: flex;
    align-items: center;
    gap: 6px;
    cursor: pointer;
    user-select: none;

    .collapse-icon {
      margin-left: auto;
      transition: transform 0.3s;

      &.rotated {
        transform: rotate(180deg);
      }
    }
  }

  .layer-list {
    .layer-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 6px 0;
      border-bottom: 1px solid rgba(64, 128, 255, 0.08);

      &:last-child {
        border-bottom: none;
      }

      .layer-info {
        display: flex;
        align-items: center;
        gap: 6px;

        .layer-dot {
          width: 8px;
          height: 8px;
          border-radius: 2px;
        }

        .layer-name {
          font-size: 12px;
          color: #B0BEC5;
        }
      }

      .layer-opacity {
        width: 60px;

        :deep(.el-slider__runway) {
          background: rgba(64, 128, 255, 0.15);
          height: 3px;
        }

        :deep(.el-slider__bar) {
          height: 3px;
        }

        :deep(.el-slider__button) {
          width: 10px;
          height: 10px;
          border-color: #4080FF;
        }
      }
    }
  }
}
</style>
