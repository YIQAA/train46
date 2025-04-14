<template>
  <a-layout id="components-layout-demo-top-side-2" style="height: 100%">
    <header-view></header-view>
    <a-layout style="height: calc(100% - 64px)"> <!-- 假设 Header 高度为 64px -->
      <SiderView v-if="showSider" />

      <a-layout style="padding: 0 24px 24px; display: flex; alignItems: 'center';justify-content: center;" >
        <!-- 原有内容区域 -->
        <a-layout-content
            :style="{
            background: '#fff',
            padding: '24px',
            margin: 0,
            minHeight: '280px',
            width: '100%',
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center'
          }"
        >
          <!-- 添加包裹容器 -->
          <div class="content-wrapper">
            <router-view></router-view>
          </div>
        </a-layout-content>
      </a-layout>
    </a-layout>
  </a-layout>



</template>

<script setup>
import HeaderView from "@/components/Header.vue";
import SiderView from "@/components/Sider.vue";
import {ref, onMounted} from 'vue';



const showSider = ref(false);

onMounted(() => {
  const userRole = localStorage.getItem('userRole');
  if (userRole === 'admin') {
    showSider.value = true;
  }
});
</script>

<style>
/* 添加全局样式限制内容最大宽度 */
.content-wrapper {
  width: 100%;
  max-width: 1200px; /* 按实际需求调整 */

  marginLeft: '90px';
}
</style>