import * as echarts from 'echarts'
import { ref, onMounted, onBeforeUnmount, watch, nextTick } from 'vue'

/**
 * ECharts composable - 初始化、resize、销毁
 * @param {object} options - 配置项
 * @param {boolean} options.autoResize - 是否自动 resize
 * @param {string} options.theme - 主题名称
 */
export function useChart(options = {}) {
  const chartRef = ref(null)
  let chartInstance = null
  const { autoResize = true, theme = 'dark' } = options

  /** 初始化图表实例 */
  function initChart(el) {
    if (el) {
      chartInstance = echarts.init(el, theme)
    }
    return chartInstance
  }

  /** 设置图表配置 */
  function setOption(option, notMerge = false) {
    if (chartInstance) {
      chartInstance.setOption(option, notMerge)
    }
  }

  /** 手动 resize */
  function resize() {
    if (chartInstance) {
      chartInstance.resize()
    }
  }

  /** 销毁图表 */
  function dispose() {
    if (chartInstance) {
      chartInstance.dispose()
      chartInstance = null
    }
  }

  /** 获取图表实例 */
  function getInstance() {
    return chartInstance
  }

  if (autoResize) {
    onMounted(() => {
      window.addEventListener('resize', resize)
    })
    onBeforeUnmount(() => {
      window.removeEventListener('resize', resize)
      dispose()
    })
  }

  return {
    chartRef,
    chartInstance,
    initChart,
    setOption,
    resize,
    dispose,
    getInstance
  }
}

// ==================== 图表颜色 ====================
export const chartColors = {
  primary: '#4080FF',
  success: '#00E396',
  warning: '#FEB019',
  danger: '#FF4560',
  info: '#90CAF9',
  purple: '#775DD0',
  cyan: '#00D4FF',
  orange: '#FF8C42',
  colors: ['#4080FF', '#00E396', '#FEB019', '#FF4560', '#775DD0', '#00D4FF', '#FF8C42', '#90CAF9']
}

// ==================== 基础图表配置 ====================
export function getBaseChartOption() {
  return {
    backgroundColor: 'transparent',
    textStyle: { color: '#8899BB', fontSize: 11 },
    grid: { left: '8%', right: '4%', top: '12%', bottom: '12%', containLabel: true },
    tooltip: {
      backgroundColor: 'rgba(16, 32, 64, 0.9)',
      borderColor: 'rgba(64, 128, 255, 0.3)',
      textStyle: { color: '#E0E8FF', fontSize: 12 }
    },
    legend: {
      textStyle: { color: '#8899BB', fontSize: 11 },
      icon: 'roundRect',
      itemWidth: 12,
      itemHeight: 8
    },
    xAxis: {
      axisLine: { lineStyle: { color: 'rgba(64, 128, 255, 0.2)' } },
      axisLabel: { color: '#667799', fontSize: 10 },
      splitLine: { show: false }
    },
    yAxis: {
      axisLine: { show: false },
      axisLabel: { color: '#667799', fontSize: 10 },
      splitLine: { lineStyle: { color: 'rgba(64, 128, 255, 0.06)' } }
    }
  }
}

// ==================== 折线图配置 ====================
export function getLineChartOption(xData, seriesData, options = {}) {
  const base = getBaseChartOption()
  const { smooth = true, showArea = true, seriesNames = [] } = options

  const series = Array.isArray(seriesData[0])
    ? seriesData.map((data, idx) => ({
        name: seriesNames[idx] || `系列${idx + 1}`,
        type: 'line',
        data,
        smooth,
        symbol: 'none',
        lineStyle: { width: 2, color: chartColors.colors[idx % chartColors.colors.length] },
        areaStyle: showArea ? {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: chartColors.colors[idx % chartColors.colors.length].replace(')', ', 0.3)').replace('rgb', 'rgba') },
            { offset: 1, color: 'rgba(0, 0, 0, 0)' }
          ])
        } : null
      }))
    : [{
        type: 'line',
        data: seriesData,
        smooth,
        symbol: 'none',
        lineStyle: { width: 2, color: chartColors.primary },
        areaStyle: showArea ? {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 128, 255, 0.3)' },
            { offset: 1, color: 'rgba(64, 128, 255, 0.02)' }
          ])
        } : null
      }]

  return {
    ...base,
    tooltip: { ...base.tooltip, trigger: 'axis' },
    xAxis: { ...base.xAxis, type: 'category', data: xData },
    yAxis: { ...base.yAxis, type: 'value' },
    series
  }
}

// ==================== 柱状图配置 ====================
export function getBarChartOption(xData, seriesData, options = {}) {
  const base = getBaseChartOption()
  const { seriesNames = [], barWidth = '40%' } = options

  const series = Array.isArray(seriesData[0])
    ? seriesData.map((data, idx) => ({
        name: seriesNames[idx] || `系列${idx + 1}`,
        type: 'bar',
        data,
        barWidth,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: chartColors.colors[idx % chartColors.colors.length] },
            { offset: 1, color: chartColors.colors[idx % chartColors.colors.length].replace(')', ', 0.3)').replace('rgb', 'rgba') }
          ]),
          borderRadius: [3, 3, 0, 0]
        }
      }))
    : [{
        type: 'bar',
        data: seriesData,
        barWidth,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: chartColors.primary },
            { offset: 1, color: 'rgba(64, 128, 255, 0.3)' }
          ]),
          borderRadius: [3, 3, 0, 0]
        }
      }]

  return {
    ...base,
    tooltip: { ...base.tooltip, trigger: 'axis' },
    xAxis: { ...base.xAxis, type: 'category', data: xData },
    yAxis: { ...base.yAxis, type: 'value' },
    series
  }
}

// ==================== 饼图配置 ====================
export function getPieChartOption(data, options = {}) {
  const base = getBaseChartOption()
  const { radius = ['40%', '70%'], roseType = false } = options

  return {
    ...base,
    tooltip: { ...base.tooltip, trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    xAxis: { show: false },
    yAxis: { show: false },
    series: [{
      type: 'pie',
      radius,
      center: ['50%', '50%'],
      roseType: roseType ? 'area' : false,
      data: data.map((item, idx) => ({
        ...item,
        itemStyle: { color: item.color || chartColors.colors[idx % chartColors.colors.length] }
      })),
      label: {
        color: '#8899BB',
        fontSize: 11,
        formatter: '{b}\n{d}%'
      },
      labelLine: {
        lineStyle: { color: 'rgba(64, 128, 255, 0.3)' }
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowColor: 'rgba(64, 128, 255, 0.5)'
        }
      }
    }]
  }
}

// ==================== 仪表盘配置 ====================
export function getGaugeChartOption(value, options = {}) {
  const { title = '', min = 0, max = 100, unit = '', colorRanges = [] } = options
  const defaultRanges = [
    [0.3, '#FF4560'],
    [0.7, '#FEB019'],
    [1, '#00E396']
  ]
  const ranges = colorRanges.length ? colorRanges : defaultRanges

  return {
    backgroundColor: 'transparent',
    series: [{
      type: 'gauge',
      startAngle: 220,
      endAngle: -40,
      min,
      max,
      radius: '85%',
      progress: { show: true, width: 12 },
      pointer: { show: false },
      axisLine: {
        lineStyle: {
          width: 12,
          color: ranges
        }
      },
      axisTick: { show: false },
      splitLine: { show: false },
      axisLabel: { show: false },
      title: {
        show: !!title,
        offsetCenter: [0, '70%'],
        color: '#8899BB',
        fontSize: 12
      },
      detail: {
        valueAnimation: true,
        fontSize: 22,
        fontWeight: 700,
        offsetCenter: [0, '40%'],
        formatter: (val) => `${val}${unit}`,
        color: '#FFFFFF'
      },
      data: [{ value, name: title }]
    }]
  }
}

// ==================== 深色主题注册 ====================
export function registerDarkTheme() {
  echarts.registerTheme('dark', {
    backgroundColor: 'transparent',
    textStyle: { color: '#8899BB' },
    title: { textStyle: { color: '#E0E8FF' } },
    legend: { textStyle: { color: '#8899BB' } },
    tooltip: {
      backgroundColor: 'rgba(16, 32, 64, 0.9)',
      borderColor: 'rgba(64, 128, 255, 0.3)',
      textStyle: { color: '#E0E8FF' }
    }
  })
}
