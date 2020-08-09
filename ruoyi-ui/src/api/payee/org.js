import request from '@/utils/request'

// 查询收款方（企业）信息列表
export function listOrg(query) {
  return request({
    url: '/payee/org/list',
    method: 'get',
    params: query
  })
}

// 查询收款方（企业）信息详细
export function getOrg(id) {
  return request({
    url: '/payee/org/' + id,
    method: 'get'
  })
}

// 新增收款方（企业）信息
export function addOrg(data) {
  return request({
    url: '/payee/org',
    method: 'post',
    data: data
  })
}

// 修改收款方（企业）信息
export function updateOrg(data) {
  return request({
    url: '/payee/org',
    method: 'put',
    data: data
  })
}

// 删除收款方（企业）信息
export function delOrg(id) {
  return request({
    url: '/payee/org/' + id,
    method: 'delete'
  })
}

// 导出收款方（企业）信息
export function exportOrg(query) {
  return request({
    url: '/payee/org/export',
    method: 'get',
    params: query
  })
}