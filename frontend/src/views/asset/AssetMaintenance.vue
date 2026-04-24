<template>
  <div>
    <el-form :model="queryParams" :inline="true" class="search-form">
      <el-form-item label="资产名称" prop="assetName"><el-input v-model="queryParams.assetName" placeholder="请输入资产名称" clearable style="width: 200px" @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item label="维保类型" prop="type"><el-select v-model="queryParams.type" placeholder="请选择" clearable style="width: 120px"><el-option label="定期保养" value="regular" /><el-option label="故障维修" value="repair" /><el-option label="巡检" value="inspect" /></el-select></el-form-item>
      <el-form-item label="状态" prop="status"><el-select v-model="queryParams.status" placeholder="请选择" clearable style="width: 120px"><el-option label="待执行" value="pending" /><el-option label="进行中" value="ongoing" /><el-option label="已完成" value="completed" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button><el-button :icon="Refresh" @click="resetQuery">重置</el-button></el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增维保</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="资产名称" prop="assetName" align="center" min-width="150" />
      <el-table-column label="维保类型" prop="type" align="center" min-width="100"><template #default="{ row }"><el-tag size="small">{{ typeNameMap[row.type] }}</el-tag></template></el-table-column>
      <el-table-column label="维保内容" prop="content" align="center" min-width="200" show-overflow-tooltip />
      <el-table-column label="计划日期" prop="planDate" align="center" min-width="120" />
      <el-table-column label="负责人" prop="leader" align="center" min-width="100" />
      <el-table-column label="状态" prop="status" align="center" min-width="80"><template #default="{ row }"><el-tag :type="statusTagMap[row.status]" size="small">{{ statusNameMap[row.status] }}</el-tag></template></el-table-column>
      <el-table-column label="操作" align="center" min-width="150" fixed="right">
        <template #default="{ row }"><el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button><el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button></template>
      </el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px" append-to-body destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="资产名称" prop="assetName"><el-input v-model="form.assetName" placeholder="请输入资产名称" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="维保类型" prop="type"><el-select v-model="form.type" placeholder="请选择" style="width: 100%"><el-option label="定期保养" value="regular" /><el-option label="故障维修" value="repair" /><el-option label="巡检" value="inspect" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="计划日期" prop="planDate"><el-date-picker v-model="form.planDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="维保内容" prop="content"><el-input v-model="form.content" type="textarea" :rows="3" placeholder="请输入维保内容" /></el-form-item>
        <el-form-item label="负责人" prop="leader"><el-input v-model="form.leader" placeholder="请输入负责人" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取 消</el-button><el-button type="primary" @click="submitForm">确 定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getMaintenancePage, addMaintenance, updateMaintenance, deleteMaintenance } from '@/api/asset'

const typeNameMap = { regular: '定期保养', repair: '故障维修', inspect: '巡检' }
const statusTagMap = { pending: 'warning', ongoing: 'primary', completed: 'success' }
const statusNameMap = { pending: '待执行', ongoing: '进行中', completed: '已完成' }

const queryParams = reactive({ pageNum: 1, pageSize: 10, assetName: '', type: '', status: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { assetName: [{ required: true, message: '资产名称不能为空', trigger: 'blur' }], type: [{ required: true, message: '请选择维保类型', trigger: 'change' }] }

function getList() {
  loading.value = true
  getMaintenancePage(queryParams).then(res => { dataList.value = res.rows || res.data || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { queryParams.assetName = ''; queryParams.type = ''; queryParams.status = ''; handleQuery() }
function handleAdd() { form.value = { id: undefined, assetName: '', type: '', content: '', planDate: '', leader: '' }; dialogTitle.value = '新增维保'; dialogVisible.value = true }
function handleEdit(row) { form.value = { ...row }; dialogTitle.value = '编辑维保'; dialogVisible.value = true }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.id ? updateMaintenance : addMaintenance; api(form.value).then(() => { ElMessage.success('操作成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteMaintenance(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
