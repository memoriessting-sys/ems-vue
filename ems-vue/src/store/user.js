import {defineStore} from 'pinia'
import {login as loginApi, getUserInfo as getUserInfoApi, logout as logoutApi} from '../api/user'
import router from '../router'

export const useUserStore = defineStore('user', {
    state: () => ({
        token: localStorage.getItem('token') || '',
        userId: null,
        account: '',
        name: '',
        roleType: null,
        permissions: []
    }),
    getters: {
        isLoggedIn: state => !!state.token,
        isAdmin: state => state.roleType === 3
    },
    actions: {
        async login(loginForm) {
            const res = await loginApi(loginForm)
            const data = res.data
            this.token = data.token
            this.userId = data.userId
            this.account = data.account
            this.name = data.name
            this.roleType = data.roleType
            this.permissions = data.permissions
            localStorage.setItem('token', data.token)
        },
        async getUserInfo() {
            const res = await getUserInfoApi()
            const data = res.data
            this.userId = data.userId
            this.account = data.account
            this.name = data.name
            this.roleType = data.roleType
            this.permissions = data.permissions
        },
        async logout() {
            try {
                await logoutApi()
            } catch (e) {
                // ignore
            }
            this.token = ''
            this.userId = null
            this.account = ''
            this.name = ''
            this.roleType = null
            this.permissions = []
            localStorage.removeItem('token')
            router.push('/login')
        },
        hasPermission(perm) {
            return this.permissions.includes(perm)
        }
    }
})
