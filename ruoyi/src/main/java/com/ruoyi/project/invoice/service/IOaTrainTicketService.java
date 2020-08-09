package com.ruoyi.project.invoice.service;

import java.util.List;
import com.ruoyi.project.invoice.domain.OaTrainTicket;

/**
 * 火车票Service接口
 * 
 * @author ruoyi
 * @date 2020-06-21
 */
public interface IOaTrainTicketService 
{
    /**
     * 查询火车票
     * 
     * @param id 火车票ID
     * @return 火车票
     */
    public OaTrainTicket selectOaTrainTicketById(Long id);

    /**
     * 查询火车票列表
     * 
     * @param oaTrainTicket 火车票
     * @return 火车票集合
     */
    public List<OaTrainTicket> selectOaTrainTicketList(OaTrainTicket oaTrainTicket);

    /**
     * 新增火车票
     * 
     * @param oaTrainTicket 火车票
     * @return 结果
     */
    public int insertOaTrainTicket(OaTrainTicket oaTrainTicket);

    /**
     * 修改火车票
     * 
     * @param oaTrainTicket 火车票
     * @return 结果
     */
    public int updateOaTrainTicket(OaTrainTicket oaTrainTicket);

    /**
     * 批量删除火车票
     * 
     * @param ids 需要删除的火车票ID
     * @return 结果
     */
    public int deleteOaTrainTicketByIds(Long[] ids);

    /**
     * 删除火车票信息
     * 
     * @param id 火车票ID
     * @return 结果
     */
    public int deleteOaTrainTicketById(Long id);
}
