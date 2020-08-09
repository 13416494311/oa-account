package com.ruoyi.project.invoice.controller;

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
import com.ruoyi.project.invoice.domain.OaInvoiceCommodity;
import com.ruoyi.project.invoice.service.IOaInvoiceCommodityService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 增值税发票商品Controller
 *
 * @author ruoyi
 * @date 2020-06-08
 */
@RestController
@RequestMapping("/invoice/commodity")
public class OaInvoiceCommodityController extends BaseController
{
    @Autowired
    private IOaInvoiceCommodityService oaInvoiceCommodityService;

    /**
     * 查询增值税发票商品列表
     */
    /*@PreAuthorize("@ss.hasPermi('invoice:commodity:list')")*/
    @GetMapping("/list")
    public TableDataInfo list(OaInvoiceCommodity oaInvoiceCommodity)
    {
        startPage();
        List<OaInvoiceCommodity> list = oaInvoiceCommodityService.selectOaInvoiceCommodityList(oaInvoiceCommodity);
        return getDataTable(list);
    }

    @GetMapping("/listNoPage")
    public AjaxResult listNoPage(OaInvoiceCommodity oaInvoiceCommodity)
    {
        List<OaInvoiceCommodity> list = oaInvoiceCommodityService.selectOaInvoiceCommodityList(oaInvoiceCommodity);
        return AjaxResult.success(list);
    }

    /**
     * 导出增值税发票商品列表
     */
    /*@PreAuthorize("@ss.hasPermi('invoice:commodity:export')")*/
    @Log(title = "增值税发票商品", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OaInvoiceCommodity oaInvoiceCommodity)
    {
        List<OaInvoiceCommodity> list = oaInvoiceCommodityService.selectOaInvoiceCommodityList(oaInvoiceCommodity);
        ExcelUtil<OaInvoiceCommodity> util = new ExcelUtil<OaInvoiceCommodity>(OaInvoiceCommodity.class);
        return util.exportExcel(list, "commodity");
    }

    /**
     * 获取增值税发票商品详细信息
     */
    /*@PreAuthorize("@ss.hasPermi('invoice:commodity:query')")*/
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(oaInvoiceCommodityService.selectOaInvoiceCommodityById(id));
    }

    /**
     * 新增增值税发票商品
     */
    /*@PreAuthorize("@ss.hasPermi('invoice:commodity:add')")*/
    @Log(title = "增值税发票商品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaInvoiceCommodity oaInvoiceCommodity)
    {
        return toAjax(oaInvoiceCommodityService.insertOaInvoiceCommodity(oaInvoiceCommodity));
    }

    /**
     * 修改增值税发票商品
     */
    /*@PreAuthorize("@ss.hasPermi('invoice:commodity:edit')")*/
    @Log(title = "增值税发票商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaInvoiceCommodity oaInvoiceCommodity)
    {
        return toAjax(oaInvoiceCommodityService.updateOaInvoiceCommodity(oaInvoiceCommodity));
    }

    /**
     * 删除增值税发票商品
     */
    /*@PreAuthorize("@ss.hasPermi('invoice:commodity:remove')")*/
    @Log(title = "增值税发票商品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaInvoiceCommodityService.deleteOaInvoiceCommodityByIds(ids));
    }
}
