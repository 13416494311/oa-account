package com.ruoyi.project.reimbursement.controller;

import java.util.List;

import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.project.system.domain.SysUser;
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
import com.ruoyi.project.reimbursement.domain.OaPayeeExpert;
import com.ruoyi.project.reimbursement.service.IOaPayeeExpertService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 专家/劳务Controller
 *
 * @author ruoyi
 * @date 2020-06-14
 */
@RestController
@RequestMapping("/reimbursement/payeeExpert")
public class OaPayeeExpertController extends BaseController
{
    @Autowired
    private IOaPayeeExpertService oaPayeeExpertService;

    /**
     * 查询专家/劳务列表
     */
    @GetMapping("/list")
    public TableDataInfo list(OaPayeeExpert oaPayeeExpert)
    {
        startPage();
        List<OaPayeeExpert> list = oaPayeeExpertService.selectOaPayeeExpertList(oaPayeeExpert);
        return getDataTable(list);
    }

    @GetMapping("/listNoPage")
    public AjaxResult listNoPage(OaPayeeExpert oaPayeeExpert)
    {
        List<OaPayeeExpert> list = oaPayeeExpertService.selectOaPayeeExpertList(oaPayeeExpert);
        return AjaxResult.success(list);
    }

    /**
     * 导出专家/劳务列表
     */
    @Log(title = "专家/劳务", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OaPayeeExpert oaPayeeExpert)
    {
        List<OaPayeeExpert> list = oaPayeeExpertService.selectOaPayeeExpertList(oaPayeeExpert);
        ExcelUtil<OaPayeeExpert> util = new ExcelUtil<OaPayeeExpert>(OaPayeeExpert.class);
        return util.exportExcel(list, "payeeExpert");
    }

    /**
     * 获取专家/劳务详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(oaPayeeExpertService.selectOaPayeeExpertById(id));
    }

    /**
     * 新增专家/劳务
     */
    @Log(title = "专家/劳务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaPayeeExpert oaPayeeExpert)
    {
        return toAjax(oaPayeeExpertService.insertOaPayeeExpert(oaPayeeExpert));
    }

    /**
     * 修改专家/劳务
     */
    @Log(title = "专家/劳务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaPayeeExpert oaPayeeExpert)
    {
        return toAjax(oaPayeeExpertService.updateOaPayeeExpert(oaPayeeExpert));
    }

    /**
     * 删除专家/劳务
     */
    @Log(title = "专家/劳务", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaPayeeExpertService.deleteOaPayeeExpertByIds(ids));
    }

    //下载模板
    @Log(title = "专家/劳务", businessType = BusinessType.EXPORT)
    @GetMapping("/importTemplate")
    public AjaxResult importTemplate()
    {
        ExcelUtil<OaPayeeExpert> util = new ExcelUtil<OaPayeeExpert>(OaPayeeExpert.class);
        return util.importTemplateExcel("专家劳务信息导入模板");
    }

    @Log(title = "专家/劳务", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport,String applyUuid) throws Exception
    {
        ExcelUtil<OaPayeeExpert> util = new ExcelUtil<OaPayeeExpert>(OaPayeeExpert.class);
        List<OaPayeeExpert> oaPayeeExpertList = util.importExcel(file.getInputStream());
        String message = oaPayeeExpertService.importOaPayeeExpert(oaPayeeExpertList, updateSupport, applyUuid);
        return AjaxResult.success(message);
    }
}
