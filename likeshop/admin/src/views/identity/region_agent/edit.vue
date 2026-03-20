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
                <el-form-item v-if="mode === 'add'" label="用户ID" prop="userId">
                    <el-input-number
                        v-model="formData.userId"
                        :min="1"
                        placeholder="请输入用户ID"
                        class="w-[220px]"
                    />
                </el-form-item>
                <el-form-item label="区域ID" prop="regionId">
                    <el-input-number
                        v-model="formData.regionId"
                        :min="1"
                        placeholder="请输入区域ID"
                        class="w-[220px]"
                    />
                </el-form-item>
                <el-form-item label="区域级别" prop="regionLevel">
                    <el-select v-model="formData.regionLevel" placeholder="请选择区域级别" class="w-[220px]">
                        <el-option label="省" :value="1" />
                        <el-option label="市" :value="2" />
                        <el-option label="区县" :value="3" />
                    </el-select>
                </el-form-item>
                <el-form-item label="分红比例" prop="ratio">
                    <el-input-number
                        v-model="formData.ratio"
                        :min="0"
                        :max="100"
                        :precision="2"
                        class="w-[160px]"
                    />
                    <span class="ml-2 text-gray-400">%</span>
                </el-form-item>
                <el-form-item label="到期时间">
                    <el-date-picker
                        v-model="expireDateStr"
                        type="datetime"
                        placeholder="不填为永久有效"
                        value-format="x"
                        class="w-[220px]"
                        @change="onExpireChange"
                    />
                </el-form-item>
                <el-form-item label="状态">
                    <el-switch
                        v-model="formData.status"
                        :active-value="1"
                        :inactive-value="0"
                        active-text="启用"
                        inactive-text="禁用"
                    />
                </el-form-item>
            </el-form>
        </popup>
    </div>
</template>
<script lang="ts" setup>
import type { FormInstance, FormRules } from 'element-plus'
import { regionAgentAdd, regionAgentEdit } from '@/api/identity/region_agent'
import Popup from '@/components/popup/index.vue'

const emit = defineEmits(['success', 'close'])
const formRef = shallowRef<FormInstance>()
const popupRef = shallowRef<InstanceType<typeof Popup>>()

const mode = ref('add')
const expireDateStr = ref<any>(null)

const formData = reactive({
    id: undefined as number | undefined,
    userId: undefined as number | undefined,
    regionId: undefined as number | undefined,
    regionLevel: undefined as number | undefined,
    ratio: 0,
    expireTime: 0,
    status: 1
})

const formRules = reactive<FormRules>({
    userId: [{ required: true, message: '请输入用户ID', trigger: 'blur' }],
    regionId: [{ required: true, message: '请输入区域ID', trigger: 'blur' }],
    regionLevel: [{ required: true, message: '请选择区域级别', trigger: 'change' }],
    ratio: [{ required: true, message: '请输入分红比例', trigger: 'blur' }]
})

const popupTitle = computed(() => (mode.value === 'edit' ? '编辑区域代理' : '新增区域代理'))

const onExpireChange = (val: any) => {
    formData.expireTime = val ? Math.floor(Number(val) / 1000) : 0
}

const open = (type = 'add') => {
    mode.value = type
    popupRef.value?.open()
}

const setFormData = (row: any) => {
    formData.id = row.id
    formData.userId = row.userId
    formData.regionId = row.regionId
    formData.regionLevel = row.regionLevel
    formData.ratio = Number(row.ratio)
    formData.expireTime = row.expireTime || 0
    formData.status = row.status
}

const handleSubmit = async () => {
    await formRef.value?.validate()
    if (mode.value === 'edit') {
        await regionAgentEdit(formData)
    } else {
        await regionAgentAdd(formData)
    }
    popupRef.value?.close()
    emit('success')
}

const handleClose = () => {
    emit('close')
}

defineExpose({ open, setFormData })
</script>
