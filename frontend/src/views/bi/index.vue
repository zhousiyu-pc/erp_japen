<template>
  <div class="bi-container">
    <el-tabs v-model="activeTab" class="bi-tabs">
      <el-tab-pane label="经营大盘看板" name="dashboard">
        <MasterDashboard />
      </el-tab-pane>
      <el-tab-pane label="多维度交叉分析" name="analysis">
        <MultiDimAnalysis />
      </el-tab-pane>
      <el-tab-pane label="AI 预测与策略" name="predictions">
        <AiPredictions />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import MasterDashboard from './components/MasterDashboard.vue'
import MultiDimAnalysis from './components/MultiDimAnalysis.vue'
import AiPredictions from './components/AiPredictions.vue'

const activeTab = ref('dashboard')
</script>

<style scoped lang="scss">
.bi-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  min-height: calc(100vh - 100px);
}
</style>
