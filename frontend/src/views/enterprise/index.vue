<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="企业信息" name="info" />
      <el-tab-pane label="服务请求" name="request" />
      <el-tab-pane label="政策信息" name="policy" />
    </el-tabs>
    <EnterpriseInfo v-if="activeTab === 'info'" />
    <ServiceRequest v-if="activeTab === 'request'" />
    <PolicyInfo v-if="activeTab === 'policy'" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import EnterpriseInfo from './EnterpriseInfo.vue'
import ServiceRequest from './ServiceRequest.vue'
import PolicyInfo from './PolicyInfo.vue'

const activeTab = ref('info')
function handleTabChange() {}
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
