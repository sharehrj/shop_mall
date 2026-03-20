import request from '@/utils/request'

// 分佣记录列表
export function commissionRecordList(params?: any) {
    return request.get({ url: '/identity/commission/list', params })
}
