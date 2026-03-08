<template>
  <div class="product-listing">
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span>多平台 Listing 管理</span>
          <el-button type="primary" size="small">同步最新状态</el-button>
        </div>
      </template>

      <el-row :gutter="20">
        <el-col :span="6">
          <!-- Platform Tree/List -->
          <el-menu default-active="1" class="platform-menu">
            <el-menu-item index="1">
              <el-icon><Platform /></el-icon>
              <span>全部平台 (120)</span>
            </el-menu-item>
            <el-menu-item index="2">
              <span>Amazon 日本站 (85)</span>
            </el-menu-item>
            <el-menu-item index="3">
              <span>Rakuten 乐天 (20)</span>
            </el-menu-item>
            <el-menu-item index="4">
              <span>Yahoo 雅虎 (15)</span>
            </el-menu-item>
            <el-menu-item index="5">
              <span>TikTok Shop (0)</span>
            </el-menu-item>
          </el-menu>
        </el-col>
        <el-col :span="18">
          <!-- Listing Table -->
          <div class="filter-bar mb-3">
            <el-input placeholder="搜索 ASIN/SKU/标题" style="width: 250px; margin-right: 10px" />
            <el-select placeholder="状态" style="width: 120px; margin-right: 10px">
              <el-option label="在线" value="online" />
              <el-option label="下线" value="offline" />
              <el-option label="违规" value="violation" />
            </el-select>
            <el-button type="primary">搜索</el-button>
            <el-button>批量编辑</el-button>
          </div>

          <el-table :data="listingData" border style="width: 100%">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="platform" label="平台" width="100" />
            <el-table-column prop="store" label="店铺" width="120" />
            <el-table-column prop="itemId" label="ASIN/商品ID" width="130" />
            <el-table-column prop="sku" label="映射 SKU" width="130" />
            <el-table-column prop="price" label="售价" width="100">
              <template #default="{ row }">
                ¥ {{ row.price }}
              </template>
            </el-table-column>
            <el-table-column prop="stock" label="平台库存" width="90" />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === '在线' ? 'success' : 'danger'">{{ row.status }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" fixed="right">
              <template #default>
                <el-button link type="primary" size="small">编辑</el-button>
                <el-button link type="warning" size="small">下架</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const listingData = ref([
  {
    platform: 'Amazon',
    store: 'JP-Store-A',
    itemId: 'B08XXXXXXX',
    sku: 'SKU-001-BLK-M',
    price: '2,980',
    stock: 145,
    status: '在线'
  },
  {
    platform: 'Rakuten',
    store: 'Rakuten-Main',
    itemId: 'item-9921',
    sku: 'SKU-001-BLK-M',
    price: '3,100',
    stock: 50,
    status: '在线'
  },
  {
    platform: 'Amazon',
    store: 'JP-Store-B',
    itemId: 'B09YYYYYYY',
    sku: 'SKU-002-WHT',
    price: '1,500',
    stock: 0,
    status: '下线'
  }
])
</script>

<style scoped lang="scss">
.product-listing {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .platform-menu {
    border-right: none;
    background: #f8f9fa;
    border-radius: 4px;
  }
  .mb-3 {
    margin-bottom: 15px;
  }
}
</style>
