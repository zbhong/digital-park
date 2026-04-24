<template>
  <div>
    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新建盘点</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="盘点单号" prop="code" align="center" min-width="150" />
      <el-table-column label="盘点名称" prop="name" align="center" min-width="180" />
      <el-table-column label="盘点范围" prop="scope" align="center" min-width="120" />
      <el-table-column label="盘点人" prop="inventoryPerson" align="center" min-width="100" />
      <el-table-column label="盘点日期" prop="inventoryDate" align="center" min-width="120" />
      <el-table-column label="资产总数" prop="totalCount" align="center" min-width="80" />
      <el-table-column label="盘盈" prop="surplus" align="center" min-width="60"><template #default="{ row }"><span style="color: #67C23A">{{ row.surplus }}</span></template></el-table-column>
      <el-table-column label="盘亏" prop="loss" align="center" min-width="60"><template #default="{ row }"><span style="color: #F56C6C">{{ row.loss }}</span></template></el-table-column>
      <el-table-column label="状态" prop="status" align="center" min-width="80"><template #default="{ row }"><el-tag :type="statusTagMap[row.status]" size="small">{{ statusNameMap[row.status] }}</el-tag></template></el-table-column>
      <el-table-column label="操作" align="center" min-width="100" fixed="right"><template #default="{ row }"><el-button link type="primary" @click="handleView(row)">详情</el-button></template></el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" append-to-body destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="盘点名称" prop="name"><el-input v-model="form.name" placeholder="请输入盘点名称" /></el-form-item>
        <el-form-item label="盘点范围" prop="scope"><el-input v-model="form.scope" placeholder="请输入盘点范围" /></el-form-item>
        <el-form-item label="盘点人" prop="inventoryPerson"><el-input v-model="form.inventoryPerson" placeholder="请输入盘点人" /></el-form-item>
        <el-form-item label="盘点日期" prop="inventoryDate"><el-date-picker v-model="form.inventoryDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取 消</el-button><el-button type="primary" @click="submitForm">确 定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getInventoryPage, addInventory } from '@/api/asset'

const statusTagMap = { pending: 'warning', ongoing: 'primary', completed: 'success' }
const statusNameMap = { pending: '待盘点', ongoing: '盘点中', completed: '已完成' }

const queryParams = reactive({ pageNum: 1, pageSize: 10 })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { name: [{ required: true, message: '盘点名称不能为空', trigger: 'blur' }] }

function getList() {
  loading.value = true
  getInventoryPage(queryParams).then(res => { dataList.value = res.rows || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleAdd() { form.value = { name: '', scope: '', inventoryPerson: '', inventoryDate: '' }; dialogTitle.value = '新建盘点'; dialogVisible.value = true }
function submitForm() { formRef.value.validate(valid => { if (valid) { addInventory(form.value).then(() => { ElMessage.success('创建成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('创建失败') }) } }) }
function handleView(row) { ElMessage.info('查看盘点详情: ' + row.name) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
