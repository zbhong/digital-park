<template>
  <div>
    <el-form :model="queryParams" :inline="true" class="search-form">
      <el-form-item label="文档名称" prop="name"><el-input v-model="queryParams.name" placeholder="请输入文档名称" clearable style="width: 200px" @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item label="类型" prop="type"><el-select v-model="queryParams.type" placeholder="请选择" clearable style="width: 120px"><el-option label="采购合同" value="contract" /><el-option label="验收报告" value="acceptance" /><el-option label="维保记录" value="maintenance" /><el-option label="其他" value="other" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button><el-button :icon="Refresh" @click="resetQuery">重置</el-button></el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">上传文档</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="文档名称" prop="name" align="center" min-width="200" show-overflow-tooltip />
      <el-table-column label="关联资产" prop="assetName" align="center" min-width="150" />
      <el-table-column label="类型" prop="type" align="center" min-width="100"><template #default="{ row }"><el-tag size="small">{{ typeNameMap[row.type] }}</el-tag></template></el-table-column>
      <el-table-column label="文件大小" prop="fileSize" align="center" min-width="100" />
      <el-table-column label="上传人" prop="uploader" align="center" min-width="100" />
      <el-table-column label="上传时间" prop="uploadTime" align="center" min-width="160" />
      <el-table-column label="操作" align="center" min-width="150" fixed="right">
        <template #default="{ row }"><el-button link type="primary" @click="handleDownload(row)">下载</el-button><el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button></template>
      </el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Delete } from '@element-plus/icons-vue'
import { getDocumentPage, deleteDocument } from '@/api/asset'

const typeNameMap = { contract: '采购合同', acceptance: '验收报告', maintenance: '维保记录', other: '其他' }
const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', type: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)

function getList() {
  loading.value = true
  getDocumentPage(queryParams).then(res => { dataList.value = res.rows || res.data || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { queryParams.name = ''; queryParams.type = ''; handleQuery() }
function handleAdd() { ElMessage.info('上传功能开发中') }
function handleDownload(row) { ElMessage.info('下载: ' + row.name) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteDocument(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
