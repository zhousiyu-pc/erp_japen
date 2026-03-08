<template>
  <div class="review-management">
    <el-card shadow="never" class="mb-4">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-box text-center p-3">
            <div class="text-gray mb-2">平均店铺评分 (Amazon)</div>
            <div class="text-3xl text-primary font-bold">4.6</div>
            <el-rate v-model="amazonRate" disabled show-score text-color="#ff9900" class="mt-2 justify-center" />
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-box text-center p-3">
            <div class="text-gray mb-2">近30天新增 Review</div>
            <div class="text-3xl font-bold">128</div>
            <div class="text-xs text-success mt-2">↑ 12% 较上月</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-box text-center p-3">
            <div class="text-gray mb-2">待处理差评预警</div>
            <div class="text-3xl text-danger font-bold">5</div>
            <div class="text-xs text-gray mt-2">星级 <= 3 星</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-box text-center p-3">
            <div class="text-gray mb-2">差评处理率</div>
            <div class="text-3xl text-success font-bold">85%</div>
            <div class="text-xs text-gray mt-2">目标: 100%</div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-card shadow="never">
      <div class="filter-bar mb-4 flex justify-between">
        <div class="left flex">
          <el-select placeholder="全部平台" style="width: 150px" class="mr-2">
            <el-option label="Amazon 日本站" value="amazon" />
            <el-option label="Rakuten 乐天" value="rakuten" />
          </el-select>
          <el-select v-model="filterStar" placeholder="星级筛选" style="width: 120px" class="mr-2">
            <el-option label="全部星级" value="all" />
            <el-option label="1 - 3 星 (差评)" value="bad" />
            <el-option label="4 - 5 星 (好评)" value="good" />
          </el-select>
          <el-input placeholder="搜索 ASIN/SKU/内容" style="width: 250px" />
        </div>
        <div class="right">
          <el-button type="warning" plain><el-icon><Message /></el-icon> 批量索评邀请</el-button>
        </div>
      </div>

      <el-table :data="reviewData" border style="width: 100%">
        <el-table-column label="商品信息" width="250">
          <template #default="{ row }">
            <div class="flex items-start">
              <el-image style="width: 40px; height: 40px" class="mr-2 bg-gray-100" />
              <div>
                <div class="font-bold text-xs text-primary">{{ row.asin }}</div>
                <div class="text-xs text-gray mt-1 text-ellipsis">{{ row.productName }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="评价星级" width="120" align="center">
          <template #default="{ row }">
            <el-rate v-model="row.star" disabled />
          </template>
        </el-table-column>
        <el-table-column prop="content" label="评价内容" min-width="300">
          <template #default="{ row }">
            <div class="font-bold text-sm">{{ row.title }}</div>
            <div class="text-sm mt-1">{{ row.content }}</div>
            <div class="text-xs text-gray mt-1 bg-gray-50 p-1" v-if="row.translation">[翻译]: {{ row.translation }}</div>
            <div class="mt-2" v-if="row.tags.length">
              <el-tag size="small" type="danger" effect="plain" v-for="tag in row.tags" :key="tag" class="mr-1">
                敏感词: {{ tag }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="date" label="评价时间" width="120" align="center" />
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" size="small">联系买家</el-button>
            <el-button link type="warning" size="small" v-if="row.star <= 3">建售后单</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="mt-4 flex justify-end">
        <el-pagination background layout="prev, pager, next" :total="128" />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const amazonRate = ref(4.6)
const filterStar = ref('bad')

const reviewData = ref([
  {
    asin: 'B08XXXXXXX',
    productName: '无线蓝牙耳机 降噪 运动防水',
    star: 2,
    title: 'すぐに壊れました',
    content: '1週間使っただけで片耳が聞こえなくなりました。最悪です。',
    translation: '只用了1周单耳就听不见了。最差劲。',
    tags: ['壊れた', '最悪'],
    date: '2026-03-01'
  },
  {
    asin: 'B09YYYYYYY',
    productName: '日式简约桌面收纳盒',
    star: 5,
    title: 'とても便利！',
    content: 'デスクの上がすっきりしました。デザインもシンプルで気に入っています。',
    translation: '桌面上变得很整洁。设计也很简约，很喜欢。',
    tags: [],
    date: '2026-02-28'
  }
])
</script>

<style scoped lang="scss">
.review-management {
  .mb-2 { margin-bottom: 8px; }
  .mb-4 { margin-bottom: 16px; }
  .mt-1 { margin-top: 4px; }
  .mt-2 { margin-top: 8px; }
  .mt-4 { margin-top: 16px; }
  .mr-1 { margin-right: 4px; }
  .mr-2 { margin-right: 8px; }
  .p-1 { padding: 4px; }
  .p-3 { padding: 12px; }
  
  .flex { display: flex; }
  .justify-between { justify-content: space-between; }
  .justify-center { justify-content: center; }
  .justify-end { justify-content: flex-end; }
  .items-start { align-items: flex-start; }
  
  .text-center { text-align: center; }
  .text-xs { font-size: 12px; }
  .text-sm { font-size: 13px; }
  .text-3xl { font-size: 24px; }
  
  .font-bold { font-weight: bold; }
  .text-gray { color: #909399; }
  .text-primary { color: #409EFF; }
  .text-success { color: #67C23A; }
  .text-danger { color: #F56C6C; }
  
  .bg-gray-50 { background-color: #f8f9fa; border-radius: 4px; }
  .bg-gray-100 { background-color: #f0f2f5; }
  
  .text-ellipsis {
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }

  .stat-box {
    border-right: 1px solid #ebeef5;
  }
  .el-col:last-child .stat-box {
    border-right: none;
  }
}
</style>
