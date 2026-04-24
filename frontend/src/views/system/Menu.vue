<template>
  <div class="app-container">
    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd(null)">新增菜单</el-button></el-col>
      <el-col :span="1.5"><el-button type="info" plain :icon="Sort" @click="toggleExpand">展开/折叠</el-button></el-col>
    </el-row>

    <el-table v-if="refreshTable" v-loading="loading" :data="menuList" row-key="menuId" :default-expand-all="isExpandAll" border>
      <el-table-column prop="menuName" label="菜单名称" min-width="180" />
      <el-table-column prop="icon" label="图标" align="center" min-width="80" />
      <el-table-column prop="orderNum" label="排序" align="center" min-width="80" />
      <el-table-column prop="path" label="路由地址" align="center" min-width="150" />
      <el-table-column prop="component" label="组件路径" align="center" min-width="180" />
      <el-table-column prop="perms" label="权限标识" align="center" min-width="150" />
      <el-table-column prop="status" label="状态" align="center" min-width="80"><template #default="{ row }"><el-tag :type="row.status === '0' ? 'success' : 'danger'" size="small">{{ row.status === '0' ? '正常' : '停用' }}</el-tag></template></el-table-column>
      <el-table-column label="操作" align="center" min-width="200" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="handleAdd(row)">新增</el-button>
          <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px" append-to-body destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="上级菜单"><el-tree-select v-model="form.parentId" :data="menuList" :props="{ value: 'menuId', label: 'menuName', children: 'children' }" value-key="menuId" placeholder="选择上级菜单" check-strictly filterable style="width: 100%" /></el-form-item>
        <el-form-item label="菜单类型" prop="menuType"><el-radio-group v-model="form.menuType"><el-radio value="M">目录</el-radio><el-radio value="C">菜单</el-radio><el-radio value="F">按钮</el-radio></el-radio-group></el-form-item>
        <el-form-item label="菜单名称" prop="menuName"><el-input v-model="form.menuName" placeholder="请输入菜单名称" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="路由地址" prop="path"><el-input v-model="form.path" placeholder="请输入路由地址" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="排序" prop="orderNum"><el-input-number v-model="form.orderNum" :min="0" style="width: 100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="权限标识" prop="perms"><el-input v-model="form.perms" placeholder="请输入权限标识" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取 消</el-button><el-button type="primary" @click="submitForm">确 定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Sort } from '@element-plus/icons-vue'
import { listMenu, addMenu, updateMenu, deleteMenu } from '@/api/system'

const loading = ref(false)
const menuList = ref([])
const isExpandAll = ref(true)
const refreshTable = ref(true)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { menuName: [{ required: true, message: '菜单名称不能为空', trigger: 'blur' }] }

function getList() {
  loading.value = true
  listMenu({}).then(res => { menuList.value = res.data || res || [] }).catch(() => {
    menuList.value = []
  }).finally(() => { loading.value = false })
}

function toggleExpand() { refreshTable.value = false; isExpandAll.value = !isExpandAll.value; nextTick(() => { refreshTable.value = true }) }
function handleAdd(row) { form.value = { parentId: row ? row.menuId : 0, menuType: 'C', menuName: '', path: '', orderNum: 0, perms: '' }; dialogTitle.value = '新增菜单'; dialogVisible.value = true }
function handleEdit(row) { form.value = { ...row }; dialogTitle.value = '编辑菜单'; dialogVisible.value = true }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.menuId ? updateMenu : addMenu; api(form.value).then(() => { ElMessage.success('操作成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteMenu(row.menuId).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
