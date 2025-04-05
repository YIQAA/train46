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
      await new Promise(resolve => setTimeout(resolve, 800))

      let response = {
        type: 'text',
        content: "您好！我是火车票务助手，请问有什么可以帮您？<br>常用功能：<br>1. <b>车次查询</b><br>2. <b>余票查询</b><br>3. <b>订单管理</b>"
      }

      if (input.includes('车次')) {
        response = {
          type: 'table',
          columns: [
            { title: '车次', dataIndex: 'train' },
            { title: '时间', dataIndex: 'time' },
            { title: '余票', dataIndex: 'tickets' }
          ],
          data: [
            { train: 'G101', time: '08:00-12:00', tickets: '二等座 120张' },
            { train: 'K202', time: '10:30-18:00', tickets: '硬卧 45张' }
          ]
        }
      } else if (input.includes('余票')) {
        response = {
          type: 'link',
          content: '点击查看实时余票信息',
          url: 'https://railway.com/tickets'
        }
      } else if (input.includes('订单')) {
        response = {
          type: 'buttons',
          buttons: [
            { text: '查询未支付订单', command: '显示未支付订单' },
            { text: '查看历史订单', command: '显示历史订单' }
          ]
        }
      }
      else if (input.includes('官网')) {
        response = {
          type: 'link',
          content: '中国铁路客户服务中心（12306官网）',
          url: 'https://www.12306.cn'
        }

      }

      this.messages.push({
        id: Date.now(),
        isUser: false,
        ...response
      })
    }
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