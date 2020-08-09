package com.ruoyi.project.invoice.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 增值税发票商品对象 oa_invoice_commodity
 *
 * @author ruoyi
 * @date 2020-06-08
 */
@Data
public class OaInvoiceCommodity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 发票号码 */
    @Excel(name = "发票ID")
    private String invoiceUuid;

    /** 货物名称 */
    @Excel(name = "货物名称")
    private String name;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String type;

    /** 单位 */
    @Excel(name = "单位")
    private String unit;

    /** 数量 */
    @Excel(name = "数量")
    private Long num;

    /** 单价 */
    @Excel(name = "单价")
    private Double price;

    /** 金额 */
    @Excel(name = "金额")
    private Double amount;

    /** 税率 */
    @Excel(name = "税率")
    private Double taxRate;

    /** 税额 */
    @Excel(name = "税额")
    private Double tax;




}
