<template>
  <!-- 卡片容器，用于放置标签页 -->
  <div class="tabs-container">
    <!-- 标签页组件，使用卡片样式，绑定当前激活的标签页 -->
    <Tabs type="card" v-model:active-key="state.activeKey">
      <!-- 未完成订单标签页 -->
      <TabPane :key="0" tab="未完成订单"></TabPane>
      <!-- 未出行订单标签页 -->
      <TabPane :key="1" tab="未出行订单"></TabPane>
      <!-- 历史订单标签页 -->
      <TabPane :key="2" tab="历史订单"></TabPane>
    </Tabs>
  </div>
  <!-- 表格组件，展示订单数据 -->
  <Table
      :columns="state.columns"
      :data-source="state.dataSource"
      :pagination="false"
      :loading="state.loading"
      :bordered="true"
  >
    <!-- 自定义渲染旅客信息列 -->
    <template #id="{ text, record }">
      <div
          :style="{
          alignItems: 'center'
        }"
      >
        <div>
          <!-- 显示姓名 -->
          {{
            record?.realName
          }}
        </div>
        <br>
        <div>
          <!-- 显示证件号码 -->
          {{ record?.idCard }}
        </div>
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
    <!-- 自定义渲染车票状态列 -->
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
        <div class="order-date">订票日期：{{ record.orderTime }}</div>
        <div>
          <!-- 显示出发站、到达站和车次 -->
          <span class="border">{{ record.departureStation }}</span>
          <span class="border">{{ '-->' }}</span>
          <span class="border">{{ record.arrivalStation }}</span>
          <span class="border">{{ record.trainNumber }}</span>
        </div>
        <div>
          <!-- 显示乘车日期、出发时间 -->
          <span class="margin">{{ record.ridingDate }}</span>
          <span class="margin">{{ record.departureTime }}</span>
          <span class="margin">开</span>
        </div>
      </div>
    </template>
    <!-- 自定义渲染操作列 -->
    <template #edit="{ text, record }">
      <Space class="vertical-buttons">
        <!-- 取消订单按钮 -->
        <Button v-if="state.activeKey === 0" type="link" @click="() => cancelOrder(record.orderSn)" >取消订单</Button>
        <!-- 去支付按钮 -->
        <Button v-if="state.activeKey === 0" type="link" @click="() => pay(record.orderSn)">去支付</Button>
        <!-- 退票按钮 -->
        <Button v-if="state.activeKey === 1" type="link" @click="() => {
              state.visible = true;
              state.currentOrder = record?.orderSn;}"
        > 退票
        </Button>
      </Space>
    </template>
  </Table>

  <!-- 分页组件，显示在页面底部 -->
  <div
      :style="{
      width: '100%',
      marginTop: '20px',
      display: 'flex',
      justifyContent: 'end'
    }"
  >
    <Pagination
        :show-total="(total) => `总共 ${state.data?.total} 条`"
        :current="state.current"
        :size="state.size"
        :total="state.data?.total"
        show-size-changer
        @change="handlePage"
    ></Pagination>
  </div>
  <!-- 温馨提示区域 -->
  <div class="tips-txt">
    <h2 :style="{ fontSize: '16px' }">温馨提示：</h2>
    <p>1. 席位已锁定，请在指定时间内完成网上支付。</p>
    <p>2. 逾期未支付，系统将取消本次交易。</p>
    <p>3. 在完成支付或取消订单之前，您将无法购买其他车票</p>
    <p>
      4.
      未尽事宜详见《国铁集团铁路旅客运输规程》《广深港高铁铁路跨境旅客运输组织规则》《中老铁路跨境旅客联运组织规则》等有关规定和车站公告。
    </p>
  </div>

  <!-- 退票模态框 -->
  <Modal
      width="40%"
      :visible="state.visible"
      title="退票申请"
      class="custom-modal"
      @cancel="state.visible = false"
      :footer="null"
  >
    <!-- 警告提示框 -->
    <Alert
        message="您确认要退款吗？"
        type="warning"
        description="如有定餐饮或特产，请按规定到网站自行办理退订"
        show-icon
        style="background-color: #fff; border: none"
    >
      <template #icon>
        <QuestionCircleFilled />
      </template>
    </Alert>
    <Divider :dashed="true" />
    <div style="padding: 0 30px">
      请选择要退票的订单：
      <!-- 复选框组，选择退票的子订单 -->
      <CheckboxGroup
          v-model:value="state.refundOrder"
          @change="(value) => console.log(value, 'value')"
          :options="getRefundOptions()"
      ></CheckboxGroup>
    </div>

    <Divider :dashed="true" />
    <div style="padding: 0 30px">
      共计退款：<a>{{ getRefundTotal() }}</a>
    </div>
    <Divider :dashed="true" />
    <div style="padding: 0 30px">
      <div style="margin-bottom: 20px">
        车票票价：<a>{{ getRefundTotal() }}</a>
      </div>
      <div>
        应退票款：<a>{{ getRefundTotal() }}</a>
      </div>
    </div>
    <Divider :dashed="true" />
    <div style="color: #999999; padding: 0 30px">
      <QuestionCircleFilled />
      <span style="margin-left: 20px">
        实际核收退票费及应退票款将按最终交易时间计算。
      </span>
    </div>
    <div style="color: #999999; padding: 0 30px">
      <QuestionCircleFilled />
      <span style="margin-left: 20px">
        如你需要办理该次列车前续、后续退票业务，请于退票车次票面开车时间前办理。
      </span>
    </div>
    <Space style="width: 100%; justify-content: center; margin-top: 20px">
      <!-- 取消按钮 -->
      <Button @click="state.visible = false">取消</Button>
      <!-- 确定按钮 -->
      <Button
          @click="handleRefund"
          type="primary"
          :disabled="!state.refundOrder.length"
      >确定
      </Button>
    </Space>
  </Modal>
</template>

<script setup>
import {
  Tabs,
  TabPane,
  Table,
  Pagination,
  message,
  CheckboxGroup,
  Modal,
  Alert,
  Divider,
  Space,
  Button
} from 'ant-design-vue';
import {QuestionCircleFilled} from '@ant-design/icons-vue';
import {fetchTicketList, fetchOrderCancel, fetchRefundTicket} from '@/service/index.js';
import {reactive, watch, h} from 'vue';
import {
  ID_CARD_TYPE,
  SEAT_CLASS_TYPE_LIST,
  TICKET_TYPE_LIST,
  TICKET_STATUS_LIST
} from '@/constants/index.js';
import Cookie from 'js-cookie';
import {useRouter} from 'vue-router';

// 响应式状态对象
const state = reactive({
  activeKey: 1, // 当前激活的标签页
  dataSource: [], // 表格数据源
  current: 1, // 当前页码
  size: '10', // 每页显示数量
  loading: false, // 加载状态
  columns: [], // 表格列配置
  checkList: [], // 选中的订单列表
  visible: false, // 退票模态框显示状态
  currentOrder: null, // 当前操作的订单
  refundOrder: [] // 选择的退票子订单
});

// 获取用户 ID
const userId = localStorage.getItem('userId');
// 获取路由实例
const router = useRouter();

// 表格列配置
const columns = [
  {
    title: '车次信息',
    dataIndex: 'arrival',
    key: 'arrival',
    slots: {customRender: 'info'},
    customCell: (record) => ({rowSpan: record?.rowSpan}),
    width: 180
  },
  {
    title: '旅客信息',
    dataIndex: 'id',
    key: 'id',
    slots: {customRender: 'id'},
    width: 200
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
  },
  {
    title: '操作',
    dataIndex: 'edit',
    key: 'edit',
    slots: {customRender: 'edit'},
    customCell: (record) => ({rowSpan: record.rowSpan}),
    width: 150
  }
];

// 固定表格列配置，去掉监听 activeKey 变化的逻辑
state.columns = columns;

// 处理分页变化
const handlePage = (page, pagesize) => {
  state.current = page;
  state.size = String(pagesize);
};

// 取消订单
const cancelOrder = (sn) => {
  console.log('取消订单', sn);
  fetchOrderCancel({orderSn: sn})
      .then((res) => {
        if (res) {
          message.success('订单取消成功');
          getTicketList(state.current, state.size, state.activeKey);
        }
      })
      .catch((err) => {
        console.error('取消订单失败', err);
        message.error('取消订单失败，请稍后重试');
      });
};

// 去支付
const pay = (sn) => {
  router.push(`/paymentTimer?sn=${sn}`);
};

// 获取订单列表
const getTicketList = (current, size, statusType) => {
  state.loading = true;
  fetchTicketList({
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

// 监听 activeKey、current、size 变化，重新获取订单列表
watch(
    () => [state.activeKey, state.current, state.size],
    (newValue) => {
      state.loading = true;
      const [statusType, current, size] = newValue;
      getTicketList(current, size, statusType);
    },
    {immediate: true}
);

// 获取退票复选框选项
const getRefundOptions = () => {
  return state.dataSource
      ?.find((item) => item.orderSn === state.currentOrder)
      ?.passengerDetails.map((item) => ({
        label: item.realName,
        value: item.id
      }));
};

// 计算退票总金额
const getRefundTotal = () => {
  return (
      '¥' +
      state.dataSource
          ?.find((item) => item.orderSn === state.currentOrder)
          ?.passengerDetails?.filter((item) =>
          state.refundOrder.includes(item.id)
      )
          ?.map((item) => item.amount)
          ?.reduce((after, pre) => after + pre, 0)
  );
};

// 退票模态框确认按钮点击事件
const handleRefund = () => {
  fetchRefundTicket({
    orderSn: state.currentOrder,
    type: 0,
    subOrderRecordIdReqList: state.refundOrder
  })
  .then((res) => {
    console.log('退票返回信息'+res);
    state.visible = false;
    message.success('退款成功');
    getTicketList(state.current, state.size, state.activeKey);
  })
  .catch((err) => {
    console.error('退票失败', err);
    message.error('退票失败，请稍后重试!!');
  });
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