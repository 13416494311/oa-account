package com.ruoyi.project.invoice.mapper;

import java.util.List;
import com.ruoyi.project.invoice.domain.OaAirTicket;

/**
 * 机票行程单Mapper接口
 *
 * @author ruoyi
 * @date 2020-06-21
 */
public interface OaAirTicketMapper
{
    /**
     * 查询机票行程单
     *
     * @param id 机票行程单ID
     * @return 机票行程单
     */
    public OaAirTicket selectOaAirTicketById(Long id);

    /**
     * 查询机票行程单列表
     *
     * @param oaAirTicket 机票行程单
     * @return 机票行程单集合
     */
    public List<OaAirTicket> selectOaAirTicketList(OaAirTicket oaAirTicket);

    /**
     * 新增机票行程单
     *
     * @param oaAirTicket 机票行程单
     * @return 结果
     */
    public int insertOaAirTicket(OaAirTicket oaAirTicket);

    /**
     * 修改机票行程单
     *
     * @param oaAirTicket 机票行程单
     * @return 结果
     */
    public int updateOaAirTicket(OaAirTicket oaAirTicket);

    /**
     * 删除机票行程单
     *
     * @param id 机票行程单ID
     * @return 结果
     */
    public int deleteOaAirTicketById(Long id);

    /**
     * 批量删除机票行程单
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOaAirTicketByIds(Long[] ids);
}
