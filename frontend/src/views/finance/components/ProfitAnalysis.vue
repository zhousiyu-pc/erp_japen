<template>
  <div class="profit-analysis">
    <el-row :gutter="20">
      <el-col :span="18">
        <el-card shadow="never" class="mb-4">
          <template #header>
            <div class="flex justify-between items-center">
              <span class="font-bold">月度利润明细报表</span>
              <div>
                <el-radio-group v-model="currency" size="small" class="mr-3">
                  <el-radio-button label="CNY">CNY 本位币</el-radio-button>
                  <el-radio-button label="JPY">JPY 原币</el-radio-button>
                </el-radio-group>
                <el-button type="success" plain size="small"><el-icon><Download /></el-icon> 导出明细</el-button>
              </div>
            </div>
          </template>

          <el-table :data="profitData" border style="width: 100%" show-summary :summary-method="getSummaries">
            <el-table-column prop="month" label="月份" width="100" />
            <el-table-column prop="sales" label="销售总额" width="120" align="right" />
            <el-table-column label="直接成本扣除" align="center">
              <el-table-column prop="productCost" label="商品采购成本" width="120" align="right" />
              <el-table-column prop="shippingCost" label="头程+末端运费" width="120" align="right" />
              <el-table-column prop="platformFee" label="平台佣金/FBA" width="120" align="right" />
            </el-table-column>
            <el-table-column label="营销与公摊" align="center">
              <el-table-column prop="adCost" label="广告与促销" width="120" align="right" />
              <el-table-column prop="sharedCost" label="公摊管理费" width="120" align="right" />
            </el-table-column>
            <el-table-column prop="grossProfit" label="毛利润" width="120" align="right">
              <template #default="{ row }"><span class="font-bold text-success">{{ row.grossProfit }}</span></template>
            </el-table-column>
            <el-table-column prop="margin" label="毛利率" width="100" align="right">
              <template #default="{ row }"><span class="font-bold">{{ row.margin }}%</span></template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-card shadow="never" header="SKU 维度利润率 Top 10">
          <div class="chart-placeholder">
            <el-icon :size="40" color="#C0C4CC"><DataAnalysis /></el-icon>
            <p class="mt-2 text-gray text-xs">柱状图：各 SKU 为公司贡献的绝对毛利与利润率排行</p>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="never" header="汇率管理中心" class="mb-4">
          <div class="exchange-rate-box">
            <div class="rate-item flex justify-between items-center mb-3">
              <div class="flex items-center">
                <el-avatar :size="24" class="bg-gray-100 mr-2">JPY</el-avatar>
                <span>日元兑人民币</span>
              </div>
              <div class="font-bold text-primary">0.0485</div>
            </div>
            <div class="rate-item flex justify-between items-center mb-3">
              <div class="flex items-center">
                <el-avatar :size="24" class="bg-gray-100 mr-2">USD</el-avatar>
                <span>美元兑人民币</span>
              </div>
              <div class="font-bold">7.1850</div>
            </div>
            <el-divider />
            <div class="text-xs text-gray text-center mb-2">更新于: 2026-03-02 10:00 (自动同步央行汇率)</div>
            <el-button class="w-full" plain>设置记账固定汇率</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const currency = ref('CNY')

const profitData = ref([
  {
    month: '2026-02',
    sales: '150,000',
    productCost: '35,000',
    shippingCost: '22,000',
    platformFee: '28,000',
    adCost: '15,000',
    sharedCost: '5,000',
    grossProfit: '45,000',
    margin: '30.0'
  },
  {
    month: '2026-01',
    sales: '180,000',
    productCost: '42,000',
    shippingCost: '25,000',
    platformFee: '33,000',
    adCost: '18,000',
    sharedCost: '5,000',
    grossProfit: '57,000',
    margin: '31.6'
  }
])

const getSummaries = (param: any) => {
  const { columns, data } = param
  const sums: string[] = []
  columns.forEach((column: any, index: number) => {
    if (index === 0) {
      sums[index] = '总计'
      return
    }
    if (index === columns.length - 1) {
      sums[index] = '-' // 利润率不适合直接相加
      return
    }
    const values = data.map((item: any) => Number(item[column.property].replace(/,/g, '')))
    if (!values.every((value: number) => Number.isNaN(value))) {
      const sum = values.reduce((prev: number, curr: number) => {
        const value = Number(curr)
        if (!Number.isNaN(value)) {
          return prev + curr
        } else {
          return prev
        }
      }, 0)
      // 简单格式化千分位
      sums[index] = sum.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",")
    } else {
      sums[index] = 'N/A'
    }
  })
  return sums
}
</script>

<style scoped lang="scss">
.profit-analysis {
  .mb-2 { margin-bottom: 8px; }
  .mb-3 { margin-bottom: 12px; }
  .mb-4 { margin-bottom: 20px; }
  .mr-2 { margin-right: 8px; }
  .mr-3 { margin-right: 12px; }
  .mt-2 { margin-top: 8px; }
  
  .flex { display: flex; }
  .justify-between { justify-content: space-between; }
  .items-center { align-items: center; }
  .text-center { text-align: center; }
  .w-full { width: 100%; }
  
  .font-bold { font-weight: bold; }
  .text-xs { font-size: 12px; }
  .text-gray { color: #909399; }
  .text-primary { color: #409EFF; }
  .text-success { color: #67C23A; }
  .bg-gray-100 { background-color: #f0f2f5; color: #909399; font-size: 10px; }

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
