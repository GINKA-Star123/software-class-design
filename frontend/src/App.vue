<template>
  <router-view />
</template>

<script setup>
import { onMounted, watch } from 'vue'
import { useSiteThemeStore } from './store/siteTheme'

const siteTheme = useSiteThemeStore()

onMounted(() => siteTheme.apply())
watch(
  () => [siteTheme.theme, siteTheme.customBg, siteTheme.opacity],
  () => siteTheme.apply()
)
</script>
