<template>
  <div class="shipping-operation">
    <el-card shadow="never" class="mb-4">
      <template #header>
        <div class="card-header">
          <span>待发货包裹列表 (已分仓)</span>
          <div class="actions">
            <el-button type="primary"><el-icon><Printer /></el-icon> 批量打印面单</el-button>
            <el-button type="success"><el-icon><Check /></el-icon> 批量发货 (回传单号)</el-button>
          </div>
        </div>
      </template>

      <div class="filter-bar mb-3 flex justify-between">
        <div class="left">
          <el-select v-model="filterForm.warehouse" placeholder="发货仓" clearable style="width: 150px; margin-right: 10px">
            <el-option label="大阪直发仓" value="osaka" />
            <el-option label="东京退货仓" value="tokyo" />
          </el-select>
          <el-select v-model="filterForm.carrier" placeholder="承运商" clearable style="width: 150px; margin-right: 10px">
            <el-option label="Yamato" value="yamato" />
            <el-option label="Sagawa" value="sagawa" />
          </el-select>
          <el-input placeholder="搜索订单号/收件人" style="width: 200px" />
        </div>
        <div class="right">
          <el-button plain>打印装箱单</el-button>
          <el-button plain>打印发货清单</el-button>
        </div>
      </div>

      <el-table :data="shippingData" border style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="packageId" label="包裹号" width="160" />
        <el-table-column prop="orderIds" label="关联订单号" min-width="180">
          <template #default="{ row }">
            <div v-for="id in row.orderIds" :key="id">
              <el-link type="primary">{{ id }}</el-link>
            </div>
            <el-tag size="small" type="warning" v-if="row.orderIds.length > 1" class="mt-1">合单发货</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="收件信息" min-width="200">
          <template #default="{ row }">
            <div class="font-bold">{{ row.recipient }} <span class="text-xs text-gray ml-1">{{ row.phone }}</span></div>
            <div class="text-xs text-gray mt-1">{{ row.address }}</div>
            <div class="address-status mt-1" v-if="row.addressError">
              <el-tag size="small" type="danger">地址解析异常，请手动修正</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="分配物流" width="160">
          <template #default="{ row }">
            <div class="flex flex-col">
              <span>{{ row.carrier }}</span>
              <span class="text-xs text-gray">{{ row.service }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="weight" label="预估重量" width="100" align="center" />
        <el-table-column label="打印状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.printed ? 'success' : 'info'">{{ row.printed ? '已打印' : '未打印' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" size="small">更换物流</el-button>
            <el-button link type="primary" size="small" :disabled="!row.printed">打单</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="mt-4 flex justify-between align-center">
        <div>
          已选择 <strong class="text-primary">{{ selectedCount }}</strong> 个包裹，预计运费 <strong class="text-danger">¥{{ estimatedCost }}</strong>
        </div>
        <el-pagination background layout="prev, pager, next" :total="100" />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'

const filterForm = reactive({
  warehouse: '',
  carrier: ''
})

const shippingData = ref([
  {
    packageId: 'PKG-20260302-001',
    orderIds: ['SO202603020001'],
    recipient: '田中 太郎',
    phone: '090-XXXX-XXXX',
    address: '〒150-0001 東京都渋谷区神宮前X-X-X',
    addressError: false,
    carrier: '黑猫宅急便',
    service: '宅急便',
    weight: '1.2 kg',
    printed: false,
    cost: 850
  },
  {
    packageId: 'PKG-20260302-002',
    orderIds: ['SO202603020002', 'SO202603020005'],
    recipient: '佐藤 花子',
    phone: '080-YYYY-YYYY',
    address: '〒530-0001 大阪府大阪市北区梅田Y-Y',
    addressError: false,
    carrier: '佐川急便',
    service: '飞脚宅配便',
    weight: '3.5 kg',
    printed: true,
    cost: 1200
  },
  {
    packageId: 'PKG-20260302-003',
    orderIds: ['SO202603020008'],
    recipient: 'Suzuki Kenji',
    phone: '070-ZZZZ-ZZZZ',
    address: 'Hokkaido Sapporo-shi ... (需转日文)',
    addressError: true,
    carrier: '日本邮政',
    service: 'ゆうパック',
    weight: '0.8 kg',
    printed: false,
    cost: 1500
  }
])

const selectedCount = ref(0)
const selectedCost = ref(0)

const estimatedCost = computed(() => {
  return selectedCost.value
})

const handleSelectionChange = (val: any[]) => {
  selectedCount.value = val.length
  selectedCost.value = val.reduce((sum, item) => sum + item.cost, 0)
}
</script>

<style scoped lang="scss">
.shipping-operation {
  .mb-4 { margin-bottom: 20px; }
  .mb-3 { margin-bottom: 15px; }
  .mt-1 { margin-top: 4px; }
  .mt-4 { margin-top: 16px; }
  .ml-1 { margin-left: 4px; }
  .flex { display: flex; }
  .flex-col { flex-direction: column; }
  .justify-between { justify-content: space-between; }
  .align-center { align-items: center; }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .font-bold { font-weight: bold; }
  .text-xs { font-size: 12px; }
  .text-gray { color: #909399; }
  .text-primary { color: #409EFF; }
  .text-danger { color: #F56C6C; }
}
</style>
