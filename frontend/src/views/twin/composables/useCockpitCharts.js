import { nextTick } from 'vue'
import * as echarts from 'echarts'

// ==================== 常量 ====================
export const CHART_COLORS = ['#00d9ff', '#28c76f', '#ffc107', '#ff3b3b', '#af52de', '#5ac8fa']
export const AXIS_STYLE = {
  axisLine: { lineStyle: { color: 'rgba(255,255,255,0.06)' } },
  axisLabel: { color: 'rgba(255,255,255,0.35)', fontSize: 10 },
  splitLine: { lineStyle: { color: 'rgba(255,255,255,0.03)' } }
}
export const TOOLTIP_STYLE = {
  backgroundColor: 'rgba(10,22,50,0.9)',
  borderColor: 'rgba(0,217,255,0.2)',
  textStyle: { color: '#e0e8ff', fontSize: 12 }
}

// ==================== 工具函数 ====================
function makeGradient(color1, color2, direction) {
  const d = direction || 'vertical'
  if (d === 'vertical') {
    return new echarts.graphic.LinearGradient(0, 0, 0, 1, [
      { offset: 0, color: color1 },
      { offset: 1, color: color2 }
    ])
  }
  return new echarts.graphic.LinearGradient(0, 0, 1, 0, [
    { offset: 0, color: color1 },
    { offset: 1, color: color2 }
  ])
}

function getLast7Days() {
  const days = []
  for (let i = 6; i >= 0; i--) {
    const d = new Date()
    d.setDate(d.getDate() - i)
    days.push((d.getMonth() + 1) + '/' + d.getDate())
  }
  return days
}

function getLast24Hours() {
  const hours = []
  for (let i = 23; i >= 0; i--) {
    hours.push(String(i).padStart(2, '0') + ':00')
  }
  return hours
}

// ==================== Composable ====================
export function useCockpitCharts() {
  const chartRefs = {}
  const chartInstances = {}

  function bindChartRef(name) {
    return (el) => {
      if (el) chartRefs[name] = el
    }
  }

  function initChart(name, option) {
    nextTick(() => {
      const dom = chartRefs[name]
      if (!dom) return
      if (chartInstances[name]) {
        chartInstances[name].dispose()
      }
      chartInstances[name] = echarts.init(dom)
      chartInstances[name].setOption(option)
    })
  }

  function disposeAllCharts() {
    Object.keys(chartInstances).forEach(key => {
      if (chartInstances[key]) {
        chartInstances[key].dispose()
        delete chartInstances[key]
      }
    })
  }

  function resizeAllCharts() {
    Object.values(chartInstances).forEach(chart => {
      if (chart && !chart.isDisposed()) {
        chart.resize()
      }
    })
  }

  // ==================== 图表初始化函数 ====================
  function initOverviewCharts(overviewData) {
    const fs = overviewData.fireStats
    initChart('overviewFireRing', {
      tooltip: { ...TOOLTIP_STYLE, trigger: 'item' },
      series: [{
        type: 'pie',
        radius: ['55%', '80%'],
        center: ['50%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 4, borderColor: '#071126', borderWidth: 2 },
        label: { show: false },
        data: [
          { value: fs.normal, name: '正常', itemStyle: { color: '#28c76f' } },
          { value: fs.maintain, name: '维护', itemStyle: { color: '#ffc107' } },
          { value: fs.abnormal, name: '异常', itemStyle: { color: '#ff3b3b' } }
        ]
      }]
    })
    initChart('overviewTrend', {
      tooltip: TOOLTIP_STYLE,
      grid: { top: 10, right: 10, bottom: 20, left: 35 },
      xAxis: { type: 'category', data: overviewData.trendDays, ...AXIS_STYLE, boundaryGap: true },
      yAxis: { type: 'value', ...AXIS_STYLE },
      series: [{
        type: 'bar',
        data: overviewData.trendValues,
        barWidth: '50%',
        itemStyle: {
          borderRadius: [4, 4, 0, 0],
          color: makeGradient('rgba(0,217,255,0.8)', 'rgba(0,217,255,0.15)')
        }
      }]
    })
  }

  function initSafetyCharts(safetyData) {
    initChart('safetyFirePie', {
      tooltip: { ...TOOLTIP_STYLE, trigger: 'item' },
      series: [{
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '50%'],
        itemStyle: { borderRadius: 4, borderColor: '#071126', borderWidth: 2 },
        label: { show: false },
        data: safetyData.fireStats.map(f => ({ value: f.value, name: f.label, itemStyle: { color: f.color } }))
      }]
    })
    initChart('safetyAlertLevel', {
      tooltip: TOOLTIP_STYLE,
      legend: { textStyle: { color: 'rgba(255,255,255,0.5)', fontSize: 10 }, top: 0, itemWidth: 10, itemHeight: 10 },
      grid: { top: 30, right: 10, bottom: 20, left: 35 },
      xAxis: { type: 'category', data: getLast7Days(), ...AXIS_STYLE, boundaryGap: true },
      yAxis: { type: 'value', ...AXIS_STYLE },
      series: [
        { name: '紧急', type: 'bar', stack: 'alert', data: getLast7Days().map(() => Math.floor(Math.random() * 3)), itemStyle: { color: '#ff3b3b', borderRadius: 0 }, barWidth: '45%' },
        { name: '重要', type: 'bar', stack: 'alert', data: getLast7Days().map(() => Math.floor(Math.random() * 5) + 1), itemStyle: { color: '#ffc107', borderRadius: 0 } },
        { name: '一般', type: 'bar', stack: 'alert', data: getLast7Days().map(() => Math.floor(Math.random() * 8) + 2), itemStyle: { color: '#00d9ff', borderRadius: [4, 4, 0, 0] } }
      ]
    })
    initChart('safetyAreaRank', {
      tooltip: TOOLTIP_STYLE,
      grid: { top: 5, right: 40, bottom: 5, left: 60 },
      xAxis: { type: 'value', ...AXIS_STYLE, show: false },
      yAxis: { type: 'category', data: ['E栋', 'D栋', 'C栋', 'B栋', 'A栋'].reverse(), ...AXIS_STYLE, axisLabel: { ...AXIS_STYLE.axisLabel, width: 50 } },
      series: [{
        type: 'bar',
        data: [3, 5, 8, 12, 6].reverse(),
        barWidth: '50%',
        itemStyle: {
          borderRadius: [0, 4, 4, 0],
          color: makeGradient('rgba(0,217,255,0.8)', 'rgba(0,217,255,0.15)', 'horizontal')
        },
        label: { show: true, position: 'right', color: 'rgba(255,255,255,0.5)', fontSize: 10 }
      }]
    })
  }

  function initEnergyCharts(energyData) {
    const sparkOpt = (data, color) => ({
      grid: { top: 5, right: 5, bottom: 5, left: 5 },
      xAxis: { type: 'category', show: false, data: getLast24Hours() },
      yAxis: { type: 'value', show: false },
      series: [{
        type: 'line', data, smooth: true, showSymbol: false,
        lineStyle: { width: 1.5, color },
        areaStyle: { color: makeGradient(color.replace(')', ',0.3)').replace('rgb', 'rgba'), 'rgba(0,0,0,0)') }
      }]
    })
    initChart('energyElecSpark', sparkOpt(energyData.elecSparkData, 'rgb(0,217,255)'))
    initChart('energyWaterSpark', sparkOpt(energyData.waterSparkData, 'rgb(40,199,111)'))
    initChart('energyGasSpark', sparkOpt(energyData.gasSparkData, 'rgb(255,193,7)'))
    initChart('energyCompare', {
      tooltip: TOOLTIP_STYLE,
      legend: { textStyle: { color: 'rgba(255,255,255,0.5)', fontSize: 10 }, top: 0, itemWidth: 12, itemHeight: 8 },
      grid: { top: 30, right: 40, bottom: 20, left: 40 },
      xAxis: { type: 'category', data: energyData.compareMonths, ...AXIS_STYLE, boundaryGap: true },
      yAxis: [
        { type: 'value', name: 'kWh', nameTextStyle: { color: 'rgba(255,255,255,0.3)', fontSize: 9 }, ...AXIS_STYLE },
        { type: 'value', name: '%', nameTextStyle: { color: 'rgba(255,255,255,0.3)', fontSize: 9 }, ...AXIS_STYLE, splitLine: { show: false } }
      ],
      series: [
        {
          name: '今年', type: 'bar', data: energyData.compareThisYear, barWidth: '30%',
          itemStyle: { borderRadius: [4, 4, 0, 0], color: makeGradient('rgba(0,217,255,0.8)', 'rgba(0,217,255,0.15)') }
        },
        {
          name: '去年', type: 'bar', data: energyData.compareLastYear, barWidth: '30%',
          itemStyle: { borderRadius: [4, 4, 0, 0], color: makeGradient('rgba(175,82,222,0.6)', 'rgba(175,82,222,0.1)') }
        },
        {
          name: '同比', type: 'line', yAxisIndex: 1,
          data: energyData.compareThisYear.map((v, i) => {
            const last = energyData.compareLastYear[i] || 1
            return +((v - last) / last * 100).toFixed(1)
          }),
          smooth: true, showSymbol: false,
          lineStyle: { width: 2, color: '#ffc107' },
          itemStyle: { color: '#ffc107' }
        }
      ]
    })
    initChart('energyElecCurve', {
      tooltip: TOOLTIP_STYLE,
      grid: { top: 15, right: 10, bottom: 20, left: 40 },
      xAxis: { type: 'category', data: energyData.elecCurveHours, ...AXIS_STYLE, boundaryGap: false },
      yAxis: { type: 'value', ...AXIS_STYLE },
      series: [{
        type: 'line', data: energyData.elecCurveData, smooth: true, showSymbol: false,
        lineStyle: { width: 2, color: '#00d9ff' },
        areaStyle: { color: makeGradient('rgba(0,217,255,0.35)', 'rgba(0,217,255,0.02)') }
      }]
    })
    initChart('energyFlow', {
      tooltip: TOOLTIP_STYLE,
      grid: { top: 10, right: 30, bottom: 20, left: 60 },
      xAxis: { type: 'value', ...AXIS_STYLE, show: false },
      yAxis: { type: 'category', data: energyData.flowSources, ...AXIS_STYLE, axisLabel: { ...AXIS_STYLE.axisLabel, width: 50 } },
      series: [{
        type: 'bar', data: energyData.flowValues, barWidth: '50%',
        itemStyle: {
          borderRadius: [0, 4, 4, 0],
          color: (params) => CHART_COLORS[params.dataIndex % CHART_COLORS.length]
        },
        label: { show: true, position: 'right', color: 'rgba(255,255,255,0.6)', fontSize: 11, formatter: '{c}%' }
      }]
    })
  }

  function initEnvironmentCharts(envData) {
    initChart('envTrend', {
      tooltip: TOOLTIP_STYLE,
      legend: { textStyle: { color: 'rgba(255,255,255,0.5)', fontSize: 10 }, top: 0, itemWidth: 12, itemHeight: 8 },
      grid: { top: 30, right: 10, bottom: 20, left: 35 },
      xAxis: { type: 'category', data: envData.trendTimes, ...AXIS_STYLE, boundaryGap: false },
      yAxis: { type: 'value', ...AXIS_STYLE },
      series: envData.trendSeries.map((s, i) => ({
        name: s.name, type: 'line', data: s.data, smooth: true, showSymbol: false,
        lineStyle: { width: 1.5, color: CHART_COLORS[i] },
        itemStyle: { color: CHART_COLORS[i] }
      }))
    })
    initChart('envAlertBar', {
      tooltip: TOOLTIP_STYLE,
      grid: { top: 10, right: 10, bottom: 20, left: 50 },
      xAxis: { type: 'value', ...AXIS_STYLE },
      yAxis: { type: 'category', data: envData.alertTypes, ...AXIS_STYLE, axisLabel: { ...AXIS_STYLE.axisLabel, width: 45 } },
      series: [{
        type: 'bar', data: envData.alertCounts, barWidth: '50%',
        itemStyle: {
          borderRadius: [0, 4, 4, 0],
          color: makeGradient('rgba(255,193,7,0.8)', 'rgba(255,193,7,0.15)', 'horizontal')
        },
        label: { show: true, position: 'right', color: 'rgba(255,255,255,0.5)', fontSize: 10 }
      }]
    })
    initChart('envAreaRank', {
      tooltip: TOOLTIP_STYLE,
      grid: { top: 5, right: 40, bottom: 5, left: 50 },
      xAxis: { type: 'value', ...AXIS_STYLE, show: false },
      yAxis: { type: 'category', data: [...envData.areaRankNames].reverse(), ...AXIS_STYLE, axisLabel: { ...AXIS_STYLE.axisLabel, width: 40 } },
      series: [{
        type: 'bar', data: [...envData.areaRankValues].reverse(), barWidth: '50%',
        itemStyle: {
          borderRadius: [0, 4, 4, 0],
          color: makeGradient('rgba(40,199,111,0.8)', 'rgba(40,199,111,0.15)', 'horizontal')
        },
        label: { show: true, position: 'right', color: 'rgba(255,255,255,0.5)', fontSize: 10 }
      }]
    })
  }

  function initAssetCharts(assetData) {
    initChart('assetBuildingBar', {
      tooltip: TOOLTIP_STYLE,
      grid: { top: 10, right: 30, bottom: 20, left: 45 },
      xAxis: { type: 'value', ...AXIS_STYLE, show: false },
      yAxis: { type: 'category', data: assetData.buildingNames, ...AXIS_STYLE, axisLabel: { ...AXIS_STYLE.axisLabel, width: 35 } },
      series: [{
        type: 'bar', data: assetData.buildingValues, barWidth: '50%',
        itemStyle: {
          borderRadius: [0, 4, 4, 0],
          color: makeGradient('rgba(0,217,255,0.8)', 'rgba(0,217,255,0.15)', 'horizontal')
        },
        label: { show: true, position: 'right', color: 'rgba(255,255,255,0.5)', fontSize: 10 }
      }]
    })
    initChart('assetHealthRing', {
      series: [{
        type: 'pie',
        radius: ['65%', '85%'],
        center: ['50%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 4, borderColor: '#071126', borderWidth: 3 },
        label: { show: false },
        data: [
          { value: assetData.healthRate, name: '完好', itemStyle: { color: '#28c76f' } },
          { value: +(100 - assetData.healthRate).toFixed(1), name: '异常', itemStyle: { color: '#ff3b3b' } }
        ]
      }]
    })
    initChart('assetCategoryPie', {
      tooltip: { ...TOOLTIP_STYLE, trigger: 'item' },
      legend: { textStyle: { color: 'rgba(255,255,255,0.5)', fontSize: 10 }, bottom: 0, itemWidth: 10, itemHeight: 10 },
      series: [{
        type: 'pie',
        radius: ['35%', '60%'],
        center: ['50%', '45%'],
        itemStyle: { borderRadius: 4, borderColor: '#071126', borderWidth: 2 },
        label: { show: false },
        data: (assetData.categoryNames.length ? assetData.categoryNames : ['电气', '暖通', '给排水', '消防', '电梯', '其他']).map((n, i) => ({
          value: assetData.categoryValues[i] || Math.floor(Math.random() * 200) + 50,
          name: n,
          itemStyle: { color: CHART_COLORS[i % CHART_COLORS.length] }
        }))
      }]
    })
  }

  function initDeviceDialogChart() {
    const hours = getLast24Hours()
    initChart('deviceTrend', {
      tooltip: TOOLTIP_STYLE,
      grid: { top: 10, right: 15, bottom: 20, left: 40 },
      xAxis: { type: 'category', data: hours, ...AXIS_STYLE, boundaryGap: false },
      yAxis: { type: 'value', ...AXIS_STYLE },
      series: [{
        type: 'line', data: hours.map(() => Math.floor(Math.random() * 30) + 20), smooth: true, showSymbol: false,
        lineStyle: { width: 2, color: '#00d9ff' },
        areaStyle: { color: makeGradient('rgba(0,217,255,0.3)', 'rgba(0,217,255,0.02)') }
      }]
    })
  }

  function initChartsForTab(tab, dataMap) {
    switch (tab) {
      case 'overview': initOverviewCharts(dataMap.overviewData); break
      case 'safety': initSafetyCharts(dataMap.safetyData); break
      case 'energy': initEnergyCharts(dataMap.energyData); break
      case 'environment': initEnvironmentCharts(dataMap.envData); break
      case 'asset': initAssetCharts(dataMap.assetData); break
    }
  }

  return {
    bindChartRef,
    initChart,
    disposeAllCharts,
    resizeAllCharts,
    initChartsForTab,
    initDeviceDialogChart
  }
}
