import request from '@/utils/request'

// 查询收款方（个人）信息列表
export function listUser(query) {
  return request({
    url: '/payee/user/list',
    method: 'get',
    params: query
  })
}

// 查询收款方（个人）信息详细
export function getUser(id) {
  return request({
    url: '/payee/user/' + id,
    method: 'get'
  })
}

// 新增收款方（个人）信息
export function addUser(data) {
  return request({
    url: '/payee/user',
    method: 'post',
    data: data
  })
}

// 修改收款方（个人）信息
export function updateUser(data) {
  return request({
    url: '/payee/user',
    method: 'put',
    data: data
  })
}

// 删除收款方（个人）信息
export function delUser(id) {
  return request({
    url: '/payee/user/' + id,
    method: 'delete'
  })
}

// 导出收款方（个人）信息
export function exportUser(query) {
  return request({
    url: '/payee/user/export',
    method: 'get',
    params: query
  })
}