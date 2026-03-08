<template>
  <div class="order-rules">
    <el-card shadow="never" class="mb-4">
      <div class="header-action">
        <h3>订单自动化规则配置</h3>
        <el-button type="primary"><el-icon><Plus /></el-icon> 新建规则</el-button>
      </div>
      
      <el-table :data="rulesData" style="width: 100%" class="mt-3">
        <el-table-column prop="name" label="规则名称" width="200" />
        <el-table-column label="执行条件 (If)" min-width="250">
          <template #default="{ row }">
            <div class="condition-box">
              <el-tag size="small" type="info" class="mr-1" v-for="(cond, i) in row.conditions" :key="i">
                {{ cond }}
              </el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="执行动作 (Then)" min-width="200">
          <template #default="{ row }">
            <span class="action-text text-primary">{{ row.action }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80" align="center" />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-switch v-model="row.active" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template #default>
            <el-button link type="primary">编辑</el-button>
            <el-button link type="danger">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover" header="智能路由策略 (示例)">
          <el-timeline>
            <el-timeline-item type="primary" size="large">
              <h4>判断收件地区</h4>
              <p class="text-sm text-gray mt-1">如果是 北海道/冲绳</p>
            </el-timeline-item>
            <el-timeline-item type="warning" size="large">
              <h4>分配承运商</h4>
              <p class="text-sm text-gray mt-1">强制分配给 日本邮政 (ゆうパック)，因为偏远地区性价比最高</p>
            </el-timeline-item>
            <el-timeline-item type="success" size="large">
              <h4>分发仓库</h4>
              <p class="text-sm text-gray mt-1">优先从 大阪仓 发货</p>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" header="订单拦截策略 (示例)">
          <el-timeline>
            <el-timeline-item type="danger" size="large">
              <h4>客单价监控</h4>
              <p class="text-sm text-gray mt-1">如果订单金额 > 50,000 日元</p>
            </el-timeline-item>
            <el-timeline-item type="danger" size="large">
              <h4>新买家判定</h4>
              <p class="text-sm text-gray mt-1">并且买家历史成交次数 = 0</p>
            </el-timeline-item>
            <el-timeline-item type="warning" size="large">
              <h4>执行动作</h4>
              <p class="text-sm text-gray mt-1">打上"高危大额"标签，拦截自动发货，转人工风控审核</p>
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
    name: '北海道偏远地区派送',
    conditions: ['地区 包含 北海道,冲绳'],
    action: '指定物流商 = 日本邮政',
    priority: 1,
    active: true
  },
  {
    name: '大客户高优发货',
    conditions: ['客单价 > ¥20,000'],
    action: '打标签 = VIP客户; 优先分配库存',
    priority: 2,
    active: true
  },
  {
    name: '防刷单拦截',
    conditions: ['同IP 24小时内下单数 >= 3'],
    action: '拦截发货; 转人工审核',
    priority: 99,
    active: false
  },
  {
    name: '自动合并订单',
    conditions: ['同收件人手机号', '时间间隔 < 12小时'],
    action: '合并为一个发货包裹',
    priority: 3,
    active: true
  }
])
</script>

<style scoped lang="scss">
.order-rules {
  .mb-4 { margin-bottom: 20px; }
  .mt-3 { margin-top: 15px; }
  .mt-1 { margin-top: 5px; }
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
    gap: 4px;
  }

  .action-text {
    font-weight: 500;
  }
  
  .text-primary { color: #409EFF; }
  .text-gray { color: #909399; }
  .text-sm { font-size: 13px; }
}
</style>
