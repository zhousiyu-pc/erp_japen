<template>
  <el-drawer
    :model-value="visible"
    @update:model-value="$emit('update:visible', $event)"
    :title="isEdit ? '编辑商品' : '新建商品'"
    size="70%"
    :before-close="handleClose"
  >
    <div class="drawer-content">
      <el-tabs v-model="activeTab">
        <!-- 基础信息 -->
        <el-tab-pane label="基础信息" name="basic">
          <el-form :model="form" label-width="100px" :rules="rules" ref="formRef">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="SPU 编码" prop="spu">
                  <el-input v-model="form.spu" placeholder="自动生成或手动输入" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="商品类目" prop="category">
                  <el-cascader v-model="form.category" :options="[]" placeholder="请选择类目" style="width: 100%" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="商品主图">
              <el-upload
                action="#"
                list-type="picture-card"
                :auto-upload="false"
              >
                <el-icon><Plus /></el-icon>
              </el-upload>
            </el-form-item>
            <el-form-item label="品牌">
              <el-select v-model="form.brand" placeholder="请选择品牌">
                <el-option label="自有品牌 A" value="A" />
                <el-option label="无品牌" value="none" />
              </el-select>
            </el-form-item>
            
            <el-divider content-position="left">多变体设置 (SKU)</el-divider>
            <div class="variant-section">
              <el-button type="primary" plain size="small" class="mb-3">添加变体属性 (如颜色、尺码)</el-button>
              <el-table :data="mockSkus" border size="small">
                <el-table-column prop="sku" label="SKU 编码" width="150" />
                <el-table-column prop="color" label="颜色" width="100" />
                <el-table-column prop="size" label="尺码" width="100" />
                <el-table-column label="采购价(¥)" width="120">
                  <template #default>
                    <el-input-number v-model="mockPrice" size="small" :min="0" :controls="false" style="width: 100%" />
                  </template>
                </el-table-column>
                <el-table-column label="重量(g)" width="100">
                  <template #default>
                    <el-input-number v-model="mockWeight" size="small" :min="0" :controls="false" style="width: 100%" />
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="80">
                  <template #default>
                    <el-button link type="danger">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-form>
        </el-tab-pane>

        <!-- 多语言文案 -->
        <el-tab-pane label="多语言文案" name="i18n">
          <el-tabs type="border-card" v-model="langTab">
            <el-tab-pane label="日文 (主推)" name="jp">
              <div class="ai-assist-bar">
                <span class="title">AI 文案助手</span>
                <el-input v-model="aiKeywords" placeholder="输入核心卖点关键词，如：降噪, 运动, 防水" size="small" style="width: 300px; margin: 0 10px;" />
                <el-button type="primary" size="small" @click="generateText">
                  <el-icon><MagicStick /></el-icon> 一键生成日文文案
                </el-button>
              </div>
              
              <el-form label-position="top">
                <el-form-item label="商品标题 (Title)">
                  <el-input v-model="form.titleJp" maxlength="200" show-word-limit />
                  <div class="compliance-warning" v-if="showWarning">
                    <el-icon color="#E6A23C"><Warning /></el-icon> 提示：标题可能包含夸大表述，建议复核景表法规范。
                  </div>
                </el-form-item>
                <el-form-item label="五点描述 (Bullet Points)">
                  <el-input v-model="form.bulletJp" type="textarea" :rows="5" placeholder="每行一点..." />
                </el-form-item>
                <el-form-item label="详细描述 (Description)">
                  <el-input v-model="form.descJp" type="textarea" :rows="8" />
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="中文 (内部)" name="cn">
              <el-form label-position="top">
                <el-form-item label="商品标题">
                  <el-input v-model="form.titleCn" />
                </el-form-item>
                <el-form-item label="详细描述">
                  <el-input v-model="form.descCn" type="textarea" :rows="5" />
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="英文" name="en">
              <el-empty description="暂未配置英文文案" :image-size="100" />
            </el-tab-pane>
          </el-tabs>
        </el-tab-pane>
      </el-tabs>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { MagicStick } from '@element-plus/icons-vue'

const props = defineProps<{
  visible: boolean
  productData: any
}>()

const emit = defineEmits(['update:visible', 'saved'])

const activeTab = ref('basic')
const langTab = ref('jp')
const formRef = ref()

const isEdit = computed(() => !!props.productData)

const form = ref({
  spu: '',
  category: [],
  brand: '',
  titleJp: '',
  bulletJp: '',
  descJp: '',
  titleCn: '',
  descCn: ''
})

const rules = {
  spu: [{ required: true, message: '请输入 SPU 编码', trigger: 'blur' }]
}

// Mock Variant Data
const mockSkus = ref([
  { sku: 'SKU-001-BLK-M', color: '黑色', size: 'M' },
  { sku: 'SKU-001-WHT-M', color: '白色', size: 'M' }
])
const mockPrice = ref(50)
const mockWeight = ref(200)

// AI Assistant Mock
const aiKeywords = ref('')
const showWarning = ref(false)

watch(() => props.visible, (newVal) => {
  if (newVal) {
    activeTab.value = 'basic'
    langTab.value = 'jp'
    if (props.productData) {
      form.value.spu = props.productData.spu
      form.value.titleJp = props.productData.name
    } else {
      form.value = {
        spu: '', category: [], brand: '', titleJp: '', bulletJp: '', descJp: '', titleCn: '', descCn: ''
      }
    }
  }
})

const generateText = () => {
  if (!aiKeywords.value) {
    ElMessage.warning('请先输入关键词')
    return
  }
  ElMessage.success('AI 正在生成文案...')
  setTimeout(() => {
    form.value.titleJp = `【2026最新進化版】ワイヤレスイヤホン ${aiKeywords.value} Bluetooth 5.3`
    form.value.bulletJp = `・${aiKeywords.value}機能搭載\n・最大40時間連続再生\n・IPX7防水仕様`
    showWarning.value = true // Simulate compliance warning
  }, 1000)
}

const handleClose = () => {
  emit('update:visible', false)
}

const handleSave = () => {
  formRef.value?.validate((valid: boolean) => {
    if (valid) {
      ElMessage.success('保存成功')
      emit('saved')
    }
  })
}
</script>

<style scoped lang="scss">
.drawer-content {
  padding: 0 20px;
  
  .mb-3 {
    margin-bottom: 15px;
  }

  .variant-section {
    background: #f8f9fa;
    padding: 15px;
    border-radius: 4px;
  }

  .ai-assist-bar {
    display: flex;
    align-items: center;
    background: #f0f9eb;
    padding: 10px 15px;
    border-radius: 4px;
    margin-bottom: 20px;
    border: 1px solid #e1f3d8;

    .title {
      font-weight: bold;
      color: #67c23a;
      display: flex;
      align-items: center;
      gap: 5px;
    }
  }

  .compliance-warning {
    font-size: 12px;
    color: #E6A23C;
    margin-top: 5px;
    display: flex;
    align-items: center;
    gap: 5px;
  }
}
</style>
