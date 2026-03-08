<template>
  <div class="order-container">
    <el-tabs v-model="activeTab" class="order-tabs">
      <el-tab-pane label="订单统一视图" name="list">
        <OrderList />
      </el-tab-pane>
      <el-tab-pane label="售后与异常" name="after-sales">
        <AfterSales />
      </el-tab-pane>
      <el-tab-pane label="自动规则引擎" name="rules">
        <OrderRules />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import OrderList from './components/OrderList.vue'
import AfterSales from './components/AfterSales.vue'
import OrderRules from './components/OrderRules.vue'

const activeTab = ref('list')
</script>

<style scoped lang="scss">
.order-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  min-height: calc(100vh - 100px);
}
</style>
