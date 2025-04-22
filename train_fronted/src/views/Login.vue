<template>
  <div class="login-wrapper">
    <div class="login-reg-panel">
      <!-- 已有账号的提示信息区域，用于提示用户已有账号可进行登录操作 -->
      <div class="login-info-box">
        <h2>已有账号？</h2>
        <h3>欢迎登录账号！</h3>
        <!-- 点击该按钮将 currentAction 设为 'login'，用于切换到登录页面 -->
        <button @click="() => currentAction = 'login'">去登录</button>
      </div>
      <!-- 没有账号的提示信息区域，用于提示用户没有账号可进行注册操作 -->
      <div class="register-info-box">
        <h2>没有账号？</h2>
        <h3>欢迎注册账号！</h3>
        <!-- 点击该按钮将 currentAction 设为'register'，用于切换到注册页面 -->
        <button @click="() => currentAction ='register'">去注册</button>
      </div>
      <div
          class="white-panel"
          :class="{ 'white-panel-left': currentAction ==='register' }"
      >
        <!-- 当 currentAction 为 'login' 时显示的登录表单区域 -->
        <div class="login-show" v-if="currentAction === 'login'">
          <h1 class="title">登录</h1>
          <Form name="basic" autocomplete="off" @submit.prevent="handleLogin">
            <FormItem v-bind="validateInfos.username">
              <Input
                  size="large"
                  v-model:value="formState.username"
                  placeholder="用户名"
              >
                <template #prefix><UserOutlined style="color: rgba(0, 0, 0, 0.25)" /></template
                >
              </Input>
            </FormItem>
            <!-- 密码输入项，绑定了对应的验证信息 validateInfos.password -->
            <FormItem v-bind="validateInfos.password">
              <InputPassword
                  size="large"
                  v-model:value="formState.password"
                  placeholder="密码"
              >
                <template #prefix><LockOutlined style="color: rgba(0, 0, 0, 0.25)"
                /></template>
              </InputPassword>
            </FormItem>
            <!-- 表单的操作区域，包含忘记密码链接和登录按钮 -->
            <FormItem>
              <div class="action-btn">
                <!-- 忘记密码的链接 -->
                <a href="">忘记密码？</a>
                <Button
                    type="primary"
                    html-type="submit"
                :style="{ backgroundColor: '#202020', border: 'none' }"
                >登录</Button>
              </div>
            </FormItem>
          </Form>
        </div>
        <!-- 当 currentAction 不为 'login'（即注册状态）时显示的注册表单区域 -->
        <div class="register-show" v-else>
          <h1 class="title">注册</h1>
          <Form name="basic" autocomplete="off" :label-col="{ span: 6 }">
            <!-- 用户名输入项，绑定了对应的验证信息 registerValidateInfos.username -->
            <FormItem label="用户名" v-bind="registerValidateInfos.username">
              <Input
                  v-model:value="registerForm.username"
                  placeholder="请输入注册手机号"
              >
              </Input>
            </FormItem>
            <!-- 密码输入项，绑定了对应的验证信息 registerValidateInfos.password -->
            <FormItem label="密码" v-bind="registerValidateInfos.password">
              <InputPassword
                  v-model:value="registerForm.password"
                  placeholder="密码"
              >
              </InputPassword>
            </FormItem>
            <!-- 姓名输入项，绑定了对应的验证信息 registerValidateInfos.realName -->
            <FormItem label="姓名" v-bind="registerValidateInfos.realName">
              <Input
                  v-model:value="registerForm.realName"
                  placeholder="请输入姓名"
              >
              </Input>
            </FormItem>
            <!-- 证件号码输入项，绑定了对应的验证信息 registerValidateInfos.idCard -->
            <FormItem label="证件号码" v-bind="registerValidateInfos.idCard">
              <Input
                  v-model:value="registerForm.idCard"
                  placeholder="请输入证件号码"
              >
              </Input>
            </FormItem>
            <!-- 手机号码输入项，绑定了对应的验证信息 registerValidateInfos.phone -->
            <FormItem label="手机号码" v-bind="registerValidateInfos.phone">
              <Input
                  v-model:value="registerForm.phone"
                  placeholder="请输入联系电话"
              >
              </Input>
            </FormItem>
            <!-- 表单的操作区域，包含空链接和注册按钮 -->
            <FormItem>
              <div class="action-btn">
                <a></a>
                <!-- 注册按钮,点击触发 registerSubmit 方法 -->
                <Button
                    type="primary"
                    html-type="button"
                    @click="registerSubmit"
                    :style="{ backgroundColor: '#202020', border: 'none' }"
                >注册</Button>
              </div>
            </FormItem>
          </Form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {
  Form,
  FormItem,
  Input,
  InputPassword,
  Button,
  message,
  Select,
  Modal
} from 'ant-design-vue'
import {reactive, ref, unref} from 'vue'
import {fetchLogin, fetchRegister} from '@/service'
import {UserOutlined, LockOutlined} from '@ant-design/icons-vue'
import {useRouter} from 'vue-router'
import Cookies from 'js-cookie'

const useForm = Form.useForm

// 定义登录表单的响应式数据对象
const formState = reactive({
  username: '',
  password: ''
})

// 定义登录表单的验证规则，用户名和密码都为必填项
const rulesRef = reactive({
  username: [
    {
      required: true,
      message: '请输入手机号'
    }
  ],
  password: [
    {
      required: true,
      message: '请输入密码'
    }
  ]
})

// 通过 useForm 方法获取登录表单的验证实例 validate 和验证信息 validateInfos
const {validate, validateInfos} = useForm(formState, rulesRef)

// 定义注册表单的响应式数据对象，包含用户名、密码、姓名、证件号码、手机号
const registerForm = reactive({
  username: '',
  password: '',
  realName: '',
  idCard: '',
  phone: ''
})

// 定义注册表单的验证规则，所有字段都为必填项
const registerRules = reactive({
  username: [
    {
      required: true,
      message: '请输入注册手机号'
    }
  ],
  password: [
    {
      required: true,
      message: '请输入密码'
    }
  ],
  realName: [
    {
      required: true,
      message: '请输入姓名'
    }
  ],
  idCard: [
    {
      required: true,
      message: '请输入证件号码'
    }
  ],
  phone: [
    {
      required: true,
      message: '请输入联系电话号码'
    }
  ]
})

// 通过 useForm 方法获取注册表单的验证实例 registerValidate 和验证信息 registerValidateInfos
const {validate: registerValidate, validateInfos: registerValidateInfos} =
    useForm(registerForm, registerRules)

// 定义当前操作状态的响应式引用，初始值为 'login'，表示初始为登录状态
let currentAction = ref('login')

// 获取 Vue Router 的路由实例
const router = useRouter()



// 登录表单提交的处理函数
const handleLogin = async () => {
  console.log('登录提交');
  try {
    // 验证登录表单
    await validate();
    console.log('登录验证通过');
    // 调用登录接口，传入用户名和密码
    const res = await fetchLogin(formState);
    console.log('登录接口返回数据:', res);
    if (res){
      // 存储token到本地存储
      localStorage.setItem('token', res.token);
      localStorage.setItem('userName', res.username);
      localStorage.setItem('userId', res.userId);
      localStorage.setItem('userRole', res.userRole);
      localStorage.setItem('realName', res.realName);

      console.log('登录成功，用户信息已存储到本地存储。');
      console.log('用户信息:', res.realName);
      console.log('token:', res.token);
      console.log('用户角色:', res.userRole);
      console.log('用户名:', res.username);
      console.log('用户ID:', res.userId);

      // 登录成功，将token、用户名和用户ID存储到cookie中
      // Cookies.set('token', res.data?.token)
      // Cookies.set('userId', res.data?.userId)
      // Cookies.set('username', res.data?.username)
      // 跳转到票务搜索页面
      if (res.userRole === 'admin') {
        router.push('/indexManagement');
      } else if (res.userRole === 'user') {
        router.push('/Welcome');
      }
    }

  } catch (error) {
    console.error('登录验证失败:', error);
    message.error('登录验证失败，请检查输入');
  }
}



// 注册表单提交的处理函数
const registerSubmit = async () => {
  console.log('注册提交');
  console.log('注册表单数据:', registerForm);
  try {
    // 验证注册表单
    await registerValidate();
    // 调用注册接口，传入注册表单的数据
    const res = await fetchRegister(registerForm);
    if (res.success) {
      message.success('注册成功');
      // 将当前操作状态切换为登录状态
      currentAction.value = 'login';
      // 将注册成功的用户名设置到登录表单的用户名/邮箱/手机号字段
      formState.username = res.data?.username;
      // 清空登录表单的密码字段
      formState.password = '';
    } else {
      // 注册失败，显示错误消息
      message.error(res.message);
    }
  } catch (error) {
    console.error('注册验证失败:', error);
    message.error('注册验证失败，请检查输入');
  }
}
</script>

<style lang="scss">
.login-wrapper {
  background-color: rgb(185, 217, 255);
  width: 100%;
  height: 100vh;
  background-clip: border-box;
}

.login-reg-panel {
  position: relative;
  top: 50%;
  transform: translateY(-50%);
  text-align: center;
  width: 40%;
  right: 0;
  left: 20%;
  margin: auto;
  min-width: 800px;
  height: 600px;
  background-color: rgb(58, 157, 216);
}

.white-panel {
  background-color: rgba(255, 255, 255, 1);
  height: 600px;
  position: absolute;
  width: 50%;
  right: calc(50% - 50px);
  transition: 0.3s ease-in-out;
  z-index: 0;
  box-sizing: border-box;
}

.white-panel-left {
  right: calc(0px + 50px);
}

.login-show,
.register-show {
  height: 100%;
  display: flex;
  flex-direction: column;
  transition: 0.3s ease-in-out;
  color: #242424;
  text-align: left;
  padding: 30px;
}

.title {
  font-size: 24px;
  font-weight: bolder;
  padding: 20px 0;
  color: #202020;
}

.action-btn {
  display: flex;
  width: 100%;
  justify-content: space-between;
}

.action-btn a {
  display: block;
  line-height: 32px;
}

.login-info-box {
  display: flex;
  flex-direction: column;
  width: 30%;
  padding: 0 50px;
  top: 20%;
  left: 0;
  position: absolute;
  text-align: left;
  justify-content: center;
  font-family: 'Mukta', sans-serif;
  color: #b8b8b8;
}

.login-info-box h2 {
  font-size: 24px;
  color: #b8b8b8;
  font-weight: bolder;
  margin-bottom: 40px;
}

.login-info-box h3 {
  font-size: 20px;
  color: #b8b8b8;
  margin-bottom: 40px;
}

.login-info-box button {
  cursor: pointer;
  width: 100%;
  background-color: transparent;
  box-shadow: none;
  border: 1px solid #b8b8b8;
  border-radius: 2px;
  height: 25px;
}

.register-info-box {
  width: 30%;
  padding: 0 50px;
  top: 20%;
  right: 0;
  position: absolute;
  text-align: left;
  font-family: 'Mukta', sans-serif;
  color: #b8b8b8;
}

.register-info-box h2 {
  font-size: 24px;
  color: #b8b8b8;
  font-weight: bolder;
  margin-bottom: 40px;
}

.register-info-box h3 {
  font-size: 20px;
  color: #b8b8b8;
  margin-bottom: 40px;
}

.register-info-box button {
  cursor: pointer;
  width: 100%;
  background-color: transparent;
  box-shadow: none;
  border: 1px solid #b8b8b8;
  border-radius: 2px;
  height: 25px;
}

.code-modal .wrapper {
  text-align: center;
}

.code-modal .tip-text {
  width: 100%;
  text-align: center;
  font-size: 14px;
  color: red;
}

.code-modal .code-input {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.code-modal .code-label {
  margin: 10px;
}

.code-modal .code-label::before {
  content: '*';
  color: red;
}

.ant-modal-header {
  background-color: #3b3b3b !important;
}

.ant-form-item-label label {
  color: #202020;
}
</style>