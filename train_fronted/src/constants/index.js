const SEAT_CLASS_TYPE_LIST = [
  { label: '特等座', code: 1 },
  { label: '商务座', code: 2 },
  { label: '一等座', code: 3 },
  { label: '二等座', code: 4 },
  { label: '软卧', code: 5 },
  { label: '硬卧', code: 6 },
  { label: '软座', code: 7 },
  { label: '硬座', code: 8 },
  { label: '无座', code: 9 }
]

const TRAIN_BRAND_LIST = [
  { code: 1, label: '动车组' },
  { code: 2, label: '普速列车' }
]


const TICKET_TYPE_LIST = [
  {
    label: '成人票',
    value: 0
  },
  {
    label: '学生票',
    value: 1
  }
]

const DISCOUNTS_TYPE = [
  {
    label: '成人',
    value: 0
  },
  {
    label: '儿童',
    value: 1
  },
  {
    label: '学生',
    value: 2
  },
  {
    label: '残疾军人',
    value: 3
  }
]



const TICKET_STATUS_LIST = [
  {
    label: '待支付',
    value: 0
  },
  {
    label: '已支付',
    value: 1
  },
  {
    label: '已取消',
    value: 2
  },
  {
    label: '已完成',
    value: 3
  },
  {
    label: '已退票',
    value: 4
  },
  {
    label: '已改签',
    value: 5
  }
]

const ID_CARD_TYPE = [
  {
    label: '中国居民身份证',
    value: 0
  },
  {
    label: '护照',
    value: 1
  }
]

const SEAT_NAME_MAP = [
  {
    0: {
      businessClass: '商务座',
      firstClass: '一等座',
      secondClass: '二等座'
    }
  },
  {
    1: {
      businessClass: '商务座',
      firstClass: '商务座',
      secondClass: '商务座'
    }
  }
]


const BANK_LIST = [
  {
    img: 'https://epay.12306.cn/pay/pages/web/images/bank_zfb.gif',
    name: '支付宝',
    value: 0
  },
  {
    img: 'https://epay.12306.cn/pay/pages/web/images/bank_wx.gif',
    name: '微信',
    value: 1
  },
  {
    img: 'https://epay.12306.cn/pay/pages/web/images/bank_gsyh2.gif',
    name: '工商银行',
    value: 10
  },
  {
    img: 'https://epay.12306.cn/pay/pages/web/images/bank_nyyh2.gif',
    name: '农业银行',
    value: 9
  },
  {
    img: 'https://epay.12306.cn/pay/pages/web/images/bank_zgyh2.gif',
    name: '中国银行',
    value: 8
  },
  {
    img: 'https://epay.12306.cn/pay/pages/web/images/bank_jsyh2.gif',
    name: '建设银行',
    value: 7
  },
  {
    img: 'https://epay.12306.cn/pay/pages/web/images/bank_zsyh2.gif',
    name: '招商银行',
    value: 6
  },
  {
    img: 'https://epay.12306.cn/pay/pages/web/images/bank_ycyh.gif',
    name: '邮储银行',
    value: 5
  },
  {
    img: 'https://epay.12306.cn/pay/pages/web/images/bank_zgyl.gif',
    name: '中国银联',
    value: 4
  },
  {
    img: 'https://epay.12306.cn/pay/pages/web/images/bank_ztytk.gif',
    name: '中铁银通卡',
    value: 3
  },
  {
    img: 'https://epay.12306.cn/pay/pages/web/images/bank_wk.gif',
    name: '国际卡',
    value: 12
  },
  {
    img: 'https://epay.12306.cn/pay/pages/web/images/bank_jtyh.png',
    name: '交通银行',
    value: 13
  }
]

const REGIN_MAP = [
  {
    value: '0',
    label: '中国'
  }
]
const CHECK_STATUS = [
  {
    value: 'active',
    label: '通过'
  },
  {
    value: 'inactive',
    label: '未通过'
  }
]
const INSURANCE_TYPE= [
  {
    value: 1,
    label: '出行无忧保'
  },
  {
    value: 2,
    label: '旅程预定取消险'
  },
  {
    value: 3,
    label: '国内旅行险'
  },
  {
    value: 4,
    label: '空铁联运险'
  }
 ]
export {
  TICKET_TYPE_LIST,
  SEAT_NAME_MAP,
  ID_CARD_TYPE,
  BANK_LIST,
  SEAT_CLASS_TYPE_LIST,
  TRAIN_BRAND_LIST,
  TICKET_STATUS_LIST,
  DISCOUNTS_TYPE,
  REGIN_MAP,
  CHECK_STATUS,
  INSURANCE_TYPE

}
