<template>
  <div class="ad-dashboard">
    <div class="filter-bar mb-4">
      <el-radio-group v-model="timeRange">
        <el-radio-button label="today">今日</el-radio-button>
        <el-radio-button label="yesterday">昨日</el-radio-button>
        <el-radio-button label="7days">近7天</el-radio-button>
        <el-radio-button label="30days">近30天</el-radio-button>
      </el-radio-group>
      <el-select v-model="platform" class="ml-4" style="width: 150px">
        <el-option label="全部平台" value="all" />
        <el-option label="Amazon 日本站" value="amazon" />
        <el-option label="Rakuten 乐天" value="rakuten" />
      </el-select>
    </div>

    <!-- KPIs -->
    <el-row :gutter="20" class="mb-4">
      <el-col :span="6" v-for="item in kpis" :key="item.title">
        <el-card shadow="hover" class="kpi-card">
          <div class="kpi-header">
            <span>{{ item.title }}</span>
            <el-tooltip :content="item.tip" placement="top">
              <el-icon class="text-gray"><InfoFilled /></el-icon>
            </el-tooltip>
          </div>
          <div class="kpi-value">{{ item.value }}</div>
          <div class="kpi-footer">
            较上期 
            <span :class="item.trend > 0 ? (item.inverseColor ? 'down' : 'up') : (item.inverseColor ? 'up' : 'down')">
              <el-icon><Top v-if="item.trend > 0" /><Bottom v-else /></el-icon>
              {{ Math.abs(item.trend) }}%
            </span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="16">
        <el-card shadow="never" header="广告花费与销售额趋势">
          <div class="chart-placeholder">
            <el-icon :size="40" color="#C0C4CC"><TrendCharts /></el-icon>
            <p class="mt-2 text-gray">ECharts 柱状+折线图区域 (花费 vs 广告销售额 vs ACOS)</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="never" header="搜索词分析预警 (AI建议)">
          <div class="warning-list">
            <el-alert
              title="建议否定搜索词"
              type="error"
              description="词: '安い イヤホン', 点击 50 次，0 转化。建议加入否定词库。"
              show-icon
              class="mb-2"
              :closable="false"
            >
              <template #default>
                <div class="mt-2"><el-button size="small" type="danger" plain>一键否定</el-button></div>
              </template>
            </el-alert>
            <el-alert
              title="建议拓词预警"
              type="success"
              description="词: '防水 イヤホン スポーツ', 转化率高达 18%，且 ROAS > 5。建议单独建立精准投放计划并提高竞价。"
              show-icon
              class="mb-2"
              :closable="false"
            >
              <template #default>
                <div class="mt-2"><el-button size="small" type="success" plain>加入拓词库</el-button></div>
              </template>
            </el-alert>
            <el-alert
              title="曝光骤降预警"
              type="warning"
              description="广告组 '秋季促销-SP' 曝光量环比下降 45%，可能因竞价过低丢失 BuyBox，请检查。"
              show-icon
              :closable="false"
            />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const timeRange = ref('7days')
const platform = ref('all')

const kpis = ref([
  { title: '广告总花费', value: '¥ 45,200', tip: '选定期间内的总广告支出', trend: 12.5, inverseColor: true },
  { title: '广告销售额', value: '¥ 325,000', tip: '由广告直接带来的销售额', trend: 18.2, inverseColor: false },
  { title: '综合 ACOS', value: '13.9%', tip: '广告花费 / 广告销售额', trend: -2.1, inverseColor: true }, // ACOS下降是好事
  { title: '综合 ROAS', value: '7.19', tip: '广告销售额 / 广告花费', trend: 5.4, inverseColor: false }
])
</script>

<style scoped lang="scss">
.ad-dashboard {
  .mb-4 { margin-bottom: 20px; }
  .mb-2 { margin-bottom: 10px; }
  .mt-2 { margin-top: 8px; }
  .ml-4 { margin-left: 16px; }

  .kpi-card {
    .kpi-header {
      display: flex;
      justify-content: space-between;
      color: #909399;
      font-size: 14px;
    }
    .kpi-value {
      font-size: 26px;
      font-weight: bold;
      margin: 15px 0;
      color: #303133;
    }
    .kpi-footer {
      font-size: 12px;
      color: #909399;
      display: flex;
      align-items: center;
      
      .up { color: #f56c6c; display: flex; align-items: center; margin-left: 5px; }
      .down { color: #67c23a; display: flex; align-items: center; margin-left: 5px; }
    }
  }

  .chart-placeholder {
    height: 380px;
    background-color: #f8f9fa;
    border-radius: 4px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }

  .warning-list {
    max-height: 380px;
    overflow-y: auto;
  }
}
</style>
