<template>
  <div>
    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd(null)">新增分类</el-button></el-col>
      <el-col :span="1.5"><el-button type="info" plain :icon="Sort" @click="toggleExpandAll">展开/折叠</el-button></el-col>
    </el-row>

    <el-table v-if="refreshTable" v-loading="loading" :data="categoryList" row-key="id" :default-expand-all="isExpandAll" border>
      <el-table-column prop="name" label="分类名称" min-width="200" />
      <el-table-column prop="code" label="分类编码" align="center" min-width="150" />
      <el-table-column prop="assetCount" label="资产数量" align="center" min-width="100" />
      <el-table-column prop="status" label="状态" align="center" min-width="80">
        <template #default="{ row }"><el-tag :type="row.status === '0' ? 'success' : 'danger'" size="small">{{ row.status === '0' ? '正常' : '停用' }}</el-tag></template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center" min-width="160" />
      <el-table-column label="操作" align="center" min-width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleAdd(row)">新增</el-button>
          <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" append-to-body destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="上级分类"><el-tree-select v-model="form.parentId" :data="categoryList" :props="{ value: 'id', label: 'name', children: 'children' }" value-key="id" placeholder="选择上级分类" check-strictly style="width: 100%" /></el-form-item>
        <el-form-item label="分类名称" prop="name"><el-input v-model="form.name" placeholder="请输入分类名称" /></el-form-item>
        <el-form-item label="分类编码" prop="code"><el-input v-model="form.code" placeholder="请输入分类编码" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Sort } from '@element-plus/icons-vue'
import { getCategoryTree, addCategory, updateCategory, deleteCategory } from '@/api/asset'

const loading = ref(false)
const categoryList = ref([])
const isExpandAll = ref(true)
const refreshTable = ref(true)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { name: [{ required: true, message: '分类名称不能为空', trigger: 'blur' }], code: [{ required: true, message: '分类编码不能为空', trigger: 'blur' }] }

function getList() {
  loading.value = true
  getCategoryTree().then(res => { categoryList.value = res.data || res || [] }).catch(() => {
    categoryList.value = []
  }).finally(() => { loading.value = false })
}

function toggleExpandAll() { refreshTable.value = false; isExpandAll.value = !isExpandAll.value; nextTick(() => { refreshTable.value = true }) }
function handleAdd(row) { form.value = { id: undefined, parentId: row ? row.id : 0, name: '', code: '' }; dialogTitle.value = '新增分类'; dialogVisible.value = true }
function handleEdit(row) { form.value = { ...row }; dialogTitle.value = '编辑分类'; dialogVisible.value = true }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.id ? updateCategory : addCategory; api(form.value).then(() => { ElMessage.success(form.value.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除该分类？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteCategory(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

import { nextTick } from 'vue'
onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
