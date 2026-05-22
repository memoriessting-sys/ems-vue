<template>
  <el-card shadow="hover" class="page-card">
    <template #header>
      <div class="page-header"><span class="page-title">薪资管理</span></div>
    </template>
    <div class="toolbar">
      <div class="toolbar-left">
        <el-input v-model="searchKeyword" placeholder="搜索员工姓名" clearable class="search-input" @clear="load">
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
        <el-select v-model="filters.salary_year_month" placeholder="选择月份" class="search-select" @change="load">
          <el-option v-for="m in 12" :key="m" :label="m+'月'" :value="m" />
        </el-select>
        <el-button type="primary" @click="load" class="search-btn"><el-icon><Search /></el-icon>查询</el-button>
      </div>
      <div class="toolbar-right">
        <el-button type="success" @click="openDialog()" v-permission="'salary:add'" class="add-btn"><el-icon><Plus /></el-icon>新增薪资</el-button>
        <el-button type="warning" @click="handleBatchPay" v-permission="'salary:pay'" :disabled="!selected.length" class="action-btn"><el-icon><Promotion /></el-icon>批量发放</el-button>
      </div>
    </div>
    <el-table :data="list" border stripe @selection-change="s => selected = s" class="data-table" :header-cell-style="{background:'#f5f7fa',color:'#606266'}">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column prop="emp_name" label="员工" width="100" />
      <el-table-column prop="salary_year_month" label="月份" width="80" align="center" />
      <el-table-column prop="base_salary" label="基本工资" width="100" align="right">
        <template #default="{ row }"><span class="money-text">{{ row.base_salary?.toLocaleString() }}</span></template>
      </el-table-column>
      <el-table-column prop="bonus" label="奖金" width="100" align="right">
        <template #default="{ row }"><span class="money-text green">{{ row.bonus?.toLocaleString() }}</span></template>
      </el-table-column>
      <el-table-column prop="deduction" label="扣款" width="100" align="right">
        <template #default="{ row }"><span class="money-text red">{{ row.deduction?.toLocaleString() }}</span></template>
      </el-table-column>
      <el-table-column prop="actual_salary" label="实发工资" width="110" align="right">
        <template #default="{ row }"><span class="money-text bold">{{ row.actual_salary?.toLocaleString() }}</span></template>
      </el-table-column>
      <el-table-column prop="status_name" label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small" effect="light">{{ row.status_name }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" @click="openDialog(row)" v-permission="'salary:edit'" class="op-btn"><el-icon><Edit /></el-icon>编辑</el-button>
          <el-popconfirm title="确认删除?" @confirm="handleDelete(row.id)">
            <template #reference><el-button link type="danger" v-permission="'salary:edit'" class="op-btn"><el-icon><Delete /></el-icon>删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination background layout="total, sizes, prev, pager, next" :total="total" :page-sizes="[10,20,50]" :page-size="page.pageSize" v-model:current-page="page.pageIndex" @current-change="load" @size-change="load" class="page-pagination" />
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑薪资' : '新增薪资'" width="520px" class="form-dialog">
      <el-form :model="form" label-width="80px" class="dialog-form">
        <el-form-item label="员工ID"><el-input v-model="form.emp_id" placeholder="请输入员工ID" /></el-form-item>
        <el-form-item label="月份"><el-input v-model="form.salary_year_month" placeholder="如 2024-01" /></el-form-item>
        <el-form-item label="基本工资"><el-input-number v-model="form.base_salary" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="奖金"><el-input-number v-model="form.bonus" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="扣款"><el-input-number v-model="form.deduction" :min="0" :precision="2" style="width:100%" /></el-form-item>
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
import { salaryPaged, salarySave, salaryDelete, salaryGetById, salaryBatchPay } from '../../api/salary'
import { ElMessage } from 'element-plus'

const filters = ref({ salary_year_month: null })
const page = ref({ pageIndex: 1, pageSize: 10 })
const searchKeyword = ref('')
const list = ref([])
const total = ref(0)
const selected = ref([])
const dialogVisible = ref(false)
const form = ref({})

onMounted(() => load())

async function load() {
  const res = await salaryPaged({ ...page.value, filters: { ...filters.value } })
  if (res.code === 200) { list.value = res.data.list; total.value = res.data.total }
}

async function openDialog(row) {
  form.value = row ? { ...await salaryGetById(row.id).then(r => r.data || row) } : { emp_id: '', salary_year_month: '', base_salary: 0, bonus: 0, deduction: 0 }
  dialogVisible.value = true
}

async function handleSave() {
  const res = await salarySave(form.value)
  if (res.code === 200) { ElMessage.success('保存成功'); dialogVisible.value = false; load() }
  else ElMessage.error(res.msg)
}

async function handleDelete(id) {
  const res = await salaryDelete(id)
  if (res.code === 200) { ElMessage.success('删除成功'); load() }
  else ElMessage.error(res.msg)
}

async function handleBatchPay() {
  const ids = selected.value.map(i => i.id)
  const res = await salaryBatchPay(ids)
  if (res.code === 200) { ElMessage.success('批量发放成功'); load() }
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
.search-input { width: 180px; }
.search-select { width: 120px; }
.search-btn { border-radius: 6px; }
.add-btn { border-radius: 6px; }
.action-btn { border-radius: 6px; }
.data-table { border-radius: 8px; }
.money-text { font-weight: 500; }
.money-text.green { color: #67c23a; }
.money-text.red { color: #f56c6c; }
.money-text.bold { color: #409eff; font-weight: 700; }
.op-btn { padding: 4px 8px; }
.page-pagination { margin-top: 20px; }
</style>