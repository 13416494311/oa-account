import request from '@/utils/request'

// 查询差旅报销明细列表
export function listTravelDetails(query) {
  return request({
    url: '/reimbursement/travelDetails/list',
    method: 'get',
    params: query
  })
}

// 查询差旅报销明细详细
export function getTravelDetails(id) {
  return request({
    url: '/reimbursement/travelDetails/' + id,
    method: 'get'
  })
}

// 新增差旅报销明细
export function addTravelDetails(data) {
  return request({
    url: '/reimbursement/travelDetails',
    method: 'post',
    data: data
  })
}

// 修改差旅报销明细
export function updateTravelDetails(data) {
  return request({
    url: '/reimbursement/travelDetails',
    method: 'put',
    data: data
  })
}

// 删除差旅报销明细
export function delTravelDetails(id) {
  return request({
    url: '/reimbursement/travelDetails/' + id,
    method: 'delete'
  })
}

// 导出差旅报销明细
export function exportTravelDetails(query) {
  return request({
    url: '/reimbursement/travelDetails/export',
    method: 'get',
    params: query
  })
}