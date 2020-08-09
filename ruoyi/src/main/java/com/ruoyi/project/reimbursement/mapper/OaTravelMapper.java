package com.ruoyi.project.reimbursement.mapper;

import java.util.List;
import com.ruoyi.project.reimbursement.domain.OaTravel;

/**
 * 差旅报销Mapper接口
 *
 * @author ruoyi
 * @date 2020-06-07
 */
public interface OaTravelMapper
{
    /**
     * 查询差旅报销
     *
     * @param id 差旅报销ID
     * @return 差旅报销
     */
    public OaTravel selectOaTravelById(Long id);

    public OaTravel selectOaTravelByApplyUuid(String applyUuid);

    /**
     * 查询差旅报销列表
     *
     * @param oaTravel 差旅报销
     * @return 差旅报销集合
     */
    public List<OaTravel> selectOaTravelList(OaTravel oaTravel);

    /**
     * 新增差旅报销
     *
     * @param oaTravel 差旅报销
     * @return 结果
     */
    public int insertOaTravel(OaTravel oaTravel);

    /**
     * 修改差旅报销
     *
     * @param oaTravel 差旅报销
     * @return 结果
     */
    public int updateOaTravel(OaTravel oaTravel);

    /**
     * 删除差旅报销
     *
     * @param id 差旅报销ID
     * @return 结果
     */
    public int deleteOaTravelById(Long id);

    public int deleteOaTravelByApplyUuid(String applyUuid);

    /**
     * 批量删除差旅报销
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOaTravelByIds(Long[] ids);

    public int deleteOaTravelByApplyUuids(String[] applyUuids);
}
