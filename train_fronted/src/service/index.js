import http from './axios'

// 登录
const fetchLogin = async (body) => {
  const { data } = await http({
    method: 'POST',
    url: '/api/user-service/v1/login',
    data: body
  })
  http.defaults.headers.common['Authorization'] = data.data?.accessToken
  return data
}
// 注册
const fetchRegister = async (body) => {
  const { data } = await http({
    method: 'POST',
    url: '/api/user-service/register',
    data: body
  })
  return data
}
// 退出
const fetchLogout = async (body) => {
  const { data } = await http({
    method: 'GET',
    url: '/api/user-service/logout',
    data: body
  })
  http.defaults.headers.common['Authorization'] = null
  return data
}



// 将用户输入内容返回到后端ai
const fetchMessageByAI = async (params) => {
  const { data } = await http({
    method: 'GET',
    url: '/api/AIchat-service/generateAnswer',
    params,
    headers: {
      'Accept': 'application/json'
      // 'Accept': 'text/event-stream'
    }
  });
  return data;
};






// 车票查询
const fetchTicketSearch = async (params) => {
  const { data } = await http({
    method: 'GET',
    url: '/api/ticket-service/ticketQueryList',
    params
  })
  return data
}
// 车次站点
const fetchTrainStation = async (params) => {
  const { data } = await http({
    method: 'GET',
    url: '/api/ticket-service/train-station/query',
    params
  })
  return data
}
// 查询所有站点
const fetchStationAll = async () => {
  const { data } = await http({
    method: 'GET',
    url: '/api/ticket-service/city/all'
  })
  return data
}





// 订单列表
const fetchTicketList = async (params) => {
  const { data } = await http({
    method: 'GET',
    url: '/api/order-service/order/ticket/page',
    params
  })
  return data
}
// 取消订单
const fetchOrderCancel = async (params) => {
  const { data } = await http({
    method: 'POST',
    url: '/api/order-service/order/ticket/cancel',
    params
  })
  return data
}

// 根据订单号查询订单
const fetchOrderBySn = async (params) => {
  const { data } = await http({
    method: 'GET',
    url: '/api/order-service/order/queryBySn',
    params
  })
  return data
}

// 购买车票************
const fetchBuyTicket = async (body) => {
  const { data } = await http({
    method: 'POST',
    url: '/api/order-service/order/create',
    data: body
  })

  return data
}

// 乘客查询
export const fetchUserPassenger = async (params) => {
  const { data } = await http({
    method: 'GET',
    url: '/api/order-service/user/passenger',
    params
  })
  return data
}


// 支付
const fetchPay = async (params) => {
  const { data } = await http({
    method: 'POST',
    url: '/api/pay-service/pay/create',
    params
  })
  return data
}
// 订单状态
const fetchOrderStatus = async (params) => {
  const { data } = await http({
    method: 'GET',
    url: '/api/pay-service/payStatus/query',
    params
  })
  return data
}
// 退票
const fetchRefundTicket = async (body) => {
  const { data } = await http({
    method: 'POST',
    url: '/api/order-service/ticket/refund',
    data: body
  })
}










// 用户信息
const fechUserInfo = async (params) => {
  const { data } = await http({
    method: 'GET',
    url: '/api/user-service/query',
    params
  })
  return data
}

// 乘客删除
const fetchDeletePassenger = async (body) => {
  const { data } = await http({
    method: 'POST',
    url: '/api/user-service/passenger/remove',
    data: body
  })
  return data
}
// 乘客新增
const fetchAddPassenger = async (body) => {
  const { data } = await http({
    method: 'POST',
    url: '/api/user-service/passenger/save',
    data: body
  })
  return data
}
// 乘客修改
const fetchEditPassenger = async (body) => {
  const { data } = await http({
    method: 'POST',
    url: '/api/user-service/passenger/update',
    data: body
  })
  return data
}


// 修改用户信息
const fetchUserUpdate = async (body) => {
  const { data } = await http({
    method: 'POST',
    url: '/api/user-service/update',
    data: body
  })
  return data
}

// 我的票
const fetchMyTicket = async (params) => {
  const { data } = await http({
    method: 'GET',
    url: '/api/order-service/order/ticket/self/page',
    params
  })
  return data
}


// 车站查询
const fetchRegionStation = async (params) => {
  const { data } = await http({
    method: 'GET',
    url: '/api/ticket-service/region-station/query',
    params
  })
  return data
}

export {
  fetchLogin,
  fetchRegister,
  fetchTicketSearch,
  fetchRegionStation,
  fetchDeletePassenger,
  fetchAddPassenger,
  fetchEditPassenger,
  fetchLogout,
  fetchBuyTicket,
  fetchOrderBySn,
  fetchPay,
  fetchStationAll,
  fechUserInfo,
  fetchTrainStation,
  fetchTicketList,
  fetchOrderCancel,
  fetchOrderStatus,
  fetchUserUpdate,
  fetchMyTicket,
  fetchRefundTicket,
  fetchMessageByAI
}
