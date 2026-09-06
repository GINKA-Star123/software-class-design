import request from '../utils/request'
export const startPlay = (storyId) => request.post('/play/stories/' + storyId + '/start')
export const choose = (data) => request.post('/play/choose', data)
export const myProgress = () => request.get('/play/progress')
export const resetProgress = (progressId) => request.post('/play/progress/' + progressId + '/reset')
