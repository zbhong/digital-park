<template>
  <div>
    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleGenerate">生成报告</el-button></el-col>
      <el-col :span="4">
        <el-select v-model="queryParams.type" placeholder="报告类型" clearable @change="getList" style="width: 100%">
          <el-option label="能源分析报告" value="energy" />
          <el-option label="安全分析报告" value="safety" />
          <el-option label="经济运行报告" value="economy" />
          <el-option label="环境分析报告" value="environment" />
          <el-option label="综合分析报告" value="comprehensive" />
        </el-select>
      </el-col>
      <el-col :span="4">
        <el-select v-model="queryParams.status" placeholder="报告状态" clearable @change="getList" style="width: 100%">
          <el-option label="生成中" value="generating" />
          <el-option label="已完成" value="completed" />
          <el-option label="失败" value="failed" />
        </el-select>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="报告名称" prop="name" align="center" min-width="250" show-overflow-tooltip />
      <el-table-column label="报告类型" prop="type" align="center" min-width="100"><template #default="{ row }"><el-tag size="small">{{ typeNameMap[row.type] }}</el-tag></template></el-table-column>
      <el-table-column label="数据周期" prop="period" align="center" min-width="150" />
      <el-table-column label="生成时间" prop="createTime" align="center" min-width="160" />
      <el-table-column label="状态" prop="status" align="center" min-width="80"><template #default="{ row }"><el-tag :type="statusTagMap[row.status]" size="small">{{ statusNameMap[row.status] }}</el-tag></template></el-table-column>
      <el-table-column label="操作" align="center" min-width="180" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleView(row)" :disabled="row.status !== 'completed'">查看</el-button>
          <el-button link type="primary" @click="handleExport(row)" :disabled="row.status !== 'completed'">导出</el-button>
          <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />

    <el-dialog title="生成AI报告" v-model="generateVisible" width="500px" append-to-body destroy-on-close>
      <el-form ref="genFormRef" :model="genForm" :rules="genRules" label-width="100px">
        <el-form-item label="报告名称" prop="title"><el-input v-model="genForm.title" placeholder="请输入报告名称" /></el-form-item>
        <el-form-item label="报告类型" prop="type"><el-select v-model="genForm.type" placeholder="请选择" style="width: 100%"><el-option label="能源分析报告" value="energy" /><el-option label="安全分析报告" value="safety" /><el-option label="经济运行报告" value="economy" /><el-option label="环境分析报告" value="environment" /><el-option label="综合分析报告" value="comprehensive" /></el-select></el-form-item>
        <el-form-item label="开始日期" prop="startDate"><el-date-picker v-model="genForm.startDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" /></el-form-item>
        <el-form-item label="结束日期" prop="endDate"><el-date-picker v-model="genForm.endDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="generateVisible = false">取 消</el-button><el-button type="primary" @click="submitGenerate">生 成</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
import { getInstancePage, generateReport, deleteInstance } from '@/api/aiReport'

const typeNameMap = { energy: '能源分析', safety: '安全分析', economy: '经济运行', environment: '环境分析', comprehensive: '综合分析' }
const statusTagMap = { generating: 'warning', completed: 'success', failed: 'danger' }
const statusNameMap = { generating: '生成中', completed: '已完成', failed: '失败' }

const queryParams = reactive({ pageNum: 1, pageSize: 10 })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const generateVisible = ref(false)
const genFormRef = ref(null)
const genForm = ref({})
const genRules = { title: [{ required: true, message: '报告名称不能为空', trigger: 'blur' }], type: [{ required: true, message: '请选择报告类型', trigger: 'change' }] }

function getList() {
  loading.value = true
  getInstancePage(queryParams).then(res => { dataList.value = res.rows || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleGenerate() { genForm.value = { title: '', type: '', startDate: '', endDate: '' }; generateVisible.value = true }
function submitGenerate() { genFormRef.value.validate(valid => { if (valid) { generateReport(genForm.value).then(() => { ElMessage.success('报告生成任务已提交'); generateVisible.value = false; getList() }).catch(() => { ElMessage.error('提交失败') }) } }) }
function handleView(row) { ElMessage.info('查看报告: ' + row.name) }
function handleExport(row) { ElMessage.info('导出报告: ' + row.name) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteInstance(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
