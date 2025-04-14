<template>
  <!-- 整个登录页面的最外层容器，设置背景颜色为半透明黑色，宽度占满屏幕，高度为视口高度，背景裁剪为边框盒 -->
  <div class="login-wrapper">
    <!-- 登录和注册的面板容器，相对定位，通过 translateY 垂直居中，宽度占屏幕 40%，水平居中，最小宽度 800px，高度 600px，背景为半透明黑色 -->
    <div class="login-reg-panel">
      <!-- 已有账号的提示信息区域，用于提示用户已有账号可进行登录操作 -->
      <div class="login-info-box">
        <!-- 提示已有账号的标题 -->
        <h2>已有账号？</h2>
        <!-- 欢迎用户登录的提示信息 -->
        <h3>欢迎登录账号！</h3>
        <!-- 点击该按钮将 currentAction 设为 'login'，用于切换到登录页面 -->
        <button @click="() => currentAction = 'login'">去登录</button>
      </div>
      <!-- 没有账号的提示信息区域，用于提示用户没有账号可进行注册操作 -->
      <div class="register-info-box">
        <!-- 提示没有账号的标题 -->
        <h2>没有账号？</h2>
        <!-- 欢迎用户注册的提示信息 -->
        <h3>欢迎注册账号！</h3>
        <!-- 点击该按钮将 currentAction 设为'register'，用于切换到注册页面 -->
        <button @click="() => currentAction ='register'">去注册</button>
      </div>
      <!-- 白色的内容面板，根据 currentAction 的值动态添加 'white-panel-left' 类来改变其样式位置 -->
      <div
          class="white-panel"
          :class="{ 'white-panel-left': currentAction ==='register' }"
      >
        <!-- 当 currentAction 为 'login' 时显示的登录表单区域 -->
        <div class="login-show" v-if="currentAction === 'login'">
          <!-- 登录表单的标题 -->
          <h1 class="title">登录</h1>
          <!-- 登录表单，设置名称为 'basic'，关闭自动完成功能 -->
          <Form name="basic" autocomplete="off">
            <!-- 用户名、邮箱或手机号的输入项，绑定了对应的验证信息 validateInfos.username -->
            <FormItem v-bind="validateInfos.username">
              <Input
                  size="large"
                  v-model:value="formState.username"
                  placeholder="用户名"
              >
                <!-- 输入框前缀的用户图标，设置颜色为半透明黑色 -->
                <template #prefix
                ><UserOutlined style="color: rgba(0, 0, 0, 0.25)" /></template
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
                <!-- 输入框前缀的锁图标，设置颜色为半透明黑色 -->
                <template #prefix
                ><LockOutlined style="color: rgba(0, 0, 0, 0.25)"
                /></template>
              </InputPassword>
            </FormItem>
            <!-- 表单的操作区域，包含忘记密码链接和登录按钮 -->
            <FormItem>
              <div class="action-btn">
                <!-- 忘记密码的链接 -->
                <a href="">忘记密码？</a>
                <!-- 登录按钮，设置为主要按钮样式，背景颜色为深灰色，无边框，点击触发 handleFinish 方法 -->
                <Button
                    type="primary"
                    :style="{ backgroundColor: '#202020', border: 'none' }"
                    @click="handleLogin"
                >登录</Button>
              </div>
            </FormItem>
          </Form>
        </div>
        <!-- 当 currentAction 不为 'login'（即注册状态）时显示的注册表单区域 -->
        <div class="register-show" v-else>
          <!-- 注册表单的标题 -->
          <h1 class="title">注册</h1>
          <!-- 注册表单，设置名称为 'basic'，关闭自动完成功能，设置标签列的跨度为 6 -->
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
                <!-- 注册按钮，设置为主要按钮样式，背景颜色为深灰色，无边框，点击触发 registerSubmit 方法 -->
                <Button
                    type="primary"
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
// 引入 Ant Design Vue 组件库中的表单、表单项、输入框、密码输入框、按钮、消息提示、选择框、模态框等组件
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
// 引入 Vue 的响应式 API，reactive 用于创建响应式对象，ref 用于创建响应式引用，unref 用于解包 ref
import {reactive, ref, unref} from 'vue'
// 引入项目中自定义的登录和注册接口请求函数
import {fetchLogin, fetchRegister} from '@/service'
// 引入 Ant Design 图标库中的用户图标和锁图标组件
import {UserOutlined, LockOutlined} from '@ant-design/icons-vue'
// 引入 Vue Router 的 useRouter 钩子函数，用于获取路由实例进行页面跳转
import {useRouter} from 'vue-router'
// 引入 js-cookie 库，用于在浏览器中操作 cookie
import Cookies from 'js-cookie'

// 获取 Ant Design Vue 表单的验证方法
const useForm = Form.useForm

// 定义登录表单的响应式数据对象，包含用户名、密码，初始值分别为空字符串
const formState = reactive({
  username: '',
  password: ''
})

// 定义模态框显示状态的响应式数据，初始值为 false，即模态框默认不显示
const state = reactive({
  open: false
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

// 定义注册表单的响应式数据对象，包含用户名、密码、姓名、证件类型、证件号码、手机号和邮箱，初始值部分为 'admin' 或空值
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
      // 存储 token 到本地存储
      localStorage.setItem('token', res.token);
      localStorage.setItem('userName', res.username);
      localStorage.setItem('userId', res.userId);
      localStorage.setItem('userRole', res.role);
      localStorage.setItem('realName', res.realName);

      console.log('登录成功，用户信息已存储到本地存储。');
      console.log('用户信息:', res.realName);
      console.log('token:', res.token);
      console.log('用户角色:', res.userRole);
      console.log('用户名:', res.username);
      console.log('用户ID:', res.userId);


      // 登录成功，将 token、用户名和用户 ID 存储到 cookie 中
      // Cookies.set('token', res.data?.token)
      // Cookies.set('userId', res.data?.userId)
      // Cookies.set('username', res.data?.username)
      // 跳转到票务搜索页面
      router.push('/Welcome');

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
      // 注册成功，显示成功提示信息
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