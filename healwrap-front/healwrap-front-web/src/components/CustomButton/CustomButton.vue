<template>
  <div class="custom-button">
    <div v-if="name === 'postArticle'" class="post-article">
      <button type="button" class="btn">
        <strong class="iconfont icon-add"></strong><strong>&nbsp;发帖</strong>
        <div id="container-stars">
          <div id="stars"></div>
        </div>

        <div id="glow">
          <div class="circle"></div>
          <div class="circle"></div>
        </div>
      </button>
    </div>
    <div v-if="name === 'loginAndRegister'" class="login-register">
      <div class="buttons">
        <button class="btn">
          <span></span>
          <p :data-title="config.text1" :data-text="config.text2"></p>
        </button>
      </div>
    </div>
    <div v-if="name === 'search'" class="search">
      <div class="button"><span class="iconfont icon-search"></span><span>&nbsp;搜索</span></div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  name: {
    type: String,
    default: 'postArticle'
  },
  config: {
    type: Object,
    default: () => ({})
  }
})
</script>

<style lang="scss" scoped>
@import '../../assets/scss/variables.scss';

.custom-button {
  display: flex;
  align-items: center;
  height: 100%;
  .post-article {
    .btn {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 110px;
      height: 37px;
      background-size: 300% 300%;
      backdrop-filter: blur(1rem);
      border-radius: 5rem;
      transition: 0.5s;
      animation: gradient_301 5s ease infinite;
      border: double 4px transparent;
      background-image: linear-gradient(#212121, #212121), linear-gradient(137.48deg, #ffdb3b 10%, #fe53bb 45%, #8f51ea 67%, #0044ff 87%);
      background-origin: border-box;
      background-clip: content-box, border-box;
      cursor: pointer;
    }

    #container-stars {
      position: fixed;
      z-index: -1;
      width: 100%;
      height: 100%;
      overflow: hidden;
      transition: 0.5s;
      backdrop-filter: blur(1rem);
      border-radius: 5rem;
    }

    strong {
      z-index: 2;
      font-family: 'Avalors Personal Use';
      font-size: 14px;
      letter-spacing: 5px;
      color: #ffffff;
      text-shadow: 0 0 4px white;
    }

    #glow {
      position: absolute;
      display: flex;
      width: 10px;
    }

    .circle {
      width: 100%;
      height: 30px;
      filter: blur(2rem);
      animation: pulse_3011 4s infinite;
      z-index: -1;
    }

    .circle:nth-of-type(1) {
      background: rgba(254, 83, 186, 0.636);
    }

    .circle:nth-of-type(2) {
      background: rgba(142, 81, 234, 0.704);
    }

    .btn:hover #container-stars {
      z-index: 1;
      background-color: #212121;
    }

    .btn:hover {
      transform: scale(1.1);
    }

    .btn:active {
      border: double 4px #fe53bb;
      background-origin: border-box;
      background-clip: content-box, border-box;
      animation: none;
    }

    .btn:active .circle {
      background: #fe53bb;
    }

    #stars {
      position: relative;
      background: transparent;
      width: 200rem;
      height: 200rem;
    }

    #stars::after {
      content: '';
      position: absolute;
      top: -10rem;
      left: -100rem;
      width: 100%;
      height: 100%;
      animation: animStarRotate 90s linear infinite;
    }

    #stars::after {
      background-image: radial-gradient(#ffffff 1px, transparent 1%);
      background-size: 50px 50px;
    }

    #stars::before {
      content: '';
      position: absolute;
      top: 0;
      left: -50%;
      width: 170%;
      height: 500%;
      animation: animStar 60s linear infinite;
    }

    #stars::before {
      background-image: radial-gradient(#ffffff 1px, transparent 1%);
      background-size: 50px 50px;
      opacity: 0.5;
    }

    @keyframes animStar {
      from {
        transform: translateY(0);
      }

      to {
        transform: translateY(-135rem);
      }
    }

    @keyframes animStarRotate {
      from {
        transform: rotate(360deg);
      }

      to {
        transform: rotate(0);
      }
    }

    @keyframes gradient_301 {
      0% {
        background-position: 0% 50%;
      }

      50% {
        background-position: 100% 50%;
      }

      100% {
        background-position: 0% 50%;
      }
    }

    @keyframes pulse_3011 {
      0% {
        transform: scale(0.75);
        box-shadow: 0 0 0 0 rgba(0, 0, 0, 0.7);
      }

      70% {
        transform: scale(0.85);
        box-shadow: 0 0 0 10px rgba(0, 0, 0, 0);
      }

      100% {
        transform: scale(0.75);
        box-shadow: 0 0 0 0 rgba(0, 0, 0, 0);
      }
    }
  }

  .login-register {
    .buttons {
      display: flex;
      justify-content: space-around;
      top: 20px;
      left: 20px;
    }

    .buttons button {
      width: 100px;
      height: 45px;
      background: transparent;
      color: #000000;
      position: relative;
      overflow: hidden;
      font-size: 14px;
      letter-spacing: 1px;
      font-weight: 500;
      text-transform: uppercase;
      transition: all 0.3s ease;
      cursor: pointer;
      border: 0;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .buttons button:after {
      content: '';
      position: absolute;
      width: 0;
      height: 3px;
      background-color: $color-primary;
      transition: all 0.3s cubic-bezier(0.35, 0.1, 0.25, 1);
    }

    .buttons button:before {
      right: 0;
      top: 0;
      transition: all 0.5s cubic-bezier(0.35, 0.1, 0.25, 1);
    }

    .buttons button:after {
      left: 0;
      bottom: 0;
    }

    .buttons button span {
      width: 100%;
      height: 100%;
      position: absolute;
      left: 0;
      top: 0;
      margin: 0;
      padding: 0;
      z-index: 1;
    }

    .buttons button span:before {
      right: 0;
      top: 0;
      transition: all 0.5s cubic-bezier(0.35, 0.1, 0.25, 1);
    }

    .buttons button span:after {
      left: 0;
      bottom: 0;
    }

    .buttons button p {
      padding: 0;
      margin: 0;
      transition: all 0.4s cubic-bezier(0.35, 0.1, 0.25, 1);
      position: absolute;
      width: 100%;
      height: 100%;
    }

    .buttons button p:before,
    .buttons button p:after {
      position: absolute;
      width: 100%;
      transition: all 0.4s cubic-bezier(0.35, 0.1, 0.25, 1);
      z-index: 1;
      left: 0;
    }

    .buttons button p:before {
      content: attr(data-title);
      top: 50%;
      transform: translateY(-50%);
    }

    .buttons button p:after {
      content: attr(data-text);
      top: 150%;
      color: $color-primary;
    }

    .buttons button:hover:before,
    .buttons button:hover:after {
      width: 100%;
    }

    .buttons button:hover span {
      z-index: 1;
    }

    .buttons button:hover span:before,
    .buttons button:hover span:after {
      height: 100%;
    }

    .buttons button:hover p:before {
      top: -50%;
      transform: rotate(5deg);
    }

    .buttons button:hover p:after {
      top: 50%;
      transform: translateY(-50%);
    }

    .buttons button.start {
      background-color: $color-primary;
      box-shadow: 0px 5px 10px -10px rgba(0, 0, 0, 0.2);
      transition: all 0.2s ease;
    }

    .buttons button.start p:before {
      top: -50%;
      transform: rotate(5deg);
    }

    .buttons button.start p:after {
      color: white;
      transition: all 0s ease;
      content: attr(data-start);
      top: 50%;
      transform: translateY(-50%);
      animation: start 0.3s ease;
      animation-fill-mode: forwards;
    }

    @keyframes start {
      from {
        top: -50%;
      }
    }

    .buttons button.start:hover:before,
    .buttons button.start:hover:after {
      display: none;
    }

    .buttons button.start:hover span {
      display: none;
    }

    .buttons button:active {
      outline: none;
      border: none;
    }

    .buttons button:focus {
      outline: 0;
    }
  }

  .search {
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 0 5px;
    width: 100%;
    height: 100%;
    cursor: pointer;

    &:after {
      content: '';
      position: absolute;
      bottom: 0;
      width: 0;
      height: 3px;
      background-color: $color-primary;
      transition: all 0.3s cubic-bezier(0.35, 0.1, 0.25, 1);
    }

    &:hover:after {
      width: 100%;
    }

    .buttons {
      width: 100%;
      height: 100%;
    }
  }
}
</style>
