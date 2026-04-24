<template>
  <div class="config-log">
    <div class="panel-title">
      <span class="title-accent"></span>
      <span>权限与操作日志</span>
    </div>

    <!-- 搜索栏 -->
    <div class="config-search">
      <el-select v-model="queryParams.module" placeholder="操作模块" clearable style="width: 130px">
        <el-option label="底图管理" value="basemap" />
        <el-option label="点位编辑" value="point" />
        <el-option label="设备绑定" value="binding" />
        <el-option label="样式配置" value="style" />
        <el-option label="接口配置" value="api" />
        <el-option label="发布管理" value="publish" />
        <el-option label="系统管理" value="system" />
      </el-select>
      <el-select v-model="queryParams.action" placeholder="操作类型" clearable style="width: 120px">
        <el-option label="新增" value="add" />
        <el-option label="修改" value="update" />
        <el-option label="删除" value="delete" />
        <el-option label="发布" value="publish" />
        <el-option label="导入" value="import" />
        <el-option label="导出" value="export" />
        <el-option label="登录" value="login" />
      </el-select>
      <el-date-picker v-model="queryParams.dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"
        style="width: 240px" value-format="YYYY-MM-DD" size="default" />
      <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
      <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
    </div>

    <!-- 日志表格 -->
    <div class="config-content">
      <el-table v-loading="loading" :data="logList" border stripe size="small">
        <el-table-column label="操作时间" prop="operTime" align="center" min-width="160" />
        <el-table-column label="操作人" prop="operator" align="center" min-width="100" />
        <el-table-column label="操作模块" prop="module" align="center" min-width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ moduleMap[row.module] || row.module }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作类型" prop="action" align="center" min-width="80">
          <template #default="{ row }">
            <el-tag :type="actionTag(row.action)" size="small">{{ actionMap[row.action] || row.action }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作对象" prop="target" align="center" min-width="120" show-overflow-tooltip />
        <el-table-column label="操作详情" prop="detail" align="center" min-width="200" show-overflow-tooltip />
        <el-table-column label="IP地址" prop="ip" align="center" min-width="120" />
        <el-table-column label="结果" prop="status" align="center" min-width="70">
          <template #default="{ row }">
            <el-tag :type="row.status === 'success' ? 'success' : 'danger'" size="small">
              {{ row.status === 'success' ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <template #empty><el-empty description="暂无日志" /></template>
      </el-table>

      <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
        :page-sizes="[10, 20, 50, 100]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
        @size-change="getList" @current-change="getList" />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search, Refresh } from '@element-plus/icons-vue'
import { getConfigLogs } from '../api/config'

const queryParams = reactive({ pageNum: 1, pageSize: 10, module: '', action: '', dateRange: null })
const loading = ref(false)
const logList = ref([])
const total = ref(0)

const moduleMap = { basemap: '底图管理', point: '点位编辑', binding: '设备绑定', style: '样式配置', api: '接口配置', publish: '发布管理', system: '系统管理' }
const actionMap = { add: '新增', update: '修改', delete: '删除', publish: '发布', import: '导入', export: '导出', login: '登录', rollback: '回滚' }
function actionTag(a) {
  const map = { add: 'success', update: '', delete: 'danger', publish: 'warning', import: '', export: '', login: 'info', rollback: 'warning' }
  return map[a] || 'info'
}

function getList() {
  loading.value = true
  const params = { ...queryParams, beginDate: queryParams.dateRange?.[0], endDate: queryParams.dateRange?.[1] }
  delete params.dateRange
  getConfigLogs(params).then(res => {
    logList.value = res.rows || res.data || []
    total.value = res.total || 0
  }).catch(() => {
    logList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { queryParams.module = ''; queryParams.action = ''; queryParams.dateRange = null; handleQuery() }

onMounted(() => { getList() })
</script>

<style scoped lang="scss">
@import '../styles/config.scss';

.config-log {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
}
</style>
