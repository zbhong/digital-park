<template>
  <div>
    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="车牌号" prop="plateNumber">
        <el-input v-model="queryParams.plateNumber" placeholder="请输入车牌号" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="车主类型" prop="ownerType">
        <el-select v-model="queryParams.ownerType" placeholder="请选择类型" clearable style="width: 140px">
          <el-option label="企业车辆" value="企业车辆" />
          <el-option label="员工车辆" value="员工车辆" />
          <el-option label="访客车辆" value="访客车辆" />
          <el-option label="临时车辆" value="临时车辆" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option label="正常" :value="1" />
          <el-option label="过期" :value="0" />
          <el-option label="黑名单" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增车辆</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="车牌号" prop="plateNumber" align="center" min-width="120" />
      <el-table-column label="车主姓名" prop="ownerName" align="center" min-width="100" />
      <el-table-column label="车主类型" prop="ownerType" align="center" min-width="100">
        <template #default="{ row }"><el-tag size="small">{{ row.ownerType }}</el-tag></template>
      </el-table-column>
      <el-table-column label="卡类型" prop="cardType" align="center" min-width="100">
        <template #default="{ row }"><el-tag size="small" type="warning">{{ row.cardType }}</el-tag></template>
      </el-table-column>
      <el-table-column label="状态" prop="status" align="center" min-width="80">
        <template #default="{ row }"><el-tag :type="statusTagMap[row.status]" size="small">{{ row.status }}</el-tag></template>
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
          <el-col :span="12"><el-form-item label="车牌号" prop="plateNumber"><el-input v-model="form.plateNumber" placeholder="请输入车牌号" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="车主姓名" prop="ownerName"><el-input v-model="form.ownerName" placeholder="请输入车主姓名" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="车主类型" prop="ownerType">
            <el-select v-model="form.ownerType" placeholder="请选择" style="width: 100%">
              <el-option label="企业车辆" value="企业车辆" /><el-option label="员工车辆" value="员工车辆" /><el-option label="访客车辆" value="访客车辆" /><el-option label="临时车辆" value="临时车辆" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="卡类型" prop="cardType">
            <el-select v-model="form.cardType" placeholder="请选择" style="width: 100%">
              <el-option label="月卡" value="月卡" /><el-option label="年卡" value="年卡" /><el-option label="临时卡" value="临时卡" />
            </el-select>
          </el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
              <el-option label="正常" :value="1" /><el-option label="过期" :value="0" /><el-option label="黑名单" :value="2" />
            </el-select>
          </el-form-item></el-col>
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getVehiclePage, getVehicle, addVehicle, updateVehicle, deleteVehicle } from '@/api/property'

const statusTagMap = { 1: 'success', 0: 'info', 2: 'danger' }

const queryParams = reactive({ pageNum: 1, pageSize: 10, plateNumber: '', ownerType: '', status: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const queryRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { plateNumber: [{ required: true, message: '车牌号不能为空', trigger: 'blur' }], ownerName: [{ required: true, message: '车主姓名不能为空', trigger: 'blur' }] }

function getList() {
  loading.value = true
  getVehiclePage(queryParams).then(res => { dataList.value = res.rows || res.data || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }
function resetForm() { form.value = { id: undefined, plateNumber: '', ownerName: '', ownerType: '', enterpriseId: undefined, cardType: '', status: 1 }; if (formRef.value) formRef.value.resetFields() }
function handleAdd() { resetForm(); dialogTitle.value = '新增车辆'; dialogVisible.value = true }
function handleEdit(row) { resetForm(); getVehicle(row.id).then(res => { form.value = res.data || res; dialogTitle.value = '编辑车辆'; dialogVisible.value = true }).catch(() => { form.value = { ...row }; dialogTitle.value = '编辑车辆'; dialogVisible.value = true }) }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.id ? updateVehicle : addVehicle; api(form.value).then(() => { ElMessage.success(form.value.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除该车辆？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteVehicle(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
