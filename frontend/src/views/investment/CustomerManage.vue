<template>
  <div>
    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="客户名称" prop="company">
        <el-input v-model="queryParams.company" placeholder="请输入公司名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="行业" prop="industry">
        <el-select v-model="queryParams.industry" placeholder="请选择行业" clearable style="width: 140px">
          <el-option label="信息技术" value="信息技术" />
          <el-option label="生物医药" value="生物医药" />
          <el-option label="智能制造" value="智能制造" />
          <el-option label="新能源" value="新能源" />
          <el-option label="其他" value="其他" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option label="潜在" value="潜在" />
          <el-option label="意向" value="意向" />
          <el-option label="已签约" value="已签约" />
          <el-option label="已流失" value="已流失" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增客户</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="公司名称" prop="company" align="center" min-width="180" show-overflow-tooltip />
      <el-table-column label="联系人" prop="contactPerson" align="center" min-width="100" />
      <el-table-column label="联系电话" prop="contactPhone" align="center" min-width="130" />
      <el-table-column label="行业" prop="industry" align="center" min-width="100">
        <template #default="{ row }"><el-tag size="small">{{ row.industry }}</el-tag></template>
      </el-table-column>
      <el-table-column label="需求面积(m2)" prop="demandArea" align="center" min-width="120" />
      <el-table-column label="预算(万元)" prop="demandBudget" align="center" min-width="120" />
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
          <el-col :span="12"><el-form-item label="公司名称" prop="company"><el-input v-model="form.company" placeholder="请输入公司名称" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="行业" prop="industry">
            <el-select v-model="form.industry" placeholder="请选择行业" style="width: 100%">
              <el-option label="信息技术" value="信息技术" /><el-option label="生物医药" value="生物医药" /><el-option label="智能制造" value="智能制造" /><el-option label="新能源" value="新能源" /><el-option label="其他" value="其他" />
            </el-select>
          </el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="联系人" prop="contactPerson"><el-input v-model="form.contactPerson" placeholder="请输入联系人" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="联系电话" prop="contactPhone"><el-input v-model="form.contactPhone" placeholder="请输入联系电话" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="需求面积" prop="demandArea"><el-input-number v-model="form.demandArea" :min="0" style="width: 100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="预算(万元)" prop="demandBudget"><el-input-number v-model="form.demandBudget" :min="0" :precision="2" style="width: 100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="来源" prop="source">
            <el-select v-model="form.source" placeholder="请选择" style="width: 100%">
              <el-option label="自主开发" value="自主开发" /><el-option label="渠道推荐" value="渠道推荐" /><el-option label="展会活动" value="展会活动" /><el-option label="网络推广" value="网络推广" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
              <el-option label="潜在" value="潜在" /><el-option label="意向" value="意向" /><el-option label="已签约" value="已签约" /><el-option label="已流失" value="已流失" />
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
import { getCustomerPage, getCustomer, addCustomer, updateCustomer, deleteCustomer } from '@/api/investment'

const statusTagMap = { '潜在': 'info', '意向': 'warning', '已签约': 'success', '已流失': 'danger' }

const queryParams = reactive({ pageNum: 1, pageSize: 10, company: '', industry: '', status: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const queryRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { company: [{ required: true, message: '公司名称不能为空', trigger: 'blur' }], contactPerson: [{ required: true, message: '联系人不能为空', trigger: 'blur' }] }

function getList() {
  loading.value = true
  getCustomerPage(queryParams).then(res => { dataList.value = res.rows || res.data || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }
function resetForm() { form.value = { id: undefined, company: '', contactPerson: '', contactPhone: '', industry: '', demandArea: 0, demandBudget: 0, source: '', status: '潜在', followUserId: undefined }; if (formRef.value) formRef.value.resetFields() }
function handleAdd() { resetForm(); dialogTitle.value = '新增客户'; dialogVisible.value = true }
function handleEdit(row) { resetForm(); getCustomer(row.id).then(res => { form.value = res.data || res; dialogTitle.value = '编辑客户'; dialogVisible.value = true }).catch(() => { form.value = { ...row }; dialogTitle.value = '编辑客户'; dialogVisible.value = true }) }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.id ? updateCustomer : addCustomer; api(form.value).then(() => { ElMessage.success(form.value.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除该客户？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteCustomer(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
