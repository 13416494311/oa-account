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
import com.ruoyi.project.payee.domain.OaPayeeUser;
import com.ruoyi.project.payee.service.IOaPayeeUserService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 收款方（个人）信息Controller
 *
 * @author ruoyi
 * @date 2020-06-03
 */
@RestController
@RequestMapping("/payee/user")
public class OaPayeeUserController extends BaseController
{
    @Autowired
    private IOaPayeeUserService oaPayeeUserService;

    /**
     * 查询收款方（个人）信息列表
     */
    @PreAuthorize("@ss.hasPermi('payee:user:list')")
    @GetMapping("/list")

    public TableDataInfo list(OaPayeeUser oaPayeeUser)
    {
        startPage();
        List<OaPayeeUser> list = oaPayeeUserService.selectOaPayeeUserList(oaPayeeUser);
        return getDataTable(list);
    }

    /**
     * 导出收款方（个人）信息列表
     */
    @PreAuthorize("@ss.hasPermi('payee:user:export')")
    @Log(title = "收款方（个人）信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OaPayeeUser oaPayeeUser)
    {
        List<OaPayeeUser> list = oaPayeeUserService.selectOaPayeeUserList(oaPayeeUser);
        ExcelUtil<OaPayeeUser> util = new ExcelUtil<OaPayeeUser>(OaPayeeUser.class);
        return util.exportExcel(list, "user");
    }

    /**
     * 获取收款方（个人）信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('payee:user:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(oaPayeeUserService.selectOaPayeeUserById(id));
    }

    /**
     * 新增收款方（个人）信息
     */
    @PreAuthorize("@ss.hasPermi('payee:user:add')")
    @Log(title = "收款方（个人）信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaPayeeUser oaPayeeUser)
    {
        return toAjax(oaPayeeUserService.insertOaPayeeUser(oaPayeeUser));
    }

    /**
     * 修改收款方（个人）信息
     */
    @PreAuthorize("@ss.hasPermi('payee:user:edit')")
    @Log(title = "收款方（个人）信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaPayeeUser oaPayeeUser)
    {
        return toAjax(oaPayeeUserService.updateOaPayeeUser(oaPayeeUser));
    }

    /**
     * 删除收款方（个人）信息
     */
    @PreAuthorize("@ss.hasPermi('payee:user:remove')")
    @Log(title = "收款方（个人）信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaPayeeUserService.deleteOaPayeeUserByIds(ids));
    }
}
