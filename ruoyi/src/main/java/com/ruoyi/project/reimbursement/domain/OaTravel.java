package com.ruoyi.project.reimbursement.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 差旅报销对象 oa_travel
 *
 * @author ruoyi
 * @date 2020-06-07
 */
@Data
public class OaTravel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 报销申请id */
    private String applyUuid;

    /** 住宿费票据张数 */
    @Excel(name = "住宿费票据张数")
    private Long hotelBillNum;

    /** 住宿费 */
    @Excel(name = "住宿费")
    private Double hotelFee;

    /** 市内车费票据张数 */
    @Excel(name = "市内车费票据张数")
    private Long cityFareBillNum;

    /** 市内车费 */
    @Excel(name = "市内车费")
    private Double cityFareFee;

    /** 邮电费票据张数 */
    @Excel(name = "邮电费票据张数")
    private Long postageBillNum;

    /** 邮电费 */
    @Excel(name = "邮电费")
    private Double postageFee;

    /** 办公用品费票据张数 */
    @Excel(name = "办公用品费票据张数")
    private Long officeSuppliesBillNum;

    /** 办公用品费 */
    @Excel(name = "办公用品费")
    private Double officeSuppliesFee;

    /** 不买卧铺补贴票据张数 */
    @Excel(name = "不买卧铺补贴票据张数")
    private Long bedSubsidyBillNum;

    /** 不买卧铺补贴 */
    @Excel(name = "不买卧铺补贴")
    private Double bedSubsidyFee;

    /** 其他费票据张数 */
    @Excel(name = "其他费票据张数")
    private Long otherBillNum;

    /** 其他费 */
    @Excel(name = "其他费")
    private Double otherFee;



}
