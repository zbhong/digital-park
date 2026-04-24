<template>
  <div class="app-container">
    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd(null)">新增组织</el-button></el-col>
    </el-row>

    <el-table v-if="refreshTable" v-loading="loading" :data="deptList" row-key="id" :default-expand-all="isExpandAll" border>
      <el-table-column prop="label" label="组织名称" min-width="200" />
      <el-table-column prop="code" label="编码" align="center" min-width="120" />
      <el-table-column prop="leader" label="负责人" align="center" min-width="100" />
      <el-table-column prop="phone" label="联系电话" align="center" min-width="120" />
      <el-table-column prop="status" label="状态" align="center" min-width="80"><template #default="{ row }"><el-tag :type="row.status === '0' ? 'success' : 'danger'" size="small">{{ row.status === '0' ? '正常' : '停用' }}</el-tag></template></el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center" min-width="160" />
      <el-table-column label="操作" align="center" min-width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleAdd(row)">新增</el-button>
          <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" :icon="Delete" v-if="row.id !== 1" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" append-to-body destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="上级组织"><el-tree-select v-model="form.parentId" :data="deptList" :props="{ value: 'id', label: 'label', children: 'children' }" value-key="id" placeholder="选择上级组织" check-strictly filterable style="width: 100%" /></el-form-item>
        <el-form-item label="组织名称" prop="label"><el-input v-model="form.label" placeholder="请输入组织名称" /></el-form-item>
        <el-form-item label="编码" prop="code"><el-input v-model="form.code" placeholder="请输入编码" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="负责人" prop="leader"><el-input v-model="form.leader" placeholder="请输入负责人" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="联系电话" prop="phone"><el-input v-model="form.phone" placeholder="请输入联系电话" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取 消</el-button><el-button type="primary" @click="submitForm">确 定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import { listDept, addDept, updateDept, deleteDept } from '@/api/system'

const loading = ref(false)
const deptList = ref([])
const isExpandAll = ref(true)
const refreshTable = ref(true)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { label: [{ required: true, message: '组织名称不能为空', trigger: 'blur' }] }

function getList() {
  loading.value = true
  listDept({}).then(res => { deptList.value = res.data || res || [] }).catch(() => {
    deptList.value = []
  }).finally(() => { loading.value = false })
}

function handleAdd(row) { form.value = { parentId: row ? row.id : 0, label: '', code: '', leader: '', phone: '' }; dialogTitle.value = '新增组织'; dialogVisible.value = true }
function handleEdit(row) { form.value = { ...row }; dialogTitle.value = '编辑组织'; dialogVisible.value = true }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.id ? updateDept : addDept; api(form.value).then(() => { ElMessage.success('操作成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteDept(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
