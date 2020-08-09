package com.ruoyi.project.invoice.mapper;

import java.util.List;
import com.ruoyi.project.invoice.domain.OaTaxiTicket;

/**
 * 出租车发票Mapper接口
 * 
 * @author ruoyi
 * @date 2020-07-31
 */
public interface OaTaxiTicketMapper 
{
    /**
     * 查询出租车发票
     * 
     * @param id 出租车发票ID
     * @return 出租车发票
     */
    public OaTaxiTicket selectOaTaxiTicketById(Long id);

    /**
     * 查询出租车发票列表
     * 
     * @param oaTaxiTicket 出租车发票
     * @return 出租车发票集合
     */
    public List<OaTaxiTicket> selectOaTaxiTicketList(OaTaxiTicket oaTaxiTicket);

    /**
     * 新增出租车发票
     * 
     * @param oaTaxiTicket 出租车发票
     * @return 结果
     */
    public int insertOaTaxiTicket(OaTaxiTicket oaTaxiTicket);

    /**
     * 修改出租车发票
     * 
     * @param oaTaxiTicket 出租车发票
     * @return 结果
     */
    public int updateOaTaxiTicket(OaTaxiTicket oaTaxiTicket);

    /**
     * 删除出租车发票
     * 
     * @param id 出租车发票ID
     * @return 结果
     */
    public int deleteOaTaxiTicketById(Long id);

    /**
     * 批量删除出租车发票
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOaTaxiTicketByIds(Long[] ids);
}
