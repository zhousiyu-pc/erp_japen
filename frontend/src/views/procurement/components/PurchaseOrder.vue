<template>
  <div class="purchase-order">
    <!-- Filters -->
    <el-card shadow="never" class="mb-4">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="采购单号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入单号" clearable />
        </el-form-item>
        <el-form-item label="供应商">
          <el-select v-model="searchForm.supplier" placeholder="全部" clearable style="width: 180px">
            <el-option label="深圳华强电子厂" value="sz_hq" />
            <el-option label="义乌小商品制造厂" value="yw_xsp" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="待审核" value="pending" />
            <el-option label="采购中" value="purchasing" />
            <el-option label="部分到货" value="partial" />
            <el-option label="已完成" value="completed" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"><el-icon><Search /></el-icon> 查询</el-button>
          <el-button>重置</el-button>
        </el-form-item>
      </el-form>
      
      <div class="toolbar mt-2">
        <el-button type="primary"><el-icon><Plus /></el-icon> 新建采购单</el-button>
        <el-button type="success" plain>批量审核</el-button>
        <el-button type="warning" plain>导出采购对账单</el-button>
      </div>
    </el-card>

    <!-- Table -->
    <el-table :data="orderData" border style="width: 100%">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="orderNo" label="采购单号" width="160">
        <template #default="{ row }">
          <el-link type="primary">{{ row.orderNo }}</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="supplier" label="供应商" min-width="150" show-overflow-tooltip />
      <el-table-column prop="warehouse" label="入库仓" width="120" />
      <el-table-column label="采购总额" width="140" align="right">
        <template #default="{ row }">
          <div class="font-bold">
            <span class="text-xs text-gray">{{ row.currency }}</span> {{ row.amount }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="进度 (已收/总数)" width="150" align="center">
        <template #default="{ row }">
          <el-progress :percentage="Math.round((row.receivedQty / row.totalQty) * 100)" />
          <div class="text-xs mt-1">{{ row.receivedQty }} / {{ row.totalQty }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">{{ row.statusLabel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="180" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small">详情</el-button>
          <el-button link type="success" size="small" v-if="row.status === 'purchasing' || row.status === 'partial'">收货质检</el-button>
          <el-button link type="warning" size="small" v-if="row.status === 'pending'">审核</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Pagination -->
    <div class="mt-4 flex justify-end">
      <el-pagination background layout="total, prev, pager, next" :total="12" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'

const searchForm = reactive({
  orderNo: '',
  supplier: '',
  status: ''
})

const orderData = ref([
  {
    orderNo: 'PO-20260302-001',
    supplier: '深圳华强电子厂',
    warehouse: '深圳一仓',
    currency: 'CNY',
    amount: '25,000.00',
    totalQty: 500,
    receivedQty: 0,
    status: 'pending',
    statusLabel: '待审核',
    createTime: '2026-03-02 10:00:00'
  },
  {
    orderNo: 'PO-20260228-015',
    supplier: '义乌小商品制造厂',
    warehouse: '义乌集货仓',
    currency: 'CNY',
    amount: '12,000.00',
    totalQty: 1000,
    receivedQty: 400,
    status: 'partial',
    statusLabel: '部分到货',
    createTime: '2026-02-28 14:30:22'
  },
  {
    orderNo: 'PO-20260220-088',
    supplier: 'Japan Packaging Co. (日本本地)',
    warehouse: '大阪直发仓',
    currency: 'JPY',
    amount: '150,000',
    totalQty: 2000,
    receivedQty: 2000,
    status: 'completed',
    statusLabel: '已完成',
    createTime: '2026-02-20 09:15:00'
  }
])

const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    pending: 'warning',
    purchasing: 'primary',
    partial: 'primary',
    completed: 'success'
  }
  return map[status] || 'info'
}
</script>

<style scoped lang="scss">
.purchase-order {
  .mb-4 { margin-bottom: 20px; }
  .mt-2 { margin-top: 8px; }
  .mt-4 { margin-top: 16px; }
  .mt-1 { margin-top: 4px; }
  .flex { display: flex; }
  .justify-end { justify-content: flex-end; }
  
  .font-bold { font-weight: bold; }
  .text-xs { font-size: 12px; }
  .text-gray { color: #909399; }
}
</style>
