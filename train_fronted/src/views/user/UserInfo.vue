<template>
  <Card>
    <TypographyTitle :level="5">基本信息</TypographyTitle>
    <Row v-for="info in state.userInfoMap" class="info-wrapper">
      <Col :span="8" :style="{ textAlign: 'end' }">
        <label class="info-label" :class="!info.unRequire && 'info-require'"
        >{{ info.label }}：</label
        ></Col
      >
      <Col span="4" :style="{ textAlign: 'start' }">
        <span class="info-value" :style="{ color: info.color }">
          {{
            info?.render ? info.render(info.value) : info.value ?? '--'
          }}
        </span>
      </Col>
    </Row>
    <Divider dashed></Divider>
    <div class="title-wrapper">
      <TypographyTitle :level="5">联系方式</TypographyTitle>
      <Button
          @click="handleContactEditTypeChange"
          v-if="state.contactEditType === 'view'"
      >编辑</Button
      >
      <Button @click="handleContactSubmit" v-else>保存</Button>
    </div>
    <div v-if="state.contactEditType === 'view'">
      <Row v-for="info in state.editUserInfoMap" class="info-wrapper">
        <Col :span="8" :style="{ textAlign: 'end' }">
          <label class="info-label" :class="info.require && 'info-require'"
          >{{ info.label }}：</label
          >
        </Col>
        <Col :span="4" :style="{ textAlign: 'start' }">
          <span class="info-value">{{ info.value }}</span></Col
        >
      </Row>
    </div>
    <div v-else>
      <Form :label-col="{ span: 8 }" :wrapper-col="{ span: 4 }">
        <div v-for="item in state.editUserInfoMap">
          <FormItem :label="item.label" :required="item.require">
            <Input
                v-model:value="item.value"
                :disabled="item.name === 'phone'"
            ></Input>
          </FormItem>
        </div>
      </Form>
    </div>
    <Divider dashed></Divider>
    <div class="title-wrapper">
      <TypographyTitle :level="5">保险订单</TypographyTitle>
    </div>

    <div>
      <a-table :columns="columns" :data-source="insuranceList">

        <template #insuranceType="{ text, record }">
          <div :style="{ textAlign: 'center' }">
            <!-- 显示保险类型 -->
            {{
              INSURANCE_TYPE.find((item) => item.value === record?.insuranceType)
                  ?.label ?? '--'
            }}
          </div>
        </template>
        <template #policy_holder="{ text, record }">
          <div :style="{alignItems: 'center'}">
            <div>{{record?.policyHolderName }}</div>
            <br>
            <div>{{ record?.policyHolderIdCard }}</div>
          </div>
        </template>
        <template #insured_people="{ text, record }">
          <div :style="{alignItems: 'center'}">
            <div>{{record?.insuredName }}</div>
            <br>
            <div>{{ record?.insuredIdCard }}</div>
          </div>
        </template>
        <template #action="{ record }">
          <a-button type="link" @click="viewInsuranceDetail(record.id)">查看详情</a-button>
        </template>
      </a-table>
    </div>
  </Card>
</template>

<script setup>
import {
  Card,
  Form,
  FormItem,
  TypographyTitle,
  Input,
  Button,
  Divider,
  Row,
  Col,
  Select,
  SelectOption,
  message
} from 'ant-design-vue'
import { ref,reactive, onMounted } from 'vue'
import { fechUserInfo, fetchUserUpdate ,fetchInsuranceList} from '@/service/index.js'
import {CHECK_STATUS, INSURANCE_TYPE, TICKET_STATUS_LIST} from '@/constants/index.js'
const useForm = Form.useForm

const state = reactive({
  contactEditType: 'view',
  otherEditType: 'view',
  userInfoMap: [
    { label: '登录账号（手机号）', value: '--', key: 'username' },
    { label: '姓名', value: '--', key: 'realName' },
    {
      label: '证件类型',
      value: '--',
      key: 'idType',
      render: (value) => (value === 0 ? '身份证' : '未知')
    },
    { label: '证件号码', value: '--', key: 'idCard' },
    {
      label: '核验状态',
      value: '--',
      unRequire: true,
      color: '#fe9a5e',
      key: 'verifyStatus',
      render: (value) =>
          CHECK_STATUS.find((item) => item.value === value)?.label ?? '--'
    }
  ],
  editUserInfoMap: [
    { label: '联系电话', value: undefined, name: 'phone', require: true }
    // ,
    // { label: '邮箱', value: undefined, name: 'mail' },
    // { label: '地址', value: undefined, name: 'address' },
    // { label: '邮编', value: undefined, name: 'postCode' }
  ]
})

const username = localStorage.getItem('userName')

const { validate } = useForm(
    state.editUserInfoMap,
    reactive({
      phone: [{ required: true, message: '请输入手机号' }]
    })
)

// 定义表格列
const columns = [
  {
    title: '保险类型',
    dataIndex: 'insuranceType',
    key: 'insuranceType',
    slots: { customRender: 'insuranceType' }
  },
  {
    title: '投保人',
    dataIndex: 'policy_holder',
    slots: { customRender: 'policy_holder' },
  },
  {
    title: '被保险人',
    dataIndex: 'insured_people',
    slots: { customRender: 'insured_people' },
  },
  {
  title: '保险期间',
  dataIndex: 'period',
  key: 'period',
  },
  {
    title: '保险状态',
    dataIndex: 'status',
    key: 'status',
  },
  {
    title: '操作',
    key: 'action',
    slots: { customRender: 'action' },
  },
];

// 定义保险列表数据
const insuranceList = ref([]);

const userId = localStorage.getItem('userId')
// 获取保险列表数据
const getInsuranceList = async () => {
  try {
    console.log('获取保险列表数据'+userId);
    fetchInsuranceList({ userId }).then((res) => {
      if (res.success) {
        insuranceList.value = res.data;
      }
    });

  } catch (error) {
    console.error('获取保险列表失败:', error);
  }
};

onMounted(() => {
  getInsuranceList();
  fechUserInfo({ username }).then((res) => {
    const { userInfoMap, editUserInfoMap } = state
    if (res.success) {
      state.userInfoMap = userInfoMap.map((item) => ({
        ...item,
        value: res.data[item.key]
      }))
      state.editUserInfoMap = editUserInfoMap.map((item) => ({
        ...item,
        value: res.data[item.name] ?? ''
      }))
      state.userType = res.data?.userType
    }
  })
})

const handleContactEditTypeChange = () => {
  state.contactEditType = state.contactEditType === 'view' ? 'edit' : 'view'
}
const handleOtherEditTypeChange = () => {
  state.otherEditType = state.otherEditType === 'view' ? 'edit' : 'view'
}

const handleContactSubmit = () => {
  fetchUserUpdate({
    username: username,
    mail: state.editUserInfoMap.find((item) => item.name === 'mail')?.value,
    postCode: state.editUserInfoMap.find((item) => item.name === 'postCode')
        ?.value,
    address: state.editUserInfoMap.find((item) => item.name === 'address')
        ?.value
  })
      .then((res) => {
        if (res.success) {
          state.contactEditType = 'view'
          message.success('修改信息成功')
        } else {
          message.error(res.message)
        }
      })
      .catch((err) => {
        state.contactEditType = 'view'
        console.log('err:::', err)
      })
}
const handleOtherSubmit = () => {
  fetchUserUpdate({
    username: username,
    userType: state.userType
  })
      .then((res) => {
        if (res.success) {
          state.otherEditType = 'view'
          message.success('修改信息成功')
        } else {
          message.error(res.message)
        }
      })
      .catch((err) => {
        state.otherEditType = 'view'
        console.log('err:::', err)
      })
}
</script>
<style lang="scss" scoped>
.info-wrapper {
  width: 100%;
  text-align: center;
  /* // padding: 10px; */
  margin-bottom: 24px;
  height: 32px;
  line-height: 32px;
  .info-label {
    display: inline-block;
    color: #787878;
  }
  .info-require::before {
    content: '* ';
    color: red;
  }
  .info-value {
    display: inline-block;
    padding: 0 10px;
    font-weight: bolder;
  }
}

.title-wrapper {
  display: flex;
  justify-content: space-between;
}
::v-deep {
  .ant-form-item-label {
    label {
      color: #787878;
    }
  }
}
</style>
