<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="费用管理" name="fee" />
      <el-tab-pane label="工单管理" name="workOrder" />
      <el-tab-pane label="访客管理" name="visitor" />
      <el-tab-pane label="车辆管理" name="vehicle" />
      <el-tab-pane label="设施运维" name="facility" />
    </el-tabs>
    <FeeManage v-if="activeTab === 'fee'" />
    <WorkOrder v-if="activeTab === 'workOrder'" />
    <VisitorManage v-if="activeTab === 'visitor'" />
    <VehicleManage v-if="activeTab === 'vehicle'" />
    <FacilityManage v-if="activeTab === 'facility'" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import FeeManage from './FeeManage.vue'
import WorkOrder from './WorkOrder.vue'
import VisitorManage from './VisitorManage.vue'
import VehicleManage from './VehicleManage.vue'
import FacilityManage from './FacilityManage.vue'

const activeTab = ref('fee')
function handleTabChange() {}
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
