<template>
  <div>
    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="预案名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入预案名称" clearable style="width: 200px" @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable style="width: 140px">
          <el-option label="火灾预案" value="fire" />
          <el-option label="自然灾害" value="natural" />
          <el-option label="公共卫生" value="health" />
          <el-option label="事故灾难" value="accident" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增预案</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="预案名称" prop="name" align="center" min-width="200" show-overflow-tooltip />
      <el-table-column label="类型" prop="type" align="center" min-width="100">
        <template #default="{ row }"><el-tag size="small">{{ typeNameMap[row.type] }}</el-tag></template>
      </el-table-column>
      <el-table-column label="状态" prop="status" align="center" min-width="80">
        <template #default="{ row }"><el-tag :type="row.status === 1 ? 'success' : 'warning'" size="small">{{ row.status === 1 ? '已发布' : '草稿' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="180" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleView(row)">查看</el-button>
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
          <el-col :span="12"><el-form-item label="预案名称" prop="name"><el-input v-model="form.name" placeholder="请输入预案名称" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="类型" prop="type">
            <el-select v-model="form.type" placeholder="请选择" style="width: 100%">
              <el-option label="火灾预案" value="fire" /><el-option label="自然灾害" value="natural" />
              <el-option label="公共卫生" value="health" /><el-option label="事故灾难" value="accident" />
            </el-select>
          </el-form-item></el-col>
        </el-row>
        <el-form-item label="预案内容" prop="content"><el-input v-model="form.content" type="textarea" :rows="5" placeholder="请输入预案内容" /></el-form-item>
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
import { getPlanPage, getPlan, addPlan, updatePlan, deletePlan } from '@/api/emergency'

const typeNameMap = { fire: '火灾预案', natural: '自然灾害', health: '公共卫生', accident: '事故灾难' }

const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', type: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const queryRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { name: [{ required: true, message: '预案名称不能为空', trigger: 'blur' }], type: [{ required: true, message: '请选择类型', trigger: 'change' }] }

function getList() {
  loading.value = true
  getPlanPage(queryParams).then(res => { dataList.value = res.rows || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }
function handleView(row) { ElMessage.info('查看预案: ' + row.name) }
function resetForm() { form.value = { id: undefined, name: '', type: '', content: '', attachment: '', status: 0 }; if (formRef.value) formRef.value.resetFields() }
function handleAdd() { resetForm(); dialogTitle.value = '新增应急预案'; dialogVisible.value = true }
function handleEdit(row) { resetForm(); getPlan(row.id).then(res => { form.value = res.data || res; dialogTitle.value = '编辑应急预案'; dialogVisible.value = true }).catch(() => { form.value = { ...row }; dialogTitle.value = '编辑应急预案'; dialogVisible.value = true }) }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.id ? updatePlan : addPlan; api(form.value).then(() => { ElMessage.success('操作成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除该预案？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deletePlan(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
