import request from '../utils/request'
export const interactStatus = (storyId) => request.get('/stories/' + storyId + '/interact/status')
export const toggleLike = (storyId) => request.post('/stories/' + storyId + '/like')
export const toggleFavorite = (storyId) => request.post('/stories/' + storyId + '/favorite')
export const listComments = (storyId, page = 1, pageSize = 20) =>
  request.get('/stories/' + storyId + '/comments', { params: { page, pageSize } })
export const addComment = (storyId, content, parentId = null) =>
  request.post('/stories/' + storyId + '/comments', { content, parentId })
export const reportStory = (storyId, reason) => request.post('/stories/' + storyId + '/report', { reason })
