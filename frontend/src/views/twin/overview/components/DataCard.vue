<template>
  <div class="glass-card" :style="{ animationDelay: delay + 's' }">
    <div class="glass-card__header" v-if="title">
      <span class="glass-card__bar"></span>
      <span class="glass-card__title">{{ title }}</span>
      <slot name="header-extra"></slot>
    </div>
    <div class="glass-card__body">
      <slot></slot>
    </div>
  </div>
</template>

<script setup>
defineProps({
  title: { type: String, default: '' },
  delay: { type: Number, default: 0 }
})
</script>

<style scoped lang="scss">
@import '../styles/cockpit.scss';

.glass-card {
  min-width: 0;
  flex-shrink: 0;

  &__header {
    position: relative;
    padding: 0 14px;
    font-size: 13px;
    color: var(--accent);
    font-weight: 600;
    display: flex;
    align-items: center;
    gap: 8px;
    user-select: none;
    overflow: hidden;
    height: 31px;
    line-height: 31px;
  }

  &__title {
    position: relative;
    z-index: 1;
    flex: 1;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    padding-left: 0px;
    margin-left: 0px;
  }

  // header-extra slot 也需要相对定位在图片上方
  &__header :deep(*) {
    position: relative;
    z-index: 1;
  }
}
</style>
