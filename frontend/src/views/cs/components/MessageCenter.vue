<template>
  <div class="message-center">
    <el-row :gutter="20" class="full-height">
      <!-- Session List -->
      <el-col :span="7" class="full-height">
        <el-card shadow="never" class="session-card full-height" body-class="p-0">
          <div class="filter-area p-3 border-bottom">
            <el-input placeholder="搜索买家/订单号" prefix-icon="Search" clearable v-model="searchKey" />
            <div class="flex justify-between mt-2">
              <el-select v-model="platform" size="small" style="width: 48%">
                <el-option label="全部平台" value="all" />
                <el-option label="Amazon" value="amazon" />
                <el-option label="Rakuten" value="rakuten" />
              </el-select>
              <el-select v-model="status" size="small" style="width: 48%">
                <el-option label="待回复 (SLA预警)" value="pending" />
                <el-option label="已回复" value="replied" />
              </el-select>
            </div>
          </div>
          <div class="session-list">
            <div 
              v-for="item in sessions" 
              :key="item.id" 
              class="session-item" 
              :class="{ active: currentSession?.id === item.id }"
              @click="currentSession = item"
            >
              <div class="flex justify-between items-start">
                <div class="buyer-name font-bold">{{ item.buyer }}</div>
                <div class="time text-xs text-gray">{{ item.time }}</div>
              </div>
              <div class="flex justify-between items-center mt-1">
                <div class="platform text-xs"><el-tag size="small" :type="item.platform === 'Amazon' ? 'warning' : 'danger'">{{ item.platform }}</el-tag></div>
                <div>
                  <el-tag size="small" :type="getEmotionType(item.emotion)" effect="plain">
                    {{ item.emotion === 'angry' ? '愤怒' : (item.emotion === 'happy' ? '满意' : '中性') }}
                  </el-tag>
                </div>
              </div>
              <div class="message-preview mt-2 text-sm text-gray text-ellipsis">
                {{ item.lastMessage }}
              </div>
              <div v-if="item.slaWarning" class="sla-warning mt-1 text-xs text-danger">
                <el-icon><Warning /></el-icon> 距离SLA超时还剩 {{ item.slaLeft }} 分钟
              </div>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- Chat & Reply Area -->
      <el-col :span="11" class="full-height">
        <el-card shadow="never" class="chat-card full-height flex flex-col" body-class="p-0 flex flex-col full-height">
          <template v-if="currentSession">
            <div class="chat-header p-3 border-bottom flex justify-between items-center bg-gray-50">
              <div>
                <span class="font-bold text-lg">{{ currentSession.buyer }}</span>
                <span class="ml-2 text-xs text-gray">订单号: <el-link type="primary">{{ currentSession.orderId }}</el-link></span>
              </div>
              <div>
                <el-button size="small" type="warning" plain>转交</el-button>
                <el-button size="small" type="success" plain>完结</el-button>
              </div>
            </div>
            
            <div class="chat-history p-3 flex-1 overflow-y-auto">
              <div class="message-bubble buyer-msg mb-4">
                <div class="msg-meta text-xs text-gray mb-1">{{ currentSession.buyer }} {{ currentSession.time }}</div>
                <div class="msg-content bg-gray-100 p-2 rounded">
                  {{ currentSession.lastMessage }}
                  <div class="mt-2 text-xs text-gray">
                    [翻译]: {{ currentSession.translation }}
                  </div>
                </div>
              </div>
            </div>

            <div class="reply-area p-3 border-top">
              <div class="ai-suggestion mb-2 flex justify-between items-center bg-blue-50 p-2 rounded border border-blue-100">
                <span class="text-primary text-sm flex items-center"><el-icon class="mr-1"><MagicStick /></el-icon> AI 智能回复建议:</span>
                <el-button size="small" type="primary" @click="useAiReply">一键应用</el-button>
              </div>
              <el-input 
                v-model="replyText" 
                type="textarea" 
                :rows="4" 
                placeholder="输入回复内容 (支持模板变量如 {buyerName})..." 
              />
              <div class="flex justify-between items-center mt-2">
                <div>
                  <el-button size="small" plain>插入模板</el-button>
                  <el-button size="small" plain>插入商品卡片</el-button>
                </div>
                <el-button type="primary" @click="sendReply">发送回复</el-button>
              </div>
            </div>
          </template>
          <div v-else class="empty-chat flex items-center justify-center full-height text-gray">
            <el-empty description="请选择左侧会话" />
          </div>
        </el-card>
      </el-col>

      <!-- Context Info -->
      <el-col :span="6" class="full-height">
        <el-card shadow="never" class="info-card full-height overflow-y-auto" v-if="currentSession">
          <template #header>
            <div class="font-bold">订单与商品上下文</div>
          </template>
          <div class="order-info mb-4">
            <div class="info-item mb-2">
              <span class="text-gray text-sm">订单状态:</span>
              <el-tag size="small" type="success" class="ml-2">已发货</el-tag>
            </div>
            <div class="info-item mb-2">
              <span class="text-gray text-sm">物流信息:</span>
              <span class="text-sm ml-2">黑猫宅急便 (派送中)</span>
            </div>
            <div class="info-item mb-2">
              <span class="text-gray text-sm">下单时间:</span>
              <span class="text-sm ml-2">2026-02-28 14:30:00</span>
            </div>
          </div>
          <el-divider />
          <div class="product-info">
            <div class="text-sm font-bold mb-2">关联商品 (1)</div>
            <div class="flex items-center">
              <el-image style="width: 50px; height: 50px" class="bg-gray-100 mr-2 rounded" />
              <div class="flex-1">
                <div class="text-sm text-ellipsis">无线蓝牙耳机 降噪 运动防水</div>
                <div class="text-xs text-gray mt-1">SKU: SKU-001-BLK-M</div>
                <div class="text-xs text-danger mt-1">¥ 4,500</div>
              </div>
            </div>
          </div>
          <el-divider />
          <div class="quick-actions">
            <el-button class="w-full mb-2">创建售后工单</el-button>
            <el-button class="w-full mb-2">申请部分退款</el-button>
            <el-button class="w-full">修改地址拦截</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const searchKey = ref('')
const platform = ref('all')
const status = ref('pending')

const sessions = ref([
  {
    id: 1,
    buyer: 'Takahashi',
    time: '10:05',
    platform: 'Amazon',
    orderId: '114-1234567-890',
    emotion: 'angry',
    lastMessage: '商品がまだ届きません。どうなっていますか？',
    translation: '商品还没到。怎么回事？',
    slaWarning: true,
    slaLeft: 15
  },
  {
    id: 2,
    buyer: 'Yuki M.',
    time: '09:30',
    platform: 'Rakuten',
    orderId: 'RK-99887766',
    emotion: 'neutral',
    lastMessage: '領収書を発行してもらえますか？',
    translation: '可以给我开发票吗？',
    slaWarning: false,
    slaLeft: 120
  }
])

const currentSession = ref<any>(sessions.value[0])
const replyText = ref('')

const getEmotionType = (emotion: string) => {
  if (emotion === 'angry') return 'danger'
  if (emotion === 'happy') return 'success'
  return 'info'
}

const useAiReply = () => {
  if (currentSession.value.emotion === 'angry') {
    replyText.value = `大変申し訳ございません、${currentSession.value.buyer}様。\n\nご注文いただいた商品は現在配送中ですが、遅延が発生しております。配送業者に確認したところ、明日にはお届けできる予定です。\n\nご迷惑をおかけして申し訳ありませんが、もう少々お待ちいただけますでしょうか。`
  } else {
    replyText.value = `${currentSession.value.buyer}様、お問い合わせありがとうございます。\n\nかしこまりました。領収書は以下のリンクからダウンロード可能です：[Link]\n\nよろしくお願いいたします。`
  }
  ElMessage.success('已应用 AI 推荐话术')
}

const sendReply = () => {
  if (!replyText.value) return
  ElMessage.success('回复发送成功')
  replyText.value = ''
}
</script>

<style scoped lang="scss">
.message-center {
  height: calc(100vh - 180px);
  
  .full-height {
    height: 100%;
  }

  .session-card {
    border-right: 1px solid #ebeef5;
    
    .session-list {
      height: calc(100% - 90px);
      overflow-y: auto;
    }
    
    .session-item {
      padding: 12px 15px;
      border-bottom: 1px solid #ebeef5;
      cursor: pointer;
      transition: background-color 0.2s;
      
      &:hover {
        background-color: #f5f7fa;
      }
      
      &.active {
        background-color: #ecf5ff;
        border-left: 3px solid #409eff;
      }
      
      .text-ellipsis {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
  }

  .chat-card {
    .bg-gray-50 { background-color: #f8f9fa; }
    .bg-gray-100 { background-color: #f0f2f5; }
    .bg-blue-50 { background-color: #ecf5ff; }
    .border-blue-100 { border-color: #d9ecff; }
    
    .message-bubble {
      max-width: 80%;
      .msg-content {
        display: inline-block;
        border-radius: 6px;
      }
    }
  }
  
  .info-card {
    .w-full { width: 100%; }
  }

  .p-0 { padding: 0 !important; }
  .p-2 { padding: 8px; }
  .p-3 { padding: 12px; }
  .mb-1 { margin-bottom: 4px; }
  .mb-2 { margin-bottom: 8px; }
  .mb-4 { margin-bottom: 16px; }
  .mt-1 { margin-top: 4px; }
  .mt-2 { margin-top: 8px; }
  .mr-1 { margin-right: 4px; }
  .mr-2 { margin-right: 8px; }
  .ml-2 { margin-left: 8px; }
  
  .flex { display: flex; }
  .flex-col { flex-direction: column; }
  .justify-between { justify-content: space-between; }
  .justify-center { justify-content: center; }
  .items-center { align-items: center; }
  .items-start { align-items: flex-start; }
  .flex-1 { flex: 1; }
  
  .border-bottom { border-bottom: 1px solid #ebeef5; }
  .border-top { border-top: 1px solid #ebeef5; }
  .rounded { border-radius: 4px; }
  
  .font-bold { font-weight: bold; }
  .text-xs { font-size: 12px; }
  .text-sm { font-size: 13px; }
  .text-lg { font-size: 16px; }
  .text-gray { color: #909399; }
  .text-primary { color: #409EFF; }
  .text-danger { color: #F56C6C; }
  .overflow-y-auto { overflow-y: auto; }
}
</style>
