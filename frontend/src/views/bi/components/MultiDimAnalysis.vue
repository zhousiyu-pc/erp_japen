<template>
  <div class="multi-dim-analysis">
    <el-card shadow="never" class="mb-4">
      <div class="header-action">
        <h3>多维度交叉分析报表</h3>
        <el-button type="primary" plain>保存当前视图为预设</el-button>
      </div>
      <el-alert
        title="您可以自由拖拽维度和指标，生成您需要的定制化报表"
        type="info"
        show-icon
        class="mt-3 mb-3"
      />

      <div class="drag-area flex mb-4">
        <!-- Dimensions -->
        <div class="dim-box mr-4">
          <div class="box-title text-sm text-gray mb-2">可拖拽维度 (行/列)</div>
          <div class="tags-container">
            <el-tag effect="plain" class="mr-2 mb-2 cursor-move"><el-icon><Grid /></el-icon> 平台</el-tag>
            <el-tag effect="plain" class="mr-2 mb-2 cursor-move"><el-icon><Grid /></el-icon> 店铺</el-tag>
            <el-tag effect="plain" class="mr-2 mb-2 cursor-move"><el-icon><Grid /></el-icon> 商品类目</el-tag>
            <el-tag effect="plain" class="mr-2 mb-2 cursor-move"><el-icon><Grid /></el-icon> 品牌</el-tag>
            <el-tag effect="plain" class="mr-2 mb-2 cursor-move"><el-icon><Grid /></el-icon> 销售团队</el-tag>
            <el-tag effect="plain" class="mr-2 mb-2 cursor-move"><el-icon><Grid /></el-icon> 年/月/日</el-tag>
          </div>
        </div>
        <!-- Metrics -->
        <div class="metric-box">
          <div class="box-title text-sm text-gray mb-2">可拖拽指标 (值)</div>
          <div class="tags-container">
            <el-tag type="success" effect="plain" class="mr-2 mb-2 cursor-move"><el-icon><DataLine /></el-icon> 销售额</el-tag>
            <el-tag type="success" effect="plain" class="mr-2 mb-2 cursor-move"><el-icon><DataLine /></el-icon> 订单量</el-tag>
            <el-tag type="success" effect="plain" class="mr-2 mb-2 cursor-move"><el-icon><DataLine /></el-icon> 客单价</el-tag>
            <el-tag type="success" effect="plain" class="mr-2 mb-2 cursor-move"><el-icon><DataLine /></el-icon> 毛利润</el-tag>
            <el-tag type="success" effect="plain" class="mr-2 mb-2 cursor-move"><el-icon><DataLine /></el-icon> 退货率</el-tag>
            <el-tag type="success" effect="plain" class="mr-2 mb-2 cursor-move"><el-icon><DataLine /></el-icon> 广告占比</el-tag>
          </div>
        </div>
      </div>

      <!-- Pivot Table Mockup -->
      <div class="pivot-table-container">
        <div class="axis-config mb-2">
          <div class="row-axis flex items-center bg-gray-50 p-2 rounded mb-2">
            <span class="text-xs text-gray w-12">行维度:</span>
            <el-tag closable class="mr-2">平台</el-tag>
            <el-tag closable class="mr-2">店铺</el-tag>
            <span class="text-xs text-gray italic ml-2">拖拽至此处...</span>
          </div>
          <div class="col-axis flex items-center bg-gray-50 p-2 rounded">
            <span class="text-xs text-gray w-12">列指标:</span>
            <el-tag type="success" closable class="mr-2">销售额</el-tag>
            <el-tag type="success" closable class="mr-2">毛利润</el-tag>
            <el-tag type="success" closable class="mr-2">毛利率</el-tag>
            <span class="text-xs text-gray italic ml-2">拖拽至此处...</span>
          </div>
        </div>

        <el-table :data="pivotData" border style="width: 100%" class="mt-3">
          <el-table-column prop="platform" label="平台" width="150" />
          <el-table-column prop="store" label="店铺" width="150" />
          <el-table-column prop="sales" label="销售额(¥)" align="right" />
          <el-table-column prop="profit" label="毛利润(¥)" align="right" />
          <el-table-column prop="margin" label="毛利率" align="right" />
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const pivotData = ref([
  { platform: 'Amazon', store: 'JP-Store-A', sales: '850,000', profit: '280,000', margin: '32.9%' },
  { platform: 'Amazon', store: 'JP-Store-B', sales: '320,000', profit: '95,000', margin: '29.6%' },
  { platform: 'Rakuten', store: 'Rakuten-Main', sales: '450,000', profit: '120,000', margin: '26.6%' },
  { platform: 'Yahoo', store: 'Yahoo-Shop', sales: '180,000', profit: '45,000', margin: '25.0%' }
])
</script>

<style scoped lang="scss">
.multi-dim-analysis {
  .mb-2 { margin-bottom: 8px; }
  .mb-3 { margin-bottom: 12px; }
  .mb-4 { margin-bottom: 20px; }
  .mr-2 { margin-right: 8px; }
  .mr-4 { margin-right: 16px; }
  .ml-2 { margin-left: 8px; }
  .mt-3 { margin-top: 12px; }
  .p-2 { padding: 8px; }
  
  .flex { display: flex; }
  .items-center { align-items: center; }
  .rounded { border-radius: 4px; }
  
  .text-xs { font-size: 12px; }
  .text-sm { font-size: 13px; }
  .text-gray { color: #909399; }
  .italic { font-style: italic; }
  .w-12 { width: 48px; display: inline-block; }
  
  .bg-gray-50 { background-color: #f8f9fa; border: 1px dashed #dcdfe6; }

  .header-action {
    display: flex;
    justify-content: space-between;
    align-items: center;
    h3 { margin: 0; }
  }

  .cursor-move { cursor: move; }

  .dim-box, .metric-box {
    flex: 1;
    border: 1px solid #ebeef5;
    padding: 15px;
    border-radius: 4px;
  }
}
</style>
