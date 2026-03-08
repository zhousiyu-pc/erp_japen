<template>
  <div class="ai-predictions">
    <el-row :gutter="20">
      <el-col :span="14">
        <el-card shadow="never" header="AI 销量预测 (未来 30 天)" class="mb-4">
          <div class="flex justify-between mb-3">
            <el-select v-model="targetSku" placeholder="选择核心 SKU" style="width: 250px">
              <el-option label="SKU-001-BLK-M (无线蓝牙耳机)" value="sku1" />
              <el-option label="SKU-002-WHT (日式收纳盒)" value="sku2" />
            </el-select>
            <el-button type="primary" plain>重新生成模型</el-button>
          </div>
          
          <div class="chart-placeholder" style="height: 350px;">
            <el-icon :size="40" color="#C0C4CC"><TrendCharts /></el-icon>
            <p class="mt-2 text-gray text-center">
              折线图：展示历史实际销量与AI预测未来30天销量曲线<br>
              <span class="text-xs">包含季节性波动与历史大促拟合</span>
            </p>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="10">
        <el-card shadow="never" header="AI 智能预警与建议" class="mb-4 alert-card">
          <el-alert
            title="库存断货红色预警"
            type="error"
            description="基于预测模型，SKU-001-BLK-M 预计在 12 天后断货，但海运补货周期需 15 天。建议立即空运补货 500 件以应对断货期。"
            show-icon
            class="mb-3"
            :closable="false"
          >
            <template #default>
              <div class="mt-2"><el-button size="small" type="danger" plain>一键生成空运采购单</el-button></div>
            </template>
          </el-alert>
          
          <el-alert
            title="广告超支与利润下滑"
            type="warning"
            description="近 3 天整体 ACOS 异常升高至 28%，导致净利润率跌破目标值 15%。建议启动广告降价规则。"
            show-icon
            class="mb-3"
            :closable="false"
          >
            <template #default>
              <div class="mt-2"><el-button size="small" type="warning" plain>前往广告规则</el-button></div>
            </template>
          </el-alert>
          
          <el-alert
            title="上新爆款潜质提醒"
            type="success"
            description="新品 SKU-005-MINI 上架首周自然转化率达 12%，远超类目平均水平。建议加大站内广告预算并尝试站外引流。"
            show-icon
            :closable="false"
          />
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never">
      <template #header>
        <div class="flex items-center">
          <el-icon class="mr-2 text-primary" :size="20"><Microphone /></el-icon>
          <span class="font-bold">自然语言数据问答 (BI Copilot)</span>
        </div>
      </template>
      <div class="chat-mockup bg-gray-50 p-4 rounded">
        <div class="message user mb-4 flex justify-end">
          <div class="msg-bubble bg-primary text-white p-2 rounded">
            "近 7 天日本站盈利最好的前 3 个 SKU 是哪些？广告花费分别是多少？"
          </div>
        </div>
        <div class="message ai flex items-start">
          <el-avatar class="mr-2 bg-blue-100 text-primary">AI</el-avatar>
          <div class="msg-bubble bg-white p-3 rounded border" style="width: 60%">
            <p class="mb-2">根据近 7 天数据，日本站盈利最好的前 3 个 SKU 如下：</p>
            <el-table :data="aiAnswerData" size="small" border>
              <el-table-column prop="sku" label="SKU" />
              <el-table-column prop="profit" label="毛利润" align="right" />
              <el-table-column prop="adSpend" label="广告花费" align="right" />
            </el-table>
            <p class="mt-2 text-xs text-gray">其中 SKU-001 表现尤为突出，建议保持当前广告策略。</p>
          </div>
        </div>
      </div>
      <div class="chat-input mt-3 flex">
        <el-input placeholder="输入您想查询的业务数据问题，例如：上个月退货率最高的类目是？" class="mr-2" />
        <el-button type="primary"><el-icon><Position /></el-icon> 发送</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const targetSku = ref('sku1')

const aiAnswerData = ref([
  { sku: 'SKU-001-BLK', profit: '¥ 25,000', adSpend: '¥ 3,200' },
  { sku: 'SKU-002-WHT', profit: '¥ 18,500', adSpend: '¥ 4,500' },
  { sku: 'SKU-003-BLU', profit: '¥ 12,000', adSpend: '¥ 1,800' }
])
</script>

<style scoped lang="scss">
.ai-predictions {
  .mb-2 { margin-bottom: 8px; }
  .mb-3 { margin-bottom: 12px; }
  .mb-4 { margin-bottom: 20px; }
  .mt-2 { margin-top: 8px; }
  .mt-3 { margin-top: 12px; }
  .mr-2 { margin-right: 8px; }
  .p-2 { padding: 8px; }
  .p-3 { padding: 12px; }
  .p-4 { padding: 16px; }

  .flex { display: flex; }
  .justify-between { justify-content: space-between; }
  .justify-end { justify-content: flex-end; }
  .items-center { align-items: center; }
  .items-start { align-items: flex-start; }

  .text-xs { font-size: 12px; }
  .text-gray { color: #909399; }
  .text-center { text-align: center; }
  .text-white { color: #fff; }
  .text-primary { color: #409EFF; }
  .font-bold { font-weight: bold; }

  .bg-gray-50 { background-color: #f8f9fa; }
  .bg-white { background-color: #fff; }
  .bg-primary { background-color: #409EFF; }
  .bg-blue-100 { background-color: #d9ecff; }
  
  .rounded { border-radius: 4px; }
  .border { border: 1px solid #ebeef5; }

  .chart-placeholder {
    background-color: #f8f9fa;
    border-radius: 4px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }

  .alert-card {
    height: 100%;
    :deep(.el-card__body) {
      height: calc(100% - 60px);
      overflow-y: auto;
    }
  }

  .chat-mockup {
    border: 1px solid #ebeef5;
    .msg-bubble {
      box-shadow: 0 2px 4px rgba(0,0,0,0.05);
    }
  }
}
</style>
