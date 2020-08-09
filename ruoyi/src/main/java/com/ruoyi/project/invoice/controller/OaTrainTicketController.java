package com.ruoyi.project.invoice.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.*;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.project.invoice.domain.OaOtherInvoice;
import com.ruoyi.project.invoice.domain.OaQuotaInvoice;
import com.ruoyi.project.invoice.service.IOaOtherInvoiceService;
import com.ruoyi.project.reimbursement.domain.OaReimbursementApply;
import com.ruoyi.project.reimbursement.service.IOaReimbursementApplyService;
import com.ruoyi.project.system.domain.SysAttachment;
import com.ruoyi.project.system.service.ISysAttachmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.invoice.domain.OaTrainTicket;
import com.ruoyi.project.invoice.service.IOaTrainTicketService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 火车票Controller
 *
 * @author ruoyi
 * @date 2020-06-21
 */
@RestController
@RequestMapping("/invoice/trainTicket")
public class OaTrainTicketController extends BaseController
{
    @Autowired
    private IOaTrainTicketService oaTrainTicketService;
    @Autowired
    private ISysAttachmentService sysAttachmentService;
    @Autowired
    private IOaReimbursementApplyService oaReimbursementApplyService;
    @Autowired
    private IOaOtherInvoiceService oaOtherInvoiceService;

    /**
     * 查询火车票列表
     */
    @GetMapping("/list")
    public TableDataInfo list(OaTrainTicket oaTrainTicket)
    {
        startPage();
        List<OaTrainTicket> list = oaTrainTicketService.selectOaTrainTicketList(oaTrainTicket);
        return getDataTable(list);
    }

    /**
     * 导出火车票列表
     */
    @Log(title = "火车票", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OaTrainTicket oaTrainTicket)
    {
        List<OaTrainTicket> list = oaTrainTicketService.selectOaTrainTicketList(oaTrainTicket);
        ExcelUtil<OaTrainTicket> util = new ExcelUtil<OaTrainTicket>(OaTrainTicket.class);
        return util.exportExcel(list, "trainTicket");
    }

    /**
     * 获取火车票详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(oaTrainTicketService.selectOaTrainTicketById(id));
    }

    /**
     * 新增火车票
     */
    @Log(title = "火车票", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaTrainTicket oaTrainTicket)
    {
        return toAjax(oaTrainTicketService.insertOaTrainTicket(oaTrainTicket));
    }

    /**
     * 修改火车票
     */
    @Log(title = "火车票", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaTrainTicket oaTrainTicket)
    {
        return toAjax(oaTrainTicketService.updateOaTrainTicket(oaTrainTicket));
    }

    /**
     * 删除火车票
     */
    @Log(title = "火车票", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaTrainTicketService.deleteOaTrainTicketByIds(ids));
    }


    /**
     * 扫描方式-新增火车票
     */
    @Log(title = "新增火车票-扫描附件方式", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addByAtt")
    public AjaxResult addByAtt(@RequestParam("file") MultipartFile file,
                               @RequestParam("uuid") String uuid,
                               SysAttachment sysAttachment) throws IOException {
        String ticketUuid = UUID.randomUUID().toString();

        File trainTicketFile = FileUtil.multipartFileToFile(file);
        String fileName = trainTicketFile.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if("pdf".equals(suffix.toLowerCase())){
            List<File> pngFileList = Pdf2PngUtils.pdf2png(trainTicketFile,0,1);
            if(StringUtils.isNotEmpty(pngFileList)){
                trainTicketFile=pngFileList.get(0);
            }
        }
        String filePath = FileUploadUtils.upload(RuoYiConfig.getProfile()+
                        "/"+ StringUtils.lineToHump(sysAttachment.getFileDictType())+"/"+sysAttachment.getFileDictValue(),
                FileUtil.fileToMultipartFile(trainTicketFile));

        String result = BaiduApi.vatInvoice(trainTicketFile,sysAttachment.getFileDictValue());
        Map<String,Object> resultMap = JSON.parseObject(result);
        Map<String,Object> oaTrainTicketMap = (Map<String, Object>) resultMap.get("words_result");
        if(oaTrainTicketMap!=null){
            oaTrainTicketMap.put("ticket_rates",
                    oaTrainTicketMap.get("ticket_rates").toString().replace("￥","").replace("元",""));

            sysAttachment.setFilePath(filePath);
            sysAttachment.setUuid(ticketUuid);
            sysAttachment.setFileName(trainTicketFile.getName());
            sysAttachmentService.insertSysAttachment(sysAttachment);

            OaTrainTicket oaTrainTicket = JSON.parseObject(JSON.toJSONString(oaTrainTicketMap), OaTrainTicket.class);
            oaTrainTicket.setTicketScanNum(oaTrainTicket.getTicketNum());
            /*if(checkTrainTicketExist(oaTrainTicket.getTicketNum())){
                oaTrainTicket.setRepeatFlag("1");
            }*/
            oaTrainTicket.setSysAttId(sysAttachment.getId());
            oaTrainTicket.setTicketUuid(ticketUuid);
            oaTrainTicket.setUuid(uuid);
            oaTrainTicket.setAccessWay("autoScan");
            oaTrainTicket.setDateTime(DateUtils.dateTime(DateUtils.YYYYMMDDHHMM,
                    oaTrainTicket.getDate()+" "+oaTrainTicket.getTime()));

            oaTrainTicket.setRepeatFlag("0");
            String checkResult = checkTrainTicketResult(oaTrainTicket);
            if(checkResult.indexOf("已报销")!=-1){
                return AjaxResult.error("该发票号码已报销;请确认！");
            }
            if(checkResult.indexOf("已上传")!=-1){
                oaTrainTicket.setAbnormalFlag("1");
            }

            oaTrainTicketService.insertOaTrainTicket(oaTrainTicket);
            return AjaxResult.success(oaTrainTicket);
        } else {
            return AjaxResult.error("无法读取火车票信息！");
        }
    }

    private boolean checkTrainTicketExist(String ticketNum){
        OaTrainTicket oaTrainTicket = new OaTrainTicket();
        oaTrainTicket.setTicketNum(ticketNum);
        oaTrainTicket.setRepeatFlag("0");
        List<OaTrainTicket> list = oaTrainTicketService.selectOaTrainTicketList(oaTrainTicket);
        return StringUtils.isNotEmpty(list);
    }

    /**
     * 检查增值税发票是否已存在
     */
    @PostMapping(value = "checkTrainTicketExist")
    public AjaxResult checkTrainTicket(@RequestBody OaTrainTicket oaTrainTicket)
    {
        return AjaxResult.success(checkTrainTicketResult(oaTrainTicket));
    }

    private String checkTrainTicketResult(OaTrainTicket oaTrainTicket){
        String result = "" ;
        if(StringUtils.isNotNull(oaTrainTicket.getTicketScanNum())&&
                !oaTrainTicket.getTicketScanNum().equals(oaTrainTicket.getTicketNum())){
            result+="该火车票号码非自动扫描发票号码;";
        }

        Boolean reimbursement = false ;
        Boolean upload = false ;

        //校验火车票中是否已上传、已报销
        OaTrainTicket oaTrainTicketParam = new OaTrainTicket();
        oaTrainTicketParam.setTicketNum(oaTrainTicket.getTicketNum());
        oaTrainTicketParam.setRepeatFlag("0");
        List<OaTrainTicket> oaTrainTicketList = oaTrainTicketService.selectOaTrainTicketList(oaTrainTicketParam);
        for(int i=0;i<oaTrainTicketList.size();i++){
            if(StringUtils.isNotNull(oaTrainTicket.getId())
                    && oaTrainTicket.getId() == oaTrainTicketList.get(i).getId()){
                continue;
            }
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

        //校验其他发票中是否已上传、已报销
        OaOtherInvoice oaOtherInvoiceParam = new OaOtherInvoice();
        oaOtherInvoiceParam.setInvoiceNumber(oaTrainTicket.getTicketNum());
        List<OaOtherInvoice> oaOtherInvoiceList = oaOtherInvoiceService.
                selectOaOtherInvoiceList(oaOtherInvoiceParam);
        for(int i=0;i<oaOtherInvoiceList.size();i++){
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
        if(upload){
            result+= "该火车票号码已上传;" ;
        }
        if(reimbursement){
            result+= "该火车票号码已报销;" ;
        }
        return result;

    }

}
