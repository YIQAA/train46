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
      <template #action="{ record }">
        <a-button @click="toggleFreeze(record.id, record.frozen)">
          {{ record.frozen ? '解冻' : '冻结' }}
        </a-button>
        <a-button @click="viewOrderHistory(record.id)">查看历史订单</a-button>
      </template>
    </a-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

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
    dataIndex: 'id',
    key: 'id',
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
    title: '是否冻结',
    dataIndex: 'frozen',
    key: 'frozen',
    render: (text) => (text ? '是' : '否'),
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
    const response = await axios.get('/api/admin/users', {
      params: { phone },
    });
    users.value = response.data;
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
    const response = await axios.get(`/api/admin/users/${userId}/orders`);
    console.log('用户历史订单:', response.data);
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