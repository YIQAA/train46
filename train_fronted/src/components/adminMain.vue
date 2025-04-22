<template>
  <a-layout style="min-height: 100vh">
    <a-layout-sider
        :collapsible="true"
        v-model:collapsed="collapsed"
        @collapse="onCollapse"
        breakpoint="md"
    >
      <div class="logo">火车订票系统后台管理</div>
      <a-menu
          mode="inline"
          :default-selected-keys="[currentRouteKey]"
          :theme="'dark'"
          @click="handleMenuClick"
      >
        <!-- 首页 -->
        <a-menu-item key="">
          <template #icon><HomeOutlined /></template>
          <router-link to="/indexManagement">首页</router-link>
        </a-menu-item>

        <!-- 用户管理 -->
        <a-menu-item key="/userManagement">
          <template #icon><UserOutlined /></template>
          <router-link to="/userManagement">用户管理</router-link>
        </a-menu-item>
        <!-- 用户管理 -->
        <a-menu-item key="/stationManagement">
          <template #icon><CompassOutlined /></template>
          <router-link to="/stationManagement">车站管理</router-link>
        </a-menu-item>
        <!-- 用户管理 -->
        <a-menu-item key="/trainManagement">
          <template #icon><CarOutlined /></template>
          <router-link to="/trainManagement">车次管理</router-link>
        </a-menu-item>
        <!-- 用户管理 -->
        <a-menu-item key="/orderManagement">
          <template #icon><MoneyCollectOutlined /></template>
          <router-link to="/orderManagement">订单管理</router-link>
        </a-menu-item>
<!--        &lt;!&ndash; 车次管理（带子菜单） &ndash;&gt;-->
<!--        <a-sub-menu key="/train">-->
<!--          <template #icon><CarOutlined /></template>-->

<!--          <template #title>车次管理</template>-->
<!--          <a-menu-item key="/train/stationList">-->
<!--            <router-link to="/train/stationList">车站管理</router-link>-->
<!--          </a-menu-item>-->
<!--          <a-menu-item key="/trainManagement">-->
<!--            <router-link to="/trainManagement">车次列表</router-link>-->
<!--          </a-menu-item>-->
<!--          <a-menu-item key="/train/add-train">-->
<!--            <router-link to="/train/add-train">添加车次</router-link>-->
<!--          </a-menu-item>-->
<!--          <a-menu-item key="/train/ticketList">-->
<!--            <router-link to="/train/ticketList">车次列表</router-link>-->
<!--          </a-menu-item>-->
<!--        </a-sub-menu>-->


      </a-menu>
    </a-layout-sider>


    <a-layout>
      <a-layout-header style="background: #02172b; padding: 0 24px">
        <div style="text-align: right">
          <a-button type="primary">退出登录</a-button>
        </div>
      </a-layout-header>

      <!-- 内容区域 -->
      <a-layout>
        <a-layout-content :style="{ padding: '24px', minHeight: 'calc(100vh - 64px)' }">
          <div :style="{ background: '#fff', padding: '24px', minHeight: 'calc(100vh - 64px - 69px)' }">
            <router-view /> <!-- 子路由内容将在此渲染 -->
          </div>
        </a-layout-content>
      </a-layout>

      <a-layout-footer style="text-align: center">
        火车订票系统后台管理 ©2025 Created by Your Name
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>

<script setup>
import {ref} from 'vue';
import {Layout, Menu, Button} from 'ant-design-vue';
import {
  HomeOutlined,
  CarOutlined,
  UserOutlined
} from '@ant-design/icons-vue';

const {Header, Footer, Sider, Content} = Layout;

const collapsed = ref(false);
const onCollapse = (value) => {
  collapsed.value = value;
};
</script>

<style>
/* 全局样式 */
body {
  margin: 0;
  padding: 0;
}

/* 组件样式 */
.logo {
  height: 32px;
  margin: 16px;
  background: rgba(255, 255, 255, 0.3);
  text-align: center;
  line-height: 32px;
  color: white;
  overflow: hidden;
}

.ant-layout {
  min-height: 100vh;
}

.ant-layout-sider {
  min-height: 100vh;
}
</style>