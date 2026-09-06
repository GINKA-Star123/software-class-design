import request from '../utils/request'
export const adminUsers = () => request.get('/admin/users')
export const updateUserStatus = (id, status) => request.put('/admin/users/' + id + '/status', null, { params: { status } })
export const updateUserRoles = (id, roleIds) => request.put('/admin/users/' + id + '/roles', { roleIds })
export const adminStories = (status) => request.get('/admin/stories', { params: status == null ? {} : { status } })
export const dashboard = () => request.get('/stats/dashboard')
