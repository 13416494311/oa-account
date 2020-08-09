package com.ruoyi.project.payee.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 收款方（企业）信息对象 oa_payee_org
 *
 * @author ruoyi
 * @date 2020-06-03
 */
public class OaPayeeOrg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 单位名称 */
    @Excel(name = "单位名称")
    private String orgName;

    /** 营业执照号 */
    @Excel(name = "营业执照号")
    private String orgCode;

    /** 开户行 */
    @Excel(name = "开户行")
    private String bank;

    /** 开户行账号 */
    @Excel(name = "开户行账号")
    private String bankAccount;

    /** 地址 */
    private String address;

    /** 联系方式(手机) */
    private String phone;

    /** 联系方式(座机) */
    private String telephone;

    /** 部门 */
    private Long deptId;

    /** 创建人 */
    private Long userId;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setOrgName(String orgName)
    {
        this.orgName = orgName;
    }

    public String getOrgName()
    {
        return orgName;
    }
    public void setOrgCode(String orgCode)
    {
        this.orgCode = orgCode;
    }

    public String getOrgCode()
    {
        return orgCode;
    }
    public void setBank(String bank)
    {
        this.bank = bank;
    }

    public String getBank()
    {
        return bank;
    }
    public void setBankAccount(String bankAccount)
    {
        this.bankAccount = bankAccount;
    }

    public String getBankAccount()
    {
        return bankAccount;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }
    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    public String getTelephone()
    {
        return telephone;
    }
    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getDeptId()
    {
        return deptId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orgName", getOrgName())
            .append("orgCode", getOrgCode())
            .append("bank", getBank())
            .append("bankAccount", getBankAccount())
            .append("address", getAddress())
            .append("phone", getPhone())
            .append("telephone", getTelephone())
            .append("deptId", getDeptId())
            .append("userId", getUserId())
            .append("createTime", getCreateTime())
            .toString();
    }
}
