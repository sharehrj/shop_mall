<template>
    <div>
        <el-card class="!border-none" shadow="never">
            <el-page-header :content="$route.meta.title" @back="$router.back()" />
        </el-card>
        <el-form ref="ruleFormRef" :model="formData" :rules="rules" label-width="120px">
            <el-card class="!border-none mt-4" shadow="never">
                <div class="font-bold text-xl">等级信息</div>
                <div class="mt-4">
                    <el-form-item label="等级名称" prop="name">
                        <el-input
                            class="w-[400px]"
                            v-model="formData.name"
                            placeholder="请输入等级名称"
                            :disabled="type == 'detail'"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="等级级别" prop="weights">
                        <div>
                            <el-input
                                class="w-[400px]"
                                v-model="formData.weights"
                                placeholder="请输入等级级别"
                                :disabled="type == 'detail'"
                            ></el-input>
                            <div class="form-tips">
                                级别数字越大表示级别越高，等级级别不能相同，填写大于1的整数
                            </div>
                        </div>
                    </el-form-item>
                    <el-form-item label="等级图标" prop="icon">
                        <div>
                            <MaterialPicker
                                v-model="formData.icon"
                                :disabled="type == 'detail'"
                            ></MaterialPicker>
                            <div class="form-tips">
                                建议尺寸：100*100像素，jpg，jpeg，png图片类型
                            </div>
                        </div>
                    </el-form-item>
                    <el-form-item label="等级背景图" prop="image">
                        <div>
                            <MaterialPicker
                                v-model="formData.image"
                                :disabled="type == 'detail'"
                            ></MaterialPicker>
                            <div class="form-tips">
                                建议尺寸：800*500像素，jpg，jpeg，png图片类型
                            </div>
                        </div>
                    </el-form-item>
                    <el-form-item label="等级标识码">
                        <el-input
                            class="w-[400px]"
                            v-model="formData.levelCode"
                            placeholder="如 VIP1、VIP2"
                            :disabled="type == 'detail'"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="等级说明" prop="description">
                        <el-input
                            class="w-[400px]"
                            v-model="formData.remark"
                            placeholder="请输入内容"
                            type="textarea"
                            :disabled="type == 'detail'"
                        ></el-input>
                    </el-form-item>
                </div>
            </el-card>
            <el-card class="!border-none mt-4" shadow="never">
                <div class="font-bold text-xl">分销佣金</div>
                <div class="mt-4">
                    <el-form-item label="自购佣金比例" prop="selfRatio">
                        <el-input
                            class="w-[400px]"
                            v-model="formData.selfRatio"
                            placeholder="请输入自购佣金比例"
                            :disabled="type == 'detail'"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="一级佣金比例" prop="firstRatio">
                        <el-input
                            class="w-[400px]"
                            v-model="formData.firstRatio"
                            placeholder="请输入一级佣金比例"
                            :disabled="type == 'detail'"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="二级佣金比例" prop="secondRatio">
                        <div>
                            <el-input
                                class="w-[400px]"
                                v-model="formData.secondRatio"
                                placeholder="请输入二级佣金比例"
                                :disabled="type == 'detail'"
                            ></el-input>
                            <div class="form-tips">
                                佣金支持小数点后2位，等级佣金总和不能超过100%
                            </div>
                        </div>
                    </el-form-item>
                </div>
            </el-card>
            <el-card class="!border-none mt-4" shadow="never">
                <div class="font-bold text-xl">身份佣金</div>
                <div class="mt-4">
                    <el-form-item label="直推佣金比例">
                        <div>
                            <el-input
                                class="w-[400px]"
                                v-model="formData.directRatio"
                                placeholder="请输入直推佣金比例"
                                :disabled="type == 'detail'"
                            ></el-input>
                            <div class="form-tips">直推：用户直接推荐人购买时获得的佣金比例</div>
                        </div>
                    </el-form-item>
                    <el-form-item label="复购佣金比例">
                        <div>
                            <el-input
                                class="w-[400px]"
                                v-model="formData.repurchaseRatio"
                                placeholder="请输入复购佣金比例"
                                :disabled="type == 'detail'"
                            ></el-input>
                            <div class="form-tips">复购：推荐人再次购买时获得的佣金比例</div>
                        </div>
                    </el-form-item>
                    <el-form-item label="价差佣金比例">
                        <div>
                            <el-input
                                class="w-[400px]"
                                v-model="formData.priceDiffRatio"
                                placeholder="请输入价差佣金比例"
                                :disabled="type == 'detail'"
                            ></el-input>
                            <div class="form-tips">价差：上下级等级差异产生的补差佣金比例</div>
                        </div>
                    </el-form-item>
                    <el-form-item label="培育佣金比例">
                        <div>
                            <el-input
                                class="w-[400px]"
                                v-model="formData.cultivateRatio"
                                placeholder="请输入培育佣金比例"
                                :disabled="type == 'detail'"
                            ></el-input>
                            <div class="form-tips">培育：培育下级成长获得的佣金比例</div>
                        </div>
                    </el-form-item>
                </div>
            </el-card>
            <el-card class="!border-none mt-4" shadow="never" v-if="formData.name != '默认等级'">
                <div class="font-bold text-xl">等级信息</div>
                <div class="mt-4">
                    <el-form-item label="自购佣金比例">
                        <el-radio-group v-model="formData.updateType" :disabled="type == 'detail'">
                            <el-radio :label="1">满足以下任何条件</el-radio>
                            <el-radio :label="2">满足以下全部条件</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item>
                        <div class="flex flex-col">
                            <el-checkbox
                                v-model="checkboxList.singleConsumptionAmount"
                                :true-label="1"
                                :false-label="0"
                                @change="
                                    checkboxChange(
                                        'singleConsumptionAmount',
                                        checkboxList.singleConsumptionAmount
                                    )
                                "
                                :disabled="type == 'detail'"
                            >
                                <template #default>
                                    <div class="flex items-center">
                                        <div class="w-[100px]">单笔消费金额</div>
                                        <el-input
                                            v-model="formData.singleConsumptionAmount"
                                            class="w-[400px] ml-2"
                                            :disabled="
                                                !checkboxList.singleConsumptionAmount ||
                                                type == 'detail'
                                            "
                                        ></el-input>
                                        <div class="ml-2">元</div>
                                    </div>
                                </template>
                            </el-checkbox>
                            <el-checkbox
                                class="mt-2"
                                v-model="checkboxList.cumulativeConsumptionAmount"
                                :true-label="1"
                                :false-label="0"
                                @change="
                                    checkboxChange(
                                        'cumulativeConsumptionAmount',
                                        checkboxList.cumulativeConsumptionAmount
                                    )
                                "
                                :disabled="type == 'detail'"
                            >
                                <template #default>
                                    <div class="flex items-center">
                                        <div class="w-[100px]">累计消费金额</div>
                                        <el-input
                                            v-model="formData.cumulativeConsumptionAmount"
                                            class="w-[400px] ml-2"
                                            :disabled="
                                                !checkboxList.cumulativeConsumptionAmount ||
                                                type == 'detail'
                                            "
                                        ></el-input>
                                        <div class="ml-2">元</div>
                                    </div>
                                </template>
                            </el-checkbox>
                            <el-checkbox
                                class="mt-2"
                                v-model="checkboxList.cumulativeConsumptionTimes"
                                :true-label="1"
                                :false-label="0"
                                @change="
                                    checkboxChange(
                                        'cumulativeConsumptionTimes',
                                        checkboxList.cumulativeConsumptionTimes
                                    )
                                "
                                :disabled="type == 'detail'"
                            >
                                <template #default>
                                    <div class="flex items-center">
                                        <div class="w-[100px]">累计消费次数</div>
                                        <el-input
                                            v-model="formData.cumulativeConsumptionTimes"
                                            class="w-[400px] ml-2"
                                            :disabled="!checkboxList.cumulativeConsumptionTimes"
                                        ></el-input>
                                        <div class="ml-2">次</div>
                                    </div>
                                </template>
                            </el-checkbox>
                            <el-checkbox
                                class="mt-2"
                                v-model="checkboxList.returnedCommission"
                                :true-label="1"
                                :false-label="0"
                                @change="
                                    checkboxChange(
                                        'returnedCommission',
                                        checkboxList.returnedCommission
                                    )
                                "
                                :disabled="type == 'detail'"
                            >
                                <template #default>
                                    <div class="flex items-center">
                                        <div class="w-[100px]">已结算佣金收入</div>
                                        <el-input
                                            v-model="formData.returnedCommission"
                                            class="w-[400px] ml-2"
                                            :disabled="!checkboxList.returnedCommission"
                                        ></el-input>
                                        <div class="ml-2">元</div>
                                    </div>
                                </template>
                            </el-checkbox>
                        </div>
                    </el-form-item>
                </div>
            </el-card>
        </el-form>
        <FooterBtns>
            <el-button v-if="type != 'detail'" type="primary" @click="submit(ruleFormRef)"
                >保存</el-button
            >
        </FooterBtns>
    </div>
</template>

<script setup lang="ts">
import { levelAdd, levelDetail, levelEdit } from '@/api/distribution/level'
import router from '@/router'
import feedback from '@/utils/feedback'
import type { FormInstance, FormRules } from 'element-plus'

//表单接口
interface IFormData {
    name: string
    levelCode: string
    weights: string
    icon: string
    image: string
    remark: string
    selfRatio: string
    firstRatio: string
    secondRatio: string
    directRatio: string
    repurchaseRatio: string
    priceDiffRatio: string
    cultivateRatio: string
    updateType: string | number
    order_one_money_is: string
    singleConsumptionAmount: string
    order_all_money_is: string
    cumulativeConsumptionAmount: string
    order_all_num_is: string
    cumulativeConsumptionTimes: string
    commission_is: string
    returnedCommission: string
}

const route = useRoute()
const id = route.query.id
const type = route.query.type
//表单ref
const ruleFormRef = ref<FormInstance>()

//表单数据
const formData = ref<IFormData>({
    name: '',
    levelCode: '',
    weights: '',
    remark: '',
    icon: '',
    image: '',
    selfRatio: '',
    firstRatio: '',
    secondRatio: '',
    directRatio: '',
    repurchaseRatio: '',
    priceDiffRatio: '',
    cultivateRatio: '',
    updateType: 1,
    order_one_money_is: '',
    singleConsumptionAmount: '',
    order_all_money_is: '',
    cumulativeConsumptionAmount: '',
    order_all_num_is: '',
    cumulativeConsumptionTimes: '',
    commission_is: '',
    returnedCommission: ''
})
//选择框列表
const checkboxList = ref({
    singleConsumptionAmount: 1,
    cumulativeConsumptionAmount: 0,
    cumulativeConsumptionTimes: 0,
    returnedCommission: 0
})

//表单校验规则
const rules = reactive<FormRules>({
    name: [{ required: true, message: '请输入等级名称', trigger: 'blur' }],
    weights: [{ required: true, message: '请输入等级级别', trigger: 'blur' }],
    icon: [{ required: true, message: '请选择背景图标', trigger: 'blur' }],
    selfRatio: [{ required: true, message: '请输入自购佣金比例', trigger: 'blur' }],
    firstRatio: [{ required: true, message: '请输入一级佣金比例', trigger: 'blur' }],
    secondRatio: [{ required: true, message: '请输入二级佣金比例', trigger: 'blur' }],
    image: [{ required: true, message: '请输入选择背景图', trigger: 'blur' }]
})

//单选框change
const checkboxChange = (key: string, cvalue: any) => {
    if (cvalue == 0) {
        //@ts-ignore
        formData.value[key] = ''
    }
}

//提交
const submit = async (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate(async (valid, fields) => {
        try {
            if (valid) {
                if (id) {
                    await levelEdit({ ...formData.value, id })
                } else {
                    await levelAdd({ ...formData.value })
                }
            } else {
                return
            }
            feedback.msgSuccess('新增成功！')
            router.back()
        } catch (error) {
            return
        }
    })
}

const getDetail = async () => {
    // formData.value = await getLevelDetail({ id })
    const res = await levelDetail({ id })
    //@ts-ignore
    Object.keys(formData.value).map((item) => {
        //@ts-ignore
        formData.value[item] = res[item]
    })
    return
}

//初始化多选框
const getCheckboxSelect = () => {
    Object.keys(checkboxList.value).map((item: any) => {
        //@ts-ignore
        if (formData.value[item]) {
            //@ts-ignore
            checkboxList.value[item] = 1
        }
    })
}

onMounted(async () => {
    if (id) {
        await getDetail()
        await getCheckboxSelect()
    }
})
</script>

<style scoped lang="scss"></style>
