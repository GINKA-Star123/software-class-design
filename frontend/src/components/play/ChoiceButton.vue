<template>
  <button class="choice" :class="{ locked: !choice.available }" :disabled="!choice.available || busy" @click="$emit('pick', choice)">
    <span>{{ choice.choiceText }}</span>
    <small v-if="!choice.available">{{ choice.lockReason }}</small>
  </button>
</template>
<script setup>
defineProps({ choice: Object, busy: Boolean })
defineEmits(['pick'])
</script>
<style scoped>
.choice { display:flex; flex-direction:column; gap:4px; width:100%; text-align:left; border:1px solid #cbd5e0; border-radius:12px; padding:14px 16px; background:#fff; font-size:16px; cursor:pointer; }
.choice:hover:not(.locked) { border-color:#2b6cb0; box-shadow:0 4px 12px rgba(43,108,176,.14); }
.choice.locked { background:#f7fafc; color:#a0aec0; cursor:not-allowed; }
.choice small { color:#c53030; font-size:12px; }
</style>
