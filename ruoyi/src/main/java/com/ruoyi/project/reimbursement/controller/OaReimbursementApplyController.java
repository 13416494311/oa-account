package com.ruoyi.project.reimbursement.controller;

import java.util.List;

import com.ruoyi.framework.aspectj.lang.annotation.DataScope;
import com.ruoyi.project.system.service.ISysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.reimbursement.domain.OaReimbursementApply;
import com.ruoyi.project.reimbursement.service.IOaReimbursementApplyService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 报销申请Controller
 *
 * @author ruoyi
 * @date 2020-06-05
 */
@RestController
@RequestMapping("/reimbursement/apply")
public class OaReimbursementApplyController extends BaseController
{
    @Autowired
    private IOaReimbursementApplyService oaReimbursementApplyService;


    /**
     * 查询报销申请列表
     */
    @GetMapping("/list")
    @DataScope(userAlias = "sysUser")
    public TableDataInfo list(OaReimbursementApply oaReimbursementApply)
    {
        startPage();
        List<OaReimbursementApply> list = oaReimbursementApplyService.selectOaReimbursementApplyList(oaReimbursementApply);
        return getDataTable(list);
    }

    /**
     * 查询报销申请列表
     */
    @GetMapping("/listNoPage")
    public AjaxResult listNoPage(OaReimbursementApply oaReimbursementApply)
    {
        List<OaReimbursementApply> list = oaReimbursementApplyService.selectOaReimbursementApplyList(oaReimbursementApply);
        return AjaxResult.success(list);
    }



    /**
     * 查询报销申请列表- 远程搜索
     */
    @GetMapping("/remoteSearch")
    public AjaxResult remoteSearch(OaReimbursementApply oaReimbursementApply)
    {
        List<OaReimbursementApply> list = oaReimbursementApplyService.remoteSearch(oaReimbursementApply);
        return AjaxResult.success(list);
    }

    /**
     * 导出报销申请列表
     */
    @Log(title = "报销申请", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OaReimbursementApply oaReimbursementApply)
    {
        List<OaReimbursementApply> list = oaReimbursementApplyService.selectOaReimbursementApplyList(oaReimbursementApply);
        ExcelUtil<OaReimbursementApply> util = new ExcelUtil<OaReimbursementApply>(OaReimbursementApply.class);
        return util.exportExcel(list, "apply");
    }

    /**
     * 获取报销申请详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(oaReimbursementApplyService.selectOaReimbursementApplyById(id));
    }

    /**
     * 新增报销申请
     */
    @Log(title = "报销申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaReimbursementApply oaReimbursementApply)
    {
        oaReimbursementApplyService.insertOaReimbursementApply(oaReimbursementApply);
        return AjaxResult.success(oaReimbursementApply) ;
    }

    /**
     * 修改报销申请
     */
    @Log(title = "报销申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaReimbursementApply oaReimbursementApply)
    {
        return toAjax(oaReimbursementApplyService.updateOaReimbursementApply(oaReimbursementApply));
    }

    /**
     * 删除报销申请
     */
    @Log(title = "报销申请", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaReimbursementApplyService.deleteOaReimbursementApplyByIds(ids));
    }

    /**
     * 批量提交报销申请
     */
    @Log(title = "报销申请", businessType = BusinessType.UPDATE)
    @PutMapping("/submit/{ids}")
    public AjaxResult submit(@PathVariable Long[] ids)
    {
        oaReimbursementApplyService.submitOaReimbursementApplyByIds(ids);
        return AjaxResult.success();
    }
}
