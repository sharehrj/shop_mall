<template>
    <view class="flex flex-col items-center">
        <!-- Canvas生成的海报图片 -->
        <view class="poster-container" v-if="!loading && posterImagePath">
            <image 
                :src="posterImagePath" 
                mode="widthFix" 
                style="width: 600rpx; border-radius: 20rpx;"
            />
        </view>

        <!-- 加载状态 -->
        <view
            v-if="loading"
            class="flex flex-col items-center justify-center"
            style="height: 400rpx"
        >
            <view class="loading-spinner mb-[20rpx]"></view>
            <text class="text-gray-500">正在生成海报...</text>
        </view>

        <!-- 使用Template方式的Canvas画板 (隐藏) -->
        <l-painter
            ref="painter"
            isCanvasToTempFilePath
            @success="onPosterSuccess"
            @fail="onPosterFail"
            @progress="onProgress"
            hidden
            css="width: 600rpx; height: 930rpx; background: #ffffff;"
        >
            <!-- 背景图片 -->
            <l-painter-image
                :src="backgroundImage"
                css="width: 600rpx; height: 930rpx; object-fit: cover; border-radius: 20rpx;"
            />
            
            <!-- 底部白色背景区域 -->
            <l-painter-view
                css="position: absolute; bottom: 0; left: 0; width: 600rpx; height: 290rpx; background: rgba(255, 255, 255, 0.9); border-radius: 20rpx;"
            >
                <!-- 用户头像 -->
                <l-painter-image
                    :src="config.userData?.avatar || defaultAvatar"
                    css="position: absolute; top: 30rpx; left: 30rpx; width: 80rpx; height: 80rpx; border-radius: 50%; object-fit: cover;"
                />
                
                <!-- 用户昵称 -->
                <l-painter-text
                    :text="config.userData?.nickname || '用户'"
                    css="position: absolute; top: 50rpx; left: 130rpx; font-size: 34rpx; font-weight: bold; color: #333333; max-width: 220rpx;"
                />
                
                <!-- 邀请文案 -->
                <l-painter-text
                    :text="config.slogan || '邀请你一起来赚大钱！！'"
                    css="position: absolute; top: 140rpx; left: 30rpx; font-size: 28rpx; color: #333333;"
                />
                
                <!-- 邀请码 -->
                <l-painter-text
                    :text="`邀请码：${config.userData?.code || '123456'}`"
                    css="position: absolute; top: 200rpx; left: 30rpx; font-size: 28rpx; color: #ff2c3c;"
                />
                
                <!-- 二维码 -->
                <l-painter-qrcode
                    :text="config.url || 'https://example.com'"
                    css="position: absolute; top: 56rpx; right: 30rpx; width: 168rpx; height: 168rpx; background: #ffffff;"
                />
            </l-painter-view>
        </l-painter>
    </view>
</template>

<script setup lang="ts">
import { nextTick, ref, watch } from 'vue'
import bg from '@/packageA/static/distribution/invitePost.png'

const props = defineProps({
    config: {
        type: Object,
        default: () => ({})
    }
})

const emit = defineEmits(['success', 'fail'])

// 状态管理
const loading = ref(false)
const posterImagePath = ref('')
const progress = ref(0)

// 图片资源
const backgroundImage = ref(bg)
const defaultAvatar = ref('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iODAiIGhlaWdodD0iODAiIHZpZXdCb3g9IjAgMCA4MCA4MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPGNpcmNsZSBjeD0iNDAiIGN5PSI0MCIgcj0iNDAiIGZpbGw9IiNFNUU1RTUiLz4KPGNpcmNsZSBjeD0iNDAiIGN5PSIzMiIgcj0iMTIiIGZpbGw9IiM5OTk5OTkiLz4KPHBhdGggZD0iTTIwIDYwQzIwIDUyLjI2ODQgMjYuMjY4NCA0NiAzNCA0Nkg0NkM1My43MzE2IDQ2IDYwIDUyLjI2ODQgNjAgNjBWNjBIMjBWNjBaIiBmaWxsPSIjOTk5OTk5Ii8+Cjwvc3ZnPgo=')

// 获取painter引用
const painter = ref(null)

// 生成海报
const drawCanvas = async () => {
    try {
        loading.value = true
        posterImagePath.value = ''
        await nextTick()
        
        console.log('开始生成海报，配置:', props.config)
        
        // 等待一下让组件完全渲染
        setTimeout(() => {
            if (painter.value) {
                // 手动触发渲染
                painter.value.$forceUpdate()
            }
        }, 100)
        
    } catch (error) {
        loading.value = false
        uni.hideLoading()
        emit('fail', error)
        console.error('海报生成失败', error)
    }
}

// 海报生成成功
const onPosterSuccess = (imagePath: string) => {
    console.log('海报生成成功:', imagePath)
    loading.value = false
    posterImagePath.value = imagePath
    uni.hideLoading()
    emit('success', imagePath)
}

// 海报生成失败
const onPosterFail = (error: any) => {
    console.error('海报生成失败:', error)
    loading.value = false
    uni.hideLoading()
    emit('fail', error)
}

// 进度更新
const onProgress = (progressValue: number) => {
    console.log('生成进度:', progressValue)
    progress.value = progressValue
}

// 监听配置变化，自动生成海报
watch(
    () => props.config,
    (newConfig) => {
        if (newConfig && newConfig.url) {
            console.log('配置更新，开始生成海报', newConfig)
            // 延迟一下确保数据绑定完成
            setTimeout(() => {
                drawCanvas()
            }, 200)
        }
    },
    { deep: true, immediate: true }
)

// 暴露方法给父组件
defineExpose({ drawCanvas })
</script>

<style scoped lang="scss">
.poster-container {
    // 海报容器样式
}

.loading-spinner {
    width: 40rpx;
    height: 40rpx;
    border: 4rpx solid #f3f3f3;
    border-top: 4rpx solid #007aff;
    border-radius: 50%;
    animation: spin 1s linear infinite;
}

@keyframes spin {
    0% {
        transform: rotate(0deg);
    }
    100% {
        transform: rotate(360deg);
    }
}
</style>