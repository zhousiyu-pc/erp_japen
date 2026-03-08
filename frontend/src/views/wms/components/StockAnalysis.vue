<template>
  <div class="stock-analysis">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover" class="mb-4">
          <template #header>
            <div class="header-with-icon">
              <span><el-icon color="#F56C6C"><WarningFilled /></el-icon> 安全库存预警 (需补货)</span>
              <el-button type="primary" link size="small">一键生成采购计划</el-button>
            </div>
          </template>
          <el-table :data="safetyStockAlerts" border style="width: 100%" size="small">
            <el-table-column prop="sku" label="SKU" width="130" />
            <el-table-column prop="warehouse" label="仓库" width="100" />
            <el-table-column label="当前可用" width="80" align="right">
              <template #default="{ row }">
                <span class="text-danger font-bold">{{ row.available }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="safety" label="安全库存线" width="90" align="right" />
            <el-table-column prop="dailySales" label="日均销量" width="80" align="right" />
            <el-table-column label="预计可售" width="100" align="center">
              <template #default="{ row }">
                <el-tag size="small" type="danger">{{ row.daysLeft }} 天</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card shadow="hover" class="mb-4">
          <template #header>
            <div class="header-with-icon">
              <span><el-icon color="#909399"><InfoFilled /></el-icon> 呆滞库存分析 (超90天无销量)</span>
              <el-button type="primary" link size="small">导出报表</el-button>
            </div>
          </template>
          <el-table :data="deadStockAlerts" border style="width: 100%" size="small">
            <el-table-column prop="sku" label="SKU" width="130" />
            <el-table-column prop="warehouse" label="仓库" width="100" />
            <el-table-column prop="stock" label="积压数量" width="80" align="right" />
            <el-table-column prop="value" label="占用资金(¥)" width="100" align="right" />
            <el-table-column label="滞销天数" width="100" align="center">
              <template #default="{ row }">
                <el-tag size="small" type="info">{{ row.deadDays }} 天</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="处理建议" min-width="120">
              <template #default="{ row }">
                <span class="text-gray text-xs">{{ row.suggestion }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- Chart Placeholder -->
    <el-card shadow="never" header="库存周转率趋势">
      <div class="chart-placeholder">
        <el-icon :size="40" color="#C0C4CC"><PieChart /></el-icon>
        <p class="mt-2 text-gray">各仓库库存周转率图表区域</p>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const safetyStockAlerts = ref([
  { sku: 'SKU-001-BLK-M', warehouse: 'FBA (NRT1)', available: 15, safety: 100, dailySales: 12, daysLeft: 1 },
  { sku: 'SKU-003-BLU', warehouse: '大阪直发仓', available: 50, safety: 150, dailySales: 8, daysLeft: 6 }
])

const deadStockAlerts = ref([
  { sku: 'SKU-009-OLD', warehouse: '深圳一仓', stock: 450, value: '9,000', deadDays: 120, suggestion: '建议捆绑促销或站外清仓' },
  { sku: 'SKU-010-ERR', warehouse: '东京退货仓', stock: 12, value: '600', deadDays: 95, suggestion: '建议直接报废处理' }
])
</script>

<style scoped lang="scss">
.stock-analysis {
  .mb-4 { margin-bottom: 20px; }
  .mt-2 { margin-top: 8px; }
  
  .header-with-icon {
    display: flex;
    justify-content: space-between;
    align-items: center;
    span {
      display: flex;
      align-items: center;
      gap: 8px;
      font-weight: bold;
    }
  }

  .text-danger { color: #F56C6C; }
  .text-gray { color: #909399; }
  .text-xs { font-size: 12px; }
  .font-bold { font-weight: bold; }

  .chart-placeholder {
    height: 300px;
    background-color: #f8f9fa;
    border-radius: 4px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }
}
</style>
