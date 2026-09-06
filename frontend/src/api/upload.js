import request from '../utils/request'
export const uploadCover = (file) => {
  const form = new FormData()
  form.append('file', file)
  form.append('type', 'covers')
  return request.post('/upload', form)
}
