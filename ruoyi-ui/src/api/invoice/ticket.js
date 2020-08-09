import request from '@/utils/request'



// 查询票据信息列表
export function listNoPage(uuid) {
  return request({
    url: '/invoice/ticket/listNoPage/' + uuid,
    method: 'get'
  })
}
