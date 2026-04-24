<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header><span>商机漏斗</span></template>
          <div ref="funnelChartRef" style="height: 400px;"></div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header><span>商机阶段看板</span></template>
          <el-table :data="dataList" border stripe>
            <el-table-column label="商机名称" prop="name" align="center" min-width="180" show-overflow-tooltip />
            <el-table-column label="客户" prop="customer" align="center" min-width="120" />
            <el-table-column label="预计面积(m2)" prop="area" align="center" min-width="100" />
            <el-table-column label="预计金额(万元)" prop="amount" align="center" min-width="120" />
            <el-table-column label="阶段" prop="stage" align="center" min-width="100"><template #default="{ row }"><el-tag :type="stageTagMap[row.stage]" size="small">{{ stageNameMap[row.stage] }}</el-tag></template></el-table-column>
            <el-table-column label="负责人" prop="owner" align="center" min-width="100" />
            <el-table-column label="操作" align="center" min-width="100" fixed="right"><template #default="{ row }"><el-button link type="primary" @click="handleDetail(row)">详情</el-button></template></el-table-column>
            <template #empty><el-empty description="暂无数据" /></template>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

const stageTagMap = { initial: 'info', visit: '', negotiation: 'warning', contract: 'primary', signed: 'success', lost: 'danger' }
const stageNameMap = { initial: '初步接触', visit: '实地考察', negotiation: '商务谈判', contract: '合同拟定', signed: '已签约', lost: '已流失' }

const dataList = ref([
  { id: 1, name: '创新科技入驻项目', customer: '创新科技', area: 300, amount: 25.2, stage: 'negotiation', owner: '张经理' },
  { id: 2, name: '数据服务扩租项目', customer: '数据服务公司', area: 500, amount: 42.0, stage: 'visit', owner: '李经理' },
  { id: 3, name: '智能制造整层租赁', customer: '智能制造企业', area: 1000, amount: 66.0, stage: 'initial', owner: '王经理' },
  { id: 4, name: '生物科技入驻项目', customer: '生物科技公司', area: 200, amount: 21.6, stage: 'contract', owner: '赵经理' },
  { id: 5, name: '新能源企业扩租', customer: '新能源公司', area: 400, amount: 26.4, stage: 'signed', owner: '张经理' }
])

const funnelChartRef = ref(null)
let funnelChart = null

function initChart() {
  if (funnelChartRef.value) {
    funnelChart = echarts.init(funnelChartRef.value)
    funnelChart.setOption({
      tooltip: { trigger: 'item' },
      series: [{
        type: 'funnel', left: '10%', width: '80%', top: 20, bottom: 20,
        data: [
          { value: 15, name: '初步接触', itemStyle: { color: '#909399' } },
          { value: 10, name: '实地考察', itemStyle: { color: '#409EFF' } },
          { value: 6, name: '商务谈判', itemStyle: { color: '#E6A23C' } },
          { value: 4, name: '合同拟定', itemStyle: { color: '#67C23A' } },
          { value: 3, name: '已签约', itemStyle: { color: '#F56C6C' } }
        ],
        label: { color: '#ccc' }
      }]
    })
  }
}

function handleDetail(row) { ElMessage.info('查看商机详情: ' + row.name) }
function handleResize() { funnelChart && funnelChart.resize() }

onMounted(() => { nextTick(() => { initChart() }); window.addEventListener('resize', handleResize) })
onBeforeUnmount(() => { window.removeEventListener('resize', handleResize); funnelChart && funnelChart.dispose() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
