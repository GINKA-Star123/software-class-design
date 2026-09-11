export const MAX_IMAGE_SIZE = 5 * 1024 * 1024
export const ALLOWED_IMAGE_EXT = ['jpg', 'jpeg', 'png', 'gif', 'webp']

export function formatFileSize(bytes) {
  if (!Number.isFinite(bytes) || bytes <= 0) return '0KB'
  if (bytes >= 1024 * 1024) return `${(bytes / 1024 / 1024).toFixed(1)}MB`
  return `${Math.ceil(bytes / 1024)}KB`
}

export function validateImageFile(file) {
  if (!file) return { ok: false, message: '请选择图片文件' }

  const name = file.name || ''
  const ext = name.includes('.') ? name.split('.').pop().toLowerCase() : ''
  if (!ALLOWED_IMAGE_EXT.includes(ext)) {
    return { ok: false, message: '仅支持 jpg/jpeg/png/gif/webp 图片' }
  }

  if (file.size > MAX_IMAGE_SIZE) {
    return {
      ok: false,
      message: `图片不能超过 5MB，当前 ${formatFileSize(file.size)}，请压缩后再上传`
    }
  }

  return { ok: true }
}
