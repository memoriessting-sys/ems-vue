<template>
  <el-card shadow="hover" class="page-card">
    <template #header>
      <div class="page-header"><span class="page-title">请假管理</span></div>
    </template>
    <div class="toolbar">
      <div class="toolbar-left">
        <el-select v-model="filters.leaveTypeCode" placeholder="请假类型" clearable class="search-select" @change="load">
          <el-option label="事假" value="1" />
          <el-option label="病假" value="2" />
          <el-option label="年假" value="3" />
          <el-option label="婚假" value="4" />
        </el-select>
        <el-select v-model="filters.status" placeholder="选择状态" clearable class="search-select" @change="load">
          <el-option label="待审批" :value="0" />
          <el-option label="已批准" :value="1" />
          <el-option label="已驳回" :value="2" />
        </el-select>
        <el-button type="primary" @click="load" class="search-btn"><el-icon><Search /></el-icon>查询</el-button>
      </div>
      <el-button type="success" @click="openDialog()" class="add-btn"><el-icon><Plus /></el-icon>申请请假</el-button>
    </div>
    <el-table :data="list" border stripe class="data-table" :header-cell-style="{background:'#f5f7fa',color:'#606266'}">
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column prop="emp_name" label="员工" width="100" />
      <el-table-column prop="leave_type_name" label="请假类型" width="100" align="center">
        <template #default="{ row }">
          <el-tag size="small" effect="plain">{{ row.leave_type_name }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="leaveStartTime" label="开始日期" width="120" />
      <el-table-column prop="leaveEndTime" label="结束日期" width="120" />
      <el-table-column prop="leave_days" label="天数" width="70" align="center" />
      <el-table-column prop="leave_reason" label="原因" min-width="150" />
      <el-table-column prop="status_name" label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="leaveStateType(row.state)" size="small" effect="light">{{ row.stateName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220" fixed="right" align="center">
        <template #default="{ row }">
          <el-button v-if="row.state === 0" link type="success" @click="handleApprove(row.id, 1)" class="op-btn"><el-icon><Check /></el-icon>批准</el-button>
          <el-button v-if="row.state === 0" link type="danger" @click="handleApprove(row.id, 2)" class="op-btn"><el-icon><Close /></el-icon>驳回</el-button>
          <el-button v-if="row.state === 1" link type="warning" @click="handleReturn(row.id)" class="op-btn"><el-icon><RefreshRight /></el-icon>销假</el-button>
          <el-popconfirm title="确认删除?" @confirm="handleDelete(row.id)">
            <template #reference><el-button link type="danger" class="op-btn"><el-icon><Delete /></el-icon>删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination background layout="total, sizes, prev, pager, next" :total="total" :page-sizes="[10,20,50]" :page-size="page.pageSize" v-model:current-page="page.pageIndex" @current-change="load" @size-change="load" class="page-pagination" />
    <el-dialog v-model="dialogVisible" title="申请请假" width="520px" class="form-dialog">
      <el-form :model="form" label-width="80px" class="dialog-form">
        <el-form-item label="请假类型"><el-select v-model="form.leaveTypeCode" placeholder="请选择类型" style="width:100%"><el-option label="事假" value="1" /><el-option label="病假" value="2" /><el-option label="年假" value="3" /><el-option label="婚假" value="4" /></el-select></el-form-item>
        <el-form-item label="开始日期"><el-date-picker v-model="form.leaveStartTime" type="datetime" value-format="YYYY-MM-DD HH:mm" placeholder="选择开始时间" style="width:100%" /></el-form-item>
        <el-form-item label="结束日期"><el-date-picker v-model="form.leaveEndTime" type="datetime" value-format="YYYY-MM-DD HH:mm" placeholder="选择结束时间" style="width:100%" /></el-form-item>
        <el-form-item label="原因"><el-input v-model="form.leaveReason" type="textarea" :rows="3" placeholder="请输入请假原因" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { leavePaged, leaveSave, leaveDelete, leaveApprove, leaveReturn } from '../../api/leave'
import { ElMessage } from 'element-plus'

const filters = ref({ status: null, leaveTypeCode: null })
const page = ref({ pageIndex: 1, pageSize: 10 })
const list = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const form = ref({})

function leaveStateType(state) {
  if (state === 1) return 'success'
  if (state === 2) return 'danger'
  return 'warning'
}

onMounted(() => load())

async function load() {
  const res = await leavePaged({ ...page.value, filters: { ...filters.value } })
  if (res.code === 200) { list.value = res.data.list; total.value = res.data.total }
}

function openDialog() {
  form.value = { leaveTypeCode: '', leaveStartTime: '', leaveEndTime: '', leaveReason: '' }
  dialogVisible.value = true
}

async function handleSave() {
  const res = await leaveSave(form.value)
  if (res.code === 200) { ElMessage.success('申请成功'); dialogVisible.value = false; load() }
  else ElMessage.error(res.msg)
}

async function handleApprove(id, status) {
  const msg = status === 1 ? '批准' : '驳回'
  const res = await leaveApprove(id, status, msg)
  if (res.code === 200) { ElMessage.success(msg + '成功'); load() }
  else ElMessage.error(res.msg)
}

async function handleReturn(id) {
  const res = await leaveReturn(id)
  if (res.code === 200) { ElMessage.success('销假成功'); load() }
  else ElMessage.error(res.msg)
}

async function handleDelete(id) {
  const res = await leaveDelete(id)
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
.search-input { width: 180px; }
.search-select { width: 120px; }
.search-btn { border-radius: 6px; }
.add-btn { border-radius: 6px; }
.data-table { border-radius: 8px; }
.op-btn { padding: 4px 8px; }
.page-pagination { margin-top: 20px; }
</style>