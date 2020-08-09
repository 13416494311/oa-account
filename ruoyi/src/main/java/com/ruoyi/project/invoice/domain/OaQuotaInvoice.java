package com.ruoyi.project.invoice.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 定额发票对象 oa_quota_invoice
 *
 * @author ruoyi
 * @date 2020-06-20
 */
@Data
public class OaQuotaInvoice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 唯一值   */
    @Excel(name = "唯一值 ")
    private String invoiceUuid;

    /** 关联业务uuid */
    @Excel(name = "关联业务uuid")
    private String uuid;

    /** 获取方式(autoScan：照片扫描  writeIn：手动录入 ) */
    @Excel(name = "获取方式(autoScan：照片扫描  writeIn：手动录入 )")
    private String accessWay;

    /** 发票代码 */
    @Excel(name = "发票代码")
    private String invoiceCode;

    /** 发票号码 */
    @Excel(name = "发票号码")
    private String invoiceNumber;

    /** 发票识别号码 */
    @Excel(name = "发票识别号码")
    private String invoiceScanNumber;

    /** 金额(大写) */
    @Excel(name = "金额(大写)")
    private String invoiceRate;

    /** 金额（小写） */
    @Excel(name = "金额(小写)")
    private Double invoiceMoney;

    /** 附件id */
    private Long sysAttId;

    /** 发票号码是否重复（0：不重复 1：重复） */
    private String repeatFlag;

    /** 发票异常标识（0：无异常 1：异常） */
    private String abnormalFlag;

}
