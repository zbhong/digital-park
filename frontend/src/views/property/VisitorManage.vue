<template>
  <div>
    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="访客姓名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option label="待审批" value="待审批" />
          <el-option label="已审批" value="已审批" />
          <el-option label="已离开" value="已离开" />
          <el-option label="已拒绝" value="已拒绝" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增访客</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="访客姓名" prop="name" align="center" min-width="100" />
      <el-table-column label="电话" prop="phone" align="center" min-width="130" />
      <el-table-column label="身份证号" prop="idCard" align="center" min-width="180" />
      <el-table-column label="访问企业" prop="visitEnterprise" align="center" min-width="150" show-overflow-tooltip />
      <el-table-column label="访问人" prop="visitPerson" align="center" min-width="100" />
      <el-table-column label="来访目的" prop="purpose" align="center" min-width="120" show-overflow-tooltip />
      <el-table-column label="来访日期" prop="visitDate" align="center" min-width="120" />
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
          <el-col :span="12"><el-form-item label="访客姓名" prop="name"><el-input v-model="form.name" placeholder="请输入姓名" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="电话" prop="phone"><el-input v-model="form.phone" placeholder="请输入电话" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="身份证号" prop="idCard"><el-input v-model="form.idCard" placeholder="请输入身份证号" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="来访日期" prop="visitDate"><el-date-picker v-model="form.visitDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="访问企业" prop="visitEnterprise"><el-input v-model="form.visitEnterprise" placeholder="请输入访问企业" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="访问人" prop="visitPerson"><el-input v-model="form.visitPerson" placeholder="请输入访问人" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="来访目的" prop="purpose"><el-input v-model="form.purpose" placeholder="请输入来访目的" /></el-form-item>
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
import { getVisitorPage, getVisitor, addVisitor, updateVisitor, deleteVisitor } from '@/api/property'

const statusTagMap = { '待审批': 'warning', '已审批': 'success', '已离开': 'info', '已拒绝': 'danger' }

const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', status: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const queryRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { name: [{ required: true, message: '访客姓名不能为空', trigger: 'blur' }], phone: [{ required: true, message: '电话不能为空', trigger: 'blur' }] }

function getList() {
  loading.value = true
  getVisitorPage(queryParams).then(res => { dataList.value = res.rows || res.data || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }
function resetForm() { form.value = { id: undefined, name: '', phone: '', idCard: '', visitEnterprise: '', visitPerson: '', purpose: '', visitDate: '', status: '待审批' }; if (formRef.value) formRef.value.resetFields() }
function handleAdd() { resetForm(); dialogTitle.value = '新增访客'; dialogVisible.value = true }
function handleEdit(row) { resetForm(); getVisitor(row.id).then(res => { form.value = res.data || res; dialogTitle.value = '编辑访客'; dialogVisible.value = true }).catch(() => { form.value = { ...row }; dialogTitle.value = '编辑访客'; dialogVisible.value = true }) }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.id ? updateVisitor : addVisitor; api(form.value).then(() => { ElMessage.success(form.value.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除该访客记录？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteVisitor(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
