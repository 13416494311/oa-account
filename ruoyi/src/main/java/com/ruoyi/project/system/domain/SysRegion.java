package com.ruoyi.project.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 地区代码对象 sys_region
 * 
 * @author ruoyi
 * @date 2020-06-07
 */
public class SysRegion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    private String regionCode;

    /** $column.columnComment */
    private String regionName;

    /** $column.columnComment */
    private String parentRegionCode;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setRegionCode(String regionCode) 
    {
        this.regionCode = regionCode;
    }

    public String getRegionCode() 
    {
        return regionCode;
    }
    public void setRegionName(String regionName) 
    {
        this.regionName = regionName;
    }

    public String getRegionName() 
    {
        return regionName;
    }
    public void setParentRegionCode(String parentRegionCode) 
    {
        this.parentRegionCode = parentRegionCode;
    }

    public String getParentRegionCode() 
    {
        return parentRegionCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("regionCode", getRegionCode())
            .append("regionName", getRegionName())
            .append("parentRegionCode", getParentRegionCode())
            .toString();
    }
}
