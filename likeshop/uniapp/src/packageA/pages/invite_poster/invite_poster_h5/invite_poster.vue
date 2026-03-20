<template>
    <page-meta :page-style="$theme.pageStyle">
        <!-- #ifndef H5 -->
        <navigation-bar :front-color="$theme.navColor" :background-color="$theme.navBgColor" />
        <!-- #endif -->
    </page-meta>

    <view class="flex flex-col items-center">
        <!-- <image :src="imgPath" mode="widthFix" class="mt-[20rpx]"></image> -->
        <invite-poster ref="poster" :config="config" @success="handleSuccess" />
        <view class="mt-[40rpx] px-[60rpx] w-full">
            <!-- <view class="m-b-40">
                <view class="m-b-10 sm lighter">我的邀请码</view>
                <view class="flex row-between">
                    <view class="font-size-44">{{ '123' }}</view>
                    <view class="sm m-r-30 copy-btn">点击复制</view>
                </view>
            </view> -->
            <!-- #ifndef H5  -->
            <u-button type="primary" shape="circle" @click="saveImg">保存到相册</u-button>
            <!-- #endif -->
            <!-- #ifdef H5 -->
            <u-button type="primary" shape="circle" @click="saveImg">长按保存到相册</u-button>
            <!-- #endif -->
        </view>
    </view>

    <!-- 页面状态 -->
    <PageStatus ref="pageRef"></PageStatus>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref, shallowRef, unref, watch } from 'vue'
import invitePoster from './components/invite-poster.vue'
import { miniAppCode } from '@/api/app'
import { useUserStore } from '@/stores/user'
import { useAppStore } from '@/stores/app'
import PageStatus from '@/components/page-status/page-status.vue'
import EmptyErrorImage from '@/packageA/static/empty/error.png'

const userStore = useUserStore()

const pageRef = shallowRef()
//海报生成器ref
const poster = shallowRef()
//海报路径
const imgPath: any = ref('')
//海报配置
const config = reactive({
    url: '',
    userData: userStore.userInfo,
    slogan: '邀请你一起来赚大钱！！'
})

const handleSuccess = (val: any) => {
    imgPath.value = val
    pageRef?.value?.close()
}

//获取海报路径
const getPostUrl = async () => {
    // 检查用户信息是否完整
    if (!userStore.userInfo?.code) {
        console.log('用户邀请码不存在，等待用户信息加载')
        return
    }

    try {
        uni.showLoading({
            title: '正在生成中'
        })
        //#ifndef H5
        const { image } = await miniAppCode({
            // 承载商品拓展类型(t = goodsType, aid = activityId
            scene: `share_code=${userStore.userInfo.code}`,
            page: 'pages/index/index'
        })
        config.url = image
        //#endif
        //#ifdef H5
        const domain = useAppStore().config.domain
        config.url = `${domain}mobile/pages/index/index?share_code=${userStore.userInfo.code}`
        //#endif
    } catch (error) {
        uni.hideLoading()
        unref(pageRef)?.show({
            text: error,
            src: EmptyErrorImage
        })
        console.log('海报生产失败', error)
    }
}

//保存图片
const saveImg = () => {
    // #ifndef H5
    uni.saveImageToPhotosAlbum({
        filePath: imgPath.value,
        success: (res) => {
            uni.$u.toast('保存成功')
        },
        fail: (err) => {
            uni.$u.toast('保存失败')
            console.log(err)
        }
    })
    // #endif
    // #ifdef H5
    uni.$u.toast('请长按图片保存')
    // #endif
}

// 监听用户信息变化，当用户邀请码存在时才执行海报生成逻辑
watch(
    () => userStore.userInfo?.code,
    (newCode) => {
        if (newCode) {
            console.log('用户邀请码已获取，开始生成海报', newCode)
            // 更新配置中的用户数据
            config.userData = userStore.userInfo
            // 获取海报URL
            getPostUrl()
        }
    },
    { immediate: true }
)

// 页面挂载时不再直接调用，改为依赖 watch 监听
onMounted(async () => {
    await nextTick()
    // 如果用户信息已经存在，直接触发
    if (userStore.userInfo?.code) {
        await getPostUrl()
    }
})
</script>

<style scoped lang="scss"></style>
