package com.ruoyi.project.invoice.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 出租车发票对象 oa_taxi_ticket
 *
 * @author ruoyi
 * @date 2020-07-31
 */
@Data
public class OaTaxiTicket extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 唯一值  关联发票商品 */
    @Excel(name = "唯一值  关联发票商品")
    private String invoiceUuid;

    /** 关联业务id */
    @Excel(name = "关联业务id")
    private String uuid;

    /** 获取方式(autoScan：照片扫描  writeIn：手动录入 ) */
    @Excel(name = "获取方式(autoScan：照片扫描  writeIn：手动录入 )")
    private String accessWay;

    /** 发票代码 */
    @Excel(name = "发票代码")
    private String invoiceCode;

    /** 发票号码 */
    @Excel(name = "发票号码")
    private String invoiceNum;

    /** 发票识别号码 */
    @Excel(name = "发票识别号码")
    private String invoiceScanNum;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String taxiNum;

    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date dateTimeStart;

    /** 出发日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date dateTimeEnd;

    /** 日期 */
    @Excel(name = "日期")
    private String date;

    /** 时间 */
    @Excel(name = "时间")
    private String time;

    /** 票价 */
    @Excel(name = "票价")
    private Double fare;

    /** 燃油附加费 */
    @Excel(name = "燃油附加费")
    private Double fuelOilSurcharge;

    /** 其他税费 */
    @Excel(name = "其他税费")
    private Double callServiceSurcharge;

    /** 票据附件id */
    @Excel(name = "票据附件id")
    private String sysAttId;

    /** 发票号码是否重复（0：不重复 1：重复） */
    @Excel(name = "发票号码是否重复", readConverterExp = "0=：不重复,1=：重复")
    private String repeatFlag;

    /** 发票异常标识（0：无异常 1：异常） */
    @Excel(name = "发票异常标识", readConverterExp = "0=：无异常,1=：异常")
    private String abnormalFlag;


}
