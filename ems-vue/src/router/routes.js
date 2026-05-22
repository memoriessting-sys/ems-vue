import Layout from '../layout/Layout.vue'

const Home = () => import('../views/home/Home.vue')
const EmpList = () => import('../views/emp/EmpList.vue')
const DeptList = () => import('../views/dept/DeptList.vue')
const DictList = () => import('../views/dict/DictList.vue')
const PostLevelList = () => import('../views/postLevel/PostLevelList.vue')
const AttendanceList = () => import('../views/attendance/AttendanceList.vue')
const SalaryList = () => import('../views/salary/SalaryList.vue')
const LeaveList = () => import('../views/leave/LeaveList.vue')
const Chart = () => import('../views/chart/Chart.vue')
const UserList = () => import('../views/user/UserList.vue')

export const businessRoutes = [
    {
        path: '/',
        component: Layout,
        redirect: '/home',
        children: [
            {
                path: 'home',
                name: 'Home',
                component: Home,
                meta: {title: '首页概览', icon: 'HomeFilled', permissions: ['home']}
            },
            {
                path: 'emp',
                name: 'Emp',
                component: EmpList,
                meta: {title: '员工管理', icon: 'User', permissions: ['emp:list']}
            },
            {
                path: 'dept',
                name: 'Dept',
                component: DeptList,
                meta: {title: '部门管理', icon: 'OfficeBuilding', permissions: ['dept:list']}
            },
            {
                path: 'dict',
                name: 'Dict',
                component: DictList,
                meta: {title: '字典管理', icon: 'Collection', permissions: ['dict:list']}
            },
            {
                path: 'postLevel',
                name: 'PostLevel',
                component: PostLevelList,
                meta: {title: '岗位等级', icon: 'Stamp', permissions: ['postLevel:list']}
            },
            {
                path: 'attendance',
                name: 'Attendance',
                component: AttendanceList,
                meta: {title: '考勤管理', icon: 'Calendar', permissions: ['attendance:list']}
            },
            {
                path: 'salary',
                name: 'Salary',
                component: SalaryList,
                meta: {title: '薪资管理', icon: 'Money', permissions: ['salary:list']}
            },
            {
                path: 'leave',
                name: 'Leave',
                component: LeaveList,
                meta: {title: '请假管理', icon: 'Clock', permissions: ['leave:list']}
            },
            {
                path: 'chart',
                name: 'Chart',
                component: Chart,
                meta: {title: '数据统计', icon: 'DataAnalysis', permissions: ['chart:view']}
            },
            {
                path: 'user',
                name: 'User',
                component: UserList,
                meta: {title: '用户管理', icon: 'Setting', permissions: ['user:list']}
            }
        ]
    }
]
