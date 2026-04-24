<template>
  <div>
    <el-form :inline="true" class="search-form">
      <el-form-item label="选择企业">
        <el-select v-model="selectedEnterpriseId" placeholder="请选择企业" clearable filterable style="width: 240px" @change="handleEnterpriseChange">
          <el-option v-for="item in enterpriseList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="行业" prop="industry">
        <el-select v-model="queryParams.industry" placeholder="请选择" clearable style="width: 140px" @change="handleQuery">
          <el-option label="信息技术" value="信息技术" />
          <el-option label="生物医药" value="生物医药" />
          <el-option label="智能制造" value="智能制造" />
          <el-option label="新能源" value="新能源" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
        <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="16" v-loading="loading">
      <el-col :span="8" v-for="item in dataList" :key="item.id" style="margin-bottom: 16px;">
        <el-card shadow="hover" class="enterprise-card">
          <div class="card-header">
            <div class="enterprise-name">{{ item.name }}</div>
            <el-tag size="small" :type="item.status === 1 ? 'success' : 'info'">{{ item.status === 1 ? '在营' : '退出' }}</el-tag>
          </div>
          <el-descriptions :column="1" size="small" :colon="false">
            <el-descriptions-item label="行业">{{ item.industry }}</el-descriptions-item>
            <el-descriptions-item label="入驻时间">{{ item.enterDate }}</el-descriptions-item>
            <el-descriptions-item label="规模">{{ item.scale }}</el-descriptions-item>
            <el-descriptions-item label="联系人">{{ item.contactPerson }}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{ item.contactPhone }}</el-descriptions-item>
          </el-descriptions>
          <div class="card-actions">
            <el-button link type="primary" size="small" @click="handleDetail(item)">查看详情</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
      :page-sizes="[9, 18, 27]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
      @size-change="getList" @current-change="getList" />

    <el-dialog title="企业详情" v-model="detailVisible" width="700px" append-to-body>
      <el-descriptions :column="2" border v-if="currentRow">
        <el-descriptions-item label="企业名称">{{ currentRow.name }}</el-descriptions-item>
        <el-descriptions-item label="信用代码">{{ currentRow.code }}</el-descriptions-item>
        <el-descriptions-item label="行业">{{ currentRow.industry }}</el-descriptions-item>
        <el-descriptions-item label="规模">{{ currentRow.scale }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ currentRow.contactPerson }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentRow.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="入驻日期">{{ currentRow.enterDate }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ currentRow.status === 1 ? '入驻' : '退出' }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ currentRow.email }}</el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">{{ currentRow.address }}</el-descriptions-item>
      </el-descriptions>
      <template #footer><el-button @click="detailVisible = false">关 闭</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search, Refresh } from '@element-plus/icons-vue'
import { getEnterpriseInfoPage, getEnterpriseInfo } from '@/api/enterprise'

const selectedEnterpriseId = ref(undefined)
const enterpriseList = ref([])
const queryParams = reactive({ pageNum: 1, pageSize: 9, name: '', industry: '' })
const loading = ref(false)
const dataList = ref([])
const total = ref(0)
const detailVisible = ref(false)
const currentRow = ref(null)

function getEnterpriseList() {
  getEnterpriseInfoPage({ pageNum: 1, pageSize: 1000, status: 1 }).then(res => {
    enterpriseList.value = res.rows || res.data || []
  }).catch(() => {
    enterpriseList.value = []
  })
}

function getList() {
  loading.value = true
  const params = { ...queryParams }
  if (selectedEnterpriseId.value) {
    params.name = ''
    // 通过企业ID筛选时，直接获取该企业详情
    getEnterpriseInfo(selectedEnterpriseId.value).then(res => {
      const data = res.data || res
      dataList.value = data ? [data] : []
      total.value = data ? 1 : 0
    }).catch(() => {
      dataList.value = []
      total.value = 0
    }).finally(() => { loading.value = false })
  } else {
    getEnterpriseInfoPage(params).then(res => {
      dataList.value = res.rows || res.data || []
      total.value = res.total || 0
    }).catch(() => {
      dataList.value = []
      total.value = 0
    }).finally(() => { loading.value = false })
  }
}

function handleEnterpriseChange() {
  queryParams.pageNum = 1
  getList()
}

function handleQuery() { queryParams.pageNum = 1; selectedEnterpriseId.value = undefined; getList() }
function resetQuery() { queryParams.name = ''; queryParams.industry = ''; queryParams.pageNum = 1; selectedEnterpriseId.value = undefined; getList() }
function handleDetail(row) {
  if (row.id) {
    getEnterpriseInfo(row.id).then(res => {
      currentRow.value = res.data || res
      detailVisible.value = true
    }).catch(() => {
      currentRow.value = row
      detailVisible.value = true
    })
  } else {
    currentRow.value = row
    detailVisible.value = true
  }
}

onMounted(() => { getEnterpriseList(); getList() })
</script>

<style lang="scss" scoped>
.app-container { padding: 16px; }
.search-form { margin-bottom: 12px; }
.toolbar { margin-bottom: 12px; }
.pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
.enterprise-card { .card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; .enterprise-name { font-size: 16px; font-weight: bold; } } .card-actions { text-align: right; margin-top: 8px; } }
</style>
