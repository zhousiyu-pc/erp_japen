<template>
  <div class="ad-rules">
    <el-card shadow="never" class="mb-4">
      <div class="header-action">
        <h3>智能出价与预算规则</h3>
        <el-button type="primary"><el-icon><Plus /></el-icon> 新建规则</el-button>
      </div>

      <el-table :data="rulesData" border style="width: 100%" class="mt-3">
        <el-table-column prop="name" label="规则名称" width="200">
          <template #default="{ row }">
            <span class="font-bold">{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="应用范围" width="150">
          <template #default="{ row }">
            <el-tag size="small" type="info">{{ row.scope }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="执行条件 (If)" min-width="250">
          <template #default="{ row }">
            <div class="condition-box">
              <el-tag size="small" type="warning" v-for="(cond, i) in row.conditions" :key="i" class="mr-1 mb-1">
                {{ cond }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="执行动作 (Then)" min-width="200">
          <template #default="{ row }">
            <span class="text-primary">{{ row.action }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-switch v-model="row.active" size="small" />
          </template>
        </el-table-column>
        <el-table-column prop="lastExec" label="上次执行时间" width="160" />
        <el-table-column label="操作" width="150" align="center">
          <template #default>
            <el-button link type="primary" size="small">日志</el-button>
            <el-button link type="primary" size="small">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover" header="ACOS 止损策略 (示例)">
          <el-timeline>
            <el-timeline-item type="danger" size="large">
              <h4>监控 ACOS 与花费</h4>
              <p class="text-sm text-gray mt-1">如果 过去 7 天 ACOS > 30% 且 点击量 > 20</p>
            </el-timeline-item>
            <el-timeline-item type="warning" size="large">
              <h4>判断转化</h4>
              <p class="text-sm text-gray mt-1">并且 订单数 < 2</p>
            </el-timeline-item>
            <el-timeline-item type="primary" size="large">
              <h4>执行动作</h4>
              <p class="text-sm text-gray mt-1">降低该关键词/投放竞价 15% (最低 ¥10)</p>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card shadow="hover" header="爆款冲顶策略 (示例)">
          <el-timeline>
            <el-timeline-item type="success" size="large">
              <h4>监控 ROAS</h4>
              <p class="text-sm text-gray mt-1">如果 过去 3 天 ROAS > 8.0</p>
            </el-timeline-item>
            <el-timeline-item type="success" size="large">
              <h4>判断预算情况</h4>
              <p class="text-sm text-gray mt-1">并且 当前广告活动预算使用率 > 90%</p>
            </el-timeline-item>
            <el-timeline-item type="primary" size="large">
              <h4>执行动作</h4>
              <p class="text-sm text-gray mt-1">自动追加预算 20% (上限 ¥10,000)，并在次日通知运营</p>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const rulesData = ref([
  {
    name: 'ACOS 超标降价止损',
    scope: '指定广告活动 (5个)',
    conditions: ['过去 7 天 ACOS > 25%', '点击量 > 20'],
    action: '降低竞价 10%',
    active: true,
    lastExec: '2026-03-02 02:00:00'
  },
  {
    name: '优质词加价卡位',
    scope: '全部 SP 广告',
    conditions: ['过去 3 天 ROAS > 5', '转化率 > 15%'],
    action: '提高竞价 15% (最高 ¥150)',
    active: true,
    lastExec: '2026-03-02 02:00:00'
  },
  {
    name: '预算智能追加 (防跑空)',
    scope: '秋季大促广告组',
    conditions: ['下午 15:00 前预算消耗 > 80%', 'ACOS < 15%'],
    action: '增加预算 ¥2,000',
    active: false,
    lastExec: '2026-03-01 14:30:00'
  }
])
</script>

<style scoped lang="scss">
.ad-rules {
  .mb-4 { margin-bottom: 20px; }
  .mb-1 { margin-bottom: 4px; }
  .mt-1 { margin-top: 5px; }
  .mt-3 { margin-top: 15px; }
  .mr-1 { margin-right: 5px; }

  .header-action {
    display: flex;
    justify-content: space-between;
    align-items: center;
    h3 { margin: 0; }
  }

  .condition-box {
    display: flex;
    flex-wrap: wrap;
  }

  .font-bold { font-weight: bold; }
  .text-primary { color: #409EFF; font-weight: 500; }
  .text-gray { color: #909399; }
  .text-sm { font-size: 13px; }
}
</style>
