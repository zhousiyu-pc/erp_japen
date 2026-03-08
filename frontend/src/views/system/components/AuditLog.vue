<template>
  <div class="audit-log">
    <el-card shadow="never" class="mb-4">
      <template #header>
        <div class="flex justify-between items-center">
          <span class="font-bold">系统操作审计日志</span>
          <el-button plain><el-icon><Download /></el-icon> 导出当前日志</el-button>
        </div>
      </template>

      <el-form :inline="true" class="mb-4">
        <el-form-item label="操作时间">
          <el-date-picker type="daterange" range-separator="-" start-placeholder="开始" end-placeholder="结束" />
        </el-form-item>
        <el-form-item label="操作人">
          <el-input placeholder="员工姓名/工号" clearable />
        </el-form-item>
        <el-form-item label="操作模块">
          <el-select placeholder="全部模块" clearable style="width: 150px">
            <el-option label="商品中心" value="product" />
            <el-option label="订单中心" value="order" />
            <el-option label="财务与结算" value="finance" />
            <el-option label="系统管理" value="system" />
          </el-select>
        </el-form-item>
        <el-form-item label="操作类型">
          <el-select placeholder="全部类型" clearable style="width: 120px">
            <el-option label="新增" value="create" />
            <el-option label="修改" value="update" />
            <el-option label="删除" value="delete" />
            <el-option label="高危操作" value="danger" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary">查询</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="auditData" border style="width: 100%">
        <el-table-column prop="time" label="操作时间" width="160" />
        <el-table-column label="操作人" width="150">
          <template #default="{ row }">
            <div class="font-bold">{{ row.operator }}</div>
            <div class="text-xs text-gray">IP: {{ row.ip }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="module" label="所属模块" width="120" />
        <el-table-column label="操作动作" width="100" align="center">
          <template #default="{ row }">
            <el-tag size="small" :type="getActionType(row.action)">{{ row.action }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="detail" label="操作详情" min-width="300" show-overflow-tooltip>
          <template #default="{ row }">
            <div>{{ row.detail }}</div>
            <div v-if="row.before && row.after" class="diff-box mt-1 text-xs">
              <span class="text-gray">变更前:</span> <del class="text-danger mr-2">{{ row.before }}</del>
              <span class="text-gray">变更后:</span> <ins class="text-success">{{ row.after }}</ins>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="mt-4 flex justify-end">
        <el-pagination background layout="total, prev, pager, next" :total="1250" />
      </div>
    </el-card>

    <el-card shadow="never" header="安全策略配置 (租户级)">
      <el-form label-position="left" label-width="250px" style="max-width: 600px">
        <el-form-item label="强制双因素认证 (2FA)">
          <el-switch v-model="security.twoFactor" active-text="开启 (所有账号登录必须验证 OTP)" />
        </el-form-item>
        <el-form-item label="密码过期强制修改">
          <el-select v-model="security.pwdExpire" style="width: 200px">
            <el-option label="每 90 天" value="90" />
            <el-option label="每 180 天" value="180" />
            <el-option label="永不过期" value="never" />
          </el-select>
        </el-form-item>
        <el-form-item label="登录 IP 白名单">
          <el-input type="textarea" v-model="security.ipWhiteList" placeholder="每行一个 IP，留空则不限制" />
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'

const security = reactive({
  twoFactor: true,
  pwdExpire: '90',
  ipWhiteList: ''
})

const auditData = ref([
  {
    time: '2026-03-02 11:45:22',
    operator: '周大牛 (运营主管)',
    ip: '113.89.23.45',
    module: '商品中心',
    action: '修改',
    detail: '修改了商品 SKU-001-BLK-M 的日文标题',
    before: '【旧版】ワイヤレスイヤホン',
    after: '【2026最新版】ワイヤレスイヤホン 降噪'
  },
  {
    time: '2026-03-02 10:12:05',
    operator: '李小花 (客服专员)',
    ip: '113.89.23.45',
    module: '订单中心',
    action: '新增',
    detail: '为订单 SO202603020001 创建了售后退款单 (金额: ¥1500)'
  },
  {
    time: '2026-03-01 18:30:00',
    operator: 'admin (系统管理员)',
    ip: '192.168.1.100',
    module: '系统管理',
    action: '高危操作',
    detail: '删除了离职员工账号: 王铁柱 (工号: E099)'
  }
])

const getActionType = (action: string) => {
  const map: Record<string, string> = {
    '新增': 'success',
    '修改': 'primary',
    '删除': 'danger',
    '高危操作': 'danger'
  }
  return map[action] || 'info'
}
</script>

<style scoped lang="scss">
.audit-log {
  .mb-4 { margin-bottom: 20px; }
  .mt-1 { margin-top: 4px; }
  .mt-4 { margin-top: 16px; }
  .mr-2 { margin-right: 8px; }

  .flex { display: flex; }
  .justify-between { justify-content: space-between; }
  .justify-end { justify-content: flex-end; }
  .items-center { align-items: center; }

  .font-bold { font-weight: bold; }
  .text-xs { font-size: 12px; }
  .text-gray { color: #909399; }
  .text-danger { color: #F56C6C; }
  .text-success { color: #67C23A; }

  .diff-box {
    background-color: #f8f9fa;
    padding: 6px;
    border-radius: 4px;
    border: 1px dashed #dcdfe6;
  }
}
</style>
