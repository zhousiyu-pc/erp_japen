<template>
  <div class="ticket-system">
    <el-row :gutter="20">
      <el-col :span="18">
        <el-card shadow="never">
          <div class="filter-bar mb-4 flex justify-between">
            <div class="left flex">
              <el-radio-group v-model="filterStatus" class="mr-3">
                <el-radio-button label="all">全部工单</el-radio-button>
                <el-radio-button label="my">我处理的</el-radio-button>
                <el-radio-button label="pending">待处理</el-radio-button>
              </el-radio-group>
              <el-select placeholder="工单类型" style="width: 120px" class="mr-2">
                <el-option label="售后退换" value="after_sale" />
                <el-option label="物流异常" value="logistics" />
                <el-option label="产品投诉" value="complaint" />
                <el-option label="财务审批" value="finance" />
              </el-select>
              <el-input placeholder="搜索工单号/订单号" style="width: 200px" />
            </div>
            <div class="right">
              <el-button type="primary"><el-icon><Plus /></el-icon> 新建工单</el-button>
            </div>
          </div>

          <el-table :data="ticketData" border style="width: 100%">
            <el-table-column prop="ticketNo" label="工单号" width="130">
              <template #default="{ row }">
                <el-link type="primary">{{ row.ticketNo }}</el-link>
              </template>
            </el-table-column>
            <el-table-column prop="title" label="标题/问题描述" min-width="200" show-overflow-tooltip />
            <el-table-column prop="type" label="类型" width="100">
              <template #default="{ row }">
                <el-tag size="small" type="info">{{ row.type }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="priority" label="优先级" width="80" align="center">
              <template #default="{ row }">
                <el-tag size="small" :type="row.priority === '高' ? 'danger' : 'info'" :effect="row.priority === '高' ? 'dark' : 'light'">
                  {{ row.priority }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">{{ row.statusLabel }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="assignee" label="当前处理人" width="100" />
            <el-table-column prop="updateTime" label="最后更新" width="150" />
            <el-table-column label="操作" width="120" fixed="right" align="center">
              <template #default="{ row }">
                <el-button link type="primary" size="small">处理</el-button>
                <el-button link type="primary" size="small" v-if="row.status !== 'completed'">流转</el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="mt-4 flex justify-end">
            <el-pagination background layout="prev, pager, next" :total="45" />
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card shadow="never" header="部门工单统计" class="mb-4">
          <div class="dept-stats">
            <div class="stat-item flex justify-between items-center mb-3">
              <span>客服部</span>
              <el-progress :percentage="85" style="width: 120px" :show-text="false" status="success" />
              <span class="text-xs text-gray">12 待办</span>
            </div>
            <div class="stat-item flex justify-between items-center mb-3">
              <span>仓储部</span>
              <el-progress :percentage="40" style="width: 120px" :show-text="false" status="warning" />
              <span class="text-xs text-gray">5 待办</span>
            </div>
            <div class="stat-item flex justify-between items-center">
              <span>财务部</span>
              <el-progress :percentage="15" style="width: 120px" :show-text="false" status="exception" />
              <span class="text-xs text-gray">2 待办</span>
            </div>
          </div>
        </el-card>

        <el-card shadow="never" header="平均处理时长分析">
          <div class="chart-placeholder">
            <el-icon :size="40" color="#C0C4CC"><PieChart /></el-icon>
            <p class="mt-2 text-gray text-xs text-center">各类型工单处理时长占比 (ECharts)</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const filterStatus = ref('all')

const ticketData = ref([
  {
    ticketNo: 'TK-260302-001',
    title: '买家反馈少发漏发，要求补发',
    type: '售后退换',
    priority: '高',
    status: 'pending',
    statusLabel: '待处理',
    assignee: '客服-小美',
    updateTime: '2026-03-02 10:30'
  },
  {
    ticketNo: 'TK-260301-045',
    title: '海运干线清关延误，需同步买家',
    type: '物流异常',
    priority: '中',
    status: 'processing',
    statusLabel: '处理中',
    assignee: '物流-老张',
    updateTime: '2026-03-01 16:15'
  },
  {
    ticketNo: 'TK-260228-088',
    title: '退款审核申请 (订单: SO...088)',
    type: '财务审批',
    priority: '中',
    status: 'completed',
    statusLabel: '已完结',
    assignee: '财务-李姐',
    updateTime: '2026-02-28 11:00'
  }
])

const getStatusType = (status: string) => {
  const map: Record<string, string> = {
    pending: 'danger',
    processing: 'warning',
    completed: 'success'
  }
  return map[status] || 'info'
}
</script>

<style scoped lang="scss">
.ticket-system {
  .mb-3 { margin-bottom: 12px; }
  .mb-4 { margin-bottom: 16px; }
  .mr-2 { margin-right: 8px; }
  .mr-3 { margin-right: 12px; }
  .mt-2 { margin-top: 8px; }
  .mt-4 { margin-top: 16px; }
  
  .flex { display: flex; }
  .justify-between { justify-content: space-between; }
  .justify-end { justify-content: flex-end; }
  .items-center { align-items: center; }
  
  .text-xs { font-size: 12px; }
  .text-gray { color: #909399; }
  .text-center { text-align: center; }

  .chart-placeholder {
    height: 150px;
    background-color: #f8f9fa;
    border-radius: 4px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }
}
</style>
