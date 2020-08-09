package com.ruoyi.project.invoice.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.invoice.mapper.OaTaxiTicketMapper;
import com.ruoyi.project.invoice.domain.OaTaxiTicket;
import com.ruoyi.project.invoice.service.IOaTaxiTicketService;

/**
 * 出租车发票Service业务层处理
 *
 * @author ruoyi
 * @date 2020-07-31
 */
@Service
public class OaTaxiTicketServiceImpl implements IOaTaxiTicketService
{
    @Autowired
    private OaTaxiTicketMapper oaTaxiTicketMapper;

    /**
     * 查询出租车发票
     *
     * @param id 出租车发票ID
     * @return 出租车发票
     */
    @Override
    public OaTaxiTicket selectOaTaxiTicketById(Long id)
    {
        return oaTaxiTicketMapper.selectOaTaxiTicketById(id);
    }

    /**
     * 查询出租车发票列表
     *
     * @param oaTaxiTicket 出租车发票
     * @return 出租车发票
     */
    @Override
    public List<OaTaxiTicket> selectOaTaxiTicketList(OaTaxiTicket oaTaxiTicket)
    {
        return oaTaxiTicketMapper.selectOaTaxiTicketList(oaTaxiTicket);
    }

    /**
     * 新增出租车发票
     *
     * @param oaTaxiTicket 出租车发票
     * @return 结果
     */
    @Override
    public int insertOaTaxiTicket(OaTaxiTicket oaTaxiTicket)
    {
        oaTaxiTicket.setCreateTime(DateUtils.getNowDate());
        return oaTaxiTicketMapper.insertOaTaxiTicket(oaTaxiTicket);
    }

    /**
     * 修改出租车发票
     *
     * @param oaTaxiTicket 出租车发票
     * @return 结果
     */
    @Override
    public int updateOaTaxiTicket(OaTaxiTicket oaTaxiTicket)
    {
        return oaTaxiTicketMapper.updateOaTaxiTicket(oaTaxiTicket);
    }

    /**
     * 批量删除出租车发票
     *
     * @param ids 需要删除的出租车发票ID
     * @return 结果
     */
    @Override
    public int deleteOaTaxiTicketByIds(Long[] ids)
    {
        return oaTaxiTicketMapper.deleteOaTaxiTicketByIds(ids);
    }

    /**
     * 删除出租车发票信息
     *
     * @param id 出租车发票ID
     * @return 结果
     */
    @Override
    public int deleteOaTaxiTicketById(Long id)
    {
        return oaTaxiTicketMapper.deleteOaTaxiTicketById(id);
    }
}
