<template>
  <div>
    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable style="width: 140px">
          <el-option label="操作违规" value="操作违规" />
          <el-option label="设备异常" value="设备异常" />
          <el-option label="环境异常" value="环境异常" />
          <el-option label="物料异常" value="物料异常" />
        </el-select>
      </el-form-item>
      <el-form-item label="严重程度" prop="severity">
        <el-select v-model="queryParams.severity" placeholder="请选择" clearable style="width: 120px">
          <el-option label="一般" value="一般" />
          <el-option label="较大" value="较大" />
          <el-option label="重大" value="重大" />
          <el-option label="特别重大" value="特别重大" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable style="width: 120px">
          <el-option label="待处理" :value="0" />
          <el-option label="处理中" :value="1" />
          <el-option label="已处理" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增记录</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="类型" prop="type" align="center" min-width="100">
        <template #default="{ row }"><el-tag size="small">{{ row.type }}</el-tag></template>
      </el-table-column>
      <el-table-column label="严重程度" prop="severity" align="center" min-width="100">
        <template #default="{ row }"><el-tag :type="severityTagMap[row.severity]" size="small">{{ row.severity }}</el-tag></template>
      </el-table-column>
      <el-table-column label="描述" prop="description" align="center" min-width="200" show-overflow-tooltip />
      <el-table-column label="状态" prop="status" align="center" min-width="80">
        <template #default="{ row }"><el-tag :type="statusTagMap[row.status]" size="small">{{ row.status }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="150" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleView(row)">详情</el-button>
          <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
        </template>
      </el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />

    <el-dialog title="记录详情" v-model="detailVisible" width="600px" append-to-body>
      <el-descriptions :column="2" border v-if="currentRow">
        <el-descriptions-item label="类型">{{ currentRow.type }}</el-descriptions-item>
        <el-descriptions-item label="严重程度"><el-tag :type="severityTagMap[currentRow.severity]">{{ currentRow.severity }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ currentRow.description }}</el-descriptions-item>
        <el-descriptions-item label="状态"><el-tag :type="statusTagMap[currentRow.status]">{{ currentRow.status }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="处理时间">{{ currentRow.handleTime }}</el-descriptions-item>
      </el-descriptions>
      <template #footer><el-button @click="detailVisible = false">关 闭</el-button></template>
    </el-dialog>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px" append-to-body destroy-on-close @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="类型" prop="type">
            <el-select v-model="form.type" placeholder="请选择" style="width: 100%">
              <el-option label="操作违规" value="操作违规" /><el-option label="设备异常" value="设备异常" /><el-option label="环境异常" value="环境异常" /><el-option label="物料异常" value="物料异常" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="严重程度" prop="severity">
            <el-select v-model="form.severity" placeholder="请选择" style="width: 100%">
              <el-option label="一般" value="一般" /><el-option label="较大" value="较大" /><el-option label="重大" value="重大" /><el-option label="特别重大" value="特别重大" />
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
import { ElMessage } from 'element-plus'
import { Search, Refresh, Plus, Edit } from '@element-plus/icons-vue'
import { getProductionPage, addProduction, updateProduction } from '@/api/safety'

const severityTagMap = { '一般': 'info', '较大': 'warning', '重大': 'danger', '特别重大': 'danger' }
const statusTagMap = { 0: 'warning', 1: 'primary', 2: 'success' }

const queryParams = reactive({ pageNum: 1, pageSize: 10, type: '', severity: '', status: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const queryRef = ref(null)
const detailVisible = ref(false)
const currentRow = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { type: [{ required: true, message: '请选择类型', trigger: 'change' }], severity: [{ required: true, message: '请选择严重程度', trigger: 'change' }] }

function getList() {
  loading.value = true
  getProductionPage(queryParams).then(res => { dataList.value = res.rows || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }
function handleView(row) { currentRow.value = row; detailVisible.value = true }
function resetForm() { form.value = { id: undefined, type: '', enterpriseId: undefined, areaId: undefined, description: '', severity: '', status: 0 }; if (formRef.value) formRef.value.resetFields() }
function handleAdd() { resetForm(); dialogTitle.value = '新增记录'; dialogVisible.value = true }
function handleEdit(row) { form.value = { ...row }; dialogTitle.value = '编辑记录'; dialogVisible.value = true }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.id ? updateProduction : addProduction; api(form.value).then(() => { ElMessage.success('操作成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
