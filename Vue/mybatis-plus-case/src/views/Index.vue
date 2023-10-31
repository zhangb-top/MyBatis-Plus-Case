<template>
  <div class="index-container">
    <top-logo></top-logo>
    <main-card></main-card>
  </div>
</template>

<script>
import MainCard from '@/components/MainCard.vue'
import TopLogo from '@/components/TopLogo.vue'
export default {
  components: { MainCard, TopLogo },
  name: 'Index'
}
</script>