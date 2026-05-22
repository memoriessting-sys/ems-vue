<template>
  <div class="home-wrap">
    <div class="stats-row">
      <div class="stat-card" v-for="item in stats" :key="item.label" :style="{'--accent': item.color}">
        <div class="stat-icon">
          <el-icon :size="28"><component :is="item.icon" /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-label">{{ item.label }}</div>
          <div class="stat-value">{{ item.value }}</div>
        </div>
      </div>
    </div>
    <el-row :gutter="20" class="chart-row">
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
import { basicStats, employeeDistribution, attendanceTrend } from '../../api/chart'
import { User, OfficeBuilding, Clock, Document } from '@element-plus/icons-vue'

const stats = ref([])
const deptChart = ref(null)
const trendChart = ref(null)

onMounted(async () => {
  const [basic, dist, trend] = await Promise.all([basicStats(), employeeDistribution(), attendanceTrend()])

  if (basic.code === 200 && basic.data) {
    const d = basic.data
    stats.value = [
      { label: '员工总数', value: d.employeeCount ?? 0, color: '#409eff', icon: User },
      { label: '部门数量', value: d.departmentCount ?? 0, color: '#67c23a', icon: OfficeBuilding },
      { label: '出勤人次', value: d.attendanceCount ?? 0, color: '#e6a23c', icon: Clock },
      { label: '请假人次', value: d.leaveCount ?? 0, color: '#f56c6c', icon: Document }
    ]
  }

  await nextTick()

  if (dist.code === 200 && dist.data && dist.data.length) {
    const c = echarts.init(deptChart.value)
    c.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: {c}人 ({d}%)' },
      series: [{
        type: 'pie', radius: ['35%', '65%'], center: ['50%', '50%'],
        data: dist.data.map(i => ({ name: i.deptName, value: i.count })),
        label: { formatter: '{b}\n{c}人', fontSize: 12 },
        itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 },
        emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.2)' } }
      }],
      color: ['#409eff', '#67c23a', '#e6a23c', '#f56c6c', '#909399', '#533483']
    })
  }

  if (trend.code === 200 && trend.data && trend.data.length) {
    const c = echarts.init(trendChart.value)
    c.setOption({
      tooltip: { trigger: 'axis' },
      grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
      xAxis: { type: 'category', data: trend.data.map(i => i.date), boundaryGap: false, axisLine: { lineStyle: { color: '#ddd' } } },
      yAxis: { type: 'value', splitLine: { lineStyle: { color: '#eee' } } },
      series: [{
        name: '出勤人数', type: 'line', smooth: true,
        data: trend.data.map(i => i.count),
        lineStyle: { width: 3, color: '#409eff' },
        areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(64,158,255,0.3)' }, { offset: 1, color: 'rgba(64,158,255,0.05)' }]) },
        itemStyle: { color: '#409eff', borderWidth: 2, borderColor: '#fff' },
        symbol: 'circle', symbolSize: 6
      }]
    })
  }
})
</script>

<style scoped>
.home-wrap { }
.stats-row { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; margin-bottom: 20px; }
.stat-card {
  background: #fff; border-radius: 12px; padding: 20px;
  display: flex; align-items: center; gap: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  transition: transform 0.2s, box-shadow 0.2s;
}
.stat-card:hover { transform: translateY(-2px); box-shadow: 0 4px 16px rgba(0,0,0,0.1); }
.stat-icon {
  width: 52px; height: 52px; border-radius: 12px;
  background: var(--accent); display: flex; align-items: center; justify-content: center;
  color: #fff; flex-shrink: 0;
}
.stat-info { flex: 1; }
.stat-label { font-size: 13px; color: #909399; margin-bottom: 6px; }
.stat-value { font-size: 28px; font-weight: 700; color: var(--accent); }
.chart-card { border-radius: 12px !important; }
.chart-title { font-weight: 600; font-size: 15px; }
.chart-box { height: 350px; }
</style>