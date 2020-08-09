package com.ruoyi.project.system.service.impl;

import java.util.List;

import com.ruoyi.common.utils.JsonTreeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.SysRegionMapper;
import com.ruoyi.project.system.domain.SysRegion;
import com.ruoyi.project.system.service.ISysRegionService;

/**
 * 地区代码Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-07
 */
@Service
public class SysRegionServiceImpl implements ISysRegionService
{
    @Autowired
    private SysRegionMapper sysRegionMapper;

    /**
     * 查询地区代码
     *
     * @param id 地区代码ID
     * @return 地区代码
     */
    @Override
    public SysRegion selectSysRegionById(Long id)
    {
        return sysRegionMapper.selectSysRegionById(id);
    }

    /**
     * 查询地区代码列表
     *
     * @param sysRegion 地区代码
     * @return 地区代码
     */
    @Override
    public List<SysRegion> selectSysRegionList(SysRegion sysRegion)
    {
        return sysRegionMapper.selectSysRegionList(sysRegion);
    }

    @Override
    public List<JsonTreeData> getRegionTree()
    {
        return sysRegionMapper.getRegionTree();
    }


}
