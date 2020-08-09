package com.ruoyi.project.system.service;

import java.util.List;

import com.ruoyi.common.utils.JsonTreeData;
import com.ruoyi.project.system.domain.SysRegion;

/**
 * 地区代码Service接口
 *
 * @author ruoyi
 * @date 2020-06-07
 */
public interface ISysRegionService
{
    /**
     * 查询地区代码
     *
     * @param id 地区代码ID
     * @return 地区代码
     */
    public SysRegion selectSysRegionById(Long id);

    /**
     * 查询地区代码列表
     *
     * @param sysRegion 地区代码
     * @return 地区代码集合
     */
    public List<SysRegion> selectSysRegionList(SysRegion sysRegion);


    public List<JsonTreeData> getRegionTree();


}
