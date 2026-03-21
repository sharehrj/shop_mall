<template>
    <div class="video-player-wrapper" :style="{ width: width, height: height }">
        <video
            ref="playerRef"
            :src="src"
            :poster="poster"
            :muted="muted"
            :autoplay="autoPlay"
            :loop="loop"
            controls
            preload="auto"
            :style="{ width: '100%', height: '100%' }"
            @play="onPlay"
            @pause="onPause"
            @timeupdate="onTimeupdate"
            @canplay="onCanplay"
        />
    </div>
</template>

<script setup lang="ts">
import { shallowRef } from 'vue'

const props = defineProps({
    src: {
        type: String,
        required: true
    },
    width: {
        type: String,
        default: '100%'
    },
    height: {
        type: String,
        default: 'auto'
    },
    poster: String,
    muted: {
        type: Boolean,
        default: false
    },
    autoPlay: {
        type: Boolean,
        default: false
    },
    loop: {
        type: Boolean,
        default: false
    }
})

const playerRef = shallowRef<HTMLVideoElement>()

const play = () => {
    playerRef.value?.play()
}

const pause = () => {
    playerRef.value?.pause()
}

const onPlay = (event: Event) => {
    console.log(event, '播放')
}
const onPause = (event: Event) => {
    console.log(event, '暂停')
}
const onTimeupdate = (event: Event) => {
    console.log(event, '时间更新')
}
const onCanplay = (event: Event) => {
    console.log(event, '可以播放')
}

defineExpose({
    play,
    pause
})
</script>
