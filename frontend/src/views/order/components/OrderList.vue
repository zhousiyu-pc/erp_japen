<template>
  <div class="order-list">
    <!-- Advanced Search / Filters -->
    <el-card shadow="never" class="filter-card">
      <el-form :inline="true" :model="searchForm" size="default">
        <el-form-item label="平台">
          <el-select v-model="searchForm.platform" placeholder="全部平台" clearable style="width: 150px">
            <el-option label="Amazon 日本站" value="amazon" />
            <el-option label="Rakuten 乐天" value="rakuten" />
            <el-option label="Yahoo 雅虎" value="yahoo" />
            <el-option label="TikTok 小店" value="tiktok" />
          </el-select>
        </el-form-item>
        <el-form-item label="店铺">
          <el-select v-model="searchForm.store" placeholder="全部店铺" clearable style="width: 150px">
            <el-option label="JP-Store-A" value="store_a" />
            <el-option label="Rakuten-Main" value="store_b" />
          </el-select>
        </el-form-item>
        <el-form-item label="订单类型">
          <el-select v-model="searchForm.orderType" placeholder="全部" clearable style="width: 120px">
            <el-option label="FBA" value="fba" />
            <el-option label="FBM" value="fbm" />
            <el-option label="自发货" value="self" />
            <el-option label="预售" value="presell" />
          </el-select>
        </el-form-item>
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderId" placeholder="平台单号 / 系统单号" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon> 搜索
          </el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="primary" link>展开高级搜索</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Status Tabs & Actions -->
    <div class="operation-bar">
      <el-tabs v-model="activeStatus" @tab-change="handleStatusChange">
        <el-tab-pane label="全部" name="all" />
        <el-tab-pane label="新订单 (风控审核)" name="new" />
        <el-tab-pane label="待分仓" name="pending_route" />
        <el-tab-pane label="待拣货" name="pending_pick" />
        <el-tab-pane label="待发货" name="pending_ship" />
        <el-tab-pane label="已发货" name="shipped" />
        <el-tab-pane label="已完成" name="completed" />
        <el-tab-pane label="已关闭" name="closed" />
      </el-tabs>

      <div class="actions">
        <el-button type="primary" size="small">自动分仓</el-button>
        <el-button type="success" size="small">合并发货</el-button>
        <el-button size="small">拆分订单</el-button>
        <el-button type="warning" plain size="small">导出订单</el-button>
      </div>
    </div>

    <!-- Order Table -->
    <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="订单信息" min-width="250">
        <template #default="{ row }">
          <div class="order-info">
            <div class="header">
              <el-tag size="small" :type="getPlatformTag(row.platform)">{{ row.platform }}</el-tag>
              <span class="store-name">{{ row.store }}</span>
            </div>
            <div class="ids">
              <span>系统单号: <el-link type="primary">{{ row.sysOrderId }}</el-link></span>
              <span class="ml-2">平台单号: {{ row.platformOrderId }}</span>
            </div>
            <div class="time">下单时间: {{ row.orderTime }}</div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="商品信息" min-width="200">
        <template #default="{ row }">
          <div v-for="(item, index) in row.items" :key="index" class="product-item">
            <el-image :src="item.image" class="product-img" fit="cover">
              <template #error>
                <div class="image-slot"><el-icon><Picture /></el-icon></div>
              </template>
            </el-image>
            <div class="product-detail">
              <div class="sku">{{ item.sku }}</div>
              <div class="qty">x {{ item.quantity }}</div>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="买家/收件人" min-width="150">
        <template #default="{ row }">
          <div>{{ row.buyerName }}</div>
          <div class="text-gray">{{ row.region }}</div>
        </template>
      </el-table-column>
      <el-table-column label="订单金额" width="120" align="right">
        <template #default="{ row }">
          <div class="amount">¥ {{ row.amount }}</div>
          <el-tag size="small" type="info" class="mt-1">{{ row.orderType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">{{ row.statusLabel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" fixed="right" align="center">
        <template #default="{ row }">
          <el-button link type="primary" size="small">详情</el-button>
          <el-button link type="success" size="small" v-if="row.status === 'pending_route'">分仓</el-button>
          <el-button link type="warning" size="small" v-if="row.status === 'shipped'">售后</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Pagination -->
    <div class="pagination-container mt-4 flex justify-end">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSearch"
        @current-change="handleSearch"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'

const searchForm = reactive({
  platform: '',
  store: '',
  orderType: '',
  orderId: ''
})

const activeStatus = ref('all')
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(45)

const tableData = ref([
  {
    platform: 'Amazon',
    store: 'JP-Store-A',
    sysOrderId: 'SO202603020001',
    platformOrderId: '114-1234567-8901234',
    orderTime: '2026-03-02 09:15:22',
    buyerName: '田中 太郎',
    region: '東京都 渋谷区',
    amount: '4,500',
    orderType: 'FBA',
    status: 'shipped',
    statusLabel: '已发货',
    items: [
      { sku: 'SKU-001-BLK-M', quantity: 1, image: '' }
    ]
  },
  {
    platform: 'Rakuten',
    store: 'Rakuten-Main',
    sysOrderId: 'SO202603020002',
    platformOrderId: 'RK-99887766',
    orderTime: '2026-03-02 10:05:10',
    buyerName: '佐藤 花子',
    region: '大阪府 大阪市',
    amount: '8,200',
    orderType: '自发货',
    status: 'pending_route',
    statusLabel: '待分仓',
    items: [
      { sku: 'SKU-002-WHT', quantity: 2, image: '' },
      { sku: 'SKU-003-BLU', quantity: 1, image: '' }
    ]
  }
])

const handleSearch = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
  }, 500)
}

const resetSearch = () => {
  searchForm.platform = ''
  searchForm.store = ''
  searchForm.orderType = ''
  searchForm.orderId = ''
  handleSearch()
}

const handleStatusChange = () => {
  handleSearch()
}

const getPlatformTag = (platform: string) => {
  const map: Record<string, string> = {
    'Amazon': 'warning',
    'Rakuten': 'danger',
    'Yahoo': 'success'
  }
  return map[platform] || 'info'
}

const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    'new': 'danger',
    'pending_route': 'warning',
    'pending_pick': 'primary',
    'pending_ship': 'primary',
    'shipped': 'success',
    'completed': 'success',
    'closed': 'info'
  }
  return map[status] || 'info'
}
</script>

<style scoped lang="scss">
.order-list {
  .filter-card {
    margin-bottom: 20px;
  }
  
  .operation-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #ebeef5;
    margin-bottom: 15px;
    padding-bottom: 0;

    :deep(.el-tabs__header) {
      margin-bottom: 0;
    }

    .actions {
      margin-bottom: 10px;
    }
  }

  .order-info {
    .header {
      margin-bottom: 5px;
      .store-name {
        margin-left: 8px;
        font-weight: bold;
      }
    }
    .ids {
      font-size: 13px;
      margin-bottom: 5px;
    }
    .time {
      font-size: 12px;
      color: #909399;
    }
  }

  .product-item {
    display: flex;
    align-items: center;
    margin-bottom: 5px;
    
    &:last-child {
      margin-bottom: 0;
    }

    .product-img {
      width: 40px;
      height: 40px;
      border-radius: 4px;
      border: 1px solid #ebeef5;
      margin-right: 10px;
    }

    .product-detail {
      flex: 1;
      display: flex;
      justify-content: space-between;
      align-items: center;
      .sku { font-size: 13px; }
      .qty { font-weight: bold; }
    }
  }

  .amount {
    font-weight: bold;
    color: #f56c6c;
  }

  .text-gray {
    color: #909399;
    font-size: 12px;
    margin-top: 4px;
  }
  
  .mt-1 { margin-top: 4px; }
  .mt-4 { margin-top: 16px; }
  .ml-2 { margin-left: 8px; }
  .flex { display: flex; }
  .justify-end { justify-content: flex-end; }
  
  .image-slot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: #f5f7fa;
    color: #909399;
  }
}
</style>
