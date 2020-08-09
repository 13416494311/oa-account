package com.ruoyi.project.reimbursement.controller;

import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.reimbursement.domain.OaReimbursementApply;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.ruoyi.project.reimbursement.domain.OaPayee;
import com.ruoyi.project.reimbursement.service.IOaPayeeService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 付款信息Controller
 *
 * @author ruoyi
 * @date 2020-06-13
 */
@RestController
@RequestMapping("/reimbursement/payee")
public class OaPayeeController extends BaseController
{
    @Autowired
    private IOaPayeeService oaPayeeService;

    /**
     * 查询付款信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(OaPayee oaPayee)
    {
        startPage();
        List<OaPayee> list = oaPayeeService.selectOaPayeeList(oaPayee);
        return getDataTable(list);
    }

    /**
     * 导出付款信息列表
     */
    @Log(title = "付款信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OaPayee oaPayee)
    {
        List<OaPayee> list = oaPayeeService.selectOaPayeeList(oaPayee);
        ExcelUtil<OaPayee> util = new ExcelUtil<OaPayee>(OaPayee.class);
        return util.exportExcel(list, "payee");
    }

    /**
     * 获取付款信息详细信息
     */
    @GetMapping(value = "/applyUuid/{applyUuid}")
    public AjaxResult getInfo(@PathVariable("applyUuid") String applyUuid)
    {
        return AjaxResult.success(oaPayeeService.selectOaPayeeByApplyUuid(applyUuid));
    }

    /**
     * 获取付款信息详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(oaPayeeService.selectOaPayeeById(id));
    }

    /**
     * 根据报销人查询最近一次报销 付款信息
     */
    @GetMapping(value = "/getPayeeListByParam")
    public AjaxResult getPayeeListByParam(OaPayee oaPayee)
    {
        List<OaPayee> list =null;
        if(StringUtils.isNotEmpty(oaPayee.getPayee())){
            list = oaPayeeService.selectOaPayeeList(oaPayee);
        }
        return AjaxResult.success(list);
    }

    /**
     * 查询报销申请列表- 远程搜索
     */
    @GetMapping("/remoteSearch")
    public AjaxResult remoteSearch(OaPayee oaPayee)
    {
        List<OaPayee> list = oaPayeeService.remoteSearch(oaPayee);
        return AjaxResult.success(list);
    }

    /**
     * 新增付款信息
     */
    @Log(title = "付款信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaPayee oaPayee)
    {
        return toAjax(oaPayeeService.insertOaPayee(oaPayee));
    }

    /**
     * 修改付款信息
     */
    @Log(title = "付款信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaPayee oaPayee)
    {
        return toAjax(oaPayeeService.updateOaPayee(oaPayee));
    }

    /**
     * 删除付款信息
     */
    @Log(title = "付款信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaPayeeService.deleteOaPayeeByIds(ids));
    }
}
