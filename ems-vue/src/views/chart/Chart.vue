<template>
  <div class="chart-wrap">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header><span class="chart-title">考勤状态分布</span></template>
          <div ref="attendanceChart" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header><span class="chart-title">请假状态分布</span></template>
          <div ref="leaveChart" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header><span class="chart-title">部门人员分布</span></template>
          <div ref="deptChart" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header><span class="chart-title">考勤趋势</span></template>
          <div ref="trendChart" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { attendanceStats, leaveStats, employeeDistribution, attendanceTrend } from '../../api/chart'

const attendanceChart = ref(null)
const leaveChart = ref(null)
const deptChart = ref(null)
const trendChart = ref(null)

const colors = ['#409eff', '#67c23a', '#e6a23c', '#f56c6c', '#909399', '#533483']

onMounted(async () => {
  const [att, lea, dist, trend] = await Promise.all([attendanceStats(), leaveStats(), employeeDistribution(), attendanceTrend()])
  await nextTick()

  // attendanceStats returns Map: {normalCount, lateCount, earlyCount, bothCount}
  if (att.code === 200 && att.data) {
    const d = att.data
    const c = echarts.init(attendanceChart.value)
    c.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
      series: [{
        type: 'pie', radius: ['35%', '65%'], center: ['50%', '50%'],
        data: [
          { name: '正常', value: d.normalCount ?? 0 },
          { name: '迟到', value: d.lateCount ?? 0 },
          { name: '早退', value: d.earlyCount ?? 0 },
          { name: '迟到+早退', value: d.bothCount ?? 0 }
        ],
        label: { formatter: '{b}\n{c}', fontSize: 12 },
        itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
        emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.2)' } }
      }],
      color: colors
    })
  }

  // leaveStats returns Map: {pendingCount, rejectedCount, approvedCount, monthLeaveCount}
  if (lea.code === 200 && lea.data) {
    const d = lea.data
    const c = echarts.init(leaveChart.value)
    c.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
      series: [{
        type: 'pie', radius: '65%', center: ['50%', '50%'],
        data: [
          { name: '待审批', value: d.pendingCount ?? 0 },
          { name: '已批准', value: d.approvedCount ?? 0 },
          { name: '已驳回', value: d.rejectedCount ?? 0 }
        ],
        label: { formatter: '{b}\n{d}%', fontSize: 12 },
        itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
        emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.2)' } }
      }],
      color: ['#e6a23c', '#67c23a', '#f56c6c']
    })
  }

  // employeeDistribution returns List: [{deptName, count}]
  if (dist.code === 200 && dist.data && dist.data.length) {
    const c = echarts.init(deptChart.value)
    c.setOption({
      tooltip: { trigger: 'axis' },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: { type: 'category', data: dist.data.map(i => i.deptName), axisLine: { lineStyle: { color: '#ddd' } } },
      yAxis: { type: 'value', splitLine: { lineStyle: { color: '#eee' } } },
      series: [{
        name: '人数', type: 'bar', data: dist.data.map(i => i.employeeCount),
        itemStyle: { borderRadius: [4, 4, 0, 0], color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: '#67c23a' }, { offset: 1, color: '#b3e19d' }]) },
        barWidth: '40%'
      }]
    })
  }

  // attendanceTrend returns List: [{date, count}]
  if (trend.code === 200 && trend.data && trend.data.length) {
    const c = echarts.init(trendChart.value)
    c.setOption({
      tooltip: { trigger: 'axis' },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: { type: 'category', data: trend.data.map(i => i.date), boundaryGap: false, axisLine: { lineStyle: { color: '#ddd' } } },
      yAxis: { type: 'value', splitLine: { lineStyle: { color: '#eee' } } },
      series: [{
        name: '出勤人数', type: 'line', smooth: true,
        data: trend.data.map(i => i.totalCount),
        lineStyle: { width: 3, color: '#409eff' },
        areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(64,158,255,0.3)' }, { offset: 1, color: 'rgba(64,158,255,0.05)' }]) },
        itemStyle: { color: '#409eff' },
        symbol: 'circle', symbolSize: 6
      }]
    })
  }
})
</script>

<style scoped>
.chart-wrap { }
.chart-card { border-radius: 12px !important; }
.chart-title { font-weight: 600; font-size: 15px; }
.chart-box { height: 350px; }
</style>