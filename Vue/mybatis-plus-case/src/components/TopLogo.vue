<template>
  <div class="top-logo-container">
    <img src="../assets/logo.svg" alt="Mybatis-Plus-Case" class="logo">
    <div class="text">MyBatis-Plus-Case</div>
  </div>
</template>

<script>
export default {}
</script>

<style lang="scss" scope>
.top-logo-container {
  display: flex;
  justify-content: space-around;
  width: 21.875rem;
  margin: 0 0 1.25rem 2.5rem;
  .logo {
    height: 2.5rem;
    width: 2.5rem;
  }
  .text {
    line-height: 2.5rem;
    font-size: 1.875rem;
    color: #37393b;
  }
}
</style>