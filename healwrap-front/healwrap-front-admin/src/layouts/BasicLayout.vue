<template>
  <layout>
    <router-view v-slot="{ Component }">
      <transition name="fade-transform" mode="out-in">
        <component :is="Component"/>
      </transition>
    </router-view>
  </layout>
</template>

<script setup>
import Layout from '@/components/Layout/Layout.vue'
</script>

<style lang="scss" scoped>

</style>
