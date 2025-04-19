<template>
  <div class="train-booking">
    <!-- 旅游推荐区 -->
    <div class="travel-recommendation-section">
      <a-carousel autoplay>
        <a-carousel-item v-for="(item, index) in travelRecommendations" :key="index">
          <img :src="item.image" alt="旅游推荐图片" />
          <h3>{{ item.title }}</h3>
          <p>{{ item.description }}</p>
        </a-carousel-item>
      </a-carousel>
    </div>
    <!-- 特色服务区 -->
    <div class="special-service-section">
      <h2>特色服务</h2>
      <a-row gutter="16">
        <a-col :span="6">
          <a-card title="订餐服务" @click="handleMealBooking">
            <img name="ii" src="../assets/04.jpg" alt="订餐服务图片"  />
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card title="接送站服务" @click="handlePickupAndDropoff">
            <img src="../assets/01.jpg" alt="接送站服务图片" />
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card title="保险服务" @click="handleInsurance">
            <img src="../assets/02.jpg" alt="重点旅客预约图片" />
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card title="e卡通" @click="handleLostItemSearch">
            <img src="../assets/03.jpg" alt="遗失物品查找图片" />
          </a-card>
        </a-col>
      </a-row>
    </div>



    <!-- 底部信息 -->
    <a-layout-footer>
      <p>© 2025 铁路 12306 版权所有</p>
    </a-layout-footer>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import {
  Button,
  Layout,
  Card,
  Row,
  Col,
  Carousel
} from 'ant-design-vue';
import router from "@/router/index.js";

// 模拟旅游推荐数据
const travelRecommendations = ref([
  {
    image: new URL('../assets/train_banner.jpg', import.meta.url).href,

  },
  {
    image: new URL('../assets/banner12.jpg', import.meta.url).href,
  }
]);

// 车票预订点击事件处理
const handleTicketBooking = () => {
  console.log('点击了车票预订');
  // 这里可以添加跳转到车票预订页面的逻辑
};
//点击保险
const handleInsurance = () => {
  router.push({ path: '/insurance' });
  console.log('点击了保险');
  // 这里可以添加跳转到保险页面的逻辑
};

</script>

<style scoped>
.train-booking {
  padding: 20px;
}

.function-section {
  margin-bottom: 20px;
  text-align: center;
}

.special-service-section {
  margin-bottom: 20px;
}

.travel-recommendation-section {
  margin-bottom: 20px;
}

a-layout-header {
  background-color: #333;
  color: white;
  text-align: center;
  line-height: 64px;
}

a-layout-footer {
  text-align: center;
}

/* 增加卡片图片样式 */
.special-service-section .ant-card img {
  width: 100%;
  height: 200px; /* 建议设置固定高度保持卡片整齐 */
  object-fit: cover; /* 保持图片比例并填充容器 */
  display: block; /* 消除图片底部间隙 */
}

/* 统一卡片高度 */
.special-service-section .ant-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}

/* 卡片内容区域自适应 */
.special-service-section .ant-card-body {
  flex: 1;
  padding: 16px; /* 可选：调整内边距 */
}
/* 调整卡片高度和图片样式 */
.special-service-section .ant-card {
  min-height: 380px; /* 增加卡片最小高度 */
  display: flex;
  flex-direction: column;
}

.special-service-section .ant-card img {
  width: 100%;
  height: 280px; /* 增加图片显示高度 */
  object-fit: cover;
  display: block;
  transition: transform 0.3s ease; /* 添加悬停动效 */
}

/* 添加鼠标悬停效果 */
.special-service-section .ant-card:hover img {
  transform: scale(1.05);
}

/* 调整卡片标题样式 */
.special-service-section .ant-card-head-title {
  white-space: normal; /* 允许标题换行 */
  padding: 12px 0;
}

/* 调整卡片内容区域 */
.special-service-section .ant-card-body {
  flex: 1;
  padding: 16px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

/* 调整旅游推荐轮播图高度 */
.travel-recommendation-section .ant-carousel {
  height: 400px;
}

.travel-recommendation-section .ant-carousel img {
  width: 100%;
  height: 400px;
  object-fit: cover;
}
</style>