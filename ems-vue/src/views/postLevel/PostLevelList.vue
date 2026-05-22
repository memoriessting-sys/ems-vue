<template>
  <el-card shadow="hover" class="page-card">
    <template #header>
      <div class="page-header"><span class="page-title">岗位等级管理</span></div>
    </template>
    <div class="toolbar">
      <div class="toolbar-left">
        <el-input v-model="filters.name" placeholder="搜索岗位名称" clearable class="search-input" @clear="load">
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
        <el-button type="primary" @click="load" class="search-btn"><el-icon><Search /></el-icon>查询</el-button>
      </div>
      <el-button type="success" @click="openDialog()" v-permission="'postLevel:add'" class="add-btn"><el-icon><Plus /></el-icon>新增岗位等级</el-button>
    </div>
    <el-table :data="list" border stripe class="data-table" :header-cell-style="{background:'#f5f7fa',color:'#606266'}">
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="level" label="岗级" width="80" align="center" />
      <el-table-column prop="salary" label="岗位薪资" width="120" align="right">
        <template #default="{ row }">
          <span class="salary-text">{{ row.salary?.toLocaleString() }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center">
        <template #default="{ row }">
          <el-button link type="primary" @click="openDialog(row)" v-permission="'postLevel:edit'" class="op-btn"><el-icon><Edit /></el-icon>编辑</el-button>
          <el-popconfirm title="确认删除?" @confirm="handleDelete(row.id)">
            <template #reference><el-button link type="danger" v-permission="'postLevel:delete'" class="op-btn"><el-icon><Delete /></el-icon>删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination background layout="total, sizes, prev, pager, next" :total="total" :page-sizes="[10,20,50]" :page-size="page.pageSize" v-model:current-page="page.pageIndex" @current-change="load" @size-change="load" class="page-pagination" />
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑岗位等级' : '新增岗位等级'" width="520px" class="form-dialog">
      <el-form :model="form" label-width="80px" class="dialog-form">
        <el-form-item label="名称"><el-input v-model="form.name" placeholder="请输入名称" /></el-form-item>
        <el-form-item label="岗级"><el-input-number v-model="form.level" :min="1" style="width:100%" /></el-form-item>
        <el-form-item label="岗位薪资"><el-input-number v-model="form.salary" :min="0" :precision="2" style="width:100%" /></el-form-item>
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
import { postLevelPaged, postLevelSave, postLevelDelete, postLevelGetById } from '../../api/postLevel'
import { ElMessage } from 'element-plus'

const filters = ref({ name: '' })
const page = ref({ pageIndex: 1, pageSize: 10 })
const list = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const form = ref({})

onMounted(() => load())

async function load() {
  const res = await postLevelPaged({ ...page.value, filters: { ...filters.value } })
  if (res.code === 200) { list.value = res.data.list; total.value = res.data.total }
}

async function openDialog(row) {
  form.value = row ? { ...await postLevelGetById(row.id).then(r => r.data || row) } : { name: '', level: 1, salary: 0 }
  dialogVisible.value = true
}

async function handleSave() {
  const res = await postLevelSave(form.value)
  if (res.code === 200) { ElMessage.success('保存成功'); dialogVisible.value = false; load() }
  else ElMessage.error(res.msg)
}

async function handleDelete(id) {
  const res = await postLevelDelete(id)
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
.search-btn { border-radius: 6px; }
.add-btn { border-radius: 6px; }
.data-table { border-radius: 8px; }
.salary-text { color: #e6a23c; font-weight: 600; }
.op-btn { padding: 4px 8px; }
.page-pagination { margin-top: 20px; }
</style>