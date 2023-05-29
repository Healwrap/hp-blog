<template>
  <Alink :src="src" :content="`@${username}`" :isOuter="true"/>
</template>

<script setup>
import {ref} from 'vue'

const props = defineProps({
  userId: {
    type: String
  },
  username: {
    type: String
  }
})
const src = ref(import.meta.env.VITE_APP_URL + '/user/' + props.userId)
</script>

<style scoped></style>
