package com.ruoyi.project.reimbursement.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 差旅报销明细对象 oa_travel_details
 *
 * @author ruoyi
 * @date 2020-06-07
 */
@Data
public class OaTravelDetails extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 报销申请UUID */
    private String applyUuid;

    /** 出差起始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出差起始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 出差终止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出差终止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 出差目的地 */
    @Excel(name = "出差目的地")
    private String startRegion;

    /** 出差目的地 */
    @Excel(name = "出差目的地")
    private String endRegion;

    /** 交通工具（1：飞机；2：火车；3：汽车；4：轮船；5：其他） */
    @Excel(name = "交通工具", readConverterExp = "1=：飞机；2：火车；3：汽车；4：轮船；5：其他")
    private String tool;

    /** 交通票据张数 */
    @Excel(name = "交通票据张数")
    private Long trafficBillNum;

    /** 交通费 */
    @Excel(name = "交通费")
    private Double trafficFee;

    /** 出差天数 */
    @Excel(name = "出差天数")
    private Long days;

    /** 出差补贴 */
    @Excel(name = "出差补贴")
    private Double subsidyFee;


}
