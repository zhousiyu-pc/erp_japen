import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string>(localStorage.getItem('erp_token') || '')
  const userInfo = ref<{ username: string; realName?: string } | null>(null)

  const isLoggedIn = () => !!token.value

  const setToken = (t: string) => {
    token.value = t
    localStorage.setItem('erp_token', t)
  }

  const setUserInfo = (info: { username: string; realName?: string }) => {
    userInfo.value = info
    localStorage.setItem('erp_user', JSON.stringify(info))
  }

  const logout = () => {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('erp_token')
    localStorage.removeItem('erp_user')
  }

  const initFromStorage = () => {
    const u = localStorage.getItem('erp_user')
    if (u) userInfo.value = JSON.parse(u)
  }

  return { token, userInfo, isLoggedIn, setToken, setUserInfo, logout, initFromStorage }
})
