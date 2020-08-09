package com.ruoyi.project.invoice.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 火车票对象 oa_train_ticket
 *
 * @author ruoyi
 * @date 2020-06-21
 */
@Data
public class OaTrainTicket extends BaseEntity
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

    /** 车票号 */
    @Excel(name = "车票号")
    private String ticketNum;

    /** 车票号 */
    @Excel(name = "车票识别号")
    private String ticketScanNum;

    /** 车次号 */
    @Excel(name = "车次号")
    private String trainNum;

    /** 始发站 */
    @Excel(name = "始发站")
    private String startingStation;

    /** 到达站 */
    @Excel(name = "到达站")
    private String destinationStation;

    /** 出发日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "出发日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date dateTime;

    /** 出发日期 */
    @Excel(name = "出发日期")
    private String date;

    /** 出发时间 */
    @Excel(name = "出发时间")
    private String time;

    /** 车票金额 */
    @Excel(name = "车票金额")
    private Double ticketRates;

    /** 席别 */
    @Excel(name = "席别")
    private String seatCategory;

    /** 席别说明 */
    @Excel(name = "席别说明")
    private String seatCategoryDesc;

    /** 乘客姓名 */
    @Excel(name = "乘客姓名")
    private String name;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idNum;

    /** 序列号 */
    @Excel(name = "序列号")
    private String serialNumber;

    /** 售站 */
    @Excel(name = "售站")
    private String salesStation;

    /** 座位号 */
    @Excel(name = "座位号")
    private String seatNum;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 票据附件id */
    @Excel(name = "票据附件id")
    private Long sysAttId;

    /** 发票号码是否重复（0：不重复 1：重复） */
    private String repeatFlag;

    /** 发票异常标识（0：无异常 1：异常） */
    private String abnormalFlag;



}
