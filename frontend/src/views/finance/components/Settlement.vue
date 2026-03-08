<template>
  <div class="settlement">
    <el-card shadow="never" class="mb-4">
      <div class="header-action">
        <h3>平台结算单解析</h3>
        <el-button type="primary"><el-icon><Upload /></el-icon> 导入/同步结算单</el-button>
      </div>
      
      <el-form :inline="true" class="mt-3">
        <el-form-item label="平台">
          <el-select v-model="platform" style="width: 150px">
            <el-option label="Amazon 日本站" value="amazon" />
            <el-option label="Rakuten 乐天" value="rakuten" />
          </el-select>
        </el-form-item>
        <el-form-item label="结算周期">
          <el-date-picker type="daterange" range-separator="至" start-placeholder="开始" end-placeholder="结束" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="status" style="width: 120px">
            <el-option label="待分摊" value="pending" />
            <el-option label="已入账" value="completed" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary">查询</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="settlementData" border style="width: 100%" class="mt-2">
        <el-table-column prop="batchNo" label="结算批次号" width="180">
          <template #default="{ row }"><el-link type="primary">{{ row.batchNo }}</el-link></template>
        </el-table-column>
        <el-table-column prop="period" label="结算周期" width="200" />
        <el-table-column label="本期收入(JPY)" width="130" align="right">
          <template #default="{ row }"><span class="text-success font-bold">+{{ row.income }}</span></template>
        </el-table-column>
        <el-table-column label="平台扣费(JPY)" width="130" align="right">
          <template #default="{ row }">
            <el-tooltip placement="top">
              <template #content>
                佣金: {{ row.commission }}<br>FBA费用: {{ row.fbaFee }}<br>广告费: {{ row.adFee }}
              </template>
              <span class="text-danger cursor-help">-{{ row.deduction }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="实收金额(JPY)" width="130" align="right">
          <template #default="{ row }"><span class="font-bold">{{ row.netAmount }}</span></template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === '已入账' ? 'success' : 'warning'">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right" align="center">
          <template #default>
            <el-button link type="primary" size="small">成本分摊</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card shadow="never" header="公摊成本录入 (如头程运费、人工)">
      <el-table :data="costData" border style="width: 100%">
        <el-table-column prop="date" label="发生日期" width="120" />
        <el-table-column prop="type" label="费用类型" width="120">
          <template #default="{ row }"><el-tag size="small" type="info">{{ row.type }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="desc" label="费用说明" min-width="200" />
        <el-table-column label="金额 (CNY)" width="120" align="right">
          <template #default="{ row }">¥ {{ row.amount }}</template>
        </el-table-column>
        <el-table-column prop="rule" label="分摊规则" width="150" />
        <el-table-column label="操作" width="100" align="center">
          <template #default>
            <el-button link type="primary" size="small">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="mt-3">
        <el-button type="dashed" class="w-full"><el-icon><Plus /></el-icon> 添加一笔外部成本</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const platform = ref('amazon')
const status = ref('pending')

const settlementData = ref([
  {
    batchNo: 'SET-AMZ-2603-01',
    period: '2026-02-15 ~ 2026-02-28',
    income: '2,500,000',
    deduction: '850,000',
    commission: '375,000',
    fbaFee: '350,000',
    adFee: '125,000',
    netAmount: '1,650,000',
    status: '待分摊'
  },
  {
    batchNo: 'SET-AMZ-2602-02',
    period: '2026-02-01 ~ 2026-02-14',
    income: '2,100,000',
    deduction: '720,000',
    commission: '315,000',
    fbaFee: '305,000',
    adFee: '100,000',
    netAmount: '1,380,000',
    status: '已入账'
  }
])

const costData = ref([
  { date: '2026-03-01', type: '干线物流', desc: '2月份海运专线头程运费账单', amount: '12,500', rule: '按商品重量分摊' },
  { date: '2026-02-28', type: '仓储费', desc: '日本海外仓2月租金', amount: '8,000', rule: '按体积占比分摊' },
  { date: '2026-02-25', type: '办公管理', desc: '运营团队2月社保与公摊', amount: '15,000', rule: '按店铺销售额比例' }
])
</script>

<style scoped lang="scss">
.settlement {
  .mb-4 { margin-bottom: 20px; }
  .mt-2 { margin-top: 8px; }
  .mt-3 { margin-top: 15px; }
  .w-full { width: 100%; }

  .header-action {
    display: flex;
    justify-content: space-between;
    align-items: center;
    h3 { margin: 0; }
  }

  .font-bold { font-weight: bold; }
  .text-success { color: #67C23A; }
  .text-danger { color: #F56C6C; }
  .cursor-help { cursor: help; border-bottom: 1px dashed #F56C6C; }
}
</style>
