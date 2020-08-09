package com.ruoyi.project.invoice.mapper;

import java.util.List;
import com.ruoyi.project.invoice.domain.OaInvoice;

/**
 * 增值税发票Mapper接口
 *
 * @author ruoyi
 * @date 2020-06-08
 */
public interface OaInvoiceMapper
{
    /**
     * 查询增值税发票
     *
     * @param id 增值税发票ID
     * @return 增值税发票
     */
    public OaInvoice selectOaInvoiceById(Long id);


    public OaInvoice selectOaInvoiceByInvoiceUuid(String invoiceUuid);
    /**
     * 查询增值税发票列表
     *
     * @param oaInvoice 增值税发票
     * @return 增值税发票集合
     */
    public List<OaInvoice> selectOaInvoiceList(OaInvoice oaInvoice);

    public List<OaInvoice> selectOaInvoiceListByUuid(String uuid);

    /**
     * 新增增值税发票
     *
     * @param oaInvoice 增值税发票
     * @return 结果
     */
    public int insertOaInvoice(OaInvoice oaInvoice);

    /**
     * 修改增值税发票
     *
     * @param oaInvoice 增值税发票
     * @return 结果
     */
    public int updateOaInvoice(OaInvoice oaInvoice);

    /**
     * 删除增值税发票
     *
     * @param id 增值税发票ID
     * @return 结果
     */
    public int deleteOaInvoiceById(Long id);

    public int deleteOaInvoiceByUuid(String uuid);

    /**
     * 批量删除增值税发票
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOaInvoiceByIds(Long[] ids);
}
