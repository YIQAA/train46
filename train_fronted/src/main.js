import { createApp } from 'vue'
import Antd, {notification} from 'ant-design-vue'
import App from './App.vue'
import router from './router'
import store from './store'
import 'ant-design-vue/dist/antd.css';
import * as Icons from '@ant-design/icons-vue'
import axios from 'axios'
// import './assets/js/enums'

const app = createApp(App)
app.use(Antd).use(store).use(router).mount('#app')

// 全局使用图标
const icons = Icons
for (const icon in icons) {
    app.component(icon, icons[icon])
}
