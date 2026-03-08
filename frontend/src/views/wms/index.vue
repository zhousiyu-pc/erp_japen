<template>
  <div class="wms-container">
    <el-tabs v-model="activeTab" class="wms-tabs">
      <el-tab-pane label="库存查询与管理" name="inventory">
        <InventoryList />
      </el-tab-pane>
      <el-tab-pane label="出入库作业" name="operations">
        <WarehouseOperation />
      </el-tab-pane>
      <el-tab-pane label="库存分析与预警" name="analysis">
        <StockAnalysis />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import InventoryList from './components/InventoryList.vue'
import WarehouseOperation from './components/WarehouseOperation.vue'
import StockAnalysis from './components/StockAnalysis.vue'

const activeTab = ref('inventory')
</script>

<style scoped lang="scss">
.wms-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  min-height: calc(100vh - 100px);
}
</style>
