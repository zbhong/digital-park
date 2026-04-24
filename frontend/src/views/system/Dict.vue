<template>
  <div class="app-container">
    <el-row :gutter="16">
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header><span>字典类型</span></template>
          <el-input v-model="dictTypeQuery" placeholder="搜索字典类型" clearable style="margin-bottom: 12px;" />
          <el-table :data="filteredDictTypes" border stripe highlight-current-row @current-change="handleDictTypeSelect" max-height="500">
            <el-table-column prop="dictName" label="字典名称" min-width="120" />
            <el-table-column prop="dictType" label="字典类型" min-width="120" />
            <el-table-column label="操作" align="center" min-width="120" fixed="right">
              <template #default="{ row }"><el-button link type="primary" @click="handleEditType(row)">编辑</el-button></template>
            </el-table-column>
            <template #empty><el-empty description="暂无数据" /></template>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header><span>字典数据 - {{ currentDictType || '请选择字典类型' }}</span></template>
          <el-row :gutter="10" style="margin-bottom: 12px;">
            <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAddData">新增</el-button></el-col>
          </el-row>
          <el-table :data="dictDataList" border stripe>
            <el-table-column prop="dictLabel" label="字典标签" align="center" min-width="120" />
            <el-table-column prop="dictValue" label="字典键值" align="center" min-width="100" />
            <el-table-column prop="sort" label="排序" align="center" min-width="80" />
            <el-table-column prop="status" label="状态" align="center" min-width="80"><template #default="{ row }"><el-tag :type="row.status === '0' ? 'success' : 'danger'" size="small">{{ row.status === '0' ? '正常' : '停用' }}</el-tag></template></el-table-column>
            <el-table-column label="操作" align="center" min-width="150" fixed="right"><template #default="{ row }"><el-button link type="primary" :icon="Edit" @click="handleEditData(row)">编辑</el-button><el-button link type="danger" :icon="Delete" @click="handleDeleteData(row)">删除</el-button></template></el-table-column>
            <template #empty><el-empty description="暂无数据" /></template>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog :title="dictDialogTitle" v-model="dictDialogVisible" width="400px" append-to-body destroy-on-close>
      <el-form ref="dictFormRef" :model="dictForm" :rules="dictRules" label-width="100px">
        <el-form-item label="字典名称" prop="dictName"><el-input v-model="dictForm.dictName" placeholder="请输入字典名称" /></el-form-item>
        <el-form-item label="字典类型" prop="dictType"><el-input v-model="dictForm.dictType" placeholder="请输入字典类型" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dictDialogVisible = false">取 消</el-button><el-button type="primary" @click="submitDictType">确 定</el-button></template>
    </el-dialog>

    <el-dialog :title="dataDialogTitle" v-model="dataDialogVisible" width="400px" append-to-body destroy-on-close>
      <el-form ref="dataFormRef" :model="dataForm" :rules="dataRules" label-width="100px">
        <el-form-item label="字典标签" prop="dictLabel"><el-input v-model="dataForm.dictLabel" placeholder="请输入字典标签" /></el-form-item>
        <el-form-item label="字典键值" prop="dictValue"><el-input v-model="dataForm.dictValue" placeholder="请输入字典键值" /></el-form-item>
        <el-form-item label="排序" prop="sort"><el-input-number v-model="dataForm.sort" :min="0" style="width: 100%" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dataDialogVisible = false">取 消</el-button><el-button type="primary" @click="submitDictData">确 定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import { listDictType, addDictType, updateDictType, listDictData, addDictData, updateDictData, deleteDictData } from '@/api/system'

const dictTypeQuery = ref('')
const dictTypeList = ref([])
const currentDictType = ref('')
const dictDataList = ref([])

const dictDialogVisible = ref(false)
const dictDialogTitle = ref('')
const dictFormRef = ref(null)
const dictForm = ref({})
const dictRules = { dictName: [{ required: true, message: '字典名称不能为空', trigger: 'blur' }], dictType: [{ required: true, message: '字典类型不能为空', trigger: 'blur' }] }

const dataDialogVisible = ref(false)
const dataDialogTitle = ref('')
const dataFormRef = ref(null)
const dataForm = ref({})
const dataRules = { dictLabel: [{ required: true, message: '字典标签不能为空', trigger: 'blur' }], dictValue: [{ required: true, message: '字典键值不能为空', trigger: 'blur' }] }

const filteredDictTypes = computed(() => {
  if (!dictTypeQuery.value) return dictTypeList.value
  return dictTypeList.value.filter(item => item.dictName.includes(dictTypeQuery.value) || item.dictType.includes(dictTypeQuery.value))
})

function getDictTypeList() {
  listDictType({ pageNum: 1, pageSize: 100 }).then(res => { dictTypeList.value = res.rows || [] }).catch(() => {
    dictTypeList.value = [
      { id: 1, dictName: '用户状态', dictType: 'sys_user_status' },
      { id: 2, dictName: '设备状态', dictType: 'device_status' },
      { id: 3, dictName: '告警等级', dictType: 'alarm_level' },
      { id: 4, dictName: '工单状态', dictType: 'work_order_status' },
      { id: 5, dictName: '费用类型', dictType: 'fee_type' }
    ]
  })
}

function handleDictTypeSelect(row) {
  currentDictType.value = row.dictType
  listDictData({ dictType: row.dictType }).then(res => { dictDataList.value = res.rows || [] }).catch(() => {
    const mockData = { sys_user_status: [{ dictLabel: '正常', dictValue: '0', sort: 1, status: '0' }, { dictLabel: '停用', dictValue: '1', sort: 2, status: '0' }], device_status: [{ dictLabel: '在线', dictValue: 'online', sort: 1, status: '0' }, { dictLabel: '离线', dictValue: 'offline', sort: 2, status: '0' }, { dictLabel: '故障', dictValue: 'fault', sort: 3, status: '0' }] }
    dictDataList.value = mockData[row.dictType] || []
  })
}

function handleEditType(row) { dictForm.value = { ...row }; dictDialogTitle.value = '编辑字典类型'; dictDialogVisible.value = true }
function submitDictType() { dictFormRef.value.validate(valid => { if (valid) { const api = dictForm.value.id ? updateDictType : addDictType; api(dictForm.value).then(() => { ElMessage.success('操作成功'); dictDialogVisible.value = false; getDictTypeList() }).catch(() => { ElMessage.error('操作失败') }) } }) }

function handleAddData() { dataForm.value = { dictType: currentDictType.value, dictLabel: '', dictValue: '', sort: 0 }; dataDialogTitle.value = '新增字典数据'; dataDialogVisible.value = true }
function handleEditData(row) { dataForm.value = { ...row }; dataDialogTitle.value = '编辑字典数据'; dataDialogVisible.value = true }
function submitDictData() { dataFormRef.value.validate(valid => { if (valid) { const api = dataForm.value.id ? updateDictData : addDictData; api(dataForm.value).then(() => { ElMessage.success('操作成功'); dataDialogVisible.value = false; handleDictTypeSelect({ dictType: currentDictType.value }) }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDeleteData(row) { ElMessageBox.confirm('是否确认删除？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deleteDictData(row.id).then(() => { ElMessage.success('删除成功'); handleDictTypeSelect({ dictType: currentDictType.value }) }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }

onMounted(() => { getDictTypeList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
