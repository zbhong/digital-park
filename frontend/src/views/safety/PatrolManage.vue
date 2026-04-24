<template>
  <div>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="巡检路线" name="route">
        <el-row :gutter="10" class="toolbar">
          <el-col :span="1.5"><el-button type="primary" plain :icon="Plus" @click="handleAddRoute">新增路线</el-button></el-col>
        </el-row>
        <el-table v-loading="loading" :data="routeList" border stripe>
          <el-table-column label="路线名称" prop="name" align="center" min-width="150" />
          <el-table-column label="巡检区域" prop="area" align="center" min-width="120" />
          <el-table-column label="点位数量" prop="pointCount" align="center" min-width="80" />
          <el-table-column label="巡检周期" prop="cycle" align="center" min-width="100" />
          <el-table-column label="负责人" prop="leader" align="center" min-width="100" />
          <el-table-column label="状态" prop="status" align="center" min-width="80">
            <template #default="{ row }"><el-tag :type="row.status === 'active' ? 'success' : 'info'" size="small">{{ row.status === 'active' ? '启用' : '停用' }}</el-tag></template>
          </el-table-column>
          <el-table-column label="操作" align="center" min-width="150" fixed="right">
            <template #default="{ row }">
              <el-button link type="primary" :icon="Edit" @click="handleEditRoute(row)">编辑</el-button>
              <el-button link type="danger" :icon="Delete" @click="handleDeleteRoute(row)">删除</el-button>
            </template>
          </el-table-column>
          <template #empty><el-empty description="暂无数据" /></template>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="巡检记录" name="record">
        <el-form :model="recordQuery" :inline="true" class="search-form">
          <el-form-item label="巡检路线" prop="routeName">
            <el-input v-model="recordQuery.routeName" placeholder="请输入路线名称" clearable style="width: 200px" @keyup.enter="handleRecordQuery" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="recordQuery.status" placeholder="请选择状态" clearable style="width: 120px">
              <el-option label="正常" value="normal" />
              <el-option label="异常" value="abnormal" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="handleRecordQuery">搜索</el-button>
            <el-button :icon="Refresh" @click="resetRecordQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <el-table v-loading="recordLoading" :data="recordList" border stripe>
          <el-table-column label="巡检路线" prop="routeName" align="center" min-width="150" />
          <el-table-column label="巡检人" prop="patrolPerson" align="center" min-width="100" />
          <el-table-column label="开始时间" prop="startTime" align="center" min-width="160" />
          <el-table-column label="结束时间" prop="endTime" align="center" min-width="160" />
          <el-table-column label="巡检点位" prop="pointCount" align="center" min-width="80" />
          <el-table-column label="异常数" prop="abnormalCount" align="center" min-width="80" />
          <el-table-column label="状态" prop="status" align="center" min-width="80">
            <template #default="{ row }"><el-tag :type="row.status === 'normal' ? 'success' : 'danger'" size="small">{{ row.status === 'normal' ? '正常' : '异常' }}</el-tag></template>
          </el-table-column>
          <template #empty><el-empty description="暂无数据" /></template>
        </el-table>
        <el-pagination v-show="recordTotal > 0" v-model:current-page="recordQuery.pageNum" v-model:page-size="recordQuery.pageSize"
          :page-sizes="[10, 20, 50]" :total="recordTotal" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
          @size-change="getRecordList" @current-change="getRecordList" />
      </el-tab-pane>
    </el-tabs>

    <el-dialog :title="routeDialogTitle" v-model="routeDialogVisible" width="500px" append-to-body destroy-on-close>
      <el-form ref="routeFormRef" :model="routeForm" :rules="routeRules" label-width="100px">
        <el-form-item label="路线名称" prop="name"><el-input v-model="routeForm.name" placeholder="请输入路线名称" /></el-form-item>
        <el-form-item label="巡检区域" prop="area"><el-input v-model="routeForm.area" placeholder="请输入巡检区域" /></el-form-item>
        <el-form-item label="巡检周期" prop="cycle">
          <el-select v-model="routeForm.cycle" placeholder="请选择" style="width: 100%">
            <el-option label="每日" value="每日" /><el-option label="每周" value="每周" /><el-option label="每月" value="每月" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="leader"><el-input v-model="routeForm.leader" placeholder="请输入负责人" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="routeDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitRouteForm">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { getPatrolRoutePage, addPatrolRoute, updatePatrolRoute, deletePatrolRoute } from '@/api/safety'

const activeTab = ref('route')
const loading = ref(false)
const routeList = ref([])
const routeDialogVisible = ref(false)
const routeDialogTitle = ref('')
const routeFormRef = ref(null)
const routeForm = ref({})
const routeRules = { name: [{ required: true, message: '路线名称不能为空', trigger: 'blur' }], area: [{ required: true, message: '巡检区域不能为空', trigger: 'blur' }] }

const recordQuery = reactive({ pageNum: 1, pageSize: 10, routeName: '', status: '' })
const recordLoading = ref(false)
const recordList = ref([])
const recordTotal = ref(0)

function getRouteList() {
  loading.value = true
  getPatrolRoutePage({ pageNum: 1, pageSize: 100 }).then(res => { routeList.value = res.rows || res.data || [] }).catch(() => {
    routeList.value = []
  }).finally(() => { loading.value = false })
}

function getRecordList() {
  recordLoading.value = true
  setTimeout(() => {
    recordList.value = [
      { id: 1, routeName: '园区外围巡检路线', patrolPerson: '张三', startTime: '2026-04-20 08:00:00', endTime: '2026-04-20 09:30:00', pointCount: 12, abnormalCount: 0, status: 'normal' },
      { id: 2, routeName: 'A栋内部巡检路线', patrolPerson: '李四', startTime: '2026-04-20 08:30:00', endTime: '2026-04-20 09:45:00', pointCount: 8, abnormalCount: 1, status: 'abnormal' },
      { id: 3, routeName: '停车场巡检路线', patrolPerson: '王五', startTime: '2026-04-19 14:00:00', endTime: '2026-04-19 15:00:00', pointCount: 6, abnormalCount: 0, status: 'normal' }
    ]
    recordTotal.value = 3
    recordLoading.value = false
  }, 300)
}

function handleAddRoute() { routeForm.value = { name: '', area: '', cycle: '', leader: '' }; routeDialogTitle.value = '新增巡检路线'; routeDialogVisible.value = true }
function handleEditRoute(row) { routeForm.value = { ...row }; routeDialogTitle.value = '编辑巡检路线'; routeDialogVisible.value = true }
function submitRouteForm() { routeFormRef.value.validate(valid => { if (valid) { const api = routeForm.value.id ? updatePatrolRoute : addPatrolRoute; api(routeForm.value).then(() => { ElMessage.success(routeForm.value.id ? '修改成功' : '新增成功'); routeDialogVisible.value = false; getRouteList() }).catch(() => { ElMessage.error('操作失败') }) } }) }
function handleDeleteRoute(row) { ElMessageBox.confirm('是否确认删除该巡检路线？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { deletePatrolRoute(row.id).then(() => { ElMessage.success('删除成功'); getRouteList() }).catch(() => { ElMessage.error('删除失败') }) }).catch(() => {}) }
function handleRecordQuery() { recordQuery.pageNum = 1; getRecordList() }
function resetRecordQuery() { recordQuery.routeName = ''; recordQuery.status = ''; handleRecordQuery() }

onMounted(() => { getRouteList(); getRecordList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
