import axios from 'axios';

const service = axios.create({
    baseURL: '/api',
    timeout: 5000
});

service.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = token;
    }
    return config;
});

export default service;