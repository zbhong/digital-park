<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" class="search-form">
      <el-form-item label="用户名" prop="username"><el-input v-model="queryParams.username" placeholder="请输入用户名" clearable style="width: 200px" @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item label="手机号" prop="phone"><el-input v-model="queryParams.phone" placeholder="请输入手机号" clearable style="width: 200px" @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item label="状态" prop="status"><el-select v-model="queryParams.status" placeholder="用户状态" clearable style="width: 120px"><el-option label="正常" value="0" /><el-option label="停用" value="1" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button><el-button :icon="Refresh" @click="resetQuery">重置</el-button></el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增用户</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="userList" border stripe>
      <el-table-column label="用户名" prop="username" align="center" min-width="100" show-overflow-tooltip />
      <el-table-column label="昵称" prop="nickname" align="center" min-width="100" show-overflow-tooltip />
      <el-table-column label="部门" prop="deptName" align="center" min-width="120" show-overflow-tooltip />
      <el-table-column label="手机号" prop="phone" align="center" min-width="120" />
      <el-table-column label="状态" prop="status" align="center" min-width="80">
        <template #default="{ row }"><el-switch v-model="row.status" active-value="0" inactive-value="1" @change="handleStatusChange(row)" /></template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" align="center" min-width="160" />
      <el-table-column label="操作" align="center" min-width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="primary" :icon="Delete" @click="handleDelete(row)">删除</el-button>
          <el-button link type="primary" :icon="Key" @click="handleResetPwd(row)">重置密码</el-button>
          <el-button link type="primary" :icon="CircleCheck" @click="handleAuthRole(row)">分配角色</el-button>
        </template>
      </el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px" append-to-body destroy-on-close @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="用户名" prop="username"><el-input v-model="form.username" placeholder="请输入用户名" :disabled="form.userId !== undefined" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="昵称" prop="nickname"><el-input v-model="form.nickname" placeholder="请输入昵称" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20" v-if="form.userId === undefined">
          <el-col :span="12"><el-form-item label="密码" prop="password"><el-input v-model="form.password" placeholder="请输入密码" type="password" show-password /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="手机号" prop="phone"><el-input v-model="form.phone" placeholder="请输入手机号" maxlength="11" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="邮箱" prop="email"><el-input v-model="form.email" placeholder="请输入邮箱" maxlength="50" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="状态" prop="status"><el-radio-group v-model="form.status"><el-radio value="0">正常</el-radio><el-radio value="1">停用</el-radio></el-radio-group></el-form-item></el-col>
        </el-row>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取 消</el-button><el-button type="primary" @click="submitForm">确 定</el-button></template>
    </el-dialog>

    <el-dialog title="分配角色" v-model="authRoleVisible" width="500px" append-to-body>
      <el-form label-width="80px">
        <el-form-item label="用户名"><el-input :model-value="authRoleForm.username" disabled /></el-form-item>
        <el-form-item label="昵称"><el-input :model-value="authRoleForm.nickname" disabled /></el-form-item>
        <el-form-item label="角色">
          <el-checkbox-group v-model="authRoleForm.roleIds">
            <el-checkbox v-for="item in roleOptions" :key="item.id" :value="item.id">{{ item.roleName }}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer><el-button @click="authRoleVisible = false">取 消</el-button><el-button type="primary" @click="submitAuthRole">确 定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete, Key, CircleCheck } from '@element-plus/icons-vue'
import { listUser, getUser, addUser, updateUser, deleteUser, resetUserPwd, changeUserStatus, getRoleOptionSelect } from '@/api/system'

const queryParams = reactive({ pageNum: 1, pageSize: 10, username: '', phone: '', status: '' })
const loading = ref(false)
const userList = ref([])
const total = ref(0)
const queryRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { username: [{ required: true, message: '用户名不能为空', trigger: 'blur' }], nickname: [{ required: true, message: '昵称不能为空', trigger: 'blur' }], password: [{ required: true, message: '密码不能为空', trigger: 'blur' }, { min: 5, max: 20, message: '密码长度为5到20个字符', trigger: 'blur' }] }
const roleOptions = ref([])
const authRoleVisible = ref(false)
const authRoleForm = ref({})

function getList() {
  loading.value = true
  listUser(queryParams).then(res => { userList.value = res.rows || res.data || []; total.value = res.total || 0 }).catch(() => {
    userList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function getOptions() {
  getRoleOptionSelect().then(res => { roleOptions.value = res.data || [] }).catch(() => {
    roleOptions.value = [{ id: 1, roleName: '超级管理员' }, { id: 2, roleName: '普通管理员' }, { id: 3, roleName: '操作员' }, { id: 4, roleName: '访客' }]
  })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { if (queryRef.value) queryRef.value.resetFields(); handleQuery() }
function resetForm() { form.value = { userId: undefined, username: '', nickname: '', password: '', phone: '', email: '', status: '0' }; if (formRef.value) formRef.value.resetFields() }
function handleAdd() { resetForm(); dialogTitle.value = '新增用户'; dialogVisible.value = true }
function handleEdit(row) { resetForm(); getUser(row.userId).then(res => { form.value = res.data || res; dialogTitle.value = '编辑用户'; dialogVisible.value = true }).catch(() => { form.value = { ...row }; dialogTitle.value = '编辑用户'; dialogVisible.value = true }) }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.userId !== undefined ? updateUser : addUser; api(form.value).then(() => { ElMessage.success(form.value.userId ? '修改成功' : '新增成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除用户"' + row.username + '"？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteUser(row.userId).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }
function handleStatusChange(row) {
  if ((row.status === '1') && (row.username === 'admin' || row.userId === 1)) {
    ElMessage.warning('不能停用超级管理员账号')
    row.status = '0'
    return
  }
  const text = row.status === '0' ? '启用' : '停用'; ElMessageBox.confirm('确认要"' + text + '"用户"' + row.username + '"吗？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { changeUserStatus({ userId: row.userId, status: row.status }).then(() => { ElMessage.success(text + '成功') }).catch(() => { ElMessage.error(text + '失败') }) }).catch(() => { row.status = row.status === '0' ? '1' : '0' })
}
function handleResetPwd(row) { ElMessageBox.prompt('请输入"' + row.username + '"的新密码', '重置密码', { confirmButtonText: '确定', cancelButtonText: '取消', inputPattern: /^.{5,20}$/, inputErrorMessage: '密码长度为5到20个字符' }).then(({ value }) => { resetUserPwd({ userId: row.userId, password: value }).then(() => { ElMessage.success('密码已重置') }).catch(() => { ElMessage.error('重置失败') }) }).catch(() => {}) }
function handleAuthRole(row) { authRoleForm.value = { userId: row.userId, username: row.username, nickname: row.nickname, roleIds: [] }; authRoleVisible.value = true }
function submitAuthRole() { updateUser({ userId: authRoleForm.value.userId, roleIds: authRoleForm.value.roleIds }).then(() => { ElMessage.success('角色分配成功'); authRoleVisible.value = false; getList() }).catch(() => { ElMessage.error('角色分配失败') }) }

onMounted(() => { getList(); getOptions() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
