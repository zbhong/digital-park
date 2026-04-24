<template>
  <div>
    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="设备名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入设备名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable style="width: 140px">
          <el-option label="光伏" value="solar" /><el-option label="风电" value="wind" />
          <el-option label="储能" value="storage" /><el-option label="配电" value="distribution" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增设备</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="设备名称" prop="name" align="center" min-width="150" show-overflow-tooltip />
      <el-table-column label="编码" prop="code" align="center" min-width="120" />
      <el-table-column label="类型" prop="type" align="center" min-width="100">
        <template #default="{ row }"><el-tag size="small">{{ typeNameMap[row.type] }}</el-tag></template>
      </el-table-column>
      <el-table-column label="额定功率" prop="ratedPower" align="center" min-width="100" />
      <el-table-column label="位置" prop="location" align="center" min-width="120" />
      <el-table-column label="状态" prop="status" align="center" min-width="80">
        <template #default="{ row }"><el-tag :type="statusTagMap[row.status]" size="small">{{ statusNameMap[row.status] }}</el-tag></template>
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
          <el-col :span="12"><el-form-item label="设备名称" prop="name"><el-input v-model="form.name" placeholder="请输入设备名称" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="编码" prop="code"><el-input v-model="form.code" placeholder="请输入编码" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="类型" prop="type">
            <el-select v-model="form.type" placeholder="请选择" style="width: 100%">
              <el-option label="光伏" value="solar" /><el-option label="风电" value="wind" />
              <el-option label="储能" value="storage" /><el-option label="配电" value="distribution" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="额定功率" prop="ratedPower"><el-input v-model="form.ratedPower" placeholder="请输入额定功率" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="位置" prop="location"><el-input v-model="form.location" placeholder="请输入安装位置" /></el-form-item>
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
import { getMeterList, getMeter, addMeter, updateMeter, deleteMeter, getSourceList, getSource, addSource, updateSource, deleteSource } from '@/api/energy'

const typeNameMap = { solar: '光伏', wind: '风电', storage: '储能', distribution: '配电' }
const statusTagMap = { running: 'success', stopped: 'info', fault: 'danger' }
const statusNameMap = { running: '运行中', stopped: '已停机', fault: '故障' }

const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', type: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const queryRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { name: [{ required: true, message: '设备名称不能为空', trigger: 'blur' }], type: [{ required: true, message: '请选择类型', trigger: 'change' }] }

function getList() {
  loading.value = true
  getSourceList(queryParams).then(res => { dataList.value = res.rows || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }
function resetForm() { form.value = { id: undefined, name: '', code: '', type: '', ratedPower: '', location: '' }; if (formRef.value) formRef.value.resetFields() }
function handleAdd() { resetForm(); dialogTitle.value = '新增源侧设备'; dialogVisible.value = true }
function handleEdit(row) { form.value = { ...row }; dialogTitle.value = '编辑源侧设备'; dialogVisible.value = true }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.id ? updateSource : addSource; api(form.value).then(() => { ElMessage.success(form.value.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteSource(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
