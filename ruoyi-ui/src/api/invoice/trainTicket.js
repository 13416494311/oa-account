import request from '@/utils/request'

// 查询火车票列表
export function listTrainTicket(query) {
  return request({
    url: '/invoice/trainTicket/list',
    method: 'get',
    params: query
  })
}

// 查询火车票详细
export function getTrainTicket(id) {
  return request({
    url: '/invoice/trainTicket/' + id,
    method: 'get'
  })
}


// 校验车票号码是否已存在
export function checkTrainTicketExist(data) {
  return request({
    url: '/invoice/trainTicket/checkTrainTicketExist' ,
    method: 'post',
    data: data
  })
}

// 新增火车票
export function addTrainTicket(data) {
  return request({
    url: '/invoice/trainTicket',
    method: 'post',
    data: data
  })
}

// 新增火车票
export function addTrainTicketByAtt(data) {
  return request({
    url: '/invoice/trainTicket/addByAtt',
    method: 'post',
    data: data
  })
}

// 修改火车票
export function updateTrainTicket(data) {
  return request({
    url: '/invoice/trainTicket',
    method: 'put',
    data: data
  })
}

// 删除火车票
export function delTrainTicket(id) {
  return request({
    url: '/invoice/trainTicket/' + id,
    method: 'delete'
  })
}

// 导出火车票
export function exportTrainTicket(query) {
  return request({
    url: '/invoice/trainTicket/export',
    method: 'get',
    params: query
  })
}
