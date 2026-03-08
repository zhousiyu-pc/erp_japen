<template>
  <div class="role-permission">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="never" class="role-list-card">
          <template #header>
            <div class="flex justify-between items-center">
              <span class="font-bold">系统角色列表</span>
              <el-button link type="primary"><el-icon><Plus /></el-icon> 新增角色</el-button>
            </div>
          </template>
          
          <div class="role-list">
            <div 
              v-for="role in roles" 
              :key="role.id" 
              class="role-item flex justify-between items-center"
              :class="{ active: currentRole?.id === role.id }"
              @click="currentRole = role"
            >
              <div>
                <div class="font-bold">{{ role.name }}</div>
                <div class="text-xs text-gray mt-1">{{ role.desc }} ({{ role.userCount }}人)</div>
              </div>
              <el-tag size="small" :type="role.isSystem ? 'info' : 'success'">{{ role.isSystem ? '内置' : '自定义' }}</el-tag>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="16">
        <el-card shadow="never" class="permission-card" v-if="currentRole">
          <template #header>
            <div class="flex justify-between items-center">
              <span class="font-bold">权限配置: <span class="text-primary">{{ currentRole.name }}</span></span>
              <el-button type="primary" :disabled="currentRole.isSystem">保存权限</el-button>
            </div>
            <div class="text-xs text-warning mt-2" v-if="currentRole.isSystem">
              <el-icon><Warning /></el-icon> 系统内置角色不可修改，如需调整请克隆为自定义角色。
            </div>
          </template>

          <el-tabs v-model="activePermTab">
            <el-tab-pane label="功能菜单权限" name="menu">
              <el-tree
                :data="menuPermissions"
                show-checkbox
                default-expand-all
                node-key="id"
                :default-checked-keys="currentRole.menuIds"
                :props="defaultProps"
                :disabled="currentRole.isSystem"
              />
            </el-tab-pane>
            <el-tab-pane label="数据范围权限" name="data">
              <el-form label-position="top">
                <el-form-item label="店铺数据访问范围">
                  <el-radio-group v-model="currentRole.dataScope" :disabled="currentRole.isSystem">
                    <el-radio label="all">全部店铺</el-radio>
                    <el-radio label="dept">本部门及下属部门店铺</el-radio>
                    <el-radio label="self">仅自己负责的店铺</el-radio>
                    <el-radio label="custom">自定义选择</el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="敏感数据操作" class="mt-4">
                  <el-checkbox-group v-model="currentRole.sensitiveOps" :disabled="currentRole.isSystem">
                    <el-checkbox label="export_report">允许导出报表</el-checkbox>
                    <el-checkbox label="view_profit">允许查看利润/成本价</el-checkbox>
                    <el-checkbox label="batch_delete">允许批量删除</el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const activePermTab = ref('menu')

const roles = ref([
  { id: 1, name: '超级管理员', desc: '拥有系统所有权限', userCount: 1, isSystem: true, menuIds: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12], dataScope: 'all', sensitiveOps: ['export_report', 'view_profit', 'batch_delete'] },
  { id: 2, name: '运营主管', desc: '负责店铺整体运营', userCount: 3, isSystem: false, menuIds: [1, 2, 3, 4, 7, 10], dataScope: 'dept', sensitiveOps: ['export_report', 'view_profit'] },
  { id: 3, name: '客服专员', desc: '处理买家消息与售后', userCount: 8, isSystem: true, menuIds: [4, 8], dataScope: 'self', sensitiveOps: [] },
  { id: 4, name: '采购管理', desc: '负责供应链与供应商', userCount: 2, isSystem: true, menuIds: [3, 5], dataScope: 'all', sensitiveOps: ['view_profit'] }
])

const currentRole = ref<any>(roles.value[1])

const defaultProps = {
  children: 'children',
  label: 'label',
  disabled: () => currentRole.value.isSystem
}

const menuPermissions = ref([
  {
    id: 1, label: '经营总览',
  },
  {
    id: 2, label: '商品中心',
    children: [
      { id: 21, label: '商品主数据 (读写)' },
      { id: 22, label: 'Listing 管理 (读写)' }
    ]
  },
  {
    id: 3, label: '订单中心',
    children: [
      { id: 31, label: '订单查询 (只读)' },
      { id: 32, label: '售后处理 (读写)' }
    ]
  },
  { id: 10, label: '统计与分析 BI' }
])
</script>

<style scoped lang="scss">
.role-permission {
  .mt-1 { margin-top: 4px; }
  .mt-2 { margin-top: 8px; }
  .mt-4 { margin-top: 16px; }

  .flex { display: flex; }
  .justify-between { justify-content: space-between; }
  .items-center { align-items: center; }

  .font-bold { font-weight: bold; }
  .text-xs { font-size: 12px; }
  .text-gray { color: #909399; }
  .text-primary { color: #409EFF; }
  .text-warning { color: #E6A23C; }

  .role-list-card {
    height: 600px;
    :deep(.el-card__body) {
      padding: 0;
    }
  }

  .role-list {
    .role-item {
      padding: 15px 20px;
      border-bottom: 1px solid #ebeef5;
      cursor: pointer;
      transition: all 0.2s;

      &:hover {
        background-color: #f5f7fa;
      }
      
      &.active {
        background-color: #ecf5ff;
        border-left: 3px solid #409eff;
      }
    }
  }
}
</style>
