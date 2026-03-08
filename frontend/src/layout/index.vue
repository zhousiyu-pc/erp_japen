<template>
  <el-container class="layout-container lingxing-theme">
    <el-aside width="200px" class="aside">
      <div class="logo">
        <h2 class="flex items-center justify-center">
          <el-icon class="mr-2"><Monitor /></el-icon>
          跨境ERP系统
        </h2>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="el-menu-vertical"
        background-color="#1e222d"
        text-color="#c1c6c8"
        active-text-color="#fff"
        router
      >
        <template v-for="route in menuRoutes" :key="route.path">
          <el-menu-item :index="'/' + route.path">
            <el-icon>
              <component :is="route.meta?.icon" />
            </el-icon>
            <span>{{ route.meta?.title }}</span>
          </el-menu-item>
        </template>
      </el-menu>
    </el-aside>
    
    <el-container class="main-container">
      <el-header class="header" height="50px">
        <div class="header-left">
          <el-icon class="toggle-btn"><Fold /></el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentRouteTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right flex items-center">
          <el-tooltip content="同步中心" placement="bottom">
            <el-icon class="action-icon"><Refresh /></el-icon>
          </el-tooltip>
          <el-tooltip content="消息通知" placement="bottom">
            <el-badge is-dot class="action-badge">
              <el-icon class="action-icon"><Bell /></el-icon>
            </el-badge>
          </el-tooltip>
          <el-dropdown @command="handleUserCommand">
            <span class="el-dropdown-link user-profile">
              <el-avatar :size="28" class="mr-2">{{ userDisplay }}</el-avatar>
              {{ userDisplay }} <el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="settings">系统设置</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- Tags View (Lingxing Style Multi-tab) -->
      <div class="tags-view-container">
        <el-scrollbar class="tags-scrollbar">
          <div class="tags-wrapper">
            <router-link
              v-for="tag in visitedViews"
              :key="tag.path"
              :to="{ path: tag.path }"
              class="tags-view-item"
              :class="{ active: isActive(tag.path) }"
            >
              {{ tag.title }}
              <el-icon v-if="visitedViews.length > 1" class="close-icon" @click.prevent.stop="closeSelectedTag(tag)">
                <Close />
              </el-icon>
            </router-link>
          </div>
        </el-scrollbar>
      </div>
      
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <keep-alive>
              <component :is="Component" />
            </keep-alive>
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useTagsViewStore } from '../store/tagsView'
import { useAuthStore } from '../store/auth'

const route = useRoute()
const router = useRouter()
const tagsViewStore = useTagsViewStore()
const authStore = useAuthStore()

const userDisplay = computed(() => authStore.userInfo?.username || authStore.userInfo?.realName || '用户')

const handleUserCommand = (cmd: string) => {
  if (cmd === 'logout') {
    authStore.logout()
    router.push('/login')
  } else if (cmd === 'profile') {
    router.push('/system')
  } else if (cmd === 'settings') {
    router.push('/system')
  }
}

// Get children of root route
const menuRoutes = computed(() => {
  const rootRoute = router.options.routes.find(r => r.path === '/')
  return rootRoute?.children || []
})

const activeMenu = computed(() => route.path)
const currentRouteTitle = computed(() => route.meta?.title || '')

const visitedViews = computed(() => tagsViewStore.visitedViews)

const isActive = (path: string) => {
  return path === route.path
}

const addTags = () => {
  const { name } = route
  if (name) {
    tagsViewStore.addView(route)
  }
}

const closeSelectedTag = (view: any) => {
  tagsViewStore.delView(view)
  if (isActive(view.path)) {
    const latestView = visitedViews.value.slice(-1)[0]
    if (latestView) {
      router.push(latestView.path)
    } else {
      router.push('/')
    }
  }
}

watch(() => route.path, () => {
  addTags()
})

onMounted(() => {
  addTags()
})
</script>

<style scoped lang="scss">
.layout-container {
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

/* Lingxing Style Theme */
.lingxing-theme {
  .aside {
    background-color: #1e222d;
    transition: width 0.28s;
    box-shadow: 2px 0 6px rgba(0,21,41,.15);
    z-index: 10;
    
    .logo {
      height: 50px;
      display: flex;
      align-items: center;
      justify-content: center;
      background-color: #171a22;
      color: #fff;
      h2 {
        margin: 0;
        font-size: 16px;
        font-weight: 600;
        letter-spacing: 1px;
      }
    }

    .el-menu-vertical {
      border-right: none;
      
      .el-menu-item {
        height: 46px;
        line-height: 46px;
        font-size: 13px;
        
        &:hover {
          background-color: #262b38 !important;
        }
        
        &.is-active {
          background-color: #409EFF !important;
          color: #fff;
          
          &::before {
            content: '';
            position: absolute;
            left: 0;
            top: 0;
            bottom: 0;
            width: 3px;
            background-color: #fff;
          }
        }
      }
    }
  }

  .main-container {
    display: flex;
    flex-direction: column;
  }

  .header {
    height: 50px;
    background-color: #fff;
    border-bottom: 1px solid #f0f2f5;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;

    .header-left {
      display: flex;
      align-items: center;
      
      .toggle-btn {
        font-size: 18px;
        cursor: pointer;
        margin-right: 20px;
        color: #5c6b77;
        
        &:hover { color: #409EFF; }
      }
    }

    .header-right {
      .action-icon {
        font-size: 18px;
        color: #5c6b77;
        margin-right: 20px;
        cursor: pointer;
        
        &:hover { color: #409EFF; }
      }
      
      .action-badge {
        margin-right: 20px;
      }

      .user-profile {
        display: flex;
        align-items: center;
        cursor: pointer;
        font-size: 13px;
        color: #333;
      }
    }
  }

  .tags-view-container {
    height: 34px;
    width: 100%;
    background: #fff;
    border-bottom: 1px solid #d8dce5;
    box-shadow: 0 1px 3px 0 rgba(0, 0, 0, .12), 0 0 3px 0 rgba(0, 0, 0, .04);
    
    .tags-wrapper {
      display: flex;
      align-items: center;
      padding: 4px 10px;
      
      .tags-view-item {
        display: inline-flex;
        align-items: center;
        position: relative;
        cursor: pointer;
        height: 26px;
        line-height: 26px;
        border: 1px solid #d8dce5;
        color: #495060;
        background: #fff;
        padding: 0 8px;
        font-size: 12px;
        margin-left: 5px;
        margin-top: 0px;
        text-decoration: none;
        border-radius: 2px;
        
        &:first-of-type { margin-left: 0; }
        
        &:hover {
          color: #409EFF;
          border-color: #c6e2ff;
          background-color: #ecf5ff;
        }

        &.active {
          background-color: #409EFF;
          color: #fff;
          border-color: #409EFF;
          
          &::before {
            content: '';
            background: #fff;
            display: inline-block;
            width: 8px;
            height: 8px;
            border-radius: 50%;
            position: relative;
            margin-right: 5px;
          }
        }
        
        .close-icon {
          margin-left: 4px;
          border-radius: 50%;
          font-size: 12px;
          padding: 1px;
          
          &:hover {
            background-color: #b4bccc;
            color: #fff;
          }
        }
      }
    }
  }

  .main-content {
    background-color: #f0f2f5;
    padding: 15px;
    height: calc(100vh - 84px);
    overflow-y: auto;
  }
}

.flex { display: flex; }
.items-center { align-items: center; }
.justify-center { justify-content: center; }
.mr-2 { margin-right: 8px; }

/* transition */
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all .3s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
