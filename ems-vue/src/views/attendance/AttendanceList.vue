<template>
  <el-card shadow="hover" class="page-card">
    <template #header>
      <div class="page-header"><span class="page-title">考勤管理</span></div>
    </template>
    <div class="toolbar">
      <div class="toolbar-left">
        <el-select v-model="filters.departmentId" placeholder="选择部门" clearable class="search-select" @change="load">
          <el-option v-for="d in departments" :key="d.id" :label="d.name" :value="d.id" />
        </el-select>
        <el-input v-model="filters.empName" placeholder="搜索员工" clearable class="search-input" @clear="load">
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
        <el-button type="primary" @click="load" class="search-btn"><el-icon><Search /></el-icon>查询</el-button>
      </div>
      <div class="toolbar-right">
        <el-button type="success" @click="handleCheckIn" class="action-btn"><el-icon><CircleCheck /></el-icon>签到</el-button>
        <el-button type="warning" @click="handleCheckOut" class="action-btn"><el-icon><CircleClose /></el-icon>签退</el-button>
      </div>
    </div>
    <el-table :data="list" border stripe class="data-table" :header-cell-style="{background:'#f5f7fa',color:'#606266'}">
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column prop="empName" label="员工" width="100" />
      <el-table-column prop="date" label="日期" width="120" />
      <el-table-column prop="checkInTime" label="签到时间" width="100" />
      <el-table-column prop="checkOutTime" label="签退时间" width="100" />
      <el-table-column prop="stateName" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="stateTagType(row.state)" size="small" effect="light">{{ row.stateName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" align="center">
        <template #default="{ row }">
          <el-popconfirm title="确认删除该记录?" @confirm="handleDelete(row.id)">
            <template #reference><el-button link type="danger" class="op-btn"><el-icon><Delete /></el-icon>删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination background layout="total, sizes, prev, pager, next" :total="total" :page-sizes="[10,20,50]" :page-size="page.pageSize" v-model:current-page="page.pageIndex" @current-change="load" @size-change="load" class="page-pagination" />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { attendancePaged, attendanceDelete, checkIn, checkOut, attendanceDepartments, attendanceEmployees } from '../../api/attendance'
import { ElMessage } from 'element-plus'

const filters = ref({ departmentId: null, empName: '' })
const page = ref({ pageIndex: 1, pageSize: 10 })
const list = ref([])
const total = ref(0)
const departments = ref([])
const employees = ref([])

function stateTagType(state) {
  if (state === 1) return 'success'
  if (state === 2) return 'warning'
  return 'info'
}

onMounted(() => {
  attendanceDepartments().then(r => { if (r.code === 200) departments.value = r.data })
  attendanceEmployees().then(r => { if (r.code === 200) employees.value = r.data })
  load()
})

async function load() {
  const res = await attendancePaged({ ...page.value, filters: { ...filters.value } })
  if (res.code === 200) { list.value = res.data.list; total.value = res.data.total }
}

async function handleCheckIn() {
  const res = await checkIn()
  if (res.code === 200) { ElMessage.success('签到成功'); load() }
  else ElMessage.error(res.msg)
}

async function handleCheckOut() {
  const res = await checkOut()
  if (res.code === 200) { ElMessage.success('签退成功'); load() }
  else ElMessage.error(res.msg)
}

async function handleDelete(id) {
  const res = await attendanceDelete(id)
  if (res.code === 200) { ElMessage.success('删除成功'); load() }
  else ElMessage.error(res.msg)
}
</script>

<style scoped>
.page-card { border-radius: 12px !important; }
.page-header { display: flex; align-items: center; }
.page-title { font-weight: 600; font-size: 16px; }
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.toolbar-left { display: flex; align-items: center; gap: 8px; }
.toolbar-right { display: flex; align-items: center; gap: 8px; }
.search-select { width: 160px; }
.search-btn { border-radius: 6px; }
.action-btn { border-radius: 6px; }
.data-table { border-radius: 8px; }
.op-btn { padding: 4px 8px; }
.page-pagination { margin-top: 20px; }
</style>