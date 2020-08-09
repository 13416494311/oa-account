import request from '@/utils/request'

// 查询专家/劳务列表
export function listNoPagePayeeExpert(query) {
  return request({
    url: '/reimbursement/payeeExpert/listNoPage',
    method: 'get',
    params: query
  })
}

// 查询专家/劳务列表
export function listPayeeExpert(query) {
  return request({
    url: '/reimbursement/payeeExpert/list',
    method: 'get',
    params: query
  })
}

// 查询专家/劳务详细
export function getPayeeExpert(id) {
  return request({
    url: '/reimbursement/payeeExpert/' + id,
    method: 'get'
  })
}

// 新增专家/劳务
export function addPayeeExpert(data) {
  return request({
    url: '/reimbursement/payeeExpert',
    method: 'post',
    data: data
  })
}

// 修改专家/劳务
export function updatePayeeExpert(data) {
  return request({
    url: '/reimbursement/payeeExpert',
    method: 'put',
    data: data
  })
}

// 删除专家/劳务
export function delPayeeExpert(id) {
  return request({
    url: '/reimbursement/payeeExpert/' + id,
    method: 'delete'
  })
}

// 导出专家/劳务
export function exportPayeeExpert(query) {
  return request({
    url: '/reimbursement/payeeExpert/export',
    method: 'get',
    params: query
  })
}

// 下载用户导入模板
export function importTemplate() {
  return request({
    url: '/reimbursement/payeeExpert/importTemplate',
    method: 'get'
  })
}

// 新增增值税发票
export function importData(data) {
  return request({
    url: '/reimbursement/payeeExpert/importData',
    method: 'post',
    data: data
  })
}
