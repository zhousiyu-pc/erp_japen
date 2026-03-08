<template>
  <div class="procurement-container">
    <el-tabs v-model="activeTab" class="procurement-tabs">
      <el-tab-pane label="智能采购计划" name="plan">
        <PurchasePlan />
      </el-tab-pane>
      <el-tab-pane label="采购单执行" name="order">
        <PurchaseOrder />
      </el-tab-pane>
      <el-tab-pane label="供应商管理" name="supplier">
        <SupplierManagement />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import PurchasePlan from './components/PurchasePlan.vue'
import PurchaseOrder from './components/PurchaseOrder.vue'
import SupplierManagement from './components/SupplierManagement.vue'

const activeTab = ref('plan')
</script>

<style scoped lang="scss">
.procurement-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  min-height: calc(100vh - 100px);
}
</style>
