<template>
  <div class="store-auth">
    <el-card shadow="never" class="mb-4">
      <div class="header-action">
        <h3>店铺授权列表</h3>
        <el-button type="primary" @click="openAddDialog"><el-icon><Plus /></el-icon> 接入新店铺</el-button>
      </div>

      <div class="platform-filters mt-3 mb-4">
        <el-radio-group v-model="filterPlatform" @change="loadStores">
          <el-radio-button label="all">全部平台</el-radio-button>
          <el-radio-button label="amazon">Amazon 日本站</el-radio-button>
          <el-radio-button label="rakuten">Rakuten 乐天</el-radio-button>
          <el-radio-button label="yahoo">Yahoo 雅虎</el-radio-button>
        </el-radio-group>
      </div>

      <div v-loading="loading" class="store-list-wrap">
      <el-row :gutter="20">
        <el-col :span="8" v-for="store in filteredStores" :key="store.id">
          <el-card shadow="hover" class="store-card mb-4" :class="{ 'expire-soon': store.daysLeft <= 15 && store.daysLeft > 0 }">
            <div class="store-header flex justify-between items-start">
              <div class="flex items-center">
                <el-avatar :size="40" class="platform-logo" :class="platformClass(store.platformCode)">
                  {{ (store.platform || store.platformCode || 'S').charAt(0) }}
                </el-avatar>
                <div class="ml-3">
                  <div class="font-bold text-lg">{{ store.storeName }}</div>
                  <div class="text-xs text-gray mt-1">{{ store.platform || store.platformCode }} | {{ store.currency || 'JPY' }}</div>
                </div>
              </div>
              <el-tag :type="statusTagType(store.status)">
                {{ store.status }}
              </el-tag>
            </div>

            <el-divider class="my-3" />

            <div class="store-info text-sm text-gray">
              <div class="flex justify-between mb-2">
                <span>时区:</span> <span>{{ store.timezone || 'Asia/Tokyo' }}</span>
              </div>
              <div class="flex justify-between mb-2">
                <span>默认发货仓:</span> <span>{{ store.defaultWarehouse || '-' }}</span>
              </div>
              <div class="flex justify-between mb-2">
                <span>负责人:</span> <span>{{ store.manager || '-' }}</span>
              </div>
              <div class="flex justify-between">
                <span>授权到期日:</span>
                <span :class="{ 'text-danger font-bold': store.daysLeft <= 15 }">
                  {{ store.expireDate || '-' }} (剩 {{ store.daysLeft ?? 0 }} 天)
                </span>
              </div>
            </div>

            <div class="store-actions mt-4 flex justify-end">
              <el-button size="small" type="primary" plain>店铺配置</el-button>
              <el-button size="small" :type="(store.daysLeft ?? 0) <= 15 ? 'warning' : 'default'" @click="reAuth(store)">重新授权</el-button>
              <el-button size="small" type="danger" link @click="unbind(store)">解绑</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
      </div>
    </el-card>

    <!-- 接入新店铺弹窗 -->
    <el-dialog v-model="addDialogVisible" title="接入新店铺" width="520px" @close="resetAddForm">
      <el-form ref="addFormRef" :model="addForm" :rules="addRules" label-width="120px">
        <el-form-item label="店铺名称" prop="storeName">
          <el-input v-model="addForm.storeName" placeholder="如: JP-Store-A" />
        </el-form-item>
        <el-form-item label="平台" prop="platformCode">
          <el-select v-model="addForm.platformCode" placeholder="请选择" style="width: 100%">
            <el-option label="乐天市场" value="RAKUTEN" />
            <el-option label="亚马逊日本" value="AMAZON_JP" />
            <el-option label="雅虎购物" value="YAHOO_JP" />
          </el-select>
        </el-form-item>
        <el-form-item label="卖家ID" prop="sellerId">
          <el-input v-model="addForm.sellerId" placeholder="平台卖家账号/ID" />
        </el-form-item>
        <el-form-item label="API Key">
          <el-input v-model="addForm.apiKey" placeholder="乐天等平台 Application ID" />
        </el-form-item>
        <el-form-item label="API Secret">
          <el-input v-model="addForm.apiSecret" type="password" placeholder="密钥" show-password />
        </el-form-item>
        <el-form-item label="默认发货仓">
          <el-select v-model="addForm.defaultWarehouseId" placeholder="请选择" clearable style="width: 100%">
            <el-option v-for="w in warehouses" :key="w.id" :label="w.warehouseName" :value="w.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="沙箱环境">
          <el-switch v-model="addForm.isSandbox" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="addSubmitting" @click="submitAdd">确定接入</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { storeApi, type StoreVO, type StoreCreateDTO, type Warehouse } from '../../../api/store'

const filterPlatform = ref('all')
const loading = ref(false)
const stores = ref<StoreVO[]>([])
const warehouses = ref<Warehouse[]>([])
const addDialogVisible = ref(false)
const addSubmitting = ref(false)
const addFormRef = ref<FormInstance>()

const addForm = ref<StoreCreateDTO & { isSandbox: boolean }>({
  storeName: '',
  platformCode: 'RAKUTEN',
  sellerId: '',
  apiKey: '',
  apiSecret: '',
  defaultWarehouseId: undefined,
  isSandbox: false
})

const addRules: FormRules = {
  storeName: [{ required: true, message: '请输入店铺名称', trigger: 'blur' }],
  platformCode: [{ required: true, message: '请选择平台', trigger: 'change' }],
  sellerId: [{ required: true, message: '请输入卖家ID', trigger: 'blur' }]
}

const filteredStores = computed(() => stores.value)

const platformClass = (code: string) => {
  const m: Record<string, string> = { RAKUTEN: 'rakuten', AMAZON_JP: 'amazon', YAHOO_JP: 'yahoo' }
  return m[code] || 'default'
}

const statusTagType = (status: string) => {
  if (status === '正常') return 'success'
  if (status === '即将过期') return 'warning'
  return 'danger'
}

const loadStores = async () => {
  loading.value = true
  try {
    const res = await storeApi.listStores(filterPlatform.value)
    if (res.code === 200 && res.data) stores.value = res.data
    else stores.value = []
  } catch {
    stores.value = []
  } finally {
    loading.value = false
  }
}

const loadWarehouses = async () => {
  try {
    const res = await storeApi.listWarehouses()
    if (res.code === 200 && res.data) warehouses.value = res.data
  } catch {
    warehouses.value = []
  }
}

const openAddDialog = () => {
  addDialogVisible.value = true
  loadWarehouses()
}

const resetAddForm = () => {
  addForm.value = {
    storeName: '',
    platformCode: 'RAKUTEN',
    sellerId: '',
    apiKey: '',
    apiSecret: '',
    defaultWarehouseId: undefined,
    isSandbox: false
  }
}

const submitAdd = async () => {
  if (!addFormRef.value) return
  await addFormRef.value.validate(async (valid) => {
    if (!valid) return
    addSubmitting.value = true
    try {
      const res = await storeApi.createStore(addForm.value)
      if (res.code === 200) {
        ElMessage.success('接入成功')
        addDialogVisible.value = false
        loadStores()
      } else {
        ElMessage.error(res.message || '接入失败')
      }
    } catch (e) {
      ElMessage.error('接入失败')
    } finally {
      addSubmitting.value = false
    }
  })
}

const reAuth = async (store: StoreVO) => {
  try {
    const res = await storeApi.getAuthUrl(store.id)
    if (res.code === 200 && res.data) {
      window.open(res.data, '_blank')
    } else {
      ElMessage.warning('请先配置 API Key 后重试')
    }
  } catch {
    ElMessage.error('获取授权链接失败')
  }
}

const unbind = async (store: StoreVO) => {
  try {
    await ElMessageBox.confirm(`确定解绑店铺「${store.storeName}」？解绑后需重新授权。`, '确认解绑', {
      type: 'warning'
    })
    const res = await storeApi.unbindStore(store.id)
    if (res.code === 200) {
      ElMessage.success('已解绑')
      loadStores()
    } else {
      ElMessage.error(res.message || '解绑失败')
    }
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('解绑失败')
  }
}

onMounted(() => loadStores())
</script>

<style scoped lang="scss">
.store-auth {
  .header-action {
    display: flex;
    justify-content: space-between;
    align-items: center;
    h3 { margin: 0; }
  }

  .mb-2 { margin-bottom: 8px; }
  .mb-4 { margin-bottom: 20px; }
  .mt-1 { margin-top: 4px; }
  .mt-3 { margin-top: 12px; }
  .mt-4 { margin-top: 16px; }
  .ml-3 { margin-left: 12px; }
  .my-3 { margin: 12px 0; }

  .flex { display: flex; }
  .justify-between { justify-content: space-between; }
  .justify-end { justify-content: flex-end; }
  .items-center { align-items: center; }
  .items-start { align-items: flex-start; }

  .text-xs { font-size: 12px; }
  .text-sm { font-size: 13px; }
  .text-lg { font-size: 16px; }
  .font-bold { font-weight: bold; }
  .text-gray { color: #909399; }
  .text-danger { color: #F56C6C; }

  .store-list-wrap {
    min-height: 120px;
  }

  .store-card {
    border-top: 4px solid transparent;
    transition: all 0.3s;

    &.expire-soon {
      border-top-color: #E6A23C;
    }

    .platform-logo {
      color: #fff;
      font-weight: bold;
      &.amazon { background-color: #FF9900; }
      &.rakuten { background-color: #BF0000; }
      &.yahoo { background-color: #FF0033; }
      &.default { background-color: #909399; }
    }
  }
}
</style>
