<template>
  <div class="platform-strategy">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="never" header="店铺分组管理" class="group-card">
          <div class="mb-3 flex justify-end">
            <el-button size="small" type="primary" plain @click="openAddGroup"><el-icon><Plus /></el-icon> 新建分组</el-button>
          </div>
          <div v-loading="groupsLoading" class="group-list">
            <el-menu :default-active="activeGroupId" class="group-menu" @select="onGroupSelect">
              <el-menu-item index="0">全部店铺</el-menu-item>
              <el-menu-item v-for="g in groups" :key="g.id" :index="String(g.id)">
                {{ g.groupName }}
                <el-button link type="danger" size="small" class="del-btn" @click.stop="deleteGroup(g)">删除</el-button>
              </el-menu-item>
            </el-menu>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card shadow="never" header="价格与调价策略 (按分组)">
          <el-alert
            title="汇率波动自动调价策略"
            type="info"
            description="当检测到日元汇率剧烈波动时，系统可根据设定规则自动调整所属店铺的商品售价，确保利润率。"
            show-icon
            class="mb-4"
            :closable="false"
          />

          <el-table :data="strategyData" border style="width: 100%">
            <el-table-column prop="ruleName" label="策略名称" width="150" />
            <el-table-column prop="trigger" label="触发条件" min-width="180">
              <template #default="{ row }">
                汇率 <span class="text-danger font-bold">{{ row.operator }}</span> {{ row.rateThreshold }}
              </template>
            </el-table-column>
            <el-table-column prop="action" label="执行动作" min-width="200">
              <template #default="{ row }">
                上调售价 <span class="text-primary font-bold">{{ row.adjustPercent }}%</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template #default="{ row }">
                <el-switch v-model="row.active" size="small" />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100" align="center">
              <template #default>
                <el-button link type="primary" size="small">编辑</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-card shadow="never" header="沙箱与测试环境" class="mt-4">
          <div class="sandbox-info text-sm text-gray">
            <p class="mb-2">为防止新员工误操作，系统提供与真实平台对接的沙箱环境。</p>
            <el-table :data="sandboxData" border size="small" style="width: 100%">
              <el-table-column prop="platform" label="平台" width="120" />
              <el-table-column prop="sandboxUrl" label="沙箱接口地址" />
              <el-table-column prop="status" label="连接状态" width="100" align="center">
                <template #default>
                  <el-tag size="small" type="success">已连通</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="addGroupVisible" title="新建分组" width="400px">
      <el-form :model="addGroupForm" label-width="80px">
        <el-form-item label="分组名称">
          <el-input v-model="addGroupForm.groupName" placeholder="如: 3C数码组" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addGroupVisible = false">取消</el-button>
        <el-button type="primary" :loading="addGroupSubmitting" @click="submitAddGroup">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { storeApi } from '../../../api/store'

const groupsLoading = ref(false)
const groups = ref<{ id: number; groupName: string }[]>([])
const activeGroupId = ref('0')
const addGroupVisible = ref(false)
const addGroupSubmitting = ref(false)
const addGroupForm = ref({ groupName: '' })

const strategyData = ref([
  { ruleName: '日元大幅贬值预警', operator: '跌破', rateThreshold: '0.045', adjustPercent: '5', active: true },
  { ruleName: '日常微调', operator: '跌破', rateThreshold: '0.048', adjustPercent: '2', active: false }
])

const sandboxData = ref([
  { platform: 'Amazon SP-API', sandboxUrl: 'https://sandbox.sellingpartnerapi-fe.amazon.com' },
  { platform: 'Rakuten RMS', sandboxUrl: 'https://api.rms.rakuten.co.jp/es/1.0/sandbox' }
])

const loadGroups = async () => {
  groupsLoading.value = true
  try {
    const res = await storeApi.listGroups()
    if (res.code === 200 && res.data) groups.value = res.data
    else groups.value = []
  } catch {
    groups.value = []
  } finally {
    groupsLoading.value = false
  }
}

const onGroupSelect = (index: string) => {
  activeGroupId.value = index
}

const openAddGroup = () => {
  addGroupForm.value.groupName = ''
  addGroupVisible.value = true
}

const submitAddGroup = async () => {
  if (!addGroupForm.value.groupName.trim()) {
    ElMessage.warning('请输入分组名称')
    return
  }
  addGroupSubmitting.value = true
  try {
    const res = await storeApi.createGroup(addGroupForm.value.groupName)
    if (res.code === 200) {
      ElMessage.success('创建成功')
      addGroupVisible.value = false
      loadGroups()
    } else {
      ElMessage.error(res.message || '创建失败')
    }
  } catch {
    ElMessage.error('创建失败')
  } finally {
    addGroupSubmitting.value = false
  }
}

const deleteGroup = async (g: { id: number; groupName: string }) => {
  try {
    await ElMessageBox.confirm(`确定删除分组「${g.groupName}」？`, '确认删除', { type: 'warning' })
    const res = await storeApi.deleteGroup(g.id)
    if (res.code === 200) {
      ElMessage.success('已删除')
      loadGroups()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('删除失败')
  }
}

onMounted(() => loadGroups())
</script>

<style scoped lang="scss">
.platform-strategy {
  .mb-2 { margin-bottom: 8px; }
  .mb-3 { margin-bottom: 12px; }
  .mb-4 { margin-bottom: 20px; }
  .mt-4 { margin-top: 16px; }

  .flex { display: flex; }
  .justify-end { justify-content: flex-end; }

  .font-bold { font-weight: bold; }
  .text-sm { font-size: 13px; }
  .text-gray { color: #909399; }
  .text-danger { color: #F56C6C; }
  .text-primary { color: #409EFF; }

  .group-card {
    height: 100%;
    .group-menu {
      border-right: none;
    }
    .del-btn {
      margin-left: auto;
      opacity: 0.6;
    }
  }
}
</style>
