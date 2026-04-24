<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="房源管理" name="asset" />
      <el-tab-pane label="客户管理" name="customer" />
      <el-tab-pane label="商机管理" name="opportunity" />
      <el-tab-pane label="合同管理" name="contract" />
      <el-tab-pane label="招商政策" name="policy" />
    </el-tabs>
    <AssetManage v-if="activeTab === 'asset'" />
    <CustomerManage v-if="activeTab === 'customer'" />
    <OpportunityManage v-if="activeTab === 'opportunity'" />
    <ContractManage v-if="activeTab === 'contract'" />
    <PolicyManage v-if="activeTab === 'policy'" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import AssetManage from './AssetManage.vue'
import CustomerManage from './CustomerManage.vue'
import OpportunityManage from './OpportunityManage.vue'
import ContractManage from './ContractManage.vue'
import PolicyManage from './PolicyManage.vue'

const activeTab = ref('asset')
function handleTabChange() {}
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
