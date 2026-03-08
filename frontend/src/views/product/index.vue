<template>
  <div class="product-container">
    <el-tabs v-model="activeTab" class="product-tabs">
      <el-tab-pane label="商品主数据 (PIM)" name="master">
        <ProductMaster />
      </el-tab-pane>
      <el-tab-pane label="多平台 Listing" name="listing">
        <ProductListing />
      </el-tab-pane>
      <el-tab-pane label="刊登模板" name="template">
        <ProductTemplate />
      </el-tab-pane>
      <el-tab-pane label="内容优化 (AI)" name="optimization">
        <ProductOptimization />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import ProductMaster from './components/ProductMaster.vue'
import ProductListing from './components/ProductListing.vue'
import ProductTemplate from './components/ProductTemplate.vue'
import ProductOptimization from './components/ProductOptimization.vue'

const activeTab = ref('master')
</script>

<style scoped lang="scss">
.product-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  min-height: calc(100vh - 100px);
}
</style>
