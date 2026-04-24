<template>
  <div>
    <el-row :gutter="16" class="stat-cards">
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #409EFF">{{ stats.total }}</div><div class="stat-label">充电桩总数</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #67C23A">{{ stats.available }}</div><div class="stat-label">空闲</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #E6A23C">{{ stats.charging }}</div><div class="stat-label">充电中</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #F56C6C">{{ stats.fault }}</div><div class="stat-label">故障</div></el-card></el-col>
    </el-row>

    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="桩名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入桩名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option label="空闲" value="available" /><el-option label="充电中" value="charging" />
          <el-option label="离线" value="offline" /><el-option label="故障" value="fault" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增充电桩</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="桩名称" prop="name" align="center" min-width="150" />
      <el-table-column label="编码" prop="code" align="center" min-width="120" />
      <el-table-column label="功率" prop="power" align="center" min-width="100" />
      <el-table-column label="位置" prop="location" align="center" min-width="120" />
      <el-table-column label="状态" prop="status" align="center" min-width="80">
        <template #default="{ row }"><el-tag :type="statusTagMap[row.status]" size="small">{{ statusNameMap[row.status] }}</el-tag></template>
      </el-table-column>
      <el-table-column label="今日充电量" prop="todayKwh" align="center" min-width="100" />
      <el-table-column label="今日收入" prop="todayIncome" align="center" min-width="100" />
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
          <el-col :span="12"><el-form-item label="桩名称" prop="name"><el-input v-model="form.name" placeholder="请输入桩名称" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="编码" prop="code"><el-input v-model="form.code" placeholder="请输入编码" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="功率" prop="power"><el-input v-model="form.power" placeholder="请输入功率" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="位置" prop="location"><el-input v-model="form.location" placeholder="请输入位置" /></el-form-item></el-col>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getChargingList, getCharging, addCharging, updateCharging, deleteCharging } from '@/api/energy'

const stats = computed(() => ({
  total: total.value,
  available: dataList.value.filter(item => item.status === 'available').length,
  charging: dataList.value.filter(item => item.status === 'charging').length,
  fault: dataList.value.filter(item => item.status === 'fault').length
}))
const statusTagMap = { available: 'success', charging: 'primary', offline: 'info', fault: 'danger' }
const statusNameMap = { available: '空闲', charging: '充电中', offline: '离线', fault: '故障' }

const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', status: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const queryRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { name: [{ required: true, message: '桩名称不能为空', trigger: 'blur' }], code: [{ required: true, message: '编码不能为空', trigger: 'blur' }] }

function getList() {
  loading.value = true
  getChargingList(queryParams).then(res => { dataList.value = res.rows || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }
function resetForm() { form.value = { id: undefined, name: '', code: '', power: '', location: '' }; if (formRef.value) formRef.value.resetFields() }
function handleAdd() { resetForm(); dialogTitle.value = '新增充电桩'; dialogVisible.value = true }
function handleEdit(row) { resetForm(); getCharging(row.id).then(res => { form.value = res.data || res; dialogTitle.value = '编辑充电桩'; dialogVisible.value = true }).catch(() => { form.value = { ...row }; dialogTitle.value = '编辑充电桩'; dialogVisible.value = true }) }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.id ? updateCharging : addCharging; api(form.value).then(() => { ElMessage.success('操作成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteCharging(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

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
