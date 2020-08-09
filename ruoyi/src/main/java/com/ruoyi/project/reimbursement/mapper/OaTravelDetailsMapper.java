package com.ruoyi.project.reimbursement.mapper;

import java.util.List;
import com.ruoyi.project.reimbursement.domain.OaTravelDetails;

/**
 * 差旅报销明细Mapper接口
 *
 * @author ruoyi
 * @date 2020-06-07
 */
public interface OaTravelDetailsMapper
{
    /**
     * 查询差旅报销明细
     *
     * @param id 差旅报销明细ID
     * @return 差旅报销明细
     */
    public OaTravelDetails selectOaTravelDetailsById(Long id);

    /**
     * 查询差旅报销明细列表
     *
     * @param oaTravelDetails 差旅报销明细
     * @return 差旅报销明细集合
     */
    public List<OaTravelDetails> selectOaTravelDetailsList(OaTravelDetails oaTravelDetails);

    /**
     * 新增差旅报销明细
     *
     * @param oaTravelDetails 差旅报销明细
     * @return 结果
     */
    public int insertOaTravelDetails(OaTravelDetails oaTravelDetails);

    /**
     * 修改差旅报销明细
     *
     * @param oaTravelDetails 差旅报销明细
     * @return 结果
     */
    public int updateOaTravelDetails(OaTravelDetails oaTravelDetails);

    /**
     * 删除差旅报销明细
     *
     * @param id 差旅报销明细ID
     * @return 结果
     */
    public int deleteOaTravelDetailsById(Long id);

    /**
     * 批量删除差旅报销明细
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteOaTravelDetailsByIds(Long[] ids);

    public int deleteOaTravelDetailsByApplyUuids(String[] applyUuids);
}
