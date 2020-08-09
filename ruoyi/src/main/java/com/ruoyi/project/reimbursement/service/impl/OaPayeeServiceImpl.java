package com.ruoyi.project.reimbursement.service.impl;

import java.util.List;
                                import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.reimbursement.mapper.OaPayeeMapper;
import com.ruoyi.project.reimbursement.domain.OaPayee;
import com.ruoyi.project.reimbursement.service.IOaPayeeService;

/**
 * 付款信息Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-13
 */
@Service
public class OaPayeeServiceImpl implements IOaPayeeService
{
    @Autowired
    private OaPayeeMapper oaPayeeMapper;

    /**
     * 查询付款信息
     *
     * @param id 付款信息ID
     * @return 付款信息
     */
    @Override
    public OaPayee selectOaPayeeById(Long id)
    {
        return oaPayeeMapper.selectOaPayeeById(id);
    }

    @Override
    public OaPayee selectOaPayeeByApplyUuid(String applyUuid)
    {
        return oaPayeeMapper.selectOaPayeeByApplyUuid(applyUuid);
    }

    /**
     * 查询付款信息列表
     *
     * @param oaPayee 付款信息
     * @return 付款信息
     */
    @Override
    public List<OaPayee> selectOaPayeeList(OaPayee oaPayee)
    {
        return oaPayeeMapper.selectOaPayeeList(oaPayee);
    }

    @Override
    public List<OaPayee> remoteSearch(OaPayee oaPayee)
    {
        List<OaPayee> list = null ;
        if(StringUtils.isNotEmpty(oaPayee.getPayee())){
            list = oaPayeeMapper.remoteSearch(oaPayee);
        }
        return list;
    }

    /**
     * 新增付款信息
     *
     * @param oaPayee 付款信息
     * @return 结果
     */
    @Override
    public int insertOaPayee(OaPayee oaPayee)
    {
        oaPayee.setCreateTime(DateUtils.getNowDate());
        return oaPayeeMapper.insertOaPayee(oaPayee);
    }

    /**
     * 修改付款信息
     *
     * @param oaPayee 付款信息
     * @return 结果
     */
    @Override
    public int updateOaPayee(OaPayee oaPayee)
    {
        return oaPayeeMapper.updateOaPayee(oaPayee);
    }

    /**
     * 批量删除付款信息
     *
     * @param ids 需要删除的付款信息ID
     * @return 结果
     */
    @Override
    public int deleteOaPayeeByIds(Long[] ids)
    {
        return oaPayeeMapper.deleteOaPayeeByIds(ids);
    }

    /**
     * 删除付款信息信息
     *
     * @param id 付款信息ID
     * @return 结果
     */
    @Override
    public int deleteOaPayeeById(Long id)
    {
        return oaPayeeMapper.deleteOaPayeeById(id);
    }
}
