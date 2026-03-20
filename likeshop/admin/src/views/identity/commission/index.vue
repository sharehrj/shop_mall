<template>
    <div class="commission-record-list">
        <el-card class="!border-none" shadow="never">
            <el-alert
                type="warning"
                title="温馨提示：查看用户分佣记录，包括直推、复购、价差、培育及区域分红佣金。"
                :closable="false"
                show-icon
            />
            <el-form ref="formRef" class="mb-[-16px] mt-[16px]" :model="queryParams" :inline="true">
                <el-form-item label="用户信息">
                    <el-input
                        v-model="queryParams.keyword"
                        placeholder="请输入用户昵称/手机号"
                        class="w-[240px]"
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="佣金类型">
                    <el-select v-model="queryParams.commissionType" placeholder="全部" class="w-[160px]" clearable>
                        <el-option label="直推" :value="1" />
                        <el-option label="复购" :value="2" />
                        <el-option label="价差" :value="3" />
                        <el-option label="培育" :value="4" />
                        <el-option label="区域分红" :value="5" />
                    </el-select>
                </el-form-item>
                <el-form-item label="结算状态">
                    <el-select v-model="queryParams.status" placeholder="全部" class="w-[160px]" clearable>
                        <el-option label="待结算" :value="0" />
                        <el-option label="已结算" :value="1" />
                        <el-option label="已取消" :value="2" />
                    </el-select>
                </el-form-item>
                <el-form-item label="时间范围">
                    <daterange-picker
                        v-model:startTime="queryParams.startTime"
                        v-model:endTime="queryParams.endTime"
                    />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="resetPage">查询</el-button>
                    <el-button @click="resetParams">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-table size="large" v-loading="pager.loading" :data="pager.lists">
                <el-table-column label="获佣用户" min-width="200">
                    <template #default="{ row }">
                        <div class="flex items-center">
                            <image-contain
                                :src="row.userAvatar"
                                :width="40"
                                :height="40"
                                fit="contain"
                                :preview-src-list="[row.userAvatar]"
                                preview-teleported
                            />
                            <div class="ml-2">
                                <div>{{ row.userNickname }}</div>
                                <div class="text-gray-400 text-xs">{{ row.userMobile }}</div>
                            </div>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="消费用户" prop="fromUserNickname" min-width="120" />
                <el-table-column label="佣金类型" prop="commissionTypeName" min-width="100" />
                <el-table-column label="所属等级" prop="levelName" min-width="100" />
                <el-table-column label="佣金比例" min-width="100">
                    <template #default="{ row }">{{ row.ratio }}%</template>
                </el-table-column>
                <el-table-column label="商品金额" min-width="110">
                    <template #default="{ row }">¥{{ row.goodsMoney }}</template>
                </el-table-column>
                <el-table-column label="佣金金额" min-width="110">
                    <template #default="{ row }">
                        <span class="text-success font-bold">¥{{ row.commissionMoney }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="状态" min-width="100">
                    <template #default="{ row }">
                        <el-tag :type="row.status === 1 ? 'success' : row.status === 2 ? 'danger' : 'warning'">
                            {{ row.statusName }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="结算时间" prop="settleTime" min-width="180" />
                <el-table-column label="创建时间" prop="createTime" min-width="180" />
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>
    </div>
</template>
<script lang="ts" setup>
import { commissionRecordList } from '@/api/identity/commission'
import { usePaging } from '@/hooks/usePaging'

const queryParams = reactive({
    keyword: '',
    commissionType: undefined as number | undefined,
    status: undefined as number | undefined,
    startTime: '',
    endTime: ''
})

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: commissionRecordList,
    params: queryParams
})

getLists()
</script>
