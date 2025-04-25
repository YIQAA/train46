<template>
  <a-layout class="chat-container">
    <a-layout-content class="chat-content">
      <div class="chat-messages" ref="messagesContainer">
        <div
            v-for="message in messages"
            :key="message.id"
            :class="['message', message.isUser ? 'user-message' : 'bot-message']"
        >
          <a-comment>
            <template #avatar>
              <a-avatar
                  :style="message.isUser ? { backgroundColor: '#1890ff' } : { backgroundColor: '#f56a00' }"
              >
                {{ message.isUser ? '我' : 'AI' }}
              </a-avatar>
            </template>
            <template #content>
              <div class="rich-content">
                <!-- 文本类型 -->
                <template v-if="message.type === 'text'">
                  <div v-html="message.content"></div>
                </template>

                <!-- 链接类型 -->
                <div v-if="message.type === 'link'" class="content-link">
                  <a :href="message.url" target="_blank">{{ message.content }}</a>
                </div>

                <!-- 表格类型 -->
                <div v-if="message.type === 'table'" class="content-table">
                  <div class="table-header">{{ message.content.text }}</div>
                  <br>
                  <a-table
                      :columns="message.content.columns"
                      :data-source="message.content.data"
                      :pagination="false"
                      size="small"
                  >
                    <template #bodyCell="{ column, record }">
                      <template v-if="column.dataIndex === 'operation'">
                        <a-button
                            type="link"
                            size="small"
                            @click="handleOperationClick(record.operation)"
                            v-if="record.operation"
                        >
                          立即购票
                        </a-button>
                      </template>
                    </template>
                  </a-table>
                </div>

                <!-- 按钮组类型 -->
                <div v-if="message.type === 'buttons'" class="content-buttons">
                  <a-button
                      v-for="(btn, index) in message.buttons"
                      :key="index"
                      type="primary"
                      size="small"
                      @click="handleButtonClick(btn)"
                      class="action-btn"
                  >
                    {{ btn.text }}
                  </a-button>
                </div>
              </div>
            </template>
          </a-comment>
        </div>
      </div>
    </a-layout-content>

    <a-layout-footer class="input-footer">
      <div class="input-area">
        <a-input
            v-model:value="userInput"
            @pressEnter="sendMessage"
            placeholder="请输入您的问题..."
            class="message-input"
            size="large"
        >
          <template #suffix>
            <a-button
                type="primary"
                shape="circle"
                icon="发送"
                @click="sendMessage"
                :loading="sending"
                class="send-btn"
            />
          </template>
        </a-input>
      </div>
    </a-layout-footer>
  </a-layout>
</template>


<script setup>
import { ref, reactive, nextTick } from 'vue';
import { fetchMessageByAI } from "@/service/index.js";

// 响应式数据
const userInput = ref('');
const messages = ref([
  {
    id: 1,
    type: 'text',
    content: '您好！我是火车票务助手，请问有什么可以帮您？',
    isUser: false
  }
]);
const sending = ref(false);
const messagesContainer = ref(null);

// 事件处理
const handleButtonClick = (btn) => {
  messages.value.push({
    id: Date.now(),
    type: 'text',
    content: btn.command,
    isUser: true
  });
  getBotResponse(btn.command);
};

// 获取AI回复
const getBotResponse = async (input, tempId) => {
  try {
    const response = await fetchMessageByAI({ message: input });
    const index = messages.value.findIndex(msg => msg.id === tempId);
    console.log(response);

    // 增强表格处理逻辑
    const processedContent = response.type === 'table' ? {
      text: response.content.text,
      columns: response.content.columns.map((title, index) => ({
        title,
        dataIndex: `${index}`,
        key: `${index}`,
        // 添加操作列特殊处理
        ...(title === '操作' ? {
          dataIndex: 'operation',
          key: 'operation',
          width: 120
        } : {})
      })),
      data: response.content.data.map((row, i) => {
        const record = { key: i };
        row.forEach((value, index) => {
          // 将"操作"列数据映射到operation字段
          record[response.content.columns[index] === '操作' ? 'operation' : `${index}`] = value;
        });
        return record;
      })
    } : response.content.text;

    const newMessage = {
      id: tempId,
      type: response.type,
      content: processedContent,
      isUser: false
    };



    if (index !== -1) {
      messages.value.splice(index, 1, newMessage);
    } else {
      messages.value.push(newMessage);
    }
  } catch (error) {
    console.error('获取AI回复出错:', error);
    const errorMessage = {
      id: tempId,
      type: 'text',
      content: '获取回复时出现错误，请稍后再试。',
      isUser: false
    };

    const index = messages.value.findIndex(msg => msg.id === tempId);
    if (index !== -1) {
      messages.value.splice(index, 1, errorMessage);
    } else {
      messages.value.push(errorMessage);
    }
  }
};

// 添加操作点击处理
const handleOperationClick = (url) => {
  window.open(url, '_blank', 'noopener,noreferrer');
};

// 发送消息
const sendMessage = async () => {
  const input = userInput.value.trim();
  if (!input || sending.value) return;

  sending.value = true;

  // 添加用户消息
  messages.value.push({
    id: Date.now(),
    type: 'text',
    content: input,
    isUser: true
  });

  // 添加临时AI消息
  const tempId = Date.now() + 1;
  messages.value.push({
    id: tempId,
    type: 'text',
    content: '正在思考中...',
    isUser: false
  });

  // 立即滚动到底部
  await nextTick();
  messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;

  userInput.value = '';

  try {
    await getBotResponse(input, tempId);
  } finally {
    sending.value = false;
    // 最终滚动到底部
    await nextTick();
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
  }
};
</script>

<style scoped lang="less">
.rich-content {
  .content-link {
    a {
      color: @primary-color;
      text-decoration: underline;
    }
  }

  .content-table {
    margin: 10px 0;

    :deep(.ant-table) {
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
    }
  }

  .content-buttons {
    display: flex;
    gap: 8px;
    margin-top: 10px;

    .action-btn {
      border-radius: 15px;
      padding: 0 15px;
    }
  }
}
.message.loading-message :deep(.ant-comment-content) {
  color: #999;
  font-style: italic;
}

// 修改样式部分
.input-footer {
  padding: 16px;
  border-top: 1px solid #e8e8e8;
  background: #fff;

  .input-area {
    display: flex;
    width: 100%;

    :deep(.ant-input-affix-wrapper) {
      flex: 1;
      width: 100%;
    }

    :deep(.ant-input) {
      border-radius: 24px;
      padding: 12px 24px;
      width: 100%;
    }

    :deep(.ant-btn) {
      margin-left: 12px;
      width: 40px;
      height: 40px;
    }
  }
}


.chat-container {
  max-width: 800px;
  margin: 20px auto;
  border: 1px solid #e8e8e8;
  border-radius: 8px;
  overflow: hidden;

  .chat-content {
    height: 500px;
    padding: 16px;
    background-color: #fafafa;
  }

  .chat-messages {
    height: 100%;
    overflow-y: auto;
    padding: 16px;

    .message {
      margin: 12px 0;

      :deep(.ant-comment-content) {
        padding: 12px 16px;
        border-radius: 8px;
        background: #fff;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
      }
    }

    .user-message {
      :deep(.ant-comment-content) {
        background-color: @primary-1;
        border: 1px solid @primary-3;
      }
    }

    .bot-message {
      :deep(.ant-comment-content) {
        background-color: #fff;
        border: 1px solid #e8e8e8;
      }
    }
  }


}

// 滚动条样式
.chat-messages::-webkit-scrollbar {
  width: 6px;
}

.chat-messages::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: #d9d9d9;
  border-radius: 4px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: #bfbfbf;
}
</style>
