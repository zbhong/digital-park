<template>
  <div class="app-container">
    <el-form :model="queryParams" :inline="true" class="search-form">
      <el-form-item label="参数名称" prop="configName"><el-input v-model="queryParams.configName" placeholder="请输入参数名称" clearable style="width: 200px" @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item label="参数键名" prop="configKey"><el-input v-model="queryParams.configKey" placeholder="请输入参数键名" clearable style="width: 200px" @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item><el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button><el-button :icon="Refresh" @click="resetQuery">重置</el-button></el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增参数</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="参数ID" prop="configId" align="center" min-width="80" />
      <el-table-column label="参数名称" prop="configName" align="center" min-width="200" show-overflow-tooltip />
      <el-table-column label="参数键名" prop="configKey" align="center" min-width="200" />
      <el-table-column label="参数键值" prop="configValue" align="center" min-width="150" show-overflow-tooltip />
      <el-table-column label="系统内置" prop="configType" align="center" min-width="80"><template #default="{ row }"><el-tag :type="row.configType === 'Y' ? 'danger' : 'info'" size="small">{{ row.configType === 'Y' ? '是' : '否' }}</el-tag></template></el-table-column>
      <el-table-column label="操作" align="center" min-width="150" fixed="right"><template #default="{ row }"><el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button><el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button></template></el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" append-to-body destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="参数名称" prop="configName"><el-input v-model="form.configName" placeholder="请输入参数名称" /></el-form-item>
        <el-form-item label="参数键名" prop="configKey"><el-input v-model="form.configKey" placeholder="请输入参数键名" /></el-form-item>
        <el-form-item label="参数键值" prop="configValue"><el-input v-model="form.configValue" placeholder="请输入参数键值" /></el-form-item>
        <el-form-item label="备注" prop="remark"><el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取 消</el-button><el-button type="primary" @click="submitForm">确 定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { listConfig, addConfig, updateConfig, deleteConfig } from '@/api/system'

const queryParams = reactive({ pageNum: 1, pageSize: 10, configName: '', configKey: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { configName: [{ required: true, message: '参数名称不能为空', trigger: 'blur' }], configKey: [{ required: true, message: '参数键名不能为空', trigger: 'blur' }], configValue: [{ required: true, message: '参数键值不能为空', trigger: 'blur' }] }

function getList() {
  loading.value = true
  listConfig(queryParams).then(res => { dataList.value = res.rows || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { queryParams.configName = ''; queryParams.configKey = ''; handleQuery() }
function handleAdd() { form.value = { configName: '', configKey: '', configValue: '', remark: '' }; dialogTitle.value = '新增参数'; dialogVisible.value = true }
function handleEdit(row) { form.value = { ...row }; dialogTitle.value = '编辑参数'; dialogVisible.value = true }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.configId ? updateConfig : addConfig; api(form.value).then(() => { ElMessage.success('操作成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteConfig(row.configId).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
