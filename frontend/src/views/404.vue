<template>
  <div class="error-page">
    <el-result
      icon="warning"
      title="404"
      sub-title="抱歉，您访问的页面不存在"
    >
      <template #extra>
        <el-button type="primary" size="large" @click="goHome">
          <el-icon><HomeFilled /></el-icon>
          <span>返回首页</span>
        </el-button>
        <el-button size="large" @click="goBack">
          <el-icon><Back /></el-icon>
          <span>返回上一页</span>
        </el-button>
      </template>
    </el-result>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { HomeFilled, Back } from '@element-plus/icons-vue'

const router = useRouter()

/**
 * 返回首页
 */
function goHome() {
  router.push('/')
}

/**
 * 返回上一页
 */
function goBack() {
  router.go(-1)
}
</script>

<style lang="scss" scoped>
.error-page {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100vh;
  background-color: #f0f2f5;

  :deep(.el-result) {
    padding: 60px 40px;

    .el-result__icon svg {
      width: 120px;
      height: 120px;
    }

    .el-result__title p {
      font-size: 48px;
      font-weight: 700;
      color: #303133;
    }

    .el-result__subtitle p {
      font-size: 16px;
      color: #909399;
    }

    .el-result__extra {
      margin-top: 30px;

      .el-button {
        padding: 10px 28px;
        font-size: 15px;
        border-radius: 8px;
      }
    }
  }
}
</style>
