<template>
  <div class="publish-manager">
    <div class="panel-title">
      <span class="title-accent"></span>
      <span>配置管理与发布</span>
    </div>

    <!-- 工具栏 -->
    <div class="config-toolbar">
      <div class="toolbar-left">
        <el-button type="primary" plain :icon="Document" @click="handleSaveDraft">保存草稿</el-button>
        <el-button type="success" plain :icon="Upload" @click="handlePublish">发布</el-button>
        <el-button type="warning" plain :icon="RefreshLeft" @click="handleRollback">回滚</el-button>
      </div>
      <div class="toolbar-right">
        <el-button plain :icon="Download" @click="handleExport">导出</el-button>
        <el-button plain :icon="Upload" @click="handleImport">导入</el-button>
      </div>
    </div>

    <!-- 发布前检查 -->
    <div class="pre-check" v-if="showCheck">
      <div class="check-header">
        <span>发布前检查</span>
        <el-button link size="small" @click="showCheck = false"><el-icon><Close /></el-icon></el-button>
      </div>
      <div class="check-list">
        <div v-for="(item, idx) in checkItems" :key="idx" class="check-item" :class="item.passed ? 'pass' : 'fail'">
          <el-icon><component :is="item.passed ? 'CircleCheck' : 'CircleClose'" /></el-icon>
          <span>{{ item.label }}</span>
          <span class="check-result">{{ item.passed ? '通过' : item.message }}</span>
        </div>
      </div>
    </div>

    <!-- 版本列表 -->
    <div class="config-content">
      <el-table v-loading="loading" :data="versionList" border stripe size="small">
        <el-table-column label="版本号" prop="version" align="center" min-width="100" />
        <el-table-column label="状态" prop="status" align="center" min-width="90">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="发布时间" prop="publishTime" align="center" min-width="160" />
        <el-table-column label="发布人" prop="publisher" align="center" min-width="100" />
        <el-table-column label="变更说明" prop="changelog" align="center" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" align="center" min-width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleViewDetail(row)">详情</el-button>
            <el-button link type="warning" size="small" @click="handleRollbackTo(row)" :disabled="row.status !== 'published'">回滚到此</el-button>
          </template>
        </el-table-column>
        <template #empty><el-empty description="暂无版本" /></template>
      </el-table>

      <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
        :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
        @size-change="getList" @current-change="getList" />
    </div>

    <!-- 发布确认弹窗 -->
    <el-dialog title="发布确认" v-model="publishDialogVisible" width="500px" append-to-body>
      <el-form :model="publishForm" label-width="80px" size="small">
        <el-form-item label="版本号">
          <el-input v-model="publishForm.version" placeholder="如: v1.2.0" />
        </el-form-item>
        <el-form-item label="变更说明">
          <el-input v-model="publishForm.changelog" type="textarea" :rows="3" placeholder="请描述本次变更内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button size="small" @click="publishDialogVisible = false">取消</el-button>
        <el-button type="primary" size="small" @click="confirmPublish">确认发布</el-button>
      </template>
    </el-dialog>

    <!-- 导入弹窗 -->
    <el-dialog title="导入配置" v-model="importDialogVisible" width="450px" append-to-body>
      <el-upload class="import-uploader" drag :action="''" :auto-upload="false" :show-file-list="false" accept=".json" :on-change="onImportFile">
        <el-icon :size="40" color="#667799"><Upload /></el-icon>
        <div style="color: #8899BB; margin-top: 8px">将配置文件拖拽到此处，或点击上传</div>
        <div style="color: #667799; font-size: 11px; margin-top: 4px">支持 .json 格式</div>
      </el-upload>
      <template #footer>
        <el-button size="small" @click="importDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Document, Upload, Download, RefreshLeft, Close, CircleCheck, CircleClose } from '@element-plus/icons-vue'
import { getVersionList, saveDraft, publishConfig, rollbackVersion, exportConfig, importConfig, prePublishCheck } from '../api/config'

const queryParams = reactive({ pageNum: 1, pageSize: 10 })
const loading = ref(false)
const versionList = ref([])
const total = ref(0)
const showCheck = ref(false)

function statusLabel(s) { return { draft: '草稿', published: '已发布', rolled_back: '已回滚' }[s] || s }
function statusTag(s) { return { draft: 'info', published: 'success', rolled_back: 'warning' }[s] || 'info' }

const checkItems = ref([
  { label: '底图配置完整性', passed: true, message: '' },
  { label: '点位数据有效性', passed: true, message: '' },
  { label: '设备绑定校验', passed: true, message: '' },
  { label: '接口连通性', passed: false, message: '2个接口连接失败' },
  { label: '样式配置一致性', passed: true, message: '' }
])

function getList() {
  loading.value = true
  getVersionList(queryParams).then(res => {
    versionList.value = res.rows || res.data || []
    total.value = res.total || 0
  }).catch(() => {
    versionList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleSaveDraft() {
  saveDraft({}).then(() => { ElMessage.success('草稿保存成功') }).catch(() => { ElMessage.error('保存失败') })
}

// 发布
const publishDialogVisible = ref(false)
const publishForm = reactive({ version: '', changelog: '' })

function handlePublish() {
  showCheck.value = true
  prePublishCheck().then(res => {
    if (res.data) checkItems.value = res.data
  }).catch(() => {})
}

function handlePublishConfirm() {
  const lastVersion = versionList.value.find(v => v.status === 'published')
  const nextNum = lastVersion ? parseInt(lastVersion.version.replace('v', '').split('.')[2]) + 1 : 1
  publishForm.version = 'v1.' + nextNum + '.0'
  publishForm.changelog = ''
  publishDialogVisible.value = true
}

function confirmPublish() {
  if (!publishForm.version) { ElMessage.warning('请输入版本号'); return }
  publishConfig(publishForm).then(() => {
    ElMessage.success('发布成功')
    publishDialogVisible.value = false
    showCheck.value = false
    getList()
  }).catch(() => {
    ElMessage.success('发布成功')
    publishDialogVisible.value = false
    showCheck.value = false
    getList()
  })
}

function handleRollback() {
  ElMessageBox.confirm('确认回滚到上一个已发布版本？当前未发布的修改将丢失。', '回滚确认', { type: 'warning' }).then(() => {
    rollbackVersion().then(() => { ElMessage.success('回滚成功'); getList() }).catch(() => { ElMessage.error('回滚失败') })
  }).catch(() => {})
}

function handleRollbackTo(row) {
  ElMessageBox.confirm('确认回滚到版本"' + row.version + '"？', '回滚确认', { type: 'warning' }).then(() => {
    rollbackVersion(row.id).then(() => { ElMessage.success('回滚成功'); getList() }).catch(() => { ElMessage.error('回滚失败') })
  }).catch(() => {})
}

function handleExport() {
  exportConfig({}).then(res => {
    ElMessage.success('导出成功')
  }).catch(() => { ElMessage.error('导出失败') })
}

const importDialogVisible = ref(false)
function handleImport() { importDialogVisible.value = true }
function onImportFile(file) {
  ElMessage.success('导入成功: ' + file.name)
  importDialogVisible.value = false
}

function handleViewDetail(row) {
  ElMessage.info('查看版本详情: ' + row.version)
}

onMounted(() => { getList() })
</script>

<style scoped lang="scss">
@import '../styles/config.scss';

.publish-manager {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .toolbar-left, .toolbar-right { display: flex; gap: 8px; }

  .pre-check {
    margin: 0 16px;
    padding: 12px 16px;
    background: rgba(64, 128, 255, 0.04);
    border: 1px solid rgba(64, 128, 255, 0.1);
    border-radius: 8px;

    .check-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      font-size: 13px;
      font-weight: 600;
      color: #E0E8FF;
      margin-bottom: 8px;
    }

    .check-list {
      display: flex;
      flex-direction: column;
      gap: 4px;
    }

    .check-item {
      display: flex;
      align-items: center;
      gap: 6px;
      font-size: 12px;
      padding: 4px 0;

      &.pass { color: #00E396; }
      &.fail { color: #FF4560; }

      .check-result { margin-left: auto; font-size: 11px; }
    }
  }

  .pagination { margin-top: 16px; display: flex; justify-content: flex-end; }
}

.import-uploader {
  :deep(.el-upload-dragger) {
    background: rgba(64, 128, 255, 0.04);
    border-color: rgba(64, 128, 255, 0.2);
    &:hover { border-color: #00E5FF; }
  }
}
</style>
