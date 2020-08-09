package com.ruoyi.project.invoice.mapper;

import java.util.List;
import com.ruoyi.project.invoice.domain.OaTrainTicket;

/**
 * 火车票Mapper接口
 * 
 * @author ruoyi
 * @date 2020-06-21
 */
public interface OaTrainTicketMapper 
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
     * 删除火车票
     * 
     * @param id 火车票ID
     * @return 结果
     */
    public int deleteOaTrainTicketById(Long id);

    /**
     * 批量删除火车票
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOaTrainTicketByIds(Long[] ids);
}
