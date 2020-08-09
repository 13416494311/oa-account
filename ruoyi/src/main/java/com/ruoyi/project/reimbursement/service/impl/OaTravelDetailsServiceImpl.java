package com.ruoyi.project.reimbursement.service.impl;

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
import com.ruoyi.project.reimbursement.mapper.OaTravelDetailsMapper;
import com.ruoyi.project.reimbursement.domain.OaTravelDetails;
import com.ruoyi.project.reimbursement.service.IOaTravelDetailsService;

/**
 * 差旅报销明细Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-07
 */
@Service
public class OaTravelDetailsServiceImpl implements IOaTravelDetailsService
{
    @Autowired
    private OaTravelDetailsMapper oaTravelDetailsMapper;

    /**
     * 查询差旅报销明细
     *
     * @param id 差旅报销明细ID
     * @return 差旅报销明细
     */
    @Override
    public OaTravelDetails selectOaTravelDetailsById(Long id)
    {
        return oaTravelDetailsMapper.selectOaTravelDetailsById(id);
    }

    /**
     * 查询差旅报销明细列表
     *
     * @param oaTravelDetails 差旅报销明细
     * @return 差旅报销明细
     */
    @Override
    public List<OaTravelDetails> selectOaTravelDetailsList(OaTravelDetails oaTravelDetails)
    {
        return oaTravelDetailsMapper.selectOaTravelDetailsList(oaTravelDetails);
    }

    /**
     * 新增差旅报销明细
     *
     * @param oaTravelDetails 差旅报销明细
     * @return 结果
     */
    @Override
    public int insertOaTravelDetails(OaTravelDetails oaTravelDetails)
    {
        oaTravelDetails.setCreateTime(DateUtils.getNowDate());
        return oaTravelDetailsMapper.insertOaTravelDetails(oaTravelDetails);
    }

    /**
     * 修改差旅报销明细
     *
     * @param oaTravelDetails 差旅报销明细
     * @return 结果
     */
    @Override
    public int updateOaTravelDetails(OaTravelDetails oaTravelDetails)
    {
        return oaTravelDetailsMapper.updateOaTravelDetails(oaTravelDetails);
    }

    /**
     * 批量删除差旅报销明细
     *
     * @param ids 需要删除的差旅报销明细ID
     * @return 结果
     */
    @Override
    public int deleteOaTravelDetailsByIds(Long[] ids)
    {
        return oaTravelDetailsMapper.deleteOaTravelDetailsByIds(ids);
    }

    @Override
    public int deleteOaTravelDetailsByApplyUuids(String[] applyUuids)
    {
        return oaTravelDetailsMapper.deleteOaTravelDetailsByApplyUuids(applyUuids);
    }

    /**
     * 删除差旅报销明细信息
     *
     * @param id 差旅报销明细ID
     * @return 结果
     */
    @Override
    public int deleteOaTravelDetailsById(Long id)
    {
        return oaTravelDetailsMapper.deleteOaTravelDetailsById(id);
    }
}
