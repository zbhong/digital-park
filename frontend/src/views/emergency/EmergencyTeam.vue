<template>
  <div>
    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAdd">新增队伍</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="队伍名称" prop="name" align="center" min-width="150" />
      <el-table-column label="队伍类型" prop="type" align="center" min-width="100">
        <template #default="{ row }"><el-tag size="small">{{ typeNameMap[row.type] }}</el-tag></template>
      </el-table-column>
      <el-table-column label="队长" prop="leader" align="center" min-width="100" />
      <el-table-column label="人数" prop="memberCount" align="center" min-width="80" />
      <el-table-column label="联系电话" prop="phone" align="center" min-width="120" />
      <el-table-column label="负责区域" prop="area" align="center" min-width="120" />
      <el-table-column label="状态" prop="status" align="center" min-width="80">
        <template #default="{ row }"><el-tag :type="row.status === 'active' ? 'success' : 'info'" size="small">{{ row.status === 'active' ? '在岗' : '休息' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="150" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
          <el-button link type="danger" :icon="Delete" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px" append-to-body destroy-on-close @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="队伍名称" prop="name"><el-input v-model="form.name" placeholder="请输入队伍名称" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="队伍类型" prop="type">
            <el-select v-model="form.type" placeholder="请选择" style="width: 100%">
              <el-option label="消防队" value="fire" /><el-option label="医疗队" value="medical" />
              <el-option label="安保队" value="security" /><el-option label="技术队" value="technical" />
            </el-select>
          </el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="队长" prop="leader"><el-input v-model="form.leader" placeholder="请输入队长姓名" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="联系电话" prop="phone"><el-input v-model="form.phone" placeholder="请输入联系电话" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="负责区域" prop="area"><el-input v-model="form.area" placeholder="请输入负责区域" /></el-form-item></el-col>
        </el-row>
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
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getTeamPage, addTeam, updateTeam, deleteTeam } from '@/api/emergency'

const typeNameMap = { fire: '消防队', medical: '医疗队', security: '安保队', technical: '技术队' }
const loading = ref(false)
const dataList = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const form = ref({})
const rules = { name: [{ required: true, message: '队伍名称不能为空', trigger: 'blur' }], type: [{ required: true, message: '请选择类型', trigger: 'change' }] }

function getList() {
  loading.value = true
  getTeamPage({}).then(res => { dataList.value = res.rows || res.data || [] }).catch(() => {
    dataList.value = []
  }).finally(() => { loading.value = false })
}

function resetForm() { form.value = { id: undefined, name: '', type: '', leader: '', phone: '', area: '' }; if (formRef.value) formRef.value.resetFields() }
function handleAdd() { resetForm(); dialogTitle.value = '新增应急队伍'; dialogVisible.value = true }
function handleEdit(row) { form.value = { ...row }; dialogTitle.value = '编辑应急队伍'; dialogVisible.value = true }
function submitForm() { formRef.value.validate(valid => { if (valid) { const api = form.value.id ? updateTeam : addTeam; api(form.value).then(() => { ElMessage.success(form.value.id ? '修改成功' : '新增成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDelete(row) { ElMessageBox.confirm('是否确认删除该队伍？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteTeam(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
