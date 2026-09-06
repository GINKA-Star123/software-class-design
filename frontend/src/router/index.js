import { createRouter, createWebHistory } from 'vue-router'
import { loadUser } from '../utils/auth'

const routes = [
  { path: '/', component: () => import('../views/MainLayout.vue'), children: [
    { path: '', name: 'home', component: () => import('../views/HomeView.vue'), meta: { title: '首页' } },
    { path: 'stories', name: 'storyList', component: () => import('../views/StoryListView.vue'), meta: { title: '故事库' } },
    { path: 'stories/:id', name: 'storyDetail', component: () => import('../views/StoryDetailView.vue'), meta: { title: '故事详情' } },
    { path: 'rank', name: 'rank', component: () => import('../views/RankView.vue'), meta: { title: '排行榜' } },
    { path: 'play/:id', name: 'play', component: () => import('../views/PlayView.vue'), meta: { title: '游玩', requiresAuth: true } },
    { path: 'progress', name: 'progress', component: () => import('../views/ProgressView.vue'), meta: { title: '我的进度', requiresAuth: true } },
    { path: 'achievements', name: 'achievements', component: () => import('../views/AchievementView.vue'), meta: { title: '我的成就', requiresAuth: true } },
    { path: 'my-stories', name: 'myStories', component: () => import('../views/MyStoriesView.vue'), meta: { title: '创作中心', requiresAuth: true, roles: ['AUTHOR', 'ADMIN'] } },
    { path: 'editor/:id', name: 'editor', component: () => import('../views/EditorView.vue'), meta: { title: '故事编辑器', requiresAuth: true, roles: ['AUTHOR', 'ADMIN'] } },
    { path: 'audit', name: 'audit', component: () => import('../views/AuditView.vue'), meta: { title: '审核中心', requiresAuth: true, roles: ['AUDITOR', 'ADMIN'] } },
    { path: 'report', name: 'report', component: () => import('../views/ReportView.vue'), meta: { title: '举报处理', requiresAuth: true, roles: ['AUDITOR', 'ADMIN'] } },
    { path: 'admin', name: 'admin', component: () => import('../views/AdminView.vue'), meta: { title: '管理后台', requiresAuth: true, roles: ['ADMIN'] } }
  ]},
  { path: '/login', name: 'login', component: () => import('../views/LoginView.vue'), meta: { title: '登录' } },
  { path: '/register', name: 'register', component: () => import('../views/RegisterView.vue'), meta: { title: '注册' } }
]

const router = createRouter({ history: createWebHistory(), routes })
router.beforeEach((to) => {
  document.title = (to.meta.title ? to.meta.title + ' · ' : '') + '抉择工坊'
  const user = loadUser()
  if (to.meta.requiresAuth && !user) return { name: 'login', query: { redirect: to.fullPath } }
  if (to.meta.roles && !((user?.roles || []).some(r => to.meta.roles.includes(r)))) return { name: 'home' }
  return true
})
export default router
