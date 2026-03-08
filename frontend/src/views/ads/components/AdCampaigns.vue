<template>
  <div class="ad-campaigns">
    <el-card shadow="never" class="mb-4">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="平台与店铺">
          <el-cascader
            v-model="searchForm.store"
            :options="storeOptions"
            placeholder="请选择"
            clearable
          />
        </el-form-item>
        <el-form-item label="广告类型">
          <el-select v-model="searchForm.type" placeholder="全部" clearable style="width: 120px">
            <el-option label="SP (商品推广)" value="sp" />
            <el-option label="SB (品牌推广)" value="sb" />
            <el-option label="SD (展示推广)" value="sd" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 100px">
            <el-option label="运行中" value="enabled" />
            <el-option label="已暂停" value="paused" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input v-model="searchForm.keyword" placeholder="搜索广告活动名称..." clearable style="width: 200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary"><el-icon><Search /></el-icon> 查询</el-button>
        </el-form-item>
      </el-form>

      <div class="toolbar mt-2">
        <el-button type="primary"><el-icon><Plus /></el-icon> 创建广告活动</el-button>
        <el-button>调整预算</el-button>
        <el-button>同步最新数据</el-button>
      </div>
    </el-card>

    <el-table :data="campaignData" border style="width: 100%">
      <el-table-column type="selection" width="40" />
      <el-table-column label="状态" width="70" align="center">
        <template #default="{ row }">
          <el-switch v-model="row.active" size="small" />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="广告活动" min-width="200" show-overflow-tooltip>
        <template #default="{ row }">
          <el-link type="primary" class="font-bold">{{ row.name }}</el-link>
          <div class="text-xs text-gray mt-1">
            {{ row.platform }} | {{ row.store }} | <el-tag size="small" type="info">{{ row.type }}</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="日预算" width="120" align="right">
        <template #default="{ row }">
          <div class="flex items-center justify-end editable-cell">
            ¥ {{ row.budget }} <el-icon class="ml-1 text-primary cursor-pointer"><EditPen /></el-icon>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="spend" label="花费" width="100" align="right" />
      <el-table-column prop="sales" label="销售额" width="120" align="right" />
      <el-table-column label="ACOS" width="100" align="right">
        <template #default="{ row }">
          <span :class="row.acos > 20 ? 'text-danger' : 'text-success'">{{ row.acos }}%</span>
        </template>
      </el-table-column>
      <el-table-column prop="roas" label="ROAS" width="80" align="right" />
      <el-table-column prop="cpc" label="CPC" width="80" align="right" />
      <el-table-column prop="orders" label="订单数" width="80" align="center" />
      <el-table-column label="操作" width="100" fixed="right" align="center">
        <template #default>
          <el-button link type="primary" size="small">广告组</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="mt-4 flex justify-end">
      <el-pagination background layout="prev, pager, next" :total="50" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'

const searchForm = reactive({
  store: [],
  type: '',
  status: '',
  keyword: ''
})

const storeOptions = [
  {
    value: 'amazon',
    label: 'Amazon 日本站',
    children: [
      { value: 'store_a', label: 'JP-Store-A' },
      { value: 'store_b', label: 'JP-Store-B' }
    ]
  }
]

const campaignData = ref([
  {
    active: true,
    name: 'Auto-耳机-2026秋季大促',
    platform: 'Amazon',
    store: 'JP-Store-A',
    type: 'SP',
    budget: '5,000',
    spend: '2,450',
    sales: '35,000',
    acos: 7.0,
    roas: 14.2,
    cpc: '15.5',
    orders: 45
  },
  {
    active: true,
    name: 'Manual-竞品词狙击',
    platform: 'Amazon',
    store: 'JP-Store-A',
    type: 'SP',
    budget: '10,000',
    spend: '8,900',
    sales: '32,000',
    acos: 27.8,
    roas: 3.5,
    cpc: '45.0',
    orders: 12
  },
  {
    active: false,
    name: 'SB-品牌视频推广-收纳盒',
    platform: 'Amazon',
    store: 'JP-Store-B',
    type: 'SB',
    budget: '3,000',
    spend: '1,200',
    sales: '8,000',
    acos: 15.0,
    roas: 6.6,
    cpc: '22.0',
    orders: 8
  }
])
</script>

<style scoped lang="scss">
.ad-campaigns {
  .mb-4 { margin-bottom: 20px; }
  .mt-1 { margin-top: 4px; }
  .mt-2 { margin-top: 8px; }
  .mt-4 { margin-top: 16px; }
  .ml-1 { margin-left: 4px; }
  
  .flex { display: flex; }
  .items-center { align-items: center; }
  .justify-end { justify-content: flex-end; }
  
  .font-bold { font-weight: bold; }
  .text-xs { font-size: 12px; }
  .text-gray { color: #909399; }
  .text-primary { color: #409EFF; }
  .text-success { color: #67C23A; }
  .text-danger { color: #F56C6C; }
  
  .cursor-pointer { cursor: pointer; }
  
  .editable-cell {
    &:hover {
      .el-icon { opacity: 1; }
    }
    .el-icon {
      opacity: 0.3;
      transition: opacity 0.2s;
    }
  }
}
</style>
