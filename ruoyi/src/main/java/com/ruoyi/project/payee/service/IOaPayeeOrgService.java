package com.ruoyi.project.payee.service;

import java.util.List;
import com.ruoyi.project.payee.domain.OaPayeeOrg;

/**
 * 收款方（企业）信息Service接口
 * 
 * @author ruoyi
 * @date 2020-06-03
 */
public interface IOaPayeeOrgService 
{
    /**
     * 查询收款方（企业）信息
     * 
     * @param id 收款方（企业）信息ID
     * @return 收款方（企业）信息
     */
    public OaPayeeOrg selectOaPayeeOrgById(Long id);

    /**
     * 查询收款方（企业）信息列表
     * 
     * @param oaPayeeOrg 收款方（企业）信息
     * @return 收款方（企业）信息集合
     */
    public List<OaPayeeOrg> selectOaPayeeOrgList(OaPayeeOrg oaPayeeOrg);

    /**
     * 新增收款方（企业）信息
     * 
     * @param oaPayeeOrg 收款方（企业）信息
     * @return 结果
     */
    public int insertOaPayeeOrg(OaPayeeOrg oaPayeeOrg);

    /**
     * 修改收款方（企业）信息
     * 
     * @param oaPayeeOrg 收款方（企业）信息
     * @return 结果
     */
    public int updateOaPayeeOrg(OaPayeeOrg oaPayeeOrg);

    /**
     * 批量删除收款方（企业）信息
     * 
     * @param ids 需要删除的收款方（企业）信息ID
     * @return 结果
     */
    public int deleteOaPayeeOrgByIds(Long[] ids);

    /**
     * 删除收款方（企业）信息信息
     * 
     * @param id 收款方（企业）信息ID
     * @return 结果
     */
    public int deleteOaPayeeOrgById(Long id);
}
