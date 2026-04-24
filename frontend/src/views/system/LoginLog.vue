<template>
  <div class="app-container">
    <el-form :model="queryParams" :inline="true" class="search-form">
      <el-form-item label="登录账号" prop="username"><el-input v-model="queryParams.username" placeholder="请输入登录账号" clearable style="width: 200px" @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item label="IP地址" prop="ipaddr"><el-input v-model="queryParams.ipaddr" placeholder="请输入IP地址" clearable style="width: 200px" @keyup.enter="handleQuery" /></el-form-item>
      <el-form-item label="状态" prop="status"><el-select v-model="queryParams.status" placeholder="请选择" clearable style="width: 120px"><el-option label="成功" value="0" /><el-option label="失败" value="1" /></el-select></el-form-item>
      <el-form-item><el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button><el-button :icon="Refresh" @click="resetQuery">重置</el-button></el-form-item>
    </el-form>

    <el-row :gutter="10" class="toolbar">
      <el-col :span="1.5"><el-button type="danger" plain :icon="Delete" @click="handleClean">清空</el-button></el-col>
      <el-col :span="1.5"><el-button type="primary" plain :icon="Unlock" @click="handleUnlock">解锁</el-button></el-col>
    </el-row>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column label="日志ID" prop="infoId" align="center" min-width="80" />
      <el-table-column label="登录账号" prop="username" align="center" min-width="120" />
      <el-table-column label="登录IP" prop="ipaddr" align="center" min-width="130" />
      <el-table-column label="登录地点" prop="loginLocation" align="center" min-width="150" />
      <el-table-column label="浏览器" prop="browser" align="center" min-width="120" />
      <el-table-column label="操作系统" prop="os" align="center" min-width="120" />
      <el-table-column label="登录时间" prop="loginTime" align="center" min-width="160" />
      <el-table-column label="状态" prop="status" align="center" min-width="80"><template #default="{ row }"><el-tag :type="row.status === '0' ? 'success' : 'danger'" size="small">{{ row.status === '0' ? '成功' : '失败' }}</el-tag></template></el-table-column>
      <el-table-column label="提示信息" prop="msg" align="center" min-width="150" show-overflow-tooltip />
      <template #empty><el-empty description="暂无数据" /></template>
    </el-table>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Delete, Unlock } from '@element-plus/icons-vue'
import { listLoginLog, cleanLoginLog, unlockLoginUser } from '@/api/system'

const queryParams = reactive({ pageNum: 1, pageSize: 10, username: '', ipaddr: '', status: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)

function getList() {
  loading.value = true
  listLoginLog(queryParams).then(res => { dataList.value = res.rows || []; total.value = res.total || 0 }).catch(() => {
    dataList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { queryParams.username = ''; queryParams.ipaddr = ''; queryParams.status = ''; handleQuery() }
function handleClean() { ElMessageBox.confirm('是否确认清空所有登录日志？', '系统提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(() => { cleanLoginLog().then(() => { ElMessage.success('清空成功'); getList() }).catch(() => { ElMessage.error('清空失败') }) }).catch(() => {}) }
function handleUnlock() { ElMessageBox.prompt('请输入要解锁的用户名', '解锁账号', { confirmButtonText: '确定', cancelButtonText: '取消' }).then(({ value }) => { unlockLoginUser(value).then(() => { ElMessage.success('解锁成功') }).catch(() => { ElMessage.error('解锁失败') }) }).catch(() => {}) }

onMounted(() => { getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
