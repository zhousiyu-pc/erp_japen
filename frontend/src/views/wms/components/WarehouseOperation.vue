<template>
  <div class="warehouse-operation">
    <el-row :gutter="20">
      <!-- Left: Pending Tasks -->
      <el-col :span="14">
        <el-card shadow="never" class="mb-4">
          <template #header>
            <div class="card-header">
              <span>待处理作业任务</span>
              <el-radio-group v-model="taskType" size="small">
                <el-radio-button label="inbound">入库单</el-radio-button>
                <el-radio-button label="outbound">出库单</el-radio-button>
                <el-radio-button label="transfer">调拨单</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          
          <el-table :data="tasksData" border style="width: 100%">
            <el-table-column prop="taskNo" label="任务单号" width="160" />
            <el-table-column prop="type" label="类型" width="100">
              <template #default="{ row }">
                <el-tag :type="row.type === '入库' ? 'success' : 'warning'">{{ row.type }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="warehouse" label="目标/源仓库" width="120" />
            <el-table-column prop="qty" label="预计数量" width="90" align="right" />
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template #default="{ row }">
                <el-tag type="info" v-if="row.status === '待处理'">{{ row.status }}</el-tag>
                <el-tag type="primary" v-else>{{ row.status }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120" fixed="right" align="center">
              <template #default>
                <el-button link type="primary" size="small">开始作业</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <!-- Right: Picking / PDA Mock -->
      <el-col :span="10">
        <el-card shadow="never" header="模拟 PDA 拣货/入库" class="pda-mock-card">
          <div class="pda-screen">
            <div class="pda-header">
              <el-icon><Monitor /></el-icon> 扫码作业终端
            </div>
            <div class="pda-body">
              <el-form label-position="top">
                <el-form-item label="扫描或输入单号 / 库位 / SKU">
                  <el-input 
                    v-model="scanInput" 
                    placeholder="请输入条码..." 
                    @keyup.enter="handleScan"
                  >
                    <template #append>
                      <el-button @click="handleScan">确认</el-button>
                    </template>
                  </el-input>
                </el-form-item>
              </el-form>

              <div v-if="currentTask" class="task-detail mt-4">
                <div class="task-title">当前任务: {{ currentTask.id }}</div>
                <div class="sku-item">
                  <span class="sku">SKU-001-BLK</span>
                  <span class="qty">需拣货: <strong>2</strong> 件</span>
                </div>
                <div class="location-hint">
                  推荐前往库位: <el-tag size="small" type="primary">A-01-03</el-tag>
                </div>
                <el-button type="success" class="mt-4 w-full" @click="completeTask">确认完成 (按回车)</el-button>
              </div>
              <div v-else class="empty-state mt-4">
                <el-empty description="请扫描任务单号开始作业" :image-size="60" />
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const taskType = ref('outbound')
const scanInput = ref('')
const currentTask = ref<any>(null)

const tasksData = ref([
  { taskNo: 'OUT-20260302-001', type: '出库', warehouse: '大阪直发仓', qty: 15, status: '待处理' },
  { taskNo: 'OUT-20260302-002', type: '出库', warehouse: '大阪直发仓', qty: 3, status: '作业中' },
  { taskNo: 'IN-20260302-001', type: '入库', warehouse: '深圳一仓', qty: 500, status: '待处理' }
])

const handleScan = () => {
  if (!scanInput.value) return
  ElMessage.success(`成功识别条码: ${scanInput.value}`)
  currentTask.value = { id: scanInput.value }
  scanInput.value = ''
}

const completeTask = () => {
  ElMessage.success('作业完成！库存已更新。')
  currentTask.value = null
}
</script>

<style scoped lang="scss">
.warehouse-operation {
  .mb-4 { margin-bottom: 20px; }
  .mt-4 { margin-top: 16px; }
  .w-full { width: 100%; }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .pda-mock-card {
    background-color: #f0f2f5;
    
    .pda-screen {
      background: #fff;
      border: 8px solid #333;
      border-radius: 20px;
      height: 400px;
      overflow: hidden;
      display: flex;
      flex-direction: column;

      .pda-header {
        background: #333;
        color: #fff;
        padding: 5px 15px;
        font-size: 14px;
        text-align: center;
      }

      .pda-body {
        padding: 20px;
        flex: 1;
        overflow-y: auto;

        .task-detail {
          background: #fdf6ec;
          border: 1px solid #e1f3d8;
          padding: 15px;
          border-radius: 8px;

          .task-title {
            font-weight: bold;
            margin-bottom: 10px;
            color: #67c23a;
          }
          .sku-item {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
            padding-bottom: 10px;
            border-bottom: 1px dashed #e1f3d8;
          }
          .location-hint {
            font-size: 14px;
            color: #606266;
          }
        }
      }
    }
  }
}
</style>
