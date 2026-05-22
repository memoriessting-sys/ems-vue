<template>
  <el-card shadow="hover" class="page-card">
    <template #header>
      <div class="page-header"><span class="page-title">用户管理</span></div>
    </template>
    <div class="toolbar">
      <div class="toolbar-left">
        <el-input v-model="filters.account" placeholder="搜索账号" clearable class="search-input" @clear="load">
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
        <el-button type="primary" @click="load" class="search-btn"><el-icon><Search /></el-icon>查询</el-button>
      </div>
      <el-button type="success" @click="openDialog()" class="add-btn"><el-icon><Plus /></el-icon>新增用户</el-button>
    </div>
    <el-table :data="list" border stripe class="data-table" :header-cell-style="{background:'#f5f7fa',color:'#606266'}">
      <el-table-column prop="id" label="ID" width="70" align="center" />
      <el-table-column prop="account" label="账号" />
      <el-table-column prop="userName" label="用户名" />
      <el-table-column prop="roleTypeName" label="角色" width="120" align="center">
        <template #default="{ row }">
          <el-tag :type="roleTagType(row.roleType)" size="small" effect="light">{{ row.roleTypeName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220" align="center">
        <template #default="{ row }">
          <el-button link type="primary" @click="openDialog(row)" class="op-btn"><el-icon><Edit /></el-icon>编辑</el-button>
          <el-button link type="warning" @click="handleResetPwd(row.id)" class="op-btn"><el-icon><RefreshRight /></el-icon>重置密码</el-button>
          <el-popconfirm title="确认删除该用户?" @confirm="handleDelete(row.id)">
            <template #reference><el-button link type="danger" class="op-btn"><el-icon><Delete /></el-icon>删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination background layout="total, sizes, prev, pager, next" :total="total" :page-sizes="[10,20,50]" :page-size="page.pageSize" v-model:current-page="page.pageIndex" @current-change="load" @size-change="load" class="page-pagination" />
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑用户' : '新增用户'" width="520px" class="form-dialog">
      <el-form :model="form" label-width="80px" class="dialog-form">
        <el-form-item label="账号"><el-input v-model="form.account" :disabled="!!form.id" placeholder="请输入账号" /></el-form-item>
        <el-form-item label="用户名"><el-input v-model="form.name" placeholder="请输入用户名" /></el-form-item>
        <el-form-item v-if="!form.id" label="密码"><el-input v-model="form.password" type="password" placeholder="请输入密码" show-password /></el-form-item>
        <el-form-item label="角色"><el-select v-model="form.roleType" placeholder="请选择角色" style="width:100%"><el-option label="普通用户" :value="1" /><el-option label="部门主管" :value="2" /><el-option label="系统管理员" :value="3" /></el-select></el-form-item>
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
import { userPaged, userSave, userDelete, resetPwd } from '../../api/user'
import { ElMessage } from 'element-plus'

const filters = ref({ account: '' })
const page = ref({ pageIndex: 1, pageSize: 10 })
const list = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const form = ref({})

function roleTagType(roleType) {
  if (roleType === 3) return 'danger'
  if (roleType === 2) return 'warning'
  return ''
}

onMounted(() => load())

async function load() {
  const res = await userPaged({ ...page.value, filters: { ...filters.value } })
  if (res.code === 200) { list.value = res.data.list; total.value = res.data.total }
}

function openDialog(row) {
  form.value = row ? { ...row } : { account: '', name: '', password: '', roleType: 1 }
  dialogVisible.value = true
}

async function handleSave() {
  const res = await userSave(form.value)
  if (res.code === 200) { ElMessage.success('保存成功'); dialogVisible.value = false; load() }
  else ElMessage.error(res.msg)
}

async function handleDelete(id) {
  const res = await userDelete(id)
  if (res.code === 200) { ElMessage.success('删除成功'); load() }
  else ElMessage.error(res.msg)
}

async function handleResetPwd(id) {
  const res = await resetPwd(id)
  if (res.code === 200) ElMessage.success('密码已重置为123456')
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
.page-pagination { margin-top: 20px; }
</style>