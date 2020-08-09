package com.ruoyi.project.payee.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.project.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.payee.mapper.OaPayeeOrgMapper;
import com.ruoyi.project.payee.domain.OaPayeeOrg;
import com.ruoyi.project.payee.service.IOaPayeeOrgService;

/**
 * 收款方（企业）信息Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-03
 */
@Service
public class OaPayeeOrgServiceImpl implements IOaPayeeOrgService
{
    @Autowired
    private OaPayeeOrgMapper oaPayeeOrgMapper;

    /**
     * 查询收款方（企业）信息
     *
     * @param id 收款方（企业）信息ID
     * @return 收款方（企业）信息
     */
    @Override
    public OaPayeeOrg selectOaPayeeOrgById(Long id)
    {
        return oaPayeeOrgMapper.selectOaPayeeOrgById(id);
    }

    /**
     * 查询收款方（企业）信息列表
     *
     * @param oaPayeeOrg 收款方（企业）信息
     * @return 收款方（企业）信息
     */
    @Override
    @DataScope(deptAlias = "o", userAlias = "o")
    public List<OaPayeeOrg> selectOaPayeeOrgList(OaPayeeOrg oaPayeeOrg)
    {
        return oaPayeeOrgMapper.selectOaPayeeOrgList(oaPayeeOrg);
    }

    /**
     * 新增收款方（企业）信息
     *
     * @param oaPayeeOrg 收款方（企业）信息
     * @return 结果
     */
    @Override
    public int insertOaPayeeOrg(OaPayeeOrg oaPayeeOrg)
    {
        LoginUser loginUser = SpringUtils.getBean(TokenService.class).getLoginUser(ServletUtils.getRequest());
        SysUser currentUser = loginUser.getUser();
        oaPayeeOrg.setDeptId(currentUser.getDeptId());
        oaPayeeOrg.setUserId(currentUser.getUserId());
        oaPayeeOrg.setCreateTime(DateUtils.getNowDate());
        return oaPayeeOrgMapper.insertOaPayeeOrg(oaPayeeOrg);
    }

    /**
     * 修改收款方（企业）信息
     *
     * @param oaPayeeOrg 收款方（企业）信息
     * @return 结果
     */
    @Override
    public int updateOaPayeeOrg(OaPayeeOrg oaPayeeOrg)
    {
        return oaPayeeOrgMapper.updateOaPayeeOrg(oaPayeeOrg);
    }

    /**
     * 批量删除收款方（企业）信息
     *
     * @param ids 需要删除的收款方（企业）信息ID
     * @return 结果
     */
    @Override
    public int deleteOaPayeeOrgByIds(Long[] ids)
    {
        return oaPayeeOrgMapper.deleteOaPayeeOrgByIds(ids);
    }

    /**
     * 删除收款方（企业）信息信息
     *
     * @param id 收款方（企业）信息ID
     * @return 结果
     */
    @Override
    public int deleteOaPayeeOrgById(Long id)
    {
        return oaPayeeOrgMapper.deleteOaPayeeOrgById(id);
    }
}
