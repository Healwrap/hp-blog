<template>
  <div class="index">
    <Image class="animate__animated animate__fadeIn" :src="getAssetsFile('icon.png')" width="200px" fit="cover" />
    <button class="btn animate__animated animate__fadeInUp animate__delay-1s" @click="enterApp">进入HealWrap</button>
  </div>
</template>

<script setup>
import { getCurrentInstance } from 'vue'
import Image from '@/components/Image/Image.vue'
import { getAssetsFile } from '@/utils/Utils'

const { proxy } = getCurrentInstance()
proxy.$store.commit('enter_app', false)
proxy.$store.commit('content_width', '100vw')
const enterApp = () => {
  proxy.$router.push({ path: '/forum/10000' })
  proxy.$store.commit('enter_app', true)
  if (document.documentElement.clientWidth > 1100) {
    proxy.$store.commit('content_width', '1100px')
  }
}
</script>

<style lang="scss" scoped>
.index {
  position: relative;
  width: 100%;
  height: 100%;
  top: 20vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  .btn {
    width: 200px;
    height: 65px;
    margin: 0.5em;
    background: black;
    color: white;
    border: none;
    font-size: 20px;
    font-weight: bold;
    cursor: pointer;
    position: relative;
    z-index: 1;
    overflow: hidden;
  }

  button {
    &:hover {
      color: black;
    }

    &:after {
      content: '';
      background: white;
      position: absolute;
      z-index: -1;
      left: -20%;
      right: -20%;
      top: 0;
      bottom: 0;
      transform: skewX(-45deg) scale(0, 1);
      transition: all 0.5s;
    }

    &:hover:after {
      transform: skewX(-45deg) scale(1, 1);
      -webkit-transition: all 0.5s;
      transition: all 0.5s;
    }
  }
}
</style>
