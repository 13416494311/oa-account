package com.ruoyi.project.invoice.service;

import java.util.List;
import com.ruoyi.project.invoice.domain.OaOtherInvoice;

/**
 * 其他发票Service接口
 * 
 * @author ruoyi
 * @date 2020-07-04
 */
public interface IOaOtherInvoiceService 
{
    /**
     * 查询其他发票
     * 
     * @param id 其他发票ID
     * @return 其他发票
     */
    public OaOtherInvoice selectOaOtherInvoiceById(Long id);

    /**
     * 查询其他发票列表
     * 
     * @param oaOtherInvoice 其他发票
     * @return 其他发票集合
     */
    public List<OaOtherInvoice> selectOaOtherInvoiceList(OaOtherInvoice oaOtherInvoice);

    /**
     * 新增其他发票
     * 
     * @param oaOtherInvoice 其他发票
     * @return 结果
     */
    public int insertOaOtherInvoice(OaOtherInvoice oaOtherInvoice);

    /**
     * 修改其他发票
     * 
     * @param oaOtherInvoice 其他发票
     * @return 结果
     */
    public int updateOaOtherInvoice(OaOtherInvoice oaOtherInvoice);

    /**
     * 批量删除其他发票
     * 
     * @param ids 需要删除的其他发票ID
     * @return 结果
     */
    public int deleteOaOtherInvoiceByIds(Long[] ids);

    /**
     * 删除其他发票信息
     * 
     * @param id 其他发票ID
     * @return 结果
     */
    public int deleteOaOtherInvoiceById(Long id);
}
