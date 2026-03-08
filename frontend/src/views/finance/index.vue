<template>
  <div class="finance-container">
    <el-tabs v-model="activeTab" class="finance-tabs">
      <el-tab-pane label="结算与分摊" name="settlement">
        <Settlement />
      </el-tab-pane>
      <el-tab-pane label="利润分析" name="profit">
        <ProfitAnalysis />
      </el-tab-pane>
      <el-tab-pane label="税务与发票" name="tax">
        <TaxAndInvoice />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Settlement from './components/Settlement.vue'
import ProfitAnalysis from './components/ProfitAnalysis.vue'
import TaxAndInvoice from './components/TaxAndInvoice.vue'

const activeTab = ref('settlement')
</script>

<style scoped lang="scss">
.finance-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  min-height: calc(100vh - 100px);
}
</style>
