<template>
    <div class="identity-package-list">
        <el-card class="!border-none" shadow="never">
            <el-alert
                type="warning"
                title="温馨提示：身份套餐关联商品与等级，用户购买套餐商品后可升级至对应等级。"
                :closable="false"
                show-icon
            />
        </el-card>
        <el-card class="!border-none mt-4" shadow="never">
            <div class="flex mb-4">
                <el-button type="primary" @click="handleAdd">
                    <template #icon><icon name="el-icon-Plus" /></template>
                    新增套餐
                </el-button>
            </div>
            <el-table size="large" v-loading="pager.loading" :data="pager.lists">
                <el-table-column label="ID" prop="id" width="80" />
                <el-table-column label="套餐商品" min-width="260">
                    <template #default="{ row }">
                        <div class="flex items-center">
                            <image-contain
                                v-if="row.goodsImage"
                                :src="row.goodsImage"
                                :width="60"
                                :height="60"
                                fit="contain"
                                :preview-src-list="[row.goodsImage]"
                                preview-teleported
                            />
                            <span class="ml-2">{{ row.goodsName || '-' }}</span>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="商品价格" min-width="100">
                    <template #default="{ row }">¥{{ row.goodsMinPrice }}</template>
                </el-table-column>
                <el-table-column label="升级等级" prop="levelName" min-width="120" />
                <el-table-column label="套餐类型" min-width="100">
                    <template #default="{ row }">
                        <el-tag :type="row.isRenew ? 'warning' : 'success'">
                            {{ row.isRenew ? '续费包' : '首购' }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column label="排序" prop="sort" min-width="80" />
                <el-table-column label="状态" min-width="100">
                    <template #default="{ row }">
                        <el-tag :type="row.status ? 'success' : 'info'">
                            {{ row.status ? '上架' : '下架' }}
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
import { identityPackageList, identityPackageDel } from '@/api/identity/package'
import { usePaging } from '@/hooks/usePaging'
import feedback from '@/utils/feedback'
import EditPopup from './edit.vue'

const editRef = shallowRef<InstanceType<typeof EditPopup>>()
const showEdit = ref(false)

const { pager, getLists, resetPage } = usePaging({
    fetchFun: identityPackageList
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
        await feedback.confirm('确定要删除该套餐吗？')
        await identityPackageDel({ id })
        getLists()
    } catch (error) {
        console.log('删除套餐失败', error)
    }
}

getLists()
</script>
