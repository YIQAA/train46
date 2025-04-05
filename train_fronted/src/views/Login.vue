<template>
  <div class="login-container">
    <form @submit.prevent="handleSubmit" class="form-container">
      <h2>用户登录</h2>
      <div class="form-group">
        <label>用户名</label>
        <input
            v-model="form.username"
            type="text"
            required
            class="form-input"
            placeholder="请输入用户名">
      </div>
      <div class="form-group">
        <label>密码</label>
        <input
            v-model="form.password"
            type="password"
            required
            class="form-input"
            placeholder="请输入密码">
      </div>
      <button type="submit" class="submit-btn">登录</button>
    </form>
  </div>
</template>

<script setup>
import { reactive } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

const router = useRouter();
const form = reactive({
  username: '',
  password: ''
});

const handleSubmit = async () => {
  try {
    const response = await axios.post('/api/auth/login', form);
    // 存储 token 到本地存储
    localStorage.setItem('token', response.data.token);
    localStorage.setItem('userName', response.data.username);
    localStorage.setItem('userId', response.data.userId);
    localStorage.setItem('userRole', response.data.role);
    localStorage.setItem('realName', response.data.realName);

    

    // 跳转到欢迎页面
    router.push('/Welcome');

    console.log('登录成功，用户信息已存储到本地存储。');
    console.log('用户信息:', response.data);
    console.log('token:', response.data.token);

  } catch (error) {
    // 显示详细错误信息
    const errorMessage = error.response?.data?.message
        || error.response?.data
        || error.message;
    alert(`登录失败: ${JSON.stringify(errorMessage)}`);

    // 打印完整错误日志到控制台
    console.error('完整错误信息:', {
      request: error.config,
      response: error.response
    });
  }
};
</script>


<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #6b73ff 0%, #000dff 100%);
  padding: 20px;
}

.form-container {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
  transition: transform 0.3s ease;
}

.form-container:hover {
  transform: translateY(-2px);
}

h2 {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 30px;
  font-size: 24px;
}

.form-group {
  margin-bottom: 25px;
}

label {
  display: block;
  margin-bottom: 8px;
  color: #555;
  font-size: 14px;
  font-weight: 500;
}

.form-input {
  width: 100%;
  padding: 12px 15px;
  border: 2px solid #e0e0e0;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.3s ease;
}

.form-input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
}

.form-input::placeholder {
  color: #aaa;
}

.submit-btn {
  width: 100%;
  padding: 12px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.3s ease;
}

.submit-btn:hover {
  background: #0056b3;
}

.submit-btn:active {
  transform: scale(0.98);
}

/* 响应式设计 */
@media (max-width: 480px) {
  .form-container {
    padding: 25px;
  }

  h2 {
    font-size: 22px;
  }
}
</style>