<template>
    <div class="dividend-pool-list">
        <el-card class="!border-none" shadow="never">
            <el-alert
                type="warning"
                title="温馨提示：分红池记录每月销售额提取比例形成分红池，执行分配后将按规则发放给区域代理。"
                :closable="false"
                show-icon
            />
            <el-form ref="formRef" class="mb-[-16px] mt-[16px]" :model="queryParams" :inline="true">
                <el-form-item label="期次">
                    <el-input
                        v-model="queryParams.period"
                        placeholder="如：2026-03"
                        class="w-[160px]"
                        @keyup.enter="resetPage"
                    />
                </el-form-item>
                <el-form-item label="状态">
                    <el-select v-model="queryParams.status" placeholder="全部" class="w-[140px]" clearable>
                        <el-option label="待分配" :value="0" />
                        <el-option label="已分配" :value="1" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="resetPage">查询</el-button>
                    <el-button @click="resetParams">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <el-table size="large" v-loading="pager.loading" :data="pager.lists">
                <el-table-column label="ID" prop="id" width="80" />
                <el-table-column label="期次" prop="period" min-width="120" />
                <el-table-column label="总销售额" min-width="130">
                    <template #default="{ row }">¥{{ row.totalSales }}</template>
                </el-table-column>
                <el-table-column label="提取比例" min-width="100">
                    <template #default="{ row }">{{ row.poolRatio }}%</template>
                </el-table-column>
                <el-table-column label="分红池金额" min-width="130">
                    <template #default="{ row }">
                        <span class="text-success font-bold">¥{{ row.poolMoney }}</span>
                    </template>
                </el-table-column>
                <el-table-column label="状态" min-width="100">
                    <template #default="{ row }">
                        <el-tag :type="row.status ? 'success' : 'warning'">
                            {{ row.statusName }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="分配时间" prop="settleTime" min-width="180" />
                <el-table-column label="创建时间" prop="createTime" min-width="180" />
                <el-table-column label="操作" width="120" fixed="right">
                    <template #default="{ row }">
                        <el-button
                            v-if="row.status === 0"
                            type="primary"
                            :link="true"
                            @click="handleSettle(row.id)"
                        >
                            执行分配
                        </el-button>
                        <span v-else class="text-gray-400 text-sm">已分配</span>
                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>
    </div>
</template>
<script lang="ts" setup>
import { dividendPoolList, dividendPoolSettle } from '@/api/identity/dividend_pool'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'

const queryParams = reactive({
    period: '',
    status: undefined as number | undefined
})

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: dividendPoolList,
    params: queryParams
})

const handleSettle = async (id: number) => {
    try {
        await feedback.confirm('确定要执行本期分红分配吗？此操作不可撤销。')
        await dividendPoolSettle({ id })
        getLists()
    } catch (error) {
        console.log('执行分配失败', error)
    }
}

getLists()
</script>
