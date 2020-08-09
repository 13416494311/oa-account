package com.ruoyi.project.payee.mapper;

import java.util.List;
import com.ruoyi.project.payee.domain.OaPayeeUser;

/**
 * 收款方（个人）信息Mapper接口
 * 
 * @author ruoyi
 * @date 2020-06-03
 */
public interface OaPayeeUserMapper 
{
    /**
     * 查询收款方（个人）信息
     * 
     * @param id 收款方（个人）信息ID
     * @return 收款方（个人）信息
     */
    public OaPayeeUser selectOaPayeeUserById(Long id);

    /**
     * 查询收款方（个人）信息列表
     * 
     * @param oaPayeeUser 收款方（个人）信息
     * @return 收款方（个人）信息集合
     */
    public List<OaPayeeUser> selectOaPayeeUserList(OaPayeeUser oaPayeeUser);

    /**
     * 新增收款方（个人）信息
     * 
     * @param oaPayeeUser 收款方（个人）信息
     * @return 结果
     */
    public int insertOaPayeeUser(OaPayeeUser oaPayeeUser);

    /**
     * 修改收款方（个人）信息
     * 
     * @param oaPayeeUser 收款方（个人）信息
     * @return 结果
     */
    public int updateOaPayeeUser(OaPayeeUser oaPayeeUser);

    /**
     * 删除收款方（个人）信息
     * 
     * @param id 收款方（个人）信息ID
     * @return 结果
     */
    public int deleteOaPayeeUserById(Long id);

    /**
     * 批量删除收款方（个人）信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOaPayeeUserByIds(Long[] ids);
}
