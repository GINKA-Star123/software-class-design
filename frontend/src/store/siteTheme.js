import { defineStore } from 'pinia'

export const SITE_THEMES = [
  { value: 'default', label: '默认', desc: '清爽浅色', css: 'linear-gradient(180deg, #f7f8fa, #eef2f7)' },
  { value: 'paper', label: '宣纸', desc: '暖白纸感', css: 'radial-gradient(circle at 18% 8%, #fffdf6, #f4efe3 58%, #ece5d6)' },
  { value: 'mist', label: '远山', desc: '青蓝远山', css: 'linear-gradient(160deg, #eef7fb, #dfeaf6 55%, #cddcee)' },
  { value: 'dusk', label: '暮色', desc: '紫蓝黄昏', css: 'linear-gradient(160deg, #f6f1fb, #e8e6f8 52%, #d7d9f2)' },
  { value: 'warm', label: '暖阳', desc: '暖橙夕阳', css: 'linear-gradient(160deg, #fff8ef, #ffe9d4 55%, #ffdcc0)' },
  { value: 'haze', label: '雾蓝', desc: '灰蓝雾面', css: 'linear-gradient(160deg, #f4f7fa, #e8eef4 55%, #dbe4ec)' }
]

function read(key, fallback = '') {
  try {
    return localStorage.getItem(key) ?? fallback
  } catch (e) {
    return fallback
  }
}

function readOpacity() {
  const value = Number(read('sw-site-bg-opacity', '0.32'))
  return Number.isFinite(value) ? value : 0.32
}

export const useSiteThemeStore = defineStore('siteTheme', {
  state: () => ({
    theme: read('sw-site-theme', 'default'),
    customBg: read('sw-site-bg', ''),
    opacity: readOpacity()
  }),
  getters: {
    preset(state) {
      return SITE_THEMES.find((item) => item.value === state.theme) || SITE_THEMES[0]
    }
  },
  actions: {
    apply() {
      const root = document.documentElement
      root.dataset.siteTheme = this.theme
      root.style.setProperty('--site-theme-bg', this.preset.css)
      root.style.setProperty('--site-bg-opacity', String(this.opacity))
      root.classList.toggle('has-custom-bg', !!this.customBg)
      if (this.customBg) {
        root.style.setProperty('--site-bg-image', `url("${this.customBg}")`)
      } else {
        root.style.removeProperty('--site-bg-image')
      }
    },
    persist() {
      try {
        localStorage.setItem('sw-site-theme', this.theme)
        localStorage.setItem('sw-site-bg', this.customBg)
        localStorage.setItem('sw-site-bg-opacity', String(this.opacity))
      } catch (e) {
        // 浏览器禁用本地存储时只影响持久化，不影响当前页面使用
      }
      this.apply()
    },
    setTheme(value) {
      this.theme = value
      this.persist()
    },
    setCustomBg(url) {
      this.customBg = url || ''
      this.persist()
    },
    setOpacity(value) {
      this.opacity = Number(value)
      this.persist()
    },
    reset() {
      this.theme = 'default'
      this.customBg = ''
      this.opacity = 0.32
      this.persist()
    }
  }
})
