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
import com.ruoyi.project.invoice.domain.OaAirTicket;
import com.ruoyi.project.invoice.service.IOaAirTicketService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 机票行程单Controller
 *
 * @author ruoyi
 * @date 2020-06-21
 */
@RestController
@RequestMapping("/invoice/airTicket")
public class OaAirTicketController extends BaseController
{
    @Autowired
    private IOaAirTicketService oaAirTicketService;
    @Autowired
    private ISysAttachmentService sysAttachmentService;
    @Autowired
    private IOaReimbursementApplyService oaReimbursementApplyService;
    @Autowired
    private IOaOtherInvoiceService oaOtherInvoiceService;

    /**
     * 查询机票行程单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(OaAirTicket oaAirTicket)
    {
        startPage();
        List<OaAirTicket> list = oaAirTicketService.selectOaAirTicketList(oaAirTicket);
        return getDataTable(list);
    }

    /**
     * 导出机票行程单列表
     */
    @Log(title = "机票行程单", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OaAirTicket oaAirTicket)
    {
        List<OaAirTicket> list = oaAirTicketService.selectOaAirTicketList(oaAirTicket);
        ExcelUtil<OaAirTicket> util = new ExcelUtil<OaAirTicket>(OaAirTicket.class);
        return util.exportExcel(list, "airTicket");
    }

    /**
     * 获取机票行程单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(oaAirTicketService.selectOaAirTicketById(id));
    }

    /**
     * 新增机票行程单
     */
    @Log(title = "机票行程单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaAirTicket oaAirTicket)
    {
        return toAjax(oaAirTicketService.insertOaAirTicket(oaAirTicket));
    }

    /**
     * 修改机票行程单
     */
    @Log(title = "机票行程单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaAirTicket oaAirTicket)
    {
        return toAjax(oaAirTicketService.updateOaAirTicket(oaAirTicket));
    }

    /**
     * 删除机票行程单
     */
    @Log(title = "机票行程单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaAirTicketService.deleteOaAirTicketByIds(ids));
    }

    /**
     * 扫描方式-新增机票行程单
     */
    @Log(title = "新增机票行程单-扫描附件方式", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addByAtt")
    public AjaxResult addByAtt(@RequestParam("file") MultipartFile file,
                               @RequestParam("uuid") String uuid,
                               SysAttachment sysAttachment) throws IOException {

        String airTicketUuid = UUID.randomUUID().toString();

        File airTicketFile = FileUtil.multipartFileToFile(file);
        String fileName = airTicketFile.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if("pdf".equals(suffix.toLowerCase())){
            List<File> pngFileList = Pdf2PngUtils.pdf2png(airTicketFile,0,1);
            if(StringUtils.isNotEmpty(pngFileList)){
                airTicketFile=pngFileList.get(0);
            }
        }
        String filePath = FileUploadUtils.upload(RuoYiConfig.getProfile()+
                        "/"+ StringUtils.lineToHump(sysAttachment.getFileDictType())+"/"+sysAttachment.getFileDictValue(),
                FileUtil.fileToMultipartFile(airTicketFile));

        String result = BaiduApi.vatInvoice(airTicketFile,sysAttachment.getFileDictValue())
                .replace("EXEMPT","");
        Map<String,Object> resultMap = JSON.parseObject(result);
        Map<String,Object> oaAirTicketMap = (Map<String, Object>) resultMap.get("words_result");
        if(oaAirTicketMap!=null){

            sysAttachment.setFilePath(filePath);
            sysAttachment.setUuid(airTicketUuid);
            sysAttachment.setFileName(airTicketFile.getName());
            sysAttachmentService.insertSysAttachment(sysAttachment);

            OaAirTicket oaAirTicket = JSON.parseObject(JSON.toJSONString(oaAirTicketMap), OaAirTicket.class);
            oaAirTicket.setTicketScanNumber(oaAirTicket.getTicketNumber());
            /*if(checkAirTicketExist(oaAirTicket.getTicketNumber())){
                oaAirTicket.setRepeatFlag("1");
            }*/
            oaAirTicket.setSysAttId(sysAttachment.getId().toString());
            oaAirTicket.setTicketUuid(airTicketUuid);
            oaAirTicket.setUuid(uuid);
            oaAirTicket.setAccessWay("autoScan");
            try{
                oaAirTicket.setDateTime(DateUtils.dateTime(DateUtils.YYYY_MM_DDHHMM,
                        oaAirTicket.getDate()+" "+oaAirTicket.getTime()));
            }catch (Exception e){
                oaAirTicket.setDateTime(null);
                oaAirTicketService.insertOaAirTicket(oaAirTicket);
                return AjaxResult.success(oaAirTicket);
            }


            oaAirTicket.setRepeatFlag("0");
            String checkResult = checkAirTicketResult(oaAirTicket);
            if(checkResult.indexOf("已报销")!=-1){
                return AjaxResult.error("该发票号码已报销;请确认！");
            }
            if(checkResult.indexOf("已上传")!=-1){
                oaAirTicket.setAbnormalFlag("1");
            }

            oaAirTicketService.insertOaAirTicket(oaAirTicket);
            return AjaxResult.success(oaAirTicket);
        } else {
            return AjaxResult.error("无法读取机票行程单信息！");
        }
    }

    private boolean checkAirTicketExist(String ticketNumber){
        OaAirTicket oaAirTicket = new OaAirTicket();
        oaAirTicket.setTicketNumber(ticketNumber);
        oaAirTicket.setRepeatFlag("0");
        List<OaAirTicket> list = oaAirTicketService.selectOaAirTicketList(oaAirTicket);
        return StringUtils.isNotEmpty(list);
    }

    /**
     * 检查增值税发票是否已存在
     */
    @PostMapping(value = "checkAirTicketExist")
    public AjaxResult checkAirTicket(@RequestBody OaAirTicket oaAirTicket)
    {
        return AjaxResult.success(checkAirTicketResult(oaAirTicket));
    }
    private String checkAirTicketResult(OaAirTicket oaAirTicket){
        String result = "" ;
        if(StringUtils.isNotNull(oaAirTicket.getTicketScanNumber())&&
                !oaAirTicket.getTicketScanNumber().equals(oaAirTicket.getTicketNumber())){
            result+="该电子客票号码非自动扫描发票号码;";
        }

        Boolean reimbursement = false ;
        Boolean upload = false ;

        //校验机票中是否已上传、已报销
        OaAirTicket oaAirTicketParam = new OaAirTicket();
        oaAirTicketParam.setTicketNumber(oaAirTicket.getTicketNumber());
        oaAirTicketParam.setRepeatFlag("0");
        List<OaAirTicket> oaAirTicketList = oaAirTicketService.selectOaAirTicketList(oaAirTicketParam);
        for(int i=0;i<oaAirTicketList.size();i++){
            if(StringUtils.isNotNull(oaAirTicket.getId())
                    && oaAirTicket.getId() == oaAirTicketList.get(i).getId()){
                continue;
            }
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
        //校验其他发票中是否已上传、已报销
        OaOtherInvoice oaOtherInvoiceParam = new OaOtherInvoice();
        oaOtherInvoiceParam.setInvoiceNumber(oaAirTicket.getTicketNumber());
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
            result+= "该电子客票号码已上传;" ;
        }
        if(reimbursement){
            result+= "该电子客票号码已报销;" ;
        }
        return result;

    }
}
