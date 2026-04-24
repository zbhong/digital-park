<template>
  <div>
    <el-form :model="queryParams" :inline="true" class="search-form">
      <el-form-item label="报告名称" prop="name"><el-input v-model="queryParams.name" placeholder="请输入报告名称" clearable style="width: 200px" @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item label="类型" prop="type"><el-select v-model="queryParams.type" placeholder="请选择" clearable style="width: 140px"><el-option label="月报" value="monthly" /><el-option label="季报" value="quarterly" /><el-option label="年报" value="annual" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button><el-button :icon="Refresh" @click="resetQuery">重置</el-button></el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="报告名称" prop="name" align="center" min-width="250" show-overflow-tooltip />
      <el-table-column label="类型" prop="type" align="center" min-width="80"><template #default="{ row }"><el-tag size="small">{{ typeNameMap[row.type] }}</el-tag></template></el-table-column>
      <el-table-column label="报告期" prop="period" align="center" min-width="120" />
      <el-table-column label="编制人" prop="author" align="center" min-width="100" />
      <el-table-column label="发布时间" prop="publishTime" align="center" min-width="160" />
      <el-table-column label="操作" align="center" min-width="150" fixed="right">
        <template #default="{ row }"><el-button link type="primary" @click="handleView(row)">查看</el-button><el-button link type="primary" @click="handleDownload(row)">下载</el-button></template>
      </el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { getEconomyReportPage, getEconomyReport } from '@/api/economy'

const typeNameMap = { monthly: '月报', quarterly: '季报', annual: '年报' }
const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', type: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)

function getList() {
  loading.value = true
  getEconomyReportPage(queryParams).then(res => { dataList.value = res.rows || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { queryParams.name = ''; queryParams.type = ''; handleQuery() }
function handleView(row) { getEconomyReport(row.id).then(res => { ElMessage.info('查看报告: ' + row.name) }).catch(() => { ElMessage.error('获取报告失败'); }) }
function handleDownload(row) { ElMessage.info('下载报告: ' + row.name) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
