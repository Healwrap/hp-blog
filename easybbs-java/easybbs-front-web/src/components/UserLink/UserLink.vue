<template>
  <router-link class="user-link" :to="src">@{{ username }}</router-link>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  userId: {
    type: String,
    required: true
  },
  username: {
    type: String,
    required: true
  }
})
const src = ref('/user/' + props.userId)
</script>

<style lang="scss" scoped>
.user-link {
  color: #409eff;
  text-decoration: none;
}
</style>
