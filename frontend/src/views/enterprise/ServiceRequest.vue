<template>
  <div>
    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="请求标题" prop="title">
        <el-input v-model="queryParams.title" placeholder="请输入标题" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="请求类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable style="width: 140px">
          <el-option label="政策咨询" value="政策咨询" />
          <el-option label="技术支持" value="技术支持" />
          <el-option label="融资服务" value="融资服务" />
          <el-option label="人才招聘" value="人才招聘" />
          <el-option label="其他" value="其他" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option label="待处理" :value="0" />
          <el-option label="处理中" :value="1" />
          <el-option label="已完成" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增请求</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="请求标题" prop="title" align="center" min-width="180" show-overflow-tooltip />
      <el-table-column label="请求类型" prop="type" align="center" min-width="100">
        <template #default="{ row }"><el-tag size="small">{{ row.type }}</el-tag></template>
      </el-table-column>
      <el-table-column label="描述" prop="description" align="center" min-width="200" show-overflow-tooltip />
      <el-table-column label="状态" prop="status" align="center" min-width="80">
        <template #default="{ row }"><el-tag :type="statusTagMap[row.status]" size="small">{{ row.status }}</el-tag></template>
      </el-table-column>
      <el-table-column label="处理时间" prop="handleTime" align="center" min-width="160" />
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
          <el-col :span="12"><el-form-item label="请求标题" prop="title"><el-input v-model="form.title" placeholder="请输入标题" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="请求类型" prop="type">
            <el-select v-model="form.type" placeholder="请选择类型" style="width: 100%">
              <el-option label="政策咨询" value="政策咨询" /><el-option label="技术支持" value="技术支持" /><el-option label="融资服务" value="融资服务" /><el-option label="人才招聘" value="人才招聘" /><el-option label="其他" value="其他" />
            </el-select>
          </el-form-item></el-col>
        </el-row>
        <el-form-item label="描述" prop="description"><el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" /></el-form-item>
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
import { getServicePage, getService, addService, updateService, deleteService } from '@/api/enterprise'

const statusTagMap = { 0: 'warning', 1: 'primary', 2: 'success' }

const queryParams = reactive({ pageNum: 1, pageSize: 10, title: '', type: '', status: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const queryRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { title: [{ required: true, message: '标题不能为空', trigger: 'blur' }], type: [{ required: true, message: '请选择类型', trigger: 'change' }] }

function getList() {
  loading.value = true
  getServicePage(queryParams).then(res => { dataList.value = res.rows || res.data || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }
function resetForm() { form.value = { id: undefined, enterpriseId: undefined, type: '', title: '', description: '', status: 0, handlerId: undefined, handleTime: '', handleResult: '' }; if (formRef.value) formRef.value.resetFields() }
function handleAdd() { resetForm(); dialogTitle.value = '新增服务请求'; dialogVisible.value = true }
function handleEdit(row) { resetForm(); getService(row.id).then(res => { form.value = res.data || res; dialogTitle.value = '编辑服务请求'; dialogVisible.value = true }).catch(() => { form.value = { ...row }; dialogTitle.value = '编辑服务请求'; dialogVisible.value = true }) }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.id ? updateService : addService; api(form.value).then(() => { ElMessage.success(form.value.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除该服务请求？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteService(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
