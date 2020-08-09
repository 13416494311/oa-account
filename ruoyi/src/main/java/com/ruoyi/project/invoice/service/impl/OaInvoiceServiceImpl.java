package com.ruoyi.project.invoice.service.impl;

import java.util.List;
                                                                                                            import com.ruoyi.common.utils.ServletUtils;
    import com.ruoyi.common.utils.spring.SpringUtils;
    import com.ruoyi.framework.security.LoginUser;
    import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.project.invoice.domain.OaInvoiceCommodity;
import com.ruoyi.project.invoice.mapper.OaInvoiceCommodityMapper;
import com.ruoyi.project.system.domain.SysUser;
            import com.ruoyi.common.utils.ServletUtils;
    import com.ruoyi.common.utils.spring.SpringUtils;
    import com.ruoyi.framework.security.LoginUser;
    import com.ruoyi.framework.security.service.TokenService;
    import com.ruoyi.project.system.domain.SysUser;
    import com.ruoyi.common.utils.DateUtils;
    import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.invoice.mapper.OaInvoiceMapper;
import com.ruoyi.project.invoice.domain.OaInvoice;
import com.ruoyi.project.invoice.service.IOaInvoiceService;

/**
 * 增值税发票Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-08
 */
@Service
public class OaInvoiceServiceImpl implements IOaInvoiceService
{
    @Autowired
    private OaInvoiceMapper oaInvoiceMapper;
    @Autowired
    private OaInvoiceCommodityMapper oaInvoiceCommodityMapper;

    /**
     * 查询增值税发票
     *
     * @param id 增值税发票ID
     * @return 增值税发票
     */
    @Override
    public OaInvoice selectOaInvoiceById(Long id)
    {
        return oaInvoiceMapper.selectOaInvoiceById(id);
    }


    public OaInvoice selectOaInvoiceByUuid(String uuid){
        return oaInvoiceMapper.selectOaInvoiceByInvoiceUuid(uuid);
    }

    /**
     * 查询增值税发票列表
     *
     * @param oaInvoice 增值税发票
     * @return 增值税发票
     */
    @Override
    public List<OaInvoice> selectOaInvoiceList(OaInvoice oaInvoice)
    {
        List<OaInvoice> list = oaInvoiceMapper.selectOaInvoiceList(oaInvoice);
        for(OaInvoice invoice:list){
            OaInvoiceCommodity commodity = new OaInvoiceCommodity();
            commodity.setInvoiceUuid(invoice.getInvoiceUuid());
            List<OaInvoiceCommodity> commodityList = oaInvoiceCommodityMapper.selectOaInvoiceCommodityList(commodity);
            invoice.setOaInvoiceCommodity(commodityList);
        }
        return list;
    }

    @Override
    public List<OaInvoice> selectOaInvoiceListByUuid(String uuid)
    {
        List<OaInvoice> list = oaInvoiceMapper.selectOaInvoiceListByUuid(uuid);
        for(OaInvoice oaInvoice:list){
            OaInvoiceCommodity commodity = new OaInvoiceCommodity();
            commodity.setInvoiceUuid(oaInvoice.getInvoiceUuid());
            List<OaInvoiceCommodity> commodityList = oaInvoiceCommodityMapper.selectOaInvoiceCommodityList(commodity);
            oaInvoice.setOaInvoiceCommodity(commodityList);
        }
        return list;
    }

    /**
     * 新增增值税发票
     *
     * @param oaInvoice 增值税发票
     * @return 结果
     */
    @Override
    public int insertOaInvoice(OaInvoice oaInvoice)
    {
        oaInvoice.setCreateTime(DateUtils.getNowDate());
        return oaInvoiceMapper.insertOaInvoice(oaInvoice);
    }

    /**
     * 修改增值税发票
     *
     * @param oaInvoice 增值税发票
     * @return 结果
     */
    @Override
    public int updateOaInvoice(OaInvoice oaInvoice)
    {
        return oaInvoiceMapper.updateOaInvoice(oaInvoice);
    }

    /**
     * 批量删除增值税发票
     *
     * @param ids 需要删除的增值税发票ID
     * @return 结果
     */
    @Override
    public int deleteOaInvoiceByIds(Long[] ids)
    {
        for(Long id : ids){
            OaInvoice oaInvoice = oaInvoiceMapper.selectOaInvoiceById(id);
            oaInvoiceCommodityMapper.deleteOaInvoiceCommodityByinvoiceUuid(oaInvoice.getInvoiceUuid());
        }
        return oaInvoiceMapper.deleteOaInvoiceByIds(ids);
    }

    /**
     * 删除增值税发票信息
     *
     * @param id 增值税发票ID
     * @return 结果
     */
    @Override
    public int deleteOaInvoiceById(Long id)
    {
        return oaInvoiceMapper.deleteOaInvoiceById(id);
    }

    @Override
    public int deleteOaInvoiceByInvoiceUuid(String invoiceUuid)
    {
        OaInvoice oaInvoice = oaInvoiceMapper.selectOaInvoiceByInvoiceUuid(invoiceUuid);
        oaInvoiceCommodityMapper.deleteOaInvoiceCommodityByinvoiceUuid(oaInvoice.getInvoiceUuid());
        return oaInvoiceMapper.deleteOaInvoiceById(oaInvoice.getId());
    }
}
