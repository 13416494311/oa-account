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
import com.ruoyi.project.reimbursement.mapper.OaTravelMapper;
import com.ruoyi.project.reimbursement.domain.OaTravel;
import com.ruoyi.project.reimbursement.service.IOaTravelService;

/**
 * 差旅报销Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-07
 */
@Service
public class OaTravelServiceImpl implements IOaTravelService
{
    @Autowired
    private OaTravelMapper oaTravelMapper;

    /**
     * 查询差旅报销
     *
     * @param id 差旅报销ID
     * @return 差旅报销
     */
    @Override
    public OaTravel selectOaTravelById(Long id)
    {
        return oaTravelMapper.selectOaTravelById(id);
    }

    @Override
    public OaTravel selectOaTravelByApplyUuid(String applyUuid)
    {
        return oaTravelMapper.selectOaTravelByApplyUuid(applyUuid);
    }

    /**
     * 查询差旅报销列表
     *
     * @param oaTravel 差旅报销
     * @return 差旅报销
     */
    @Override
    public List<OaTravel> selectOaTravelList(OaTravel oaTravel)
    {
        return oaTravelMapper.selectOaTravelList(oaTravel);
    }

    /**
     * 新增差旅报销
     *
     * @param oaTravel 差旅报销
     * @return 结果
     */
    @Override
    public int insertOaTravel(OaTravel oaTravel)
    {

        oaTravel.setCreateTime(DateUtils.getNowDate());
        return oaTravelMapper.insertOaTravel(oaTravel);
    }

    /**
     * 修改差旅报销
     *
     * @param oaTravel 差旅报销
     * @return 结果
     */
    @Override
    public int updateOaTravel(OaTravel oaTravel)
    {
        return oaTravelMapper.updateOaTravel(oaTravel);
    }

    /**
     * 批量删除差旅报销
     *
     * @param ids 需要删除的差旅报销ID
     * @return 结果
     */
    @Override
    public int deleteOaTravelByIds(Long[] ids)
    {
        return oaTravelMapper.deleteOaTravelByIds(ids);
    }

    @Override
    public int deleteOaTravelByApplyUuids(String[] applyUuids)
    {
        return oaTravelMapper.deleteOaTravelByApplyUuids(applyUuids);
    }

    /**
     * 删除差旅报销信息
     *
     * @param id 差旅报销ID
     * @return 结果
     */
    @Override
    public int deleteOaTravelById(Long id)
    {
        return oaTravelMapper.deleteOaTravelById(id);
    }

    @Override
    public int deleteOaTravelByApplyUuid(String applyUuid)
    {
        return oaTravelMapper.deleteOaTravelByApplyUuid(applyUuid);
    }
}
