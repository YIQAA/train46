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
        <a-radio-group v-model:value="formState.feedbackType">
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
            <span>{{ formatTime(item.createdAt) }}</span>
            <a-tag :color="statusColors[item.status]">
              {{ statusMap[item.status] }}
            </a-tag>
          </template>
          <a-list-item-meta
              :title="item.title"
              :description="`类型：${typeMap[item.feedbackType]}`"
          />
          <div class="content">{{ item.content }}</div>
          <div v-if="item.replyContent" class="reply-content">
            <a-alert
                message="管理员回复"
                type="info"
                :description="item.replyContent"
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
import {fetchFeedBack,fetchUserFeedBack} from "@/service/index.js";
// 表单状态
const formState = reactive({
  feedbackType: 'suggestion',
  title: '',
  content: '',
  contact: '',
  userId:  localStorage.getItem('userId')
})

const submitting = ref(false)


const typeMap = {
  suggestion: '建议',
  complaint: '投诉',
  system_error: '系统问题'
}


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
    fetchFeedBack (formState).then((res) => {
      console.log('res',res)
    })
    console.log('提交数据：', JSON.parse(JSON.stringify(formState)))
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
    fetchUserFeedBack({userId: formState.userId}).then((res) => {
      console.log('res',res)
      historyList.value = res


    })
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