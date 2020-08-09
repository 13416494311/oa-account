import request from '@/utils/request'

// 查询流程模型数据
export function listTable(query) {
  return request({
    url: '/activiti/model/list',
    method: 'get',
    params: query
  })
}

export function addModel() {
  return request({
    url: '/activiti/model/add',
    method: 'get'
  })
}

export function delModel(ids) {
  return request({
    url: '/activiti/model/remove/'+ ids,
    method: 'delete',
  })
}

export function expModel(id) {
  return request({
    url: '/activiti/model/export/'+ id,
    method: 'get',
  })
}

export function depModel(id) {
  return request({
    url: '/activiti/model/deploy/'+ id,
    method: 'get',
  })
}
