import request from '@/utils/request'

// 分红池列表
export function dividendPoolList(params?: any) {
    return request.get({ url: '/identity/dividend-pool/list', params })
}

// 执行分配
export function dividendPoolSettle(params?: any) {
    return request.post({ url: '/identity/dividend-pool/settle', params })
}
