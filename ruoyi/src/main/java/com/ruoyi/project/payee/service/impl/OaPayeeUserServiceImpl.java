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
import com.ruoyi.project.payee.mapper.OaPayeeUserMapper;
import com.ruoyi.project.payee.domain.OaPayeeUser;
import com.ruoyi.project.payee.service.IOaPayeeUserService;

/**
 * 收款方（个人）信息Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-03
 */
@Service
public class OaPayeeUserServiceImpl implements IOaPayeeUserService
{
    @Autowired
    private OaPayeeUserMapper oaPayeeUserMapper;

    /**
     * 查询收款方（个人）信息
     *
     * @param id 收款方（个人）信息ID
     * @return 收款方（个人）信息
     */
    @Override
    public OaPayeeUser selectOaPayeeUserById(Long id)
    {
        return oaPayeeUserMapper.selectOaPayeeUserById(id);
    }

    /**
     * 查询收款方（个人）信息列表
     *
     * @param oaPayeeUser 收款方（个人）信息
     * @return 收款方（个人）信息
     */
    @Override
    @DataScope(deptAlias = "u", userAlias = "u")
    public List<OaPayeeUser> selectOaPayeeUserList(OaPayeeUser oaPayeeUser)
    {
        return oaPayeeUserMapper.selectOaPayeeUserList(oaPayeeUser);
    }

    /**
     * 新增收款方（个人）信息
     *
     * @param oaPayeeUser 收款方（个人）信息
     * @return 结果
     */
    @Override
    public int insertOaPayeeUser(OaPayeeUser oaPayeeUser)
    {
        LoginUser loginUser = SpringUtils.getBean(TokenService.class).getLoginUser(ServletUtils.getRequest());
        SysUser currentUser = loginUser.getUser();
        oaPayeeUser.setDeptId(currentUser.getDeptId());
        oaPayeeUser.setUserId(currentUser.getUserId());
        oaPayeeUser.setCreateTime(DateUtils.getNowDate());
        return oaPayeeUserMapper.insertOaPayeeUser(oaPayeeUser);
    }

    /**
     * 修改收款方（个人）信息
     *
     * @param oaPayeeUser 收款方（个人）信息
     * @return 结果
     */
    @Override
    public int updateOaPayeeUser(OaPayeeUser oaPayeeUser)
    {
        return oaPayeeUserMapper.updateOaPayeeUser(oaPayeeUser);
    }

    /**
     * 批量删除收款方（个人）信息
     *
     * @param ids 需要删除的收款方（个人）信息ID
     * @return 结果
     */
    @Override
    public int deleteOaPayeeUserByIds(Long[] ids)
    {
        return oaPayeeUserMapper.deleteOaPayeeUserByIds(ids);
    }

    /**
     * 删除收款方（个人）信息信息
     *
     * @param id 收款方（个人）信息ID
     * @return 结果
     */
    @Override
    public int deleteOaPayeeUserById(Long id)
    {
        return oaPayeeUserMapper.deleteOaPayeeUserById(id);
    }
}
