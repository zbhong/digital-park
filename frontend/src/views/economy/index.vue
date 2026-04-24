<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="企业画像" name="profile" />
      <el-tab-pane label="经济指标" name="indicator" />
      <el-tab-pane label="经济报告" name="report" />
    </el-tabs>
    <EnterpriseProfile v-if="activeTab === 'profile'" />
    <EconomyIndicator v-if="activeTab === 'indicator'" />
    <EconomyReport v-if="activeTab === 'report'" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import EnterpriseProfile from './EnterpriseProfile.vue'
import EconomyIndicator from './EconomyIndicator.vue'
import EconomyReport from './EconomyReport.vue'

const activeTab = ref('profile')
function handleTabChange() {}
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
