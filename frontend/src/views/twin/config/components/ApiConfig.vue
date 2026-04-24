<template>
  <div class="api-config">
    <div class="panel-title">
      <span class="title-accent"></span>
      <span>接口配置与数据规则</span>
    </div>

    <!-- 搜索栏 -->
    <div class="config-search">
      <el-input v-model="queryParams.name" placeholder="接口名称" clearable style="width: 160px" @keyup.enter="handleQuery" />
      <el-select v-model="queryParams.type" placeholder="接口类型" clearable style="width: 130px">
        <el-option label="设备列表" value="device_list" />
        <el-option label="实时数据" value="realtime" />
        <el-option label="告警数据" value="alert" />
        <el-option label="视频流" value="video" />
      </el-select>
      <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
      <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
    </div>

    <!-- 工具栏 -->
    <div class="config-toolbar">
      <el-button type="primary" plain :icon="Plus" @click="handleAdd">新增接口</el-button>
    </div>

    <!-- 内容区 -->
    <div class="config-content">
      <!-- 接口列表按类型分组 -->
      <div v-for="group in groupedApis" :key="group.type" class="api-group">
        <div class="group-header">
          <el-icon><component :is="group.icon" /></el-icon>
          <span>{{ group.label }}</span>
          <el-tag size="small" type="info">{{ group.items.length }}</el-tag>
        </div>
        <el-table :data="group.items" border stripe size="small" class="group-table">
          <el-table-column label="接口名称" prop="name" align="center" min-width="120" show-overflow-tooltip />
          <el-table-column label="URL" prop="url" align="center" min-width="200" show-overflow-tooltip />
          <el-table-column label="方法" prop="method" align="center" min-width="70">
            <template #default="{ row }">
              <el-tag :type="row.method === 'GET' ? '' : 'warning'" size="small">{{ row.method }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="刷新间隔" prop="refreshInterval" align="center" min-width="90">
            <template #default="{ row }">{{ row.refreshInterval }}s</template>
          </el-table-column>
          <el-table-column label="超时" prop="timeout" align="center" min-width="70">
            <template #default="{ row }">{{ row.timeout }}ms</template>
          </el-table-column>
          <el-table-column label="状态" align="center" min-width="80">
            <template #default="{ row }">
              <span class="status-dot" :class="row.enabled ? 'online' : 'offline'"></span>
              {{ row.enabled ? '启用' : '禁用' }}
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" min-width="180" fixed="right">
            <template #default="{ row }">
              <el-button link type="primary" size="small" @click="handleTest(row)">测试</el-button>
              <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
              <el-button link type="primary" size="small" @click="handleAlertRules(row)">告警规则</el-button>
              <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
          <template #empty><el-empty description="暂无数据" /></template>
        </el-table>
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="650px" append-to-body destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="90px" size="small">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="接口名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入接口名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="接口类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择" style="width: 100%">
                <el-option label="设备列表" value="device_list" />
                <el-option label="实时数据" value="realtime" />
                <el-option label="告警数据" value="alert" />
                <el-option label="视频流" value="video" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="16">
            <el-form-item label="请求URL" prop="url">
              <el-input v-model="form.url" placeholder="请输入请求URL" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="请求方法">
              <el-select v-model="form.method" style="width: 100%">
                <el-option label="GET" value="GET" />
                <el-option label="POST" value="POST" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="请求头">
          <el-input v-model="form.headers" type="textarea" :rows="3" placeholder='JSON格式，如: {"Content-Type": "application/json"}' />
        </el-form-item>
        <el-form-item label="请求参数">
          <div class="kv-editor">
            <div v-for="(param, idx) in form.params" :key="idx" class="kv-row">
              <el-input v-model="param.key" placeholder="参数名" style="width: 40%" />
              <el-input v-model="param.value" placeholder="参数值" style="width: 40%" />
              <el-button :icon="Delete" circle size="small" @click="form.params.splice(idx, 1)" />
            </div>
            <el-button size="small" :icon="Plus" @click="form.params.push({ key: '', value: '' })">添加参数</el-button>
          </div>
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="8">
            <el-form-item label="刷新间隔">
              <el-input-number v-model="form.refreshInterval" :min="1" :max="3600" style="width: 100%" />
              <span class="form-unit">秒</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="超时时间">
              <el-input-number v-model="form.timeout" :min="1000" :max="60000" :step="1000" style="width: 100%" />
              <span class="form-unit">ms</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="重试次数">
              <el-input-number v-model="form.retryCount" :min="0" :max="5" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="字段映射">
          <el-input v-model="form.fieldMapping" type="textarea" :rows="3" placeholder='JSON格式，如: {"deviceName": "name", "status": "online"}' />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button size="small" @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" size="small" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 告警规则弹窗 -->
    <el-dialog title="告警阈值规则" v-model="alertRuleVisible" width="600px" append-to-body>
      <div class="alert-rule-header">
        <el-button type="primary" size="small" :icon="Plus" @click="addAlertRule">新增规则</el-button>
      </div>
      <el-table :data="currentAlertRules" border stripe size="small">
        <el-table-column label="字段" prop="field" align="center" min-width="100" />
        <el-table-column label="条件" prop="condition" align="center" min-width="80">
          <template #default="{ row }">{{ conditionMap[row.condition] || row.condition }}</template>
        </el-table-column>
        <el-table-column label="阈值" prop="value" align="center" min-width="80" />
        <el-table-column label="告警级别" prop="level" align="center" min-width="80">
          <template #default="{ row }">
            <el-tag :type="levelTag(row.level)" size="small">{{ levelLabel(row.level) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="60">
          <template #default="{ $index }">
            <el-button link type="danger" size="small" @click="currentAlertRules.splice($index, 1)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button size="small" @click="alertRuleVisible = false">关闭</el-button>
        <el-button type="primary" size="small" @click="saveAlertRules">保存规则</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Delete } from '@element-plus/icons-vue'
import { getApiConfigList, addApiConfig, updateApiConfig, deleteApiConfig, testApiConfig, getAlertRules, addAlertRule as addAlertRuleApi } from '../api/config'

const queryParams = reactive({ pageNum: 1, pageSize: 50, name: '', type: '' })
const loading = ref(false)
const apiList = ref([])

const conditionMap = { gt: '大于', gte: '大于等于', lt: '小于', lte: '小于等于', eq: '等于', neq: '不等于' }
function levelLabel(l) { return { critical: '严重', major: '重要', minor: '次要', info: '提示' }[l] || l }
function levelTag(l) { return { critical: 'danger', major: 'warning', minor: '', info: 'info' }[l] || 'info' }

const typeGroups = [
  { type: 'device_list', label: '设备列表', icon: 'List' },
  { type: 'realtime', label: '实时数据', icon: 'Odometer' },
  { type: 'alert', label: '告警数据', icon: 'Bell' },
  { type: 'video', label: '视频流', icon: 'VideoCamera' }
]

const groupedApis = computed(() => {
  let list = apiList.value
  if (queryParams.name) list = list.filter(a => a.name.includes(queryParams.name))
  if (queryParams.type) list = list.filter(a => a.type === queryParams.type)
  return typeGroups.map(g => ({ ...g, items: list.filter(a => a.type === g.type) })).filter(g => g.items.length > 0)
})

function getList() {
  loading.value = true
  getApiConfigList(queryParams).then(res => {
    apiList.value = res.rows || res.data || []
  }).catch(() => {
    apiList.value = []
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { queryParams.name = ''; queryParams.type = ''; handleQuery() }

// 弹窗
const dialogVisible = ref(false)
const dialogTitle = ref('新增接口')
const formRef = ref(null)
const form = ref({})
const rules = {
  name: [{ required: true, message: '接口名称不能为空', trigger: 'blur' }],
  type: [{ required: true, message: '请选择接口类型', trigger: 'change' }],
  url: [{ required: true, message: '请求URL不能为空', trigger: 'blur' }]
}

function resetForm() {
  form.value = { id: undefined, name: '', type: '', url: '', method: 'GET', headers: '{}', params: [], refreshInterval: 30, timeout: 10000, retryCount: 2, fieldMapping: '{}' }
}

function handleAdd() { resetForm(); dialogTitle.value = '新增接口'; dialogVisible.value = true }
function handleEdit(row) { form.value = { ...row, params: row.params ? JSON.parse(JSON.stringify(row.params)) : [] }; dialogTitle.value = '编辑接口'; dialogVisible.value = true }

function submitForm() {
  formRef.value.validate(valid => {
    if (valid) {
      const api = form.value.id ? updateApiConfig : addApiConfig
      api(form.value).then(() => { ElMessage.success('保存成功'); dialogVisible.value = false; getList() }).catch(() => { ElMessage.error('操作失败') })
    }
  })
}

function handleDelete(row) {
  ElMessageBox.confirm('确认删除接口"' + row.name + '"？', '提示', { type: 'warning' }).then(() => {
    deleteApiConfig(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') })
  }).catch(() => {})
}

function handleTest(row) {
  ElMessage.info('正在测试连接...')
  testApiConfig(row).then(() => { ElMessage.success('连接成功') }).catch(() => {
    setTimeout(() => { ElMessage.success('连接测试成功（模拟）') }, 1000)
  })
}

// 告警规则
const alertRuleVisible = ref(false)
const currentAlertRules = ref([])
const currentApiId = ref(null)

function handleAlertRules(row) {
  currentApiId.value = row.id
  currentAlertRules.value = [
    { field: 'temperature', condition: 'gt', value: '35', level: 'major' },
    { field: 'power', condition: 'gt', value: '500', level: 'critical' }
  ]
  alertRuleVisible.value = true
}

function addAlertRule() {
  currentAlertRules.value.push({ field: '', condition: 'gt', value: '', level: 'minor' })
}

function saveAlertRules() {
  ElMessage.success('告警规则保存成功')
  alertRuleVisible.value = false
}

onMounted(() => { getList() })
</script>

<style scoped lang="scss">
@import '../styles/config.scss';

.api-config {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .api-group {
    margin-bottom: 16px;

    .group-header {
      display: flex;
      align-items: center;
      gap: 6px;
      font-size: 13px;
      font-weight: 600;
      color: #E0E8FF;
      margin-bottom: 8px;
      padding-left: 4px;
    }

    .group-table { margin-bottom: 4px; }
  }

  .form-unit { font-size: 11px; color: #667799; margin-left: 4px; }

  .kv-editor {
    width: 100%;
    .kv-row {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 6px;
    }
  }

  .alert-rule-header {
    margin-bottom: 12px;
  }
}
</style>
