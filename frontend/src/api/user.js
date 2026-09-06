import request from '../utils/request'
export const myRoles = () => request.get('/users/me/roles')
export const applyAuthor = () => request.post('/users/me/apply-author')
export const updateMe = (data) => request.put('/users/me', data)
