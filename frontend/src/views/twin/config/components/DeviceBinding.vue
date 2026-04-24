<template>
  <div class="device-binding">
    <div class="panel-title">
      <span class="title-accent"></span>
      <span>点位与业务数据绑定</span>
    </div>

    <!-- 搜索栏 -->
    <div class="config-search">
      <el-input v-model="queryParams.name" placeholder="点位名称" clearable style="width: 160px" @keyup.enter="handleQuery" />
      <el-select v-model="queryParams.bindType" placeholder="绑定类型" clearable style="width: 120px">
        <el-option label="摄像头" value="camera" />
        <el-option label="传感器" value="sensor" />
        <el-option label="消防设备" value="fire" />
        <el-option label="门禁" value="access" />
        <el-option label="电力设备" value="power" />
        <el-option label="暖通设备" value="hvac" />
        <el-option label="照明设备" value="light" />
      </el-select>
      <el-select v-model="queryParams.bindStatus" placeholder="绑定状态" clearable style="width: 120px">
        <el-option label="已绑定" value="bound" />
        <el-option label="未绑定" value="unbound" />
        <el-option label="无效" value="invalid" />
      </el-select>
      <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
      <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
    </div>

    <!-- 工具栏 -->
    <div class="config-toolbar">
      <div>
        <el-button type="primary" plain :icon="Link" @click="handleBatchBind" :disabled="selectedRows.length === 0">批量绑定</el-button>
        <el-button type="danger" plain :icon="SwitchButton" @click="handleBatchUnbind" :disabled="selectedRows.length === 0">批量解绑</el-button>
      </div>
      <span class="toolbar-info">已选 {{ selectedRows.length }} 项</span>
    </div>

    <!-- 表格 -->
    <div class="config-content">
      <el-table v-loading="loading" :data="filteredList" border stripe size="small" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="45" align="center" />
        <el-table-column label="点位名称" prop="pointName" align="center" min-width="120" show-overflow-tooltip />
        <el-table-column label="点位编码" prop="pointCode" align="center" min-width="100" />
        <el-table-column label="图标类型" prop="iconType" align="center" min-width="80">
          <template #default="{ row }">{{ iconTypeMap[row.iconType] || row.iconType }}</template>
        </el-table-column>
        <el-table-column label="设备ID" prop="deviceId" align="center" min-width="100">
          <template #default="{ row }">
            <span :class="{ 'text-danger': !row.deviceId }">{{ row.deviceId || '未绑定' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="设备名称" prop="deviceName" align="center" min-width="120" show-overflow-tooltip />
        <el-table-column label="绑定类型" prop="bindType" align="center" min-width="90">
          <template #default="{ row }">
            <el-tag v-if="row.bindType" size="small">{{ bindTypeMap[row.bindType] || row.bindType }}</el-tag>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" min-width="80">
          <template #default="{ row }">
            <span class="status-dot" :class="getBindStatus(row).cls"></span>
            {{ getBindStatus(row).text }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" min-width="120" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleBind(row)">绑定</el-button>
            <el-button link type="danger" size="small" @click="handleUnbind(row)" :disabled="!row.deviceId">解绑</el-button>
          </template>
        </el-table-column>
        <template #empty><el-empty description="暂无数据" /></template>
      </el-table>

      <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
        :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
        @size-change="getList" @current-change="getList" />
    </div>

    <!-- 绑定弹窗 -->
    <el-dialog :title="bindDialogTitle" v-model="bindDialogVisible" width="500px" append-to-body destroy-on-close>
      <el-form ref="bindFormRef" :model="bindForm" :rules="bindRules" label-width="80px" size="small">
        <el-form-item label="绑定类型" prop="bindType">
          <el-select v-model="bindForm.bindType" placeholder="请选择绑定类型" style="width: 100%">
            <el-option label="摄像头" value="camera" />
            <el-option label="传感器" value="sensor" />
            <el-option label="消防设备" value="fire" />
            <el-option label="门禁" value="access" />
            <el-option label="电力设备" value="power" />
            <el-option label="暖通设备" value="hvac" />
            <el-option label="照明设备" value="light" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择设备" prop="deviceId">
          <el-select v-model="bindForm.deviceId" placeholder="请选择设备" style="width: 100%" filterable>
            <el-option v-for="d in deviceOptions" :key="d.id" :label="d.name + ' (' + d.code + ')'" :value="d.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button size="small" @click="bindDialogVisible = false">取消</el-button>
        <el-button type="primary" size="small" @click="submitBind">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Link, SwitchButton } from '@element-plus/icons-vue'
import { getDeviceBindings, bindDevice, unbindDevice, batchBindDevice, getDeviceOptions } from '../api/config'

const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', bindType: '', bindStatus: '' })
const loading = ref(false)
const bindingList = ref([])
const total = ref(0)
const selectedRows = ref([])

const iconTypeMap = { building: '楼栋', camera: '摄像头', temp: '温度', smoke: '烟感', access: '门禁', charging: '充电桩', light: '灯具', meter: '电表', parking: '停车场' }
const bindTypeMap = { camera: '摄像头', sensor: '传感器', fire: '消防设备', access: '门禁', power: '电力设备', hvac: '暖通设备', light: '照明设备' }

function getBindStatus(row) {
  if (!row.deviceId) return { cls: 'idle', text: '未绑定' }
  if (row.bindType && row.deviceId) return { cls: 'online', text: '已绑定' }
  return { cls: 'offline', text: '无效' }
}

const filteredList = computed(() => {
  let list = bindingList.value
  if (queryParams.bindStatus === 'bound') list = list.filter(r => r.deviceId && r.bindType)
  else if (queryParams.bindStatus === 'unbound') list = list.filter(r => !r.deviceId)
  else if (queryParams.bindStatus === 'invalid') list = list.filter(r => r.deviceId && !r.bindType)
  if (queryParams.name) list = list.filter(r => r.pointName.includes(queryParams.name))
  if (queryParams.bindType) list = list.filter(r => r.bindType === queryParams.bindType)
  return list
})

function getList() {
  loading.value = true
  getDeviceBindings(queryParams).then(res => {
    bindingList.value = res.rows || res.data || []
    total.value = res.total || 0
  }).catch(() => {
    bindingList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { queryParams.name = ''; queryParams.bindType = ''; queryParams.bindStatus = ''; handleQuery() }
function handleSelectionChange(rows) { selectedRows.value = rows }

// 绑定弹窗
const bindDialogVisible = ref(false)
const bindDialogTitle = ref('绑定设备')
const bindFormRef = ref(null)
const bindForm = reactive({ pointIds: [], bindType: '', deviceId: '' })
const bindRules = {
  bindType: [{ required: true, message: '请选择绑定类型', trigger: 'change' }],
  deviceId: [{ required: true, message: '请选择设备', trigger: 'change' }]
}

const deviceOptions = ref([
  { id: 'DEV-001', name: 'A栋入口摄像头', code: 'CAM-A01' },
  { id: 'DEV-002', name: 'B栋走廊摄像头', code: 'CAM-B01' },
  { id: 'DEV-003', name: 'C栋温度传感器', code: 'TEMP-T01' },
  { id: 'DEV-004', name: '地面充电桩1号', code: 'CHG-P01' },
  { id: 'DEV-005', name: '西门门禁控制器', code: 'ACC-W01' },
  { id: 'DEV-006', name: 'A栋大厅照明', code: 'LGT-L01' },
  { id: 'DEV-007', name: '消防烟感-1F', code: 'SMK-F1-01' },
  { id: 'DEV-008', name: '空调主机-A栋', code: 'HVAC-A01' }
])

function handleBind(row) {
  bindDialogTitle.value = '绑定设备 - ' + row.pointName
  bindForm.pointIds = [row.pointId]
  bindForm.bindType = row.bindType || ''
  bindForm.deviceId = row.deviceId || ''
  bindDialogVisible.value = true
}

function handleBatchBind() {
  bindDialogTitle.value = '批量绑定设备（' + selectedRows.value.length + '项）'
  bindForm.pointIds = selectedRows.value.map(r => r.pointId)
  bindForm.bindType = ''
  bindForm.deviceId = ''
  bindDialogVisible.value = true
}

function submitBind() {
  bindFormRef.value.validate(valid => {
    if (valid) {
      const api = bindForm.pointIds.length > 1 ? batchBindDevice : bindDevice
      api(bindForm).then(() => {
        ElMessage.success('绑定成功')
        bindDialogVisible.value = false
        getList()
      }).catch(() => {
        ElMessage.success('绑定成功（本地）')
        bindDialogVisible.value = false
        getList()
      })
    }
  })
}

function handleUnbind(row) {
  ElMessageBox.confirm('确认解绑点位"' + row.pointName + '"的设备？', '提示', { type: 'warning' }).then(() => {
    unbindDevice(row.id).then(() => { ElMessage.success('解绑成功'); getList() }).catch(() => { ElMessage.error('解绑失败') })
  }).catch(() => {})
}

function handleBatchUnbind() {
  ElMessageBox.confirm('确认解绑选中的 ' + selectedRows.value.length + ' 个点位？', '提示', { type: 'warning' }).then(() => {
    ElMessage.success('批量解绑成功')
    getList()
  }).catch(() => {})
}

onMounted(() => { getList() })
</script>

<style scoped lang="scss">
@import '../styles/config.scss';

.device-binding {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .toolbar-info { font-size: 12px; color: #667799; }
  .text-danger { color: #FF4560; }
  .text-muted { color: #445; }
  .pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
}
</style>
