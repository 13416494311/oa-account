package com.ruoyi.project.reimbursement.service;

import java.util.List;
import com.ruoyi.project.reimbursement.domain.OaPayeeExpert;

/**
 * 专家/劳务Service接口
 *
 * @author ruoyi
 * @date 2020-06-14
 */
public interface IOaPayeeExpertService
{
    /**
     * 查询专家/劳务
     *
     * @param id 专家/劳务ID
     * @return 专家/劳务
     */
    public OaPayeeExpert selectOaPayeeExpertById(Long id);

    /**
     * 查询专家/劳务列表
     *
     * @param oaPayeeExpert 专家/劳务
     * @return 专家/劳务集合
     */
    public List<OaPayeeExpert> selectOaPayeeExpertList(OaPayeeExpert oaPayeeExpert);

    /**
     * 新增专家/劳务
     *
     * @param oaPayeeExpert 专家/劳务
     * @return 结果
     */
    public int insertOaPayeeExpert(OaPayeeExpert oaPayeeExpert);

    /**
     * 修改专家/劳务
     *
     * @param oaPayeeExpert 专家/劳务
     * @return 结果
     */
    public int updateOaPayeeExpert(OaPayeeExpert oaPayeeExpert);

    /**
     * 批量删除专家/劳务
     *
     * @param ids 需要删除的专家/劳务ID
     * @return 结果
     */
    public int deleteOaPayeeExpertByIds(Long[] ids);

    /**
     * 删除专家/劳务信息
     *
     * @param id 专家/劳务ID
     * @return 结果
     */
    public int deleteOaPayeeExpertById(Long id);

    /**
     * 导入专家/劳务信息数据
     *
     * @param oaPayeeExpertList
     * @param isUpdateSupport
     * @param applyUuid
     * @return
     */
    public String importOaPayeeExpert(List<OaPayeeExpert> oaPayeeExpertList,
                                      Boolean isUpdateSupport, String applyUuid);
}
