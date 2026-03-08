<template>
  <div class="supplier-management">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="never">
          <template #header>
            <div class="header-action">
              <span>供应商分类</span>
              <el-button link type="primary"><el-icon><Plus /></el-icon></el-button>
            </div>
          </template>
          <el-menu default-active="all" class="category-menu">
            <el-menu-item index="all">全部供应商 (45)</el-menu-item>
            <el-menu-item index="1">核心供应商 (8)</el-menu-item>
            <el-menu-item index="2">普通供应商 (32)</el-menu-item>
            <el-menu-item index="3">淘汰/黑名单 (5)</el-menu-item>
          </el-menu>
        </el-card>
      </el-col>
      
      <el-col :span="18">
        <el-card shadow="never">
          <div class="filter-bar mb-4 flex justify-between">
            <div class="left">
              <el-input placeholder="搜索供应商名称/联系人" style="width: 250px; margin-right: 10px" clearable>
                <template #append><el-button><el-icon><Search /></el-icon></el-button></template>
              </el-input>
              <el-select placeholder="评级" clearable style="width: 120px">
                <el-option label="S级 (优秀)" value="S" />
                <el-option label="A级 (良好)" value="A" />
                <el-option label="B级 (合格)" value="B" />
                <el-option label="C级 (需改进)" value="C" />
              </el-select>
            </div>
            <div class="right">
              <el-button type="primary"><el-icon><Plus /></el-icon> 新增供应商</el-button>
            </div>
          </div>

          <el-table :data="supplierData" border style="width: 100%">
            <el-table-column prop="code" label="编号" width="100" />
            <el-table-column prop="name" label="供应商名称" min-width="180" show-overflow-tooltip>
              <template #default="{ row }">
                <div class="font-bold">{{ row.name }}</div>
                <div class="text-xs text-gray"><el-icon><Location /></el-icon> {{ row.location }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="contact" label="联系人" width="120">
              <template #default="{ row }">
                <div>{{ row.contact }}</div>
                <div class="text-xs text-gray">{{ row.phone }}</div>
              </template>
            </el-table-column>
            <el-table-column label="综合评级" width="120" align="center">
              <template #default="{ row }">
                <el-rate v-model="row.rating" disabled text-color="#ff9900" :max="5" />
                <div class="text-xs text-gray mt-1">退货率: {{ row.returnRate }}%</div>
              </template>
            </el-table-column>
            <el-table-column label="合作商品数" width="100" align="center">
              <template #default="{ row }">
                <el-link type="primary">{{ row.skuCount }}</el-link>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="90" align="center">
              <template #default="{ row }">
                <el-tag :type="row.status === '正常' ? 'success' : 'danger'">{{ row.status }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" fixed="right" align="center">
              <template #default>
                <el-button link type="primary" size="small">档案</el-button>
                <el-button link type="primary" size="small">报价单</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const supplierData = ref([
  {
    code: 'SUP-001',
    name: '深圳华强电子厂',
    location: '广东省深圳市宝安区',
    contact: '张经理',
    phone: '138****1234',
    rating: 4.5,
    returnRate: 1.2,
    skuCount: 45,
    status: '正常'
  },
  {
    code: 'SUP-002',
    name: '义乌小商品制造厂',
    location: '浙江省金华市义乌市',
    contact: '王老板',
    phone: '139****5678',
    rating: 4.0,
    returnRate: 2.5,
    skuCount: 120,
    status: '正常'
  },
  {
    code: 'SUP-003',
    name: 'Japan Packaging Co.',
    location: '東京都新宿区...',
    contact: '田中',
    phone: '03-****-****',
    rating: 5.0,
    returnRate: 0.1,
    skuCount: 5,
    status: '正常'
  }
])
</script>

<style scoped lang="scss">
.supplier-management {
  .header-action {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .category-menu {
    border-right: none;
  }

  .mb-4 { margin-bottom: 20px; }
  .mt-1 { margin-top: 4px; }
  .flex { display: flex; }
  .justify-between { justify-content: space-between; }
  
  .font-bold { font-weight: bold; }
  .text-xs { font-size: 12px; }
  .text-gray { color: #909399; }
}
</style>
