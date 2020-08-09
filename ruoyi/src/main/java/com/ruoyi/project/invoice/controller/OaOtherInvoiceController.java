package com.ruoyi.project.invoice.controller;

import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.invoice.domain.*;
import com.ruoyi.project.invoice.service.*;
import com.ruoyi.project.reimbursement.domain.OaReimbursementApply;
import com.ruoyi.project.reimbursement.service.IOaReimbursementApplyService;
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
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 其他发票Controller
 *
 * @author ruoyi
 * @date 2020-07-04
 */
@RestController
@RequestMapping("/invoice/otherInvoice")
public class OaOtherInvoiceController extends BaseController
{
    @Autowired
    private IOaOtherInvoiceService oaOtherInvoiceService;
    @Autowired
    private IOaReimbursementApplyService oaReimbursementApplyService;
    @Autowired
    private IOaInvoiceService oaInvoiceService;
    @Autowired
    private IOaQuotaInvoiceService oaQuotaInvoiceService;
    @Autowired
    private IOaTrainTicketService oaTrainTicketService;
    @Autowired
    private IOaAirTicketService oaAirTicketService;

    /**
     * 查询其他发票列表
     */
    @PreAuthorize("@ss.hasPermi('invoice:otherInvoice:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaOtherInvoice oaOtherInvoice)
    {
        startPage();
        List<OaOtherInvoice> list = oaOtherInvoiceService.selectOaOtherInvoiceList(oaOtherInvoice);
        return getDataTable(list);
    }

    /**
     * 导出其他发票列表
     */
    @PreAuthorize("@ss.hasPermi('invoice:otherInvoice:export')")
    @Log(title = "其他发票", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OaOtherInvoice oaOtherInvoice)
    {
        List<OaOtherInvoice> list = oaOtherInvoiceService.selectOaOtherInvoiceList(oaOtherInvoice);
        ExcelUtil<OaOtherInvoice> util = new ExcelUtil<OaOtherInvoice>(OaOtherInvoice.class);
        return util.exportExcel(list, "otherInvoice");
    }

    /**
     * 获取其他发票详细信息
     */
    @PreAuthorize("@ss.hasPermi('invoice:otherInvoice:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(oaOtherInvoiceService.selectOaOtherInvoiceById(id));
    }

    /**
     * 新增其他发票
     */
    @PreAuthorize("@ss.hasPermi('invoice:otherInvoice:add')")
    @Log(title = "其他发票", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaOtherInvoice oaOtherInvoice)
    {
        return toAjax(oaOtherInvoiceService.insertOaOtherInvoice(oaOtherInvoice));
    }

    /**
     * 修改其他发票
     */
    @PreAuthorize("@ss.hasPermi('invoice:otherInvoice:edit')")
    @Log(title = "其他发票", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaOtherInvoice oaOtherInvoice)
    {
        return toAjax(oaOtherInvoiceService.updateOaOtherInvoice(oaOtherInvoice));
    }

    /**
     * 删除其他发票
     */
    @PreAuthorize("@ss.hasPermi('invoice:otherInvoice:remove')")
    @Log(title = "其他发票", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaOtherInvoiceService.deleteOaOtherInvoiceByIds(ids));
    }


    /**
     * 检查定额发票是否已存在
     */
    @PostMapping(value = "checkOtherInvoiceExist")
    public AjaxResult checkOtherInvoice(@RequestBody OaOtherInvoice oaOtherInvoice)
    {
        return AjaxResult.success(checkOtherInvoiceResult(oaOtherInvoice));
    }
    private String checkOtherInvoiceResult(OaOtherInvoice oaOtherInvoice){
        String result = "" ;

        Boolean reimbursement = false ;
        Boolean upload = false ;

        //校验其他发票中是否已上传、已报销
        OaOtherInvoice oaOtherInvoiceParam = new OaOtherInvoice();
        oaOtherInvoiceParam.setInvoiceNumber(oaOtherInvoice.getInvoiceNumber());
        List<OaOtherInvoice> oaOtherInvoiceList = oaOtherInvoiceService.
                selectOaOtherInvoiceList(oaOtherInvoiceParam);
        for(int i=0;i<oaOtherInvoiceList.size();i++){
            if(StringUtils.isNotNull(oaOtherInvoice.getId()) &&
                    oaOtherInvoice.getId() == oaOtherInvoiceList.get(i).getId()){
                continue;
            }
            OaReimbursementApply apply = oaReimbursementApplyService.
                    selectOaReimbursementApplyByUuid(oaOtherInvoiceList.get(i).getUuid());
            if(StringUtils.isNotNull(apply)){
                if("2".equals(apply.getAuditState())){  //已报销
                    reimbursement = true ;
                }else{           //已上传
                    upload = true ;
                }
            }
        }

        //校验增值税发票中是否已上传、已报销
        OaInvoice oaInvoiceParam = new OaInvoice();
        oaInvoiceParam.setInvoiceNum(oaOtherInvoice.getInvoiceNumber());
        oaInvoiceParam.setRepeatFlag("0");
        List<OaInvoice> oaInvoiceList = oaInvoiceService.selectOaInvoiceList(oaInvoiceParam);
        for(int i=0;i<oaInvoiceList.size();i++){
            OaReimbursementApply apply = oaReimbursementApplyService.
                    selectOaReimbursementApplyByUuid(oaInvoiceList.get(i).getUuid());
            if(StringUtils.isNotNull(apply)){
                if("2".equals(apply.getAuditState())){  //已报销
                    reimbursement = true ;
                }else{           //已上传
                    upload = true ;
                }
            }
        }

        //校验定额发票中是否已上传、已报销
        OaQuotaInvoice oaQuotaInvoiceParam = new OaQuotaInvoice();
        oaQuotaInvoiceParam.setInvoiceNumber(oaOtherInvoice.getInvoiceNumber());
        oaQuotaInvoiceParam.setRepeatFlag("0");
        List<OaQuotaInvoice> oaQuotaInvoiceList = oaQuotaInvoiceService.
                selectOaQuotaInvoiceList(oaQuotaInvoiceParam);
        for(int i=0;i<oaQuotaInvoiceList.size();i++){
            OaReimbursementApply apply = oaReimbursementApplyService.
                    selectOaReimbursementApplyByUuid(oaQuotaInvoiceList.get(i).getUuid());
            if(StringUtils.isNotNull(apply)){
                if("2".equals(apply.getAuditState())){  //已报销
                    reimbursement = true ;
                }else{           //已上传
                    upload = true ;
                }
            }
        }

        //校验火车票中是否已上传、已报销
        OaTrainTicket oaTrainTicketParam = new OaTrainTicket();
        oaTrainTicketParam.setTicketNum(oaOtherInvoice.getInvoiceNumber());
        oaTrainTicketParam.setRepeatFlag("0");
        List<OaTrainTicket> oaTrainTicketList = oaTrainTicketService.selectOaTrainTicketList(oaTrainTicketParam);
        for(int i=0;i<oaTrainTicketList.size();i++){
            OaReimbursementApply apply = oaReimbursementApplyService.
                    selectOaReimbursementApplyByUuid(oaTrainTicketList.get(i).getUuid());
            if(StringUtils.isNotNull(apply)){
                if("2".equals(apply.getAuditState())){  //已报销
                    reimbursement = true ;
                }else{           //已上传
                    upload = true ;
                }
            }
        }

        //校验机票中是否已上传、已报销
        OaAirTicket oaAirTicketParam = new OaAirTicket();
        oaAirTicketParam.setTicketNumber(oaOtherInvoice.getInvoiceNumber());
        oaAirTicketParam.setRepeatFlag("0");
        List<OaAirTicket> oaAirTicketList = oaAirTicketService.selectOaAirTicketList(oaAirTicketParam);
        for(int i=0;i<oaAirTicketList.size();i++){
            OaReimbursementApply apply = oaReimbursementApplyService.
                    selectOaReimbursementApplyByUuid(oaAirTicketList.get(i).getUuid());
            if(StringUtils.isNotNull(apply)){
                if("2".equals(apply.getAuditState())){  //已报销
                    reimbursement = true ;
                }else{           //已上传
                    upload = true ;
                }
            }
        }

        if(upload){
            result+= "该发票号码已上传;" ;
        }
        if(reimbursement){
            result+= "该发票号码已报销;" ;
        }
        return result;

    }
}
