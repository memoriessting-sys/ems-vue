<template>
  <div class="chart-wrap">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header><span class="chart-title">薪资统计</span></template>
          <div ref="salaryChart" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header><span class="chart-title">请假统计</span></template>
          <div ref="leaveChart" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header><span class="chart-title">考勤统计</span></template>
          <div ref="attendanceChart" class="chart-box"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <template #header><span class="chart-title">部门人员分布</span></template>
          <div ref="deptChart" class="chart-box"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { salaryStats, leaveStats, attendanceStats, employeeDistribution } from '../../api/chart'

const salaryChart = ref(null)
const leaveChart = ref(null)
const attendanceChart = ref(null)
const deptChart = ref(null)

const colors = ['#409eff', '#67c23a', '#e6a23c', '#f56c6c', '#909399', '#533483']

onMounted(async () => {
  const [sal, lea, att, dist] = await Promise.all([salaryStats(), leaveStats(), attendanceStats(), employeeDistribution()])
  await nextTick()

  if (sal.code === 200 && sal.data) {
    const c = echarts.init(salaryChart.value)
    c.setOption({
      tooltip: { trigger: 'axis' },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: { type: 'category', data: sal.data.map(i => i.name), axisLine: { lineStyle: { color: '#ddd' } } },
      yAxis: { type: 'value', splitLine: { lineStyle: { color: '#eee' } } },
      series: [{
        name: '薪资', type: 'bar', data: sal.data.map(i => i.value),
        itemStyle: { borderRadius: [4, 4, 0, 0], color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: '#409eff' }, { offset: 1, color: '#79bbff' }]) },
        barWidth: '40%'
      }]
    })
  }

  if (lea.code === 200 && lea.data) {
    const c = echarts.init(leaveChart.value)
    c.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
      series: [{
        type: 'pie', radius: '65%', center: ['50%', '50%'],
        data: lea.data.map(i => ({ name: i.name, value: i.count })),
        label: { formatter: '{b}\n{d}%', fontSize: 12 },
        itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
        emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.2)' } }
      }],
      color: colors
    })
  }

  if (att.code === 200 && att.data) {
    const c = echarts.init(attendanceChart.value)
    c.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
      series: [{
        type: 'pie', radius: ['35%', '65%'], center: ['50%', '50%'],
        data: att.data.map(i => ({ name: i.name, value: i.count })),
        label: { formatter: '{b}\n{c}', fontSize: 12 },
        itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
        emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.2)' } }
      }],
      color: colors
    })
  }

  if (dist.code === 200 && dist.data) {
    const c = echarts.init(deptChart.value)
    c.setOption({
      tooltip: { trigger: 'axis' },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: { type: 'category', data: dist.data.map(i => i.name), axisLine: { lineStyle: { color: '#ddd' } } },
      yAxis: { type: 'value', splitLine: { lineStyle: { color: '#eee' } } },
      series: [{
        name: '人数', type: 'bar', data: dist.data.map(i => i.count),
        itemStyle: { borderRadius: [4, 4, 0, 0], color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: '#67c23a' }, { offset: 1, color: '#b3e19d' }]) },
        barWidth: '40%'
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