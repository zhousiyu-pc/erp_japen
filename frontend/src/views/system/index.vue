<template>
  <div class="system-container">
    <el-tabs v-model="activeTab" class="system-tabs">
      <el-tab-pane label="组织架构与员工" name="org">
        <Organization />
      </el-tab-pane>
      <el-tab-pane label="角色与权限分配" name="role">
        <RolePermission />
      </el-tab-pane>
      <el-tab-pane label="安全与审计日志" name="audit">
        <AuditLog />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Organization from './components/Organization.vue'
import RolePermission from './components/RolePermission.vue'
import AuditLog from './components/AuditLog.vue'

const activeTab = ref('org')
</script>

<style scoped lang="scss">
.system-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  min-height: calc(100vh - 100px);
}
</style>
