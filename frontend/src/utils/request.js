import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const service = axios.create({
  baseURL: '/api',
  timeout: 15000,
  withCredentials: true
})

service.interceptors.response.use(
  (resp) => {
    const d = resp.data
    if (d && typeof d === 'object' && 'code' in d) {
      if (d.code === 200 || d.code === 0) return d.data
      if (d.code === 2006 || d.code === 401) {
        localStorage.removeItem('sw_user')
        if (router.currentRoute.value.name !== 'login') router.push({ name: 'login' })
      }
      ElMessage.error(d.message || '操作失败')
      return Promise.reject(new Error(d.message || '操作失败'))
    }
    return d
  },
  (err) => {
    if (err.response?.status === 401 || err.response?.status === 2006) {
      localStorage.removeItem('sw_user')
      router.push({ name: 'login' })
    }
    ElMessage.error(err.response?.data?.message || err.message || '网络错误')
    return Promise.reject(err)
  }
)
export default service
