<template>
  <div>
    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="房源名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入房源名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 120px">
          <el-option label="空置" value="空置" />
          <el-option label="已租" value="已租" />
          <el-option label="预留" value="预留" />
          <el-option label="维修中" value="维修中" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增房源</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="房源名称" prop="name" align="center" min-width="150" show-overflow-tooltip />
      <el-table-column label="楼层" prop="floor" align="center" min-width="80" />
      <el-table-column label="房间号" prop="roomNumber" align="center" min-width="100" />
      <el-table-column label="面积(m2)" prop="area" align="center" min-width="100" />
      <el-table-column label="单价(元/m2)" prop="pricePerSqm" align="center" min-width="120" />
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
          <el-col :span="12"><el-form-item label="房源名称" prop="name"><el-input v-model="form.name" placeholder="请输入房源名称" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="楼层" prop="floor"><el-input v-model="form.floor" placeholder="请输入楼层" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="房间号" prop="roomNumber"><el-input v-model="form.roomNumber" placeholder="请输入房间号" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="面积(m2)" prop="area"><el-input-number v-model="form.area" :min="0" :precision="2" style="width: 100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="单价(元/m2)" prop="pricePerSqm"><el-input-number v-model="form.pricePerSqm" :min="0" :precision="2" style="width: 100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
              <el-option label="空置" value="空置" /><el-option label="已租" value="已租" /><el-option label="预留" value="预留" /><el-option label="维修中" value="维修中" />
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
import { getAssetPage, getAsset, addAsset, updateAsset, deleteAsset } from '@/api/investment'

const statusTagMap = { '空置': 'success', '已租': 'primary', '预留': 'warning', '维修中': 'danger' }

const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', status: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const queryRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { name: [{ required: true, message: '房源名称不能为空', trigger: 'blur' }], roomNumber: [{ required: true, message: '房间号不能为空', trigger: 'blur' }] }

function getList() {
  loading.value = true
  getAssetPage(queryParams).then(res => { dataList.value = res.rows || res.data || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }
function resetForm() { form.value = { id: undefined, name: '', buildingId: undefined, floor: '', roomNumber: '', area: 0, pricePerSqm: 0, status: '空置', enterpriseId: undefined }; if (formRef.value) formRef.value.resetFields() }
function handleAdd() { resetForm(); dialogTitle.value = '新增房源'; dialogVisible.value = true }
function handleEdit(row) { resetForm(); getAsset(row.id).then(res => { form.value = res.data || res; dialogTitle.value = '编辑房源'; dialogVisible.value = true }).catch(() => { form.value = { ...row }; dialogTitle.value = '编辑房源'; dialogVisible.value = true }) }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.id ? updateAsset : addAsset; api(form.value).then(() => { ElMessage.success(form.value.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除该房源？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteAsset(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
