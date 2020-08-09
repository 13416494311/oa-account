package com.ruoyi.project.reimbursement.service;

import java.util.List;
import com.ruoyi.project.reimbursement.domain.OaPayee;

/**
 * 付款信息Service接口
 *
 * @author ruoyi
 * @date 2020-06-13
 */
public interface IOaPayeeService
{
    /**
     * 查询付款信息
     *
     * @param id 付款信息ID
     * @return 付款信息
     */
    public OaPayee selectOaPayeeById(Long id);

    public OaPayee selectOaPayeeByApplyUuid(String applyUuid);
    /**
     * 查询付款信息列表
     *
     * @param oaPayee 付款信息
     * @return 付款信息集合
     */
    public List<OaPayee> selectOaPayeeList(OaPayee oaPayee);

    public List<OaPayee> remoteSearch(OaPayee oaPayee);

    /**
     * 新增付款信息
     *
     * @param oaPayee 付款信息
     * @return 结果
     */
    public int insertOaPayee(OaPayee oaPayee);

    /**
     * 修改付款信息
     *
     * @param oaPayee 付款信息
     * @return 结果
     */
    public int updateOaPayee(OaPayee oaPayee);

    /**
     * 批量删除付款信息
     *
     * @param ids 需要删除的付款信息ID
     * @return 结果
     */
    public int deleteOaPayeeByIds(Long[] ids);

    /**
     * 删除付款信息信息
     *
     * @param id 付款信息ID
     * @return 结果
     */
    public int deleteOaPayeeById(Long id);
}
