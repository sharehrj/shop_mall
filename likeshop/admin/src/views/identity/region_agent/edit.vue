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
                <el-form-item v-if="mode === 'add'" label="代理用户" prop="userId">
                    <el-select
                        v-model="formData.userId"
                        filterable
                        remote
                        reserve-keyword
                        placeholder="请输入用户昵称/手机号搜索"
                        :remote-method="searchUsers"
                        :loading="userLoading"
                        class="w-[360px]"
                    >
                        <el-option
                            v-for="item in userList"
                            :key="item.id"
                            :label="`${item.nickname}（${item.mobile || item.username}）`"
                            :value="item.id"
                        >
                            <div class="flex items-center">
                                <el-avatar :size="28" :src="item.avatar" class="mr-2 flex-none" />
                                <div>
                                    <span>{{ item.nickname }}</span>
                                    <span class="text-xs text-gray-400 ml-2">{{ item.mobile }}</span>
                                </div>
                            </div>
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item v-else label="代理用户">
                    <span class="text-gray-700">{{ selectedUserName }}</span>
                </el-form-item>
                <el-form-item label="区域级别" prop="regionLevel">
                    <el-select
                        v-model="formData.regionLevel"
                        placeholder="请选择区域级别"
                        class="w-[160px]"
                        @change="onRegionLevelChange"
                    >
                        <el-option label="省" :value="1" />
                        <el-option label="市" :value="2" />
                        <el-option label="区县" :value="3" />
                    </el-select>
                </el-form-item>
                <el-form-item label="所属区域" prop="regionId">
                    <el-select
                        v-model="formData.regionId"
                        filterable
                        remote
                        reserve-keyword
                        placeholder="请输入区域名称搜索"
                        :remote-method="searchRegions"
                        :loading="regionLoading"
                        class="w-[360px]"
                    >
                        <el-option
                            v-for="item in regionList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
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
                    <span class="ml-2 text-gray-500">%</span>
                </el-form-item>
                <el-form-item label="到期时间">
                    <el-date-picker
                        v-model="expireDateStr"
                        type="datetime"
                        placeholder="不填为永久有效"
                        value-format="x"
                        class="w-[240px]"
                        @change="onExpireChange"
                    />
                    <span class="ml-2 text-gray-400 text-sm">不填或清空表示永久有效</span>
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
import { getUserList } from '@/api/consumer'
import Popup from '@/components/popup/index.vue'
import request from '@/utils/request'

const emit = defineEmits(['success', 'close'])
const formRef = shallowRef<FormInstance>()
const popupRef = shallowRef<InstanceType<typeof Popup>>()

const mode = ref('add')
const expireDateStr = ref<any>(null)
const userList = ref<any[]>([])
const regionList = ref<any[]>([])
const userLoading = ref(false)
const regionLoading = ref(false)
const selectedUserName = ref('')

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
    userId: [{ required: true, message: '请选择代理用户', trigger: 'change' }],
    regionId: [{ required: true, message: '请选择所属区域', trigger: 'change' }],
    regionLevel: [{ required: true, message: '请选择区域级别', trigger: 'change' }],
    ratio: [{ required: true, message: '请输入分红比例', trigger: 'blur' }]
})

const popupTitle = computed(() => (mode.value === 'edit' ? '编辑区域代理' : '新增区域代理'))

const searchUsers = async (query: string) => {
    if (!query) return
    userLoading.value = true
    try {
        const data = await getUserList({ pageNo: 1, pageSize: 20, keyword: query })
        userList.value = data?.lists || []
    } catch (error) {
        console.log('搜索用户失败', error)
    } finally {
        userLoading.value = false
    }
}

const searchRegions = async (query: string) => {
    if (!query) return
    regionLoading.value = true
    try {
        const data: any = await request.get({ url: '/setting/map/region', params: { name: query, level: formData.regionLevel || 2, pageNo: 1, pageSize: 30 } })
        regionList.value = data?.lists || data || []
    } catch (error) {
        console.log('搜索区域失败', error)
    } finally {
        regionLoading.value = false
    }
}

const onRegionLevelChange = () => {
    formData.regionId = undefined
    regionList.value = []
}

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
    selectedUserName.value = `${row.userNickname || ''}（${row.userMobile || ''}）`
    if (row.regionId && row.regionName) {
        regionList.value = [{ id: row.regionId, name: row.regionName }]
    }
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
