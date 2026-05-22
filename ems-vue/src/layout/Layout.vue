<template>
  <el-container class="layout-wrap">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="layout-aside">
      <div class="aside-logo">
        <div class="logo-icon">
          <el-icon :size="24" color="#fff"><OfficeBuilding /></el-icon>
        </div>
        <span v-if="!isCollapse" class="logo-text">企业管理系统</span>
      </div>
      <el-menu :default-active="$route.path" router :collapse="isCollapse" class="aside-menu">
        <el-menu-item index="/home"><el-icon><HomeFilled /></el-icon><span>首页概览</span></el-menu-item>
        <el-menu-item index="/emp"><el-icon><User /></el-icon><span>员工管理</span></el-menu-item>
        <el-menu-item index="/dept"><el-icon><OfficeBuilding /></el-icon><span>部门管理</span></el-menu-item>
        <el-menu-item index="/dict"><el-icon><Collection /></el-icon><span>字典管理</span></el-menu-item>
        <el-menu-item index="/postLevel"><el-icon><Trophy /></el-icon><span>岗位等级</span></el-menu-item>
        <el-menu-item index="/attendance"><el-icon><Clock /></el-icon><span>考勤管理</span></el-menu-item>
        <el-menu-item index="/salary"><el-icon><Money /></el-icon><span>薪资管理</span></el-menu-item>
        <el-menu-item index="/leave"><el-icon><Document /></el-icon><span>请假管理</span></el-menu-item>
        <el-menu-item index="/chart"><el-icon><DataAnalysis /></el-icon><span>数据统计</span></el-menu-item>
        <el-menu-item v-if="isAdmin" index="/user"><el-icon><Setting /></el-icon><span>用户管理</span></el-menu-item>
      </el-menu>
    </el-aside>
    <el-container class="layout-main">
      <el-header class="layout-header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapse=!isCollapse"><Fold v-if="!isCollapse" /><Expand v-else /></el-icon>
          <span class="header-title">{{ currentPageTitle }}</span>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleCommand" trigger="click">
            <div class="user-info">
              <el-avatar :size="32" class="user-avatar">
                <el-icon><UserFilled /></el-icon>
              </el-avatar>
              <span class="user-name">{{ user?.userName || user?.name }}</span>
              <el-icon class="arrow-icon"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout" divided>
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="layout-content">
        <transition name="fade" mode="out-in">
          <router-view />
        </transition>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useUserStore } from '../store/user'
import { useRouter, useRoute } from 'vue-router'

const isCollapse = ref(false)
const store = useUserStore()
const router = useRouter()
const route = useRoute()
const user = computed(() => store.user)
const isAdmin = computed(() => store.user?.roleType === 3)

const titleMap = {
  '/home': '首页概览', '/emp': '员工管理', '/dept': '部门管理',
  '/dict': '字典管理', '/postLevel': '岗位等级', '/attendance': '考勤管理',
  '/salary': '薪资管理', '/leave': '请假管理', '/chart': '数据统计', '/user': '用户管理'
}
const currentPageTitle = computed(() => titleMap[route.path] || '企业管理系统')

async function handleCommand(cmd) {
  if (cmd === 'logout') {
    await store.logout()
    router.push('/login')
  }
}
</script>

<style scoped>
.layout-wrap { height: 100vh; }
.layout-aside {
  background: linear-gradient(180deg, #1a1a2e 0%, #16213e 100%);
  transition: width 0.3s ease; overflow: hidden;
  box-shadow: 2px 0 8px rgba(0,0,0,0.15);
}
.aside-logo {
  height: 60px; display: flex; align-items: center; justify-content: center;
  padding: 0 16px; gap: 10px; border-bottom: 1px solid rgba(255,255,255,0.1);
}
.logo-icon {
  width: 36px; height: 36px; border-radius: 10px;
  background: linear-gradient(135deg, #409eff, #533483);
  display: flex; align-items: center; justify-content: center; flex-shrink: 0;
}
.logo-text { color: #fff; font-size: 16px; font-weight: 600; white-space: nowrap; }
.aside-menu {
  background: transparent !important; border-right: none !important;
  --el-menu-bg-color: transparent;
  --el-menu-text-color: rgba(255,255,255,0.7);
  --el-menu-active-color: #fff;
  --el-menu-hover-bg-color: rgba(255,255,255,0.08);
}
.aside-menu .el-menu-item {
  margin: 4px 8px; border-radius: 8px; height: 44px; line-height: 44px;
}
.aside-menu .el-menu-item.is-active {
  background: linear-gradient(135deg, #409eff, #533483) !important;
  color: #fff !important;
}
.layout-main { background: #f0f2f5; }
.layout-header {
  background: #fff; height: 56px; display: flex;
  align-items: center; justify-content: space-between;
  padding: 0 20px; border-bottom: 1px solid #e8e8e8;
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);
}
.header-left { display: flex; align-items: center; gap: 12px; }
.collapse-btn { cursor: pointer; font-size: 20px; color: #606266; transition: color 0.2s; }
.collapse-btn:hover { color: #409eff; }
.header-title { font-size: 16px; font-weight: 600; color: #303133; }
.header-right { display: flex; align-items: center; }
.user-info { display: flex; align-items: center; gap: 8px; cursor: pointer; padding: 4px 8px; border-radius: 8px; transition: background 0.2s; }
.user-info:hover { background: #f5f7fa; }
.user-avatar { background: linear-gradient(135deg, #409eff, #533483); }
.user-name { font-size: 14px; color: #303133; font-weight: 500; }
.arrow-icon { font-size: 12px; color: #909399; }
.layout-content { padding: 20px; background: #f0f2f5; overflow-y: auto; }
</style>