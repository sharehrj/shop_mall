import request from '@/utils/request'

// 区域代理列表
export function regionAgentList(params?: any) {
    return request.get({ url: '/identity/region-agent/list', params })
}

// 区域代理详情
export function regionAgentDetail(params?: any) {
    return request.get({ url: '/identity/region-agent/detail', params })
}

// 新增区域代理
export function regionAgentAdd(params?: any) {
    return request.post({ url: '/identity/region-agent/add', params })
}

// 编辑区域代理
export function regionAgentEdit(params?: any) {
    return request.post({ url: '/identity/region-agent/edit', params })
}

// 删除区域代理
export function regionAgentDel(params?: any) {
    return request.post({ url: '/identity/region-agent/del', params })
}
