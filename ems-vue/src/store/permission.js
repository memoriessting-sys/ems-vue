import {defineStore} from 'pinia'

export const usePermissionStore = defineStore('permission', {
    state: () => ({
        routes: [],
        isRoutesLoaded: false
    }),
    actions: {
        generateRoutes(businessRoutes, permissions) {
            const filtered = filterRoutes(businessRoutes, permissions)
            this.routes = filtered
            this.isRoutesLoaded = true
            return filtered
        },
        resetRoutes() {
            this.routes = []
            this.isRoutesLoaded = false
        }
    }
})

function filterRoutes(routes, permissions) {
    return routes.filter(route => {
        const required = route.meta?.permissions || []
        if (required.length === 0) return true
        const hasAccess = required.some(p => permissions.includes(p))
        if (hasAccess && route.children) {
            route.children = filterRoutes(route.children, permissions)
        }
        return hasAccess
    })
}
