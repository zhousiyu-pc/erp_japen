<template>
  <div class="store-container">
    <el-tabs v-model="activeTab" class="store-tabs">
      <el-tab-pane label="店铺接入与授权" name="auth">
        <StoreAuth />
      </el-tab-pane>
      <el-tab-pane label="同步策略配置" name="sync">
        <SyncSettings />
      </el-tab-pane>
      <el-tab-pane label="多平台策略与分组" name="strategy">
        <PlatformStrategy />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import StoreAuth from './components/StoreAuth.vue'
import SyncSettings from './components/SyncSettings.vue'
import PlatformStrategy from './components/PlatformStrategy.vue'

const activeTab = ref('auth')
</script>

<style scoped lang="scss">
.store-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  min-height: calc(100vh - 100px);
}
</style>
