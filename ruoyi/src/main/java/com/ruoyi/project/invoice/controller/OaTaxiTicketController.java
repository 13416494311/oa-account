package com.ruoyi.project.invoice.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

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
import com.ruoyi.project.invoice.domain.OaTaxiTicket;
import com.ruoyi.project.invoice.service.IOaTaxiTicketService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 出租车发票Controller
 *
 * @author ruoyi
 * @date 2020-07-31
 */
@RestController
@RequestMapping("/invoice/taxiTicket")
public class OaTaxiTicketController extends BaseController
{
    @Autowired
    private IOaTaxiTicketService oaTaxiTicketService;
    @Autowired
    private ISysAttachmentService sysAttachmentService;
    @Autowired
    private IOaReimbursementApplyService oaReimbursementApplyService;
    @Autowired
    private IOaOtherInvoiceService oaOtherInvoiceService;

    /**
     * 查询出租车发票列表
     */
    @PreAuthorize("@ss.hasPermi('invoice:taxiTicket:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaTaxiTicket oaTaxiTicket)
    {
        startPage();
        List<OaTaxiTicket> list = oaTaxiTicketService.selectOaTaxiTicketList(oaTaxiTicket);
        return getDataTable(list);
    }

    /**
     * 导出出租车发票列表
     */
    @PreAuthorize("@ss.hasPermi('invoice:taxiTicket:export')")
    @Log(title = "出租车发票", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OaTaxiTicket oaTaxiTicket)
    {
        List<OaTaxiTicket> list = oaTaxiTicketService.selectOaTaxiTicketList(oaTaxiTicket);
        ExcelUtil<OaTaxiTicket> util = new ExcelUtil<OaTaxiTicket>(OaTaxiTicket.class);
        return util.exportExcel(list, "taxiTicket");
    }

    /**
     * 获取出租车发票详细信息
     */
    @PreAuthorize("@ss.hasPermi('invoice:taxiTicket:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(oaTaxiTicketService.selectOaTaxiTicketById(id));
    }

    /**
     * 新增出租车发票
     */
    @PreAuthorize("@ss.hasPermi('invoice:taxiTicket:add')")
    @Log(title = "出租车发票", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaTaxiTicket oaTaxiTicket)
    {
        return toAjax(oaTaxiTicketService.insertOaTaxiTicket(oaTaxiTicket));
    }

    /**
     * 修改出租车发票
     */
    @PreAuthorize("@ss.hasPermi('invoice:taxiTicket:edit')")
    @Log(title = "出租车发票", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaTaxiTicket oaTaxiTicket)
    {
        return toAjax(oaTaxiTicketService.updateOaTaxiTicket(oaTaxiTicket));
    }

    /**
     * 删除出租车发票
     */
    @PreAuthorize("@ss.hasPermi('invoice:taxiTicket:remove')")
    @Log(title = "出租车发票", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaTaxiTicketService.deleteOaTaxiTicketByIds(ids));
    }

    /**
     * 扫描方式-新增机票行程单
     */
    @Log(title = "新增机票行程单-扫描附件方式", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addByAtt")
    public AjaxResult addByAtt(@RequestParam("file") MultipartFile file,
                               @RequestParam("uuid") String uuid,
                               SysAttachment sysAttachment) throws IOException {

        String taxiTicketUuid = UUID.randomUUID().toString();

        File taxiTicketFile = FileUtil.multipartFileToFile(file);
        String fileName = taxiTicketFile.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if("pdf".equals(suffix.toLowerCase())){
            List<File> pngFileList = Pdf2PngUtils.pdf2png(taxiTicketFile,0,1);
            if(StringUtils.isNotEmpty(pngFileList)){
                taxiTicketFile=pngFileList.get(0);
            }
        }
        String filePath = FileUploadUtils.upload(RuoYiConfig.getProfile()+
                        "/"+ StringUtils.lineToHump(sysAttachment.getFileDictType())+"/"+sysAttachment.getFileDictValue(),
                FileUtil.fileToMultipartFile(taxiTicketFile));

        String result = BaiduApi.vatInvoice(taxiTicketFile,sysAttachment.getFileDictValue())
                .replace("¥","").replace("元","");
        Map<String,Object> resultMap = JSON.parseObject(result);
        Map<String,Object> oaTaxiTicketMap = (Map<String, Object>) resultMap.get("words_result");
        if(oaTaxiTicketMap!=null){

            sysAttachment.setFilePath(filePath);
            sysAttachment.setUuid(taxiTicketUuid);
            sysAttachment.setFileName(taxiTicketFile.getName());
            sysAttachmentService.insertSysAttachment(sysAttachment);

            OaTaxiTicket oaTaxiTicket = JSON.parseObject(JSON.toJSONString(oaTaxiTicketMap), OaTaxiTicket.class);
            oaTaxiTicket.setInvoiceScanNum(oaTaxiTicket.getInvoiceNum());
            /*if(checkTaxiTicketExist(oaTaxiTicket.getTicketNumber())){
                oaTaxiTicket.setRepeatFlag("1");
            }*/
            oaTaxiTicket.setSysAttId(sysAttachment.getId().toString());
            oaTaxiTicket.setInvoiceUuid(taxiTicketUuid);
            oaTaxiTicket.setUuid(uuid);
            oaTaxiTicket.setAccessWay("autoScan");

            String[] times = oaTaxiTicket.getTime().split("-");
            try{
                oaTaxiTicket.setDateTimeStart(DateUtils.dateTime(DateUtils.YYYY_MM_DDHHMM,
                        oaTaxiTicket.getDate()+" "+times[0]));
                oaTaxiTicket.setDateTimeEnd(DateUtils.dateTime(DateUtils.YYYY_MM_DDHHMM,
                        oaTaxiTicket.getDate()+" "+times[1]));
            }catch (Exception e){
                oaTaxiTicket.setRepeatFlag("0");
                String checkResult = checkTaxiTicketResult(oaTaxiTicket);
                if(checkResult.indexOf("已报销")!=-1){
                    return AjaxResult.error("该发票号码已报销;请确认！");
                }
                if(checkResult.indexOf("已上传")!=-1){
                    oaTaxiTicket.setAbnormalFlag("1");
                }
                oaTaxiTicket.setDateTimeStart(null);
                oaTaxiTicket.setDateTimeEnd(null);
                oaTaxiTicketService.insertOaTaxiTicket(oaTaxiTicket);
                return AjaxResult.success(oaTaxiTicket);
            }

            oaTaxiTicket.setRepeatFlag("0");
            String checkResult = checkTaxiTicketResult(oaTaxiTicket);
            if(checkResult.indexOf("已报销")!=-1){
                return AjaxResult.error("该发票号码已报销;请确认！");
            }
            if(checkResult.indexOf("已上传")!=-1){
                oaTaxiTicket.setAbnormalFlag("1");
            }
            if(checkResult.indexOf("连号")!=-1){
                oaTaxiTicket.setAbnormalFlag("1");
            }

            oaTaxiTicketService.insertOaTaxiTicket(oaTaxiTicket);
            return AjaxResult.success(oaTaxiTicket);
        } else {
            return AjaxResult.error("无法读取机票行程单信息！");
        }
    }

    private boolean checkTaxiTicketExist(String ticketNumber){
        OaTaxiTicket oaTaxiTicket = new OaTaxiTicket();
        oaTaxiTicket.setInvoiceNum(ticketNumber);
        oaTaxiTicket.setRepeatFlag("0");
        List<OaTaxiTicket> list = oaTaxiTicketService.selectOaTaxiTicketList(oaTaxiTicket);
        return StringUtils.isNotEmpty(list);
    }

    /**
     * 检查增值税发票是否已存在
     */
    @PostMapping(value = "checkTaxiTicketExist")
    public AjaxResult checkTaxiTicket(@RequestBody OaTaxiTicket oaTaxiTicket)
    {
        return AjaxResult.success(checkTaxiTicketResult(oaTaxiTicket));
    }
    private String checkTaxiTicketResult(OaTaxiTicket oaTaxiTicket){
        String result = "" ;
        if(StringUtils.isNotNull(oaTaxiTicket.getInvoiceScanNum())&&
                !oaTaxiTicket.getInvoiceScanNum().equals(oaTaxiTicket.getInvoiceNum())){
            result+="该电子客票号码非自动扫描发票号码;";
        }

        Boolean reimbursement = false ;
        Boolean upload = false ;

        //校验机票中是否已上传、已报销
        OaTaxiTicket oaTaxiTicketParam = new OaTaxiTicket();
        oaTaxiTicketParam.setInvoiceNum(oaTaxiTicket.getInvoiceNum());
        oaTaxiTicketParam.setRepeatFlag("0");
        List<OaTaxiTicket> oaTaxiTicketList = oaTaxiTicketService.selectOaTaxiTicketList(oaTaxiTicketParam);
        for(int i=0;i<oaTaxiTicketList.size();i++){
            if(StringUtils.isNotNull(oaTaxiTicket.getId())
                    && oaTaxiTicket.getId() == oaTaxiTicketList.get(i).getId()){
                continue;
            }
            OaReimbursementApply apply = oaReimbursementApplyService.
                    selectOaReimbursementApplyByUuid(oaTaxiTicketList.get(i).getUuid());
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
        oaOtherInvoiceParam.setInvoiceNumber(oaTaxiTicket.getInvoiceNum());
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
            result+= "该出租车发票号码已上传;" ;
        }
        if(reimbursement){
            result+= "该出租车发票已报销;" ;
        }

        Boolean serialNumber = false ;  //连号

        if(isInteger(oaTaxiTicket.getInvoiceNum())){
            Integer invoiceNum = Integer.parseInt(oaTaxiTicket.getInvoiceNum());
            OaTaxiTicket oaTaxiTicketParam1 = new OaTaxiTicket();
            oaTaxiTicketParam1.setUuid(oaTaxiTicket.getUuid());
            oaTaxiTicketParam1.setRepeatFlag("0");
            List<OaTaxiTicket> oaTaxiTicketList1 = oaTaxiTicketService.selectOaTaxiTicketList(oaTaxiTicketParam1);
            for(int i=0;i<oaTaxiTicketList1.size();i++){
                if(StringUtils.isNotNull(oaTaxiTicket.getId())
                        && oaTaxiTicket.getId() == oaTaxiTicketList1.get(i).getId()){
                    continue;
                }
                if(isInteger(oaTaxiTicketList1.get(i).getInvoiceNum())){
                    Integer invoiceNum1 = Integer.parseInt(oaTaxiTicketList1.get(i).getInvoiceNum());
                    if(invoiceNum1-invoiceNum==1||invoiceNum1-invoiceNum==-1){
                        serialNumber = true;
                    }
                }
            }
        }
        if(serialNumber){
            result+= "该出租车发票连号;" ;
        }

        return result;
    }

    private boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
