<template>
  <div class="alert-center dark-theme">
    <!-- 告警统计卡片 -->
    <el-row :gutter="16" class="stat-cards">
      <el-col :xs="24" :sm="8">
        <div class="stat-card stat-card--danger">
          <div class="stat-card__icon">&#9888;</div>
          <div>
            <div class="stat-card__label">未处理</div>
            <div class="stat-card__value">{{ statistics.pending }}</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="8">
        <div class="stat-card stat-card--warning">
          <div class="stat-card__icon">&#9203;</div>
          <div>
            <div class="stat-card__label">处理中</div>
            <div class="stat-card__value">{{ statistics.processing }}</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="8">
        <div class="stat-card stat-card--success">
          <div class="stat-card__icon">&#10003;</div>
          <div>
            <div class="stat-card__label">已处理</div>
            <div class="stat-card__value">{{ statistics.resolved }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 搜索栏 -->
    <el-form :model="queryParams" :inline="true" class="search-form">
      <el-form-item label="告警类型">
        <el-select v-model="queryParams.type" placeholder="请选择" clearable style="width:150px">
          <el-option label="设备故障" value="fault" />
          <el-option label="能耗异常" value="energy" />
          <el-option label="环境异常" value="environment" />
          <el-option label="安全告警" value="safety" />
          <el-option label="通信异常" value="communication" />
        </el-select>
      </el-form-item>
      <el-form-item label="告警等级">
        <el-select v-model="queryParams.level" placeholder="请选择" clearable style="width:120px">
          <el-option label="紧急" value="critical" />
          <el-option label="重要" value="major" />
          <el-option label="一般" value="minor" />
          <el-option label="提示" value="info" />
        </el-select>
      </el-form-item>
      <el-form-item label="处理状态">
        <el-select v-model="queryParams.status" placeholder="请选择" clearable style="width:120px">
          <el-option label="未处理" value="pending" />
          <el-option label="处理中" value="processing" />
          <el-option label="已处理" value="resolved" />
        </el-select>
      </el-form-item>
      <el-form-item label="时间范围">
        <el-date-picker
          v-model="queryParams.dateRange"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD"
          style="width:240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">搜索</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 告警统计图表 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col :span="12">
        <div class="glass-panel">
          <div class="panel-title"><span>告警趋势</span></div>
          <div class="chart-container" ref="trendChartRef"></div>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="glass-panel">
          <div class="panel-title"><span>告警类型分布</span></div>
          <div class="chart-container" ref="typeChartRef"></div>
        </div>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <div class="glass-panel">
      <el-table v-loading="loading" :data="alertList" border stripe size="small">
        <el-table-column label="告警标题" prop="title" min-width="200" show-overflow-tooltip />
        <el-table-column label="告警类型" prop="typeName" min-width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ row.typeName || getTypeName(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="等级" prop="level" min-width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="getLevelTagType(row.level)" size="small" effect="dark">
              {{ getLevelName(row.level) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="来源" prop="source" min-width="100" show-overflow-tooltip />
        <el-table-column label="位置" prop="location" min-width="120" show-overflow-tooltip />
        <el-table-column label="告警时间" prop="createTime" min-width="160" />
        <el-table-column label="处理状态" prop="status" min-width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)" size="small">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">详情</el-button>
            <el-button link type="primary" size="small" v-if="row.status !== 'resolved'" @click="handleProcess(row)">处理</el-button>
          </template>
        </el-table-column>
        <template #empty><el-empty description="暂无数据" /></template>
      </el-table>

      <!-- 分页 -->
      <div style="display:flex;justify-content:flex-end;margin-top:12px">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          background
          size="small"
          @size-change="handleQuery"
          @current-change="handleQuery"
        />
      </div>
    </div>

    <!-- 告警详情弹窗 -->
    <el-dialog v-model="detailVisible" title="告警详情" width="600px" append-to-body>
      <el-descriptions :column="2" border size="small" v-if="currentAlert">
        <el-descriptions-item label="告警标题">{{ currentAlert.title }}</el-descriptions-item>
        <el-descriptions-item label="告警类型">{{ getTypeName(currentAlert.type) }}</el-descriptions-item>
        <el-descriptions-item label="告警等级">
          <el-tag :type="getLevelTagType(currentAlert.level)" size="small" effect="dark">
            {{ getLevelName(currentAlert.level) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理状态">
          <el-tag :type="getStatusTagType(currentAlert.status)" size="small">
            {{ getStatusName(currentAlert.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="来源">{{ currentAlert.source }}</el-descriptions-item>
        <el-descriptions-item label="位置">{{ currentAlert.location }}</el-descriptions-item>
        <el-descriptions-item label="告警时间" :span="2">{{ currentAlert.createTime }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button size="small" @click="detailVisible = false">关闭</el-button>
        <el-button type="primary" size="small" v-if="currentAlert?.status !== 'resolved'" @click="handleProcess(currentAlert)">处理告警</el-button>
      </template>
    </el-dialog>

    <!-- 告警处理弹窗 -->
    <el-dialog v-model="processVisible" title="处理告警" width="500px" append-to-body>
      <el-form :model="processForm" label-width="80px" size="small">
        <el-form-item label="处理方式">
          <el-select v-model="processForm.method" placeholder="请选择" style="width:100%">
            <el-option label="已确认-无需处理" value="confirmed" />
            <el-option label="已修复" value="fixed" />
            <el-option label="转派处理" value="escalate" />
            <el-option label="临时处理" value="temporary" />
          </el-select>
        </el-form-item>
        <el-form-item label="处理说明">
          <el-input v-model="processForm.remark" type="textarea" :rows="3" placeholder="请输入处理说明" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button size="small" @click="processVisible = false">取消</el-button>
        <el-button type="primary" size="small" @click="submitProcess">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, nextTick, watch } from 'vue'
import * as echarts from 'echarts'
import { useAlertData } from '../composables/useTwinData'
import { handleTwinAlert } from '@/api/twin'
import { ElMessage } from 'element-plus'
import '@/views/twin/styles/twin.scss'

const {
  alertList, statistics, loading, queryParams, total,
  fetchAlerts, fetchStatistics,
  getTypeName, getLevelName, getStatusName, getLevelTagType, getStatusTagType
} = useAlertData()

// 图表
const trendChartRef = ref(null)
const typeChartRef = ref(null)
let charts = []

// 详情弹窗
const detailVisible = ref(false)
const currentAlert = ref(null)

// 处理弹窗
const processVisible = ref(false)
const processForm = reactive({ id: null, method: '', remark: '' })

function handleQuery() {
  fetchAlerts()
}

function resetQuery() {
  queryParams.type = ''
  queryParams.level = ''
  queryParams.status = ''
  queryParams.dateRange = null
  queryParams.pageNum = 1
  fetchAlerts()
}

function handleView(row) {
  currentAlert.value = row
  detailVisible.value = true
}

function handleProcess(row) {
  processForm.id = row.id
  processForm.method = ''
  processForm.remark = ''
  processVisible.value = true
  detailVisible.value = false
}

async function submitProcess() {
  if (!processForm.method) { ElMessage.warning('请选择处理方式'); return }
  try {
    await handleTwinAlert(processForm.id, {
      method: processForm.method,
      remark: processForm.remark
    })
    ElMessage.success('处理成功')
    processVisible.value = false
    fetchAlerts()
    fetchStatistics()
  } catch (e) {
    ElMessage.error('处理失败')
  }
}

function initCharts() {
  // 告警趋势
  if (trendChartRef.value) {
    const chart = echarts.init(trendChartRef.value)
    charts.push(chart)
    const days = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    chart.setOption({
      backgroundColor: 'transparent',
      tooltip: { trigger: 'axis', backgroundColor: 'rgba(16,32,64,0.9)', borderColor: 'rgba(64,128,255,0.3)', textStyle: { color: '#E0E8FF' } },
      grid: { left: '8%', right: '4%', top: '10%', bottom: '15%', containLabel: true },
      xAxis: { type: 'category', data: days, axisLine: { lineStyle: { color: 'rgba(64,128,255,0.2)' } }, axisLabel: { color: '#667799', fontSize: 10 } },
      yAxis: { type: 'value', axisLine: { show: false }, axisLabel: { color: '#667799', fontSize: 10 }, splitLine: { lineStyle: { color: 'rgba(64,128,255,0.06)' } } },
      series: [
        { name: '紧急', type: 'bar', stack: 'total', data: [2, 1, 3, 0, 2, 1, 0], itemStyle: { color: '#FF4560' } },
        { name: '重要', type: 'bar', stack: 'total', data: [3, 2, 4, 1, 3, 2, 1], itemStyle: { color: '#FEB019' } },
        { name: '一般', type: 'bar', stack: 'total', data: [5, 4, 6, 3, 5, 4, 3], itemStyle: { color: '#90CAF9' } },
        { name: '提示', type: 'bar', stack: 'total', data: [8, 6, 10, 5, 8, 6, 4], itemStyle: { color: '#666', borderRadius: [3, 3, 0, 0] } }
      ]
    })
  }

  // 告警类型分布
  if (typeChartRef.value) {
    const chart = echarts.init(typeChartRef.value)
    charts.push(chart)
    chart.setOption({
      backgroundColor: 'transparent',
      tooltip: { trigger: 'item', backgroundColor: 'rgba(16,32,64,0.9)', borderColor: 'rgba(64,128,255,0.3)', textStyle: { color: '#E0E8FF' } },
      series: [{
        type: 'pie', radius: ['35%', '65%'], center: ['50%', '50%'],
        data: [
          { value: 35, name: '设备故障', itemStyle: { color: '#4080FF' } },
          { value: 20, name: '能耗异常', itemStyle: { color: '#FEB019' } },
          { value: 15, name: '环境异常', itemStyle: { color: '#00D4FF' } },
          { value: 18, name: '安全告警', itemStyle: { color: '#FF4560' } },
          { value: 12, name: '通信异常', itemStyle: { color: '#775DD0' } }
        ],
        label: { color: '#8899BB', fontSize: 10 },
        labelLine: { lineStyle: { color: 'rgba(64,128,255,0.3)' } }
      }]
    })
  }
}

onMounted(() => {
  nextTick(() => initCharts())
  window.addEventListener('resize', () => charts.forEach(c => c.resize()))
})

onBeforeUnmount(() => {
  charts.forEach(c => c.dispose())
  charts = []
})
</script>

<style scoped lang="scss">
@import '../styles/twin.scss';

.alert-center {
  padding: 16px;
  background: linear-gradient(180deg, #0A1628 0%, #0D1F3C 50%, #0A1628 100%);
  min-height: 100%;

  .stat-cards {
    margin-bottom: 16px;

    .stat-card {
      border-radius: 12px;
      padding: 16px 20px;
      display: flex;
      align-items: center;
      gap: 16px;

      &--danger {
        background: linear-gradient(135deg, rgba(255, 69, 96, 0.15), rgba(255, 69, 96, 0.05));
        border: 1px solid rgba(255, 69, 96, 0.3);
      }

      &--warning {
        background: linear-gradient(135deg, rgba(254, 176, 25, 0.15), rgba(254, 176, 25, 0.05));
        border: 1px solid rgba(254, 176, 25, 0.3);
      }

      &--success {
        background: linear-gradient(135deg, rgba(0, 227, 150, 0.15), rgba(0, 227, 150, 0.05));
        border: 1px solid rgba(0, 227, 150, 0.3);
      }

      .stat-card__icon {
        font-size: 24px;
        opacity: 0.8;
      }

      .stat-card__label {
        font-size: 12px;
        color: #8899BB;
      }

      .stat-card__value {
        font-size: 28px;
        font-weight: 700;
        color: #FFFFFF;
      }
    }
  }

  .search-form {
    margin-bottom: 16px;
    padding: 12px 16px;
    border-radius: 12px;
    background: rgba(16, 32, 64, 0.4);
    border: 1px solid rgba(64, 128, 255, 0.1);

    :deep(.el-form-item__label) {
      color: #8899BB;
    }
  }
}
</style>
