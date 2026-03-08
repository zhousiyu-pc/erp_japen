<template>
  <div class="logistics-analysis">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover" header="近30天物流成本构成" class="chart-card">
          <div class="chart-placeholder">
            <el-icon :size="40" color="#C0C4CC"><PieChart /></el-icon>
            <p class="mt-2 text-gray">各物流商费用占比分布图</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" header="末端签收时效分布" class="chart-card">
          <div class="chart-placeholder">
            <el-icon :size="40" color="#C0C4CC"><DataLine /></el-icon>
            <p class="mt-2 text-gray">各地区签收时效曲线图 (平均 1.5 天)</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="mt-4">
      <template #header>
        <div class="header-with-icon">
          <span><el-icon color="#E6A23C"><Warning /></el-icon> 异常线路与承运商预警</span>
        </div>
      </template>

      <el-table :data="warningData" border style="width: 100%">
        <el-table-column prop="route" label="线路/区域" width="150" />
        <el-table-column prop="carrier" label="承运商" width="120" />
        <el-table-column prop="issue" label="异常表现" min-width="200" show-overflow-tooltip />
        <el-table-column prop="impact" label="受影响包裹数" width="120" align="center">
          <template #default="{ row }">
            <span class="text-danger font-bold">{{ row.impact }}</span>
          </template>
        </el-table-column>
        <el-table-column label="系统建议" min-width="250">
          <template #default="{ row }">
            <span class="text-primary">{{ row.suggestion }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" align="center">
          <template #default>
            <el-button link type="primary" size="small">切换规则</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const warningData = ref([
  {
    route: '关东 -> 北海道',
    carrier: '佐川急便',
    issue: '连续 3 天平均延误超过 48 小时，妥投率降至 85%',
    impact: 42,
    suggestion: '建议临时将北海道方向订单切换至【日本邮政】，预计单均成本增加 ¥150，但时效可保障。'
  },
  {
    route: '深圳 -> 大阪 (干线)',
    carrier: '中日快速海运专线',
    issue: '近期清关查验率飙升至 15%，整体时效拉长 2-3 天',
    impact: 1250,
    suggestion: '急需补货的热销 SKU 建议转走【空运包裹】通道，避免断货。'
  }
])
</script>

<style scoped lang="scss">
.logistics-analysis {
  .mt-4 { margin-top: 16px; }
  .mt-2 { margin-top: 8px; }
  
  .chart-card {
    height: 350px;
    display: flex;
    flex-direction: column;
    :deep(.el-card__body) {
      flex: 1;
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }

  .chart-placeholder {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }

  .header-with-icon {
    display: flex;
    align-items: center;
    gap: 8px;
    font-weight: bold;
  }

  .text-danger { color: #F56C6C; }
  .text-primary { color: #409EFF; }
  .text-gray { color: #909399; }
  .font-bold { font-weight: bold; }
}
</style>
