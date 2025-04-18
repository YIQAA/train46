<script setup>
import {
  Card,
  Divider,
  Space,
  Checkbox,
  CheckboxGroup,
  Table,
  Select,
  SelectOption,
  Input,
  Button,
  Modal,
  message
} from 'ant-design-vue'
import { useRoute } from 'vue-router'
import {
  fetchTicketSearch,
  fetchBuyTicket,
  fetchUserPassenger
} from '@/service'
import { onMounted, reactive, toRaw, watch, ref } from 'vue'
import { getWeekNumber } from '@/utils'
import { UserOutlined, CloseCircleOutlined } from '@ant-design/icons-vue'
import { TICKET_TYPE_LIST, ID_CARD_TYPE } from '@/constants'
import IconFont from '@/components/IconFont.vue'
import dayjs from 'dayjs'
import { SEAT_CLASS_TYPE_LIST } from '@/constants'
import Cookie from 'js-cookie'
import router from '@/router'
const styleWidth = { width: '145px' }
const styleWidth2 = { width: '170px' }

const { query } = useRoute()

const state = reactive({
  // 当前选择的列车信息，***挂载时获取
  currTrain: null,

  // 乘客信息表格，包含票种、席别、姓名、证件等信息
  Passengers: [],
  rawPassengers: [],

  // 当前列车的座位类型列表，包含不同席别的余票信息和价格 ***挂载时获取
  currSeatList: [],

  // 控制确认订单弹窗的显示状态
  open: false,

  // 用户已选的座位位置代码（如'A','B'等）
  currSeatListCode: [],

  // 当前席别类型下所有可选座位的位置列表（动态生成）
  seatPosition: [],

  // 是否允许自选座位（当所有乘客选择相同席别时为true）
  isChooseSeat: true,

  // 左侧座位数量（用于布局计算）
  seatLeft: 3,

  // 一共座位总数（不同席别类型值*乘客数）
  seatNumber: 5,

  // 自增key生成器，确保乘客行唯一标识
  nextKey: 1,     // 用于生成唯一key

  loading: false
})
const currPassenger = ref([])


onMounted(() => {

  const username = JSON.parse(localStorage.getItem('userName'))
  console.log(username)
  // 获取用户信息
  fetchUserPassenger({
    userName: username
  }).then((res) => {
    if (res) {
      console.log("返回了用户信息")
      console.log(res)
      // 初始化乘客信息
      state.Passengers = [{
        key: state.nextKey++,
        discountType: 0, // 默认成人票
        seatType: undefined,
        realName: res.realName,
        idType: 0,          // 默认身份证类型
        idCard: res.idCard
      }]
      state.rawPassengers = toRaw(state.Passengers).map(item => ({ ...item })); // 保留原始数据副本
      console.log(JSON.parse(JSON.stringify(state.rawPassengers))); // 输出原始数据
    }
  })

  console.log( "传来的参数！！！！！")
  console.log(query)

  fetchTicketSearch({
    fromCity: query.fromCity,
    toCity: query.toCity,
    departureDate: query.departureDate
  }).then((res) => {
    if (res.trainList) {
      //获取初始车次信息
      const currTrain = res.trainList.find(
          (item) => item.trainNumber === query.trainNumber
              && item.trainId === query.trainId
              && item.departureStation === query.departureStation
              && item.arrivalStation === query.arrivalStation
      )
      console.log("过滤后的车次！！")
      console.log(currTrain)
      //获取初始座位信息
      state.currSeatList = currTrain.seatClassList
      state.currTrain = currTrain
    }
  })

})

// 监听车票列表变化，实时更新座位位置信息，以便生成弹窗中的图标
watch(
    () => state.Passengers,
    (newValue) => {
      let seatType = newValue?.length && newValue[0]?.seatType
      let seatPosition = ['A', 'B', 'C', 'D', 'F']
      let seatLeft = 3
      // 如果选择特等座，则座位位置为A、F，座位左边位置为1
      if (seatType === 1) {
        seatPosition = ['A', 'F']
        seatLeft = 1
      }
      //如果选择商务座，则座位位置为A、C、F，座位左边位置为2
      if (seatType === 2) {
        seatPosition = ['A', 'C', 'F']
        seatLeft = 2
      }
      // 如果选择一等座或软座，则座位位置为A、C、D、F，座位左边位置为2
      if (seatType === 3 || seatType === 7) {
        seatPosition = ['A', 'C', 'D', 'F']
        seatLeft = 2
      }
      // 如果选择二等座，则座位位置为A、B、C、D、F，座位左边位置为3
      if (seatType === 4) {
        seatPosition = ['A', 'B', 'C', 'D', 'F']
        seatLeft = 3
      }
      //如果选择软卧，则座位位置为上铺、下铺
      if (seatType === 5) {
        seatPosition = ['上铺', '下铺']
      }
      // 如果选择硬卧，则座位位置为上铺、中铺、下铺
      if (seatType === 6) {
        seatPosition = ['上铺', '中铺', '下铺']
      }
      if (seatType === 8 || seatType === 9) {
        state.isChooseSeat = false
        console.log("软卧和硬卧不支持自选座位！！！！"+state.isChooseSeat)
      }

      //初始数组，长度=乘客数 × 一排座位的位置数
      let seatList = new Array(
          (newValue?.length ?? 1) * (seatPosition?.length ?? 1)
      ).fill('')
      //生成['A0', 'B0', 'C0', 'D0', 'F0', 'A1', 'B1', 'C1', 'D1', 'F1']等
      seatList = seatList?.map((item, index) => {
        if (index < seatPosition?.length - 1) {
          return `${seatPosition[index]}0`
        } else {
          return `${seatPosition[index % seatPosition.length]}${Math.floor(
              index / seatPosition?.length
          )}`
        }
      })
      state.seatPosition = seatList
      state.seatLeft = seatLeft
      state.seatNumber = seatPosition?.length
    },
    {
      deep: true
    }
)

// 根据乘客列表更新车票列表
watch([() => currPassenger.value], (newValue) => {
  const list = newValue.map((item) => toRaw(item))?.flat()
  const newPassengers = state.rawPassengers.filter((item) => {
    return list.includes(item.id)
  })
  state.Passengers = newPassengers
})

// 判断是否选择同一类型座位，决定是否允许自选座位
watch(
    () => state.Passengers,
    (newValue) => {
      let isChooseSeat1 = true
      let seatType = newValue?.length && newValue[0].seatType
      newValue?.length &&
      newValue.map((item) => {
        if (item.seatType !== seatType) {
          isChooseSeat1 = false
        }
      })
      state.isChooseSeat = isChooseSeat1
    },
    { deep: true }
)

// 修改columns定义
const columns = [
  {
    title: '序号',
    dataIndex: 'key'
  },
  {
    title: '票种',
    dataIndex: 'discountType',
    slots: { customRender: 'ticketType' }
  },
  {
    title: '席别',
    dataIndex: 'seatType',
    slots: { customRender: 'seatType' }
  },
  {
    title: '姓名',
    dataIndex: 'realName',
    slots: { customRender: 'realName' }
  },
  {
    title: '证件类型',
    dataIndex: 'idType',
    slots: { customRender: 'idType' }
  },
  {
    title: '证件号码',
    dataIndex: 'idCard',
    slots: { customRender: 'idCard' }
  },
  {
    title: '操作',
    slots: { customRender: 'edit' }
  }
];
const checkColumns = [
  {
    title: '序号',
    dataIndex: 'key'
  },
  {
    title: '席别',
    dataIndex: 'seatType',
    slots: { customRender: 'seatType' }
  },
  {
    title: '票种',
    dataIndex: 'discountType',
    slots: { customRender: 'discountType' }
  },
  {
    title: '姓名',
    dataIndex: 'realName'
  },
  {
    title: '证件类型',
    dataIndex: 'idType',
    slots: { customRender: 'idType' }
  },
  {
    title: '证件号码',
    dataIndex: 'idCard'
  }
]
// 添加新的乘车人行
const addPassengerRow = () => {
  state.Passengers.push({
    key: state.nextKey++,
    discountType: undefined, // 票种
    seatType: undefined,     // 席别
    realName: '',           // 姓名
    idType: 0,            // 默认身份证
    idCard: ''              // 证件号
  });
};
// 移除行
const removePassengerRow = (key) => {
  state.Passengers = state.Passengers.filter(item => item.key !== key);
};


// 修改数据更新方法（直接操作响应式数组）
const handleChange = (key, field, value) => {
  const index = state.Passengers.findIndex(item => item.key === key)
  if (index !== -1) {
    state.Passengers[index][field] = value
  }
}

// 提交订单1
const handleSubmit = () => {

  const canNotSubmit = state.Passengers.some(
      (item) => item.seatType === null || item.seatType === undefined
  )
  if (canNotSubmit || state.Passengers?.length === 0) {
    return message.error('请补全信息')
  }
  for (let i = 0; i < state.Passengers.length; i++) {
    if (state.Passengers[i].seatType === 8 || state.Passengers[i].seatType === 9) {
      state.isChooseSeat = false
    }
  }
  state.open = true
}


// 选择座位
const handleSelectSeat = (code) => {
  if (state.currSeatListCode?.indexOf(code) !== -1) {
    state.currSeatListCode = state.currSeatListCode?.filter(
        (item) => item !== code
    )
  } else {
    if (state.currSeatListCode.length === state.Passengers.length) {
      state.currSeatListCode[state.currSeatListCode.length - 1] = code
    } else {
      state.currSeatListCode.push(code)
    }
  }
}


// 提交订单2
const handleSubmitBuyTicket = () => {
  let params = { trainId: query?.trainId ,userId:localStorage.getItem('userId')}
  const passengers = state.Passengers.map((item) => ({
    realName: item.realName,
    idType: item.idType,
    idCard: item.idCard,
    ticketType: item.discountType,
    seatType: item.seatType
  }));

  params = {
    ...params,
    passengers,
    chooseSeats: toRaw(state.currSeatListCode),
    isChooseSeat: state.isChooseSeat,
    departureStation: query?.departureStation,
    arrivalStation: query?.arrivalStation
  }
  console.log('params:::', params)
  console.log('datasourse:::', state.Passengers)
  state.loading = true

  fetchBuyTicket(params)
      .then((res) => {
        if (res) {
          console.log('res:::', res)
          console.log("软卧和硬卧不支持自选座位！！！！"+state.isChooseSeat)
          message.success('下单成功，正在跳转至订单')
          setTimeout(() => {
            router.push(`/paymentTimer?sn=${res}`)
          }, 500)
        } else {
          message.error("错误")
        }
        state.loading = false
      })
      .catch((error) => {
        state.loading = false
        console.log(error)
      })
}


</script>
<template>
  <div class="buy-ticket-wrapper">
    <Space direction="vertical" :style="{ width: '100%' }" size="large">
      <!--列车信息卡片      -->
      <Card class="card-container">
        <template #title>
          <div class="title-wrapper">
            <h1>列车信息</h1>
            <h2>（以下余票信息仅供参考）</h2>
          </div>
        </template>
        <div class="train-info">
          <span class="important-text">{{ query.departureDate }}</span>
          <span class="important-text"
            >（{{ getWeekNumber(dayjs(query.departureDate).day()) }}）</span
          >
          <span class="important-text">{{ query.trainNumber }}</span>
        </div>
        <div class="train-info">
          <span class="important-text">{{ state.currTrain?.departureStation }}</span>
          <span class="important-text"
          >({{ state.currTrain?.departureTime }}) —— </span
          >
          <span class="important-text">{{ state.currTrain?.arrivalStation }}</span>
          <span class="important-text"
          >({{ state.currTrain?.arrivalTime}})</span
          >
        </div>
        <Divider dashed></Divider>
        <div class="seat-wrapper">
          <div v-for="item in state.currSeatList">
            <span>{{
              SEAT_CLASS_TYPE_LIST.find((seat) => seat.code === item.type)
                ?.label
            }}</span>
            （ <span class="price">￥{{ item?.price }}</span
            >）
            <span>{{ item.quantity >= 1 ? '有票' : '1张票' }}</span>
          </div>
        </div>
        <div class="tip">
          *显示的卧铺票价均为上铺票价，供您参考，具体票价以您确认支付时实际购买的铺别票价为准。
        </div>
        <Divider dashed></Divider>
        <div class="info-tip">
          本次列车，<span v-for="item in state.currSeatList">
            {{
            SEAT_CLASS_TYPE_LIST.find((seat) => seat.code === item.type)
                ?.label
          }}余票{{ item.quantity }},</span
        >
        </div>
      </Card>
      <!-- 乘客信息部分的卡片 -->
      <Card class="card-container">
        <template #title>
          <div class="title-wrapper">
            <h1>乘客信息</h1>
            <h2>（请手动填写乘车人信息）</h2>
          </div>
        </template>
        <div class="user-tip">
          <UserOutlined />
          <span>乘车人</span>
          <Button
              @click="addPassengerRow"
              type="primary"
              size="small"
              style="margin-left: 20px"
          >
            + 添加
          </Button>
        </div>

        <Table
            :columns="columns"
            :data-source="state.Passengers"
            :pagination="false"
        >
          <template #ticketType="{ record }">
            <Select
                :style="styleWidth"
                :value="record.discountType"
                @select="(value) => handleChange(record.key, 'discountType', value)"
            >
              <SelectOption
                  v-for="item in TICKET_TYPE_LIST"
                  :value="item.value"
              >{{ item.label }}</SelectOption>
            </Select>
          </template>

          <template #seatType="{ record }">
            <Select
                :style="styleWidth"
                :value="record.seatType"
                @select="(value) => handleChange(record.key, 'seatType', value)"
            >
              <SelectOption
                  v-for="item in state.currSeatList"
                  :value="item.type"
              >
                {{ SEAT_CLASS_TYPE_LIST.find(s => s.code === item.type)?.label }}(￥{{ item.price }})
              </SelectOption>
            </Select>
          </template>

          <template #realName="{ record }">
            <Input
                :value="record.realName"
                @change="e => handleChange(record.key, 'realName', e.target.value)"
                :style="styleWidth"
            ></Input>
          </template>

          <template #idType="{ record }">
            <Select
                :style="styleWidth"
                :value="record.idType"
                @select="(value) => handleChange(record.key, 'idType', value)"
            >
              <SelectOption v-for="item in ID_CARD_TYPE" :value="item.value">
                {{ item.label }}
              </SelectOption>
            </Select>
          </template>

          <template #idCard="{ record }">
            <Input
                :value="record.idCard"
                @change="e => handleChange(record.key, 'idCard', e.target.value)"
                :style="styleWidth2"
            ></Input>
          </template>

          <template #edit="{ record }">
            <CloseCircleOutlined
                :style="{ color: '#fd6a09' }"
                @click="() => removePassengerRow(record.key)"
            />
          </template>
        </Table>
      </Card>
      <div>
        <span>提交订单表示已阅读并同意</span>
        <span
          ><a href="">《国铁集团铁路旅客运输规程》</a
          ><a href="">《服务条款》</a></span
        >
      </div>
      <div :style="{ width: '100%', textAlign: 'center' }">
        <Space size="large">
          <Button @click="() => router.push('/ticketSearch')">上一步</Button>
          <Button class="submit-btn" @click="() => {handleSubmit()}">提交订单</Button>
        </Space>
      </div>
<!--      温馨提示-->
      <div class="tips-txt">
        <h2>温馨提示：</h2>
        <p>
          1.
          一张有效身份证件同一乘车日期同一车次只能购买一张车票，高铁动卧列车除外。
        </p>
        <p>
          2.
          购买儿童票时，乘车儿童有有效身份证件的，请填写本人有效身份证件信息。自2023年1月1日起，每一名持票成年人旅客可免费携带一名未满6周岁且不单独占用席位的儿童乘车，超过一名时，超过人数应购买儿童优惠票。免费儿童可以在购票成功后添加。
        </p>
        <p>
          3.
          购买残疾军人（伤残警察）优待票的，须在购票后、开车前办理换票手续方可进站乘车。换票时，不符合规定的减价优待条件，没有有效"中华人民共和国残疾军人证"或"中华人民共和国伤残人民警察证"的，不予换票，所购车票按规定办理退票手续。
        </p>
        <p>
          4.
          一天内3次申请车票成功后取消订单（包含无座票时取消5次计为取消1次），当日将不能在12306继续购票。
        </p>
        <p>
          <strong
            >5.
            购买铁路乘意险的注册用户年龄须在18周岁以上，使用非中国居民身份证注册的用户如购买铁路乘意险，须在<a
              href="../view/information.html"
              shape="rect"
              >我的12306——个人信息</a
            >
            如实填写“出生日期”。</strong
          >
        </p>
        <p>
          <strong
            >6.
            父母为未成年子女投保，须在<a
              href="../view/passengers.html"
              shape="rect"
              >我的乘车人</a
            >
            登记未成年子女的有效身份证件信息。</strong
          >
        </p>
        <p>7.
          未尽事宜详见《铁路旅客运输规程》等有关规定和车站公告。</p>
        <p name="xjky" style="display: none">
          8.
          为确保乘客在旅途中有一个安全、舒适的乘坐环境，自2020年11月17日起，<span
            style="color: red"
            >旅客不得随身携带长宽高之和大于130厘米的雪具乘车</span
          >
          。您可选择雪具快运服务，请提前1-2天选择雪具快运“门到站”或“站到站”服务，中铁快运提供雪具到站后3日免费保管，请您根据出行时间，提前咨询和办理。中铁快运客服热线：95572<br
            clear="none"
          />
        </p>
      </div>
    </Space>
<!--    弹框-->
    <Modal
      :visible="state.open"
      title="请核对以下信息"
      wrap-class-name="check-info-wrapper"
      width="40%"
      @cancel="state.open = false"
      :footer="null"
    >
      <Space direction="vertical" :style="{ width: '100%' }">
<!--        列车信息-->
        <div class="train-info">
          <span class="important-text">{{ query.departureDate }}</span>
          <span class="important-text"
          >（{{ getWeekNumber(dayjs(query.departureDate).day()) }}）</span
          >
          <span class="important-text">{{ query.trainNumber }}</span>
        </div>
        <div class="train-info">
          <span class="important-text">{{ state.currTrain?.departureStation }}</span>
          <span class="important-text"
          >({{ state.currTrain?.departureTime }}) —— </span
          >
          <span class="important-text">{{ state.currTrain?.arrivalStation }}</span>
          <span class="important-text"
          >({{ state.currTrain?.arrivalTime}})</span
          >
        </div>
<!--        乘客列表-->
        <Table
          :columns="checkColumns"
          :data-source="state.Passengers"
          :pagination="false"
        >
          <template #seatType="{ text, record }">
            <span>
              {{
                SEAT_CLASS_TYPE_LIST.find((seat) => seat.code === text)?.label
              }}
            </span>
          </template>
          <template #discountType="{ text }">
            {{ TICKET_TYPE_LIST.find((item) => item.value === text)?.label }}
          </template>
          <template #idType="{ text }">
            {{ ID_CARD_TYPE.find((item) => item.value === text)?.label }}
          </template>
        </Table>
<!--        选座服务-->
        <div
          v-if="
            state.isChooseSeat &&
            !(state.Passengers?.length > 5) &&
            state.Passengers?.length <= state.seatPosition.length
          "
        >
          <div class="seat-choose-wrapper">
            <div>
              <div class="tip">选座服务</div>
              <div>已选座{{ state.currSeatListCode.length }}/{{state.Passengers.length}}</div>
            </div>
<!--            选座图标-->
            <div>
              <div v-for="(item, index) in state.Passengers?.slice(0, 2)">
                <div class="action-wrapper">
                  <div>窗</div>
                  <Divider type="vertical"></Divider>
                  <div>
                    <div
                      class="seat-img"
                      v-for="(item, index, length) in state.seatPosition
                        .slice(
                          0 + index * state.seatNumber,
                          (1 + index) * state.seatNumber
                        )
                        .slice(0, state.seatLeft)"
                      @click="() => handleSelectSeat(item)"
                      :class="{
                        cur: state.currSeatListCode?.indexOf(item) !== -1
                      }"
                    >
                      {{ item.slice(0, 1) }}
                    </div>
                    <Divider type="vertical"></Divider>
                  </div>
                  <div>过道</div>
                  <Divider type="vertical"></Divider>
                  <div>
                    <div
                      class="seat-img"
                      v-for="(item, index, length) in state.seatPosition
                        .slice(
                          0 + index * state.seatNumber,
                          (1 + index) * state.seatNumber
                        )
                        .slice(state.seatLeft, state.seatNumber)"
                      @click="() => handleSelectSeat(item)"
                      :class="{
                        cur: state.currSeatListCode?.indexOf(item) !== -1
                      }"
                    >
                      {{ item.slice(0, 1) }}
                    </div>
                    <Divider type="vertical"></Divider>
                  </div>
                  <div>窗</div>
                </div>
              </div>
            </div>
          </div>
          <a href=""
          >*如果本次列车剩余席位无法满足您的选座需求，系统将自动为您分配席位</a
          >
        </div>
        <div v-else>
          <a href="">*系统将随机为您申请席位，暂不支持自选席位</a>
        </div>

        <Space
          size="large"
          :style="{
            width: '100%',
            textAlign: 'center',
            justifyContent: 'center'
          }"
        >
          <Button @click="state.open = false">返回修改</Button>
          <Button
            :loading="state.loading"
            :style="{
              backgroundColor: '#ff8001',
              color: '#fff',
              border: 'none'
            }"
            @click="handleSubmitBuyTicket"
          >
            确认
          </Button>
        </Space>
      </Space>
    </Modal>
  </div>
</template>

<style lang="scss" scoped>
.card-container {
  border: 1px solid #1890ff;
  background: #fff;
  overflow: hidden;
  padding: 0;
  border-radius: 4px;
}

.buy-ticket-wrapper {
  .title-wrapper {
    display: flex;
    height: 20px;
    line-height: 20px;
    h1 {
      font-size: 16px;
      margin: 0;
      color: #fff;
    }
    h2 {
      font-size: 14px;
      margin: 0;
      color: #fff;
    }
  }
  .important-text {
    font-size: 16px;
    font-weight: bolder;
    // margin-right: 5px;
  }
  .seat-wrapper {
    display: flex;
    margin-bottom: 10px;
    > div {
      margin-right: 10px;
      .price {
        color: #f57531;
      }
    }
  }
  .tip {
    color: #1e71bd;
  }
  .user-tip {
    .anticon-user {
      color: #1e71bd;
      font-size: 20px;
      margin-right: 5px;
    }
  }
  .check-wrapper {
    padding: 5px 10px;
  }
  .submit-btn {
    background-color: #fd6a09;
    color: #fff;
  }
  .tips-txt {
    background: #fffbe5;
    border: 1px solid #fbd800;
    padding: 5px;
  }
}
::v-deep {
  .ant-card-head {
    background-color: #3A9DD8FF;
  }
  .ant-card-body {
    background-color: #ebedf6;
    padding: 10px 15px;
  }
  .ant-divider-dashed {
    border-color: #d1d3da;
  }
  .ant-divider-horizontal {
    margin: 10px 0;
  }
  .ant-table-thead {
    .ant-table-cell {
      background: #f3f3f3 !important;
      background-color: #f3f3f3 !important;
    }
    > tr > th {
      padding: 8px;
    }
  }
  .check-info-wrapper {
    .ant-modal-header {
      background-color: #2386c8;
    }
  }
}
.check-info-wrapper {
  .important-text {
    font-size: 16px;
    font-weight: bolder;
  }
  .info-tip {
    color: #909090;
    span {
      font-size: 18px;
      color: #db0000;
      font-weight: bolder;
    }
  }
}
.seat-choose-wrapper {
  display: flex;
  width: 100%;
  background-color: #f3f3f3;
  padding: 20px;
  .tip {
    color: #ff8001;
    margin-right: 40px;
  }
  .action-wrapper {
    display: flex;
    align-items: center;

    padding: 20px;
    .seat-img {
      display: inline-block;
      text-align: center;
      width: 30px;
      height: 28px;
      line-height: 25px;
      background: url(https://kyfw.12306.cn/otn/resources/images/bg017.png)
        no-repeat;
      color: #fff;
      margin: 0 5px;
      cursor: pointer;
      background-position: -80px 0;
    }
    .cur {
      background-position: -40px 0;
    }
  }
}

/* ：通过 margin 添加间隔 */
.train-info {
  display: flex;
  align-items: center;
  gap: 8px; /* 控制元素间距 */
  flex-wrap: wrap; /* 允许内容换行 */
}
</style>
