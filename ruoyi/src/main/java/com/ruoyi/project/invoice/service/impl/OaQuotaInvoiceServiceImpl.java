package com.ruoyi.project.invoice.service.impl;

import java.util.List;
                                import com.ruoyi.common.utils.DateUtils;
    import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.invoice.mapper.OaQuotaInvoiceMapper;
import com.ruoyi.project.invoice.domain.OaQuotaInvoice;
import com.ruoyi.project.invoice.service.IOaQuotaInvoiceService;

/**
 * 定额发票Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-20
 */
@Service
public class OaQuotaInvoiceServiceImpl implements IOaQuotaInvoiceService
{
    @Autowired
    private OaQuotaInvoiceMapper oaQuotaInvoiceMapper;

    /**
     * 查询定额发票
     *
     * @param id 定额发票ID
     * @return 定额发票
     */
    @Override
    public OaQuotaInvoice selectOaQuotaInvoiceById(Long id)
    {
        return oaQuotaInvoiceMapper.selectOaQuotaInvoiceById(id);
    }

    public OaQuotaInvoice selectOaQuotaInvoiceByInvoiceNumber(String invoiceNumber){
        return oaQuotaInvoiceMapper.selectOaQuotaInvoiceByInvoiceNumber(invoiceNumber);
    }

    /**
     * 查询定额发票列表
     *
     * @param oaQuotaInvoice 定额发票
     * @return 定额发票
     */
    @Override
    public List<OaQuotaInvoice> selectOaQuotaInvoiceList(OaQuotaInvoice oaQuotaInvoice)
    {
        return oaQuotaInvoiceMapper.selectOaQuotaInvoiceList(oaQuotaInvoice);
    }

    /**
     * 新增定额发票
     *
     * @param oaQuotaInvoice 定额发票
     * @return 结果
     */
    @Override
    public int insertOaQuotaInvoice(OaQuotaInvoice oaQuotaInvoice)
    {

        oaQuotaInvoice.setCreateTime(DateUtils.getNowDate());
        return oaQuotaInvoiceMapper.insertOaQuotaInvoice(oaQuotaInvoice);
    }

    /**
     * 修改定额发票
     *
     * @param oaQuotaInvoice 定额发票
     * @return 结果
     */
    @Override
    public int updateOaQuotaInvoice(OaQuotaInvoice oaQuotaInvoice)
    {
        return oaQuotaInvoiceMapper.updateOaQuotaInvoice(oaQuotaInvoice);
    }

    /**
     * 批量删除定额发票
     *
     * @param ids 需要删除的定额发票ID
     * @return 结果
     */
    @Override
    public int deleteOaQuotaInvoiceByIds(Long[] ids)
    {
        return oaQuotaInvoiceMapper.deleteOaQuotaInvoiceByIds(ids);
    }

    /**
     * 删除定额发票信息
     *
     * @param id 定额发票ID
     * @return 结果
     */
    @Override
    public int deleteOaQuotaInvoiceById(Long id)
    {
        return oaQuotaInvoiceMapper.deleteOaQuotaInvoiceById(id);
    }
}
