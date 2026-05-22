import {useUserStore} from '../store/user'
import {usePermissionStore} from '../store/permission'
import {businessRoutes} from './routes'
import router from './index'

const whiteList = ['/login', '/forgot-password']

router.beforeEach(async (to, from, next) => {
    const userStore = useUserStore()
    const permissionStore = usePermissionStore()

    if (userStore.isLoggedIn) {
        if (to.path === '/login') {
            next({path: '/'})
        } else {
            if (permissionStore.isRoutesLoaded) {
                next()
            } else {
                try {
                    await userStore.getUserInfo()
                    const accessRoutes = permissionStore.generateRoutes(businessRoutes, userStore.permissions)
                    accessRoutes.forEach(route => {
                        router.addRoute(route)
                    })
                    next({...to, replace: true})
                } catch (error) {
                    await userStore.logout()
                    next(`/login?redirect=${to.path}`)
                }
            }
        }
    } else {
        if (whiteList.includes(to.path)) {
            next()
        } else {
            next(`/login?redirect=${to.path}`)
        }
    }
})
