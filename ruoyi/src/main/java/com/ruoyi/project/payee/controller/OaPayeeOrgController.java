package com.ruoyi.project.payee.controller;

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
import com.ruoyi.project.payee.domain.OaPayeeOrg;
import com.ruoyi.project.payee.service.IOaPayeeOrgService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 收款方（企业）信息Controller
 *
 * @author ruoyi
 * @date 2020-06-03
 */
@RestController
@RequestMapping("/payee/org")
public class OaPayeeOrgController extends BaseController
{
    @Autowired
    private IOaPayeeOrgService oaPayeeOrgService;

    /**
     * 查询收款方（企业）信息列表
     */
    @PreAuthorize("@ss.hasPermi('payee:org:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaPayeeOrg oaPayeeOrg)
    {
        startPage();
        List<OaPayeeOrg> list = oaPayeeOrgService.selectOaPayeeOrgList(oaPayeeOrg);
        return getDataTable(list);
    }

    /**
     * 导出收款方（企业）信息列表
     */
    @PreAuthorize("@ss.hasPermi('payee:org:export')")
    @Log(title = "收款方（企业）信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OaPayeeOrg oaPayeeOrg)
    {
        List<OaPayeeOrg> list = oaPayeeOrgService.selectOaPayeeOrgList(oaPayeeOrg);
        ExcelUtil<OaPayeeOrg> util = new ExcelUtil<OaPayeeOrg>(OaPayeeOrg.class);
        return util.exportExcel(list, "org");
    }

    /**
     * 获取收款方（企业）信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('payee:org:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(oaPayeeOrgService.selectOaPayeeOrgById(id));
    }

    /**
     * 新增收款方（企业）信息
     */
    @PreAuthorize("@ss.hasPermi('payee:org:add')")
    @Log(title = "收款方（企业）信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaPayeeOrg oaPayeeOrg)
    {
        oaPayeeOrg.setBankAccount(oaPayeeOrg.getBankAccount().replace(" ",""));
        return toAjax(oaPayeeOrgService.insertOaPayeeOrg(oaPayeeOrg));
    }

    /**
     * 修改收款方（企业）信息
     */
    @PreAuthorize("@ss.hasPermi('payee:org:edit')")
    @Log(title = "收款方（企业）信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaPayeeOrg oaPayeeOrg)
    {
        oaPayeeOrg.setBankAccount(oaPayeeOrg.getBankAccount().replace(" ",""));
        return toAjax(oaPayeeOrgService.updateOaPayeeOrg(oaPayeeOrg));
    }

    /**
     * 删除收款方（企业）信息
     */
    @PreAuthorize("@ss.hasPermi('payee:org:remove')")
    @Log(title = "收款方（企业）信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaPayeeOrgService.deleteOaPayeeOrgByIds(ids));
    }
}
