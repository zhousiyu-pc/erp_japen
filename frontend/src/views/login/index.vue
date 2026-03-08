<template>
  <div class="login-container">
    <div class="login-bg" />
    <div class="login-box">
      <div class="login-header">
        <el-icon class="logo-icon"><Monitor /></el-icon>
        <h1>日本跨境电商 ERP</h1>
        <p class="subtitle">一体化运营管理平台</p>
      </div>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        class="login-form"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入手机号/邮箱" size="large">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" size="large" show-password>
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <div class="flex justify-between items-center w-full">
            <el-checkbox v-model="form.remember">记住我</el-checkbox>
            <el-link type="primary" :underline="false">忘记密码？</el-link>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            class="w-full"
            :loading="loading"
            @click="handleLogin"
          >
            登 录
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <span class="text-gray">默认账号: 18929352591 / Sinolife2008</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { User, Lock } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { useAuthStore } from '../../store/auth'

const router = useRouter()
const authStore = useAuthStore()

const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  username: localStorage.getItem('erp_username') || '',
  password: '',
  remember: true
})

const rules: FormRules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    try {
      // 模拟登录，后续对接 /api/v1/auth/login
      await new Promise((r) => setTimeout(r, 800))
      const mockToken = 'mock_jwt_' + Date.now()
      authStore.setToken(mockToken)
      authStore.setUserInfo({ username: form.username, realName: '管理员' })
      if (form.remember) {
        localStorage.setItem('erp_username', form.username)
      } else {
        localStorage.removeItem('erp_username')
      }
      ElMessage.success('登录成功')
      router.replace('/')
    } catch {
      ElMessage.error('登录失败')
    } finally {
      loading.value = false
    }
  })
}

onMounted(() => {
  authStore.initFromStorage()
})
</script>

<style scoped lang="scss">
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.login-bg {
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, #1e222d 0%, #2c3e50 50%, #1e222d 100%);
  &::after {
    content: '';
    position: absolute;
    inset: 0;
    background: url("data:image/svg+xml,%3Csvg width='60' height='60' viewBox='0 0 60 60' xmlns='http://www.w3.org/2000/svg'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg fill='%23ffffff' fill-opacity='0.03'%3E%3Cpath d='M36 34v-4h-2v4h-4v2h4v4h2v-4h4v-2h-4zm0-30V0h-2v4h-4v2h4v4h2V6h4V4h-4zM6 34v-4H4v4H0v2h4v4h2v-4h4v-2H6zM6 4V0H4v4H0v2h4v4h2V6h4V4H6z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
    opacity: 0.5;
  }
}

.login-box {
  position: relative;
  z-index: 1;
  width: 420px;
  padding: 48px 40px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;

  .logo-icon {
    font-size: 48px;
    color: #409eff;
    margin-bottom: 16px;
  }

  h1 {
    margin: 0 0 8px;
    font-size: 24px;
    font-weight: 600;
    color: #303133;
  }

  .subtitle {
    margin: 0;
    font-size: 14px;
    color: #909399;
  }
}

.login-form {
  :deep(.el-input__wrapper) {
    padding: 4px 15px;
  }
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  font-size: 12px;
}

.flex { display: flex; }
.justify-between { justify-content: space-between; }
.items-center { align-items: center; }
.w-full { width: 100%; }
.text-gray { color: #909399; }
</style>
