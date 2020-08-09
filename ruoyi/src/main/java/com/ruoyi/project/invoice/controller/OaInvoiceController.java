package com.ruoyi.project.invoice.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.*;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.project.invoice.domain.OaInvoiceCommodity;
import com.ruoyi.project.invoice.domain.OaOtherInvoice;
import com.ruoyi.project.invoice.service.IOaInvoiceCommodityService;
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
import com.ruoyi.project.invoice.domain.OaInvoice;
import com.ruoyi.project.invoice.service.IOaInvoiceService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 增值税发票Controller
 *
 * @author ruoyi
 * @date 2020-06-08
 */
@RestController
@RequestMapping("/invoice/invoice")
public class OaInvoiceController extends BaseController
{
    @Autowired
    private IOaInvoiceService oaInvoiceService;
    @Autowired
    private IOaInvoiceCommodityService oaInvoiceCommodityService;
    @Autowired
    private ISysAttachmentService sysAttachmentService;
    @Autowired
    private IOaReimbursementApplyService oaReimbursementApplyService;
    @Autowired
    private IOaOtherInvoiceService oaOtherInvoiceService;

    /**
     * 查询增值税发票列表
     */
    /*@PreAuthorize("@ss.hasPermi('invoice:invoice:list')")*/
    @GetMapping("/list")
    public TableDataInfo list(OaInvoice oaInvoice)
    {
        startPage();
        List<OaInvoice> list = oaInvoiceService.selectOaInvoiceList(oaInvoice);
        return getDataTable(list);
    }

    @GetMapping("/listNoPage")
    public AjaxResult listNoPage(OaInvoice oaInvoice)
    {
        List<OaInvoice> list = oaInvoiceService.selectOaInvoiceList(oaInvoice);
        return AjaxResult.success(list);
    }

    /**
     * 导出增值税发票列表
     */
    /*@PreAuthorize("@ss.hasPermi('invoice:invoice:export')")*/
    @Log(title = "增值税发票", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(OaInvoice oaInvoice)
    {
        List<OaInvoice> list = oaInvoiceService.selectOaInvoiceList(oaInvoice);
        ExcelUtil<OaInvoice> util = new ExcelUtil<OaInvoice>(OaInvoice.class);
        return util.exportExcel(list, "invoice");
    }

    /**
     * 获取增值税发票详细信息
     */
   /* @PreAuthorize("@ss.hasPermi('invoice:invoice:query')")*/
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(oaInvoiceService.selectOaInvoiceById(id));
    }

    /**
     * 新增增值税发票
     */
    /*@PreAuthorize("@ss.hasPermi('invoice:invoice:add')")*/
    @Log(title = "增值税发票", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaInvoice oaInvoice)
    {
        return toAjax(oaInvoiceService.insertOaInvoice(oaInvoice));
    }

    /**
     * 扫描方式-新增增值税发票
     */
    /*@PreAuthorize("@ss.hasPermi('invoice:invoice:add')")*/
    @Log(title = "新增值税发票-扫描附件方式", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addByAtt")
    public AjaxResult addByAtt(@RequestParam("file") MultipartFile file,
                               @RequestParam("uuid") String uuid,
                               SysAttachment sysAttachment) throws IOException {


        String invoiceUuid = UUID.randomUUID().toString();

        File invoiceFile = FileUtil.multipartFileToFile(file);
        String fileName = invoiceFile.getName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if("pdf".equals(suffix.toLowerCase())){
            List<File> pngFileList = Pdf2PngUtils.pdf2png(invoiceFile,0,1);
            if(StringUtils.isNotEmpty(pngFileList)){
                invoiceFile=pngFileList.get(0);
            }
        }
        String filePath = FileUploadUtils.upload(RuoYiConfig.getProfile()+
                        "/"+ StringUtils.lineToHump(sysAttachment.getFileDictType())+"/"+sysAttachment.getFileDictValue(),
                FileUtil.fileToMultipartFile(invoiceFile));


        String result = BaiduApi.vatInvoice(invoiceFile,sysAttachment.getFileDictValue()).replace("*","");
        Map<String,Object> resultMap = JSON.parseObject(result);
        Map<String,Object> oaInvoiceMap = (Map<String, Object>) resultMap.get("words_result");
        if(oaInvoiceMap!=null){
            sysAttachment.setFilePath(filePath);
            sysAttachment.setFileName(invoiceFile.getName());
            sysAttachmentService.insertSysAttachment(sysAttachment);

            OaInvoice oaInvoice  = JSON.parseObject(JSON.toJSONString(oaInvoiceMap), OaInvoice.class);
            oaInvoice.setInvoiceScanNum(oaInvoice.getInvoiceNum());
            /*if(checkInvoiceExist(oaInvoice.getInvoiceNum())){
                oaInvoice.setRepeatFlag("1");
            }*/
            oaInvoice.setSysAttId(sysAttachment.getId());
            oaInvoice.setInvoiceUuid(invoiceUuid);
            oaInvoice.setUuid(uuid);
            oaInvoice.setAccessWay("autoScan");

            oaInvoice.setRepeatFlag("0");
            String checkResult = checkInvoiceResult(oaInvoice);
            if(checkResult.indexOf("已报销")!=-1){
                return AjaxResult.error("该发票号码已报销;请确认！");
            }
            if(checkResult.indexOf("已上传")!=-1
                    ||!"51100000500002862F".equals(oaInvoice.getPurchaserRegisterNum())
            ||(StringUtils.isNotNull(oaInvoice.getPurchaserName())&&!"中国自动化学会".equals(oaInvoice.getPurchaserName()))){
                oaInvoice.setAbnormalFlag("1");
            }
            oaInvoiceService.insertOaInvoice(oaInvoice);
            oaInvoice.convertComondity();
            for(OaInvoiceCommodity oaInvoiceCommodity:oaInvoice.getOaInvoiceCommodity()){
                oaInvoiceCommodityService.insertOaInvoiceCommodity(oaInvoiceCommodity);
            }
            return AjaxResult.success(oaInvoice);
        }else{
            return AjaxResult.error("无法读取增值税发票信息！");
        }
    }

    private boolean checkInvoiceExist(String invoiceNum){
        OaInvoice oaInvoice=new OaInvoice();
        oaInvoice.setInvoiceNum(invoiceNum);
        oaInvoice.setRepeatFlag("0");
        List<OaInvoice> oaInvoiceList = oaInvoiceService.selectOaInvoiceList(oaInvoice);
        return StringUtils.isNotEmpty(oaInvoiceList);
    }

    /**
     * 检查增值税发票是否已存在
     */
    @PostMapping(value = "checkInvoiceExist")
    public AjaxResult checkInvoice(@RequestBody OaInvoice oaInvoice)
    {
        return AjaxResult.success(checkInvoiceResult(oaInvoice));
    }

    private String checkInvoiceResult(OaInvoice oaInvoice){
        String result = "" ;
        if(StringUtils.isNotNull(oaInvoice.getInvoiceScanNum())&&
                !oaInvoice.getInvoiceScanNum().equals(oaInvoice.getInvoiceNum())){
            result+="该发票号码非自动扫描发票号码;";
        }

        Boolean reimbursement = false ;
        Boolean upload = false ;

        //校验增值税发票中是否已上传、已报销
        OaInvoice oaInvoiceParam = new OaInvoice();
        oaInvoiceParam.setInvoiceNum(oaInvoice.getInvoiceNum());
        oaInvoiceParam.setRepeatFlag("0");
        List<OaInvoice> oaInvoiceList = oaInvoiceService.selectOaInvoiceList(oaInvoiceParam);
        for(int i=0;i<oaInvoiceList.size();i++){
            if(StringUtils.isNotNull(oaInvoice.getId()) && oaInvoice.getId() == oaInvoiceList.get(i).getId()){
                continue;
            }
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
        //校验其他发票中是否已上传、已报销
        OaOtherInvoice oaOtherInvoiceParam = new OaOtherInvoice();
        oaOtherInvoiceParam.setInvoiceNumber(oaInvoice.getInvoiceNum());
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
            result+= "该发票号码已上传;" ;
        }
        if(reimbursement){
            result+= "该发票号码已报销;" ;
        }
        return result;

    }

    /**
     * 修改增值税发票
     */
   /* @PreAuthorize("@ss.hasPermi('invoice:invoice:edit')")*/
    @Log(title = "增值税发票", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaInvoice oaInvoice)
    {
        return toAjax(oaInvoiceService.updateOaInvoice(oaInvoice));
    }

    /**
     * 删除增值税发票
     */
    /*@PreAuthorize("@ss.hasPermi('invoice:invoice:remove')")*/
    @Log(title = "增值税发票", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaInvoiceService.deleteOaInvoiceByIds(ids));
    }
}
