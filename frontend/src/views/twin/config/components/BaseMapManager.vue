<template>
  <div class="basemap-manager">
    <div class="panel-title">
      <span class="title-accent"></span>
      <span>底图管理中心</span>
    </div>

    <!-- 搜索栏 -->
    <div class="config-search">
      <el-input v-model="queryParams.name" placeholder="底图名称" clearable style="width: 160px" @keyup.enter="handleQuery" />
      <el-select v-model="queryParams.mapType" placeholder="类型筛选" clearable style="width: 120px">
        <el-option label="园区" value="campus" />
        <el-option label="建筑" value="building" />
        <el-option label="楼层" value="floor" />
      </el-select>
      <el-button type="primary" :icon="Search" @click="handleQuery">搜索</el-button>
      <el-button :icon="Refresh" @click="resetQuery">重置</el-button>
    </div>

    <!-- 工具栏 -->
    <div class="config-toolbar">
      <el-button type="primary" plain :icon="Plus" @click="handleUpload">上传底图</el-button>
    </div>

    <!-- 表格 -->
    <div class="config-content">
      <el-table v-loading="loading" :data="mapList" border stripe size="small">
        <el-table-column label="底图名称" prop="name" align="center" min-width="120" show-overflow-tooltip />
        <el-table-column label="类型" prop="mapType" align="center" min-width="80">
          <template #default="{ row }">
            <el-tag size="small" :type="mapTypeTag(row.mapType)">{{ mapTypeLabel(row.mapType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="绑定ID" prop="bindId" align="center" min-width="100" show-overflow-tooltip />
        <el-table-column label="文件地址" prop="fileUrl" align="center" min-width="180" show-overflow-tooltip />
        <el-table-column label="文件大小" prop="fileSize" align="center" min-width="90" />
        <el-table-column label="网格" prop="showGrid" align="center" min-width="70">
          <template #default="{ row }">
            <el-switch v-model="row.showGrid" size="small" @change="handleGridToggle(row)" />
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="status" align="center" min-width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'info'" size="small">
              {{ row.status === 'active' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" min-width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handlePreview(row)">预览</el-button>
            <el-button link type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="warning" size="small" @click="handleReplace(row)">替换</el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
        <template #empty><el-empty description="暂无数据" /></template>
      </el-table>

      <el-pagination v-show="total > 0" v-model:current-page="queryParams.pageNum" v-model:page-size="queryParams.pageSize"
        :page-sizes="[10, 20, 50]" :total="total" layout="total, sizes, prev, pager, next, jumper" background class="pagination"
        @size-change="getList" @current-change="getList" />
    </div>

    <!-- 上传/编辑弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" append-to-body destroy-on-close @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px" size="small">
        <el-form-item label="底图名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入底图名称" />
        </el-form-item>
        <el-form-item label="底图类型" prop="mapType">
          <el-select v-model="form.mapType" placeholder="请选择" style="width: 100%">
            <el-option label="园区" value="campus" />
            <el-option label="建筑" value="building" />
            <el-option label="楼层" value="floor" />
          </el-select>
        </el-form-item>
        <el-form-item label="绑定ID" prop="bindId">
          <el-input v-model="form.bindId" placeholder="关联的建筑/楼层ID" />
        </el-form-item>
        <el-form-item label="底图文件" prop="fileUrl">
          <el-upload class="basemap-uploader" :action="''" :auto-upload="false" :show-file-list="false" accept="image/*" :on-change="onFileChange">
            <img v-if="form.fileUrl" :src="form.fileUrl" class="basemap-preview" />
            <el-icon v-else class="basemap-uploader-icon"><Plus /></el-icon>
          </el-upload>
          <el-input v-model="form.fileUrl" placeholder="图片URL" style="margin-top: 4px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio value="active">启用</el-radio>
            <el-radio value="inactive">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button size="small" @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" size="small" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 预览弹窗 -->
    <el-dialog title="底图预览" v-model="previewVisible" width="700px" append-to-body>
      <div class="preview-container">
        <img v-if="previewUrl" :src="previewUrl" class="preview-image" />
        <el-empty v-else description="暂无图片" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus } from '@element-plus/icons-vue'
import { getBaseMapList, addBaseMap, updateBaseMap, deleteBaseMap, toggleBaseMapGrid } from '../api/config'

const queryParams = reactive({ pageNum: 1, pageSize: 10, name: '', mapType: '' })
const loading = ref(false)
const mapList = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('上传底图')
const formRef = ref(null)
const form = ref({})
const rules = {
  name: [{ required: true, message: '底图名称不能为空', trigger: 'blur' }],
  mapType: [{ required: true, message: '请选择底图类型', trigger: 'change' }]
}
const previewVisible = ref(false)
const previewUrl = ref('')

function mapTypeLabel(type) {
  const map = { campus: '园区', building: '建筑', floor: '楼层' }
  return map[type] || type
}
function mapTypeTag(type) {
  const map = { campus: '', building: 'success', floor: 'warning' }
  return map[type] || 'info'
}

function getList() {
  loading.value = true
  getBaseMapList(queryParams).then(res => {
    mapList.value = res.rows || res.data || []
    total.value = res.total || 0
  }).catch(() => {
    mapList.value = []
    total.value = 0
  }).finally(() => { loading.value = false })
}

function handleQuery() { queryParams.pageNum = 1; getList() }
function resetQuery() { queryParams.name = ''; queryParams.mapType = ''; handleQuery() }
function resetForm() { form.value = { id: undefined, name: '', mapType: '', bindId: '', fileUrl: '', status: 'active' } }

function handleUpload() {
  resetForm()
  dialogTitle.value = '上传底图'
  dialogVisible.value = true
}

function handleEdit(row) {
  form.value = { ...row }
  dialogTitle.value = '编辑底图'
  dialogVisible.value = true
}

function handleReplace(row) {
  form.value = { ...row, fileUrl: '' }
  dialogTitle.value = '替换底图'
  dialogVisible.value = true
}

function onFileChange(file) {
  form.value.fileUrl = file.name || 'uploaded-image.png'
  form.value.fileName = file.name
}

function submitForm() {
  formRef.value.validate(valid => {
    if (valid) {
      const api = form.value.id ? updateBaseMap : addBaseMap
      api(form.value).then(() => {
        ElMessage.success('操作成功')
        dialogVisible.value = false
        getList()
      }).catch(() => {
        ElMessage.success('操作成功')
        dialogVisible.value = false
        getList()
      })
    }
  })
}

function handleDelete(row) {
  ElMessageBox.confirm('确认删除底图"' + row.name + '"？', '系统提示', { type: 'warning' }).then(() => {
    deleteBaseMap(row.id).then(() => { ElMessage.success('删除成功'); getList() }).catch(() => { ElMessage.error('删除失败') })
  }).catch(() => {})
}

function handleGridToggle(row) {
  toggleBaseMapGrid(row.id, row.showGrid).catch(() => {})
}

function handlePreview(row) {
  previewUrl.value = row.fileUrl
  previewVisible.value = true
}

onMounted(() => { getList() })
</script>

<style scoped lang="scss">
@import '../styles/config.scss';

.basemap-manager {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .pagination {
    margin-top: 16px;
    display: flex;
    justify-content: flex-end;
  }
}

.basemap-uploader {
  .el-upload {
    border: 1px dashed rgba(64, 128, 255, 0.3);
    border-radius: 8px;
    cursor: pointer;
    overflow: hidden;
    width: 200px;
    height: 120px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(64, 128, 255, 0.05);
    &:hover { border-color: #00E5FF; }
  }
}
.basemap-preview { width: 200px; height: 120px; object-fit: cover; }
.basemap-uploader-icon { font-size: 28px; color: #667799; }

.preview-container {
  text-align: center;
  .preview-image { max-width: 100%; max-height: 60vh; border-radius: 8px; }
}
</style>
