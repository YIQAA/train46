<script setup>
import {reactive, toRaw, ref, h, watch, onMounted,nextTick } from 'vue'
import {
  Card,
  Form,
  FormItem,
  Space,
  Select,
  Button,
  TabPane,
  Tabs,
  DatePicker,
  Table,
  Checkbox,
  CheckboxGroup,
  CheckableTag,
  Row,
  Col,
  Alert,
  Tooltip,
  message
} from 'ant-design-vue'
import {RetweetOutlined, SwapRightOutlined} from '@ant-design/icons-vue'
import dayjs from 'dayjs'
import {getWeekNumber, getTicketNumber} from '@/utils'
import {
  fetchTicketSearch,
  fetchStationAll,
  fetchTrainStation
} from '@/service/index'
import {SEAT_CLASS_TYPE_LIST, TRAIN_BRAND_LIST} from '@/constants'
import router from "@/router/index.js";

/* 使用Vue的响应式表单hook */
const useForm = Form.useForm

// headSearch：定义了表单的输入数据，包括出发地、目的地、出发日期等。
/* 查询表单的响应式数据对象 */
const headSearch = reactive({
  fromCity: 'BJ', // 默认出发城市
  toCity: 'SH',   // 默认到达城市
  departureDate: dayjs(),  // 出发日期
  train_type: [],     // 车次类型
  departure: [],      // 出发站点
  arrival: [],        // 到达地点
  seatClass: []       // 座位类型
})

/* 应用状态管理 */
const state = reactive({
  seatClassTypeListSelect: [1,2,3,4,5,6,7,8,9],// 可选座位类型列表  ****删掉
  trainBrandListSelect: [1,2],   // 可选车次类型列表  ****删掉
  trainList: [],                // 当前显示的车次列表
  stationList: [],              // 所有城市列表
  trainStationList: [],         // 车次详细经停站信息
  loading: false                // 加载状态
})

/* 原始车次数据（用于过滤操作） */
const rowState = reactive({
  rawTrainList: []
})

// 监听出发日期变化，实时请求数据
watch(
    () => headSearch.departureDate,
    (newDate) => {
      const {fromCity, toCity} = toRaw(headSearch)
      const dateString = newDate.format('YYYY-MM-DD')

      console.log("日期变化，新日期"+dateString) // 示例：用格式化后的字符串请求数据
      state.loading = true // 开启加载状态
      fetchTicketSearch({
        fromCity,
        toCity,
        departureDate: dateString
      }).then((res) => {
        if (res.trainList) {
          // 更新车次列表和原始数据
          state.trainList = res.trainList?.map((item) => ({
            ...item,
            key: item.trainId
          }))
          rowState.rawTrainList = res.trainList?.map((item) => ({
            ...item,
            key: item.trainId
          }))
        }
      }).finally(() => {
        state.loading = false // 关闭加载状态
      })
    }
)

/* 监听查询条件变化，实时过滤车次列表 */
watch(headSearch, (newValue) => {
  // 每次过滤都从原始数据开始
  state.trainList = rowState.rawTrainList
  console.log("监听生效！！！！")
  // 多条件链式过滤
  // 如果有车次类型选择，则进行过滤
  if (newValue?.train_type?.length) {
    console.log("有车次类型选择，进行过滤")
    state.trainList = state.trainList.filter((item) => {
      return newValue?.train_type?.includes(item.trainType)
    })
  }
  // 如果有出发地选择，则进行过滤
  if (newValue?.departure?.length) {
    console.log("有出发地选择，进行过滤")
    state.trainList = state.trainList.filter((item) => {
      return newValue?.departure?.includes(item.departureStation)
    })
  }
  // 如果有到达地选择，则进行过滤
  if (newValue?.arrival?.length) {
    console.log("有到达地选择，进行过滤")
    state.trainList = state.trainList.filter((item) => {
      return newValue?.arrival?.includes(item.arrivalStation)
    })
  }
  // 如果有座位类型选择，则进行过滤
  if (newValue?.seatClass?.length) {
    console.log("有座位类型选择，进行过滤")
    state.trainList = state.trainList.filter((item) => {
      const list =
          item.seatClassList?.filter(
              (item) => newValue.seatClass.includes(item.type) && item.quantity
          ) ?? []
      return list.length
    })
  }
})


/* 当前可选车站列表 */
const OptionalDepartureStationList = ref([])      // 出发站列表
const OptionalArrivalStationList = ref([])   // 到达站列表

/* 全选标签状态控制 */
const checkTagCardType = ref(false)   // 车次类型全选
const departureTagAll = ref(false)    // 出发站全选
const arrivalTagAll = ref(false)      // 到达站全选
const seatTagAll = ref(false)         // 座位类型全选

// 定义表格列配置
// columns：车次查询结果的主表格，用于展示车次信息。
// cardInfoColumns：车次详情Tooltip中的经停站表格，经停站信息列配置

/* 车次信息列 */
const columns = [
  {
    /* 车次信息列 */
    title: '车次',
    dataIndex: 'trainNumber',
    slots: {
      customRender: 'trainNumber'
    },
    key: 'trainNumber',
    width: 90,
    align: 'center',
    resizable: false
  },

  /* 车站信息列（复合列） */
  {
    key: 'station',
    slots: {
      title: 'customStaionTitle',
      customRender: 'station'
    },
    width: 100,
    ellipsis: true,
    align: 'left'
  },

  {
    dataIndex: 'time',
    key: 'time',
    slots: {title: 'customTimeTitle', customRender: 'time'},
    width: 100,
    ellipsis: true,
    resizable: false,
    align: 'center'
  },
  {
    title: '历时',
    dataIndex: 'duration',
    key: 'duration',
    width: 120,
    ellipsis: true,
    resizable: false,
    align: 'center'
  },
  {
    title: '特等座',
    dataIndex: 'seatClassList',
    key: 'seat1',
    slots: { customRender: 'seat1'},
    width: 80,
    ellipsis: true,
    resizable: false,
    align: 'center'
  },
  {
    title: '商务座',
    dataIndex: 'seatClassList',
    key: 'seat2',
    slots: { customRender: 'seat2'},
    width: 80,
    ellipsis: true,
    resizable: false,
    align: 'center'
  },
  {
    title: '一等座',
    dataIndex: 'seatClassList',
    slots: {customRender: 'seat3'},
    key: 'first_seat',
    width: 80,
    ellipsis: true,
    resizable: false,
    align: 'center'
  },
  {
    title: '二等座',
    dataIndex: 'seatClassList',
    key: 'second_seat',
    slots: { customRender: 'seat4'},
    width: 80,
    ellipsis: true,
    resizable: false,
    align: 'center'
  },
  {
    // 软卧，一等卧
    dataIndex: 'seatClassList',
    key: 'first_bed',
    slots: {title: 'customFirstBedSeatTitle', customRender: 'seat5'},
    width: 80,
    ellipsis: true,
    resizable: false,
    align: 'center'
  },
  {
    //硬卧，二等卧
    dataIndex: 'seatClassList',
    key: 'scond_hard_bed',
    slots: {title: 'customScondHardSeadTitle', customRender: 'seat6'},
    width: 80,
    ellipsis: true,
    resizable: false,
    align: 'center'
  },
  {
    title: '软座',
    dataIndex: 'seatClassList',
    key: 'first_soft_seat',
    slots: {customRender: 'seat7'},
    width: 80,
    ellipsis: true,
    resizable: false,
    align: 'center'
  },
  {
    title: '硬座',
    dataIndex: 'seatClassList',
    key: 'hard_seat',
    slots: {customRender: 'seat8'},
    width: 80,
    ellipsis: true,
    resizable: false,
    align: 'center'
  },
  {
    title: '无座',
    dataIndex: 'seatClassList',
    key: 'no_seat',
    slots: {customRender: 'seat9'},
    width: 80,
    ellipsis: true,
    resizable: false,
    align: 'center'
  },
  {
    title: '备注',
    dataIndex: 'remark',
    slots: {customRender: 'operation'},
    align: 'center'

  }
]

const cardInfoColumns = [
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
  }
]

/* 表单验证处理 */
const {resetFields, validate} = useForm(headSearch)

//days：生成一个包含未来 15 天日期的数组。
const days = new Array(15)
    .fill(',')
    .map((item, index) => dayjs().add(index, 'days'))

//*****************************************************************************
const handSubmit = () => {
  validate().then(() => {
    const {fromCity, toCity, departureDate} = toRaw(headSearch)
    state.loading = true // 开启加载状态
    // 调用API获取车次数据
    fetchTicketSearch({
      /* 参数处理 */
      fromCity,
      toCity,
      departureDate: departureDate.format('YYYY-MM-DD')
    }).then((res) => {
      //如果响应失败，则显示错误信息
      if (!res) console.log('获取车次数据失败')
      //如果响应成功，则更新车次列表和原始车次列表
      if (res.trainList) {
        // 更新车次列表和原始数据
        state.trainList = res.trainList?.map((item) => ({
          ...item,
          key: item.trainId
        }))
        rowState.rawTrainList = res.trainList?.map((item) => ({
          ...item,
          key: item.trainId
        }))
      }
      // 更新筛选选项数据
      OptionalDepartureStationList.value = res.departureStationList
      OptionalArrivalStationList.value = res.arrivalStationList
      console.log("点击提交返回了")
      console.log(res)
    }).finally(() => {
      state.loading = false // 关闭加载状态
    })
  })
}

const getTitle = (item) => {
  const key = item.format('MM-DD')
  if (key === headSearch.departureDate.format('MM-DD')) {
    return key + '　' + getWeekNumber(item.day())
  } else {
    return key
  }
}

/* 初始化获取所有车站 */
const getStationAll = () => {
  fetchStationAll().then((res) => {
    state.stationList = res
    console.log("获取所有城市----------------")
    console.log(res)
  })
}

const handlePriceShow = (price) => {
  if (price) {
    return '￥' + price
  } else {
    return ''
  }
}

/* 组件挂载时初始化数据 */
onMounted(() => {
  console.log("挂载初始！！！！！！！！！！！！！！")
  // 初始查询默认车次
  fetchTicketSearch({
    fromCity: headSearch.fromCity,
    toCity: headSearch.toCity,
    departureDate: dayjs(new Date()).format('YYYY-MM-DD')
  }).then((res) => {
    console.log("返回数据")
    if (!res)
    {return message.error(res.message)}
    console.log(res.trainList)
    if (res.trainList) {
      //如果响应成功，则更新车次列表和原始车次列表
      console.log("返回成功")
      state.trainList = res.trainList?.map((item) => ({
        ...item,
        key: item.trainId
      }))
      // 更新原始数据
      rowState.rawTrainList = res.trainList?.map((item) => ({
        ...item,
        key: item.trainId
      }))
    }
    console.log("挂载成功！！！！！！！！！！！！！！")
    console.log(res.departureStationList)
    OptionalDepartureStationList.value = res.departureStationList
    OptionalArrivalStationList.value = res.arrivalStationList
  })
  getStationAll()// 获取所有车站
})

/* 交换出发/到达站 */
const exchangeCity = () => {
  const [a, b] = [toRaw(headSearch.fromCity), toRaw(headSearch.toCity)]
  console.log(a, b)

  headSearch.fromCity = b
  headSearch.toCity = a
}
/* 获取车次详细经停站信息 */
const handleTrainClick = (trainId) => {
  console.log("获取车次详细经停站信息")
  console.log(trainId)
  fetchTrainStation({trainId}).then((res) => {
    console.log(res)
    state.trainStationList = res
    state.loading = false
  })
}
/* 处理车次预订 */
const handleBook = (record) => {
  // 1. 处理表单参数
  const searchParams = Object.entries(headSearch)
      .filter(([_, v]) => v != null) // 过滤空值
      .map(([key, value]) => {
        // 处理日期格式化
        const processedValue = key === 'departureDate'
            ? value?.format?.('YYYY-MM-DD') ?? value
            : value;
        return `${encodeURIComponent(key)}=${encodeURIComponent(processedValue)}`;
      })
      .join('&');

  // 2. 合并车次参数
  const params = new URLSearchParams(searchParams);
  params.append('trainNumber', record.trainNumber);
  params.append('trainId', record.trainId);
  params.append('departureStation', record.departureStation);
  params.append('arrivalStation', record.arrivalStation);

  setTimeout(() => {
    router.push(`buyTicket?${params.toString()}`)
  }, 500)
  // 3. 打开新窗口
  // window.open(`buyTicket?${params.toString()}`);
};

// 点击标签页时间
const handleTabChange = (tabKey) => {
  const year = dayjs().format('YYYY')
  const newDate = dayjs(`${year}-${tabKey}`, 'YYYY-MM-DD')

  // 更新 headSearch
  headSearch.departureDate = newDate
}

</script>

<template>
  <div>
    <!-- 搜索表单区域 -->
    <Space direction="vertical" size="middle" :style="{ width: '100%' }">
      <!-- 主查询表单 -->
      <Form :form="useForm" layout="inline" :colon="false" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
        <Space direction="vertical" size="middle" :style="{ width: '100%' }">

          <!-- 第一个查询条件卡片   出发地目的地时间-->
          <Card :style="{ width: '100%' }" class="card-container">
            <Row :style="{ maxWidth: '1150px' }">

              <!-- 出发地选择 -->
              <Col :span="5">
                <FormItem label="出发地" name="fromCity">
                  <Select
                      v-model:value="headSearch.fromCity"
                      :style="{ width: '150px' }"
                      :show-arrow="false"
                      :show-search="true"
                      :options="state.stationList.map((item) => ({ label: item.name, value: item.code }))"
                  />
                </FormItem>
              </Col>

              <!-- 城市交换按钮 -->
              <Col :span="2" :style="{ display: 'flex' }">
                <div :style="{ width: '100%', textAlign: 'center', alignItems: 'center', lineHeight: '32px' }">
                  <RetweetOutlined
                      @click="exchangeCity"
                      :style="{ cursor: 'pointer', fontSize: '20px', color: '#1990ff' }"
                  />
                </div>
              </Col>

              <!-- 目的地选择 -->
              <Col :span="5">
                <FormItem label="目的地" name="toCity">
                  <Select
                      v-model:value="headSearch.toCity"
                      :style="{ width: '150px' }"
                      :show-arrow="false"
                      :show-search="true"
                      :options="state.stationList.map((item) => ({ label: item.name, value: item.code }))"
                  />
                </FormItem>
              </Col>

              <!-- 日期选择 -->
              <Col :span="5">
                <FormItem label="出发日" name="departureDate">
                  <DatePicker
                      v-model:value="headSearch.departureDate"
                      :style="{ width: '150px' }"
                      :show-arrow="false"
                      :allow-clear="false"
                  />
                </FormItem>
              </Col>

              <!-- 查询按钮 -->
              <Col span="2">
                <FormItem>
                  <Button type="primary" @click="handSubmit">查询</Button>
                </FormItem>
              </Col>
            </Row>
          </Card>

          <!-- 第二个车次筛选条件卡片   时间车次类型 车站 座位类型 -->
          <div class="card-container">
            <!-- 标签页容器（按日期分页），默认激活当天标签，@change标签切换事件处理 -->
            <Tabs
                type="card"
                :default-active-key="headSearch.departureDate.format('MM-DD')"
                :key="headSearch.departureDate.format('MM-DD')"
                @change="handleTabChange"
            >
              <!-- 循环生成日期标签页 -->
              <TabPane
                  v-for="item in days"
                  :key="item.format('MM-DD')"
                  :tab="getTitle(item)"
              >
                <!-- 筛选条件卡片容器 -->
                <Card flex :bordered="false">
                  <Row justify="space">
                    <!-- 左侧筛选条件区域（占20列） -->
                    <Col :span="20" flex>

                      <!--
                        筛选条件结构说明（车次/出发站/到达站/席别等）：
                        1. 每个条件都包含类似的FormItem结构
                        2. 使用CheckboxGroup管理多选状态
                        3. 包含全选标签和动态生成的选项
                        4. 数据绑定方式与车次类型类似，具体数据源不同
                      -->

                      <!-- 车次类型筛选表单项 -->
                      <FormItem>
                        <template v-slot:label>
                          <span class="spacial-label">车次类型</span>
                        </template>

                        <!-- 多选框组容器 -->
                        <CheckboxGroup
                            :value="headSearch.train_type"
                            @change=" (value) => {
                              // 当且仅当：当前选中的选项数量 == 总选项数量时，全选按钮选中，否则取消全选
                              checkTagCardType = value.length === state.trainBrandListSelect.length
                              headSearch.train_type = value
                            }"
                        >
                          <!-- 全选，标签组件 -->
                          <CheckableTag
                              :checked="checkTagCardType"
                              @change="
                              (value) => {
                                checkTagCardType = value
                                if (value) {
                                  headSearch.train_type =
                                    state.trainBrandListSelect
                                } else {
                                  headSearch.train_type = []
                                }
                              }"
                              :style="{ border: '1px solid #f0f0f0', marginRight: '10px' }"
                          >全部
                          </CheckableTag>

                          <!-- 动态生成车次类型选项 ，，遍历可用车次类型，获取类型编码-->
                          <Checkbox
                              v-for="seatItem in state.trainBrandListSelect"
                              :value="TRAIN_BRAND_LIST.find((item) => item.code === seatItem)?.code"
                          >
                            <!-- 显示车次类型标签 -->
                            {{ TRAIN_BRAND_LIST.find((item) => item.code === seatItem)?.label }}
                          </Checkbox>
                        </CheckboxGroup>
                      </FormItem>

                      <!-- 出发车站筛选表单项 -->
                      <FormItem>
                        <template v-slot:label>
                          <span class="spacial-label">出发车站</span>
                        </template>
                        <CheckboxGroup
                            :value="headSearch.departure"
                            @change="
                            (value) => {
                              departureTagAll =
                                value.length === OptionalDepartureStationList.length
                              headSearch.departure = value
                            }
                          "
                        >
                          <CheckableTag
                              :style="{
                              border: '1px solid #f0f0f0',
                              marginRight: '10px'
                            }"
                              :checked="departureTagAll"
                              @change="
                              (value) => {
                                departureTagAll = value
                                if (value) {
                                  headSearch.departure = OptionalDepartureStationList
                                } else {
                                  headSearch.departure = []
                                }
                              }
                            "
                          >全部
                          </CheckableTag
                          >
                          <Checkbox
                              v-for="item in OptionalDepartureStationList"
                              :value="item"
                          >{{ item }}
                          </Checkbox
                          >
                        </CheckboxGroup>
                      </FormItem>

                      <!-- 到达车站筛选表单项 -->
                      <FormItem>
                        <template v-slot:label>
                          <span class="spacial-label">到达车站</span>
                        </template>
                        <CheckboxGroup
                            :value="headSearch.arrival"
                            @change="
                            (value) => {
                              arrivalTagAll =
                                value.length === OptionalArrivalStationList.length
                              headSearch.arrival = value
                            }
                          "
                        >
                          <CheckableTag
                              :checked="arrivalTagAll"
                              :style="{
                              border: '1px solid #f0f0f0',
                              marginRight: '10px'
                            }"
                              @change="
                              (value) => {
                                arrivalTagAll = value
                                if (value) {
                                  headSearch.arrival = OptionalArrivalStationList
                                } else {
                                  headSearch.arrival = []
                                }
                              }
                            "
                          >全部
                          </CheckableTag
                          >
                          <Checkbox
                              v-for="item in OptionalArrivalStationList"
                              :value="item"
                          >{{ item }}
                          </Checkbox
                          >
                        </CheckboxGroup>
                      </FormItem>

                      <!-- 车次席别筛选表单项 -->
                      <FormItem>
                        <template v-slot:label>
                          <span class="spacial-label">车次席别</span>
                        </template>
                        <CheckboxGroup
                            :style="{ minWidth: '300px', textWrap: 'nowrap' }"
                            :value="headSearch.seatClass"
                            @change="
                            (value) => {
                              seatTagAll =
                                value.length ===
                                state.seatClassTypeListSelect.length
                              headSearch.seatClass = value
                            }
                          "
                        >
                          <CheckableTag
                              :checked="seatTagAll"
                              @change="
                              (value) => {
                                seatTagAll = value
                                if (value) {
                                  headSearch.seatClass =
                                    state.seatClassTypeListSelect
                                } else {
                                  headSearch.seatClass = []
                                }
                              }
                            "
                              :style="{
                              border: '1px solid #f0f0f0',
                              marginRight: '10px'
                            }"
                          >全部
                          </CheckableTag
                          >
<!--                          如果显示的席别有数据，就显示，没有就显示全部-->
                          <Checkbox
                              v-for="seatItem in state.seatClassTypeListSelect"
                              :value="
                              SEAT_CLASS_TYPE_LIST.find(
                                (item) => item.code === seatItem
                              )?.code
                            "
                          >{{
                              SEAT_CLASS_TYPE_LIST.find(
                                  (item) => item.code === seatItem
                              )?.label
                            }}
                          </Checkbox
                          >
                        </CheckboxGroup>
                      </FormItem>
                    </Col>

                  </Row>
                </Card>
              </TabPane>
            </Tabs>
          </div>
        </Space>
      </Form>

      <!-- 路线信息展示区块 -->
      <Row>
        <Col :span="12">
          <!-- 出发城市 -->
          <span class="city-name">
            {{
              state.stationList.find(
                  (item) => item.code === headSearch.fromCity
              )?.name
            }}
          </span>

          <!-- 箭头图标分隔 -->
          <SwapRightOutlined/>

          <!-- 到达城市 -->
          <span class="city-name">
            {{
              state.stationList.find(
                  (item) => item.code === headSearch.toCity
              )?.name
            }}
          </span>


          <!-- 日期信息展示 -->
          <span class="date-info">
            （
                <!-- 格式化显示日期 MM月DD日 -->
            <span class="time-title">
              {{ headSearch.departureDate.format('MM月DD日') }}
            </span>

                <!-- 显示星期几 -->
            <span class="time-title">
              {{ getWeekNumber(headSearch.departureDate.day()) }}
            </span>

            ）
          </span>

          <!-- 车次统计 -->
          <span class="train-count">
            共计
            <span :style="{ fontWeight: 'bolder' }">
              {{ state.trainList?.length || 0 }}  <!-- 安全取值处理 -->
            </span>
            个车次
          </span>
        </Col>
      </Row>

      <!-- ***车次列表信息表格*** *******************************************************************************************-->
      <div class="custome-table">
        <Table :columns="columns" :data-source="state.trainList"
               :rowKey="record => `${record.trainId}-${record.departureTime}`">

          <!-- 车次号及标签显示 -->
          <template #trainNumber="{ text, record }">
            <h1 class="card-name" @click="handleTrainClick(record.trainId)">
              <Tooltip :get-popup-container="(node) => node.parentNode" placement="rightTop" trigger="click">
                <!-- 车次号 -->
                <div>{{ text }}</div>
                <!-- 车站信息弹窗 -->
                <template #title>
                  <Table
                      :columns="cardInfoColumns"
                      :data-source="state.trainStationList"
                      :pagination="false"
                      :loading="state.loading"
                  />
                </template>
              </Tooltip>
            </h1>
          </template>

          <!-- 车站信息列 -->
          <template #station="{  record }">
            <div>
              <span
                  :style="{
                  display: 'inline-block',
                  padding: '2px 4px',
                  backgroundColor: record.departureFlag ? '#cca567' : '#7597d5',
                  color: '#fff',
                  borderRadius: '4px',
                  fontSize: '10px',
                  fontWeight: 'bolder',
                  marginRight: '2px'
                }"
              >{{ record.departureFlag ? '始' : '过' }}</span
              >{{ record.departureStation }}
            </div>
            <div>
              <span
                  :style="{
                  display: 'inline-block',
                  padding: '2px 4px',
                  backgroundColor: record.arrivalFlag ? '#6da77f' : '#7597d5',
                  color: '#fff',
                  borderRadius: '4px',
                  fontSize: '10px',
                  fontWeight: 'bolder',
                  marginRight: '2px'
                }"
              >{{ record.arrivalFlag ? '终' : '过' }}</span
              >{{ record.arrivalStation }}
            </div>
          </template>

          <template #customStaionTitle>
            <div>出发站</div>
            <div>到达站</div>
          </template>
          <template #time="{ _, record }">
            <div>{{ record.departureTime }}</div>
            <div>{{ record.arrivalTime }}</div>
          </template>


          <template #seat1="{ text }">
            <div
                :style="{
                color: getTicketNumber(
                  text?.find((item) => item?.type === 1)?.quantity
                )?.color
              }"
            >
              {{
                getTicketNumber(
                    text?.find((item) => item?.type === 1)?.quantity
                )?.label ??
                '--'
              }}
            </div>
            <div :style="{ color: '#fc8302' }">
              {{
                handlePriceShow(
                    text?.find((item) => item?.type === 1)?.price
                )
              }}
            </div>
          </template>
          <template #seat2="{ text }">
            <div
                :style="{
                color: getTicketNumber(
                  text?.find((item) => item?.type === 2)?.quantity
                )?.color
              }"
            >
              {{
                getTicketNumber(
                    text?.find((item) => item?.type === 2)?.quantity
                )?.label ??
                '--'
              }}
            </div>
            <div :style="{ color: '#fc8302' }">
              {{
                handlePriceShow(
                    text?.find((item) => item?.type === 2)?.price
                )
              }}
            </div>
          </template>
          <template #seat3="{ text }">
            <div
                :style="{
                color: getTicketNumber(
                  text?.find((item) => item?.type === 3)?.quantity
                )?.color
              }"
            >
              {{
                getTicketNumber(
                    text?.find((item) => item?.type === 3)?.quantity
                )?.label ?? '--'
              }}
            </div>
            <div :style="{ color: '#fc8302' }">
              {{
                handlePriceShow(
                    text?.find((item) => item?.type === 3)?.price
                )
              }}
            </div>
          </template>
          <template #seat4="{ text }">
            <div
                :style="{
                color: getTicketNumber(
                  text?.find((item) => item?.type === 4)?.quantity

                )?.color
              }"
            >
              {{
                getTicketNumber(
                    text?.find((item) => item?.type === 4)?.quantity
                )?.label ??
                '--'
              }}
            </div>
            <div :style="{ color: '#fc8302' }">
              {{
                handlePriceShow(
                    text?.find((item) => item?.type === 4)?.price
                )
              }}
            </div>
          </template>
          <template #seat5="{ text }">
            <div
                :style="{
                color: getTicketNumber(
                  text?.find((item) => item?.type === 5)?.quantity
                )?.color
              }"
            >
              {{
                getTicketNumber(
                    text?.find((item) => item?.type === 5)?.quantity
                )?.label ??
                '--'
              }}
            </div>
            <div :style="{ color: '#fc8302' }">
              {{
                handlePriceShow(
                    text?.find((item) => item?.type === 5)?.price
                )
              }}
            </div>
          </template>
          <template #seat6="{ text }">
            <div
                :style="{
                color: getTicketNumber(
                  text?.find((item) => item?.type === 6)?.quantity
                )?.color
              }"
            >
              {{
                getTicketNumber(
                    text?.find((item) => item?.type === 6)?.quantity
                )?.label ??
                '--'
              }}
            </div>
            <div :style="{ color: '#fc8302' }">
              {{
                handlePriceShow(
                    text?.find((item) => item?.type === 6)?.price
                )
              }}
            </div>
          </template>
          <template #seat7="{ text }">
            <div
                :style="{
                color: getTicketNumber(
                  text?.find((item) => item?.type === 7)?.quantity
                )?.color
              }"
            >
              {{
                getTicketNumber(
                    text?.find((item) => item?.type === 7)?.quantity
                )?.label ?? '--'
              }}
            </div>
            <div :style="{ color: '#fc8302' }">
              {{
                handlePriceShow(
                    text?.find((item) => item?.type === 7)?.price
                )
              }}
            </div>
          </template>
          <template #seat8="{ text }">
            <div
                :style="{
                color: getTicketNumber(
                  text?.find((item) => item?.type === 8)?.quantity
                )?.color
              }"
            >
              {{
                getTicketNumber(
                    text?.find((item) => item?.type === 8)?.quantity
                )?.label ?? '--'
              }}
            </div>
            <div :style="{ color: '#fc8302' }">
              {{
                handlePriceShow(
                    text?.find((item) => item?.type === 8)?.price
                )
              }}
            </div>
          </template>
          <template #seat9="{ text }">
            <div
                :style="{
                color: getTicketNumber(
                  text?.find((item) => item?.type === 9)?.quantity
                )?.color
              }"
            >
              {{
                getTicketNumber(
                    text?.find((item) => item?.type === 9)?.quantity
                )?.label ?? '--'
              }}
            </div>
            <div :style="{ color: '#fc8302' }">
              {{
                handlePriceShow(
                    text?.find((item) => item?.type === 9)?.price
                )
              }}
            </div>
          </template>
          <template #operation="{ text, record }">
            <Button
                type="link"
                size="small"
                @click=" () => { handleBook(record) }"
                :disabled="!record.seatClassList.filter((item) => item.quantity)?.length">
              预定
            </Button>
          </template>


<!--          双行名称，给column使用-->
          <template #customTimeTitle>
            <div>出发时间</div>
            <div>到达时间</div>
          </template>
          <template #customSeatTitle>
            <div>商务座</div>
            <div>特等座</div>
          </template>
          <template #customScondHardSeadTitle>
            <div>硬卧</div>
            <div>二等卧</div>
          </template>
          <template #customFirstBedSeatTitle>
            <div>软卧</div>
            <div>一等卧</div>
          </template>

        </Table>
      </div>
      <Alert type="warning">
        <template #description>
          <div>
            如果查询结果中没有满足需求的车次，您还可以使用中转换乘功能，查询途中换乘一次的部分列车余票情况。
          </div>
          <div>
            显示的卧铺票价均为上铺票价，供您参考。具体票价以您确认支付时实际购买的铺别票价为准。如因运力原因或者其他不可控因素导致列车调度调整时，当前车型可能会发生变动
          </div>
        </template>
      </Alert>
    </Space>
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

.spacial-label {
  font-weight: bolder;
}

.city-name {
  display: inline-block;
  padding: 0 10px;
  font-weight: bolder;
}

.time-title {
  display: inline-block;
  padding: 0 5px;
  font-weight: bolder;
}

::v-deep {
  .custom-select {
    .ant-select-item-option-active::before {
      content: '√';
    }
  }

  .ant-tooltip-inner {
    background-color: #fff;
    padding: 0px;
    width: 500px;
  }

  .ant-tooltip-arrow-content {
    background-color: #3a9dd8;
  }

  .ant-table-thead {
    .ant-table-cell {
      background: #3A9DD8FF;
      background-size: cover;
    }
  }

  .ant-tabs {
    border: 1px solid #f0f0f0;
    border-top: none;
  }

  .ant-form {
    align-items: center;
    justify-content: space-between;
  }

  .ant-divider-vertical {
    height: 100%;
  }

  .ant-tabs-card.ant-tabs-top > .ant-tabs-nav .ant-tabs-tab + .ant-tabs-tab {
    margin-left: 0;
  }

  .ant-tabs-card > .ant-tabs-nav .ant-tabs-tab {
    /* // border-right: none; */
  }

  .ant-tabs-nav-wrap {
    width: 100%;

    .ant-tabs-nav-list {
      width: 100%;

      .ant-tabs-tab:first-child {
        border-left: none;
      }

      .ant-tabs-tab {
        /* // flex: 1; */
        width: 6.5%;
        text-align: center;
        border-top-left-radius: 0px !important;
        border-top-right-radius: 0px !important;

        .ant-tabs-tab-btn {
          width: 100%;
          text-align: center;
        }
      }

      .ant-tabs-tab.ant-tabs-tab-active {
        // border: 1px solid #1890ff;
        flex-grow: 1.5;
      }
    }
  }
}

::v-deep(.custome-table) {
  .ant-table-thead > tr > th {
    color: #fff;
  }
}
</style>
