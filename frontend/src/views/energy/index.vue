<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="能源计量" name="meter" />
      <el-tab-pane label="能源数据" name="data" />
      <el-tab-pane label="源侧设备" name="source" />
      <el-tab-pane label="储能管理" name="storage" />
      <el-tab-pane label="充电桩管理" name="charging" />
    </el-tabs>
    <EnergyMeter v-if="activeTab === 'meter'" />
    <EnergyData v-if="activeTab === 'data'" />
    <EnergySource v-if="activeTab === 'source'" />
    <EnergyStorage v-if="activeTab === 'storage'" />
    <EnergyCharging v-if="activeTab === 'charging'" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import EnergyMeter from './EnergyMeter.vue'
import EnergyData from './EnergyData.vue'
import EnergySource from './EnergySource.vue'
import EnergyStorage from './EnergyStorage.vue'
import EnergyCharging from './EnergyCharging.vue'

const activeTab = ref('meter')
function handleTabChange() {}
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
