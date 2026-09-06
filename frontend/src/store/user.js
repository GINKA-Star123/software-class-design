import { defineStore } from 'pinia'
import { login as apiLogin, logout as apiLogout } from '../api/auth'
import { saveUser, loadUser, clearUser } from '../utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({ user: loadUser() }),
  getters: {
    isLogin: (s) => !!s.user,
    roles: (s) => (s.user && s.user.roles) || [],
    isAuthor: (s) => rolesOf(s).includes('AUTHOR'),
    isAuditor: (s) => rolesOf(s).includes('AUDITOR'),
    isAdmin: (s) => rolesOf(s).includes('ADMIN')
  },
  actions: {
    async login(payload) {
      const data = await apiLogin(payload)
      this.user = data
      saveUser(data)
      return data
    },
    setUser(user) { this.user = user; saveUser(user) },
    async logout() {
      try { await apiLogout() } catch (e) { /* ignore */ }
      this.user = null
      clearUser()
    }
  }
})
function rolesOf(s) { return (s.user && s.user.roles) || [] }
