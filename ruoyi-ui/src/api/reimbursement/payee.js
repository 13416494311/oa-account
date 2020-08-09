import request from '@/utils/request'


// 远程搜索补齐
export function remoteSearch(query) {
  return request({
    url: '/reimbursement/payee/remoteSearch',
    method: 'get',
    params: query
  })
}
// 根据报销人查询最近一次报销 付款信息
export function getPayeeListByParam(query) {
  return request({
    url: '/reimbursement/payee/getPayeeListByParam',
    method: 'get',
    params: query
  })
}

// 查询付款信息列表
export function listPayee(query) {
  return request({
    url: '/reimbursement/payee/list',
    method: 'get',
    params: query
  })
}

// 查询付款信息详细
export function getPayee(id) {
  return request({
    url: '/reimbursement/payee/' + id,
    method: 'get'
  })
}
// 查询付款信息详细
export function getPayeeByApplyUuid(applyUuid) {
  return request({
    url: '/reimbursement/payee/applyUuid/' + applyUuid,
    method: 'get'
  })
}

// 新增付款信息
export function addPayee(data) {
  return request({
    url: '/reimbursement/payee',
    method: 'post',
    data: data
  })
}

// 修改付款信息
export function updatePayee(data) {
  return request({
    url: '/reimbursement/payee',
    method: 'put',
    data: data
  })
}

// 删除付款信息
export function delPayee(id) {
  return request({
    url: '/reimbursement/payee/' + id,
    method: 'delete'
  })
}

// 导出付款信息
export function exportPayee(query) {
  return request({
    url: '/reimbursement/payee/export',
    method: 'get',
    params: query
  })
}
