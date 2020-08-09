package com.ruoyi.project.invoice.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.invoice.mapper.OaTrainTicketMapper;
import com.ruoyi.project.invoice.domain.OaTrainTicket;
import com.ruoyi.project.invoice.service.IOaTrainTicketService;

/**
 * 火车票Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-21
 */
@Service
public class OaTrainTicketServiceImpl implements IOaTrainTicketService
{
    @Autowired
    private OaTrainTicketMapper oaTrainTicketMapper;

    /**
     * 查询火车票
     *
     * @param id 火车票ID
     * @return 火车票
     */
    @Override
    public OaTrainTicket selectOaTrainTicketById(Long id)
    {
        return oaTrainTicketMapper.selectOaTrainTicketById(id);
    }

    /**
     * 查询火车票列表
     *
     * @param oaTrainTicket 火车票
     * @return 火车票
     */
    @Override
    public List<OaTrainTicket> selectOaTrainTicketList(OaTrainTicket oaTrainTicket)
    {
        return oaTrainTicketMapper.selectOaTrainTicketList(oaTrainTicket);
    }

    /**
     * 新增火车票
     *
     * @param oaTrainTicket 火车票
     * @return 结果
     */
    @Override
    public int insertOaTrainTicket(OaTrainTicket oaTrainTicket)
    {

        oaTrainTicket.setCreateTime(DateUtils.getNowDate());
        return oaTrainTicketMapper.insertOaTrainTicket(oaTrainTicket);
    }

    /**
     * 修改火车票
     *
     * @param oaTrainTicket 火车票
     * @return 结果
     */
    @Override
    public int updateOaTrainTicket(OaTrainTicket oaTrainTicket)
    {
        return oaTrainTicketMapper.updateOaTrainTicket(oaTrainTicket);
    }

    /**
     * 批量删除火车票
     *
     * @param ids 需要删除的火车票ID
     * @return 结果
     */
    @Override
    public int deleteOaTrainTicketByIds(Long[] ids)
    {
        return oaTrainTicketMapper.deleteOaTrainTicketByIds(ids);
    }

    /**
     * 删除火车票信息
     *
     * @param id 火车票ID
     * @return 结果
     */
    @Override
    public int deleteOaTrainTicketById(Long id)
    {
        return oaTrainTicketMapper.deleteOaTrainTicketById(id);
    }
}
