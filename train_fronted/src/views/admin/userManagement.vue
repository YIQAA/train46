<template>
  <div>
    <h1>用户列表</h1>
    <a-form :form="searchForm" layout="inline">
      <!-- 手机号搜索框 -->
      <a-form-item style="width: 200px;">
        <a-input
            v-model="searchPhone"
            placeholder="请输入手机号搜索"
            @pressEnter="searchUsers"
        />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="searchUsers">搜索</a-button>
      </a-form-item>
    </a-form>
    <!-- 用户列表表格 -->
    <a-table :columns="columns" :data-source="users" :loading="loading">
      <template #frozen="{ record }">
        <div v-if="record.frozen">冻结</div>
        <div v-else>正常</div>
      </template>

      <template #action="{ record }">
        <a-button @click="toggleFreeze(record.userid, record.frozen)">
          {{ record.frozen ? '解冻' : '冻结' }}
        </a-button>
        <a-button @click="viewOrderHistory(record.userid)">查看历史订单</a-button>
      </template>
    </a-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import {fetchUserList} from '@/service/index';
import router from "@/router/index.js";
// 搜索手机号
const searchPhone = ref('');
// 用户列表数据
const users = ref([]);
// 加载状态
const loading = ref(false);

// 表格列配置
const columns = [
  {
    title: 'ID',
    dataIndex: 'userid',
    key: 'userid',
  },
  {
    title: '用户名',
    dataIndex: 'username',
    key: 'username',
  },
  {
    title: '手机号',
    dataIndex: 'phone',
    key: 'phone',
  },
  {
    title: '订单数',
    dataIndex: 'orderAmount',
    key: 'orderAmount'
  },
  {
    title: '状态',
    dataIndex: 'frozen',
    key: 'frozen',
    slots: { customRender: 'frozen' },
  },
  {
    title: '操作',
    key: 'action',
    slots: { customRender: 'action' },
  },
];

// 获取用户列表
const getUsers = async (phone = '') => {
  loading.value = true;
  try {

    fetchUserList().then((response) => {
      console.log('获取用户列表成功')
      console.log(response);
      users.value = response;
    })

  } catch (error) {
    console.error('获取用户列表失败', error);
  } finally {
    loading.value = false;
  }
};

// 搜索用户
const searchUsers = () => {
  getUsers(searchPhone.value);
};

// 冻结/解冻用户
const toggleFreeze = async (userId, isFrozen) => {
  try {
    const response = await axios.put(`/api/admin/users/${userId}/freeze`, {
      frozen: !isFrozen,
    });
    if (response.status === 200) {
      getUsers(searchPhone.value);
    }
  } catch (error) {
    console.error('冻结/解冻用户失败', error);
  }
};

// 查看用户历史订单
const viewOrderHistory = async (userId) => {
  try {
    await router.push({path: '/orderManagement', query: {userId}})
    // 这里可以添加弹窗或跳转页面来显示订单详情
  } catch (error) {
    console.error('查看用户历史订单失败', error);
  }
};

// 页面加载时获取用户列表
onMounted(() => {
  getUsers();
});
</script>

<style scoped>
/* 可以添加一些自定义样式 */
</style>