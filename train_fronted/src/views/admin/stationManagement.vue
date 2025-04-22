<template>
  <div>
    <h1>车站管理</h1>
    <div style="margin-bottom: 20px;">
      <a-button type="primary" @click="showCreateModal">新增车站</a-button>
    </div>

    <!-- 车站列表表格 -->
    <a-table
        :columns="columns"
        :data-source="stations"
        :loading="loading"
        :rowKey="record => record.stationId"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <a-button type="link" danger @click="handleDelete(record.stationId)">删除</a-button>
        </template>
      </template>
    </a-table>

    <!-- 新增车站模态框 -->
    <a-modal
        v-model:visible="createModalVisible"
        title="新增车站"
        @ok="handleCreate"
        @cancel="handleCancelCreate"
    >
      <a-form
          :model="createFormState"
          :label-col="{ span: 6 }"
          :wrapper-col="{ span: 18 }"
      >
        <a-form-item
            label="车站名称"
            stationName="stationName"
            :rules="[{ required: true, message: '请输入车站名称' }]"
        >
          <a-input v-model:value="createFormState.stationName" />
        </a-form-item>
        <a-form-item
            label="城市名称"
            stationName="cityName"
            :rules="[{ required: true, message: '请输入城市名称' }]"
        >
          <a-input v-model:value="createFormState.cityName" />
        </a-form-item>
        <a-form-item
            label="城市编码"
            stationName="cityCode"
            :rules="[{ required: true, message: '请输入城市编码' }]"
        >
          <a-input v-model:value="createFormState.cityCode" />
        </a-form-item>

      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import {fetchStationList,fetchCreateStation} from '@/service/index.js';
// 加载状态
const loading = ref(false);
const stations = ref([]);

// 模态框控制
const createModalVisible = ref(false);

// 表单状态
const createFormState = reactive({
  stationName: '' ,
  cityName: '',
  cityCode: ''
});

// 表格列配置
const columns = [
  {
    title: 'stationId',
    dataIndex: 'stationId',
    key: 'stationId',
  },
  {
    title: '车站名称',
    dataIndex: 'stationName',
    key: 'stationName',
  },
  {
    title: '所属城市',
    dataIndex: 'city',
    key: 'city',
  },
  {
    title: '城市编码',
    dataIndex: 'cityCode',
    key: 'cityCode',
  },
  {
    title: '操作',
    key: 'action',
    width: 150
  }
];

// 获取车站列表
const getStations = async () => {
  try {
    loading.value = true;
    fetchStationList().then(response => {
      stations.value = response;
    }).catch(error => {
      message.error('获取车站列表失败');
      console.error(error);
    });
  } finally {
    loading.value = false;
  }
};

// 新增车站相关逻辑
const showCreateModal = () => {
  createFormState.stationName = '';
  createFormState.cityName = '';
  createFormState.cityCode = '';
  createModalVisible.value = true;
};

const handleCreate = async () => {
  try {
    fetchCreateStation({
      stationName: createFormState.stationName,
      cityName: createFormState.cityName,
      cityCode: createFormState.cityCode}
    ).then(response => {
      message.success('创建成功');
      createModalVisible.value = false;
    });
    await getStations();
  } catch (error) {
    message.error('创建车站失败');
    console.error(error);
  }
};

const handleCancelCreate = () => {
  createModalVisible.value = false;
};



// 删除车站
const handleDelete = async (stationId) => {
  try {
    const response = await axios.delete(`/api/stations/${stationId}`);
    if (response.status === 200) {
      message.success('删除成功');
      await getStations();
    }
  } catch (error) {
    message.error('删除车站失败');
    console.error(error);
  }
};

// 初始化加载
onMounted(() => {
  getStations();
});
</script>