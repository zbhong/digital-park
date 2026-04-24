<template>
  <div class="app-container">
    <el-form :model="queryParams" :inline="true" class="search-form">
      <el-form-item label="操作模块" prop="title"><el-input v-model="queryParams.title" placeholder="请输入操作模块" clearable style="width: 200px" @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item label="操作人" prop="operName"><el-input v-model="queryParams.operName" placeholder="请输入操作人" clearable style="width: 200px" @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item label="状态" prop="status"><el-select v-model="queryParams.status" placeholder="请选择" clearable style="width: 120px"><el-option label="成功" value="0" /><el-option label="失败" value="1" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button><el-button :icon="Refresh" @click="resetQuery">重置</el-button></el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="danger" plain :icon="Delete" @click="handleClean">清空</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="日志ID" prop="operId" align="center" min-width="80" />
      <el-table-column label="操作模块" prop="title" align="center" min-width="150" />
      <el-table-column label="操作类型" prop="businessType" align="center" min-width="100"><template #default="{ row }"><el-tag size="small">{{ row.businessType }}</el-tag></template></el-table-column>
      <el-table-column label="操作人" prop="operName" align="center" min-width="100" />
      <el-table-column label="操作IP" prop="operIp" align="center" min-width="130" />
      <el-table-column label="操作时间" prop="operTime" align="center" min-width="160" />
      <el-table-column label="状态" prop="status" align="center" min-width="80"><template #default="{ row }"><el-tag :type="row.status === '0' ? 'success' : 'danger'" size="small">{{ row.status === '0' ? '成功' : '失败' }}</el-tag></template></el-table-column>
      <el-table-column label="操作" align="center" min-width="100" fixed="right"><template #default="{ row }"><el-button link type="primary" @click="handleView(row)">详情</el-button></template></el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />

    <el-dialog title="操作日志详情" v-model="detailVisible" width="600px" append-to-body>
      <el-descriptions :column="2" border v-if="currentRow">
        <el-descriptions-item label="操作模块">{{ currentRow.title }}</el-descriptions-item>
        <el-descriptions-item label="操作类型">{{ currentRow.businessType }}</el-descriptions-item>
        <el-descriptions-item label="操作人">{{ currentRow.operName }}</el-descriptions-item>
        <el-descriptions-item label="操作IP">{{ currentRow.operIp }}</el-descriptions-item>
        <el-descriptions-item label="操作时间">{{ currentRow.operTime }}</el-descriptions-item>
        <el-descriptions-item label="状态"><el-tag :type="currentRow.status === '0' ? 'success' : 'danger'">{{ currentRow.status === '0' ? '成功' : '失败' }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="请求方法" :span="2">{{ currentRow.requestMethod }}</el-descriptions-item>
        <el-descriptions-item label="请求URL" :span="2">{{ currentRow.operUrl }}</el-descriptions-item>
        <el-descriptions-item label="操作参数" :span="2">{{ currentRow.operParam }}</el-descriptions-item>
      </el-descriptions>
      <template #footer><el-button @click="detailVisible = false">关 闭</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Delete } from '@element-plus/icons-vue'
import { listOperLog, cleanOperLog, getOperLog } from '@/api/system'

const queryParams = reactive({ pageNum: 1, pageSize: 10, title: '', operName: '', status: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const detailVisible = ref(false)
const currentRow = ref(null)

function getList() {
  loading.value = true
  listOperLog(queryParams).then(res => { dataList.value = res.rows || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { queryParams.title = ''; queryParams.operName = ''; queryParams.status = ''; handleQuery() }
function handleView(row) { getOperLog(row.operId).then(res => { currentRow.value = res.data || res; detailVisible.value = true }).catch(() => { currentRow.value = row; detailVisible.value = true }) }
function handleClean() { ElMessageBox.confirm('是否确认清空所有操作日志？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { cleanOperLog().then(() => { ElMessage.success('清空成功'); getList() }).catch(() => { ElMessage.error('清空失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
