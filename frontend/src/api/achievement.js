import request from '../utils/request'
export const myAchievements = () => request.get('/achievements/mine')
