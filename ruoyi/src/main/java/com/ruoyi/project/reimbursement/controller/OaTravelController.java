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
import com.ruoyi.project.reimbursement.domain.OaTravel;
import com.ruoyi.project.reimbursement.service.IOaTravelService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 差旅报销Controller
 *
 * @author ruoyi
 * @date 2020-06-07
 */
@RestController
@RequestMapping("/reimbursement/travel")
public class OaTravelController extends BaseController
{
    @Autowired
    private IOaTravelService oaTravelService;

    /**
     * 查询差旅报销列表
     */
    /*@PreAuthorize("@ss.hasPermi('reimbursement:travel:list')")*/
    @GetMapping("/list")
    public TableDataInfo list(OaTravel oaTravel)
    {
        startPage();
        List<OaTravel> list = oaTravelService.selectOaTravelList(oaTravel);
        return getDataTable(list);
    }

    /**
     * 导出差旅报销列表
     */
    /*@PreAuthorize("@ss.hasPermi('reimbursement:travel:export')")*/
    @Log(title = "差旅报销", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OaTravel oaTravel)
    {
        List<OaTravel> list = oaTravelService.selectOaTravelList(oaTravel);
        ExcelUtil<OaTravel> util = new ExcelUtil<OaTravel>(OaTravel.class);
        return util.exportExcel(list, "travel");
    }

    /**
     * 获取差旅报销详细信息
     */
   /* @PreAuthorize("@ss.hasPermi('reimbursement:travel:query')")*/
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(oaTravelService.selectOaTravelById(id));
    }

    @GetMapping(value = "/applyUuid/{applyUuid}")
    public AjaxResult getInfo(@PathVariable("applyUuid") String applyUuid)
    {
        return AjaxResult.success(oaTravelService.selectOaTravelByApplyUuid(applyUuid));
    }

    /**
     * 新增差旅报销
     */
    /*@PreAuthorize("@ss.hasPermi('reimbursement:travel:add')")*/
    @Log(title = "差旅报销", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaTravel oaTravel)
    {
        return toAjax(oaTravelService.insertOaTravel(oaTravel));
    }

    /**
     * 修改差旅报销
     */
    /*@PreAuthorize("@ss.hasPermi('reimbursement:travel:edit')")*/
    @Log(title = "差旅报销", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaTravel oaTravel)
    {
        return toAjax(oaTravelService.updateOaTravel(oaTravel));
    }

    /**
     * 删除差旅报销
     */
    /*@PreAuthorize("@ss.hasPermi('reimbursement:travel:remove')")*/
    @Log(title = "差旅报销", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaTravelService.deleteOaTravelByIds(ids));
    }
}
