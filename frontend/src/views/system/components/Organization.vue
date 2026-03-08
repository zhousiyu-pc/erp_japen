<template>
  <div class="organization">
    <el-row :gutter="20">
      <!-- Department Tree -->
      <el-col :span="6">
        <el-card shadow="never" class="org-tree-card">
          <div class="tree-header mb-3 flex justify-between items-center">
            <span class="font-bold">部门架构 (BU)</span>
            <div class="flex items-center gap-1">
              <el-button v-if="currentNode" link type="primary" size="small" @click="openEdit(currentNode)">编辑</el-button>
              <el-button v-if="currentNode" link type="danger" size="small" @click="handleDelete(currentNode)">删除</el-button>
              <el-dropdown @command="handleTreeCommand">
                <el-icon class="cursor-pointer"><MoreFilled /></el-icon>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="add-root">添加根组织</el-dropdown-item>
                    <el-dropdown-item :disabled="!currentNode" command="add-child">添加子部门</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
          <el-input v-model="filterText" placeholder="搜索部门" class="mb-3" clearable />
          <div v-loading="treeLoading" class="tree-wrap">
            <el-tree
              v-if="orgTree.length > 0"
              ref="treeRef"
              :data="orgTree"
              :props="defaultProps"
              default-expand-all
              :filter-node-method="filterNode"
              highlight-current
              node-key="id"
              @node-click="onNodeClick"
            >
              <template #default="{ node }">
                <span class="tree-node">
                  <span>{{ node.label }}</span>
                  <span v-if="node.data.orgCode" class="org-code">({{ node.data.orgCode }})</span>
                </span>
              </template>
            </el-tree>
            <el-empty v-else-if="!treeLoading" description="暂无组织数据，请添加根组织" />
          </div>
        </el-card>
      </el-col>

      <!-- Employee List (保留 mock，后续对接用户 API) -->
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

    <!-- 组织新增/编辑弹窗 -->
    <el-dialog
      v-model="orgDialogVisible"
      :title="orgForm.id ? '编辑组织' : '新增组织'"
      width="450px"
      @close="resetOrgForm"
    >
      <el-form :model="orgForm" label-width="90px">
        <el-form-item label="组织名称" required>
          <el-input v-model="orgForm.orgNameJa" placeholder="日文/中文名称，如：東京営業部" />
        </el-form-item>
        <el-form-item label="英文名称">
          <el-input v-model="orgForm.orgNameEn" placeholder="英文/拼音，如：Tokyo_Sales" />
        </el-form-item>
        <el-form-item label="组织编码" required>
          <el-input v-model="orgForm.orgCode" placeholder="4-20字符，大写英文+数字" :disabled="!!orgForm.id" />
        </el-form-item>
        <el-form-item label="上级组织">
          <el-tree-select
            v-model="orgForm.parentOrgId"
            :data="orgTreeForSelect"
            :props="{ label: 'label', value: 'id' }"
            placeholder="不选则为根组织"
            check-strictly
            :render-after-expand="false"
            clearable
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="orgForm.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item v-if="orgForm.id" label="状态">
          <el-radio-group v-model="orgForm.status">
            <el-radio :value="0">启用</el-radio>
            <el-radio :value="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="orgDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="orgSubmitLoading" @click="submitOrgForm">
          {{ orgForm.id ? '保存' : '创建' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted, computed } from 'vue'
import { ElTree, ElMessage, ElMessageBox } from 'element-plus'
import { systemApi, type OrgTreeNode, type OrgCreateDTO } from '@/api/system'

const filterText = ref('')
const treeRef = ref<InstanceType<typeof ElTree>>()
const orgTree = ref<OrgTreeNode[]>([])
const treeLoading = ref(false)
const currentNode = ref<OrgTreeNode | null>(null)

const defaultProps = {
  children: 'children',
  label: 'label'
}

const orgDialogVisible = ref(false)
const orgSubmitLoading = ref(false)
const orgForm = ref<OrgCreateDTO & { id?: number }>({
  orgNameJa: '',
  orgNameEn: '',
  orgCode: '',
  parentOrgId: undefined,
  sortOrder: 0,
  status: 0
})

// 用于树形选择器（排除当前编辑节点及其子节点）
const orgTreeForSelect = computed(() => {
  if (!orgForm.value.id) return orgTree.value
  const excludeId = orgForm.value.id
  const exclude = (list: OrgTreeNode[]): OrgTreeNode[] =>
    list
      .filter((n) => n.id !== excludeId)
      .map((n) => ({
        ...n,
        children: n.children && n.children.length ? exclude(n.children) : undefined
      }))
  return exclude(orgTree.value)
})

function loadOrgTree() {
  treeLoading.value = true
  systemApi
    .getOrgTree()
    .then((res) => {
      if (res.code === 200 && res.data) {
        orgTree.value = res.data
      } else {
        ElMessage.error(res.message || '加载组织树失败')
      }
    })
    .catch(() => {
      ElMessage.error('加载组织树失败')
    })
    .finally(() => {
      treeLoading.value = false
    })
}

function handleTreeCommand(cmd: string) {
  if (cmd === 'add-root') {
    resetOrgForm()
    orgForm.value.parentOrgId = undefined
    orgForm.value.orgCode = ''
    orgForm.value.orgNameJa = ''
    orgForm.value.orgNameEn = ''
    orgForm.value.sortOrder = 0
    orgDialogVisible.value = true
  } else if (cmd === 'add-child' && currentNode.value) {
    resetOrgForm()
    orgForm.value.parentOrgId = currentNode.value.id
    orgForm.value.orgCode = ''
    orgForm.value.orgNameJa = ''
    orgForm.value.orgNameEn = ''
    orgForm.value.sortOrder = 0
    orgDialogVisible.value = true
  }
}

function onNodeClick(data: OrgTreeNode) {
  currentNode.value = data
  // TODO: 加载该部门下的员工
}

function resetOrgForm() {
  orgForm.value = {
    id: undefined,
    orgNameJa: '',
    orgNameEn: '',
    orgCode: '',
    parentOrgId: undefined,
    sortOrder: 0,
    status: 0
  }
}

function openEdit(node: OrgTreeNode) {
  if (!node) return
  orgForm.value = {
    id: node.id,
    orgNameJa: node.label,
    orgNameEn: '',
    orgCode: node.orgCode || '',
    parentOrgId: node.parentId && node.parentId > 0 ? node.parentId : undefined,
    sortOrder: 0,
    status: node.status ?? 0
  }
  orgDialogVisible.value = true
}

function handleDelete(node: OrgTreeNode) {
  if (!node) return
  const hasChildren = node.children && node.children.length > 0
  if (hasChildren) {
    ElMessage.warning('该组织下存在子组织，禁止删除')
    return
  }
  ElMessageBox.confirm('确定删除该组织吗？', '提示', {
    type: 'warning'
  })
    .then(() => {
      return systemApi.deleteOrg(node.id)
    })
    .then((res) => {
      if (res.code === 200) {
        ElMessage.success('删除成功')
        currentNode.value = null
        loadOrgTree()
      } else {
        ElMessage.error(res.message || '删除失败')
      }
    })
    .catch((e) => {
      if (e !== 'cancel') ElMessage.error('删除失败')
    })
}

function submitOrgForm() {
  if (!orgForm.value.orgNameJa?.trim()) {
    ElMessage.warning('请输入组织名称')
    return
  }
  if (!orgForm.value.orgCode?.trim()) {
    ElMessage.warning('请输入组织编码')
    return
  }
  const dto: OrgCreateDTO = {
    orgNameJa: orgForm.value.orgNameJa.trim(),
    orgNameEn: orgForm.value.orgNameEn?.trim(),
    orgCode: orgForm.value.orgCode.trim().toUpperCase(),
    parentOrgId: orgForm.value.parentOrgId,
    sortOrder: orgForm.value.sortOrder ?? 0,
    status: orgForm.value.status ?? 0
  }
  orgSubmitLoading.value = true
  const id = orgForm.value.id
  const action = id
    ? systemApi.updateOrg(id, dto)
    : systemApi.createOrg(dto)
  action
    .then((res) => {
      if (res.code === 200) {
        ElMessage.success(id ? '保存成功' : '创建成功')
        orgDialogVisible.value = false
        loadOrgTree()
      } else {
        ElMessage.error(res.message || '操作失败')
      }
    })
    .catch(() => ElMessage.error('操作失败'))
    .finally(() => {
      orgSubmitLoading.value = false
    })
}

watch(filterText, (val) => {
  treeRef.value?.filter(val)
})

const filterNode = (value: string, data: OrgTreeNode) => {
  if (!value) return true
  const label = data.label || ''
  const code = data.orgCode || ''
  return label.includes(value) || code.includes(value)
}

onMounted(() => {
  loadOrgTree()
})

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

  .tree-wrap {
    min-height: 300px;
  }

  .tree-node .org-code {
    color: #909399;
    font-size: 12px;
    margin-left: 4px;
  }
}
</style>
