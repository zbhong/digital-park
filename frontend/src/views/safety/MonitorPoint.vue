<template>
  <div>
    <!-- 视频监控区域 -->
    <div class="video-section">
      <div class="video-section-header">
        <span class="video-section-title">实时视频监控</span>
        <span class="video-section-time">{{ currentTime }}</span>
      </div>
      <div class="video-grid">
        <div v-for="(camera, index) in cameraList" :key="index" class="video-panel" :class="{ 'video-offline': camera.status !== 1 }">
          <div class="video-panel-inner">
            <div class="video-placeholder">
              <svg v-if="camera.status === 1" class="video-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                <path d="M15.75 10.5l4.72-4.72a.75.75 0 011.28.53v11.38a.75.75 0 01-1.28.53l-4.72-4.72M4.5 18.75h9a2.25 2.25 0 002.25-2.25v-9a2.25 2.25 0 00-2.25-2.25h-9A2.25 2.25 0 002.25 7.5v9a2.25 2.25 0 002.25 2.25z" />
              </svg>
              <svg v-else class="video-icon offline-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                <path d="M3.53 2.47a.75.75 0 00-1.06 1.06l18 18a.75.75 0 101.06-1.06l-18-18zM22.676 12.553a11.249 11.249 0 01-2.631 4.31l-3.099-3.099a5.25 5.25 0 00-6.71-6.71L7.759 4.577a11.217 11.217 0 014.242-.827c4.97 0 9.185 3.223 10.675 7.69.12.362.12.752 0 1.113z" />
                <path d="M15.75 12c0 .18-.013.357-.037.53l-4.244-4.243A3.75 3.75 0 0115.75 12zM12.53 15.713l-4.243-4.244a3.75 3.75 0 004.244 4.243z" />
                <path d="M6.75 12c0-.619.107-1.213.304-1.764l-3.1-3.1a11.25 11.25 0 00-2.63 4.31c-.12.362-.12.752 0 1.114 1.489 4.467 5.704 7.69 10.675 7.69 1.5 0 2.933-.294 4.242-.827l-2.477-2.477A5.25 5.25 0 016.75 12z" />
              </svg>
              <div class="video-status-text">{{ camera.status === 1 ? '信号正常' : '信号中断' }}</div>
            </div>
          </div>
          <div class="video-overlay">
            <div class="video-camera-name">{{ camera.name }}</div>
            <div class="video-camera-info">
              <span class="video-rec" v-if="camera.status === 1">REC</span>
              <span class="video-camera-location">{{ camera.location }}</span>
            </div>
          </div>
          <div class="video-timestamp">{{ currentTime }}</div>
          <div class="video-status-dot" :class="camera.status === 1 ? 'dot-online' : 'dot-offline'"></div>
        </div>
      </div>
    </div>

    <el-divider />

    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="点位名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入点位名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="点位类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable style="width: 140px">
          <el-option label="摄像头" value="摄像头" />
          <el-option label="烟感" value="烟感" />
          <el-option label="温感" value="温感" />
          <el-option label="门禁" value="门禁" />
          <el-option label="火焰探测" value="火焰探测" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option label="在线" :value="1" />
          <el-option label="离线" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5">
        <el-button type="primary" plain :icon="Plus" @click="handleAdd">新增点位</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="点位名称" prop="name" align="center" min-width="150" show-overflow-tooltip />
      <el-table-column label="设备编码" prop="deviceCode" align="center" min-width="120" />
      <el-table-column label="点位类型" prop="type" align="center" min-width="100">
        <template #default="{ row }">
          <el-tag size="small">{{ row.type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="安装位置" prop="location" align="center" min-width="150" show-overflow-tooltip />
      <el-table-column label="IP地址" prop="ipAddress" align="center" min-width="130" />
      <el-table-column label="状态" prop="status" align="center" min-width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '在线' : '离线' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="150" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px" append-to-body destroy-on-close @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="点位名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入点位名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备编码" prop="deviceCode">
              <el-input v-model="form.deviceCode" placeholder="请输入设备编码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="点位类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择类型" style="width: 100%">
                <el-option label="摄像头" value="摄像头" />
                <el-option label="烟感" value="烟感" />
                <el-option label="温感" value="温感" />
                <el-option label="门禁" value="门禁" />
                <el-option label="火焰探测" value="火焰探测" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="安装位置" prop="location">
              <el-input v-model="form.location" placeholder="请输入安装位置" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="IP地址" prop="ipAddress">
              <el-input v-model="form.ipAddress" placeholder="请输入IP地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="在线" :value="1" />
                <el-option label="离线" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getMonitorPointPage, getMonitorPoint, addMonitorPoint, updateMonitorPoint, deleteMonitorPoint } from '@/api/safety'

// 视频监控摄像头列表
const cameraList = ref([
  { name: 'CAM-001 正门入口', location: 'A区正门', status: 1 },
  { name: 'CAM-002 地下车库', location: 'B1层车库入口', status: 1 },
  { name: 'CAM-003 电梯间', location: 'A栋1层电梯', status: 1 },
  { name: 'CAM-004 园区周界', location: '东侧围墙', status: 0 },
  { name: 'CAM-005 消防通道', location: 'B栋3层通道', status: 1 },
  { name: 'CAM-006 机房', location: '数据中心机房', status: 1 }
])

// 实时时钟
const currentTime = ref('')
let timer = null
function updateTime() {
  const now = new Date()
  const pad = n => String(n).padStart(2, '0')
  currentTime.value = `${now.getFullYear()}-${pad(now.getMonth() + 1)}-${pad(now.getDate())} ${pad(now.getHours())}:${pad(now.getMinutes())}:${pad(now.getSeconds())}`
}

const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', type: '', status: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const queryRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = {
  name: [{ required: true, message: '点位名称不能为空', trigger: 'blur' }],
  type: [{ required: true, message: '请选择点位类型', trigger: 'change' }]
}

function getList() {
  loading.value = true
  getMonitorPointPage(queryParams).then(res => {
    dataList.value = res.rows || res.data || []
    total.value = res.total || 0
  }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }
function resetForm() { form.value = { id: undefined, name: '', deviceCode: '', type: '', location: '', areaId: undefined, buildingId: undefined, status: 1, ipAddress: '' }; if (formRef.value) formRef.value.resetFields() }
function handleAdd() { resetForm(); dialogTitle.value = '新增监测点位'; dialogVisible.value = true }
function handleEdit(row) { resetForm(); getMonitorPoint(row.id).then(res => { form.value = res.data || res; dialogTitle.value = '编辑监测点位'; dialogVisible.value = true }).catch(() => { form.value = { ...row }; dialogTitle.value = '编辑监测点位'; dialogVisible.value = true }) }
function submitForm() {
  formRef.value.validate(valid => {
    if (valid) {
      const api = form.value.id ? updateMonitorPoint : addMonitorPoint
      api(form.value).then(() => {
        ElMessage.success(form.value.id ? '修改成功' : '新增成功')
        dialogVisible.value = false
        getList()
      }).catch(() => { ElMessage.error('操作失败') })
    }
  })
}
function handleDelete(row) {
  ElMessageBox.confirm('是否确认删除该监测点位？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
    .then(() => {
      deleteMonitorPoint(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') })
    }).catch(() => {})
}

onMounted(() => {
  updateTime()
  timer = setInterval(updateTime, 1000)
  getList()
})
onUnmounted(() => { if (timer) clearInterval(timer) })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }

/* 视频监控区域 */
.video-section {
  margin-bottom: 16px;
  border-radius: 8px;
  overflow: hidden;
  background: #0a0e1a;
  border: 1px solid #1a2332;
}
.video-section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 16px;
  background: linear-gradient(135deg, #0d1926, #132238);
  border-bottom: 1px solid #1e3a5f;
}
.video-section-title {
  color: #00d4ff;
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 1px;
}
.video-section-time {
  color: #7eb8da;
  font-size: 13px;
  font-family: 'Courier New', monospace;
}
.video-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2px;
  padding: 2px;
  background: #0d1926;
}
.video-panel {
  position: relative;
  aspect-ratio: 16 / 9;
  background: #0f1923;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  &:hover {
    .video-overlay { opacity: 1; }
    .video-panel-inner { transform: scale(1.02); }
  }
}
.video-panel-inner {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.3s ease;
}
.video-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}
.video-icon {
  width: 40px;
  height: 40px;
  color: #1a4a3a;
  opacity: 0.6;
}
.offline-icon {
  color: #4a1a1a;
}
.video-status-text {
  color: #3a6a5a;
  font-size: 12px;
  font-family: 'Courier New', monospace;
}
.video-offline .video-status-text {
  color: #6a3a3a;
}
.video-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  padding: 8px 10px;
  background: linear-gradient(180deg, rgba(0,0,0,0.7) 0%, transparent 100%);
  opacity: 0.8;
  transition: opacity 0.3s ease;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}
.video-camera-name {
  color: #e0e8f0;
  font-size: 12px;
  font-weight: 500;
}
.video-camera-info {
  display: flex;
  align-items: center;
  gap: 6px;
}
.video-rec {
  color: #ff3b3b;
  font-size: 10px;
  font-weight: 700;
  animation: blink 1.5s infinite;
  padding: 1px 4px;
  border: 1px solid #ff3b3b;
  border-radius: 2px;
}
@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.3; }
}
.video-camera-location {
  color: #8aa4b8;
  font-size: 11px;
}
.video-timestamp {
  position: absolute;
  bottom: 8px;
  right: 10px;
  color: #7eb8da;
  font-size: 11px;
  font-family: 'Courier New', monospace;
  background: rgba(0,0,0,0.5);
  padding: 1px 6px;
  border-radius: 2px;
}
.video-status-dot {
  position: absolute;
  bottom: 10px;
  left: 10px;
  width: 8px;
  height: 8px;
  border-radius: 50%;
}
.dot-online {
  background: #00e676;
  box-shadow: 0 0 6px #00e676;
  animation: pulse 2s infinite;
}
.dot-offline {
  background: #ff5252;
  box-shadow: 0 0 6px #ff5252;
}
@keyframes pulse {
  0%, 100% { box-shadow: 0 0 4px #00e676; }
  50% { box-shadow: 0 0 12px #00e676; }
}
.video-offline {
  background: #1a0a0a;
  border: 1px solid #3a1a1a;
}

/* 响应式 */
@media (max-width: 1200px) {
  .video-grid { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 768px) {
  .video-grid { grid-template-columns: 1fr; }
}
</style>
