<template>
  <div class="product-optimization">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover" header="日文文案评分与检测">
          <el-form label-position="top">
            <el-form-item label="输入待检测文案">
              <el-input type="textarea" :rows="6" placeholder="请输入日文标题或描述..." v-model="textToScore" />
            </el-form-item>
            <el-button type="primary" @click="checkCompliance">检测合规性与评分</el-button>
          </el-form>
          
          <div v-if="scoreResult" class="result-area mt-4">
            <el-descriptions title="检测结果" :column="1" border>
              <el-descriptions-item label="综合评分">
                <el-rate v-model="scoreResult.score" disabled show-score text-color="#ff9900" score-template="{value} 分" />
              </el-descriptions-item>
              <el-descriptions-item label="合规风险 (药事法/景表法)">
                <el-tag type="danger" v-if="scoreResult.risks.length > 0">发现敏感词</el-tag>
                <el-tag type="success" v-else>未发现风险</el-tag>
                <ul v-if="scoreResult.risks.length > 0" class="risk-list">
                  <li v-for="(risk, index) in scoreResult.risks" :key="index">
                    关键词: <strong>{{ risk.word }}</strong> - {{ risk.reason }}
                  </li>
                </ul>
              </el-descriptions-item>
              <el-descriptions-item label="优化建议">
                {{ scoreResult.suggestion }}
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card shadow="hover" header="AI 智能生成">
          <el-alert title="输入核心卖点与关键词，自动生成日文标题、五点、描述，多版本可选。" type="success" :closable="false" class="mb-3" />
          <!-- Placeholder for AI generation interface, similar to what's in the Drawer but more advanced -->
          <el-empty description="AI 批量生成工具开发中" :image-size="100" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const textToScore = ref('世界一のダイエット効果！絶対に痩せる魔法のサプリメント')
const scoreResult = ref<any>(null)

const checkCompliance = () => {
  if (!textToScore.value) {
    ElMessage.warning('请输入文案')
    return
  }
  
  // Mock check
  scoreResult.value = {
    score: 2.5,
    risks: [
      { word: '世界一', reason: '涉嫌违反景表法（夸大宣传，无客观依据的最高级表述）' },
      { word: '絶対に痩せる', reason: '涉嫌违反药事法（对保健品承诺绝对的医疗/减肥效果）' }
    ],
    suggestion: '建议移除绝对化用语，使用「サポートする」「期待できる」等客观表述。增加核心关键词密度。'
  }
}
</script>

<style scoped lang="scss">
.product-optimization {
  .mt-4 {
    margin-top: 20px;
  }
  .mb-3 {
    margin-bottom: 15px;
  }
  .risk-list {
    margin: 5px 0 0;
    padding-left: 20px;
    color: #F56C6C;
  }
}
</style>
