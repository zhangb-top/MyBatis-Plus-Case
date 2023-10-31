<template>
  <div class="form-item-container">
    <head-title :name="name" :isRequired="isRequired"></head-title>
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <div v-for="(value,key) in form" :key="key">
        <el-form-item :label="value.chinese" :prop="key">
          <el-select v-if="value.type==='select'" clearable :placeholder="`请选择${value.chinese}`" @change="selectDBHandler" v-model="value.content">
            <el-option v-for="(item,index) in value.dbConfigList" :key="index" :label="item.db" :value="item.db">
            </el-option>
          </el-select>
          <el-input v-else-if="value.type==='textarea'" type="textarea" v-model="value.content" :placeholder="`请输入${value.chinese}`"></el-input>
          <el-switch v-else-if="value.type==='switch'" v-model="value.content" active-color="#13ce66" inactive-color="#ff4949">
          </el-switch>
          <el-radio-group v-else-if="value.type==='radio-group'" v-model="value.content">
            <el-radio v-for="(item,index) in value.radios" :key="index" :label="item">{{item}}</el-radio>
          </el-radio-group>
          <el-input v-else v-model="value.content" clearable :placeholder="`请输入${value.chinese}`"></el-input>
          <slot v-if="value.hasSlot"></slot>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import HeadTitle from './HeadTitle.vue'

export default {
  components: { HeadTitle },
  props: {
    name: {
      type: String
    },
    isRequired: {
      type: Boolean
    },
    form: {
      type: Object,
      required: true
    },
    rules: {
      type: Object
    }
  },
  methods: {
    selectDBHandler(e) {
      this.$emit('selectDB', e)
    }
  }
}
</script>

<style lang="scss" scope>
.form-item-container {
  width: 100%;
  .el-select {
    width: 9.375rem;
  }
  button {
    position: absolute;
    right: 0;
  }
}
</style>