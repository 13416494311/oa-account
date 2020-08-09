package com.ruoyi.project.reimbursement.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 报销申请对象 oa_reimbursement_apply
 *
 * @author ruoyi
 * @date 2020-06-05
 */
@Data
public class OaReimbursementApply extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    private String uuid;

    /** 报销类型 */
    @Excel(name = "报销类型")
    private String type;

    /** 报销人 */
    @Excel(name = "报销人")
    private String reimburser;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 课题 */
    @Excel(name = "课题")
    private String topic;

    /** 经办人 */
    @Excel(name = "经办人")
    private String operator;

    /** 报销事由 */
    @Excel(name = "报销事由")
    private String reason;

    /** 报销总金额 */
    @Excel(name = "报销总金额")
    private Double totalMoney;

    @Excel(name = "收款方式", readConverterExp = "1=：汇款；2：现金；3：支票")
    private String payeeType;

    /** 开户行 */
    @Excel(name = "开户行")
    private String bank;

    /** 开户行账号 */
    @Excel(name = "开户行账号")
    private String bankNo;

    /** 审核状态 */
    @Excel(name = "审核状态")
    private String auditState;

    private Long createDeptId;

    private Long createUserId;


}
