<template>
  <div>
    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="事件名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入事件名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable style="width: 140px">
          <el-option label="入侵检测" value="ids" />
          <el-option label="病毒告警" value="virus" />
          <el-option label="异常流量" value="traffic" />
          <el-option label="违规访问" value="access" />
        </el-select>
      </el-form-item>
      <el-form-item label="级别" prop="level">
        <el-select v-model="queryParams.level" placeholder="请选择级别" clearable style="width: 120px">
          <el-option label="高" value="high" />
          <el-option label="中" value="medium" />
          <el-option label="低" value="low" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="事件名称" prop="name" align="center" min-width="180" show-overflow-tooltip />
      <el-table-column label="类型" prop="type" align="center" min-width="100">
        <template #default="{ row }"><el-tag size="small">{{ typeNameMap[row.type] }}</el-tag></template>
      </el-table-column>
      <el-table-column label="级别" prop="level" align="center" min-width="80">
        <template #default="{ row }"><el-tag :type="levelTagMap[row.level]" size="small">{{ levelNameMap[row.level] }}</el-tag></template>
      </el-table-column>
      <el-table-column label="源IP" prop="sourceIp" align="center" min-width="130" />
      <el-table-column label="目标IP" prop="targetIp" align="center" min-width="130" />
      <el-table-column label="发生时间" prop="eventTime" align="center" min-width="160" />
      <el-table-column label="状态" prop="status" align="center" min-width="80">
        <template #default="{ row }"><el-tag :type="row.status === 'resolved' ? 'success' : 'danger'" size="small">{{ row.status === 'resolved' ? '已解决' : '未解决' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="100" fixed="right">
        <template #default="{ row }"><el-button link type="primary" @click="handleView(row)">详情</el-button></template>
      </el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />

    <el-dialog title="事件详情" v-model="detailVisible" width="600px" append-to-body>
      <el-descriptions :column="2" border v-if="currentRow">
        <el-descriptions-item label="事件名称">{{ currentRow.name }}</el-descriptions-item>
        <el-descriptions-item label="事件类型">{{ typeNameMap[currentRow.type] }}</el-descriptions-item>
        <el-descriptions-item label="事件级别"><el-tag :type="levelTagMap[currentRow.level]">{{ levelNameMap[currentRow.level] }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="状态"><el-tag :type="currentRow.status === 'resolved' ? 'success' : 'danger'">{{ currentRow.status === 'resolved' ? '已解决' : '未解决' }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="源IP">{{ currentRow.sourceIp }}</el-descriptions-item>
        <el-descriptions-item label="目标IP">{{ currentRow.targetIp }}</el-descriptions-item>
        <el-descriptions-item label="发生时间">{{ currentRow.eventTime }}</el-descriptions-item>
        <el-descriptions-item label="处理时间">{{ currentRow.resolveTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="事件描述" :span="2">{{ currentRow.description }}</el-descriptions-item>
      </el-descriptions>
      <template #footer><el-button @click="detailVisible = false">关 闭</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search, Refresh } from '@element-plus/icons-vue'
import { getSafetyEventStatistics } from '@/api/safety'

const typeNameMap = { ids: '入侵检测', virus: '病毒告警', traffic: '异常流量', access: '违规访问' }
const levelTagMap = { high: 'danger', medium: 'warning', low: 'info' }
const levelNameMap = { high: '高', medium: '中', low: '低' }

const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', type: '', level: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const queryRef = ref(null)
const detailVisible = ref(false)
const currentRow = ref(null)

function getList() {
  loading.value = true
  getSafetyEventStatistics(queryParams).then(res => { dataList.value = res.rows || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }
function handleView(row) { currentRow.value = row; detailVisible.value = true }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
