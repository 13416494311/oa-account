import request from '@/utils/request'


export function upload(data) {
  return request({
    url: '/system/attachment/upload',
    method: 'post',
    data: data
  })
}
export function uploadPdfToPng(data) {
  return request({
    url: '/system/attachment/uploadPdfToPng',
    method: 'post',
    data: data
  })
}

// 附件字典列表
export function listAttForReimbursement(query) {
  return request({
    url: '/system/attachment/reimbursement',
    method: 'get',
    params: query
  })
}

// 查询附件信息列表
export function listAttachment(query) {
  return request({
    url: '/system/attachment/list',
    method: 'get',
    params: query
  })
}

// 查询附件信息详细
export function getAttachment(id) {
  return request({
    url: '/system/attachment/' + id,
    method: 'get'
  })
}

// 新增附件信息
export function addAttachment(data) {
  return request({
    url: '/system/attachment',
    method: 'post',
    data: data
  })
}

// 修改附件信息
export function updateAttachment(data) {
  return request({
    url: '/system/attachment',
    method: 'put',
    data: data
  })
}

// 删除附件信息
export function delAttachment(id) {
  return request({
    url: '/system/attachment/' + id,
    method: 'delete'
  })
}

// 导出附件信息
export function exportAttachment(query) {
  return request({
    url: '/system/attachment/export',
    method: 'get',
    params: query
  })
}
