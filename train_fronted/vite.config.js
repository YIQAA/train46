import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  css: {
    preprocessorOptions: {
      less: {
        modifyVars: {
          'primary-color': '#1890ff', // 主色调
          'border-radius-base': '4px' // 圆角
        },
        javascriptEnabled: true,
        additionalData: `@import "ant-design-vue/dist/antd.less";` // 引入官方样式
      }
    }
  },


  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  // ...其他配置
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        // rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})


