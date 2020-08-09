package com.ruoyi.project.system.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.SysAuditLogMapper;
import com.ruoyi.project.system.domain.SysAuditLog;
import com.ruoyi.project.system.service.ISysAuditLogService;

/**
 * 审核记录Service业务层处理
 *
 * @author ruoyi
 * @date 2020-07-16
 */
@Service
public class SysAuditLogServiceImpl implements ISysAuditLogService
{
    @Autowired
    private SysAuditLogMapper sysAuditLogMapper;
    @Autowired
    private SysUserMapper userMapper;
    /**
     * 查询审核记录
     *
     * @param id 审核记录ID
     * @return 审核记录
     */
    @Override
    public SysAuditLog selectSysAuditLogById(Long id)
    {
        return sysAuditLogMapper.selectSysAuditLogById(id);
    }

    /**
     * 查询审核记录列表
     *
     * @param sysAuditLog 审核记录
     * @return 审核记录
     */
    @Override
    public List<SysAuditLog> selectSysAuditLogList(SysAuditLog sysAuditLog)
    {
        List<SysAuditLog> list = sysAuditLogMapper.selectSysAuditLogList(sysAuditLog);
        list = list.stream().map(auditLog->{
            SysUser sysUser =userMapper.selectUserById(auditLog.getUserId());
            auditLog.setSysUser(sysUser);
            return auditLog;
        }).collect(Collectors.toList());
        return list;
    }

    /**
     * 新增审核记录
     *
     * @param sysAuditLog 审核记录
     * @return 结果
     */
    @Override
    public int insertSysAuditLog(SysAuditLog sysAuditLog)
    {

        LoginUser loginUser = SpringUtils.getBean(TokenService.class).getLoginUser(ServletUtils.getRequest());
        SysUser currentUser = loginUser.getUser();
        sysAuditLog.setUserId(currentUser.getUserId());
        sysAuditLog.setCreateTime(DateUtils.getNowDate());
        return sysAuditLogMapper.insertSysAuditLog(sysAuditLog);
    }

    /**
     * 修改审核记录
     *
     * @param sysAuditLog 审核记录
     * @return 结果
     */
    @Override
    public int updateSysAuditLog(SysAuditLog sysAuditLog)
    {
        return sysAuditLogMapper.updateSysAuditLog(sysAuditLog);
    }

    /**
     * 批量删除审核记录
     *
     * @param ids 需要删除的审核记录ID
     * @return 结果
     */
    @Override
    public int deleteSysAuditLogByIds(Long[] ids)
    {
        return sysAuditLogMapper.deleteSysAuditLogByIds(ids);
    }

    /**
     * 删除审核记录信息
     *
     * @param id 审核记录ID
     * @return 结果
     */
    @Override
    public int deleteSysAuditLogById(Long id)
    {
        return sysAuditLogMapper.deleteSysAuditLogById(id);
    }
}
