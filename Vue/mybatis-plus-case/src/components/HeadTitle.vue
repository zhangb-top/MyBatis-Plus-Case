<template>
  <div class="head-title-container">
    <span v-if="isRequired">*</span>
    {{name}}
  </div>
</template>

<script>
export default {
  name: 'HeadTitle',
  props: {
    // 名称
    name: {
      type: String
    },
    // 是否必填
    isRequired: {
      type: Boolean
    }
  }
}
</script>

<style lang="scss" scope>
.head-title-container {
  position: relative;
  box-sizing: border-box;
  width: 100%;
  height: 2.5rem;
  line-height: 2.5rem;
  background-color: rgba(112, 161, 255, 0.4);
  color: #1e90ff;
  padding-left: 5%;
  margin-bottom: 1.25rem;
}
.head-title-container::before {
  content: '';
  display: block;
  position: absolute;
  left: 0;
  width: 2%;
  height: 100%;
  background-color: #686de0;
}
</style>