<template>
  <el-card shadow="hover" class="page-card">
    <template #header>
      <div class="page-header"><span class="page-title">部门管理</span></div>
    </template>
    <div class="toolbar">
      <div class="toolbar-left">
        <el-input v-model="filters.name" placeholder="搜索部门名称" clearable class="search-input" @clear="load">
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
        <el-button type="primary" @click="load" class="search-btn"><el-icon><Search /></el-icon>查询</el-button>
      </div>
      <el-button type="success" @click="openDialog()" class="add-btn"><el-icon><Plus /></el-icon>新增部门</el-button>
    </div>
    <el-table :data="treeData" border row-key="id" default-expand-all class="data-table" :header-cell-style="{background:'#f5f7fa',color:'#606266'}" :tree-props="{children:'children',hasChildren:'hasChildren'}">
      <el-table-column prop="name" label="部门名称" />
      <el-table-column prop="code" label="部门编码" width="150" />
      <el-table-column label="操作" width="200" align="center">
        <template #default="{ row }">
          <el-button link type="primary" @click="openDialog(row)" class="op-btn"><el-icon><Edit /></el-icon>编辑</el-button>
          <el-popconfirm title="确认删除该部门?" @confirm="handleDelete(row.id)">
            <template #reference><el-button link type="danger" class="op-btn"><el-icon><Delete /></el-icon>删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑部门' : '新增部门'" width="520px" class="form-dialog">
      <el-form :model="form" label-width="80px" class="dialog-form">
        <el-form-item label="名称"><el-input v-model="form.name" placeholder="请输入部门名称" /></el-form-item>
        <el-form-item label="编码"><el-input v-model="form.code" placeholder="请输入部门编码" /></el-form-item>
        <el-form-item label="上级部门"><el-tree-select v-model="form.parentId" :data="treeData" :props="{label:'name',value:'id'}" check-strictly clearable placeholder="请选择上级部门" style="width:100%" /></el-form-item>
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
import { deptPaged, deptSave, deptDelete, deptTree } from '../../api/dept'
import { ElMessage } from 'element-plus'

const filters = ref({ name: '' })
const page = ref({ pageIndex: 1, pageSize: 100 })
const treeData = ref([])
const dialogVisible = ref(false)
const form = ref({})

onMounted(() => load())

async function load() {
  const [paged, tree] = await Promise.all([deptPaged({ ...page.value, filters: { ...filters.value } }), deptTree()])
  if (tree.code === 200) treeData.value = tree.data
}

async function openDialog(row) {
  form.value = row ? { ...row } : { name: '', code: '', parentId: null }
  dialogVisible.value = true
}

async function handleSave() {
  const res = await deptSave(form.value)
  if (res.code === 200) { ElMessage.success('保存成功'); dialogVisible.value = false; load() }
  else ElMessage.error(res.msg)
}

async function handleDelete(id) {
  const res = await deptDelete(id)
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
.op-btn { padding: 4px 8px; }
</style>