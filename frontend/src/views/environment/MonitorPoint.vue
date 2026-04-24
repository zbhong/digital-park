<template>
  <div>
    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="设备名称" prop="machineName">
        <el-input v-model="queryParams.machineName" placeholder="请输入设备名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="设备类型" prop="machineType">
        <el-select v-model="queryParams.machineType" placeholder="请选择设备类型" clearable style="width: 160px">
          <el-option v-for="item in envDeviceTypes" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option label="在线" value="1" />
          <el-option label="离线" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增设备</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain :icon="Refresh" @click="getList">同步数据</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column type="index" label="序号" width="55" align="center" />
      <el-table-column label="设备名称" prop="machineName" align="center" min-width="150" show-overflow-tooltip />
      <el-table-column label="设备编码" prop="machineCode" align="center" min-width="130" show-overflow-tooltip />
      <el-table-column label="设备类型" prop="machineType" align="center" min-width="100">
        <template #default="{ row }"><el-tag size="small">{{ getDeviceTypeName(row.machineType) }}</el-tag></template>
      </el-table-column>
      <el-table-column label="安装位置" prop="location" align="center" min-width="150" show-overflow-tooltip />
      <el-table-column label="IP地址" prop="ipAddress" align="center" min-width="130" />
      <el-table-column label="状态" prop="status" align="center" min-width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === '1' || row.status === 1 ? 'success' : 'info'" size="small">
            {{ row.status === '1' || row.status === 1 ? '在线' : '离线' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="150" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" :icon="View" @click="handleView(row)">详情</el-button>
          <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
      <template #empty><el-empty description="暂无数据，请确认设备接口是否连通" /></template>
    </el-table>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50, 100]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="650px" append-to-body destroy-on-close @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备名称" prop="machineName">
              <el-input v-model="form.machineName" placeholder="请输入设备名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="设备编码" prop="machineCode">
              <el-input v-model="form.machineCode" placeholder="请输入设备编码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="设备类型" prop="machineType">
              <el-select v-model="form.machineType" placeholder="请选择设备类型" style="width: 100%">
                <el-option v-for="item in envDeviceTypes" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="在线" value="1" />
                <el-option label="离线" value="0" />
              </el-select>
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
            <el-form-item label="端口号" prop="port">
              <el-input v-model="form.port" placeholder="请输入端口号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="安装位置" prop="location">
          <el-input v-model="form.location" placeholder="请输入安装位置" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="设备详情" v-model="detailVisible" width="650px" append-to-body destroy-on-close>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="设备名称">{{ detailData.machineName }}</el-descriptions-item>
        <el-descriptions-item label="设备编码">{{ detailData.machineCode }}</el-descriptions-item>
        <el-descriptions-item label="设备类型">{{ getDeviceTypeName(detailData.machineType) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="detailData.status === '1' || detailData.status === 1 ? 'success' : 'info'" size="small">
            {{ detailData.status === '1' || detailData.status === 1 ? '在线' : '离线' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ detailData.ipAddress }}</el-descriptions-item>
        <el-descriptions-item label="端口号">{{ detailData.port }}</el-descriptions-item>
        <el-descriptions-item label="安装位置" :span="2">{{ detailData.location }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete, View } from '@element-plus/icons-vue'
import { getDeviceMachineList, getDeviceMachineDetail, addDeviceMachine, updateDeviceMachine, deleteDeviceMachine } from '@/api/starnet'

// 环境管理下的设备类型（水浸等）
const envDeviceTypes = ref([
  { label: '水浸', value: '水浸' },
  { label: '温湿度', value: '温湿度' },
  { label: '烟感', value: '烟感' },
  { label: '空气质量', value: '空气质量' },
  { label: '噪声', value: '噪声' },
  { label: '水质', value: '水质' }
])

// 设备类型名称映射
const deviceTypeNameMap = {
  '水浸': '水浸',
  '温湿度': '温湿度',
  '烟感': '烟感',
  '空气质量': '空气质量',
  '噪声': '噪声',
  '水质': '水质'
}

function getDeviceTypeName(type) {
  return deviceTypeNameMap[type] || type || '未知'
}

const queryParams = reactive({ pageNum: 1, pageSize: 10, machineName: '', machineType: '', status: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const queryRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const detailVisible = ref(false)
const detailData = ref({})
const rules = {
  machineName: [{ required: true, message: '设备名称不能为空', trigger: 'blur' }],
  machineCode: [{ required: true, message: '设备编码不能为空', trigger: 'blur' }]
}

function getList() {
  loading.value = true
  getDeviceMachineList(queryParams).then(res => {
    // 兼容若依框架返回格式
    const rows = res.rows || res.data || []
    // 过滤出环境类设备
    const envTypes = envDeviceTypes.value.map(t => t.value)
    dataList.value = Array.isArray(rows) ? rows.filter(item => envTypes.includes(item.machineType)) : []
    total.value = res.total || dataList.value.length
  }).catch(err => {
    console.warn('[环境监测] 接口请求失败，请确认设备系统是否连通:', err.message)
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }

function resetForm() {
  form.value = {
    id: undefined,
    machineName: '',
    machineCode: '',
    machineType: '',
    status: '1',
    ipAddress: '',
    port: '',
    location: '',
    remark: ''
  }
  if (formRef.value) formRef.value.resetFields()
}

function handleAdd() {
  resetForm()
  dialogTitle.value = '新增环境设备'
  dialogVisible.value = true
}

function handleEdit(row) {
  resetForm()
  form.value = { ...row }
  dialogTitle.value = '编辑环境设备'
  dialogVisible.value = true
}

function handleView(row) {
  detailData.value = { ...row }
  detailVisible.value = true
}

function submitForm() {
  formRef.value.validate(valid => {
    if (valid) {
      const api = form.value.id ? updateDeviceMachine : addDeviceMachine
      api(form.value).then(() => {
        ElMessage.success(form.value.id ? '修改成功' : '新增成功')
        dialogVisible.value = false
        getList()
      }).catch(() => { ElMessage.error('操作失败') })
    }
  })
}

function handleDelete(row) {
  ElMessageBox.confirm('是否确认删除该设备？', '系统提示', {
    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
  }).then(() => {
    deleteDeviceMachine(row.id).then(() => {
      ElMessage.success('删除成功')
      getList()
    }).catch(() => { ElMessage.error('删除失败') })
  }).catch(() => {})
}

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
