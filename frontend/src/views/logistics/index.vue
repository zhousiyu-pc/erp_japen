<template>
  <div class="logistics-container">
    <el-tabs v-model="activeTab" class="logistics-tabs">
      <el-tab-pane label="打单与发货" name="shipping">
        <ShippingOperation />
      </el-tab-pane>
      <el-tab-pane label="物流商与渠道" name="carrier">
        <CarrierManagement />
      </el-tab-pane>
      <el-tab-pane label="时效与成本分析" name="analysis">
        <LogisticsAnalysis />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import ShippingOperation from './components/ShippingOperation.vue'
import CarrierManagement from './components/CarrierManagement.vue'
import LogisticsAnalysis from './components/LogisticsAnalysis.vue'

const activeTab = ref('shipping')
</script>

<style scoped lang="scss">
.logistics-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  min-height: calc(100vh - 100px);
}
</style>
