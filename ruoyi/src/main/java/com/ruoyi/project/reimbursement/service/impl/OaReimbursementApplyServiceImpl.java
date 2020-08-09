package com.ruoyi.project.reimbursement.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.project.invoice.service.IOaInvoiceService;
import com.ruoyi.project.reimbursement.service.IOaTravelDetailsService;
import com.ruoyi.project.reimbursement.service.IOaTravelService;
import com.ruoyi.project.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.reimbursement.mapper.OaReimbursementApplyMapper;
import com.ruoyi.project.reimbursement.domain.OaReimbursementApply;
import com.ruoyi.project.reimbursement.service.IOaReimbursementApplyService;

/**
 * 报销申请Service业务层处理
 *
 * @author ruoyi
 * @date 2020-06-05
 */
@Service
public class OaReimbursementApplyServiceImpl implements IOaReimbursementApplyService
{
    @Autowired
    private OaReimbursementApplyMapper oaReimbursementApplyMapper;

    @Autowired
    private IOaTravelService oaTravelService;

    @Autowired
    private IOaTravelDetailsService oaTravelDetailsService;

    @Autowired
    private IOaInvoiceService oaInvoiceService;



    /**
     * 查询报销申请
     *
     * @param id 报销申请ID
     * @return 报销申请
     */
    @Override
    public OaReimbursementApply selectOaReimbursementApplyById(Long id)
    {
        return oaReimbursementApplyMapper.selectOaReimbursementApplyById(id);
    }

    @Override
    public OaReimbursementApply selectOaReimbursementApplyByUuid(String uuid)
    {
        return oaReimbursementApplyMapper.selectOaReimbursementApplyByUuid(uuid);
    }

    /**
     * 查询报销申请列表
     *
     * @param oaReimbursementApply 报销申请
     * @return 报销申请
     */
    @Override
    public List<OaReimbursementApply> selectOaReimbursementApplyList(OaReimbursementApply oaReimbursementApply)
    {
        return oaReimbursementApplyMapper.selectOaReimbursementApplyList(oaReimbursementApply);
    }

    @Override
    public List<OaReimbursementApply> remoteSearch(OaReimbursementApply oaReimbursementApply)
    {
        List<OaReimbursementApply> list = null;
        if(StringUtils.isNotEmpty(oaReimbursementApply.getReimburser())||
                StringUtils.isNotEmpty(oaReimbursementApply.getDeptName())||
                StringUtils.isNotEmpty(oaReimbursementApply.getTopic())||
                StringUtils.isNotEmpty(oaReimbursementApply.getOperator())){
            list = oaReimbursementApplyMapper.remoteSearch(oaReimbursementApply);

        }
        return list;
    }

    /**
     * 新增报销申请
     *
     * @param oaReimbursementApply 报销申请
     * @return 结果
     */
    @Override
    public OaReimbursementApply insertOaReimbursementApply(OaReimbursementApply oaReimbursementApply)
    {

        LoginUser loginUser = SpringUtils.getBean(TokenService.class).getLoginUser(ServletUtils.getRequest());
        SysUser currentUser = loginUser.getUser();
        oaReimbursementApply.setCreateUserId(currentUser.getUserId());
        oaReimbursementApply.setCreateDeptId(currentUser.getDeptId());
        oaReimbursementApply.setCreateTime(DateUtils.getNowDate());
        oaReimbursementApplyMapper.insertOaReimbursementApply(oaReimbursementApply);
        return oaReimbursementApply;
    }

    /**
     * 修改报销申请
     *
     * @param oaReimbursementApply 报销申请
     * @return 结果
     */
    @Override
    public int updateOaReimbursementApply(OaReimbursementApply oaReimbursementApply)
    {
        return oaReimbursementApplyMapper.updateOaReimbursementApply(oaReimbursementApply);
    }

    /**
     * 批量删除报销申请
     *
     * @param ids 需要删除的报销申请ID
     * @return 结果
     */
    @Override
    public int deleteOaReimbursementApplyByIds(Long[] ids)
    {

        /*oaTravelService.deleteOaTravelByApplyUuids(ids);
        oaTravelDetailsService.deleteOaTravelDetailsByApplyIds(ids);
        for(Long id: ids ){
            OaReimbursementApply apply = oaReimbursementApplyMapper.selectOaReimbursementApplyById(id);
            oaInvoiceService.deleteOaInvoiceByUuid(apply.getUuid());
        }*/

        return oaReimbursementApplyMapper.deleteOaReimbursementApplyByIds(ids);
    }

    @Override
    public void submitOaReimbursementApplyByIds(Long[] ids)
    {

        for(Long id:ids){
            OaReimbursementApply apply = new OaReimbursementApply();
            apply.setId(id);
            apply.setAuditState("1");
            oaReimbursementApplyMapper.updateOaReimbursementApply(apply);
        }
    }

    /**
     * 删除报销申请信息
     *
     * @param id 报销申请ID
     * @return 结果
     */
    @Override
    public int deleteOaReimbursementApplyById(Long id)
    {
        return oaReimbursementApplyMapper.deleteOaReimbursementApplyById(id);
    }
}
