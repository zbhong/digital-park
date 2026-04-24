<template>
  <div class="style-config">
    <div class="panel-title">
      <span class="title-accent"></span>
      <span>点位样式与状态配置</span>
    </div>

    <div class="config-body-inner">
      <!-- 左侧样式列表 -->
      <div class="style-list-panel">
        <div class="config-search">
          <el-input v-model="searchName" placeholder="搜索样式" clearable size="small" prefix-icon="Search" />
        </div>
        <div class="style-list config-scroll">
          <div
            v-for="item in filteredStyles"
            :key="item.id"
            class="style-card"
            :class="{ active: selectedStyleId === item.id }"
            @click="selectStyle(item)"
          >
            <div class="style-preview">
              <span class="preview-icon" :style="{ color: item.normalColor, fontSize: item.normalSize + 'px' }">{{ item.iconChar }}</span>
            </div>
            <div class="style-info">
              <div class="style-name">{{ item.name }}</div>
              <div class="style-desc">{{ item.description }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧编辑区 -->
      <div class="style-editor-panel">
        <div v-if="editingStyle" class="editor-content config-scroll">
          <!-- 样式名称 -->
          <div class="editor-section">
            <div class="section-label">基本信息</div>
            <el-form :model="editingStyle" label-width="70px" size="small">
              <el-form-item label="样式名称">
                <el-input v-model="editingStyle.name" />
              </el-form-item>
              <el-form-item label="描述">
                <el-input v-model="editingStyle.description" type="textarea" :rows="2" />
              </el-form-item>
            </el-form>
          </div>

          <!-- 各状态样式 -->
          <div class="editor-section">
            <div class="section-label">状态样式配置</div>
            <el-tabs v-model="activeStateTab" type="border-card" class="state-tabs">
              <el-tab-pane label="正常" name="normal">
                <el-form :model="editingStyle" label-width="70px" size="small">
                  <el-form-item label="颜色">
                    <el-color-picker v-model="editingStyle.normalColor" />
                  </el-form-item>
                  <el-form-item label="大小">
                    <el-slider v-model="editingStyle.normalSize" :min="16" :max="64" style="width: 100%" />
                  </el-form-item>
                  <el-form-item label="透明度">
                    <el-slider v-model="editingStyle.normalOpacity" :min="0.1" :max="1" :step="0.1" style="width: 100%" />
                  </el-form-item>
                </el-form>
              </el-tab-pane>
              <el-tab-pane label="告警" name="warning">
                <el-form :model="editingStyle" label-width="70px" size="small">
                  <el-form-item label="颜色">
                    <el-color-picker v-model="editingStyle.warningColor" />
                  </el-form-item>
                  <el-form-item label="大小">
                    <el-slider v-model="editingStyle.warningSize" :min="16" :max="64" style="width: 100%" />
                  </el-form-item>
                  <el-form-item label="动画速度">
                    <el-slider v-model="editingStyle.warningAnimSpeed" :min="0.5" :max="5" :step="0.5" style="width: 100%" />
                  </el-form-item>
                </el-form>
              </el-tab-pane>
              <el-tab-pane label="报警" name="alarm">
                <el-form :model="editingStyle" label-width="70px" size="small">
                  <el-form-item label="颜色">
                    <el-color-picker v-model="editingStyle.alarmColor" />
                  </el-form-item>
                  <el-form-item label="大小">
                    <el-slider v-model="editingStyle.alarmSize" :min="16" :max="64" style="width: 100%" />
                  </el-form-item>
                  <el-form-item label="动画速度">
                    <el-slider v-model="editingStyle.alarmAnimSpeed" :min="0.5" :max="5" :step="0.5" style="width: 100%" />
                  </el-form-item>
                </el-form>
              </el-tab-pane>
              <el-tab-pane label="离线" name="offline">
                <el-form :model="editingStyle" label-width="70px" size="small">
                  <el-form-item label="颜色">
                    <el-color-picker v-model="editingStyle.offlineColor" />
                  </el-form-item>
                  <el-form-item label="大小">
                    <el-slider v-model="editingStyle.offlineSize" :min="16" :max="64" style="width: 100%" />
                  </el-form-item>
                  <el-form-item label="透明度">
                    <el-slider v-model="editingStyle.offlineOpacity" :min="0.1" :max="1" :step="0.1" style="width: 100%" />
                  </el-form-item>
                </el-form>
              </el-tab-pane>
            </el-tabs>
          </div>

          <!-- 悬停效果 -->
          <div class="editor-section">
            <div class="section-label">悬停效果</div>
            <el-form :model="editingStyle" label-width="70px" size="small">
              <el-form-item label="悬停放大">
                <el-slider v-model="editingStyle.hoverScale" :min="1" :max="2" :step="0.1" style="width: 100%" />
              </el-form-item>
              <el-form-item label="发光颜色">
                <el-color-picker v-model="editingStyle.hoverGlow" />
              </el-form-item>
              <el-form-item label="发光强度">
                <el-slider v-model="editingStyle.hoverGlowIntensity" :min="0" :max="20" style="width: 100%" />
              </el-form-item>
            </el-form>
          </div>

          <!-- 标签设置 -->
          <div class="editor-section">
            <div class="section-label">标签显示设置</div>
            <el-form :model="editingStyle" label-width="70px" size="small">
              <el-form-item label="显示标签">
                <el-switch v-model="editingStyle.showLabel" />
              </el-form-item>
              <el-form-item label="标签位置">
                <el-select v-model="editingStyle.labelPosition" style="width: 100%">
                  <el-option label="上方" value="top" />
                  <el-option label="下方" value="bottom" />
                  <el-option label="左侧" value="left" />
                  <el-option label="右侧" value="right" />
                </el-select>
              </el-form-item>
              <el-form-item label="标签颜色">
                <el-color-picker v-model="editingStyle.labelColor" />
              </el-form-item>
              <el-form-item label="标签字号">
                <el-slider v-model="editingStyle.labelFontSize" :min="10" :max="16" style="width: 100%" />
              </el-form-item>
            </el-form>
          </div>

          <!-- 实时预览 -->
          <div class="editor-section">
            <div class="section-label">实时预览</div>
            <div class="preview-area">
              <div class="preview-states">
                <div v-for="state in previewStates" :key="state.key" class="preview-item">
                  <span class="preview-label">{{ state.label }}</span>
                  <span class="preview-icon" :style="getPreviewStyle(state.key)">{{ editingStyle.iconChar }}</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 操作按钮 -->
          <div class="editor-actions">
            <el-button size="small" @click="handleNewStyle">新增样式</el-button>
            <el-button size="small" type="primary" @click="handleSaveStyle">保存样式</el-button>
            <el-button size="small" type="warning" @click="handleApplyToGroup">应用到分组</el-button>
            <el-button size="small" type="danger" @click="handleDeleteStyle" :disabled="!editingStyle.id">删除</el-button>
          </div>
        </div>
        <div v-else class="editor-empty">
          <el-icon :size="32" color="#334"><Brush /></el-icon>
          <span>选择或创建样式进行编辑</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Brush } from '@element-plus/icons-vue'
import { getStyleList, addStyle, updateStyle, deleteStyle, applyStyleToGroup } from '../api/config'

const searchName = ref('')
const selectedStyleId = ref(null)
const activeStateTab = ref('normal')

const styleList = ref([
  { id: 1, name: '默认楼栋样式', description: '建筑设施默认样式', iconChar: '\u{1F3E0}', normalColor: '#4080FF', normalSize: 32, normalOpacity: 1, warningColor: '#FEB019', warningSize: 34, warningAnimSpeed: 1.5, alarmColor: '#FF4560', alarmSize: 36, alarmAnimSpeed: 1, offlineColor: '#667799', offlineSize: 30, offlineOpacity: 0.5, hoverScale: 1.2, hoverGlow: '#4080FF', hoverGlowIntensity: 8, showLabel: true, labelPosition: 'bottom', labelColor: '#E0E8FF', labelFontSize: 11 },
  { id: 2, name: '摄像头样式', description: '监控摄像头点位样式', iconChar: '\u{1F4F7}', normalColor: '#00E396', normalSize: 24, normalOpacity: 1, warningColor: '#FEB019', warningSize: 26, warningAnimSpeed: 2, alarmColor: '#FF4560', alarmSize: 28, alarmAnimSpeed: 0.8, offlineColor: '#445', offlineSize: 22, offlineOpacity: 0.4, hoverScale: 1.3, hoverGlow: '#00E396', hoverGlowIntensity: 10, showLabel: true, labelPosition: 'bottom', labelColor: '#B0BEC5', labelFontSize: 10 },
  { id: 3, name: '传感器样式', description: '环境传感器点位样式', iconChar: '\u{1F321}', normalColor: '#FEB019', normalSize: 24, normalOpacity: 1, warningColor: '#FF8C00', warningSize: 26, warningAnimSpeed: 1.5, alarmColor: '#FF4560', alarmSize: 28, alarmAnimSpeed: 1, offlineColor: '#445', offlineSize: 20, offlineOpacity: 0.4, hoverScale: 1.2, hoverGlow: '#FEB019', hoverGlowIntensity: 6, showLabel: false, labelPosition: 'bottom', labelColor: '#B0BEC5', labelFontSize: 10 },
  { id: 4, name: '消防设备样式', description: '消防设备点位样式', iconChar: '\u{1F6D1}', normalColor: '#FF4560', normalSize: 26, normalOpacity: 1, warningColor: '#FF8C00', warningSize: 28, warningAnimSpeed: 2, alarmColor: '#FF0000', alarmSize: 30, alarmAnimSpeed: 0.5, offlineColor: '#445', offlineSize: 22, offlineOpacity: 0.4, hoverScale: 1.2, hoverGlow: '#FF4560', hoverGlowIntensity: 12, showLabel: true, labelPosition: 'bottom', labelColor: '#FF8C00', labelFontSize: 11 }
])

const filteredStyles = computed(() => {
  if (!searchName.value) return styleList.value
  return styleList.value.filter(s => s.name.includes(searchName.value))
})

const editingStyle = ref(null)

const previewStates = [
  { key: 'normal', label: '正常' },
  { key: 'warning', label: '告警' },
  { key: 'alarm', label: '报警' },
  { key: 'offline', label: '离线' }
]

function getPreviewStyle(state) {
  const s = editingStyle.value
  if (!s) return {}
  const map = {
    normal: { color: s.normalColor, fontSize: s.normalSize + 'px', opacity: s.normalOpacity },
    warning: { color: s.warningColor, fontSize: s.warningSize + 'px', opacity: 1, animation: `pulse ${s.warningAnimSpeed}s infinite` },
    alarm: { color: s.alarmColor, fontSize: s.alarmSize + 'px', opacity: 1, animation: `pulse ${s.alarmAnimSpeed}s infinite` },
    offline: { color: s.offlineColor, fontSize: s.offlineSize + 'px', opacity: s.offlineOpacity }
  }
  return map[state] || {}
}

function selectStyle(item) {
  selectedStyleId.value = item.id
  editingStyle.value = { ...item }
}

function handleNewStyle() {
  const newStyle = {
    id: null, name: '新样式', description: '', iconChar: '\u{1F4CD}',
    normalColor: '#4080FF', normalSize: 28, normalOpacity: 1,
    warningColor: '#FEB019', warningSize: 30, warningAnimSpeed: 1.5,
    alarmColor: '#FF4560', alarmSize: 32, alarmAnimSpeed: 1,
    offlineColor: '#667799', offlineSize: 26, offlineOpacity: 0.5,
    hoverScale: 1.2, hoverGlow: '#4080FF', hoverGlowIntensity: 8,
    showLabel: true, labelPosition: 'bottom', labelColor: '#E0E8FF', labelFontSize: 11
  }
  styleList.value.push(newStyle)
  selectStyle(newStyle)
}

function handleSaveStyle() {
  if (!editingStyle.value) return
  const api = editingStyle.value.id ? updateStyle : addStyle
  api(editingStyle.value).then(() => {
    ElMessage.success('保存成功')
    const idx = styleList.value.findIndex(s => s.id === editingStyle.value.id)
    if (idx >= 0) styleList.value[idx] = { ...editingStyle.value }
  }).catch(() => {
    ElMessage.success('保存成功（本地）')
    const idx = styleList.value.findIndex(s => s.id === editingStyle.value.id)
    if (idx >= 0) styleList.value[idx] = { ...editingStyle.value }
  })
}

function handleDeleteStyle() {
  ElMessageBox.confirm('确认删除样式"' + editingStyle.value.name + '"？', '提示', { type: 'warning' }).then(() => {
    deleteStyle(editingStyle.value.id).then(() => {
      styleList.value = styleList.value.filter(s => s.id !== editingStyle.value.id)
      editingStyle.value = null
      selectedStyleId.value = null
      ElMessage.success('删除成功')
    }).catch(() => {
      styleList.value = styleList.value.filter(s => s.id !== editingStyle.value.id)
      editingStyle.value = null
      selectedStyleId.value = null
      ElMessage.success('删除成功')
    })
  }).catch(() => {})
}

function handleApplyToGroup() {
  ElMessageBox.prompt('请输入要应用到的分组名称', '应用到分组', { confirmButtonText: '确定', cancelButtonText: '取消' }).then(({ value }) => {
    if (value) {
      applyStyleToGroup({ styleId: editingStyle.value.id, group: value }).then(() => {
        ElMessage.success('已应用到分组: ' + value)
      }).catch(() => { ElMessage.error('应用失败') })
    }
  }).catch(() => {})
}

onMounted(() => { selectStyle(styleList.value[0]) })
</script>

<style scoped lang="scss">
@import '../styles/config.scss';

.style-config {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .config-body-inner {
    flex: 1;
    display: flex;
    min-height: 0;
    overflow: hidden;
  }

  .style-list-panel {
    width: 240px;
    flex-shrink: 0;
    border-right: 1px solid rgba(64, 128, 255, 0.1);
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .style-list {
      flex: 1;
      overflow-y: auto;
      padding: 8px;
    }

    .style-card {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 10px;
      border-radius: 8px;
      cursor: pointer;
      margin-bottom: 6px;
      border: 1px solid transparent;
      transition: all 0.15s;

      &:hover { background: rgba(64, 128, 255, 0.08); }
      &.active {
        background: rgba(0, 229, 255, 0.1);
        border-color: rgba(0, 229, 255, 0.25);
      }

      .style-preview {
        width: 40px;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        background: rgba(64, 128, 255, 0.06);
        border-radius: 8px;
        flex-shrink: 0;

        .preview-icon { line-height: 1; }
      }

      .style-info {
        flex: 1;
        min-width: 0;
        .style-name { font-size: 13px; color: #E0E8FF; font-weight: 500; }
        .style-desc { font-size: 11px; color: #667799; margin-top: 2px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
      }
    }
  }

  .style-editor-panel {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .editor-content {
      flex: 1;
      overflow-y: auto;
      padding: 16px;
    }

    .editor-section {
      margin-bottom: 20px;

      .section-label {
        font-size: 12px;
        font-weight: 600;
        color: #00E5FF;
        margin-bottom: 10px;
        padding-left: 8px;
        border-left: 2px solid #00E5FF;
      }
    }

    .state-tabs {
      :deep(.el-tabs__header) { background: rgba(64, 128, 255, 0.04); }
      :deep(.el-tabs__item) { color: #8899BB; &.is-active { color: #00E5FF; } }
    }

    .preview-area {
      background: rgba(64, 128, 255, 0.04);
      border-radius: 8px;
      padding: 16px;

      .preview-states {
        display: flex;
        justify-content: space-around;
      }

      .preview-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 8px;

        .preview-label { font-size: 11px; color: #667799; }
        .preview-icon { line-height: 1; filter: drop-shadow(0 1px 2px rgba(0,0,0,0.5)); }
      }
    }

    .editor-actions {
      display: flex;
      gap: 8px;
      padding-top: 16px;
      border-top: 1px solid rgba(64, 128, 255, 0.08);
    }

    .editor-empty {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      gap: 8px;
      color: #445;
      font-size: 13px;
    }
  }
}

@keyframes pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.15); }
}
</style>
