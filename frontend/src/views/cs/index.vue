<template>
  <div class="cs-container">
    <el-tabs v-model="activeTab" class="cs-tabs">
      <el-tab-pane label="多平台消息聚合" name="messages">
        <MessageCenter />
      </el-tab-pane>
      <el-tab-pane label="评价与 Review" name="reviews">
        <ReviewManagement />
      </el-tab-pane>
      <el-tab-pane label="工单与协作" name="tickets">
        <TicketSystem />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import MessageCenter from './components/MessageCenter.vue'
import ReviewManagement from './components/ReviewManagement.vue'
import TicketSystem from './components/TicketSystem.vue'

const activeTab = ref('messages')
</script>

<style scoped lang="scss">
.cs-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  min-height: calc(100vh - 100px);
}
</style>
