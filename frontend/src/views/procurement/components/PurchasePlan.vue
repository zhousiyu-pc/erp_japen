<template>
  <div class="purchase-plan">
    <el-card shadow="never" class="mb-4">
      <template #header>
        <div class="card-header">
          <span>AI 补货建议生成器</span>
          <el-button type="primary"><el-icon><MagicStick /></el-icon> 重新生成建议</el-button>
        </div>
      </template>
      <el-form :inline="true" :model="filterForm">
        <el-form-item label="建议类型">
          <el-select v-model="filterForm.type" style="width: 150px">
            <el-option label="紧急补货 (缺货风险)" value="urgent" />
            <el-option label="常规补货 (维持安全线)" value="normal" />
            <el-option label="大促备货 (如 Prime Day)" value="event" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标仓库">
          <el-select v-model="filterForm.warehouse" clearable style="width: 150px">
            <el-option label="FBA (NRT1)" value="fba" />
            <el-option label="大阪直发仓" value="osaka" />
          </el-select>
        </el-form-item>
      </el-form>

      <el-alert
        title="当前算法基于：近30天日均销量 + 供应商平均交期 + 物流头程时效 + 安全库存预设值。"
        type="info"
        show-icon
        class="mb-3"
        :closable="false"
      />

      <el-table :data="planData" border style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="sku" label="SKU / 商品信息" min-width="200">
          <template #default="{ row }">
            <div class="font-bold">{{ row.sku }}</div>
            <div class="text-xs text-gray">{{ row.name }}</div>
          </template>
        </el-table-column>
        <el-table-column label="库存数据" align="center" width="240">
          <template #default="{ row }">
            <div class="stock-info">
              <span>可用: <strong>{{ row.available }}</strong></span>
              <span>在途: <strong>{{ row.inTransit }}</strong></span>
              <span>日均销: <strong>{{ row.dailySales }}</strong></span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="断货风险" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="row.daysLeft < 15 ? 'danger' : 'warning'">预计 {{ row.daysLeft }} 天后</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="系统建议补货量" width="150" align="center">
          <template #default="{ row }">
            <span class="text-primary font-bold text-lg">{{ row.suggestQty }}</span>
          </template>
        </el-table-column>
        <el-table-column label="实际调整量" width="150" align="center">
          <template #default="{ row }">
            <el-input-number v-model="row.actualQty" :min="0" size="small" style="width: 100px" />
          </template>
        </el-table-column>
        <el-table-column label="推荐供应商" width="150" show-overflow-tooltip>
          <template #default="{ row }">
            <div>{{ row.supplier }}</div>
            <div class="text-xs text-gray">预计交期: {{ row.leadTime }}天</div>
          </template>
        </el-table-column>
      </el-table>

      <div class="batch-actions mt-4 flex justify-between align-center">
        <div>
          已选择 <strong class="text-primary">{{ selectedCount }}</strong> 个商品
        </div>
        <el-button type="success" :disabled="selectedCount === 0" @click="createOrders">
          批量生成采购单
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const filterForm = reactive({
  type: 'urgent',
  warehouse: ''
})

const planData = ref([
  {
    sku: 'SKU-001-BLK-M',
    name: '无线蓝牙耳机 降噪 运动防水',
    available: 140,
    inTransit: 0,
    dailySales: 15,
    daysLeft: 9,
    suggestQty: 500,
    actualQty: 500,
    supplier: '深圳华强电子厂',
    leadTime: 7
  },
  {
    sku: 'SKU-003-BLU',
    name: '便携式迷你小风扇',
    available: 200,
    inTransit: 100,
    dailySales: 20,
    daysLeft: 15,
    suggestQty: 1000,
    actualQty: 1200,
    supplier: '义乌小商品制造厂',
    leadTime: 10
  }
])

const selectedCount = ref(0)

const handleSelectionChange = (val: any[]) => {
  selectedCount.value = val.length
}

const createOrders = () => {
  ElMessage.success(`成功生成 ${selectedCount.value} 张采购草稿单，请前往"采购单执行"查看`)
}
</script>

<style scoped lang="scss">
.purchase-plan {
  .mb-4 { margin-bottom: 20px; }
  .mb-3 { margin-bottom: 15px; }
  .mt-4 { margin-top: 16px; }
  .flex { display: flex; }
  .justify-between { justify-content: space-between; }
  .align-center { align-items: center; }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .font-bold { font-weight: bold; }
  .text-xs { font-size: 12px; }
  .text-lg { font-size: 16px; }
  .text-gray { color: #909399; }
  .text-primary { color: #409EFF; }

  .stock-info {
    display: flex;
    flex-direction: column;
    text-align: left;
    font-size: 13px;
    gap: 4px;
    strong { color: #303133; }
  }
}
</style>
