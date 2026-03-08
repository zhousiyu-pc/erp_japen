<template>
  <div class="ads-container">
    <el-tabs v-model="activeTab" class="ads-tabs">
      <el-tab-pane label="统一广告看板" name="dashboard">
        <AdDashboard />
      </el-tab-pane>
      <el-tab-pane label="广告活动管理" name="campaigns">
        <AdCampaigns />
      </el-tab-pane>
      <el-tab-pane label="自动化规则" name="rules">
        <AdRules />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import AdDashboard from './components/AdDashboard.vue'
import AdCampaigns from './components/AdCampaigns.vue'
import AdRules from './components/AdRules.vue'

const activeTab = ref('dashboard')
</script>

<style scoped lang="scss">
.ads-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  min-height: calc(100vh - 100px);
}
</style>
