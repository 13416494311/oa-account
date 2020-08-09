import request from '@/utils/request'

// 查询报销申请列表
export function listApply(query) {
  return request({
    url: '/reimbursement/apply/list',
    method: 'get',
    params: query
  })
}

// 查询报销申请列表
export function listApplyNoPage(query) {
  return request({
    url: '/reimbursement/apply/listNoPage',
    method: 'get',
    params: query
  })
}


export function listApplyDeptName(query) {
  return request({
    url: '/reimbursement/apply/listApplyDeptName',
    method: 'get',
    params: query
  })
}

// 远程搜索补齐
export function remoteSearch(query) {
  return request({
    url: '/reimbursement/apply/remoteSearch',
    method: 'get',
    params: query
  })
}

// 查询报销申请详细
export function getApply(id) {
  return request({
    url: '/reimbursement/apply/' + id,
    method: 'get'
  })
}

// 新增报销申请
export function addApply(data) {
  return request({
    url: '/reimbursement/apply',
    method: 'post',
    data: data
  })
}

// 修改报销申请
export function updateApply(data) {
  return request({
    url: '/reimbursement/apply',
    method: 'put',
    data: data
  })
}

// 删除报销申请
export function delApply(id) {
  return request({
    url: '/reimbursement/apply/' + id,
    method: 'delete'
  })
}

// 删除报销申请
export function submitApply(id) {
  return request({
    url: '/reimbursement/apply/submit/' + id,
    method: 'put'
  })
}

// 导出报销申请
export function exportApply(query) {
  return request({
    url: '/reimbursement/apply/export',
    method: 'get',
    params: query
  })
}
