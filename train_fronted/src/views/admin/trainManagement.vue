
<template>
  <div>
    <h1>车次列表管理</h1>
    <!-- 筛选条件 -->
    <a-form layout="inline">
      <a-form-item>
        <a-button type="primary" @click="showCreateModal">新增车次</a-button>
      </a-form-item>
    </a-form>
    <br>
    <!-- 车次列表表格 -->
    <a-table :columns="columns"
             :data-source="state.dataSource"
             :pagination="false"
             :loading="state.loading"
             :bordered="true">

      <template #customStaionTitle>
        <div>出发站</div>
        <div>到达站</div>
      </template>
      <template #station="{  record }">
        <div>
              <span
                  :style="{
                  display: 'inline-block',
                  padding: '2px 4px',
                  backgroundColor: '#cca567' ,
                  color: '#fff',
                  borderRadius: '4px',
                  fontSize: '10px',
                  fontWeight: 'bolder',
                  marginRight: '2px'
                }"
              >{{ '始' }}</span
              >{{ record.trainStartStation }}
        </div>
        <div>
              <span
                  :style="{
                  display: 'inline-block',
                  padding: '2px 4px',
                  backgroundColor: '#6da77f' ,
                  color: '#fff',
                  borderRadius: '4px',
                  fontSize: '10px',
                  fontWeight: 'bolder',
                  marginRight: '2px'
                }"
              >{{ '终' }}</span
              >{{ record.trainEndStation }}
        </div>
      </template>

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
import {fetchAdminTicketList, fetchTrainList} from '@/service/index.js';
import {message, Table} from "ant-design-vue";
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
  {
    title: '车次编号',
    dataIndex: 'trainNumber',
    key: 'trainNumber',
    customCell: (record) => ({rowSpan: record?.rowSpan}),
    align: 'center'
  },
  {
    title: '车次类型',
    dataIndex: 'trainType',
    key: 'trainType',
    customCell: (record) => ({rowSpan: record?.rowSpan})
  },
  /* 车站信息列（复合列） */
  {
    key: 'station',
    slots: {
      title: 'customStaionTitle',
      customRender: 'station'
    },
    customCell: (record) => ({rowSpan: record?.rowSpan}),
    width: 100,
    ellipsis: true,
    align: 'left'
  },
  {
    title: '总时间',
    dataIndex: 'trainTotalTime',
    key: 'trainTotalTime',
    customCell: (record) => ({rowSpan: record?.rowSpan}),
    width: 120,
    ellipsis: true,
    resizable: false,
    align: 'center'
  },
  {
    title: '总里程',
    dataIndex: 'trainDistance',
    key: 'trainDistance',
    customCell: (record) => ({rowSpan: record?.rowSpan}),
    width: 120,
    ellipsis: true,
    resizable: false,
    align: 'center'
  },
  {
    title: '站序',
    dataIndex: 'sequence',
    key: 'sequence'
  },
  {
    title: '站名',
    dataIndex: 'stationName',
    key: 'departureStation'
  },
  {
    title: '到站时间',
    dataIndex: 'arrivalTime',
    key: 'arrivalTime'
  },
  {
    title: '出发时间',
    dataIndex: 'departureTime',
    key: 'departureTime'
  },
  {
    title: '停留时间',
    dataIndex: 'stopoverTime',
    key: 'stopoverTime'
  },
  {
    title: '操作',
    key: 'action',
    slots: {customRender: 'action'},
    customCell: (record) => ({rowSpan: record?.rowSpan})
  },
];

// 生命周期
onMounted(() => {
  getTrains();
});

const state = reactive({
  dataSource: [],
  data: [],
  loading: false,
  pagination: {
    current: 1,
    pageSize: 10,
    total: 0,
    showSizeChanger: true,
    showQuickJumper: true,
    showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
  }
});

// 方法封装
const getTrains =  () => {
  loading.value = true;
  fetchTrainList()
  .then((res) => {
    console.log('订单返回信息');
    console.log(res);
    let dataSource = [];
    res.map((info) => {
      console.log('当前处理的记录:', info);
      info.trainStationQueryRespDTOList?.map((item, index) => {
        dataSource.push({
          ...info,
          ...item,
          rowSpan: index === 0 ? info.trainStationQueryRespDTOList.length : 0
        });
      });
    });
    console.log('处理后的数据源:', dataSource);
    state.dataSource = dataSource;
    state.data = res;
    state.loading = false;
  })
  .catch((err) => {
    console.error('获取车次列表失败', err);
    message.error('获取车次列表失败，请稍后重试');
    state.loading = false;
  });
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