package com.ruoyi.project.reimbursement.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 付款信息对象 oa_payee
 *
 * @author ruoyi
 * @date 2020-06-13
 */
@Data
public class OaPayee extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 报销申请唯一id */
    @Excel(name = "报销申请唯一id")
    private String applyUuid;

    /** 收款人 */
    @Excel(name = "收款人")
    private String payee;

    /** 收款方式（1：汇款；2：现金；3：支票） */
    @Excel(name = "收款方式", readConverterExp = "1=：汇款；2：现金；3：支票")
    private String payeeType;

    /** 身份证 */
    @Excel(name = "身份证")
    private String cardNo;

    /** 电话 */
    @Excel(name = "电话")
    private String phone;

    /** 开户行 */
    @Excel(name = "开户行")
    private String bank;

    /** 开户行账号 */
    @Excel(name = "开户行账号")
    private String bankNo;


}
