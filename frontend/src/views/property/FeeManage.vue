<template>
  <div>
    <el-row :gutter="16" class="stat-cards">
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #409EFF">{{ stats.total }}</div><div class="stat-label">应收总额(万元)</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #67C23A">{{ stats.received }}</div><div class="stat-label">已收金额(万元)</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #E6A23C">{{ stats.pending }}</div><div class="stat-label">待收金额(万元)</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #F56C6C">{{ stats.overdue }}</div><div class="stat-label">逾期金额(万元)</div></el-card></el-col>
    </el-row>

    <el-form :model="queryParams" :inline="true" class="search-form">
      <el-form-item label="企业名称" prop="enterpriseName"><el-input v-model="queryParams.enterpriseName" placeholder="请输入企业名称" clearable style="width: 200px" @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item label="费用类型" prop="feeType"><el-select v-model="queryParams.feeType" placeholder="请选择" clearable style="width: 120px"><el-option label="租金" value="rent" /><el-option label="物业费" value="property" /><el-option label="水电费" value="utility" /></el-select></el-form-item>
      <el-form-item label="缴费状态" prop="payStatus"><el-select v-model="queryParams.payStatus" placeholder="请选择" clearable style="width: 120px"><el-option label="已缴" value="paid" /><el-option label="未缴" value="unpaid" /><el-option label="逾期" value="overdue" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button><el-button :icon="Refresh" @click="resetQuery">重置</el-button></el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="企业名称" prop="enterpriseName" align="center" min-width="150" />
      <el-table-column label="费用类型" prop="feeType" align="center" min-width="100"><template #default="{ row }"><el-tag size="small">{{ typeNameMap[row.feeType] }}</el-tag></template></el-table-column>
      <el-table-column label="账单周期" prop="period" align="center" min-width="120" />
      <el-table-column label="应收金额" prop="amount" align="center" min-width="100" />
      <el-table-column label="缴费状态" prop="payStatus" align="center" min-width="80"><template #default="{ row }"><el-tag :type="statusTagMap[row.payStatus]" size="small">{{ statusNameMap[row.payStatus] }}</el-tag></template></el-table-column>
      <el-table-column label="操作" align="center" min-width="100" fixed="right"><template #default="{ row }"><el-button link type="primary" v-if="row.payStatus !== 'paid'" @click="handlePay(row)">缴费</el-button></template></el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { getFeePage, payFee } from '@/api/property'

const typeNameMap = { rent: '租金', property: '物业费', utility: '水电费' }
const statusTagMap = { paid: 'success', unpaid: 'warning', overdue: 'danger' }
const statusNameMap = { paid: '已缴', unpaid: '未缴', overdue: '逾期' }
const stats = computed(() => ({
  total: dataList.value.reduce((sum, item) => sum + (Number(item.amount) || 0), 0),
  received: dataList.value.filter(item => item.payStatus === 'paid').reduce((sum, item) => sum + (Number(item.amount) || 0), 0),
  pending: dataList.value.filter(item => item.payStatus === 'unpaid').reduce((sum, item) => sum + (Number(item.amount) || 0), 0),
  overdue: dataList.value.filter(item => item.payStatus === 'overdue').reduce((sum, item) => sum + (Number(item.amount) || 0), 0)
}))

const queryParams = reactive({ pageNum: 1, pageSize: 10, enterpriseName: '', feeType: '', payStatus: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)

function getList() {
  loading.value = true
  getFeePage(queryParams).then(res => { dataList.value = res.rows || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { queryParams.enterpriseName = ''; queryParams.feeType = ''; queryParams.payStatus = ''; handleQuery() }
function handlePay(row) { ElMessageBox.confirm('确认标记为已缴费？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { payFee(row.id).then(() => { ElMessage.success('缴费成功'); getList() }).catch(() => { ElMessage.error('缴费失败'); }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
.stat-cards { margin-bottom: 16px; }
.stat-card { text-align: center; padding: 10px 0; }
.stat-value { font-size: 28px; font-weight: bold; }
.stat-label { font-size: 14px; color: #909399; margin-top: 4px; }
</style>
