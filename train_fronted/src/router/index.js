import { createRouter, createWebHistory } from 'vue-router';
import Login from '../views/Login.vue';
import BuyTicket from "@/views/BuyTicket.vue";
import PaymentTimer from "@/views/PaymentTimer.vue";
import OrderList from '@/views/order-list/index.vue'
import PaySuccess from "@/views/PaySuccess.vue";
import Login2 from "@/views/Login2.vue";


const routes = [
    { path: '/login', component: Login2 },
    {
        path: '/',
        redirect: '/login' // 添加默认重定向
    },
    {
        path: '/',
        name: 'main',
        component: () => import(/* webpackChunkName: "about" */ '../views/main.vue'),
        meta: {
            loginRequired: true
        },
        children: [{
            label: '欢迎页',
            path: '/welcome',
            name: 'welcome',
            component: () => import( '../views/Welcome.vue')
        },
        {
            label: '车票查询',
            path: '/ticketSearch',
            name: 'ticketSearch',
            component: () => import( '../views/TicketSearch.vue'),
            icon: 'icon-chaxun',
            meta: { requiresAuth: false }
        },
        {
            label: '购买车票',
            path: '/buyTicket',
            name: 'buyTicket',
            component: BuyTicket,
            icon: 'icon-goumai',
            meta: { requiresAuth: true }
        },
        {
            label: '等待支付订单',
            path: '/paymentTimer',
            name: 'paymentTimer',
            component: PaymentTimer,
            icon: 'icon-dingdan',
            meta: { requiresAuth: true }
        },
        {
            label: '智能客服',
            path: '/chatWindow',
            name: 'chatWindow',
            component: () => import( '../views/smart-assistant/ChatWindow.vue'),
            icon: 'icon-dingdan',
            meta: { requiresAuth: true }
        },
        {
            label: '订单列表',
            path: '/orderList',
            name: 'orderList',
            component: OrderList,
            icon: 'icon-zhifubao0',
            meta: { requiresAuth: true }
        },
        {
            label: '支付成功',
            path: '/paySuccess',
            name: 'paySuccess',
            component: PaySuccess,
            icon: 'icon-zhifubao0',
            meta: { requiresAuth: true }
        }

        ]
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

router.beforeEach((to, from, next) => {
    if (to.meta.requiresAuth && !localStorage.getItem('token')) {
        next('/login');
    } else {
        next();
    }
});

export default router;