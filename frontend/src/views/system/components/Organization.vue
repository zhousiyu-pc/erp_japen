<template>
  <div class="organization">
    <el-row :gutter="20">
      <!-- Department Tree -->
      <el-col :span="6">
        <el-card shadow="never" class="org-tree-card">
          <div class="tree-header mb-3 flex justify-between items-center">
            <span class="font-bold">部门架构 (BU)</span>
            <el-dropdown>
              <el-icon class="cursor-pointer"><MoreFilled /></el-icon>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>添加平级部门</el-dropdown-item>
                  <el-dropdown-item>添加子部门</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          <el-input v-model="filterText" placeholder="搜索部门" class="mb-3" />
          <el-tree
            ref="treeRef"
            :data="orgTree"
            :props="defaultProps"
            default-expand-all
            :filter-node-method="filterNode"
            highlight-current
          />
        </el-card>
      </el-col>

      <!-- Employee List -->
      <el-col :span="18">
        <el-card shadow="never">
          <div class="filter-bar mb-4 flex justify-between">
            <div class="left flex">
              <el-input placeholder="搜索姓名/工号/手机号" style="width: 250px" class="mr-2" />
              <el-select placeholder="状态" style="width: 120px">
                <el-option label="在职" value="active" />
                <el-option label="离职" value="inactive" />
              </el-select>
            </div>
            <div class="right">
              <el-button type="primary"><el-icon><Plus /></el-icon> 添加员工</el-button>
              <el-button plain>批量导入</el-button>
            </div>
          </div>

          <el-table :data="employeeData" border style="width: 100%">
            <el-table-column prop="empId" label="工号" width="100" />
            <el-table-column prop="name" label="姓名" width="120">
              <template #default="{ row }">
                <div class="flex items-center">
                  <el-avatar :size="24" class="mr-2">{{ row.name.charAt(0) }}</el-avatar>
                  <span class="font-bold">{{ row.name }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="department" label="所属部门" width="150" />
            <el-table-column prop="role" label="系统角色" min-width="150">
              <template #default="{ row }">
                <el-tag size="small" v-for="r in row.roles" :key="r" class="mr-1 mb-1">{{ r }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="phone" label="手机号" width="120" />
            <el-table-column prop="status" label="状态" width="80" align="center">
              <template #default="{ row }">
                <el-switch v-model="row.active" size="small" />
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180" fixed="right" align="center">
              <template #default>
                <el-button link type="primary" size="small">编辑</el-button>
                <el-button link type="primary" size="small">分配店铺</el-button>
                <el-button link type="danger" size="small">重置密码</el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <div class="mt-4 flex justify-end">
            <el-pagination background layout="total, prev, pager, next" :total="45" />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { ElTree } from 'element-plus'

const filterText = ref('')
const treeRef = ref<InstanceType<typeof ElTree>>()

const defaultProps = {
  children: 'children',
  label: 'label',
}

const orgTree = ref([
  {
    id: 1,
    label: '总经办 (租户: 星云跨境)',
    children: [
      {
        id: 2,
        label: '运营中心',
        children: [
          { id: 21, label: '日本 Amazon 一部' },
          { id: 22, label: '日本 Rakuten 二部' }
        ]
      },
      {
        id: 3,
        label: '供应链中心',
        children: [
          { id: 31, label: '采购部' },
          { id: 32, label: '国内仓储部' }
        ]
      },
      { id: 4, label: '财务部' },
      { id: 5, label: 'IT信息部' }
    ]
  }
])

watch(filterText, (val) => {
  treeRef.value!.filter(val)
})

const filterNode = (value: string, data: any) => {
  if (!value) return true
  return data.label.includes(value)
}

const employeeData = ref([
  { empId: 'E001', name: '周大牛', department: '日本 Amazon 一部', roles: ['运营主管', '广告操盘手'], phone: '13800000001', active: true },
  { empId: 'E002', name: '李小花', department: '日本 Amazon 一部', roles: ['客服专员'], phone: '13800000002', active: true },
  { empId: 'E015', name: '王建国', department: '采购部', roles: ['高级采购'], phone: '13900000015', active: true },
  { empId: 'E022', name: '张会计', department: '财务部', roles: ['财务审核'], phone: '13700000022', active: true }
])
</script>

<style scoped lang="scss">
.organization {
  .mb-3 { margin-bottom: 12px; }
  .mb-4 { margin-bottom: 20px; }
  .mr-1 { margin-right: 4px; }
  .mr-2 { margin-right: 8px; }
  .mb-1 { margin-bottom: 4px; }
  .mt-4 { margin-top: 16px; }

  .flex { display: flex; }
  .justify-between { justify-content: space-between; }
  .justify-end { justify-content: flex-end; }
  .items-center { align-items: center; }
  
  .font-bold { font-weight: bold; }
  .cursor-pointer { cursor: pointer; }

  .org-tree-card {
    min-height: 500px;
  }
}
</style>
