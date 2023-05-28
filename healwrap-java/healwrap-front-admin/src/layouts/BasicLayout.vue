<template>
  <layout>
    <router-view v-slot="{ Component }">
      <transition
        enter-active-class="animate animate__animated animate__fadeInLeft animate__faster"
        appear-active-class="animate animate__animated animate__fadeOutRight "
        mode="in-out"
      >
        <component :is="Component" />
      </transition>
    </router-view>
  </layout>
</template>

<script setup>
import Layout from '@/components/Layout/Layout.vue'
</script>

<style lang="scss" scoped>
.animate {
  position: relative;
  animation-duration: 0.5s;
}
</style>
