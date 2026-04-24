<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="资产分类" name="category" />
      <el-tab-pane label="资产台账" name="info" />
      <el-tab-pane label="资产文档" name="document" />
      <el-tab-pane label="维保管理" name="maintenance" />
      <el-tab-pane label="资产盘点" name="inventory" />
    </el-tabs>
    <AssetCategory v-if="activeTab === 'category'" />
    <AssetInfo v-if="activeTab === 'info'" />
    <AssetDocument v-if="activeTab === 'document'" />
    <AssetMaintenance v-if="activeTab === 'maintenance'" />
    <AssetInventory v-if="activeTab === 'inventory'" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import AssetCategory from './AssetCategory.vue'
import AssetInfo from './AssetInfo.vue'
import AssetDocument from './AssetDocument.vue'
import AssetMaintenance from './AssetMaintenance.vue'
import AssetInventory from './AssetInventory.vue'

const activeTab = ref('category')
function handleTabChange() {}
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
