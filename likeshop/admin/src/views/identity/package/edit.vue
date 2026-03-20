<template>
    <div class="edit-popup">
        <popup
            ref="popupRef"
            :title="popupTitle"
            :async="true"
            width="560px"
            @confirm="handleSubmit"
            @close="handleClose"
        >
            <el-form ref="formRef" :model="formData" label-width="90px" :rules="formRules">
                <el-form-item label="套餐商品" prop="goodsId">
                    <el-input-number
                        v-model="formData.goodsId"
                        :min="1"
                        placeholder="请输入商品ID"
                        class="w-[220px]"
                    />
                    <span class="ml-2 text-gray-400 text-sm">输入商品ID关联套餐商品</span>
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
import Popup from '@/components/popup/index.vue'

const emit = defineEmits(['success', 'close'])
const formRef = shallowRef<FormInstance>()
const popupRef = shallowRef<InstanceType<typeof Popup>>()

const mode = ref('add')
const levelList = ref<any[]>([])

const formData = reactive({
    id: undefined as number | undefined,
    goodsId: undefined as number | undefined,
    levelId: undefined as number | undefined,
    isRenew: 0,
    status: 1,
    sort: 0
})

const formRules = reactive<FormRules>({
    goodsId: [{ required: true, message: '请输入商品ID', trigger: 'blur' }],
    levelId: [{ required: true, message: '请选择升级等级', trigger: 'change' }],
    isRenew: [{ required: true, message: '请选择套餐类型', trigger: 'change' }]
})

const popupTitle = computed(() => (mode.value === 'edit' ? '编辑套餐' : '新增套餐'))

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
