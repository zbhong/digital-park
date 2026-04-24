<template>
  <div>
    <el-row :gutter="16" class="stat-cards">
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #409EFF">{{ displayStats.total }}</div><div class="stat-label">资产总数</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #67C23A">{{ displayStats.inUse }}</div><div class="stat-label">在用</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #E6A23C">{{ displayStats.idle }}</div><div class="stat-label">闲置</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover" class="stat-card"><div class="stat-value" style="color: #F56C6C">{{ displayStats.scrap }}</div><div class="stat-label">报废</div></el-card></el-col>
    </el-row>

    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="资产名称" prop="name"><el-input v-model="queryParams.name" placeholder="请输入资产名称" clearable style="width: 200px" @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item label="资产类型" prop="category"><el-select v-model="queryParams.category" placeholder="请选择" clearable style="width: 140px"><el-option label="房屋建筑" value="BLD" /><el-option label="机电设备" value="MEC" /><el-option label="办公设备" value="OFC" /></el-select></el-form-item>
      <el-form-item label="状态" prop="status"><el-select v-model="queryParams.status" placeholder="请选择" clearable style="width: 120px"><el-option label="在用" value="inUse" /><el-option label="闲置" value="idle" /><el-option label="报废" value="scrap" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button><el-button :icon="Refresh" @click="resetQuery">重置</el-button></el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增资产</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="资产名称" prop="name" align="center" min-width="150" show-overflow-tooltip />
      <el-table-column label="资产编码" prop="code" align="center" min-width="130" />
      <el-table-column label="分类" prop="categoryName" align="center" min-width="100" />
      <el-table-column label="规格型号" prop="spec" align="center" min-width="120" />
      <el-table-column label="存放位置" prop="location" align="center" min-width="120" />
      <el-table-column label="原值(万元)" prop="originalValue" align="center" min-width="100" />
      <el-table-column label="状态" prop="status" align="center" min-width="80">
        <template #default="{ row }"><el-tag :type="statusTagMap[row.status]" size="small">{{ statusNameMap[row.status] }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="150" fixed="right">
        <template #default="{ row }"><el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button><el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button></template>
      </el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px" append-to-body destroy-on-close @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="资产名称" prop="name"><el-input v-model="form.name" placeholder="请输入资产名称" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="资产编码" prop="code"><el-input v-model="form.code" placeholder="请输入资产编码" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="分类" prop="categoryName"><el-input v-model="form.categoryName" placeholder="请输入分类" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="规格型号" prop="spec"><el-input v-model="form.spec" placeholder="请输入规格型号" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="存放位置" prop="location"><el-input v-model="form.location" placeholder="请输入存放位置" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="原值(万元)" prop="originalValue"><el-input-number v-model="form.originalValue" :precision="2" style="width: 100%" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取 消</el-button><el-button type="primary" @click="submitForm">确 定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getAssetInfoPage, getAssetInfo, addAssetInfo, updateAssetInfo, deleteAssetInfo, getAssetStatisticsByStatus } from '@/api/asset'

const statusTagMap = { inUse: 'success', idle: 'warning', scrap: 'danger', '在用': 'success', '闲置': 'warning', '报废': 'danger', '维修': 'info' }
const statusNameMap = { inUse: '在用', idle: '闲置', scrap: '报废', '在用': '在用', '闲置': '闲置', '报废': '报废', '维修': '维修' }

const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', category: '', status: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const queryRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { name: [{ required: true, message: '资产名称不能为空', trigger: 'blur' }], code: [{ required: true, message: '资产编码不能为空', trigger: 'blur' }] }

// 统计数据从表格数据动态计算
const stats = computed(() => {
  const list = dataList.value
  const total = list.length
  let inUse = 0, idle = 0, scrap = 0
  list.forEach(item => {
    const s = item.status || ''
    if (s === '在用' || s === 'inUse') inUse++
    else if (s === '闲置' || s === 'idle') idle++
    else if (s === '报废' || s === 'scrap') scrap++
  })
  return { total, inUse, idle, scrap }
})

// 尝试从后端获取全量统计（覆盖分页数据）
function loadStatistics() {
  getAssetStatisticsByStatus().then(res => {
    if (res && res.statusStatistics) {
      const ss = res.statusStatistics
      statsOverride.value = {
        total: res.totalCount || 0,
        inUse: ss['在用'] || ss['inUse'] || 0,
        idle: ss['闲置'] || ss['idle'] || 0,
        scrap: ss['报废'] || ss['scrap'] || 0
      }
    }
  }).catch(() => {})
}
const statsOverride = ref(null)
const displayStats = computed(() => statsOverride.value || stats.value)

function getList() {
  loading.value = true
  getAssetInfoPage(queryParams).then(res => { dataList.value = res.rows || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }
function resetForm() { form.value = { id: undefined, name: '', code: '', categoryName: '', spec: '', location: '', originalValue: 0 }; if (formRef.value) formRef.value.resetFields() }
function handleAdd() { resetForm(); dialogTitle.value = '新增资产'; dialogVisible.value = true }
function handleEdit(row) { resetForm(); getAssetInfo(row.id).then(res => { form.value = res.data || res; dialogTitle.value = '编辑资产'; dialogVisible.value = true }).catch(() => { form.value = { ...row }; dialogTitle.value = '编辑资产'; dialogVisible.value = true }) }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.id ? updateAssetInfo : addAssetInfo; api(form.value).then(() => { ElMessage.success('操作成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败'); }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteAssetInfo(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败'); }) }).catch(() => {}) }

onMounted(() => { getList(); loadStatistics() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
.stat-cards { margin-bottom: 16px; }
.stat-card { text-align: center; padding: 10px 0; }
.stat-value { font-size: 28px; font-weight: bold; }
.stat-label { font-size: 14px; color: #909399; margin-top: 4px; }
</style>
