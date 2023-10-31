<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

* {
  margin: 0;
  padding: 0;
}

html {
  background-image: url('./assets/bg.jpg');
  background-size: 100%, 100%;
  background-repeat: no-repeat;
  background-attachment: fixed;
  padding: 1.25rem 0;
}
</style>
