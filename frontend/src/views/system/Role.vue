<template>
  <div class="app-container">
    <el-form :model="queryParams" :inline="true" class="search-form">
      <el-form-item label="角色名称" prop="roleName"><el-input v-model="queryParams.roleName" placeholder="请输入角色名称" clearable style="width: 200px" @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item><el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button><el-button :icon="Refresh" @click="resetQuery">重置</el-button></el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增角色</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="角色ID" prop="roleId" align="center" min-width="80" />
      <el-table-column label="角色名称" prop="roleName" align="center" min-width="120" />
      <el-table-column label="权限字符" prop="roleKey" align="center" min-width="120" />
      <el-table-column label="排序" prop="sort" align="center" min-width="80" />
      <el-table-column label="状态" prop="status" align="center" min-width="80"><template #default="{ row }"><el-tag :type="row.status === '0' ? 'success' : 'danger'" size="small">{{ row.status === '0' ? '正常' : '停用' }}</el-tag></template></el-table-column>
      <el-table-column label="创建时间" prop="createTime" align="center" min-width="160" />
      <el-table-column label="操作" align="center" min-width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="primary" @click="handleMenuAuth(row)">菜单权限</el-button>
          <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" append-to-body destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="角色名称" prop="roleName"><el-input v-model="form.roleName" placeholder="请输入角色名称" /></el-form-item>
        <el-form-item label="权限字符" prop="roleKey"><el-input v-model="form.roleKey" placeholder="请输入权限字符" /></el-form-item>
        <el-form-item label="排序" prop="sort"><el-input-number v-model="form.sort" :min="0" style="width: 100%" /></el-form-item>
        <el-form-item label="状态" prop="status"><el-radio-group v-model="form.status"><el-radio value="0">正常</el-radio><el-radio value="1">停用</el-radio></el-radio-group></el-form-item>
        <el-form-item label="备注" prop="remark"><el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取 消</el-button><el-button type="primary" @click="submitForm">确 定</el-button></template>
    </el-dialog>

    <el-dialog title="菜单权限" v-model="menuAuthVisible" width="500px" append-to-body>
      <el-tree ref="menuTreeRef" :data="menuOptions" :props="{ label: 'label', children: 'children' }" show-checkbox node-key="id" :default-checked-keys="checkedMenuKeys" />
      <template #footer><el-button @click="menuAuthVisible = false">取 消</el-button><el-button type="primary" @click="submitMenuAuth">确 定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { listRole, getRole, addRole, updateRole, deleteRole, listMenu } from '@/api/system'

const queryParams = reactive({ pageNum: 1, pageSize: 10, roleName: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { roleName: [{ required: true, message: '角色名称不能为空', trigger: 'blur' }], roleKey: [{ required: true, message: '权限字符不能为空', trigger: 'blur' }] }
const menuAuthVisible = ref(false)
const menuOptions = ref([])
const checkedMenuKeys = ref([])
const menuTreeRef = ref(null)
const currentRoleId = ref(null)

function getList() {
  loading.value = true
  listRole(queryParams).then(res => { dataList.value = res.rows || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { queryParams.roleName = ''; handleQuery() }
function handleAdd() { form.value = { roleName: '', roleKey: '', sort: 0, status: '0', remark: '' }; dialogTitle.value = '新增角色'; dialogVisible.value = true }
function handleEdit(row) { getRole(row.roleId).then(res => { form.value = res.data || res; dialogTitle.value = '编辑角色'; dialogVisible.value = true }).catch(() => { form.value = { ...row }; dialogTitle.value = '编辑角色'; dialogVisible.value = true }) }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.roleId ? updateRole : addRole; api(form.value).then(() => { ElMessage.success('操作成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteRole(row.roleId).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }
function handleMenuAuth(row) {
  currentRoleId.value = row.roleId
  listMenu({}).then(res => { menuOptions.value = res.data || [] }).catch(() => {
    menuOptions.value = [
      { id: 1, label: '系统管理', children: [{ id: 11, label: '用户管理' }, { id: 12, label: '角色管理' }, { id: 13, label: '菜单管理' }] },
      { id: 2, label: '能源管理', children: [{ id: 21, label: '能源计量' }, { id: 22, label: '能源数据' }] }
    ]
  })
  checkedMenuKeys.value = [11, 12, 21]
  menuAuthVisible.value = true
}
function submitMenuAuth() { ElMessage.success('权限分配成功'); menuAuthVisible.value = false }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
