<template>
  <div class="tags-view-container">
    <el-scrollbar>
      <div class="tags-view-wrapper">
        <router-link
          v-for="tag in appStore.visitedViews"
          :key="tag.path"
          :to="tag.path"
          class="tags-view-item"
          :class="{ active: isActive(tag) }"
          @contextmenu.prevent="openContextMenu($event, tag)"
        >
          <span class="tag-dot" v-if="isActive(tag)"></span>
          {{ tag.title }}
          <el-icon
            v-if="!tag.meta?.affix"
            class="tag-close"
            @click.prevent.stop="closeTag(tag)"
          >
            <Close />
          </el-icon>
        </router-link>
      </div>
    </el-scrollbar>

    <!-- 右键菜单 -->
    <ul
      v-show="contextMenuVisible"
      class="context-menu"
      :style="{ left: contextMenuLeft + 'px', top: contextMenuTop + 'px' }"
    >
      <li @click="closeOtherTags">关闭其他</li>
      <li @click="closeAllTags">关闭所有</li>
    </ul>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAppStore } from '@/store/modules'

const route = useRoute()
const router = useRouter()
const appStore = useAppStore()

// 右键菜单状态
const contextMenuVisible = ref(false)
const contextMenuLeft = ref(0)
const contextMenuTop = ref(0)
const selectedTag = ref(null)

/**
 * 判断标签是否激活
 * @param {object} tag - 标签对象
 * @returns {boolean}
 */
function isActive(tag) {
  return tag.path === route.path
}

/**
 * 添加标签页
 */
function addTag() {
  if (route.meta?.title) {
    appStore.addView(route)
    appStore.addCachedView(route)
  }
}

/**
 * 关闭标签页
 * @param {object} tag - 标签对象
 */
function closeTag(tag) {
  appStore.delView(tag.path)
  appStore.delCachedView(tag.name)
  // 如果关闭的是当前激活的标签页，则跳转到最近的标签页
  if (isActive(tag)) {
    const latestView = appStore.visitedViews.slice(-1)[0]
    if (latestView) {
      router.push(latestView.path)
    } else {
      router.push('/')
    }
  }
}

/**
 * 关闭其他标签页
 */
function closeOtherTags() {
  if (selectedTag.value) {
    router.push(selectedTag.value.path)
    appStore.delOtherViews(selectedTag.value.path)
  }
  contextMenuVisible.value = false
}

/**
 * 关闭所有标签页
 */
function closeAllTags() {
  appStore.delAllViews()
  router.push('/')
  contextMenuVisible.value = false
}

/**
 * 打开右键菜单
 * @param {Event} e - 事件对象
 * @param {object} tag - 标签对象
 */
function openContextMenu(e, tag) {
  selectedTag.value = tag
  contextMenuLeft.value = e.clientX
  contextMenuTop.value = e.clientY
  contextMenuVisible.value = true
}

/**
 * 关闭右键菜单
 */
function closeContextMenu() {
  contextMenuVisible.value = false
}

// 监听路由变化，自动添加标签
watch(
  () => route.path,
  () => {
    addTag()
  },
  { immediate: true }
)

// 点击其他区域关闭右键菜单
onMounted(() => {
  document.addEventListener('click', closeContextMenu)
})
</script>

<style lang="scss" scoped>
.tags-view-container {
  height: 34px;
  width: 100%;
  background: #fff;
  border-bottom: 1px solid #d8dce5;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.04), 0 1px 2px -1px rgba(0, 0, 0, 0.04);
  position: relative;
}

.tags-view-wrapper {
  display: flex;
  align-items: center;
  padding: 2px 8px;
  height: 34px;
  white-space: nowrap;
  overflow: hidden;
}

.tags-view-item {
  display: inline-flex;
  align-items: center;
  height: 26px;
  padding: 0 8px;
  margin-right: 4px;
  font-size: 12px;
  color: #495060;
  background: #fff;
  border: 1px solid #d8dce5;
  border-radius: 3px;
  text-decoration: none;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    color: #409eff;
  }

  &.active {
    background-color: #409eff;
    color: #fff;
    border-color: #409eff;

    .tag-close {
      &:hover {
        background-color: rgba(255, 255, 255, 0.3);
        color: #fff;
      }
    }
  }

  .tag-dot {
    display: inline-block;
    width: 6px;
    height: 6px;
    border-radius: 50%;
    background-color: #fff;
    margin-right: 4px;
  }

  .tag-close {
    margin-left: 4px;
    font-size: 12px;
    border-radius: 50%;
    width: 16px;
    height: 16px;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.2s;

    &:hover {
      background-color: #b4bccc;
      color: #fff;
    }
  }
}

.context-menu {
  position: fixed;
  z-index: 3000;
  list-style: none;
  margin: 0;
  padding: 5px 0;
  background: #fff;
  border-radius: 4px;
  box-shadow: 2px 2px 12px rgba(0, 0, 0, 0.12);
  font-size: 12px;

  li {
    padding: 8px 16px;
    cursor: pointer;
    color: #606266;

    &:hover {
      background: #ecf5ff;
      color: #409eff;
    }
  }
}
</style>
