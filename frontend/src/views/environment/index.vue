<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="监测点位" name="point" />
      <el-tab-pane label="环境数据" name="data" />
      <el-tab-pane label="污染溯源" name="pollution" />
    </el-tabs>
    <MonitorPoint v-if="activeTab === 'point'" />
    <MonitorData v-if="activeTab === 'data'" />
    <PollutionSource v-if="activeTab === 'pollution'" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import MonitorPoint from './MonitorPoint.vue'
import MonitorData from './MonitorData.vue'
import PollutionSource from './PollutionSource.vue'

const activeTab = ref('point')
function handleTabChange() {}
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
