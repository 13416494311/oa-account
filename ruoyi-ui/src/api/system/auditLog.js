import request from '@/utils/request'

// 查询审核记录列表
export function listAuditLog(query) {
  return request({
    url: '/system/auditLog/list',
    method: 'get',
    params: query
  })
}

// 查询审核记录列表
export function listAuditLogNoPage(query) {
  return request({
    url: '/system/auditLog/listNoPage',
    method: 'get',
    params: query
  })
}

// 查询审核记录详细
export function getAuditLog(id) {
  return request({
    url: '/system/auditLog/' + id,
    method: 'get'
  })
}

// 新增审核记录
export function addAuditLog(data) {
  return request({
    url: '/system/auditLog',
    method: 'post',
    data: data
  })
}

// 修改审核记录
export function updateAuditLog(data) {
  return request({
    url: '/system/auditLog',
    method: 'put',
    data: data
  })
}

// 删除审核记录
export function delAuditLog(id) {
  return request({
    url: '/system/auditLog/' + id,
    method: 'delete'
  })
}

// 导出审核记录
export function exportAuditLog(query) {
  return request({
    url: '/system/auditLog/export',
    method: 'get',
    params: query
  })
}
