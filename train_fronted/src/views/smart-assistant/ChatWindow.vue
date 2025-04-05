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
                  <a-table
                      :columns="message.columns"
                      :data-source="message.data"
                      :pagination="false"
                      size="small"
                  />
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

<script>
import axios from 'axios'; // 引入axios

// 假设http是axios实例
const http = axios.create({
  baseURL: 'http://localhost:8080'
});

const fetchMessageByAI = async (params) => {
  const { data } = await http({
    method: 'GET',
    url: '/api/AIchat-service/generateStreamAsString',
    params
  });
  return data;
};

export default {
  data() {
    return {
      userInput: '',
      messages: [
        {
          id: 1,
          type: 'text',
          content: '您好！我是火车票务助手，请问有什么可以帮您？',
          isUser: false
        }
      ],
      sending: false
    };
  },
  methods: {
    async sendMessage() {
      const input = this.userInput.trim();
      if (!input || this.sending) return;

      this.sending = true;

      this.messages.push({
        id: Date.now(),
        type: 'text',
        content: input,
        isUser: true
      });

      this.messages.push({
        id: Date.now(),
        type: 'text',
        content: 'waiting...',
        isUser: false
      });

      try {
        const response = await fetchMessageByAI({ message: input });
        const lastMessageIndex = this.messages.length - 1;
        this.messages[lastMessageIndex].content = response;
      } catch (error) {
        console.error('请求出错:', error);
        const lastMessageIndex = this.messages.length - 1;
        this.messages[lastMessageIndex].content = '请求出错，请稍后再试';
      }

      this.userInput = '';
      this.sending = false;

      this.$nextTick(() => {
        const container = this.$refs.messagesContainer;
        container.scrollTop = container.scrollHeight;
      });
    },

    handleButtonClick(btn) {
      this.messages.push({
        id: Date.now(),
        type: 'text',
        content: btn.command,
        isUser: true
      });
      this.getBotResponse(btn.command);
    },

    async getBotResponse(input) {
      this.messages.push({
        id: Date.now(),
        type: 'text',
        content: 'waiting...',
        isUser: false
      });

      try {
        const response = await fetchMessageByAI({ message: input });
        const lastMessageIndex = this.messages.length - 1;
        this.messages[lastMessageIndex].content = response;
      } catch (error) {
        console.error('请求出错:', error);
        const lastMessageIndex = this.messages.length - 1;
        this.messages[lastMessageIndex].content = '请求出错，请稍后再试';
      }
    }


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
