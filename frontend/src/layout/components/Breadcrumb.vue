<template>
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
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

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
</script>

<style lang="scss" scoped>
.breadcrumb {
  font-size: 13px;

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
</style>
