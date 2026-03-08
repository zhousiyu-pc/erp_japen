import { request } from './request'

export interface StoreVO {
  id: number
  storeName: string
  platformCode: string
  platform: string
  sellerId: string
  currency: string
  timezone: string
  defaultWarehouse: string
  manager: string
  authStatus: number
  status: string
  expireDate: string
  daysLeft: number
  isSandbox: boolean
  createTime: string
}

export interface StoreCreateDTO {
  storeName: string
  platformCode: string
  sellerId: string
  apiKey?: string
  apiSecret?: string
  redirectUri?: string
  defaultWarehouseId?: number
  managerId?: number
  isSandbox?: boolean
}

export interface SyncConfigDTO {
  orderSyncRate: string
  stockSyncStrategy: string
  stockReserve: number
  filterTestOrders: boolean
  excludeSkus: string[]
  globalSyncEnabled: boolean
}

export interface Warehouse {
  id: number
  warehouseName: string
  warehouseCode?: string
}

const platformCodeMap: Record<string, string> = {
  all: 'all',
  amazon: 'AMAZON_JP',
  rakuten: 'RAKUTEN',
  yahoo: 'YAHOO_JP',
  tiktok: 'TIKTOK',
  temu: 'TEMU'
}

export const storeApi = {
  listStores: (platformCode = 'all') =>
    request.get<StoreVO[]>('/v1/store/stores', { platformCode: platformCodeMap[platformCode] || platformCode }),

  createStore: (dto: StoreCreateDTO) =>
    request.post<number>('/v1/store/stores', dto),

  unbindStore: (id: number) =>
    request.delete<void>(`/v1/store/stores/${id}`),

  getAuthUrl: (id: number) =>
    request.get<string>(`/v1/store/stores/${id}/auth-url`),

  listWarehouses: () =>
    request.get<Warehouse[]>('/v1/store/warehouses'),

  getSyncConfig: () =>
    request.get<SyncConfigDTO>('/v1/store/sync-config'),

  saveSyncConfig: (dto: SyncConfigDTO) =>
    request.post<void>('/v1/store/sync-config', dto),

  listGroups: () =>
    request.get<{ id: number; groupName: string }[]>('/v1/store/groups'),

  createGroup: (groupName: string) =>
    request.post<number>('/v1/store/groups', { groupName }),

  deleteGroup: (id: number) =>
    request.delete<void>(`/v1/store/groups/${id}`)
}
