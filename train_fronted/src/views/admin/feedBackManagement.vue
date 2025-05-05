<template>
  <a-card title="投诉管理" class="complaint-manage">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <a-input-search
          v-model:value="searchKey"
          placeholder="输入关键词搜索"
          @search="loadComplaints"
          enter-button
          style="max-width: 400px"
      />
    </div>

    <!-- 投诉表格 -->
    <a-table
        :data-source="complaintList"
        :columns="columns"
        :pagination="pagination"
        :loading="loading"
        row-key="id"
    >

      <template #type="{ text }">
        <div>{{ typeMap[text] }}</div>
      </template>
      <!-- 状态标签 -->
      <template #status="{ text }">
        <a-tag :color="statusColors[text]">
          {{ statusMap[text] }}
        </a-tag>
      </template>

      <!-- 操作列 -->
      <template #action="{ record }">
        <a-button type="link" @click="showReplyModal(record)">回复</a-button>
        <a-dropdown>
          <template #overlay>
            <a-menu @click="handleStatusChange($event, record.feedbackId)">
              <a-menu-item v-for="(label, key) in statusMap" :key="key">
                {{ label }}
              </a-menu-item>
            </a-menu>
          </template>
          <a-button type="link">修改状态</a-button>
        </a-dropdown>
      </template>

      <!-- 回复内容 -->
      <template #replyContent="{ text }">
        <div v-if="text" class="reply-content">
          <a-alert :message="text" type="info" show-icon />
        </div>
        <span v-else>-</span>
      </template>
    </a-table>

    <!-- 回复模态框 -->
    <a-modal
        v-model:visible="replyModalVisible"
        title="回复投诉"
        @ok="handleReplySubmit"
        :confirm-loading="replying"
    >
      <a-form :model="replyForm" layout="vertical">
        <a-form-item
            label="回复内容"
            name="content"
            :rules="[{ required: true, message: '请输入回复内容' }]"
        >
          <a-textarea
              v-model:value="replyForm.content"
              :rows="4"
              placeholder="请输入对投诉的回复..."
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-card>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import {fetchComplainList, fetchComplainReply, fetchComplainUpdate} from "../../service/index";
// 表格列配置
const columns = [
  {
    title: '投诉标题',
    dataIndex: 'title',
    width: 200
  },
  {
    title: '类型',
    dataIndex: 'feedbackType',
    slots: { customRender: 'type' },
    width: 100
  },
  {
    title: '状态',
    dataIndex: 'status',
    slots: { customRender: 'status' },
    width: 100

  },
  {
    title: '投诉内容',
    dataIndex: 'content',
    ellipsis: true
  },
  {
    title: '提交时间',
    dataIndex: 'createdAt',
    width: 180
  },
  {
    title: '回复内容',
    dataIndex: 'replyContent',
    slots: { customRender: 'replyContent' }
  },
  {
    title: '操作',
    slots: { customRender: 'action' },
    width: 150
  }
]

// 投诉数据
const complaintList = ref([])
const loading = ref(false)
const currentComplaintId = ref(null)
const searchKey = ref('')
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0
})

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

// 类型映射
const typeMap = {
  complaint: '投诉',
  suggestion: '建议',
  system_error: '系统问题'
}

// 回复对话框
const replyModalVisible = ref(false)
const replying = ref(false)
const replyForm = reactive({
  content: ''
})

// 加载投诉列表
const loadComplaints = async () => {
  try {
    loading.value = true

    fetchComplainList().then(res => {
      complaintList.value = res
    })

  } catch (error) {
    message.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 显示回复对话框
const showReplyModal = (record) => {
  currentComplaintId.value = record.feedbackId
  replyForm.content = record.replyContent || ''
  replyModalVisible.value = true
}

// 提交回复
const handleReplySubmit = async () => {
  try {
    replying.value = true
    fetchComplainReply({
      feedbackId: currentComplaintId.value,
      replyContent: replyForm.content}
    ).then(res => {
      console.log('res',res)
    })
    message.success('回复成功')
    replyModalVisible.value = false
    // 更新本地数据
    await loadComplaints()
  } catch (error) {
    message.error('回复失败')
  } finally {
    replying.value = false
  }

}

// 修改状态
const handleStatusChange = async ({ key }, complaintId) => {
  try {

    fetchComplainUpdate({
      feedbackId: complaintId,
      status: key}
    ).then(res => {
      console.log('res',res)
    })



    message.success('状态更新成功')
    // 更新本地数据
    await loadComplaints()

  } catch (error) {
    message.error('状态更新失败')
  }
}

// 初始化加载
onMounted(() => {
  loadComplaints()
})
</script>

<style scoped>
.complaint-manage {
  margin: 20px;
}

.search-bar {
  margin-bottom: 20px;
}

.reply-content {
  max-width: 300px;
}
</style>