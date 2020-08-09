package com.ruoyi.project.system.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 审核记录对象 sys_audit_log
 *
 * @author ruoyi
 * @date 2020-07-16
 */
@Data
public class SysAuditLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 关联uuid */
    @Excel(name = "关联uuid")
    private String uuid;

    /** 审核人userId */
    @Excel(name = "审核人userId")
    private Long userId;

    private SysUser sysUser;

    /** 审核状态(2：通过 3：不通过) */
    @Excel(name = "审核状态(2：通过 3：不通过)")
    private String state;

    /** 审核原因 */
    @Excel(name = "审核原因")
    private String reason;


}
