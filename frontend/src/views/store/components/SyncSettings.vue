<template>
  <div class="sync-settings">
    <el-card shadow="never">
      <template #header>
        <div class="flex justify-between items-center">
          <span class="font-bold">店铺数据同步策略</span>
          <el-button type="primary" size="small" :loading="saving" @click="saveConfig">保存全局配置</el-button>
        </div>
      </template>

      <div v-loading="loading" class="config-form-wrap">
        <el-form label-position="left" label-width="200px" style="max-width: 800px;">
          <el-divider content-position="left">订单与库存同步</el-divider>
          <el-form-item label="订单抓取频率">
            <el-select v-model="form.orderSyncRate" style="width: 200px">
              <el-option label="每 15 分钟" value="15" />
              <el-option label="每 30 分钟" value="30" />
              <el-option label="每 1 小时" value="60" />
              <el-option label="手动触发" value="manual" />
            </el-select>
          </el-form-item>
          <el-form-item label="库存回传策略">
            <el-radio-group v-model="form.stockSyncStrategy">
              <el-radio label="realtime">实时回传 (可用库存发生变化时)</el-radio>
              <el-radio label="scheduled">定时回传 (每小时)</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="平台可售库存保留比例">
            <el-input-number v-model="form.stockReserve" :min="0" :max="100" />
            <span class="ml-2 text-gray text-xs">为防止超卖，各平台仅同步 (总可用库存 - 预留库存) 的百分比</span>
          </el-form-item>

          <el-divider content-position="left">订单过滤规则</el-divider>
          <el-form-item label="过滤测试订单">
            <el-switch v-model="form.filterTestOrders" active-text="自动拦截金额为 0 或买家账号在黑名单的订单" />
          </el-form-item>
          <el-form-item label="特定商品不发货">
            <el-select v-model="form.excludeSkus" multiple placeholder="请选择不自动处理的 SKU" style="width: 100%">
              <el-option label="SKU-GIFT-001 (虚拟赠品)" value="sku1" />
              <el-option label="SKU-PRESELL (预售款)" value="sku2" />
            </el-select>
          </el-form-item>

          <el-divider content-position="left">紧急控制台</el-divider>
          <el-form-item label="系统级同步开关">
            <el-alert
              title="关闭后将暂停所有店铺的数据抓取和回传，仅在系统维护或发生重大异常时使用。"
              type="warning"
              show-icon
              :closable="false"
              class="mb-2"
            />
            <el-switch
              v-model="form.globalSyncEnabled"
              active-text="全局同步已开启"
              inactive-text="全局同步已暂停"
              active-color="#13ce66"
              inactive-color="#ff4949"
            />
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { storeApi, type SyncConfigDTO } from '../../../api/store'

const loading = ref(false)
const saving = ref(false)

const form = reactive<SyncConfigDTO>({
  orderSyncRate: '15',
  stockSyncStrategy: 'realtime',
  stockReserve: 10,
  filterTestOrders: true,
  excludeSkus: [],
  globalSyncEnabled: true
})

const loadConfig = async () => {
  loading.value = true
  try {
    const res = await storeApi.getSyncConfig()
    if (res.code === 200 && res.data) {
      Object.assign(form, res.data)
    }
  } catch {
    // 使用默认值
  } finally {
    loading.value = false
  }
}

const saveConfig = async () => {
  saving.value = true
  try {
    const res = await storeApi.saveSyncConfig(form)
    if (res.code === 200) {
      ElMessage.success('保存成功')
    } else {
      ElMessage.error(res.message || '保存失败')
    }
  } catch {
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

onMounted(() => loadConfig())
</script>

<style scoped lang="scss">
.sync-settings {
  .mb-2 { margin-bottom: 8px; }
  .ml-2 { margin-left: 8px; }

  .flex { display: flex; }
  .justify-between { justify-content: space-between; }
  .items-center { align-items: center; }

  .font-bold { font-weight: bold; }
  .text-xs { font-size: 12px; }
  .text-gray { color: #909399; }

  .config-form-wrap {
    min-height: 200px;
  }
}
</style>
