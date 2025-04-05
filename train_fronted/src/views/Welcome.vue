<!-- src/views/Welcome.vue -->
<template>
  <div class="welcome-container">
    <h1>欢迎，{{ userName }}！</h1>
    <h2>token:{{ tokenData }}</h2>
    <!-- 其他欢迎页面内容 -->

<!--    按钮，跳转-->
  </div>
</template>

<script>

import {defineComponent} from 'vue';

export default defineComponent({
  name: "welcome-view",
  setup() {
    return {};
  },
});
</script>

<script setup>
import {ref, onMounted} from 'vue'
import {useRoute} from 'vue-router'

const userName = ref('访客')
const tokenData = ref('未登录')

// 获取登录时存储的用户信息
onMounted(() => {
  const userData = localStorage.getItem('realName')
  const token1 = localStorage.getItem('token')

  if (userData) {
    userName.value = userData || '用户';
    tokenData.value = token1 || '未登录';
  }
})
</script>

<style>
.welcome-container {
  text-align: center;
  padding: 2rem;
  font-size: 1.5rem;
}
</style>