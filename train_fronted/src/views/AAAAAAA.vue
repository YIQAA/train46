<template>
  <div class="insurance-selection-wrapper">
    <Space direction="vertical" :style="{ width: '100%' }" size="large">
      <!-- 保险选择卡片 -->
      <Card class="card-container">
        <template #title>
          <div class="title-wrapper">
            <h1>保险选择</h1>
          </div>
        </template>
        <div>
          <Tabs v-model:active-key="formState.insuranceType" @change="handleInsuranceSelect">
            <TabPane v-for="item in INSURANCE_TYPE" :key="item.value" :tab="item.label">
              <!-- 这里可以放置每个保险种类对应的额外内容，如果没有特殊内容，可以留空 -->

            </TabPane>
          </Tabs>
        </div>
        <div class="insurance-description" >
          <!-- 保险描述 -->
          <h3>保险描述</h3>
          <div v-if="formState.insuranceType" class="description-table">
            <div v-html="getInsuranceDescription(formState.insuranceType)"></div>
          </div>
        </div>
        <div class="insurance-photo">
          <h3>保险特色</h3>
          <!-- 保险图片 -->
          <img v-if="formState.insuranceType===1" src="../../assets/bao1.jpg" alt="'保险特色图片'" height="520px"  />
          <img v-if="formState.insuranceType===2" src="../../assets/bao2.jpg" alt="'保险特色图片'" height="490px"  />
          <img v-if="formState.insuranceType===3" src="../../assets/bao3.jpg" alt="'保险特色图片'" height="520px"  />
          <img v-if="formState.insuranceType===4" src="../../assets/bao4.jpg" alt="'保险特色图片'" height="520px"  />
        </div>
        <div class="insurance-photo duration-buttons">
          <h3>保障期限</h3>
          <div class="duration-container" v-if="formState.insuranceType===1">
            <button
                @click="formState.duration = 4"
                :class="{ 'selected': formState.duration === 4 }"
            >4天</button>
          </div>
          <div class="duration-container" v-if="formState.insuranceType===2">
            <button
                @click="formState.duration = 0"
                :class="{ 'selected': formState.duration === 0 }"
            >单次</button>
          </div>
          <div class="duration-container" v-if="formState.insuranceType===3">
            <button
                @click="formState.duration = 2"
                :class="{ 'selected': formState.duration === 2 }"
            >2天</button>
            <button
                @click="formState.duration = 7"
                :class="{ 'selected': formState.duration === 7 }"
            >7天</button>
            <button
                @click="formState.duration = 15"
                :class="{ 'selected': formState.duration === 15 }"
            >15天</button>
            <button
                @click="formState.duration = 30"
                :class="{ 'selected': formState.duration === 30 }"
            >30天</button>
          </div>
          <div class="duration-container" v-if="formState.insuranceType===4">
            <button
                @click="formState.duration = 3"
                :class="{ 'selected': formState.duration === 3 }"
            >3天</button>
          </div>
        </div>

      </Card>
      <!-- 投保人信息卡片和被投保人信息卡片并列 -->
      <div class="card-row">
        <Card class="card-container">
          <template #title>
            <div class="title-wrapper">
              <h1>投保人信息</h1>
            </div>
          </template>
          <Form :form="formRef">
            <FormItem label="投保人姓名" name="policyHolderName">
              <Input v-model:value="formState.policyHolderName" />
            </FormItem>
            <FormItem label="投保人证件类型" name="policyHolderIdType">
              <Select v-model:value="formState.policyHolderIdType">
                <SelectOption v-for="item in ID_CARD_TYPE" :value="item.value" :key="item.value">
                  {{ item.label }}
                </SelectOption>
              </Select>
            </FormItem>
            <FormItem label="投保人证件号码" name="policyHolderIdCard">
              <Input v-model:value="formState.policyHolderIdCard" />
            </FormItem>
          </Form>
        </Card>
        <Card class="card-container">
          <template #title>
            <div class="title-wrapper">
              <h1>被投保人信息</h1>
            </div>
          </template>
          <Form :form="formRef">
            <FormItem label="被投保人姓名" name="insuredName">
              <Input v-model:value="formState.insuredName" />
            </FormItem>
            <FormItem label="被投保人证件类型" name="insuredIdType">
              <Select v-model:value="formState.insuredIdType">
                <SelectOption v-for="item in ID_CARD_TYPE" :value="item.value" :key="item.value">
                  {{ item.label }}
                </SelectOption>
              </Select>
            </FormItem>
            <FormItem label="被投保人证件号码" name="insuredIdCard">
              <Input v-model:value="formState.insuredIdCard" />
            </FormItem>
          </Form>
        </Card>
      </div>
      <!-- 保障期限卡片 -->
      <Card class="card-container">
        <template #title>
          <div class="title-wrapper">
            <h1>保障期限</h1>
          </div>
        </template>
        <Form :form="formRef">
          <div class="date-range">
            <FormItem label="起保时间" name="startTime">
              <DatePicker v-model:value="formState.startTime" format="YYYY-MM-DD" />
            </FormItem>
            <FormItem label="终保时间" name="endTime">
              <DatePicker v-model:value="formState.endTime" format="YYYY-MM-DD" />
            </FormItem>
          </div>
        </Form>
      </Card>
      <!-- 阅读并同意条款复选框 -->
      <div :style="{ width: '100%', textAlign: 'center' }">
        <Checkbox v-model:checked="formState.agreed"></Checkbox>
        <span>我已阅读并同意</span>
        <span>
          <a href="">《投保须知》</a>
          <a href="">《保险条款》</a>
          <a href="">《责任免除条款》</a>
          <a href="">《平台服务协议》</a>
          <a href="">《客户告知书》</a>
        </span>
        <span>,此服务由上海保险有限公司提供</span>
      </div>
      <div :style="{ width: '100%', textAlign: 'center' }">
        <Space size="large">
          <Button @click="submitInsurance">提交保险订单</Button>
        </Space>
      </div>
    </Space>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted,watch} from 'vue';
import dayjs from 'dayjs';
import {
  Card,
  Space,
  Select,
  SelectOption,
  Input,
  Form,
  FormItem,
  DatePicker,
  Button,
  message,
  Tabs,
  Checkbox
} from 'ant-design-vue';

const {TabPane} = Tabs;
import {useRoute} from 'vue-router';
import {ID_CARD_TYPE, INSURANCE_TYPE} from '@/constants';

const route = useRoute();

const INSURANCE_DESCRIPTION = {
  1: `
    <p>出行无忧保为您的出行提供全方位保障，涵盖意外身故、伤残、医疗等多项保障。</p>
    <p>无论您是选择飞机、火车、汽车等任何交通工具出行，都能为您保驾护航。</p>
    <p>当您在旅途中遭遇意外事故时，我们将根据保险条款为您提供及时的经济赔偿，让您无后顾之忧。</p>
    <h4>保障责任</h4>
    <table>
      <tr>
        <th style="width: 60%;">保障责任</th>
        <th>保障金额</th>
      </tr>
      <tr>
        <td>乘坐飞机</td>
        <td class="highlight">90万元</td>
      </tr>
      <tr>
        <td>意外伤害</td>
        <td class="highlight">80万元</td>
      </tr>
      <tr>
        <td>乘坐轨道交通</td>
        <td class="highlight">60万元</td>
      </tr>
      <tr>
        <td>乘坐客运汽车</td>
        <td class="highlight">30万元</td>
      </tr>
      <tr>
        <td>个人旅行法律责任</td>
        <td class="highlight">15万元</td>
      </tr>
      <tr>
        <td>意外医疗（免赔，100%赔付）</td>
        <td class="highlight">8万元</td>
      </tr>
      <tr>
        <td>新型冠状病毒感染（重症或危重症）</td>
        <td class="highlight">8000元</td>
      </tr>
      <tr>
        <td>新型冠状病毒感染住院津贴</td>
        <td class="highlight">150元/天</td>
      </tr>
      <tr>
        <td>基本保障之室内装潢，室内财产</td>
        <td class="highlight">3000元</td>
      </tr>
    </table>

  `,
  2: `
    <p>旅程预定取消险可在您因特殊原因取消旅程时，为您提供相应的经济补偿。</p>
    <p>生活中难免会有突发状况，如生病、工作安排变动等，导致您不得不取消已经预定好的旅程。</p>
    <p>购买此保险后，您可以在符合条件的情况下获得一定比例的费用返还，减少您的经济损失。</p>
    <h4>保障责任</h4>
    <table>
      <tr>
        <th style="width: 60%;">保障责任</th>
        <th>保障金额</th>
      </tr>
      <tr>
        <td>旅程预定取消</td>
        <td class="highlight">1.5万元</td>
      </tr>
    </table>
  `,
  3: `
    <p>国内旅行险针对国内旅行设计，保障您在旅行期间的安全和权益。</p>
    <p>无论是自然风光游、历史文化游还是城市购物游，我们都能为您提供贴心的保障。</p>
    <p>在旅行过程中，如果您遇到意外受伤、突发疾病、行李丢失等情况，我们将为您提供必要的援助和赔偿。</p>
    <table>
      <tr>
        <th style="width: 60%;">保障责任</th>
        <th>保障金额</th>
      </tr>
      <tr>
        <td>旅行意外身故/伤残</td>
        <td class="highlight">30万元</td>
      </tr>
      <tr>
        <td>新冠（重症，危重症）</td>
        <td class="highlight">1万元</td>
      </tr>
      <tr>
        <td>新冠身故</td>
        <td class="highlight">10万元</td>
      </tr>
      <tr>
        <td>飞机意外伤害</td>
        <td class="highlight">100万元</td>
      </tr>
      <tr>
        <td>火车意外身故及伤残</td>
        <td class="highlight">50万元</td>
      </tr>
      <tr>
        <td>乘坐营运汽车身故及伤残</td>
        <td class="highlight">20万元</td>
      </tr>
      <tr>
        <td>乘坐营运轮船身故及伤残</td>
        <td class="highlight">10万元</td>
      </tr>
      <tr>
        <td>意外门急诊/意外住院医疗（0免赔，100%赔付）</td>
        <td class="highlight">2万元</td>
      </tr>
      <tr>
        <td>食物中毒医疗</td>
        <td class="highlight">1000元</td>
      </tr>
      <tr>
        <td>意外住院津贴</td>
        <td class="highlight">100元/天，30天为限</td>
      </tr>
      <tr>
        <td>紧急医疗运送及送返</td>
        <td class="highlight">10万元</td>
      </tr>
      <tr>
        <td>慰问探访</td>
        <td class="highlight">1000元</td>
      </tr>
      <tr>
        <td>个人责任</td>
        <td class="highlight">8万元</td>
      </tr>
      <tr>
        <td>行李延误（每6小时赔付300元）</td>
        <td class="highlight">最高赔付600元</td>
      </tr>
      <tr>
        <td>银行卡盗刷</td>
        <td class="highlight">2000元</td>
      </tr>
      <tr>
        <td>旅行证件重置费用</td>
        <td class="highlight">500元</td>
      </tr>
      <tr>
        <td>旅行期间家财保障</td>
        <td class="highlight">2000元</td>
      </tr>
    </table>
  `,
  4: `
    <p>空铁联运险适用于采用空铁联运方式出行的旅客，提供全程保障。</p>
    <p>随着交通的发展，越来越多的人选择空铁联运的方式出行，这种方式更加便捷高效。</p>
    <p>本保险将为您在整个行程中的安全负责，无论您是在飞机上还是火车上，都能享受到我们的保障服务。</p>
    <table>
      <tr>
        <th style="width: 60%;">保障责任</th>
        <th>保障金额</th>
      </tr>
      <tr>
        <td>出行意外事故死亡、伤残</td>
        <td class="highlight">30万元</td>
      </tr>
      <tr>
        <td>航班延误4小时（到达延误）</td>
        <td class="highlight">200元</td>
      </tr>
      <tr>
        <td>高铁到达延误时间30+分钟</td>
        <td class="highlight">50元</td>
      </tr>

    </table>
  `
};


const formRef = Form.useForm();

const formState = reactive({
  // 选择保险类型
  insuranceType: 1,
  //金额
  amount: 0,
  //投保人信息
  policyHolderName: '',
  policyHolderIdType: 0,
  policyHolderIdCard: '',
  policyHolderPhone: '',
  // 被投保人信息
  insuredName: '',
  insuredIdType: 0,
  insuredIdCard: '',
  insuredPhone: '',
  //
  startTime: null,
  endTime: null,
  agreed: false, // 新增：同意条款状态
  duration: 4
});

watch(
    () => formState.insuranceType,
    (newValue) => {
      const selectedType = INSURANCE_TYPE.find(t => t.value === newValue)
      if (selectedType) {
        message.success(`您已选择 ${selectedType.label} 保险`)
      }


      // 根据选择的保险类型设置默认保障期限
      if (newValue === 1) {
        formState.duration = 4;
      } else if (newValue === 2) {
        formState.duration = 0;
      } else if (newValue === 3) {
        formState.duration = 2;
      } else if (newValue === 4) {
        formState.duration =3;
      }
    }
)
watch(
    [() => formState.startTime, () => formState.duration],
    ([newStartTime, newDuration]) => {
      if (newStartTime && newDuration !== undefined) {
        // 使用 dayjs 计算终保时间
        const endTime = dayjs(newStartTime).add(newDuration, 'day');
        formState.endTime = endTime;

        // 显示格式化后的日期
        // message.success(`起保时间 ${formState.startTime.format('YYYY-MM-DD')}`);
        // message.success(`终保时间 ${formState.endTime.format('YYYY-MM-DD')}`);
      }
    },
    { immediate: true } // 初始化时立即执行一次
);

const handleInsuranceSelect = (value) => {
  formState.insuranceType = value;
};

const getInsuranceDescription = (insuranceValue) => {
  return INSURANCE_DESCRIPTION[insuranceValue];
};

const submitInsurance = () => {
  console.log('formState', formState);
  if (!formState.insuranceType) {
    message.error('请选择保险类型');
    return;
  }
  if (!formState.insuredName  || !formState.insuredIdCard || !formState.startTime || !formState.endTime) {
    message.error('请完善被投保人信息和保障期限');
    return;
  }
  if (!formState.policyHolderName || !formState.policyHolderIdCard || !formState.policyHolderPhone) {
    message.error('请完善投保人信息和保障期限');
    return;
  }
  if (!formState.agreed) {
    message.error('请阅读并同意《投保须知》《保险条款》《责任免除条款》《平台服务协议》《客户告知书》');
    return;
  }
  // 这里可以添加提交保险订单的逻辑，例如调用后端接口
  console.log('提交的保险信息：', {
    insuranceType: formState.insuranceType,
    ...formState
  });
  message.success('保险订单提交成功');
};
</script>

<style lang="scss" scoped>
.card-container {
  border: 1px solid #1890ff;
  background: #fff;
  overflow: hidden;
  padding: 0;
  border-radius: 4px;
}

.insurance-selection-wrapper {
  .title-wrapper {
    display: flex;
    height: 20px;
    line-height: 20px;

    h1 {
      font-size: 16px;
      margin: 0;
      color: #fff;
    }
  }

  .submit-btn {
    background-color: #fd6a09;
    color: #fff;
  }

  .card-row {
    display: flex;
    gap: 20px; // 卡片之间的间距
  }

  .card-row .card-container {
    flex: 1; // 让两个卡片平均分配宽度
  }

  .date-range {
    display: flex;
    gap: 20px; // 日期选择器之间的间距
  }
}

.insurance-description, .insurance-photo {
  background: #ffffff;
  border-radius: 8px;
  padding: 20px;
  margin: 16px 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border: 1px solid #e8e8e8;

  h3 {
    color: #1890ff;
    margin-bottom: 12px;
    font-size: 16px;
    border-bottom: 1px solid #eee;
    padding-bottom: 8px;
  }
}

.insurance-description {
  .description-content {
    line-height: 1.6;
    color: #666;

    p {
      margin-bottom: 12px;
    }
  }
  /* 表格样式 */
  :deep(.description-table table) {
    width: 100%;
    border-collapse: collapse;
    margin: 16px 0;
  }

  :deep(.description-table table th),
  :deep(.description-table table td) {
    padding: 12px;
    border: 1px solid #e8e8e8;
    text-align: left;
  }

  :deep(.description-table table th) {
    background-color: #fafafa;
    font-weight: 600;
  }

  :deep(.description-table table tr:nth-child(even)) {
    background-color: #f9f9f9;
  }

  :deep(.description-table table .highlight) {
    color: #1890ff;
    font-weight: 500;
  }
}

.insurance-photo {
  .photo-container {
    text-align: center;
    img {
      max-width: 100%;
      height: auto;
      border-radius: 4px;
      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
    }
  }
}
:deep {
  .ant-card-head {
    background-color: #3A9DD8FF;
  }

  .ant-card-body {
    background-color: #ebedf6;
    padding: 10px 15px;
  }

  .ant-form-item-label label {
    color: #333;
    width: 150px; // 设置 label 宽度一致
    text-align: right;
    padding-right: 10px;
  }

  .ant-form-item-control-input-content > * {
    width: 400px; // 设置 input 宽度固定为 400px
  }
}
//保障期限按钮
.duration-buttons {
  .duration-container {
    display: flex;
    gap: 20px;
    margin-top: 12px;

    button {
      width: 150px;  // 固定宽度
      height: 48px;  // 固定高度
      padding: 12px 24px;
      border: 2px solid #1890ff;
      border-radius: 8px;
      background: #f0f9ff;
      color: #1890ff;
      font-size: 16px;
      font-weight: 500;
      cursor: pointer;
      transition: all 0.3s ease;
      white-space: nowrap;  // 防止文字换行

      &:hover {
        background: #e6f7ff;
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(24, 144, 255, 0.2);
      }

      &.selected {
        background: #1890ff;
        color: white;
        border-color: #096dd9;
        box-shadow: 0 2px 8px rgba(24, 144, 255, 0.4);
      }
    }
  }
}
</style>