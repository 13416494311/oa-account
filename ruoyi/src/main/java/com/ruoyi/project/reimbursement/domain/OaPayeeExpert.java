package com.ruoyi.project.reimbursement.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 专家/劳务对象 oa_payee_expert
 *
 * @author ruoyi
 * @date 2020-06-14
 */
@Data
public class OaPayeeExpert extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 唯一id */
    private String uuid;

    /** 报销申请唯一id */
    private String applyUuid;

    /** 专家/劳务 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 单位 */
    @Excel(name = "单位")
    private String company;

    /** 所在区域 */
    @Excel(name = "所在区域", readConverterExp = "0=境内,1=境外")
    private String area;

    /** 身份证 */
    @Excel(name = "身份证")
    private String cardNo;

    /** 证件照片附件id */
    private String sysAttId;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 开户行 */
    @Excel(name = "开户行")
    private String bank;

    /** 开户行账号 */
    @Excel(name = "开户行账号")
    private String bankNo;

    /** 税前 */
    @Excel(name = "税前")
    private Long preTax;

    /** 税后 */
    @Excel(name = "税后")
    private Long aftTax;


}
