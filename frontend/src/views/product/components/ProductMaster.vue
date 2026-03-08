<template>
  <div class="product-master">
    <!-- Search Bar -->
    <el-form :inline="true" :model="searchForm" class="search-form">
      <el-form-item label="商品名称/SPU">
        <el-input v-model="searchForm.keyword" placeholder="请输入名称或SPU" clearable />
      </el-form-item>
      <el-form-item label="商品类目">
        <el-cascader
          v-model="searchForm.category"
          :options="categoryOptions"
          placeholder="请选择类目"
          clearable
        />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 120px">
          <el-option label="在售" value="active" />
          <el-option label="停售" value="inactive" />
          <el-option label="草稿" value="draft" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleSearch">
          <el-icon><Search /></el-icon> 查询
        </el-button>
        <el-button @click="resetSearch">
          <el-icon><Refresh /></el-icon> 重置
        </el-button>
      </el-form-item>
    </el-form>

    <!-- Toolbar -->
    <div class="toolbar">
      <el-button type="primary" @click="openCreateDialog">
        <el-icon><Plus /></el-icon> 新建商品
      </el-button>
      <el-button type="success" plain>
        <el-icon><Upload /></el-icon> 批量导入
      </el-button>
      <el-button type="warning" plain>
        <el-icon><Download /></el-icon> 导出
      </el-button>
    </div>

    <!-- Table -->
    <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column type="selection" width="55" />
      <el-table-column label="主图" width="80" align="center">
        <template #default="{ row }">
          <el-image
            style="width: 50px; height: 50px"
            :src="row.image"
            :preview-src-list="[row.image]"
            fit="cover"
            preview-teleported
          >
            <template #error>
              <div class="image-slot">
                <el-icon><Picture /></el-icon>
              </div>
            </template>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="spu" label="SPU" width="120" />
      <el-table-column prop="name" label="商品名称" min-width="200" show-overflow-tooltip />
      <el-table-column prop="category" label="类目" width="150" />
      <el-table-column prop="variantsCount" label="变体数" width="80" align="center" />
      <el-table-column label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">{{ getStatusLabel(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="最后更新时间" width="160" />
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="editProduct(row)">编辑</el-button>
          <el-button size="small" type="success" link @click="createListing(row)">一键分发</el-button>
          <el-button size="small" type="danger" link>删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- Pagination -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- Create/Edit Drawer -->
    <ProductFormDrawer v-model:visible="drawerVisible" :product-data="currentProduct" @saved="handleSaved" />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import ProductFormDrawer from './ProductFormDrawer.vue'

// Search Form
const searchForm = reactive({
  keyword: '',
  category: [],
  status: ''
})

const categoryOptions = [
  {
    value: 'electronics',
    label: '3C数码',
    children: [
      { value: 'phone', label: '手机配件' },
      { value: 'audio', label: '影音电器' }
    ]
  },
  {
    value: 'home',
    label: '家居生活',
    children: [
      { value: 'kitchen', label: '厨房用品' },
      { value: 'storage', label: '收纳整理' }
    ]
  }
]

// Table Data
const loading = ref(false)
const tableData = ref([
  {
    id: 1,
    spu: 'SPU-ELEC-001',
    name: '【2026新款】无线蓝牙耳机 降噪 运动防水 (日文: ワイヤレスイヤホン ノイズキャンセリング)',
    category: '3C数码 / 影音电器',
    variantsCount: 3,
    status: 'active',
    image: 'https://via.placeholder.com/150',
    updateTime: '2026-03-02 10:23:45'
  },
  {
    id: 2,
    spu: 'SPU-HOME-002',
    name: '日式简约桌面收纳盒 多功能化妆品整理盒',
    category: '家居生活 / 收纳整理',
    variantsCount: 5,
    status: 'draft',
    image: 'https://via.placeholder.com/150',
    updateTime: '2026-03-01 15:10:22'
  },
  {
    id: 3,
    spu: 'SPU-ELEC-003',
    name: 'iPhone 15 磁吸手机壳 防摔透明保护套',
    category: '3C数码 / 手机配件',
    variantsCount: 12,
    status: 'inactive',
    image: '',
    updateTime: '2026-02-28 09:45:11'
  }
])

// Pagination
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(3)

// Drawer
const drawerVisible = ref(false)
const currentProduct = ref(null)

// Methods
const handleSearch = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
  }, 500)
}

const resetSearch = () => {
  searchForm.keyword = ''
  searchForm.category = []
  searchForm.status = ''
  handleSearch()
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  handleSearch()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  handleSearch()
}

const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    active: 'success',
    inactive: 'danger',
    draft: 'info'
  }
  return map[status] || 'info'
}

const getStatusLabel = (status: string) => {
  const map: Record<string, string> = {
    active: '在售',
    inactive: '停售',
    draft: '草稿'
  }
  return map[status] || '未知'
}

const openCreateDialog = () => {
  currentProduct.value = null
  drawerVisible.value = true
}

const editProduct = (row: any) => {
  currentProduct.value = { ...row }
  drawerVisible.value = true
}

const createListing = (row: any) => {
  // Mock action for one-click distribution
  console.log('Distribute to platforms:', row)
}

const handleSaved = () => {
  drawerVisible.value = false
  handleSearch()
}
</script>

<style scoped lang="scss">
.product-master {
  .search-form {
    background: #f8f9fa;
    padding: 18px 18px 0;
    border-radius: 4px;
    margin-bottom: 20px;
  }

  .toolbar {
    margin-bottom: 15px;
  }

  .image-slot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: #f5f7fa;
    color: #909399;
    font-size: 20px;
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
