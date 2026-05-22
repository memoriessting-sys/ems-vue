import {useUserStore} from '../store/user'

export default {
    mounted(el, binding) {
        const userStore = useUserStore()
        const required = binding.value
        if (!required) return
        if (!userStore.permissions.includes(required)) {
            el.parentNode && el.parentNode.removeChild(el)
        }
    }
}
