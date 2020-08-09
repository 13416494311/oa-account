package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.project.system.domain.SysAuditLog;

/**
 * 审核记录Service接口
 * 
 * @author ruoyi
 * @date 2020-07-16
 */
public interface ISysAuditLogService 
{
    /**
     * 查询审核记录
     * 
     * @param id 审核记录ID
     * @return 审核记录
     */
    public SysAuditLog selectSysAuditLogById(Long id);

    /**
     * 查询审核记录列表
     * 
     * @param sysAuditLog 审核记录
     * @return 审核记录集合
     */
    public List<SysAuditLog> selectSysAuditLogList(SysAuditLog sysAuditLog);

    /**
     * 新增审核记录
     * 
     * @param sysAuditLog 审核记录
     * @return 结果
     */
    public int insertSysAuditLog(SysAuditLog sysAuditLog);

    /**
     * 修改审核记录
     * 
     * @param sysAuditLog 审核记录
     * @return 结果
     */
    public int updateSysAuditLog(SysAuditLog sysAuditLog);

    /**
     * 批量删除审核记录
     * 
     * @param ids 需要删除的审核记录ID
     * @return 结果
     */
    public int deleteSysAuditLogByIds(Long[] ids);

    /**
     * 删除审核记录信息
     * 
     * @param id 审核记录ID
     * @return 结果
     */
    public int deleteSysAuditLogById(Long id);
}
