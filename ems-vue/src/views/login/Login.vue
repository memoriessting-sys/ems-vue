<template>
  <div class="login-wrap">
    <div class="login-bg-shapes">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
    </div>
    <el-card class="login-card" shadow="always">
      <div class="login-header">
        <div class="login-logo">
          <el-icon :size="40" color="#fff"><OfficeBuilding /></el-icon>
        </div>
        <h2 class="login-title">EMS 员工管理系统</h2>
        <p class="login-subtitle">Enterprise Management System</p>
      </div>
      <div class="tab-bar">
        <div :class="['tab-item', { active: mode === 'login' }]" @click="mode = 'login'">登录</div>
        <div :class="['tab-item', { active: mode === 'register' }]" @click="mode = 'register'">注册</div>
        <div class="tab-indicator" :style="{ left: mode === 'login' ? '0' : '50%' }"></div>
      </div>

      <!-- 登录表单 -->
      <el-form v-show="mode === 'login'" :model="loginForm" @keyup.enter="handleLogin" class="login-form">
        <el-form-item>
          <el-input v-model="loginForm.account" placeholder="请输入账号" :prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item>
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" :prefix-icon="Lock" show-password size="large" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" :loading="loginLoading" @click="handleLogin" class="login-btn">登 录</el-button>
        </el-form-item>
      </el-form>

      <!-- 注册表单 -->
      <el-form v-show="mode === 'register'" :model="regForm" :rules="regRules" ref="regFormRef" @keyup.enter="handleRegister" class="login-form">
        <el-form-item prop="account">
          <el-input v-model="regForm.account" placeholder="请输入账号" :prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="name">
          <el-input v-model="regForm.name" placeholder="请输入用户名" :prefix-icon="UserFilled" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="regForm.password" type="password" placeholder="请输入密码(至少6位)" :prefix-icon="Lock" show-password size="large" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" :loading="regLoading" @click="handleRegister" class="login-btn">注 册</el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer" v-if="mode === 'login'">默认账号: admin / 123456</div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../../store/user'
import { register } from '../../api/user'
import { User, UserFilled, Lock, OfficeBuilding } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const mode = ref('login')
const router = useRouter()
const route = useRoute()
const store = useUserStore()

// 登录
const loginForm = ref({ account: '', password: '' })
const loginLoading = ref(false)

async function handleLogin() {
  if (!loginForm.value.account || !loginForm.value.password) return ElMessage.warning('请输入账号和密码')
  loginLoading.value = true
  try {
    await store.login(loginForm.value)
    ElMessage.success('登录成功')
    const redirect = route.query.redirect || '/'
    router.push(redirect)
  } catch (e) {
    // Error already handled by request interceptor
  } finally {
    loginLoading.value = false
  }
}

// 注册
const regFormRef = ref(null)
const regForm = ref({ account: '', name: '', password: '' })
const regLoading = ref(false)
const regRules = {
  account: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '密码至少6位', trigger: 'blur' }]
}

async function handleRegister() {
  const valid = await regFormRef.value?.validate().catch(() => false)
  if (!valid) return
  regLoading.value = true
  try {
    const res = await register(regForm.value)
    if (res.code === 200) {
      ElMessage.success('注册成功，请登录')
      loginForm.value.account = regForm.value.account
      loginForm.value.password = ''
      mode.value = 'login'
      regForm.value = { account: '', name: '', password: '' }
    } else {
      ElMessage.error(res.msg || '注册失败')
    }
  } finally {
    regLoading.value = false
  }
}
</script>

<style scoped>
.login-wrap {
  height: 100vh; display: flex; align-items: center; justify-content: center;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);
  position: relative; overflow: hidden;
}
.login-bg-shapes { position: absolute; inset: 0; }
.shape { position: absolute; border-radius: 50%; opacity: 0.15; animation: float 6s ease-in-out infinite; }
.shape-1 { width: 300px; height: 300px; background: #e94560; top: -80px; right: -60px; }
.shape-2 { width: 200px; height: 200px; background: #533483; bottom: -40px; left: -40px; animation-delay: 2s; }
.shape-3 { width: 150px; height: 150px; background: #409eff; top: 50%; left: 60%; animation-delay: 4s; }
@keyframes float {
  0%, 100% { transform: translateY(0) scale(1); }
  50% { transform: translateY(-20px) scale(1.05); }
}
.login-card {
  width: 420px; border-radius: 16px !important;
  background: rgba(255,255,255,0.95); backdrop-filter: blur(10px);
  border: none !important; overflow: hidden;
}
.login-header {
  background: linear-gradient(135deg, #1a1a2e, #0f3460);
  padding: 30px 20px 20px; text-align: center;
}
.login-logo {
  width: 64px; height: 64px; border-radius: 16px;
  background: linear-gradient(135deg, #409eff, #533483);
  display: flex; align-items: center; justify-content: center;
  margin: 0 auto 12px;
}
.login-title { color: #fff; font-size: 22px; margin-bottom: 4px; }
.login-subtitle { color: rgba(255,255,255,0.6); font-size: 13px; }

/* Tab 切换 */
.tab-bar {
  display: flex; position: relative; margin: 20px 24px 0;
  border-bottom: 2px solid #e8e8e8;
}
.tab-item {
  flex: 1; text-align: center; padding: 10px 0; font-size: 15px;
  color: #909399; cursor: pointer; transition: color 0.3s; font-weight: 500;
  position: relative; z-index: 1;
}
.tab-item.active { color: #1a1a2e; font-weight: 600; }
.tab-indicator {
  position: absolute; bottom: -2px; width: 50%; height: 2px;
  background: linear-gradient(135deg, #1a1a2e, #0f3460);
  transition: left 0.3s ease;
}

.login-form { padding: 20px 24px 8px; }
.login-btn {
  width: 100%; border-radius: 8px; font-size: 16px;
  background: linear-gradient(135deg, #1a1a2e, #0f3460);
  letter-spacing: 4px;
}
.login-btn:hover { background: linear-gradient(135deg, #0f3460, #533483); }
.login-footer { text-align: center; color: #999; font-size: 12px; padding: 8px 24px 20px; }
</style>