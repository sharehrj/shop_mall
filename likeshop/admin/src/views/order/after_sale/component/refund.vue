<template>
    <div class="edit-popup">
        <popup
            ref="popupRef"
            title="同意退款"
            :async="true"
            width="550px"
            @confirm="handleSubmit"
            @close="handleClose"
        >
            <el-alert
                title="您同意退款后，退款将自动原路退回至买家付款账户"
                type="warning"
                :closable="false"
            />
            <el-form>
                <el-form-item label="售后类型">
                    {{ formData.type_text }}
                </el-form-item>
                <el-form-item label="申请退款金额"> ¥{{ formData.refund_money }} </el-form-item>
                <el-form-item label="退款金额">
                    <el-input v-model="formData.refund_money" class="w-[140px]" disabled>
                        <template #append> 元 </template>
                    </el-input>
                </el-form-item>
            </el-form>
            
            <!-- 自定义底部按钮，替换默认的popup按钮 -->
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="handleClose">取消</el-button>
                    <el-button 
                        type="primary" 
                        :loading="submitLoading"
                        :disabled="submitLoading"
                        @click="handleSubmit"
                    >
                        {{ submitLoading ? '处理中...' : '确认退款' }}
                    </el-button>
                </div>
            </template>
        </popup>
    </div>
</template>
<script lang="ts" setup>
import { afterSaleConfirmRefund } from '@/api/order/after_sale'
import Popup from '@/components/popup/index.vue'

const emit = defineEmits(['success', 'close'])
const popupRef = shallowRef<InstanceType<typeof Popup>>()

// 添加提交loading状态
const submitLoading = ref(false)

type RefundType = {
    id: string | number
    type_text: string
    refund_money: string
    [index: number | string]: number | string
}

const formData = reactive<RefundType>({
    id: '',
    type_text: '',
    refund_money: ''
})

const handleSubmit = async () => {
    // 防止重复提交
    if (submitLoading.value) {
        return
    }
    try {
        submitLoading.value = true
        await afterSaleConfirmRefund({
            id: formData.id as number
        })
        popupRef.value?.close()
        emit('success')
    } catch (error) {
        // 处理错误，可以添加错误提示
        console.error('退款确认失败:', error)
    } finally {
        submitLoading.value = false
    }
}

const open = (data: RefundType) => {
    // 重置loading状态
    submitLoading.value = false
    
    Reflect.ownKeys(data).map((item: any) => {
        formData[item] = data[item]
    })
    popupRef.value?.open()
}

const handleClose = () => {
    // 重置loading状态
    submitLoading.value = false
    emit('close')
}

defineExpose({
    open
})
</script>
