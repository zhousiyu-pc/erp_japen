import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import Layout from '../layout/index.vue'
import { useAuthStore } from '../store/auth'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/login/index.vue'),
    meta: { title: '登录', noAuth: true }
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('../views/dashboard/index.vue'), meta: { title: '经营总览', icon: 'DataBoard' } },
      { path: 'store', name: 'Store', component: () => import('../views/store/index.vue'), meta: { title: '店铺与渠道', icon: 'Shop' } },
      { path: 'product', name: 'Product', component: () => import('../views/product/index.vue'), meta: { title: '商品中心', icon: 'Goods' } },
      { path: 'order', name: 'Order', component: () => import('../views/order/index.vue'), meta: { title: '订单中心', icon: 'List' } },
      { path: 'wms', name: 'WMS', component: () => import('../views/wms/index.vue'), meta: { title: '库存与仓储', icon: 'Box' } },
      { path: 'procurement', name: 'Procurement', component: () => import('../views/procurement/index.vue'), meta: { title: '采购与供应链', icon: 'ShoppingCart' } },
      { path: 'logistics', name: 'Logistics', component: () => import('../views/logistics/index.vue'), meta: { title: '物流与发货', icon: 'Van' } },
      { path: 'ads', name: 'Ads', component: () => import('../views/ads/index.vue'), meta: { title: '广告与推广', icon: 'DataLine' } },
      { path: 'cs', name: 'CS', component: () => import('../views/cs/index.vue'), meta: { title: '客服与工单', icon: 'Service' } },
      { path: 'finance', name: 'Finance', component: () => import('../views/finance/index.vue'), meta: { title: '财务与结算', icon: 'Money' } },
      { path: 'bi', name: 'BI', component: () => import('../views/bi/index.vue'), meta: { title: '统计与分析', icon: 'PieChart' } },
      { path: 'system', name: 'System', component: () => import('../views/system/index.vue'), meta: { title: '系统与组织', icon: 'Setting' } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, _from, next) => {
  const authStore = useAuthStore()
  if (to.meta.noAuth) {
    if (authStore.isLoggedIn() && to.path === '/login') {
      next('/')
    } else {
      next()
    }
    return
  }
  if (to.meta.requiresAuth && !authStore.isLoggedIn()) {
    next({ path: '/login', query: { redirect: to.fullPath } })
  } else {
    next()
  }
})

export default router
