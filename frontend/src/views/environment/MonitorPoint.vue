<template>
  <div>
    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="点位名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入点位名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="监测类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable style="width: 140px">
          <el-option label="空气质量" value="空气质量" />
          <el-option label="噪声" value="噪声" />
          <el-option label="水质" value="水质" />
          <el-option label="土壤" value="土壤" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option label="在线" :value="1" />
          <el-option label="离线" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增点位</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="点位名称" prop="name" align="center" min-width="150" show-overflow-tooltip />
      <el-table-column label="监测类型" prop="type" align="center" min-width="100">
        <template #default="{ row }"><el-tag size="small">{{ row.type }}</el-tag></template>
      </el-table-column>
      <el-table-column label="室内/室外" prop="indoorOutdoor" align="center" min-width="100">
        <template #default="{ row }"><el-tag :type="row.indoorOutdoor === '室内' ? 'primary' : 'success'" size="small">{{ row.indoorOutdoor }}</el-tag></template>
      </el-table-column>
      <el-table-column label="经度" prop="lng" align="center" min-width="120" />
      <el-table-column label="纬度" prop="lat" align="center" min-width="120" />
      <el-table-column label="状态" prop="status" align="center" min-width="80">
        <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '在线' : '离线' }}</el-tag></template>
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
          <el-col :span="12"><el-form-item label="点位名称" prop="name"><el-input v-model="form.name" placeholder="请输入点位名称" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="监测类型" prop="type">
            <el-select v-model="form.type" placeholder="请选择类型" style="width: 100%">
              <el-option label="空气质量" value="空气质量" /><el-option label="噪声" value="噪声" /><el-option label="水质" value="水质" /><el-option label="土壤" value="土壤" />
            </el-select>
          </el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="室内/室外" prop="indoorOutdoor">
            <el-select v-model="form.indoorOutdoor" placeholder="请选择" style="width: 100%">
              <el-option label="室内" value="室内" /><el-option label="室外" value="室外" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
              <el-option label="在线" :value="1" /><el-option label="离线" :value="0" />
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
import { getMonitorPointPage, getMonitorPoint, addMonitorPoint, updateMonitorPoint, deleteMonitorPoint } from '@/api/environment'

const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', type: '', status: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const queryRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { name: [{ required: true, message: '点位名称不能为空', trigger: 'blur' }], type: [{ required: true, message: '请选择监测类型', trigger: 'change' }] }

function getList() {
  loading.value = true
  getMonitorPointPage(queryParams).then(res => { dataList.value = res.rows || res.data || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }
function resetForm() { form.value = { id: undefined, name: '', type: '', areaId: undefined, buildingId: undefined, indoorOutdoor: '室外', lng: '', lat: '', status: 1 }; if (formRef.value) formRef.value.resetFields() }
function handleAdd() { resetForm(); dialogTitle.value = '新增监测点位'; dialogVisible.value = true }
function handleEdit(row) { resetForm(); getMonitorPoint(row.id).then(res => { form.value = res.data || res; dialogTitle.value = '编辑监测点位'; dialogVisible.value = true }).catch(() => { form.value = { ...row }; dialogTitle.value = '编辑监测点位'; dialogVisible.value = true }) }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.id ? updateMonitorPoint : addMonitorPoint; api(form.value).then(() => { ElMessage.success(form.value.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除该监测点位？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteMonitorPoint(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
