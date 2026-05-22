import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getUserInfo, logout as logoutApi } from '../api/user'

export const useUserStore = defineStore('user', () => {
  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))

  function setUser(u) {
    user.value = u
    localStorage.setItem('user', JSON.stringify(u))
  }

  async function fetchUser() {
    const res = await getUserInfo()
    if (res.code === 200) setUser(res.data)
  }

  async function logout() {
    await logoutApi()
    user.value = null
    localStorage.removeItem('user')
  }

  function clear() {
    user.value = null
    localStorage.removeItem('user')
  }

  return { user, setUser, fetchUser, logout, clear }
})
