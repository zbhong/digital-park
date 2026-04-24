<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="应急物资" name="material" />
      <el-tab-pane label="应急队伍" name="team" />
      <el-tab-pane label="应急预案" name="plan" />
      <el-tab-pane label="应急演练" name="drill" />
      <el-tab-pane label="应急事件" name="event" />
    </el-tabs>
    <EmergencyMaterial v-if="activeTab === 'material'" />
    <EmergencyTeam v-if="activeTab === 'team'" />
    <EmergencyPlan v-if="activeTab === 'plan'" />
    <EmergencyDrill v-if="activeTab === 'drill'" />
    <EmergencyEvent v-if="activeTab === 'event'" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import EmergencyMaterial from './EmergencyMaterial.vue'
import EmergencyTeam from './EmergencyTeam.vue'
import EmergencyPlan from './EmergencyPlan.vue'
import EmergencyDrill from './EmergencyDrill.vue'
import EmergencyEvent from './EmergencyEvent.vue'

const activeTab = ref('material')
function handleTabChange() {}
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
