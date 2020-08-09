package com.ruoyi.project.invoice.service.impl;

import java.util.List;
                                                import com.ruoyi.common.utils.ServletUtils;
    import com.ruoyi.common.utils.spring.SpringUtils;
    import com.ruoyi.framework.security.LoginUser;
    import com.ruoyi.framework.security.service.TokenService;
    import com.ruoyi.project.system.domain.SysUser;
            import com.ruoyi.common.utils.ServletUtils;
    import com.ruoyi.common.utils.spring.SpringUtils;
    import com.ruoyi.framework.security.LoginUser;
    import com.ruoyi.framework.security.service.TokenService;
    import com.ruoyi.project.system.domain.SysUser;
    import com.ruoyi.common.utils.DateUtils;
    import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.invoice.mapper.OaInvoiceCommodityMapper;
import com.ruoyi.project.invoice.domain.OaInvoiceCommodity;
import com.ruoyi.project.invoice.service.IOaInvoiceCommodityService;

/**
 * 增值税发票商品Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-08
 */
@Service
public class OaInvoiceCommodityServiceImpl implements IOaInvoiceCommodityService
{
    @Autowired
    private OaInvoiceCommodityMapper oaInvoiceCommodityMapper;

    /**
     * 查询增值税发票商品
     *
     * @param id 增值税发票商品ID
     * @return 增值税发票商品
     */
    @Override
    public OaInvoiceCommodity selectOaInvoiceCommodityById(Long id)
    {
        return oaInvoiceCommodityMapper.selectOaInvoiceCommodityById(id);
    }

    /**
     * 查询增值税发票商品列表
     *
     * @param oaInvoiceCommodity 增值税发票商品
     * @return 增值税发票商品
     */
    @Override
    public List<OaInvoiceCommodity> selectOaInvoiceCommodityList(OaInvoiceCommodity oaInvoiceCommodity)
    {
        return oaInvoiceCommodityMapper.selectOaInvoiceCommodityList(oaInvoiceCommodity);
    }

    /**
     * 新增增值税发票商品
     *
     * @param oaInvoiceCommodity 增值税发票商品
     * @return 结果
     */
    @Override
    public int insertOaInvoiceCommodity(OaInvoiceCommodity oaInvoiceCommodity)
    {
        oaInvoiceCommodity.setCreateTime(DateUtils.getNowDate());
        return oaInvoiceCommodityMapper.insertOaInvoiceCommodity(oaInvoiceCommodity);
    }

    /**
     * 修改增值税发票商品
     *
     * @param oaInvoiceCommodity 增值税发票商品
     * @return 结果
     */
    @Override
    public int updateOaInvoiceCommodity(OaInvoiceCommodity oaInvoiceCommodity)
    {
        return oaInvoiceCommodityMapper.updateOaInvoiceCommodity(oaInvoiceCommodity);
    }

    /**
     * 批量删除增值税发票商品
     *
     * @param ids 需要删除的增值税发票商品ID
     * @return 结果
     */
    @Override
    public int deleteOaInvoiceCommodityByIds(Long[] ids)
    {
        return oaInvoiceCommodityMapper.deleteOaInvoiceCommodityByIds(ids);
    }

    /**
     * 删除增值税发票商品信息
     *
     * @param id 增值税发票商品ID
     * @return 结果
     */
    @Override
    public int deleteOaInvoiceCommodityById(Long id)
    {
        return oaInvoiceCommodityMapper.deleteOaInvoiceCommodityById(id);
    }
}
