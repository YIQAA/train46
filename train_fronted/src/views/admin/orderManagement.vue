<template>
  <div>
    <h1>订单列表</h1>
    <!-- 搜索条件 -->
    <a-form :form="searchForm" layout="inline">
      <a-form-item label="日期范围">
        <a-range-picker v-model:value="searchDateRange" />
      </a-form-item>
      <a-form-item label="订单状态">
        <a-select v-model:value="searchStatus" placeholder="请选择状态">
          <a-select-option value="0">待支付</a-select-option>
          <a-select-option value="1">已支付</a-select-option>
          <a-select-option value="2">已取消</a-select-option>
          <a-select-option value="3">已完成</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="handleSearch">搜索</a-button>
      </a-form-item>
    </a-form>
    <!-- 订单列表表格 -->
    <a-table :columns="columns" :data-source="orders" :loading="loading">
      <template #action="{ record }">
        <a-button @click="toggleDetails(record.orderSn)">详情</a-button>
      </template>
      <template #expandedRowRender="{ record }">
        <div v-if="expandedOrderSn === record.orderSn">
          <a-table :columns="detailColumns" :data-source="record.passengerDetails" :pagination="false" />
        </div>
      </template>
    </a-table>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';

import axios from 'axios';

// 搜索表单实例

// 搜索日期范围
const searchDateRange = ref([]);
// 搜索订单状态
const searchStatus = ref(null);
// 订单列表数据
const orders = ref([]);
// 加载状态
const loading = ref(false);
// 展开详情的订单号
const expandedOrderSn = ref(null);

// 表格列配置
const columns = [
  {
    title: '订单号',
    dataIndex: 'orderSn',
    key: 'orderSn',
  },
  {
    title: '乘车日期',
    dataIndex: 'ridingDate',
    key: 'ridingDate',
  },
  {
    title: '车次',
    dataIndex: 'trainNumber',
    key: 'trainNumber',
  },
  {
    title: '出发站',
    dataIndex: 'departureStation',
    key: 'departureStation',
  },
  {
    title: '到达站',
    dataIndex: 'arrivalStation',
    key: 'arrivalStation',
  },
  {
    title: '订单状态',
    dataIndex: 'orderStatus',
    key: 'orderStatus',
    render: (text) => {
      const statusMap = {
        0: '待支付',
        1: '已支付',
        2: '已取消',
        3: '已完成',
      };
      return statusMap[text];
    },
  },
  {
    title: '操作',
    key: 'action',
    slots: { customRender: 'action' },
  },
];

// 详情表格列配置
const detailColumns = [
  {
    title: '乘客姓名',
    dataIndex: 'realName',
    key: 'realName',
  },
  {
    title: '证件类型',
    dataIndex: 'idType',
    key: 'idType',
  },
  {
    title: '证件号',
    dataIndex: 'idCard',
    key: 'idCard',
  },
  {
    title: '票种类型',
    dataIndex: 'ticketType',
    key: 'ticketType',
    render: (text) => {
      const ticketTypeMap = {
        0: '成人票',
        1: '学生票',
      };
      return ticketTypeMap[text];
    },
  },
  {
    title: '车票金额',
    dataIndex: 'amount',
    key: 'amount',
    render: (text) => `￥${text}`,
  },
];

// 获取订单列表
const getOrders = async () => {
  loading.value = true;
  try {
    const response = await axios.get('/api/order-service/order/ticket/page', {
      params: {
        userId: '123', // 假设用户 ID 为 123，可根据实际情况修改
        statusType: searchStatus.value,
        // 处理日期范围
        startDate: searchDateRange.value[0] ? searchDateRange.value[0].format('YYYY-MM-DD') : null,
        endDate: searchDateRange.value[1] ? searchDateRange.value[1].format('YYYY-MM-DD') : null,
      },
    });
    orders.value = response.data.records;
  } catch (error) {
    console.error('获取订单列表失败', error);
  } finally {
    loading.value = false;
  }
};

// 处理搜索
const handleSearch = () => {
  getOrders();
};

// 切换详情展开状态
const toggleDetails = (orderSn) => {
  if (expandedOrderSn.value === orderSn) {
    expandedOrderSn.value = null;
  } else {
    expandedOrderSn.value = orderSn;
  }
};

// 页面加载时获取订单列表
onMounted(() => {
  getOrders();
});
</script>

<style scoped>
/* 可以添加一些自定义样式 */
</style>