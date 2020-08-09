package com.ruoyi.project.invoice.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 其他发票对象 oa_other_invoice
 *
 * @author ruoyi
 * @date 2020-07-04
 */
@Data
public class OaOtherInvoice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 唯一值 */
    @Excel(name = "唯一值")
    private String invoiceUuid;

    /** 关联业务uuid */
    @Excel(name = "关联业务uuid")
    private String uuid;

    /** 获取方式(autoScan：照片扫描  writeIn：手动录入 ) */
    @Excel(name = "获取方式(autoScan：照片扫描  writeIn：手动录入 )")
    private String accessWay;

    /** 发票类型 */
    @Excel(name = "发票类型")
    private String invoiceType;

    /** 发票号码 */
    @Excel(name = "发票号码")
    private String invoiceNumber;

    /** 金额（小写） */
    @Excel(name = "金额")
    private Double invoiceMoney;

    /** 开票日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开票日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date invoiceDate;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

    /** 票据附件id */
    @Excel(name = "票据附件id")
    private Long sysAttId;

    /** 发票异常标识（0：无异常 1：异常） */
    private String abnormalFlag;

}
