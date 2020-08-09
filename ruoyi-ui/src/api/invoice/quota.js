import request from '@/utils/request'

// 查询定额发票列表
export function listQuota(query) {
  return request({
    url: '/invoice/quota/list',
    method: 'get',
    params: query
  })
}

// 查询定额发票详细
export function getQuota(id) {
  return request({
    url: '/invoice/quota/' + id,
    method: 'get'
  })
}

// 校验定额发票号码是否已存在
export function checkQuotaInvoiceExist(data) {
  return request({
    url: '/invoice/quota/checkQuotaInvoiceExist' ,
    method: 'post',
    data: data
  })
}

// 新增定额发票
export function addQuota(data) {
  return request({
    url: '/invoice/quota',
    method: 'post',
    data: data
  })
}
// 新增增值税发票
export function addQuotaByAtt(data) {
  return request({
    url: '/invoice/quota/addByAtt',
    method: 'post',
    data: data
  })
}
// 修改定额发票
export function updateQuota(data) {
  return request({
    url: '/invoice/quota',
    method: 'put',
    data: data
  })
}

// 删除定额发票
export function delQuota(id) {
  return request({
    url: '/invoice/quota/' + id,
    method: 'delete'
  })
}

// 导出定额发票
export function exportQuota(query) {
  return request({
    url: '/invoice/quota/export',
    method: 'get',
    params: query
  })
}
