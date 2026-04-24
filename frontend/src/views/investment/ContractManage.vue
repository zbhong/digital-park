<template>
  <div>
    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="合同编号" prop="contractNo">
        <el-input v-model="queryParams.contractNo" placeholder="请输入合同编号" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option label="草稿" :value="0" />
          <el-option label="生效中" :value="1" />
          <el-option label="已到期" :value="2" />
          <el-option label="已终止" :value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增合同</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="合同编号" prop="contractNo" align="center" min-width="150" />
      <el-table-column label="客户ID" prop="customerId" align="center" min-width="100" />
      <el-table-column label="企业ID" prop="enterpriseId" align="center" min-width="100" />
      <el-table-column label="房源ID" prop="assetId" align="center" min-width="100" />
      <el-table-column label="月租金(元)" prop="rentAmount" align="center" min-width="120" />
      <el-table-column label="押金(元)" prop="deposit" align="center" min-width="120" />
      <el-table-column label="开始日期" prop="startDate" align="center" min-width="120" />
      <el-table-column label="结束日期" prop="endDate" align="center" min-width="120" />
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

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="700px" append-to-body destroy-on-close @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="合同编号" prop="contractNo"><el-input v-model="form.contractNo" placeholder="请输入合同编号" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="客户ID" prop="customerId"><el-input-number v-model="form.customerId" :min="1" style="width: 100%" placeholder="请输入客户ID" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="企业ID" prop="enterpriseId"><el-input-number v-model="form.enterpriseId" :min="1" style="width: 100%" placeholder="请输入企业ID" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="房源ID" prop="assetId"><el-input-number v-model="form.assetId" :min="1" style="width: 100%" placeholder="请输入房源ID" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="月租金(元)" prop="rentAmount"><el-input-number v-model="form.rentAmount" :min="0" :precision="2" style="width: 100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="押金(元)" prop="deposit"><el-input-number v-model="form.deposit" :min="0" :precision="2" style="width: 100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="开始日期" prop="startDate"><el-date-picker v-model="form.startDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="结束日期" prop="endDate"><el-date-picker v-model="form.endDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
              <el-option label="草稿" :value="0" /><el-option label="生效中" :value="1" /><el-option label="已到期" :value="2" /><el-option label="已终止" :value="3" />
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
import { getContractPage, getContract, addContract, updateContract, deleteContract } from '@/api/investment'

const statusTagMap = { 0: 'info', 1: 'success', 2: 'warning', 3: 'danger' }

const queryParams = reactive({ pageNum: 1, pageSize: 10, contractNo: '', status: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const queryRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { contractNo: [{ required: true, message: '合同编号不能为空', trigger: 'blur' }], customerId: [{ required: true, message: '客户ID不能为空', trigger: 'blur' }] }

function getList() {
  loading.value = true
  getContractPage(queryParams).then(res => { dataList.value = res.rows || res.data || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }
function resetForm() { form.value = { id: undefined, customerId: undefined, enterpriseId: undefined, assetId: undefined, contractNo: '', startDate: '', endDate: '', rentAmount: 0, deposit: 0, status: 0 }; if (formRef.value) formRef.value.resetFields() }
function handleAdd() { resetForm(); dialogTitle.value = '新增合同'; dialogVisible.value = true }
function handleEdit(row) { resetForm(); getContract(row.id).then(res => { form.value = res.data || res; dialogTitle.value = '编辑合同'; dialogVisible.value = true }).catch(() => { form.value = { ...row }; dialogTitle.value = '编辑合同'; dialogVisible.value = true }) }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.id ? updateContract : addContract; api(form.value).then(() => { ElMessage.success(form.value.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除该合同？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteContract(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
