import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../store/user'

const routes = [
  { path: '/login', component: () => import('../views/login/Login.vue') },
  {
    path: '/',
    component: () => import('../layout/Layout.vue'),
    redirect: '/home',
    children: [
      { path: 'home', component: () => import('../views/home/Home.vue') },
      { path: 'emp', component: () => import('../views/emp/EmpList.vue') },
      { path: 'dept', component: () => import('../views/dept/DeptList.vue') },
      { path: 'dict', component: () => import('../views/dict/DictList.vue') },
      { path: 'postLevel', component: () => import('../views/postLevel/PostLevelList.vue') },
      { path: 'attendance', component: () => import('../views/attendance/AttendanceList.vue') },
      { path: 'salary', component: () => import('../views/salary/SalaryList.vue') },
      { path: 'leave', component: () => import('../views/leave/LeaveList.vue') },
      { path: 'chart', component: () => import('../views/chart/Chart.vue') },
      { path: 'user', component: () => import('../views/user/UserList.vue') }
    ]
  }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  const store = useUserStore()
  if (to.path === '/login') return next()
  if (!store.user) return next('/login')
  next()
})

export default router
