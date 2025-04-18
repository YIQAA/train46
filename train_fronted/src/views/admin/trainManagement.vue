
<template>
  <div>
    <h1>车次列表管理</h1>
    <!-- 筛选条件 -->
    <a-form layout="inline">
      <a-form-item label="状态">
        <a-select v-model:value="filterState.status" style="width: 120px">
          <a-select-option value="">全部</a-select-option>
          <a-select-option value="正常">正常</a-select-option>
          <a-select-option value="停运">停运</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="handleFilter">筛选</a-button>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="showCreateModal">新增车次</a-button>
      </a-form-item>
    </a-form>

    <!-- 车次列表表格 -->
    <a-table :columns="columns" :data-source="trains" :loading="loading">
      <template #action="{ record }">
        <a-button type="link" @click="showEditModal(record)">编辑</a-button>
        <a-button type="link" @click="handleDelete(record.id)">删除</a-button>
      </template>
    </a-table>

    <!-- 新增模态框 -->
    <a-modal
        v-model:visible="createModalVisible"
        title="新增车次"
        @ok="handleCreate"
    >
      <a-form-model ref="createFormRef" :model="createFormState" :rules="formRules">
        <a-form-item label="车次编号" name="trainNumber">
          <a-input v-model:value="createFormState.trainNumber" />
        </a-form-item>
        <a-form-item label="状态" name="status">
          <a-select v-model:value="createFormState.status">
            <a-select-option value="正常">正常</a-select-option>
            <a-select-option value="停运">停运</a-select-option>
          </a-select>
        </a-form-item>
      </a-form-model>
    </a-modal>

    <!-- 编辑模态框 -->
    <a-modal
        v-model:visible="editModalVisible"
        title="编辑车次"
        @ok="handleEdit"
    >
      <a-form-model ref="editFormRef" :model="editFormState" :rules="formRules">
        <a-form-item label="车次编号" name="trainNumber">
          <a-input v-model:value="editFormState.trainNumber" />
        </a-form-item>
        <a-form-item label="状态" name="status">
          <a-select v-model:value="editFormState.status">
            <a-select-option value="正常">正常</a-select-option>
            <a-select-option value="停运">停运</a-select-option>
          </a-select>
        </a-form-item>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue';
import axios from 'axios';

// 响应式状态
const loading = ref(false);
const trains = ref([]);
const filterState = reactive({status: ''});
const createModalVisible = ref(false);
const editModalVisible = ref(false);

// 表单相关
const createFormState = reactive({trainNumber: '', status: ''});
const editFormState = reactive({id: null, trainNumber: '', status: ''});
const createFormRef = ref(null);
const editFormRef = ref(null);

// 表单验证规则
const formRules = reactive({
  trainNumber: [{required: true, message: '请输入车次编号', trigger: 'blur'}],
  status: [{required: true, message: '请选择状态', trigger: 'change'}]
});

// 表格列配置
const columns = [
  {title: 'ID', dataIndex: 'id', key: 'id'},
  {title: '车次编号', dataIndex: 'trainNumber', key: 'trainNumber'},
  {title: '状态', dataIndex: 'status', key: 'status'},
  {title: '操作', key: 'action', slots: {customRender: 'action'}},
];

// 生命周期
onMounted(() => {
  getTrains();
});

// 方法封装
const getTrains = async (status = '') => {
  loading.value = true;
  try {
    const response = await axios.get('/api/trains', {params: {status}});
    trains.value = response.data;
  } catch (error) {
    console.error('获取车次列表失败', error);
  } finally {
    loading.value = false;
  }
};

const handleFilter = () => getTrains(filterState.status);

const showCreateModal = () => {
  createFormRef.value?.resetFields();
  createModalVisible.value = true;
};

const handleCreate = async () => {
  try {
    await createFormRef.value.validate();
    const response = await axios.post('/api/trains', createFormState);
    if (response.status === 201) {
      await getTrains();
      createModalVisible.value = false;
      createFormRef.value.resetFields();
    }
  } catch (error) {
    console.error('新增车次失败', error);
  }
};

const showEditModal = (record) => {
  editFormRef.value?.resetFields();
  Object.assign(editFormState, record);
  editModalVisible.value = true;
};

const handleEdit = async () => {
  try {
    await editFormRef.value.validate();
    const response = await axios.put(`/api/trains/${editFormState.id}`, editFormState);
    if (response.status === 200) {
      await getTrains();
      editModalVisible.value = false;
      editFormRef.value.resetFields();
    }
  } catch (error) {
    console.error('编辑车次失败', error);
  }
};

const handleDelete = async (id) => {
  try {
    const response = await axios.delete(`/api/trains/${id}`);
    if (response.status === 200) {
      await getTrains();
    }
  } catch (error) {
    console.error('删除车次失败', error);
  }
};
</script>