package com.ruoyi.project.invoice.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 机票行程单对象 oa_air_ticket
 *
 * @author ruoyi
 * @date 2020-06-21
 */
@Data
public class OaAirTicket extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 唯一值 */
    @Excel(name = "唯一值")
    private String ticketUuid;

    /** 关联业务uuid */
    @Excel(name = "关联业务uuid")
    private String uuid;

    /** 获取方式(autoScan：照片扫描  writeIn：手动录入 ) */
    @Excel(name = "获取方式(autoScan：照片扫描  writeIn：手动录入 )")
    private String accessWay;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 始发站 */
    @Excel(name = "始发站")
    private String startingStation;

    /** 目的站 */
    @Excel(name = "目的站")
    private String destinationStation;

    /** 航班号 */
    @Excel(name = "航班号")
    private String flight;

    /** 舱位 */
    @Excel(name = "舱位")
    private String seatCategory;

    /** 舱位说明 */
    @Excel(name = "舱位说明")
    private String seatCategoryDesc;

    /** 出发日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "出发日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date dateTime;

    /** 日期 */
    @Excel(name = "日期")
    private String date;

    /** 时间 */
    @Excel(name = "时间")
    private String time;

    /** 电子客票号码 */
    @Excel(name = "电子客票号码")
    private String ticketNumber;

    /** 电子客票号码 */
    @Excel(name = "电子客票识别号码")
    private String ticketScanNumber;

    /** 票价 */
    @Excel(name = "票价")
    private Double fare;

    /** 民航发展基金/基建费 */
    @Excel(name = "民航发展基金/基建费")
    private Double devFund;

    /** 燃油附加费 */
    @Excel(name = "燃油附加费")
    private Double fuelSurcharge;

    /** 其他税费 */
    @Excel(name = "其他税费")
    private Double otherTax;

    /** 合计金额 */
    @Excel(name = "合计金额")
    private Double ticketRates;

    /** 填开日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "填开日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date issuedDate;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idNum;

    /** 承运人 */
    @Excel(name = "承运人")
    private String carrier;

    /** 订票渠道 */
    @Excel(name = "订票渠道")
    private String issuedBy;

    /** 印刷序号 */
    @Excel(name = "印刷序号")
    private String serialNumber;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 票据附件id */
    @Excel(name = "票据附件id")
    private String sysAttId;

    /** 发票号码是否重复（0：不重复 1：重复） */
    private String repeatFlag;

    /** 发票异常标识（0：无异常 1：异常） */
    private String abnormalFlag;
}
