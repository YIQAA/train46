import { createRouter, createWebHistory } from 'vue-router';
import BuyTicket from "@/views/BuyTicket.vue";
import PaymentTimer from "@/views/PaymentTimer.vue";
import OrderList from '@/views/OrderList.vue'
import PaySuccess from "@/views/PaySuccess.vue";
import Login from "@/views/Login.vue";
import AdminPage from "@/components/adminMain.vue";
import userMain from "@/components/userMain.vue";



const routes = [
    {
        path: '/login',
        component: Login },
    {
        path: '/',
        redirect: '/login' // 添加默认重定向
    },
    {
        path: '/',
        name: 'userMain',
        component:  userMain,
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
            label: '个人中心',
            path: '/userDetail',
            name: 'userDetail',
            component: () => import( '../views/user/UserInfo.vue'),
            icon: 'icon-shouye',
            meta: { requiresAuth: false }
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
        },
        //保险
        {
            label: '保险',
            path: '/insurance',
            name: 'insurance',
            component: () => import( '../views/user/Insurance.vue'),
            icon: 'icon-baoxian',
            meta: { requiresAuth: true }
        },
        //问题反馈
        {
            label: '问题反馈',
            path: '/feedback',
            name: 'feedback',
            component: () => import( '../views/user/Feedback.vue'),
            icon: 'icon-baoxian',
            meta: { requiresAuth: true }
        },

        ]
    },
    {
        label: '管理员界面',
        path: '/adminPage',
        name: 'adminPage',
        component: AdminPage,
        icon: 'icon-guanli',
        meta: { requiresAuth: true },
        children: [
            {
                label: '用户管理',
                path: '/userManagement',
                name: 'userManagement',
                component: () => import( '../views/admin/UserManagement.vue'),
                icon: 'icon-yonghuguanli',
                meta: { requiresAuth: true }
            },

            {
                label: '订单管理',
                path: '/orderManagement',
                name: 'orderManagement',
                component: () => import( '../views/admin/OrderManagement.vue'),
                icon: 'icon-dingdan',
                meta: { requiresAuth: true }
            },
            {
                label: '车站管理',
                path: '/stationManagement',
                name: 'stationManagement',
                component: () => import( '../views/admin/stationManagement.vue'),
                icon: 'icon-yonghuguanli',
                meta: { requiresAuth: true }
            },
            {
                label: '车次管理',
                path: '/trainManagement',
                name: 'trainManagement',
                component: () => import( '../views/admin/trainManagement.vue'),
                icon: 'icon-yonghuguanli',
                meta: { requiresAuth: true }
            },
            {
                label: '管理员首页',
                path: '/indexManagement',
                name: 'indexManagement',
                component: () => import( '../views/admin/indexManagement.vue'),
                icon: 'icon-yonghuguanli',
                meta: { requiresAuth: true }
            },
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