export function saveUser(user) {
  localStorage.setItem('sw_user', JSON.stringify(user || null))
}
export function loadUser() {
  try {
    return JSON.parse(localStorage.getItem('sw_user') || 'null')
  } catch (e) { return null }
}
export function clearUser() {
  localStorage.removeItem('sw_user')
}
