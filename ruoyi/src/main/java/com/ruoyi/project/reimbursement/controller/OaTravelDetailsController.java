package com.ruoyi.project.reimbursement.controller;

import java.util.List;
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
import com.ruoyi.project.reimbursement.domain.OaTravelDetails;
import com.ruoyi.project.reimbursement.service.IOaTravelDetailsService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 差旅报销明细Controller
 *
 * @author ruoyi
 * @date 2020-06-07
 */
@RestController
@RequestMapping("/reimbursement/travelDetails")
public class OaTravelDetailsController extends BaseController
{
    @Autowired
    private IOaTravelDetailsService oaTravelDetailsService;

    /**
     * 查询差旅报销明细列表
     */
    /*@PreAuthorize("@ss.hasPermi('reimbursement:travelDetails:list')")*/
    @GetMapping("/list")
    public AjaxResult list(OaTravelDetails oaTravelDetails)
    {
        List<OaTravelDetails> list = oaTravelDetailsService.selectOaTravelDetailsList(oaTravelDetails);
        return AjaxResult.success(list);
    }

    /**
     * 导出差旅报销明细列表
     */
    /*@PreAuthorize("@ss.hasPermi('reimbursement:travelDetails:export')")*/
    @Log(title = "差旅报销明细", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OaTravelDetails oaTravelDetails)
    {
        List<OaTravelDetails> list = oaTravelDetailsService.selectOaTravelDetailsList(oaTravelDetails);
        ExcelUtil<OaTravelDetails> util = new ExcelUtil<OaTravelDetails>(OaTravelDetails.class);
        return util.exportExcel(list, "travelDetails");
    }

    /**
     * 获取差旅报销明细详细信息
     */
    /*@PreAuthorize("@ss.hasPermi('reimbursement:travelDetails:query')")*/
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(oaTravelDetailsService.selectOaTravelDetailsById(id));
    }

    /**
     * 新增差旅报销明细
     */
   /* @PreAuthorize("@ss.hasPermi('reimbursement:travelDetails:add')")*/
    @Log(title = "差旅报销明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaTravelDetails oaTravelDetails)
    {
        return toAjax(oaTravelDetailsService.insertOaTravelDetails(oaTravelDetails));
    }

    /**
     * 修改差旅报销明细
     */
    /*@PreAuthorize("@ss.hasPermi('reimbursement:travelDetails:edit')")*/
    @Log(title = "差旅报销明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaTravelDetails oaTravelDetails)
    {
        return toAjax(oaTravelDetailsService.updateOaTravelDetails(oaTravelDetails));
    }

    /**
     * 删除差旅报销明细
     */
    /*@PreAuthorize("@ss.hasPermi('reimbursement:travelDetails:remove')")*/
    @Log(title = "差旅报销明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaTravelDetailsService.deleteOaTravelDetailsByIds(ids));
    }
}
