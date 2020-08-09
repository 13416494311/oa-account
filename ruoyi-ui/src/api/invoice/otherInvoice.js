import request from '@/utils/request'

// 查询其他发票列表
export function listOtherInvoice(query) {
  return request({
    url: '/invoice/otherInvoice/list',
    method: 'get',
    params: query
  })
}

// 查询其他发票详细
export function getOtherInvoice(id) {
  return request({
    url: '/invoice/otherInvoice/' + id,
    method: 'get'
  })
}

// 校验增值税发票号码是否已存在
export function checkOtherInvoiceExist(data) {
  return request({
    url: '/invoice/otherInvoice/checkOtherInvoiceExist' ,
    method: 'post',
    data: data
  })
}

// 新增其他发票
export function addOtherInvoice(data) {
  return request({
    url: '/invoice/otherInvoice',
    method: 'post',
    data: data
  })
}

// 修改其他发票
export function updateOtherInvoice(data) {
  return request({
    url: '/invoice/otherInvoice',
    method: 'put',
    data: data
  })
}

// 删除其他发票
export function delOtherInvoice(id) {
  return request({
    url: '/invoice/otherInvoice/' + id,
    method: 'delete'
  })
}

// 导出其他发票
export function exportOtherInvoice(query) {
  return request({
    url: '/invoice/otherInvoice/export',
    method: 'get',
    params: query
  })
}
