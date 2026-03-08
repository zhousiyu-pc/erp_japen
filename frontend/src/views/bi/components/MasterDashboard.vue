<template>
  <div class="master-dashboard">
    <div class="filter-bar mb-4 flex justify-between items-center">
      <div class="left">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :shortcuts="shortcuts"
        />
        <el-select v-model="store" placeholder="全部店铺" class="ml-3" style="width: 150px">
          <el-option label="全部店铺" value="all" />
          <el-option label="Amazon 日本站" value="amazon" />
          <el-option label="Rakuten 乐天" value="rakuten" />
        </el-select>
      </div>
      <div class="right">
        <el-button type="primary" plain><el-icon><Download /></el-icon> 导出报告</el-button>
      </div>
    </div>

    <!-- Core KPIs -->
    <el-row :gutter="20" class="mb-4">
      <el-col :span="6" v-for="item in kpis" :key="item.title">
        <el-card shadow="hover" class="kpi-card" :class="item.type">
          <div class="kpi-header">
            <span>{{ item.title }}</span>
          </div>
          <div class="kpi-value">{{ item.prefix }}{{ item.value }}</div>
          <div class="kpi-footer flex justify-between">
            <span class="text-xs text-gray">环比 <span :class="item.trend > 0 ? 'text-success' : 'text-danger'">{{ item.trend > 0 ? '+' : '' }}{{ item.trend }}%</span></span>
            <span class="text-xs text-gray">同比 <span :class="item.yoy > 0 ? 'text-success' : 'text-danger'">{{ item.yoy > 0 ? '+' : '' }}{{ item.yoy }}%</span></span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Charts -->
    <el-row :gutter="20" class="mb-4">
      <el-col :span="16">
        <el-card shadow="never" header="销售额与利润趋势 (近30天)">
          <div class="chart-box" style="height: 300px;">
            <el-icon :size="48" color="#C0C4CC"><TrendCharts /></el-icon>
            <span class="mt-2 text-gray text-sm">双轴折线柱状图：销售额 vs 毛利 vs 毛利率</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="never" header="多平台业绩占比">
          <div class="chart-box" style="height: 300px;">
            <el-icon :size="48" color="#C0C4CC"><PieChart /></el-icon>
            <span class="mt-2 text-gray text-sm">环形图：Amazon 65% | Rakuten 25% | Yahoo 10%</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Top Lists -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="never">
          <template #header>
            <div class="flex justify-between items-center">
              <span>高毛利 SKU 排行榜 (Top 5)</span>
              <el-button link type="primary">查看全部</el-button>
            </div>
          </template>
          <el-table :data="topProfitSkus" style="width: 100%" size="small">
            <el-table-column type="index" width="50" align="center" />
            <el-table-column prop="sku" label="SKU" min-width="120" />
            <el-table-column prop="sales" label="销售额(¥)" width="100" align="right" />
            <el-table-column prop="profit" label="毛利润(¥)" width="100" align="right">
              <template #default="{ row }"><span class="text-success font-bold">{{ row.profit }}</span></template>
            </el-table-column>
            <el-table-column prop="margin" label="毛利率" width="80" align="right" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <template #header>
            <div class="flex justify-between items-center">
              <span>严重退货 SKU 预警 (Top 5)</span>
              <el-button link type="danger">分析原因</el-button>
            </div>
          </template>
          <el-table :data="topReturnSkus" style="width: 100%" size="small">
            <el-table-column type="index" width="50" align="center" />
            <el-table-column prop="sku" label="SKU" min-width="120" />
            <el-table-column prop="qty" label="销量" width="80" align="right" />
            <el-table-column prop="returns" label="退货量" width="80" align="right" />
            <el-table-column prop="rate" label="退货率" width="100" align="right">
              <template #default="{ row }"><span class="text-danger font-bold">{{ row.rate }}%</span></template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const dateRange = ref('')
const store = ref('all')

const shortcuts = [
  { text: '今天', value: () => { const end = new Date(); const start = new Date(); return [start, end] } },
  { text: '近7天', value: () => { const end = new Date(); const start = new Date(); start.setTime(start.getTime() - 3600 * 1000 * 24 * 7); return [start, end] } },
  { text: '本月', value: () => { const end = new Date(); const start = new Date(end.getFullYear(), end.getMonth(), 1); return [start, end] } }
]

const kpis = ref([
  { title: '总销售额', value: '1,250,800', prefix: '¥ ', trend: 15.2, yoy: 22.4, type: 'primary' },
  { title: '总订单数', value: '8,420', prefix: '', trend: 12.5, yoy: 18.0, type: 'info' },
  { title: '总毛利润', value: '385,000', prefix: '¥ ', trend: 8.4, yoy: 15.6, type: 'success' },
  { title: '整体毛利率', value: '30.8', prefix: '', trend: -1.2, yoy: 2.1, type: 'warning' }
])

const topProfitSkus = ref([
  { sku: 'SKU-001-BLK-M', sales: '85,000', profit: '35,000', margin: '41.1%' },
  { sku: 'SKU-002-WHT', sales: '62,000', profit: '28,000', margin: '45.1%' },
  { sku: 'SKU-003-BLU', sales: '45,000', profit: '15,000', margin: '33.3%' }
])

const topReturnSkus = ref([
  { sku: 'SKU-CLO-099-XL', qty: 150, returns: 32, rate: '21.3' },
  { sku: 'SKU-ELEC-102', qty: 420, returns: 65, rate: '15.4' },
  { sku: 'SKU-HOME-055', qty: 85, returns: 10, rate: '11.7' }
])
</script>

<style scoped lang="scss">
.master-dashboard {
  .mb-4 { margin-bottom: 20px; }
  .ml-3 { margin-left: 12px; }
  .mt-2 { margin-top: 8px; }
  
  .flex { display: flex; }
  .justify-between { justify-content: space-between; }
  .items-center { align-items: center; }

  .text-xs { font-size: 12px; }
  .text-sm { font-size: 13px; }
  .text-gray { color: #909399; }
  .text-success { color: #67C23A; }
  .text-danger { color: #F56C6C; }
  .font-bold { font-weight: bold; }

  .kpi-card {
    border-left: 4px solid transparent;
    &.primary { border-left-color: #409EFF; }
    &.info { border-left-color: #909399; }
    &.success { border-left-color: #67C23A; }
    &.warning { border-left-color: #E6A23C; }

    .kpi-header { color: #909399; font-size: 14px; margin-bottom: 10px; }
    .kpi-value { font-size: 28px; font-weight: bold; color: #303133; margin-bottom: 15px; }
  }

  .chart-box {
    background-color: #f8f9fa;
    border-radius: 4px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
}
</style>
