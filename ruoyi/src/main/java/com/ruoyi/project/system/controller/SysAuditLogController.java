package com.ruoyi.project.system.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.domain.SysAuditLog;
import com.ruoyi.project.system.service.ISysAuditLogService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 审核记录Controller
 *
 * @author ruoyi
 * @date 2020-07-16
 */
@RestController
@RequestMapping("/system/auditLog")
public class SysAuditLogController extends BaseController
{
    @Autowired
    private ISysAuditLogService sysAuditLogService;

    /**
     * 查询审核记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(SysAuditLog sysAuditLog)
    {
        startPage();
        List<SysAuditLog> list = sysAuditLogService.selectSysAuditLogList(sysAuditLog);
        return getDataTable(list);
    }

    /**
     * 查询审核记录列表 不分页
     */
    @GetMapping("/listNoPage")
    public AjaxResult listNoPage(SysAuditLog sysAuditLog)
    {
        List<SysAuditLog> list = sysAuditLogService.selectSysAuditLogList(sysAuditLog);
        return AjaxResult.success(list);
    }
    /**
     * 导出审核记录列表
     */
    @Log(title = "审核记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysAuditLog sysAuditLog)
    {
        List<SysAuditLog> list = sysAuditLogService.selectSysAuditLogList(sysAuditLog);
        ExcelUtil<SysAuditLog> util = new ExcelUtil<SysAuditLog>(SysAuditLog.class);
        return util.exportExcel(list, "auditLog");
    }

    /**
     * 获取审核记录详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysAuditLogService.selectSysAuditLogById(id));
    }

    /**
     * 新增审核记录
     */
    @Log(title = "审核记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAuditLog sysAuditLog)
    {
        return toAjax(sysAuditLogService.insertSysAuditLog(sysAuditLog));
    }

    /**
     * 修改审核记录
     */
    @Log(title = "审核记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAuditLog sysAuditLog)
    {
        return toAjax(sysAuditLogService.updateSysAuditLog(sysAuditLog));
    }

    /**
     * 删除审核记录
     */
    @Log(title = "审核记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysAuditLogService.deleteSysAuditLogByIds(ids));
    }
}
