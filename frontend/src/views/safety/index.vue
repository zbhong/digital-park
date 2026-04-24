<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="安防监控" name="monitor" />
      <el-tab-pane label="安全事件" name="event" />
      <el-tab-pane label="巡检管理" name="patrol" />
      <el-tab-pane label="消防设备" name="fire" />
      <el-tab-pane label="网络安全" name="network" />
      <el-tab-pane label="生产安全" name="production" />
    </el-tabs>
    <MonitorPoint v-if="activeTab === 'monitor'" />
    <SafetyEvent v-if="activeTab === 'event'" />
    <PatrolManage v-if="activeTab === 'patrol'" />
    <FireEquipment v-if="activeTab === 'fire'" />
    <NetworkSecurity v-if="activeTab === 'network'" />
    <ProductionSafety v-if="activeTab === 'production'" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import MonitorPoint from './MonitorPoint.vue'
import SafetyEvent from './SafetyEvent.vue'
import PatrolManage from './PatrolManage.vue'
import FireEquipment from './FireEquipment.vue'
import NetworkSecurity from './NetworkSecurity.vue'
import ProductionSafety from './ProductionSafety.vue'

const activeTab = ref('monitor')
function handleTabChange() {}
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
