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
        :rowKey="record => record.id"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <a-button type="link" @click="showEditModal(record)">编辑</a-button>
          <a-button type="link" danger @click="handleDelete(record.id)">删除</a-button>
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
            name="name"
            :rules="[{ required: true, message: '请输入车站名称' }]"
        >
          <a-input v-model:value="createFormState.name" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 编辑车站模态框 -->
    <a-modal
        v-model:visible="editModalVisible"
        title="编辑车站"
        @ok="handleEdit"
        @cancel="handleCancelEdit"
    >
      <a-form
          :model="editFormState"
          :label-col="{ span: 6 }"
          :wrapper-col="{ span: 18 }"
      >
        <a-form-item
            label="车站名称"
            name="name"
            :rules="[{ required: true, message: '请输入车站名称' }]"
        >
          <a-input v-model:value="editFormState.name" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';

// 加载状态
const loading = ref(false);
const stations = ref([]);

// 模态框控制
const createModalVisible = ref(false);
const editModalVisible = ref(false);

// 表单状态
const createFormState = reactive({ name: '' });
const editFormState = reactive({
  id: null,
  name: '',
  originalData: null // 保存原始数据用于取消时恢复
});

// 表格列配置
const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    key: 'id',
  },
  {
    title: '车站名称',
    dataIndex: 'name',
    key: 'name',
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
    const response = await axios.get('/api/stations');
    stations.value = response.data;
  } catch (error) {
    message.error('获取车站列表失败');
    console.error(error);
  } finally {
    loading.value = false;
  }
};

// 新增车站相关逻辑
const showCreateModal = () => {
  createFormState.name = '';
  createModalVisible.value = true;
};

const handleCreate = async () => {
  try {
    const response = await axios.post('/api/stations', {
      name: createFormState.name
    });

    if (response.status === 201) {
      message.success('创建成功');
      await getStations();
      createModalVisible.value = false;
    }
  } catch (error) {
    message.error('创建车站失败');
    console.error(error);
  }
};

const handleCancelCreate = () => {
  createModalVisible.value = false;
};

// 编辑车站相关逻辑
const showEditModal = (record) => {
  editFormState.id = record.id;
  editFormState.name = record.name;
  editFormState.originalData = { ...record }; // 保存原始数据
  editModalVisible.value = true;
};

const handleEdit = async () => {
  try {
    const response = await axios.put(`/api/stations/${editFormState.id}`, {
      name: editFormState.name
    });

    if (response.status === 200) {
      message.success('更新成功');
      await getStations();
      editModalVisible.value = false;
    }
  } catch (error) {
    message.error('更新车站失败');
    console.error(error);
  }
};

const handleCancelEdit = () => {
  // 恢复原始数据
  if (editFormState.originalData) {
    editFormState.name = editFormState.originalData.name;
  }
  editModalVisible.value = false;
};

// 删除车站
const handleDelete = async (id) => {
  try {
    const response = await axios.delete(`/api/stations/${id}`);
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