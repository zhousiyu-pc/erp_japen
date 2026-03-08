<template>
  <div class="inventory-list">
    <!-- Filters -->
    <el-card shadow="never" class="mb-4">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="仓库">
          <el-select v-model="searchForm.warehouse" placeholder="全部仓库" clearable style="width: 180px">
            <el-option-group label="国内仓">
              <el-option label="深圳一仓" value="sz_1" />
              <el-option label="义乌集货仓" value="yw_1" />
            </el-option-group>
            <el-option-group label="海外仓 (日本)">
              <el-option label="大阪直发仓" value="jp_osaka" />
              <el-option label="东京退货仓" value="jp_tokyo" />
            </el-option-group>
            <el-option-group label="FBA虚拟仓">
              <el-option label="FBA (NRT1)" value="fba_nrt1" />
              <el-option label="FBA (KIX3)" value="fba_kix3" />
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item label="SKU/商品名">
          <el-input v-model="searchForm.keyword" placeholder="请输入..." clearable style="width: 200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary"><el-icon><Search /></el-icon> 查询</el-button>
          <el-button>重置</el-button>
          <el-button type="success" plain><el-icon><Download /></el-icon> 导出盘点表</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Table -->
    <el-table :data="inventoryData" border style="width: 100%">
      <el-table-column prop="warehouse" label="所在仓库" width="150" />
      <el-table-column prop="sku" label="SKU" width="150">
        <template #default="{ row }">
          <span class="font-bold">{{ row.sku }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="商品名称" min-width="200" show-overflow-tooltip />
      <el-table-column prop="location" label="库位" width="120" />
      
      <el-table-column label="库存维度 (件)" align="center">
        <el-table-column prop="physicalStock" label="物理库存" width="100" align="right" />
        <el-table-column prop="availableStock" label="可售库存" width="100" align="right">
          <template #default="{ row }">
            <span class="text-success font-bold">{{ row.availableStock }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="lockedStock" label="锁定库存" width="100" align="right">
          <template #default="{ row }">
            <el-tooltip content="含订单占用、调拨占用" placement="top">
              <span class="text-warning">{{ row.lockedStock }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="inTransitStock" label="在途库存" width="100" align="right">
          <template #default="{ row }">
            <el-link type="primary" :underline="false">{{ row.inTransitStock }}</el-link>
          </template>
        </el-table-column>
      </el-table-column>
      
      <el-table-column label="操作" width="150" fixed="right" align="center">
        <template #default>
          <el-button link type="primary" size="small">调整库存</el-button>
          <el-button link type="primary" size="small">库存流水</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Pagination -->
    <div class="mt-4 flex justify-end">
      <el-pagination background layout="prev, pager, next" :total="50" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'

const searchForm = reactive({
  warehouse: '',
  keyword: ''
})

const inventoryData = ref([
  {
    warehouse: '大阪直发仓',
    sku: 'SKU-001-BLK-M',
    name: '无线蓝牙耳机 降噪 运动防水',
    location: 'A-01-03',
    physicalStock: 250,
    availableStock: 200,
    lockedStock: 50,
    inTransitStock: 500
  },
  {
    warehouse: 'FBA (NRT1)',
    sku: 'SKU-001-BLK-M',
    name: '无线蓝牙耳机 降噪 运动防水',
    location: 'FBA-VIRTUAL',
    physicalStock: 145,
    availableStock: 140,
    lockedStock: 5,
    inTransitStock: 0
  },
  {
    warehouse: '深圳一仓',
    sku: 'SKU-002-WHT',
    name: '桌面收纳盒',
    location: 'C-05-12',
    physicalStock: 1000,
    availableStock: 800,
    lockedStock: 200,
    inTransitStock: 2000
  }
])
</script>

<style scoped lang="scss">
.inventory-list {
  .mb-4 { margin-bottom: 20px; }
  .mt-4 { margin-top: 16px; }
  .flex { display: flex; }
  .justify-end { justify-content: flex-end; }
  
  .font-bold { font-weight: bold; }
  .text-success { color: #67C23A; }
  .text-warning { color: #E6A23C; }
}
</style>
