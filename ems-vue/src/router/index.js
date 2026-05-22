import {createRouter, createWebHistory} from 'vue-router'

const Login = () => import('../views/login/Login.vue')

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: {title: '登录'}
    }
]

const router = createRouter({
    history: createWebHistory('/ems/'),
    routes
})

import './guard'

export default router
