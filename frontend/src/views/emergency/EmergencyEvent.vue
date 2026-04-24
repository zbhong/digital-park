<template>
  <div>
    <el-row :gutter="16" class="stat-cards">
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #F56C6C">{{ stats.total }}</div><div class="stat-label">事件总数</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #E6A23C">{{ stats.processing }}</div><div class="stat-label">处理中</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #67C23A">{{ stats.resolved }}</div><div class="stat-label">已解决</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #409EFF">{{ stats.drillCount }}</div><div class="stat-label">演练次数</div></el-card></el-col>
    </el-row>

    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="事件名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入事件名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="等级" prop="level">
        <el-select v-model="queryParams.level" placeholder="请选择等级" clearable style="width: 120px">
          <el-option label="I级" value="I级" /><el-option label="II级" value="II级" /><el-option label="III级" value="III级" /><el-option label="IV级" value="IV级" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option label="待处理" value="pending" /><el-option label="处理中" value="processing" /><el-option label="已解决" value="resolved" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="事件名称" prop="name" align="center" min-width="200" show-overflow-tooltip />
      <el-table-column label="等级" prop="level" align="center" min-width="80">
        <template #default="{ row }"><el-tag :type="levelTagMap[row.level]" size="small">{{ row.level }}</el-tag></template>
      </el-table-column>
      <el-table-column label="发生时间" prop="eventTime" align="center" min-width="160" />
      <el-table-column label="发生地点" prop="location" align="center" min-width="120" />
      <el-table-column label="影响范围" prop="scope" align="center" min-width="100" />
      <el-table-column label="状态" prop="status" align="center" min-width="80">
        <template #default="{ row }"><el-tag :type="statusTagMap[row.status]" size="small">{{ statusNameMap[row.status] }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="150" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleView(row)">详情</el-button>
          <el-button link type="primary" v-if="row.status !== 'resolved'" @click="handleProcess(row)">处理</el-button>
        </template>
      </el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />

    <el-dialog title="事件详情" v-model="detailVisible" width="600px" append-to-body>
      <el-descriptions :column="2" border v-if="currentRow">
        <el-descriptions-item label="事件名称">{{ currentRow.name }}</el-descriptions-item>
        <el-descriptions-item label="事件等级"><el-tag :type="levelTagMap[currentRow.level]">{{ currentRow.level }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="发生时间">{{ currentRow.eventTime }}</el-descriptions-item>
        <el-descriptions-item label="发生地点">{{ currentRow.location }}</el-descriptions-item>
        <el-descriptions-item label="影响范围">{{ currentRow.scope }}</el-descriptions-item>
        <el-descriptions-item label="状态"><el-tag :type="statusTagMap[currentRow.status]">{{ statusNameMap[currentRow.status] }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="事件描述" :span="2">{{ currentRow.description }}</el-descriptions-item>
        <el-descriptions-item label="处理措施" :span="2">{{ currentRow.measure || '-' }}</el-descriptions-item>
      </el-descriptions>
      <template #footer><el-button @click="detailVisible = false">关 闭</el-button></template>
    </el-dialog>

    <el-dialog title="处理事件" v-model="processVisible" width="500px" append-to-body destroy-on-close>
      <el-form ref="processFormRef" :model="processForm" :rules="processRules" label-width="100px">
        <el-form-item label="处理措施" prop="measure"><el-input v-model="processForm.measure" type="textarea" :rows="4" placeholder="请输入处理措施" /></el-form-item>
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
import { getEmergencyEventPage, getEmergencyEvent, updateEmergencyEvent } from '@/api/emergency'

const levelTagMap = { 'I级': 'danger', 'II级': 'warning', 'III级': '', 'IV级': 'info' }
const statusTagMap = { pending: 'danger', processing: 'warning', resolved: 'success' }
const statusNameMap = { pending: '待处理', processing: '处理中', resolved: '已解决' }

const stats = reactive({ total: 0, processing: 0, resolved: 0, drillCount: 0 })
const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', level: '', status: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const queryRef = ref(null)
const detailVisible = ref(false)
const currentRow = ref(null)
const processVisible = ref(false)
const processFormRef = ref(null)
const processForm = ref({ measure: '' })
const processRules = { measure: [{ required: true, message: '请输入处理措施', trigger: 'blur' }] }

function getList() {
  loading.value = true
  getEmergencyEventPage(queryParams).then(res => { dataList.value = res.rows || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
    stats.total = 3; stats.processing = 1; stats.resolved = 2; stats.drillCount = 5
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }
function handleView(row) { currentRow.value = row; detailVisible.value = true }
function handleProcess(row) { currentRow.value = row; processForm.value = { measure: '' }; processVisible.value = true }
function submitProcess() { processFormRef.value.validate(valid => { if (valid) { updateEmergencyEvent({ id: currentRow.value.id, status: 'resolved', measure: processForm.value.measure }).then(() => { ElMessage.success('处理成功'); processVisible.value = false; getList() }).catch(() => { ElMessage.error('处理失败') }) } }) }

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
