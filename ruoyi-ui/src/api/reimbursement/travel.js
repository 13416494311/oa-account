import request from '@/utils/request'

// 查询差旅报销列表
export function listTravel(query) {
  return request({
    url: '/reimbursement/travel/list',
    method: 'get',
    params: query
  })
}

// 查询差旅报销详细
export function getTravel(id) {
  return request({
    url: '/reimbursement/travel' + id,
    method: 'get'
  })
}

export function getTravelByApplyUuid(applyUuid) {
  return request({
    url: '/reimbursement/travel/applyUuid/' + applyUuid,
    method: 'get'
  })
}

// 新增差旅报销
export function addTravel(data) {
  return request({
    url: '/reimbursement/travel',
    method: 'post',
    data: data
  })
}

// 修改差旅报销
export function updateTravel(data) {
  return request({
    url: '/reimbursement/travel',
    method: 'put',
    data: data
  })
}

// 删除差旅报销
export function delTravel(id) {
  return request({
    url: '/reimbursement/travel/' + id,
    method: 'delete'
  })
}

// 导出差旅报销
export function exportTravel(query) {
  return request({
    url: '/reimbursement/travel/export',
    method: 'get',
    params: query
  })
}
