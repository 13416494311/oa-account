package com.ruoyi.project.invoice.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.invoice.service.IOaTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 票据信息Controller
 *
 * @author ruoyi
 * @date 2020-06-17
 */
@RestController
@RequestMapping("/invoice/ticket")
public class OaTicketController extends BaseController {

    @Autowired
    private IOaTicketService oaTicketService;

    @GetMapping("/listNoPage/{uuid}")
    public AjaxResult listNoPage(@PathVariable("uuid") String uuid) {
        return AjaxResult.success(oaTicketService.selectOaTicketListByUuid(uuid));
    }


}
