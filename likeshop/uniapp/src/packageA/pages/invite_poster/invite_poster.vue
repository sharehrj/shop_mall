<template>
    <page-meta :page-style="$theme.pageStyle">
        <!-- #ifndef H5 -->
        <navigation-bar :front-color="$theme.navColor" :background-color="$theme.navBgColor" />
        <!-- #endif -->
    </page-meta>
    
    <!-- 小程序版本 -->
    <invite-poster-mnp v-if="isMiniProgram" />
    <!-- H5版本 -->
    <invite-poster-h5 v-else />
</template>

<script setup lang="ts">
/**
 * @description 邀请海报页面 - 根据客户端类型动态渲染
 * @author damonyuan
 * @date 2024-12-19
 */
import { computed } from 'vue'
import { getClient } from '@/utils/client'
import { ClientEnum } from '@/enums/appEnums'
import { useThemeStore } from '@/stores/theme'
import { useAppStore } from '@/stores/app'
import InvitePosterMnp from './invite_poster_mnp/invite_poster.vue'
import InvitePosterH5 from './invite_poster_h5/invite_poster.vue'

// 获取当前客户端类型
const currentClient = getClient()

// 获取主题 store
const themeStore = useThemeStore()
const appStore = useAppStore()

// 判断是否为小程序环境
const isMiniProgram = computed(() => {
    return currentClient === ClientEnum.MP_WEIXIN
})

// 主题配置 - 替代 mixin 中的 $theme
const $theme = computed(() => {
    return {
        primaryColor: themeStore.primaryColor,
        pageStyle: themeStore.vars,
        navColor: themeStore.navColor,
        navBgColor: themeStore.navBgColor
    }
})
</script>

<style scoped lang="scss"></style>