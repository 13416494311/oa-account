package com.ruoyi.project.invoice.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.project.system.domain.SysAttachment;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 票据信息对象 oa_ticket
 *
 * @author ruoyi
 * @date 2020-06-17
 */
@Data
public class OaTicket extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    private String uuid;

    private String accessWay;


    /** 票据类型 */
    @Excel(name = "票据类型")
    private String ticketType;

    /** 票据类型 */
    @Excel(name = "票据子类")
    private String ticketSubType;

    /** 票据号码 */
    @Excel(name = "票据号码")
    private String ticketNum;

    /** 票据扫描号码 */
    @Excel(name = "票据扫描号码")
    private String ticketScanNum;

    /** 日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date ticketDate;

    /** 金额 */
    @Excel(name = "金额")
    private Double totalAmount;

    /** 购方名称 */
    @Excel(name = "购方名称")
    private String purchaserName;

    /** 购方纳税人识别号 */
    @Excel(name = "购方纳税人识别号")
    private String purchaserNum;

    /** 发票内容 */
    @Excel(name = "发票内容")
    private List<String> content;

    /** 销售方名称 */
    @Excel(name = "销售方名称")
    private String sellerName;

    /** 销售方纳税人识别号 */
    @Excel(name = "销售方纳税人识别号")
    private String sellerNum;

    /** 发票异常标识（0：无异常 1：异常） */
    private String abnormalFlag;

    private List<SysAttachment> sysAtts;


}
