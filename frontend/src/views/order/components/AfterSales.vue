<template>
  <div class="after-sales">
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card shadow="never" header="售后工单列表">
          <div class="filter-bar mb-3">
            <el-select placeholder="工单类型" style="width: 120px; margin-right: 10px">
              <el-option label="退款" value="refund" />
              <el-option label="退货" value="return" />
              <el-option label="换货" value="exchange" />
              <el-option label="投诉" value="complaint" />
            </el-select>
            <el-select placeholder="处理状态" style="width: 120px; margin-right: 10px">
              <el-option label="待处理" value="pending" />
              <el-option label="处理中" value="processing" />
              <el-option label="已完结" value="completed" />
            </el-select>
            <el-input placeholder="搜索订单号/买家" style="width: 200px; margin-right: 10px" />
            <el-button type="primary">搜索</el-button>
          </div>

          <el-table :data="afterSalesData" border style="width: 100%">
            <el-table-column prop="ticketId" label="工单号" width="130" />
            <el-table-column prop="orderId" label="关联订单号" width="150" />
            <el-table-column prop="type" label="类型" width="80">
              <template #default="{ row }">
                <el-tag :type="getTypeTag(row.type)" size="small">{{ row.typeLabel }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="reason" label="原因" show-overflow-tooltip />
            <el-table-column prop="status" label="状态" width="90">
              <template #default="{ row }">
                <el-tag :type="getStatusTag(row.status)" effect="dark" size="small">{{ row.statusLabel }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="assignee" label="责任人" width="100" />
            <el-table-column label="操作" width="120" fixed="right">
              <template #default>
                <el-button link type="primary" size="small">处理</el-button>
                <el-button link type="info" size="small">流转</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card shadow="never" header="异常订单监控" class="abnormal-card">
          <div class="abnormal-list">
            <el-alert
              v-for="(item, index) in abnormalData"
              :key="index"
              :title="item.title"
              :type="item.level"
              :description="item.desc"
              show-icon
              class="mb-3"
              :closable="false"
            >
              <template #default>
                <div class="alert-action mt-2">
                  <el-button size="small" :type="item.level" plain>立即处理</el-button>
                </div>
              </template>
            </el-alert>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const afterSalesData = ref([
  {
    ticketId: 'AS-20260301-01',
    orderId: 'SO202602280015',
    type: 'return',
    typeLabel: '退货',
    reason: '商品与描述不符 (日文: 商品が説明と異なる)',
    status: 'pending',
    statusLabel: '待处理',
    assignee: '客服-小美'
  },
  {
    ticketId: 'AS-20260301-02',
    orderId: 'SO202602250088',
    type: 'refund',
    typeLabel: '退款',
    reason: '物流一直未更新',
    status: 'processing',
    statusLabel: '处理中',
    assignee: '财务-老李'
  }
])

const abnormalData = ref([
  {
    title: '防刷单预警',
    level: 'error',
    desc: '系统检测到同一收件地址 (東京都新宿区...) 在1小时内下单 5 次，请人工复核。'
  },
  {
    title: '地址解析异常',
    level: 'warning',
    desc: '订单 SO202603020055 邮编与都道府县不匹配，无法生成面单。'
  },
  {
    title: '库存不足拦截',
    level: 'warning',
    desc: '订单 SO202603020089 包含超卖商品 (SKU-001-BLK)，已自动暂停发货流程。'
  }
])

const getTypeTag = (type: string) => {
  const map: Record<string, string> = {
    return: 'danger',
    refund: 'warning',
    exchange: 'primary',
    complaint: 'info'
  }
  return map[type] || 'info'
}

const getStatusTag = (status: string) => {
  const map: Record<string, string> = {
    pending: 'danger',
    processing: 'warning',
    completed: 'success'
  }
  return map[status] || 'info'
}
</script>

<style scoped lang="scss">
.after-sales {
  .mb-3 {
    margin-bottom: 15px;
  }
  .mt-2 {
    margin-top: 8px;
  }
  
  .abnormal-card {
    height: 100%;
    
    .abnormal-list {
      max-height: 500px;
      overflow-y: auto;
    }
  }
}
</style>
