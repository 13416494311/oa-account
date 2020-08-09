import request from '@/utils/request'

// 查询增值税发票商品列表
export function listCommodity(query) {
  return request({
    url: '/invoice/commodity/list',
    method: 'get',
    params: query
  })
}

// 查询增值税发票商品列表
export function listCommodityNoPage(query) {
  return request({
    url: '/invoice/commodity/listNoPage',
    method: 'get',
    params: query
  })
}

// 查询增值税发票商品详细
export function getCommodity(id) {
  return request({
    url: '/invoice/commodity/' + id,
    method: 'get'
  })
}

// 新增增值税发票商品
export function addCommodity(data) {
  return request({
    url: '/invoice/commodity',
    method: 'post',
    data: data
  })
}

// 修改增值税发票商品
export function updateCommodity(data) {
  return request({
    url: '/invoice/commodity',
    method: 'put',
    data: data
  })
}

// 删除增值税发票商品
export function delCommodity(id) {
  return request({
    url: '/invoice/commodity/' + id,
    method: 'delete'
  })
}

// 导出增值税发票商品
export function exportCommodity(query) {
  return request({
    url: '/invoice/commodity/export',
    method: 'get',
    params: query
  })
}
