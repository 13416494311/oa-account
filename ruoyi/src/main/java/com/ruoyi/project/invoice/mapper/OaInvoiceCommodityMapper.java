package com.ruoyi.project.invoice.mapper;

import java.util.List;
import com.ruoyi.project.invoice.domain.OaInvoiceCommodity;

/**
 * 增值税发票商品Mapper接口
 *
 * @author ruoyi
 * @date 2020-06-08
 */
public interface OaInvoiceCommodityMapper
{
    /**
     * 查询增值税发票商品
     *
     * @param id 增值税发票商品ID
     * @return 增值税发票商品
     */
    public OaInvoiceCommodity selectOaInvoiceCommodityById(Long id);

    /**
     * 查询增值税发票商品列表
     *
     * @param oaInvoiceCommodity 增值税发票商品
     * @return 增值税发票商品集合
     */
    public List<OaInvoiceCommodity> selectOaInvoiceCommodityList(OaInvoiceCommodity oaInvoiceCommodity);

    /**
     * 新增增值税发票商品
     *
     * @param oaInvoiceCommodity 增值税发票商品
     * @return 结果
     */
    public int insertOaInvoiceCommodity(OaInvoiceCommodity oaInvoiceCommodity);

    /**
     * 修改增值税发票商品
     *
     * @param oaInvoiceCommodity 增值税发票商品
     * @return 结果
     */
    public int updateOaInvoiceCommodity(OaInvoiceCommodity oaInvoiceCommodity);

    /**
     * 删除增值税发票商品
     *
     * @param id 增值税发票商品ID
     * @return 结果
     */
    public int deleteOaInvoiceCommodityById(Long id);

    public int deleteOaInvoiceCommodityByinvoiceUuid(String invoiceUuid);

    /**
     * 批量删除增值税发票商品
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOaInvoiceCommodityByIds(Long[] ids);
}
