import request from '@/utils/request'

// 查询地区代码列表
export function listRegion(query) {
  return request({
    url: '/system/region/list',
    method: 'get',
    params: query
  })
}

// 查询地区代码详细
export function getRegion(regionCode) {
  return request({
    url: '/system/region/' + regionCode,
    method: 'get'
  })
}


