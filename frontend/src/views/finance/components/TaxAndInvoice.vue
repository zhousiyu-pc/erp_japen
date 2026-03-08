<template>
  <div class="tax-and-invoice">
    <el-card shadow="never" class="mb-4">
      <div class="header-action">
        <h3>日本消费税 (Consumption Tax) 管理</h3>
      </div>
      <el-alert
        title="日本消费税税率说明"
        type="info"
        description="标准税率：10%（大部分商品）；轻减税率：8%（食品、饮料等）。系统会自动根据商品类目匹配默认税率。"
        show-icon
        class="mt-3 mb-3"
        :closable="false"
      />

      <el-table :data="taxData" border style="width: 100%">
        <el-table-column prop="month" label="报税月份" width="120" />
        <el-table-column prop="sales" label="含税销售额 (JPY)" width="150" align="right" />
        <el-table-column prop="standardTax" label="10% 销项税额" width="150" align="right" />
        <el-table-column prop="reducedTax" label="8% 销项税额" width="150" align="right" />
        <el-table-column prop="inputTax" label="可抵扣进项税 (JPY)" width="150" align="right">
          <template #default="{ row }">
            <el-tooltip content="如日本当地采购、当地物流费产生的消费税" placement="top">
              <span class="cursor-help text-primary">{{ row.inputTax }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="payableTax" label="应缴消费税 (JPY)" width="150" align="right">
          <template #default="{ row }">
            <span class="font-bold text-danger">{{ row.payableTax }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === '已申报' ? 'success' : 'warning'">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template #default>
            <el-button link type="primary" size="small">导出申报表</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card shadow="never">
      <template #header>
        <div class="flex justify-between items-center">
          <div>
            <span class="font-bold">インボイス制度 (适格发票制度) 配置</span>
            <el-tag size="small" type="success" class="ml-2">已开启</el-tag>
          </div>
          <el-button type="primary" plain size="small">设置开票模板</el-button>
        </div>
      </template>

      <el-form label-position="left" label-width="200px" class="mt-2" style="max-width: 600px;">
        <el-form-item label="适格请求书发行事业者编号">
          <el-input v-model="invoiceForm.regNo" placeholder="T+13位数字" />
        </el-form-item>
        <el-form-item label="公司日文名称">
          <el-input v-model="invoiceForm.companyName" />
        </el-form-item>
        <el-form-item label="自动开票规则">
          <el-switch v-model="invoiceForm.autoIssue" active-text="买家申请时自动生成并发送" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary">保存配置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'

const taxData = ref([
  {
    month: '2026-02',
    sales: '5,500,000',
    standardTax: '500,000',
    reducedTax: '0',
    inputTax: '120,000',
    payableTax: '380,000',
    status: '待申报'
  },
  {
    month: '2026-01',
    sales: '4,400,000',
    standardTax: '400,000',
    reducedTax: '0',
    inputTax: '85,000',
    payableTax: '315,000',
    status: '已申报'
  }
])

const invoiceForm = reactive({
  regNo: 'T1234567890123',
  companyName: '株式会社クロスボーダー',
  autoIssue: true
})
</script>

<style scoped lang="scss">
.tax-and-invoice {
  .mb-3 { margin-bottom: 12px; }
  .mb-4 { margin-bottom: 20px; }
  .mt-2 { margin-top: 8px; }
  .mt-3 { margin-top: 12px; }
  .ml-2 { margin-left: 8px; }

  .header-action {
    display: flex;
    justify-content: space-between;
    align-items: center;
    h3 { margin: 0; }
  }

  .flex { display: flex; }
  .justify-between { justify-content: space-between; }
  .items-center { align-items: center; }

  .font-bold { font-weight: bold; }
  .text-danger { color: #F56C6C; }
  .text-primary { color: #409EFF; }
  .cursor-help { cursor: help; border-bottom: 1px dashed #409EFF; }
}
</style>
