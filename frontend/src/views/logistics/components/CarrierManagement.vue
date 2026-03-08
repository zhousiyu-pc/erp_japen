<template>
  <div class="carrier-management">
    <el-card shadow="never" class="mb-4">
      <div class="header-action">
        <h3>物流商与渠道配置</h3>
        <el-button type="primary"><el-icon><Plus /></el-icon> 接入新物流商</el-button>
      </div>

      <el-tabs v-model="activeCarrierTab" class="mt-3">
        <el-tab-pane label="日本本地末端" name="local">
          <el-table :data="localCarriers" border style="width: 100%">
            <el-table-column prop="name" label="承运商" width="180">
              <template #default="{ row }">
                <div class="font-bold">{{ row.name }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="code" label="代码" width="120" />
            <el-table-column label="支持服务" min-width="200">
              <template #default="{ row }">
                <el-tag size="small" v-for="srv in row.services" :key="srv" class="mr-1 mb-1">{{ srv }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="接口状态" width="100" align="center">
              <template #default="{ row }">
                <el-tag :type="row.status === '正常' ? 'success' : 'danger'">{{ row.status }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" align="center">
              <template #default>
                <el-button link type="primary" size="small">配置面单</el-button>
                <el-button link type="primary" size="small">计费规则</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="跨境干线 (中->日)" name="cross">
          <el-table :data="crossCarriers" border style="width: 100%">
            <el-table-column prop="name" label="专线/承运商" width="180" />
            <el-table-column prop="type" label="运输方式" width="100">
              <template #default="{ row }">
                <el-tag :type="row.type === '海运' ? 'info' : 'warning'">{{ row.type }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="time" label="参考时效" width="120" />
            <el-table-column prop="price" label="参考单价" width="150" />
            <el-table-column label="操作" width="100" align="center">
              <template #default>
                <el-button link type="primary" size="small">计费配置</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-card shadow="never" header="运费模板测试计算器">
      <el-form :inline="true" :model="calcForm">
        <el-form-item label="重量(g)">
          <el-input-number v-model="calcForm.weight" :min="0" :step="100" />
        </el-form-item>
        <el-form-item label="体积(cm³)">
          <el-input v-model="calcForm.volume" placeholder="长*宽*高" style="width: 120px" />
        </el-form-item>
        <el-form-item label="目的地区域">
          <el-select v-model="calcForm.region" style="width: 120px">
            <el-option label="关东 (东京等)" value="kanto" />
            <el-option label="关西 (大阪等)" value="kansai" />
            <el-option label="北海道" value="hokkaido" />
            <el-option label="冲绳" value="okinawa" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="success">计算最优方案</el-button>
        </el-form-item>
      </el-form>
      
      <div class="calc-result mt-3" v-if="showResult">
        <el-alert title="推荐方案：日本邮政 (ゆうパック) - 预计运费 ¥850，时效 1-2天" type="success" show-icon :closable="false" />
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'

const activeCarrierTab = ref('local')

const localCarriers = ref([
  {
    name: '黑猫宅急便 (Yamato)',
    code: 'YAMATO',
    services: ['宅急便', '宅急便Compact', 'Nekopos'],
    status: '正常'
  },
  {
    name: '佐川急便 (Sagawa)',
    code: 'SAGAWA',
    services: ['飞脚宅配便', '飞脚大件便'],
    status: '正常'
  },
  {
    name: '日本邮政 (Japan Post)',
    code: 'JP_POST',
    services: ['ゆうパック', 'ゆうパケット', '定形外'],
    status: '正常'
  }
])

const crossCarriers = ref([
  {
    name: '中日快速海运专线',
    type: '海运',
    time: '5-7 天',
    price: '¥ 12.0 / kg'
  },
  {
    name: '大阪直飞空运包裹',
    type: '空运',
    time: '2-3 天',
    price: '¥ 35.0 / kg'
  }
])

const calcForm = reactive({
  weight: 500,
  volume: '',
  region: 'kanto'
})

const showResult = ref(true) // Mock showing result
</script>

<style scoped lang="scss">
.carrier-management {
  .mb-4 { margin-bottom: 20px; }
  .mt-3 { margin-top: 15px; }
  .mr-1 { margin-right: 5px; }
  .mb-1 { margin-bottom: 5px; }
  
  .header-action {
    display: flex;
    justify-content: space-between;
    align-items: center;
    h3 { margin: 0; }
  }

  .font-bold { font-weight: bold; }
}
</style>
