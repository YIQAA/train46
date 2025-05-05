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
    <Table
        :columns="columns"
        :data-source="state.dataSource"
        :pagination="false"
        :loading="state.loading"
        :bordered="true"
    >
      <!-- 自定义渲染旅客信息列 -->
      <template #passenger="{ text, record }">
        <div :style="{alignItems: 'center'}">
          <!-- 显示姓名 -->
          <div>{{record?.realName }}</div>
          <br>
          <!-- 显示证件号码 -->
          <div>{{ record?.idCard }}</div>
        </div>
      </template>
      <!-- 自定义渲染席位信息列 -->
      <template #seatType="{ text, record }">
        <div>
          <!-- 显示席位类型 -->
          {{
            SEAT_CLASS_TYPE_LIST.find(
                (item) => item.code === record?.seatType
            )?.label
          }}
        </div>
        <div>
          <!-- 显示车厢号和座位号 -->
          <span>{{ record?.carriageNumber }}</span>
          <span>车</span>
          <span>{{ record?.seatNumber }}</span>
          <span>号</span>
        </div>
      </template>
      <!-- 自定义渲染票价列 -->
      <template #amount="{ text, record }">
        <div>
          <!-- 显示车票类型 -->
          {{
            TICKET_TYPE_LIST.find((item) => item.value === record?.ticketType)
                ?.label
          }}
        </div>
        <div :style="{ color: 'orange' }">
          <!-- 显示票价 -->
          ￥{{ record?.amount }}
        </div>
      </template>

      <!-- 车票状态列 -->
      <template #status="{ text, record }">
        <div :style="{ textAlign: 'center' }">
          <!-- 显示车票状态 -->
          {{
            TICKET_STATUS_LIST.find((item) => item.value === record?.status)
                ?.label ?? '--'
          }}
        </div>
      </template>

      <!-- 自定义渲染车次信息列 -->
      <template #info="{ text, record }">
        <div>
          <!-- 显示订票日期 -->
          <div class="border">车次：{{ record.trainNumber }}</div>
          <div>
            <!-- 显示出发站、到达站和车次 -->
            <span class="border">{{ record.departureStation }}</span>
            <span class="border">{{ '-->' }}</span>
            <span class="border">{{ record.arrivalStation }}</span>
          </div>
          <div>
            <!-- 显示乘车日期、出发时间 -->
            <span class="margin">{{ record.ridingDate }}</span>
            <span class="margin">{{ record.departureTime }}</span>
            <span class="margin">发车</span>
          </div>
        </div>
      </template>
    </Table>



  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import {SEAT_CLASS_TYPE_LIST, TICKET_STATUS_LIST, TICKET_TYPE_LIST} from "@/constants/index.js";
import {message, Table} from "ant-design-vue";
import {fetchAdminTicketList} from "@/service/index.js";
import {useRoute} from "vue-router";

// 搜索表单实例
// 响应式状态对象
const state = reactive({
  dataSource: [], // 表格数据源
  current: 1, // 当前页码
  size: '10', // 每页显示数量
  loading: false, // 加载状态
  columns: [], // 表格列配置


  currentOrder: null, // 当前操作的订单
  refundOrder: [] // 选择的退票子订单
});
const rowState = reactive({
  dataSource: []
})
// 表格列配置
const columns = [
  {
    title: '订单号',
    dataIndex: 'orderSn',
    key: 'orderSn',
    customCell: (record) => ({rowSpan: record?.rowSpan}),
    width: 150
  },
  {
    title: '用户名',
    dataIndex: 'userName',
    key: 'userName',
    customCell: (record) => ({rowSpan: record?.rowSpan}),
    width: 100
  },
  {
    title: '订票时间',
    dataIndex: 'orderTime',
    key: 'orderTime',
    customCell: (record) => ({rowSpan: record?.rowSpan}),
    width: 120
  },
  {
    title: '车次信息',
    dataIndex: 'arrival',
    key: 'arrival',
    slots: {customRender: 'info'},
    customCell: (record) => ({rowSpan: record?.rowSpan}),
    width: 200
  },
  {
    title: '旅客信息',
    dataIndex: 'passenger',
    key: 'passenger',
    slots: {customRender: 'passenger'},
    width: 150
  },
  {
    title: '席位信息',
    dataIndex: 'seatType',
    key: 'seatType',
    slots: {customRender: 'seatType'},
    width: 150
  },
  {
    title: '票价',
    dataIndex: 'amount',
    key: 'amount',
    slots: {customRender: 'amount'},
    width: 100
  },
  {
    title: '车票状态',
    dataIndex: 'status',
    key: 'status',
    slots: {customRender: 'status'},
    customCell: (record) => ({rowSpan: record.rowSpan}),
    width: 120
  }
];

// 搜索日期范围
const searchDateRange = ref([]);
// 搜索订单状态
const searchStatus = ref(null);


let userId = ref(null);

const { query } = useRoute()
// 页面加载时获取订单列表
onMounted(() => {
  userId = query.userId;
  getOrders();
});


// 获取订单列表
const getOrders = (current, size, statusType) => {
  state.loading = true;
  fetchAdminTicketList({
    userId,
    current,
    size,
    statusType
  })
      .then((res) => {
        console.log('订单返回信息');
        console.log(res);
        let dataSource = [];
        res.records.map((info) => {
          console.log('当前处理的记录:', info);
          info.passengerDetails?.map((item, index) => {
            dataSource.push({
              ...info,
              ...item,
              rowSpan: index === 0 ? info.passengerDetails.length : 0
            });
          });
        });
        console.log('处理后的数据源:', dataSource);
        rowState.dataSource = dataSource;
        state.dataSource = dataSource;
        state.data = res;
        state.loading = false;
      })
      .catch((err) => {
        console.error('获取订单列表失败', err);
        message.error('获取订单列表失败，请稍后重试');
        state.loading = false;
      });
};



// 处理搜索
const handleSearch = () => {
  getOrders();
};


</script>

<style lang="scss" scoped>

.tabs-container {
  :deep(.ant-tabs-nav) {
    margin: 0;
    &:before {
      border-bottom: 1px solid #f0f0f0;
    }

    .ant-tabs-tab {
      background: #fafafa !important;
      border: 1px solid #f0f0f0 !important;
      border-bottom: 0 !important;


      &-active {
        background: #fff !important;
        border-bottom: 1px solid #fff !important;
        margin-bottom: -1px;
      }
    }
  }

  :deep(.ant-tabs-content-holder) {
    border: 1px solid #f0f0f0;
    border-top: 0;
    border-radius: 0 0 0px 0px;
    padding: 16px;
  }
}

.tips-txt {
  background: #fffbe5;
  border: 1px solid #fbd800;
  padding: 5px;
  margin-top: 10px;
}


::v-deep {
  .ant-table-thead > tr > th {
    background-color: #f8f8f8;
  }

  .ant-table-thead .ant-table-cell {
    background-image: none;
  }

  .ant-tabs-top > .ant-tabs-nav {
    margin: 0;
  }

  .ant-tabs-content-holder {
    padding: 12px;
    background-color: #fff;
    box-sizing: border-box;
    background-image: none;
  }

  .custom-modal {
    .ant-alert-warning {
      background-color: #fff !important;
      border: none !important;
    }
  }
}

.order-date {
  color: #8f9598;
  padding: 6px 0;
}

.border {
  font-weight: bolder;
  padding: 0 4px;
}

.margin {
  padding: 0 4px;
}

.vertical-buttons {
  flex-direction: column;
}
</style>