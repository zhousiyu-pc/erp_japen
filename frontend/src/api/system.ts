import { request } from './request'

export interface OrgTreeNode {
  id: number
  label: string
  orgCode?: string
  parentId?: number
  status?: number
  children?: OrgTreeNode[]
}

export interface OrgCreateDTO {
  orgNameJa: string
  orgNameEn?: string
  parentOrgId?: number
  orgCode: string
  leaderId?: number
  sortOrder?: number
  status?: number
}

export const systemApi = {
  getOrgTree: (tenantId?: number) =>
    request.get<OrgTreeNode[]>('/v1/system/org/tree', tenantId != null ? { tenantId } : undefined),

  createOrg: (dto: OrgCreateDTO, tenantId?: number) =>
    request.post<number>(
      '/v1/system/org/create' + (tenantId != null ? `?tenantId=${tenantId}` : ''),
      dto
    ),

  updateOrg: (id: number, dto: OrgCreateDTO, tenantId?: number) =>
    request.put<void>(
      `/v1/system/org/update/${id}` + (tenantId != null ? `?tenantId=${tenantId}` : ''),
      dto
    ),

  deleteOrg: (id: number, tenantId?: number) =>
    request.delete<void>(`/v1/system/org/${id}` + (tenantId != null ? `?tenantId=${tenantId}` : ''))
}
