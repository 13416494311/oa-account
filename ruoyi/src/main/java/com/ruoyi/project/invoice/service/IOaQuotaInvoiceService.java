package com.ruoyi.project.invoice.service;

import java.util.List;
import com.ruoyi.project.invoice.domain.OaQuotaInvoice;

/**
 * 定额发票Service接口
 *
 * @author ruoyi
 * @date 2020-06-20
 */
public interface IOaQuotaInvoiceService
{
    /**
     * 查询定额发票
     *
     * @param id 定额发票ID
     * @return 定额发票
     */
    public OaQuotaInvoice selectOaQuotaInvoiceById(Long id);


    public OaQuotaInvoice selectOaQuotaInvoiceByInvoiceNumber(String invoiceNumber);

    /**
     * 查询定额发票列表
     *
     * @param oaQuotaInvoice 定额发票
     * @return 定额发票集合
     */
    public List<OaQuotaInvoice> selectOaQuotaInvoiceList(OaQuotaInvoice oaQuotaInvoice);

    /**
     * 新增定额发票
     *
     * @param oaQuotaInvoice 定额发票
     * @return 结果
     */
    public int insertOaQuotaInvoice(OaQuotaInvoice oaQuotaInvoice);

    /**
     * 修改定额发票
     *
     * @param oaQuotaInvoice 定额发票
     * @return 结果
     */
    public int updateOaQuotaInvoice(OaQuotaInvoice oaQuotaInvoice);

    /**
     * 批量删除定额发票
     *
     * @param ids 需要删除的定额发票ID
     * @return 结果
     */
    public int deleteOaQuotaInvoiceByIds(Long[] ids);

    /**
     * 删除定额发票信息
     *
     * @param id 定额发票ID
     * @return 结果
     */
    public int deleteOaQuotaInvoiceById(Long id);
}
