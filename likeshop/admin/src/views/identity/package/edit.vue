<template>
    <div class="edit-popup">
        <popup
            ref="popupRef"
            :title="popupTitle"
            :async="true"
            width="580px"
            @confirm="handleSubmit"
            @close="handleClose"
        >
            <el-form ref="formRef" :model="formData" label-width="90px" :rules="formRules">
                <el-form-item label="套餐商品" prop="goodsId">
                    <div class="w-full">
                        <el-select
                            v-model="formData.goodsId"
                            filterable
                            remote
                            reserve-keyword
                            placeholder="请输入商品名称搜索"
                            :remote-method="searchGoods"
                            :loading="goodsLoading"
                            class="w-[360px]"
                        >
                            <el-option
                                v-for="item in goodsList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id"
                            >
                                <div class="flex items-center">
                                    <el-image
                                        class="w-[32px] h-[32px] mr-2 flex-none"
                                        :src="item.image"
                                        fit="cover"
                                    />
                                    <div>
                                        <div class="text-sm">{{ item.name }}</div>
                                        <div class="text-xs text-gray-400">¥{{ item.minPrice }}</div>
                                    </div>
                                </div>
                            </el-option>
                        </el-select>
                    </div>
                </el-form-item>
                <el-form-item label="升级等级" prop="levelId">
                    <el-select v-model="formData.levelId" placeholder="请选择升级等级" class="w-[220px]">
                        <el-option
                            v-for="item in levelList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="套餐类型" prop="isRenew">
                    <el-radio-group v-model="formData.isRenew">
                        <el-radio :label="0">首购套餐</el-radio>
                        <el-radio :label="1">续费套餐</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="状态">
                    <el-switch
                        v-model="formData.status"
                        :active-value="1"
                        :inactive-value="0"
                        active-text="上架"
                        inactive-text="下架"
                    />
                </el-form-item>
                <el-form-item label="排序">
                    <el-input-number v-model="formData.sort" :min="0" class="w-[160px]" />
                    <span class="ml-2 text-gray-400 text-sm">数值越小越靠前</span>
                </el-form-item>
            </el-form>
        </popup>
    </div>
</template>
<script lang="ts" setup>
import type { FormInstance, FormRules } from 'element-plus'
import { identityPackageAdd, identityPackageEdit } from '@/api/identity/package'
import { levelList as getLevelSelectList } from '@/api/distribution/level'
import { goodsLists } from '@/api/goods/goods'
import Popup from '@/components/popup/index.vue'

const emit = defineEmits(['success', 'close'])
const formRef = shallowRef<FormInstance>()
const popupRef = shallowRef<InstanceType<typeof Popup>>()

const mode = ref('add')
const levelList = ref<any[]>([])
const goodsList = ref<any[]>([])
const goodsLoading = ref(false)

const formData = reactive({
    id: undefined as number | undefined,
    goodsId: undefined as number | undefined,
    levelId: undefined as number | undefined,
    isRenew: 0,
    status: 1,
    sort: 0
})

const formRules = reactive<FormRules>({
    goodsId: [{ required: true, message: '请选择套餐商品', trigger: 'change' }],
    levelId: [{ required: true, message: '请选择升级等级', trigger: 'change' }],
    isRenew: [{ required: true, message: '请选择套餐类型', trigger: 'change' }]
})

const popupTitle = computed(() => (mode.value === 'edit' ? '编辑套餐' : '新增套餐'))

const searchGoods = async (query: string) => {
    if (!query) return
    goodsLoading.value = true
    try {
        const data = await goodsLists({ pageNo: 1, pageSize: 20, name: query } as any)
        goodsList.value = data?.lists || []
    } catch (error) {
        console.log('搜索商品失败', error)
    } finally {
        goodsLoading.value = false
    }
}

const fetchLevelList = async () => {
    try {
        const data = await getLevelSelectList({ pageNo: 1, pageSize: 50 })
        levelList.value = data?.lists || []
    } catch (error) {
        console.log('获取等级列表失败', error)
    }
}

const open = (type = 'add') => {
    mode.value = type
    popupRef.value?.open()
    fetchLevelList()
}

const setFormData = (row: any) => {
    formData.id = row.id
    formData.goodsId = row.goodsId
    formData.levelId = row.levelId
    formData.isRenew = row.isRenew
    formData.status = row.status
    formData.sort = row.sort
    // 编辑时把当前商品加入列表供展示
    if (row.goodsId && row.goodsName) {
        goodsList.value = [{ id: row.goodsId, name: row.goodsName, image: row.goodsImage, minPrice: row.goodsMinPrice }]
    }
}

const handleSubmit = async () => {
    await formRef.value?.validate()
    if (mode.value === 'edit') {
        await identityPackageEdit(formData)
    } else {
        await identityPackageAdd(formData)
    }
    popupRef.value?.close()
    emit('success')
}

const handleClose = () => {
    emit('close')
}

defineExpose({ open, setFormData })
</script>
