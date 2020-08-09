package com.ruoyi.project.invoice.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.invoice.mapper.OaAirTicketMapper;
import com.ruoyi.project.invoice.domain.OaAirTicket;
import com.ruoyi.project.invoice.service.IOaAirTicketService;

/**
 * 机票行程单Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-21
 */
@Service
public class OaAirTicketServiceImpl implements IOaAirTicketService
{
    @Autowired
    private OaAirTicketMapper oaAirTicketMapper;

    /**
     * 查询机票行程单
     *
     * @param id 机票行程单ID
     * @return 机票行程单
     */
    @Override
    public OaAirTicket selectOaAirTicketById(Long id)
    {
        return oaAirTicketMapper.selectOaAirTicketById(id);
    }

    /**
     * 查询机票行程单列表
     *
     * @param oaAirTicket 机票行程单
     * @return 机票行程单
     */
    @Override
    public List<OaAirTicket> selectOaAirTicketList(OaAirTicket oaAirTicket)
    {
        return oaAirTicketMapper.selectOaAirTicketList(oaAirTicket);
    }

    /**
     * 新增机票行程单
     *
     * @param oaAirTicket 机票行程单
     * @return 结果
     */
    @Override
    public int insertOaAirTicket(OaAirTicket oaAirTicket)
    {
        oaAirTicket.setCreateTime(DateUtils.getNowDate());
        return oaAirTicketMapper.insertOaAirTicket(oaAirTicket);
    }

    /**
     * 修改机票行程单
     *
     * @param oaAirTicket 机票行程单
     * @return 结果
     */
    @Override
    public int updateOaAirTicket(OaAirTicket oaAirTicket)
    {
        return oaAirTicketMapper.updateOaAirTicket(oaAirTicket);
    }

    /**
     * 批量删除机票行程单
     *
     * @param ids 需要删除的机票行程单ID
     * @return 结果
     */
    @Override
    public int deleteOaAirTicketByIds(Long[] ids)
    {
        return oaAirTicketMapper.deleteOaAirTicketByIds(ids);
    }

    /**
     * 删除机票行程单信息
     *
     * @param id 机票行程单ID
     * @return 结果
     */
    @Override
    public int deleteOaAirTicketById(Long id)
    {
        return oaAirTicketMapper.deleteOaAirTicketById(id);
    }
}
