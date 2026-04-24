<template>
  <div>
    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stat-cards">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-value" style="color: #F56C6C">{{ stats.total }}</div>
          <div class="stat-label">事件总数</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-value" style="color: #E6A23C">{{ stats.pending }}</div>
          <div class="stat-label">待处理</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-value" style="color: #409EFF">{{ stats.processing }}</div>
          <div class="stat-label">处理中</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-value" style="color: #67C23A">{{ stats.done }}</div>
          <div class="stat-label">已处理</div>
        </el-card>
      </el-col>
    </el-row>

    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="事件名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入事件名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable style="width: 140px">
          <el-option label="入侵报警" value="intrusion" />
          <el-option label="异常行为" value="abnormal" />
          <el-option label="设备故障" value="fault" />
          <el-option label="消防报警" value="fire" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option label="待处理" value="pending" />
          <el-option label="处理中" value="processing" />
          <el-option label="已处理" value="done" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="事件名称" prop="name" align="center" min-width="150" show-overflow-tooltip />
      <el-table-column label="类型" prop="type" align="center" min-width="100">
        <template #default="{ row }"><el-tag size="small">{{ typeNameMap[row.type] }}</el-tag></template>
      </el-table-column>
      <el-table-column label="等级" prop="level" align="center" min-width="80">
        <template #default="{ row }"><el-tag :type="levelTagMap[row.level]" size="small">{{ levelNameMap[row.level] }}</el-tag></template>
      </el-table-column>
      <el-table-column label="位置" prop="location" align="center" min-width="120" show-overflow-tooltip />
      <el-table-column label="发生时间" prop="eventTime" align="center" min-width="160" />
      <el-table-column label="状态" prop="status" align="center" min-width="80">
        <template #default="{ row }"><el-tag :type="statusTagMap[row.status]" size="small">{{ statusNameMap[row.status] }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="150" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleView(row)">查看</el-button>
          <el-button link type="primary" v-if="row.status !== 'done'" @click="handleProcess(row)">处理</el-button>
        </template>
      </el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />

    <!-- 事件详情弹窗 -->
    <el-dialog title="事件详情" v-model="detailVisible" width="600px" append-to-body>
      <el-descriptions :column="2" border v-if="currentRow">
        <el-descriptions-item label="事件名称">{{ currentRow.name }}</el-descriptions-item>
        <el-descriptions-item label="事件类型">{{ typeNameMap[currentRow.type] }}</el-descriptions-item>
        <el-descriptions-item label="事件等级"><el-tag :type="levelTagMap[currentRow.level]">{{ levelNameMap[currentRow.level] }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="处理状态"><el-tag :type="statusTagMap[currentRow.status]">{{ statusNameMap[currentRow.status] }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="发生位置">{{ currentRow.location }}</el-descriptions-item>
        <el-descriptions-item label="发生时间">{{ currentRow.eventTime }}</el-descriptions-item>
        <el-descriptions-item label="事件描述" :span="2">{{ currentRow.description }}</el-descriptions-item>
      </el-descriptions>
      <template #footer><el-button @click="detailVisible = false">关 闭</el-button></template>
    </el-dialog>

    <!-- 处理弹窗 -->
    <el-dialog title="处理事件" v-model="processVisible" width="500px" append-to-body destroy-on-close>
      <el-form ref="processFormRef" :model="processForm" :rules="processRules" label-width="100px">
        <el-form-item label="处理结果" prop="result">
          <el-input v-model="processForm.result" type="textarea" :rows="3" placeholder="请输入处理结果" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="processVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitProcess">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { getSafetyEventPage, handleSafetyEvent } from '@/api/safety'

const typeNameMap = { intrusion: '入侵报警', abnormal: '异常行为', fault: '设备故障', fire: '消防报警' }
const levelTagMap = { urgent: 'danger', important: 'warning', normal: 'info' }
const levelNameMap = { urgent: '紧急', important: '重要', normal: '一般' }
const statusTagMap = { pending: 'danger', processing: 'warning', done: 'success' }
const statusNameMap = { pending: '待处理', processing: '处理中', done: '已处理' }

const stats = reactive({ total: 0, pending: 0, processing: 0, done: 0 })
const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', type: '', status: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const queryRef = ref(null)
const detailVisible = ref(false)
const currentRow = ref(null)
const processVisible = ref(false)
const processFormRef = ref(null)
const processForm = ref({ result: '' })
const processRules = { result: [{ required: true, message: '请输入处理结果', trigger: 'blur' }] }

function getList() {
  loading.value = true
  getSafetyEventPage(queryParams).then(res => {
    dataList.value = res.rows || res.data || []
    total.value = res.total || 0
  }).catch(() => {
    ElMessage.error('获取数据失败')
    dataList.value = []
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }
function handleView(row) { currentRow.value = row; detailVisible.value = true }
function handleProcess(row) { currentRow.value = row; processForm.value = { result: '' }; processVisible.value = true }
function submitProcess() {
  processFormRef.value.validate(valid => {
    if (valid) {
      handleSafetyEvent(currentRow.value.id, { status: 'processed', handleResult: processForm.value.result }).then(() => {
        ElMessage.success('处理成功')
        processVisible.value = false
        getList()
      })
    }
  })
}

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
