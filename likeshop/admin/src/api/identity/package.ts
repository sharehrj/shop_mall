import request from '@/utils/request'

// 身份套餐列表
export function identityPackageList(params?: any) {
    return request.get({ url: '/identity/package/list', params })
}

// 身份套餐详情
export function identityPackageDetail(params?: any) {
    return request.get({ url: '/identity/package/detail', params })
}

// 身份套餐新增
export function identityPackageAdd(params?: any) {
    return request.post({ url: '/identity/package/add', params })
}

// 身份套餐编辑
export function identityPackageEdit(params?: any) {
    return request.post({ url: '/identity/package/edit', params })
}

// 身份套餐删除
export function identityPackageDel(params?: any) {
    return request.post({ url: '/identity/package/del', params })
}
