<template>
  <div class="dashboard-container">
    <!-- 初始化引导 -->
    <el-card v-if="showInitGuide" shadow="hover" class="init-guide mb-4">
      <template #header>
        <span><el-icon class="mr-2"><Guide /></el-icon>初始化引导</span>
        <el-button type="primary" link @click="showInitGuide = false">收起</el-button>
      </template>
      <el-steps :active="initStep" align-center finish-status="success">
        <el-step title="店铺授权" description="连接亚马逊/乐天/雅虎店铺" />
        <el-step title="商品同步" description="导入商品主数据" />
        <el-step title="库存配置" description="配置仓库与库存策略" />
        <el-step title="订单规则" description="设置自动处理规则" />
      </el-steps>
      <div class="init-actions mt-4">
        <el-button type="primary" @click="goToStore">去授权店铺</el-button>
        <el-button @click="goToProduct">去同步商品</el-button>
      </div>
    </el-card>

    <!-- KPI 卡片 -->
    <el-row :gutter="20" class="mb-4">
      <el-col :span="6" v-for="item in kpiData" :key="item.title">
        <el-card shadow="hover" class="kpi-card">
          <div class="kpi-header">
            <span>{{ item.title }}</span>
            <el-tag :type="item.tagType" size="small">{{ item.tag }}</el-tag>
          </div>
          <div class="kpi-value">
            {{ item.prefix }}{{ item.value }}
          </div>
          <div class="kpi-footer">
            <span>环比: <span :class="item.trend > 0 ? 'up' : 'down'">{{ item.trend > 0 ? '+' : '' }}{{ item.trend }}%</span></span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 实时销量 + 实时广告 -->
    <el-row :gutter="20" class="mb-4">
      <el-col :span="12">
        <el-card shadow="hover" class="realtime-card">
          <template #header>
            <span><el-icon class="mr-2"><TrendCharts /></el-icon>实时销量</span>
            <el-tag size="small" type="success">今日</el-tag>
          </template>
          <div class="realtime-list">
            <div v-for="r in realtimeSales" :key="r.platform" class="realtime-item">
              <span class="platform">{{ r.platform }}</span>
              <span class="value">{{ r.value }}</span>
              <span class="unit">{{ r.unit }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="realtime-card">
          <template #header>
            <span><el-icon class="mr-2"><DataLine /></el-icon>实时广告</span>
            <el-tag size="small" type="primary">今日</el-tag>
          </template>
          <div class="realtime-list">
            <div v-for="a in realtimeAds" :key="a.name" class="realtime-item">
              <span class="platform">{{ a.name }}</span>
              <span class="value">{{ a.value }}</span>
              <span class="unit">{{ a.unit }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 业绩走势 + AI 简报 -->
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header-flex">
              <span>业绩走势</span>
              <el-radio-group v-model="trendRange" size="small">
                <el-radio-button label="7d">近7天</el-radio-button>
                <el-radio-button label="30d">近30天</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div class="chart-placeholder">
            <div class="trend-bars">
              <div v-for="(v, i) in trendData" :key="i" class="bar-wrap">
                <div class="bar" :style="{ height: v + '%' }" />
                <span class="label">{{ v }}</span>
              </div>
            </div>
            <p class="chart-hint">多平台销售趋势 (亚马逊 vs 乐天 vs 雅虎)</p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" header="AI 运营简报">
          <div class="ai-report">
            <el-alert
              title="销量异常预警"
              type="warning"
              description="SKU: JPN-ABC-001 近三日销量下滑 30%，建议检查广告投放或竞品价格。"
              show-icon
              class="mb-3"
            />
            <el-alert
              title="库存预警"
              type="error"
              description="FBA 东京仓有 3 个 SKU 库存不足 7 天，请尽快安排补货。"
              show-icon
              class="mb-3"
            />
            <el-alert
              title="广告优化建议"
              type="success"
              description="广告组 '秋季大促' ACOS 表现优异 (12%)，建议增加 20% 预算。"
              show-icon
            />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const showInitGuide = ref(true)
const initStep = ref(1)
const trendRange = ref('7d')

const kpiData = ref([
  { title: '今日销售额', value: '1,250,000', prefix: '¥', tag: '日', tagType: 'success', trend: 5.2 },
  { title: '今日订单数', value: '3,420', prefix: '', tag: '日', tagType: 'primary', trend: -1.5 },
  { title: '平均客单价', value: '365.5', prefix: '¥', tag: '月', tagType: 'warning', trend: 2.1 },
  { title: '广告 ACOS', value: '15.4', prefix: '', tag: '月', tagType: 'danger', trend: -0.8 }
])

const realtimeSales = ref([
  { platform: '亚马逊', value: '¥ 520,000', unit: '销售额' },
  { platform: '乐天', value: '¥ 380,000', unit: '销售额' },
  { platform: '雅虎', value: '¥ 350,000', unit: '销售额' }
])

const realtimeAds = ref([
  { name: '广告花费', value: '¥ 48,200', unit: '' },
  { name: 'ACOS', value: '15.4%', unit: '' },
  { name: 'ROAS', value: '6.5', unit: '' }
])

const trendData = ref([65, 72, 58, 80, 75, 88, 82])

const goToStore = () => router.push('/store')
const goToProduct = () => router.push('/product')
</script>

<style scoped lang="scss">
.dashboard-container {
  .mb-4 { margin-bottom: 20px; }
  .mb-3 { margin-bottom: 15px; }
  .mt-4 { margin-top: 16px; }
  .mr-2 { margin-right: 8px; }
  .ml-auto { margin-left: auto; }

  .card-header-flex {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
  }

  .init-guide {
    background: linear-gradient(135deg, #ecf5ff 0%, #f0f9ff 100%);
    border: 1px solid #d9ecff;
  }

  .init-actions {
    display: flex;
    gap: 12px;
    justify-content: center;
  }

  .kpi-card {
    .kpi-header {
      display: flex;
      justify-content: space-between;
      color: #909399;
      font-size: 14px;
    }
    .kpi-value {
      font-size: 28px;
      font-weight: bold;
      margin: 15px 0;
      color: #303133;
    }
    .kpi-footer {
      font-size: 13px;
      color: #909399;
      .up { color: #f56c6c; }
      .down { color: #67c23a; }
    }
  }

  .realtime-card {
    .realtime-list {
      display: flex;
      flex-direction: column;
      gap: 12px;
    }
    .realtime-item {
      display: flex;
      align-items: center;
      padding: 8px 12px;
      background: #f8f9fa;
      border-radius: 6px;
      .platform { flex: 1; font-weight: 500; }
      .value { font-size: 16px; font-weight: 600; color: #409eff; }
      .unit { margin-left: 8px; font-size: 12px; color: #909399; }
    }
  }

  .chart-placeholder {
    height: 260px;
    padding: 20px;
    background-color: #f8f9fa;
    border-radius: 4px;
    .trend-bars {
      display: flex;
      align-items: flex-end;
      justify-content: space-around;
      height: 180px;
      gap: 8px;
    }
    .bar-wrap {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      .bar {
        width: 100%;
        max-width: 40px;
        min-height: 4px;
        background: linear-gradient(180deg, #409eff 0%, #79bbff 100%);
        border-radius: 4px 4px 0 0;
        transition: height 0.3s;
      }
      .label {
        font-size: 12px;
        color: #909399;
        margin-top: 8px;
      }
    }
    .chart-hint {
      text-align: center;
      color: #909399;
      font-size: 13px;
      margin-top: 16px;
    }
  }

  .ai-report {
    height: 300px;
    overflow-y: auto;
  }
}
</style>
