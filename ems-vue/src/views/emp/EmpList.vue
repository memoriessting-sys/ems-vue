<template>
  <el-card shadow="hover" class="page-card">
    <template #header>
      <div class="page-header">
        <span class="page-title">员工管理</span>
      </div>
    </template>
    <div class="toolbar">
      <div class="toolbar-left">
        <el-input v-model="filters.name" placeholder="搜索姓名" clearable class="search-input" @clear="load">
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
        <el-select v-model="filters.deptId" placeholder="选择部门" clearable class="search-select" @change="load">
          <el-option v-for="d in depts" :key="d.id" :label="d.name" :value="d.id" />
        </el-select>
        <el-button type="primary" @click="load" class="search-btn"><el-icon><Search /></el-icon>查询</el-button>
      </div>
      <el-button type="success" @click="openDialog()" class="add-btn"><el-icon><Plus /></el-icon>新增员工</el-button>
    </div>
    <el-table :data="list" border stripe class="data-table" :header-cell-style="{background:'#f5f7fa',color:'#606266'}">
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column prop="name" label="姓名" width="100" />
      <el-table-column prop="code" label="工号" width="120" />
      <el-table-column prop="idCard" label="证件号" width="180" />
      <el-table-column prop="deptName" label="部门" width="120" />
      <el-table-column prop="postLevelName" label="岗位等级" width="120" />
      <el-table-column prop="mobile" label="电话" width="130" />
      <el-table-column prop="email" label="邮箱" min-width="160" />
      <el-table-column label="操作" width="150" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" @click="openDialog(row)" class="op-btn"><el-icon><Edit /></el-icon>编辑</el-button>
          <el-popconfirm title="确认删除该员工?" @confirm="handleDelete(row.id)">
            <template #reference><el-button link type="danger" class="op-btn"><el-icon><Delete /></el-icon>删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination background layout="total, sizes, prev, pager, next" :total="total" :page-sizes="[10,20,50]" :page-size="page.pageSize" v-model:current-page="page.pageIndex" @current-change="load" @size-change="load" class="page-pagination" />
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑员工' : '新增员工'" width="520px" class="form-dialog">
      <el-form :model="form" label-width="80px" class="dialog-form">
        <el-form-item label="姓名"><el-input v-model="form.name" placeholder="请输入姓名" /></el-form-item>
        <el-form-item label="工号"><el-input v-model="form.code" placeholder="请输入工号" /></el-form-item>
        <el-form-item label="证件号"><el-input v-model="form.idCard" placeholder="请输入证件号" /></el-form-item>
        <el-form-item label="部门"><el-select v-model="form.deptId" placeholder="请选择部门" style="width:100%"><el-option v-for="d in depts" :key="d.id" :label="d.name" :value="d.id" /></el-select></el-form-item>
        <el-form-item label="岗位等级"><el-select v-model="form.postLevelId" placeholder="请选择岗位等级" style="width:100%"><el-option v-for="p in postLevels" :key="p.id" :label="p.name" :value="p.id" /></el-select></el-form-item>
        <el-form-item label="电话"><el-input v-model="form.mobile" placeholder="请输入电话" /></el-form-item>
        <el-form-item label="邮箱"><el-input v-model="form.email" placeholder="请输入邮箱" /></el-form-item>
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
import { empPaged, empSave, empDelete, empGetById } from '../../api/emp'
import { deptListAll } from '../../api/dept'
import { postLevelListAll } from '../../api/postLevel'
import { ElMessage } from 'element-plus'

const filters = ref({ name: '', deptId: null })
const page = ref({ pageIndex: 1, pageSize: 10 })
const list = ref([])
const total = ref(0)
const depts = ref([])
const postLevels = ref([])
const dialogVisible = ref(false)
const form = ref({})

onMounted(() => {
  deptListAll().then(r => { if (r.code === 200) depts.value = r.data })
  postLevelListAll().then(r => { if (r.code === 200) postLevels.value = r.data })
  load()
})

async function load() {
  const res = await empPaged({ ...page.value, filters: { ...filters.value } })
  if (res.code === 200) { list.value = res.data.list; total.value = res.data.total }
}

async function openDialog(row) {
  form.value = row ? { ...await empGetById(row.id).then(r => r.data || row) } : { name: '', code: '', deptId: null, postLevelId: null, mobile: '', email: '' }
  dialogVisible.value = true
}

async function handleSave() {
  const res = await empSave(form.value)
  if (res.code === 200) { ElMessage.success('保存成功'); dialogVisible.value = false; load() }
  else ElMessage.error(res.msg)
}

async function handleDelete(id) {
  const res = await empDelete(id)
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
.search-select { width: 160px; }
.search-btn { border-radius: 6px; }
.add-btn { border-radius: 6px; }
.data-table { border-radius: 8px; }
.op-btn { padding: 4px 8px; }
.page-pagination { margin-top: 20px; }
</style>