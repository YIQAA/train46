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


import {fetchMessageByAI} from "@/service/index.js";
let eventSource;
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
    }
  },
  methods: {

    async sendMessage() {
      const input = this.userInput.trim()
      if (!input || this.sending) return

      this.sending = true

      this.messages.push({
        id: Date.now(),
        type: 'text',
        content: input,
        isUser: true
      })

      await this.getBotResponse(input)

      this.userInput = ''
      this.sending = false

      this.$nextTick(() => {
        const container = this.$refs.messagesContainer
        container.scrollTop = container.scrollHeight
      })
    },

    handleButtonClick(btn) {
      this.messages.push({
        id: Date.now(),
        type: 'text',
        content: btn.command,
        isUser: true
      })
      this.getBotResponse(btn.command)
    },
    async getBotResponse(input) {
      if (eventSource) {
        eventSource.close();
      }

      const newMessageIndex = this.messages.length;
      this.messages.push({
        id: Date.now(),
        type: 'text',
        content: '',
        isUser: false
      });

      // sse: 服务端推送 Server-Sent Events
      eventSource = new EventSource(`http://localhost:8080/api/AIchat-service/generateStreamAsString?message=${input}`);
      eventSource.onmessage = (event) => {
        if (event.data === '[complete]') {
          eventSource.close();
          return;
        }
        this.messages[newMessageIndex].content += event.data;
      };
      eventSource.onopen = (event) => {
        this.messages[newMessageIndex].content = '';
      };
    }

    // async getBotResponse(input) {
    //   try {
    //     const response = await fetchMessageByAI({message: input});
    //     console.log(response);
    //     this.messages.push({
    //       id: Date.now(),
    //       type: 'text',
    //       content: response,
    //       isUser: false
    //     });
    //   } catch (error) {
    //     console.error('获取AI回复出错:', error);
    //     this.messages.push({
    //       id: Date.now(),
    //       type: 'text',
    //       content: '获取回复时出现错误，请稍后再试。',
    //       isUser: false
    //     });
    //   }
    // }
  }
}
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
