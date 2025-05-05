<template>
  <a-card class="feedback-container">
    <h2 class="form-title">意见反馈</h2>

    <!-- 反馈表单 -->
    <a-form
        :model="formState"
        @finish="handleSubmit"
        class="feedback-form"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 18 }"
    >
      <a-form-item label="反馈类型" name="type">
        <a-radio-group v-model:value="formState.type">
          <a-radio-button value="suggestion">建议</a-radio-button>
          <a-radio-button value="complaint">投诉</a-radio-button>
          <a-radio-button value="system_error">系统问题</a-radio-button>
        </a-radio-group>
      </a-form-item>

      <a-form-item
          label="问题标题"
          name="title"
          :rules="[{ required: true, message: '请输入问题标题' }]"
      >
        <a-input
            v-model:value="formState.title"
            placeholder="请简要描述您的问题"
        />
      </a-form-item>

      <a-form-item
          label="详细描述"
          name="content"
          :rules="[{ required: true, message: '请输入详细描述' }]"
      >
        <a-textarea
            v-model:value="formState.content"
            placeholder="请详细描述您遇到的问题或建议..."
            :rows="4"
        />
      </a-form-item>

      <a-form-item label="联系方式" name="contact">
        <a-input
            v-model:value="formState.contact"
            placeholder="手机/邮箱（选填）"
        />
      </a-form-item>

      <a-form-item :wrapper-col="{ offset: 4 }">
        <a-button
            type="primary"
            html-type="submit"
            :loading="submitting"
        >
          提交反馈
        </a-button>
      </a-form-item>
    </a-form>

    <!-- 反馈历史 -->
    <a-divider />
    <h3>历史反馈</h3>
    <a-list
        :data-source="historyList"
        :loading="loadingHistory"
        item-layout="vertical"
    >
      <template #renderItem="{ item }">
        <a-list-item>
          <template #actions>
            <span>{{ formatTime(item.created_at) }}</span>
            <a-tag :color="statusColors[item.status]">
              {{ statusMap[item.status] }}
            </a-tag>
          </template>
          <a-list-item-meta
              :title="item.title"
              :description="`类型：${typeMap[item.type]}`"
          />
          <div class="content">{{ item.content }}</div>
          <div v-if="item.reply_content" class="reply-content">
            <a-alert
                message="管理员回复"
                type="info"
                :description="item.reply_content"
                show-icon
            />
          </div>
        </a-list-item>
      </template>
    </a-list>
  </a-card>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import dayjs from 'dayjs'

// 表单状态
const formState = reactive({
  type: 'suggestion',
  title: '',
  content: '',
  contact: ''
})

const submitting = ref(false)

// 反馈类型映射
const typeMap = {
  suggestion: '建议',
  complaint: '投诉',
  system_error: '系统问题'
}

// 状态映射
const statusMap = {
  pending: '待处理',
  processing: '处理中',
  resolved: '已解决',
  rejected: '已驳回'
}

const statusColors = {
  pending: 'gold',
  processing: 'blue',
  resolved: 'green',
  rejected: 'red'
}

// 历史记录
const historyList = ref([])
const loadingHistory = ref(false)

// 格式化时间
const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

// 提交处理
const handleSubmit = async () => {
  try {
    submitting.value = true

    // 模拟API调用
    // const { data } = await axios.post('/api/feedback', formState)
    console.log('提交数据：', formState)
    await new Promise(resolve => setTimeout(resolve, 1000))

    message.success('反馈提交成功')
    formState.title = ''
    formState.content = ''
    formState.contact = ''

    // 刷新历史记录
    loadHistory()
  } catch (error) {
    message.error('提交失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

// 加载历史记录
const loadHistory = async () => {
  try {
    loadingHistory.value = true

    // 模拟数据
    historyList.value = [
      {
        id: 1,
        type: 'system_error',
        title: '支付页面加载缓慢',
        content: '在点击支付按钮后页面响应时间过长',
        status: 'resolved',
        created_at: '2023-08-20 14:30:00',
        reply_content: '已优化支付接口响应速度，感谢反馈'
      }
    ]

    // 实际调用
    // const { data } = await axios.get('/api/feedback/history')
  } catch (error) {
    message.error('加载历史记录失败')
  } finally {
    loadingHistory.value = false
  }
}

// 初始化加载
onMounted(() => {
  loadHistory()
})
</script>

<style scoped>
.feedback-container {
  max-width: 800px;
  margin: 20px auto;
}

.form-title {
  color: rgba(0, 0, 0, 0.85);
  font-size: 20px;
  margin-bottom: 24px;
  text-align: center;
}

.content {
  color: rgba(0, 0, 0, 0.65);
  margin-bottom: 12px;
}

.reply-content {
  margin-top: 16px;
}
</style>