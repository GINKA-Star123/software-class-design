import request from '../utils/request'
export const home = () => request.get('/home')
export const listStories = (params) => request.get('/stories', { params })
export const getStory = (id) => request.get('/stories/' + id)
export const rank = (limit = 20) => request.get('/stats/rank', { params: { limit } })
