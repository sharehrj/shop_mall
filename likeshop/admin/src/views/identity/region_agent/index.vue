<template>
    <div class="region-agent-list">
        <el-card class="!border-none" shadow="never">
            <el-alert
                type="warning"
                title="温馨提示：区域代理可获取其负责区域内订单的分红佣金。"
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
                <el-form-item label="区域级别">
                    <el-select v-model="queryParams.regionLevel" placeholder="全部" class="w-[140px]" clearable>
                        <el-option label="省" :value="1" />
                        <el-option label="市" :value="2" />
                        <el-option label="区县" :value="3" />
                    </el-select>
                </el-form-item>
                <el-form-item label="状态">
                    <el-select v-model="queryParams.status" placeholder="全部" class="w-[120px]" clearable>
                        <el-option label="启用" :value="1" />
                        <el-option label="禁用" :value="0" />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="resetPage">查询</el-button>
                    <el-button @click="resetParams">重置</el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <el-card class="!border-none mt-4" shadow="never">
            <div class="flex mb-4">
                <el-button type="primary" @click="handleAdd">
                    <template #icon><icon name="el-icon-Plus" /></template>
                    新增代理
                </el-button>
            </div>
            <el-table size="large" v-loading="pager.loading" :data="pager.lists">
                <el-table-column label="代理用户" min-width="220">
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
                <el-table-column label="区域" prop="regionName" min-width="120" />
                <el-table-column label="区域级别" prop="regionLevelName" min-width="100" />
                <el-table-column label="分红比例" min-width="100">
                    <template #default="{ row }">{{ row.ratio }}%</template>
                </el-table-column>
                <el-table-column label="到期时间" prop="expireTime" min-width="140" />
                <el-table-column label="状态" min-width="100">
                    <template #default="{ row }">
                        <el-tag :type="row.status ? 'success' : 'info'">
                            {{ row.status ? '启用' : '禁用' }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="创建时间" prop="createTime" min-width="180" />
                <el-table-column label="操作" width="150" fixed="right">
                    <template #default="{ row }">
                        <el-button type="primary" :link="true" @click="handleEdit(row)">编辑</el-button>
                        <el-button type="danger" :link="true" @click="handleDelete(row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="flex justify-end mt-4">
                <pagination v-model="pager" @change="getLists" />
            </div>
        </el-card>

        <edit-popup v-if="showEdit" ref="editRef" @success="getLists" @close="showEdit = false" />
    </div>
</template>
<script lang="ts" setup>
import { regionAgentList, regionAgentDel } from '@/api/identity/region_agent'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'
import EditPopup from './edit.vue'

const editRef = shallowRef<InstanceType<typeof EditPopup>>()
const showEdit = ref(false)

const queryParams = reactive({
    keyword: '',
    regionLevel: undefined as number | undefined,
    status: undefined as number | undefined
})

const { pager, getLists, resetPage, resetParams } = usePaging({
    fetchFun: regionAgentList,
    params: queryParams
})

const handleAdd = async () => {
    showEdit.value = true
    await nextTick()
    editRef.value?.open('add')
}

const handleEdit = async (row: any) => {
    showEdit.value = true
    await nextTick()
    editRef.value?.open('edit')
    editRef.value?.setFormData(row)
}

const handleDelete = async (id: number) => {
    try {
        await feedback.confirm('确定要删除该区域代理吗？')
        await regionAgentDel({ id })
        getLists()
    } catch (error) {
        console.log('删除失败', error)
    }
}

getLists()
</script>
