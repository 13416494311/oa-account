package com.ruoyi.project.reimbursement.service.impl;

import java.util.List;
import java.util.UUID;

import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.system.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.reimbursement.mapper.OaPayeeExpertMapper;
import com.ruoyi.project.reimbursement.domain.OaPayeeExpert;
import com.ruoyi.project.reimbursement.service.IOaPayeeExpertService;

/**
 * 专家/劳务Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-14
 */
@Slf4j
@Service
public class OaPayeeExpertServiceImpl implements IOaPayeeExpertService
{
    @Autowired
    private OaPayeeExpertMapper oaPayeeExpertMapper;

    /**
     * 查询专家/劳务
     *
     * @param id 专家/劳务ID
     * @return 专家/劳务
     */
    @Override
    public OaPayeeExpert selectOaPayeeExpertById(Long id)
    {
        return oaPayeeExpertMapper.selectOaPayeeExpertById(id);
    }

    /**
     * 查询专家/劳务列表
     *
     * @param oaPayeeExpert 专家/劳务
     * @return 专家/劳务
     */
    @Override
    public List<OaPayeeExpert> selectOaPayeeExpertList(OaPayeeExpert oaPayeeExpert)
    {
        return oaPayeeExpertMapper.selectOaPayeeExpertList(oaPayeeExpert);
    }

    /**
     * 新增专家/劳务
     *
     * @param oaPayeeExpert 专家/劳务
     * @return 结果
     */
    @Override
    public int insertOaPayeeExpert(OaPayeeExpert oaPayeeExpert)
    {
        oaPayeeExpert.setCreateTime(DateUtils.getNowDate());
        return oaPayeeExpertMapper.insertOaPayeeExpert(oaPayeeExpert);
    }

    /**
     * 修改专家/劳务
     *
     * @param oaPayeeExpert 专家/劳务
     * @return 结果
     */
    @Override
    public int updateOaPayeeExpert(OaPayeeExpert oaPayeeExpert)
    {
        return oaPayeeExpertMapper.updateOaPayeeExpert(oaPayeeExpert);
    }

    /**
     * 批量删除专家/劳务
     *
     * @param ids 需要删除的专家/劳务ID
     * @return 结果
     */
    @Override
    public int deleteOaPayeeExpertByIds(Long[] ids)
    {
        return oaPayeeExpertMapper.deleteOaPayeeExpertByIds(ids);
    }

    /**
     * 删除专家/劳务信息
     *
     * @param id 专家/劳务ID
     * @return 结果
     */
    @Override
    public int deleteOaPayeeExpertById(Long id)
    {
        return oaPayeeExpertMapper.deleteOaPayeeExpertById(id);
    }

    /**
     * 导入专家/劳务信息数据
     *
     * @param oaPayeeExpertList 专家/劳务信息数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param applyUuid 报销申请唯一id
     * @return 结果
     */
    @Override
    public String importOaPayeeExpert(List<OaPayeeExpert> oaPayeeExpertList,
                                      Boolean isUpdateSupport, String applyUuid)
    {
        if (StringUtils.isNull(oaPayeeExpertList) || oaPayeeExpertList.size() == 0)
        {
            throw new CustomException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (OaPayeeExpert oaPayeeExpert : oaPayeeExpertList)
        {
            try
            {
                // 验证是否存在这个专家/劳务
                oaPayeeExpert.setApplyUuid(applyUuid);
                oaPayeeExpert.setUuid(UUID.randomUUID().toString());
                OaPayeeExpert e = null;
                if(StringUtils.isNotNull(oaPayeeExpert.getCardNo())){
                    e = oaPayeeExpertMapper.selectOaPayeeExpertByCardNo(applyUuid,oaPayeeExpert.getCardNo());
                }
                if (StringUtils.isNull(e))
                {
                    this.insertOaPayeeExpert(oaPayeeExpert);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、姓名 " + oaPayeeExpert.getName() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    oaPayeeExpert.setUuid(null);
                    this.updateOaPayeeExpert(oaPayeeExpert);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、姓名 " + oaPayeeExpert.getName() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、姓名 " +oaPayeeExpert.getName() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + oaPayeeExpert.getName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
