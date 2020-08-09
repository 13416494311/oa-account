import request from '@/utils/request'

// 查询增值税发票列表
export function listInvoice(query) {
  return request({
    url: '/invoice/invoice/list',
    method: 'get',
    params: query
  })
}

// 查询增值税发票列表
export function listNoPageInvoice(query) {
  return request({
    url: '/invoice/invoice/listNoPage',
    method: 'get',
    params: query
  })
}
// 查询增值税发票详细
export function getInvoice(id) {
  return request({
    url: '/invoice/invoice/' + id,
    method: 'get'
  })
}

// 校验增值税发票号码是否已存在
export function checkInvoiceExist(data) {
  return request({
    url: '/invoice/invoice/checkInvoiceExist' ,
    method: 'post',
    data: data
  })
}

// 新增增值税发票
export function addInvoice(data) {
  return request({
    url: '/invoice/invoice',
    method: 'post',
    data: data
  })
}
// 新增增值税发票
export function addInvoiceByAtt(data) {
  return request({
    url: '/invoice/invoice/addByAtt',
    method: 'post',
    data: data
  })
}

// 修改增值税发票
export function updateInvoice(data) {
  return request({
    url: '/invoice/invoice',
    method: 'put',
    data: data
  })
}

// 删除增值税发票
export function delInvoice(id) {
  return request({
    url: '/invoice/invoice/' + id,
    method: 'delete'
  })
}

// 导出增值税发票
export function exportInvoice(query) {
  return request({
    url: '/invoice/invoice/export',
    method: 'get',
    params: query
  })
}
