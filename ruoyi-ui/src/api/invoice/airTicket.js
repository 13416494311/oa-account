import request from '@/utils/request'

// 查询机票行程单列表
export function listAirTicket(query) {
  return request({
    url: '/invoice/airTicket/list',
    method: 'get',
    params: query
  })
}

// 查询机票行程单详细
export function getAirTicket(id) {
  return request({
    url: '/invoice/airTicket/' + id,
    method: 'get'
  })
}

// 校验飞机票号码是否已存在
export function checkAirTicketExist(data) {
  return request({
    url: '/invoice/airTicket/checkAirTicketExist' ,
    method: 'post',
    data: data
  })
}

// 新增机票行程单
export function addAirTicket(data) {
  return request({
    url: '/invoice/airTicket',
    method: 'post',
    data: data
  })
}

// 新增机票行程单
export function addAirTicketByAtt(data) {
  return request({
    url: '/invoice/airTicket/addByAtt',
    method: 'post',
    data: data
  })
}

// 修改机票行程单
export function updateAirTicket(data) {
  return request({
    url: '/invoice/airTicket',
    method: 'put',
    data: data
  })
}

// 删除机票行程单
export function delAirTicket(id) {
  return request({
    url: '/invoice/airTicket/' + id,
    method: 'delete'
  })
}

// 导出机票行程单
export function exportAirTicket(query) {
  return request({
    url: '/invoice/airTicket/export',
    method: 'get',
    params: query
  })
}
