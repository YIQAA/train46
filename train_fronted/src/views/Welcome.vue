<template>
  <div class="train-booking">
    <!-- 特色服务区 -->
    <div class="special-service-section">
      <h2>特色服务</h2>
      <a-row gutter="16">
        <a-col :span="6">
          <a-card title="订餐服务" @click="handleMealBooking">
            <img name="ii" src="../assets/train_banner.jpg" alt="订餐服务图片" />
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card title="接送站服务" @click="handlePickupAndDropoff">
            <img src="https://via.placeholder.com/150" alt="接送站服务图片" />
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card title="重点旅客预约" @click="handleSpecialPassengerReservation">
            <img src="https://via.placeholder.com/150" alt="重点旅客预约图片" />
          </a-card>
        </a-col>
        <a-col :span="6">
          <a-card title="遗失物品查找" @click="handleLostItemSearch">
            <img src="https://via.placeholder.com/150" alt="遗失物品查找图片" />
          </a-card>
        </a-col>
      </a-row>
    </div>

    <!-- 旅游推荐区 -->
    <div class="travel-recommendation-section">
      <h2>旅游推荐</h2>
      <a-carousel autoplay>
        <a-carousel-item v-for="(item, index) in travelRecommendations" :key="index">
          <img :src="item.image" alt="旅游推荐图片" />
          <h3>{{ item.title }}</h3>
          <p>{{ item.description }}</p>
        </a-carousel-item>
      </a-carousel>
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

// 模拟旅游推荐数据
const travelRecommendations = ref([
  {
    image: '../assets/train_banner.jpg',
    title: '环西部火车游 - 陇上江南行',
    description: '体验陇上江南的美丽风光，探索独特的人文风情。'
  },
  {
    image: '../assets/train_banner.jpg',
    title: '环西部火车游 - 华夏寻根之旅',
    description: '追寻华夏文明的根源，感受历史的厚重。'
  }
]);

// 车票预订点击事件处理
const handleTicketBooking = () => {
  console.log('点击了车票预订');
  // 这里可以添加跳转到车票预订页面的逻辑
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
</style>