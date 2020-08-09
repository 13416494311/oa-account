import request from '@/utils/request'

// 查询出租车发票列表
export function listTaxiTicket(query) {
  return request({
    url: '/invoice/taxiTicket/list',
    method: 'get',
    params: query
  })
}

// 查询出租车发票详细
export function getTaxiTicket(id) {
  return request({
    url: '/invoice/taxiTicket/' + id,
    method: 'get'
  })
}

// 校验飞机票号码是否已存在
export function checkTaxiTicketExist(data) {
  return request({
    url: '/invoice/taxiTicket/checkTaxiTicketExist' ,
    method: 'post',
    data: data
  })
}

// 新增出租车发票
export function addTaxiTicket(data) {
  return request({
    url: '/invoice/taxiTicket',
    method: 'post',
    data: data
  })
}

// 新增机票行程单
export function addTaxiTicketByAtt(data) {
  return request({
    url: '/invoice/taxiTicket/addByAtt',
    method: 'post',
    data: data
  })
}

// 修改出租车发票
export function updateTaxiTicket(data) {
  return request({
    url: '/invoice/taxiTicket',
    method: 'put',
    data: data
  })
}

// 删除出租车发票
export function delTaxiTicket(id) {
  return request({
    url: '/invoice/taxiTicket/' + id,
    method: 'delete'
  })
}

// 导出出租车发票
export function exportTaxiTicket(query) {
  return request({
    url: '/invoice/taxiTicket/export',
    method: 'get',
    params: query
  })
}
