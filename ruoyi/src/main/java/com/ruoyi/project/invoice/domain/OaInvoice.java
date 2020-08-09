package com.ruoyi.project.invoice.domain;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.utils.NumberUtils;
import com.ruoyi.common.utils.StringUtils;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import com.alibaba.fastjson.JSON;

/**
 * 增值税发票对象 oa_invoice
 *
 * @author ruoyi
 * @date 2020-06-08
 */
@Data
public class OaInvoice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 关联业务id */
    private String uuid;

    /** 唯一值  关联发票商品*/
    private String invoiceUuid;

    /** 获取方式 */
    private String accessWay ;

    /** 发票种类 */
    @Excel(name = "发票种类")
    private String invoiceType;

    /** 发票名称 */
    @Excel(name = "发票名称")
    private String invoiceTypeOrg;

    /** 发票代码 */
    @Excel(name = "发票代码")
    private String invoiceCode;

    /** 发票号码 */
    @Excel(name = "发票号码")
    private String invoiceNum;

    /** 发票识别号码 */
    @Excel(name = "发票识别号码")
    private String invoiceScanNum;

    /** 校验码 */
    @Excel(name = "校验码")
    private String checkCode;

    /** 开票日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开票日期", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date invoiceDate;

    /** 购方名称 */
    @Excel(name = "购方名称")
    private String purchaserName;

    /** 购方纳税人识别号 */
    @Excel(name = "购方纳税人识别号")
    private String purchaserRegisterNum;

    /** 购方地址及电话 */
    @Excel(name = "购方地址及电话")
    private String purchaserAddress;

    /** 购方开户行及账号 */
    @Excel(name = "购方开户行及账号")
    private String purchaserBank;

    /** 密码区 */
    @Excel(name = "密码区")
    private String password;

    /** 销售方名称 */
    @Excel(name = "销售方名称")
    private String sellerName;

    /** 销售方纳税人识别号 */
    @Excel(name = "销售方纳税人识别号")
    private String sellerRegisterNum;

    /** 销售方地址及电话 */
    @Excel(name = "销售方地址及电话")
    private String sellerAddress;

    /** 销售方开户行及账号 */
    @Excel(name = "销售方开户行及账号")
    private String sellerBank;

    /** 合计金额 */
    @Excel(name = "合计金额")
    private Double totalAmount;

    /** 合计税额 */
    @Excel(name = "合计税额")
    private Double totalTax;

    /** 价税合计(大写) */
    @Excel(name = "价税合计(大写)")
    private String amountInWords;

    /** 价税合计(小写) */
    @Excel(name = "价税合计(小写)")
    private Double amountInFiguers;

    /** 收款人 */
    @Excel(name = "收款人")
    private String payee;

    /** 复核 */
    @Excel(name = "复核")
    private String checker;

    /** 开票人 */
    @Excel(name = "开票人")
    private String noteDrawer;

    /** 备注 */
    @Excel(name = "备注")
    private String remarks;

    private Long sysAttId;

    /** 发票号码是否重复（0：不重复 1：重复） */
    private String repeatFlag;

    /** 发票异常标识（0：无异常 1：异常） */
    private String abnormalFlag;

    private List<Comondity> commodityName;

    private List<Comondity> commodityType;

    private List<Comondity> commodityUnit;

    private List<Comondity> commodityNum;

    private List<Comondity> commodityPrice;

    private List<Comondity> commodityAmount;

    private List<Comondity> commodityTaxRate;

    private List<Comondity> commodityTax;

    private List<OaInvoiceCommodity> oaInvoiceCommodity;



    public void convertComondity(){
        if(!StringUtils.isEmpty(commodityName)){
            this.oaInvoiceCommodity = commodityName.stream().map( name ->{
                OaInvoiceCommodity oaInvoiceCommodity= new OaInvoiceCommodity();
                oaInvoiceCommodity.setInvoiceUuid(this.invoiceUuid);
                oaInvoiceCommodity.setName(name.getWord());
                if(!StringUtils.isEmpty(commodityType)){
                    for(Comondity comondity:commodityType){
                        if(comondity.getRow().equals(name.getRow())){
                            oaInvoiceCommodity.setType(comondity.getWord());
                        }
                    }
                }
                if(!StringUtils.isEmpty(commodityUnit)){
                    for(Comondity comondity:commodityUnit){
                        if(comondity.getRow().equals(name.getRow())){
                            oaInvoiceCommodity.setUnit(comondity.getWord());
                        }
                    }
                }
                if(!StringUtils.isEmpty(commodityNum)){
                    for(Comondity comondity:commodityNum){
                        if(comondity.getRow().equals(name.getRow())){
                            if(NumberUtils.isNumber(comondity.getWord())){
                                oaInvoiceCommodity.setNum(Long.parseLong(comondity.getWord()));
                            };
                        }
                    }
                }
                if(!StringUtils.isEmpty(commodityPrice)){
                    for(Comondity comondity:commodityPrice){
                        if(comondity.getRow().equals(name.getRow())){
                            if(NumberUtils.isNumber(comondity.getWord())){
                                oaInvoiceCommodity.setPrice(Double.parseDouble(comondity.getWord()));
                            };
                        }
                    }
                }
                if(!StringUtils.isEmpty(commodityAmount)){
                    for(Comondity comondity:commodityAmount){
                        if(comondity.getRow().equals(name.getRow())&&!StringUtils.isEmpty(comondity.getWord())){
                            if(NumberUtils.isNumber(comondity.getWord())){
                                oaInvoiceCommodity.setAmount(Double.parseDouble(comondity.getWord()));
                            };
                        }
                    }
                }
                if(!StringUtils.isEmpty(commodityTaxRate)){
                    for(Comondity comondity:commodityTaxRate){
                        if(comondity.getRow().equals(name.getRow())&&!StringUtils.isEmpty(comondity.getWord())){
                            if(NumberUtils.isNumber(comondity.getWord())){
                                oaInvoiceCommodity.setTaxRate(Double.parseDouble(comondity.getWord()));
                            }else if (comondity.getWord().substring(comondity.getWord().length() - 1).equals("%")) { //百分号情况
                                oaInvoiceCommodity.setTaxRate(Double.parseDouble(comondity.getWord().replace("%",
                                        "")) * 0.01);
                            }
                        }
                    }
                }
                if(!StringUtils.isEmpty(commodityTax)){
                    for(Comondity comondity:commodityTax){
                        if(comondity.getRow().equals(name.getRow())&&!StringUtils.isEmpty(comondity.getWord())){
                            if(NumberUtils.isNumber(comondity.getWord())){
                                oaInvoiceCommodity.setTax(Double.parseDouble(comondity.getWord()));
                            };
                        }
                    }
                }
                return oaInvoiceCommodity;
            }).collect(Collectors.toList());
        }

    }



}
