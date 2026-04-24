<template>
  <div class="cockpit-header">
    <div class="header-bg">
      <img src="../assets/header-title.png" alt="" class="header-bg-img" />
    </div>
    <div class="header-content">
      <div class="header-title-row">
        <div class="header-title">银河数字园区一体化平台</div>
      </div>
      <div class="tab-group" role="tablist">
        <div
          v-for="tab in allTabs"
          :key="tab.key"
          class="tab-item"
          :class="{ active: modelValue === tab.key }"
          role="tab"
          :aria-selected="modelValue === tab.key"
          tabindex="0"
          @click="$emit('update:modelValue', tab.key)"
          @keydown.enter="$emit('update:modelValue', tab.key)"
        >
          {{ tab.label }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  modelValue: { type: String, default: 'overview' }
})
defineEmits(['update:modelValue'])

const allTabs = [
  { key: 'overview', label: '园区总览' },
  { key: 'safety', label: '安全管理' },
  { key: 'energy', label: '能源管理' },
  { key: 'environment', label: '环境管理' },
  { key: 'asset', label: '资产管理' }
]
</script>

<style scoped lang="scss">
@import '../styles/cockpit.scss';

.cockpit-header {
  position: relative;
  overflow: visible;
  height: 70px;
  flex-shrink: 0;
  z-index: 100;
}

.header-bg {
  position: absolute;
  top: 12px;
  left: 0;
  width: 100%;
  height: 33px;
  z-index: 0;
  pointer-events: none;

  .header-bg-img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    object-position: center center;
  }
}

.header-content {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  height: 100%;
  padding-top: 4px;
}

.header-title-row {
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  width: 100%;
  height: 33px;
}

.tab-group {
  display: flex;
  gap: 50px;
  margin-top: 4px;
  z-index: 10;
}
</style>
