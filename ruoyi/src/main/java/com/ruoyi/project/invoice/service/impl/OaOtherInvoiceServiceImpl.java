package com.ruoyi.project.invoice.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.invoice.mapper.OaOtherInvoiceMapper;
import com.ruoyi.project.invoice.domain.OaOtherInvoice;
import com.ruoyi.project.invoice.service.IOaOtherInvoiceService;

/**
 * 其他发票Service业务层处理
 *
 * @author ruoyi
 * @date 2020-07-04
 */
@Service
public class OaOtherInvoiceServiceImpl implements IOaOtherInvoiceService
{
    @Autowired
    private OaOtherInvoiceMapper oaOtherInvoiceMapper;

    /**
     * 查询其他发票
     *
     * @param id 其他发票ID
     * @return 其他发票
     */
    @Override
    public OaOtherInvoice selectOaOtherInvoiceById(Long id)
    {
        return oaOtherInvoiceMapper.selectOaOtherInvoiceById(id);
    }

    /**
     * 查询其他发票列表
     *
     * @param oaOtherInvoice 其他发票
     * @return 其他发票
     */
    @Override
    public List<OaOtherInvoice> selectOaOtherInvoiceList(OaOtherInvoice oaOtherInvoice)
    {
        return oaOtherInvoiceMapper.selectOaOtherInvoiceList(oaOtherInvoice);
    }

    /**
     * 新增其他发票
     *
     * @param oaOtherInvoice 其他发票
     * @return 结果
     */
    @Override
    public int insertOaOtherInvoice(OaOtherInvoice oaOtherInvoice)
    {
        oaOtherInvoice.setCreateTime(DateUtils.getNowDate());
        return oaOtherInvoiceMapper.insertOaOtherInvoice(oaOtherInvoice);
    }

    /**
     * 修改其他发票
     *
     * @param oaOtherInvoice 其他发票
     * @return 结果
     */
    @Override
    public int updateOaOtherInvoice(OaOtherInvoice oaOtherInvoice)
    {
        return oaOtherInvoiceMapper.updateOaOtherInvoice(oaOtherInvoice);
    }

    /**
     * 批量删除其他发票
     *
     * @param ids 需要删除的其他发票ID
     * @return 结果
     */
    @Override
    public int deleteOaOtherInvoiceByIds(Long[] ids)
    {
        return oaOtherInvoiceMapper.deleteOaOtherInvoiceByIds(ids);
    }

    /**
     * 删除其他发票信息
     *
     * @param id 其他发票ID
     * @return 结果
     */
    @Override
    public int deleteOaOtherInvoiceById(Long id)
    {
        return oaOtherInvoiceMapper.deleteOaOtherInvoiceById(id);
    }
}
