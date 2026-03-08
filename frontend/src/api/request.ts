/**
 * API 请求封装 - 预留后端对接
 * 后续可替换为真实 axios 实例
 */

const BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'

export interface ApiResponse<T = unknown> {
  code: number
  message: string
  data: T
}

export const request = {
  async get<T>(url: string, params?: Record<string, unknown>): Promise<ApiResponse<T>> {
    const qs = params ? '?' + new URLSearchParams(params as Record<string, string>).toString() : ''
    const res = await fetch(`${BASE_URL}${url}${qs}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('erp_token') || ''}`
      }
    })
    return res.json()
  },

  async post<T>(url: string, data?: unknown): Promise<ApiResponse<T>> {
    const res = await fetch(`${BASE_URL}${url}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('erp_token') || ''}`
      },
      body: data ? JSON.stringify(data) : undefined
    })
    return res.json()
  },

  async put<T>(url: string, data?: unknown): Promise<ApiResponse<T>> {
    const res = await fetch(`${BASE_URL}${url}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('erp_token') || ''}`
      },
      body: data ? JSON.stringify(data) : undefined
    })
    return res.json()
  },

  async delete<T>(url: string): Promise<ApiResponse<T>> {
    const res = await fetch(`${BASE_URL}${url}`, {
      method: 'DELETE',
      headers: { Authorization: `Bearer ${localStorage.getItem('erp_token') || ''}` }
    })
    return res.json()
  }
}
