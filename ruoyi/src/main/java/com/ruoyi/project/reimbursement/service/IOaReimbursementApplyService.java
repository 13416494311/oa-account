package com.ruoyi.project.reimbursement.service;

import java.util.List;
import com.ruoyi.project.reimbursement.domain.OaReimbursementApply;

/**
 * 报销申请Service接口
 *
 * @author ruoyi
 * @date 2020-06-05
 */
public interface IOaReimbursementApplyService
{
    /**
     * 查询报销申请
     *
     * @param id 报销申请ID
     * @return 报销申请
     */
    public OaReimbursementApply selectOaReimbursementApplyById(Long id);

    public OaReimbursementApply selectOaReimbursementApplyByUuid(String uuid);
    /**
     * 查询报销申请列表
     *
     * @param oaReimbursementApply 报销申请
     * @return 报销申请集合
     */
    public List<OaReimbursementApply> selectOaReimbursementApplyList(OaReimbursementApply oaReimbursementApply);

    public List<OaReimbursementApply> remoteSearch(OaReimbursementApply oaReimbursementApply);

    /**
     * 新增报销申请
     *
     * @param oaReimbursementApply 报销申请
     * @return 结果
     */
    public OaReimbursementApply insertOaReimbursementApply(OaReimbursementApply oaReimbursementApply);

    /**
     * 修改报销申请
     *
     * @param oaReimbursementApply 报销申请
     * @return 结果
     */
    public int updateOaReimbursementApply(OaReimbursementApply oaReimbursementApply);

    /**
     * 批量删除报销申请
     *
     * @param ids 需要删除的报销申请ID
     * @return 结果
     */
    public int deleteOaReimbursementApplyByIds(Long[] ids);

    public void submitOaReimbursementApplyByIds(Long[] ids);

    /**
     * 删除报销申请信息
     *
     * @param id 报销申请ID
     * @return 结果
     */
    public int deleteOaReimbursementApplyById(Long id);
}
