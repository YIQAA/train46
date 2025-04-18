<template>
  <a-layout-header class="header">
    <div class="logo">
      <router-link to="/welcome" style="color: white; font-size: 18px">IKUN12306</router-link>
    </div>
    <div style="float: right; color: white;">
      您好，{{userName}}
      <router-link to="/login" style="color: white;">退出登录</router-link>
    </div>
    <a-menu
        v-model:selectedKeys="selectedKeys"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/welcome">
        <router-link to="/welcome">
          <coffee-outlined></coffee-outlined>&nbsp; 欢迎
        </router-link>
      </a-menu-item>
      <a-menu-item key="/userDetail">
        <router-link to="/userDetail">
          <user-outlined/> &nbsp; 个人中心
        </router-link>
      </a-menu-item>
      <a-menu-item key="/ticketSearch">
        <router-link to="/ticketSearch">
          <border-outer-outlined/> &nbsp; 余票查询
        </router-link>
      </a-menu-item>
      <a-menu-item key="/orderList">
        <router-link to="/orderList">
          <idcard-outlined/> &nbsp; 我的车票
        </router-link>
      </a-menu-item>
      <a-menu-item key="/chatWindow">
        <router-link to="/chatWindow">
          <CommentOutlined /> 智能客服
        </router-link>
      </a-menu-item>
      <a-menu-item key="/adminPage" v-if="userRole === 'admin'">
        <router-link to="/adminPage">
          <desktop-outlined/> &nbsp; 关于控台管理
        </router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-header>
</template>


<script setup>
  import { ref, watch, computed } from 'vue';
  import store from "@/store";
  import router from "@/router";

  // 响应式状态
  const member = computed(() => store.state.member);
  const userName = localStorage.getItem('realName');
  const userRole = localStorage.getItem('userRole');
  const selectedKeys = ref([]);

  // 路由监听逻辑
  watch(
  () => router.currentRoute.value.path,
  (newValue) => {
  console.log('watch', newValue);
  selectedKeys.value = [newValue];
},
  { immediate: true }
  );
</script>

<style scoped>
.logo {
  float: left;
  height: 31px;
  width: 150px;
  color: white;
  font-size: 20px;
}
</style>
