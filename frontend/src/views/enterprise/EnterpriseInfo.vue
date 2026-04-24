<template>
  <div>
    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="企业名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入企业名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
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
          <el-option label="入驻" :value="1" />
          <el-option label="退出" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增企业</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="企业名称" prop="name" align="center" min-width="180" show-overflow-tooltip />
      <el-table-column label="统一社会信用代码" prop="code" align="center" min-width="180" />
      <el-table-column label="行业" prop="industry" align="center" min-width="100">
        <template #default="{ row }"><el-tag size="small">{{ row.industry }}</el-tag></template>
      </el-table-column>
      <el-table-column label="规模" prop="scale" align="center" min-width="80" />
      <el-table-column label="联系人" prop="contactPerson" align="center" min-width="100" />
      <el-table-column label="联系电话" prop="contactPhone" align="center" min-width="130" />
      <el-table-column label="入驻日期" prop="enterDate" align="center" min-width="120" />
      <el-table-column label="状态" prop="status" align="center" min-width="80">
        <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '入驻' : '退出' }}</el-tag></template>
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
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="企业名称" prop="name"><el-input v-model="form.name" placeholder="请输入企业名称" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="信用代码" prop="code"><el-input v-model="form.code" placeholder="请输入统一社会信用代码" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="行业" prop="industry">
            <el-select v-model="form.industry" placeholder="请选择行业" style="width: 100%">
              <el-option label="信息技术" value="信息技术" /><el-option label="生物医药" value="生物医药" /><el-option label="智能制造" value="智能制造" /><el-option label="新能源" value="新能源" /><el-option label="其他" value="其他" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="规模" prop="scale"><el-input v-model="form.scale" placeholder="请输入企业规模" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="联系人" prop="contactPerson"><el-input v-model="form.contactPerson" placeholder="请输入联系人" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="联系电话" prop="contactPhone"><el-input v-model="form.contactPhone" placeholder="请输入联系电话" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="邮箱" prop="email"><el-input v-model="form.email" placeholder="请输入邮箱" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="入驻日期" prop="enterDate"><el-date-picker v-model="form.enterDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="地址" prop="address"><el-input v-model="form.address" placeholder="请输入地址" /></el-form-item>
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
import { getEnterpriseInfoPage, getEnterpriseInfo, addEnterpriseInfo, updateEnterpriseInfo, deleteEnterpriseInfo } from '@/api/enterprise'

const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', industry: '', status: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const queryRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { name: [{ required: true, message: '企业名称不能为空', trigger: 'blur' }], code: [{ required: true, message: '信用代码不能为空', trigger: 'blur' }] }

function getList() {
  loading.value = true
  getEnterpriseInfoPage(queryParams).then(res => { dataList.value = res.rows || res.data || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }
function resetForm() { form.value = { id: undefined, name: '', code: '', industry: '', scale: '', contactPerson: '', contactPhone: '', email: '', address: '', areaId: undefined, buildingId: undefined, floor: '', room: '', enterDate: '', contractExpire: '', status: 1 }; if (formRef.value) formRef.value.resetFields() }
function handleAdd() { resetForm(); dialogTitle.value = '新增企业'; dialogVisible.value = true }
function handleEdit(row) { resetForm(); getEnterpriseInfo(row.id).then(res => { form.value = res.data || res; dialogTitle.value = '编辑企业'; dialogVisible.value = true }).catch(() => { form.value = { ...row }; dialogTitle.value = '编辑企业'; dialogVisible.value = true }) }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.id ? updateEnterpriseInfo : addEnterpriseInfo; api(form.value).then(() => { ElMessage.success(form.value.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除该企业？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteEnterpriseInfo(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
